package pl.pollub.shoppinglist.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bolts.Task;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.MessageViewAdapter;
import pl.pollub.shoppinglist.databinding.ActivityMessagesBinding;
import pl.pollub.shoppinglist.model.Conversation;
import pl.pollub.shoppinglist.model.Message;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

/**
 * @author Adrian
 * @since 2017-12-01
 */
public class MessagesActivity extends AppCompatActivity {

    private ActivityMessagesBinding binding;
    private MessageViewAdapter recyclerViewAdapter;
    private User friend;
    private final User currentUser = User.getCurrentUser();
    private Conversation conversation;
    private final List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_messages);

        friend = getIntent().getParcelableExtra(User.CLASS_NAME);

        if (friend == null || !friend.isDataAvailable()) {
            throw new IllegalArgumentException("User is null or not fetched");
        }

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.conversation_with) + " " + friend.getUsername());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.messageList.setVisibility(View.GONE);
        binding.emptyLabel.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        recyclerViewAdapter = new MessageViewAdapter(this);
        binding.messageList.setAdapter(recyclerViewAdapter);

        binding.sendButton.setOnClickListener(view -> {
            String input = binding.messageInput.getText().toString();
            if (input.trim().isEmpty()) {
                return;
            }

            final Message message = new Message();
            message.setAuthor(currentUser).setContent(input);

            conversation.addUniqueMessage(message);
            conversation.saveInBackground().onSuccessTask(task -> {
                this.runOnUiThread(() -> {
                    // the list must be visible before adding first item
                    if (recyclerViewAdapter.getList().isEmpty()) {
                        toggleMessagesVisibility(View.VISIBLE);
                    }

                    binding.messageInput.setText("");
                    recyclerViewAdapter.addItem(message);
                });

                return Task.forResult(null);
            }).continueWith(task -> {
                if (task.isFaulted() || task.isCancelled()) {
                    this.runOnUiThread(() -> {
                        Toast.makeText(
                                MessagesActivity.this,
                                "Nie udało się wysłać wiadomości!",
                                Toast.LENGTH_SHORT
                        ).show();

                        Log.w("MessagesActivity", task.getError());
                    });
                }

                return null;
            });
        });

        findAndBindMessages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findAndBindMessages() {
        Task<UserData> dataTask = currentUser.getUserData().fetchInBackground();

        dataTask.onSuccessTask(task -> {
            final ParseRelation<Conversation> relation = task.getResult().getConversationsRelation();

            return relation.getQuery()
                    .include(Conversation.KEY_MESSAGES)
                    .whereEqualTo(Conversation.KEY_PARTICIPANTS, friend)
                    .findInBackground();
        }).onSuccessTask(task -> {
            if (task.getResult() == null || task.getResult().isEmpty()) {
                conversation = new Conversation();
                conversation.addUniqueParticipants(Arrays.asList(currentUser, friend));
                conversation.save();
                currentUser.getUserData().getConversationsRelation().add(conversation);
                friend.getUserData().getConversationsRelation().add(conversation);
                ParseObject.saveAll(Arrays.asList(currentUser.getUserData(), friend.getUserData()));

                return Task.forResult(Collections.<Message>emptyList());
            } else if (task.getResult().size() == 1) {
                conversation = task.getResult().get(0);

                return Task.forResult(conversation.getMessages());
            } else {
                throw new IllegalStateException("There can only be one conversation between two users!");
            }
        }).onSuccessTask(task -> {
            messages.addAll(task.getResult());
            Collections.sort(messages, (o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
            return Task.forResult(messages);
        }).continueWith(task -> {
            this.runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));

            if (task.isFaulted() || task.isCancelled()) {
                this.runOnUiThread(() -> Toast.makeText(
                        MessagesActivity.this,
                        R.string.couldnt_load_messages,
                        Toast.LENGTH_LONG
                ).show());

                Log.w("MessagesActivity", task.getError());

                return null;
            }

            if (task.getResult().size() > 0) {
                this.runOnUiThread(() -> {
                    recyclerViewAdapter.setList(task.getResult());
                    recyclerViewAdapter.notifyDataSetChanged();
                    toggleMessagesVisibility(View.VISIBLE);
                });
            } else {
                this.runOnUiThread(() -> toggleMessagesVisibility(View.GONE));
            }

            return null;
        });
    }

    private void toggleMessagesVisibility(int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                binding.messageList.setVisibility(View.VISIBLE);
                binding.emptyLabel.setVisibility(View.GONE);
                break;
            case View.GONE:
                binding.messageList.setVisibility(View.GONE);
                binding.emptyLabel.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("Not implemented for " + visibility);
        }
    }
}
