package pl.pollub.shoppinglist.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SubscriptionHandling;

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
    private ParseLiveQueryClient liveQueryClient;
    private int messageCount;

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

            messageCount++;
            conversation.addUniqueMessage(message);
            conversation.saveInBackground().onSuccessTask(task -> {
                this.runOnUiThread(() -> {
                    // the list must be visible before adding first item
                    if (recyclerViewAdapter.getList().isEmpty()) {
                        toggleMessagesVisibility(View.VISIBLE);
                    }

                    binding.messageInput.setText("");
                    recyclerViewAdapter.addItem(message);
                    binding.messageList.smoothScrollToPosition(messageCount - 1);
                });

                return Task.forResult(null);
            }).continueWith(task -> {
                if (task.isFaulted() || task.isCancelled()) {
                    this.runOnUiThread(() -> {
                        messageCount--;
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

        subscribeToConversationEvents(findAndBindMessages());
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

    private Task<?> findAndBindMessages() {
        Task<UserData> dataTask = currentUser.getUserData().fetchInBackground();

        return dataTask.onSuccessTask(task -> {
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

                return Task.forResult(null);
            } else if (task.getResult().size() == 1) {
                conversation = task.getResult().get(0);

                return Task.forResult(conversation.getMessages());
            } else {
                throw new IllegalStateException("There can only be one conversation between two users!");
            }
        }).continueWithTask(task -> {
            this.runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));

            if (task.isFaulted() || task.isCancelled()) {
                this.runOnUiThread(() -> Toast.makeText(
                        MessagesActivity.this,
                        R.string.couldnt_load_messages,
                        Toast.LENGTH_LONG
                ).show());

                Log.w("MessagesActivity", task.getError());

                return Task.forError(task.getError());
            }

            final List<Message> messages = task.getResult();

            if (messages != null && !messages.isEmpty()) {
                Collections.sort(messages, (o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                this.runOnUiThread(() -> {
                    recyclerViewAdapter.setList(messages);
                    messageCount = recyclerViewAdapter.getItemCount();
                    toggleMessagesVisibility(View.VISIBLE);
                });
            } else {
                this.runOnUiThread(() -> toggleMessagesVisibility(View.GONE));
            }

            return Task.forResult(null);
        });
    }

    private void subscribeToConversationEvents(Task<?> initMessagesTask) {
        initMessagesTask.continueWith(task -> {
            if (task.isFaulted() || task.isCancelled()) {
                return null;
            }

            liveQueryClient = ParseLiveQueryClient.Factory.getClient();

            // WORKAROUND: LiveQuery don't work with ParseRelation's queries
            // also doesn't work with more complex queries
            // (only whereEqualTo condition is proven to work)
            ParseQuery<Conversation> conversationQuery = ParseQuery.getQuery(Conversation.class)
                    //.selectKeys(Collections.singletonList(Conversation.KEY_MESSAGES))
                    .include(Conversation.KEY_MESSAGES)
                    .include(Conversation.KEY_PARTICIPANTS)
                    .whereEqualTo(Conversation.KEY_PARTICIPANTS, currentUser);
                    //.whereContainedIn(Conversation.KEY_PARTICIPANTS, Arrays.asList(currentUser, friend));

            SubscriptionHandling<Conversation> subscriptionHandling = liveQueryClient
                    .subscribe(conversationQuery);
            subscriptionHandling.handleEvents((query, event, conversation) -> {

                // WORKAROUND: as it is required to listen to changes for all conversations currentUser
                // participates in (see above why), condition must check participants of returned conversation
                if (conversation.getMessages() == null
                        || conversation.getMessages().isEmpty()
                        || conversation.getMessages().size() == messageCount
                        || !conversation.getParticipants().containsAll(
                        Arrays.asList(currentUser, friend))) {
                    return;
                }

                final List<Message> messages = conversation.getMessages();
                // fetch is needed as newly added pointers are not fetched initially
                ParseObject.fetchAllIfNeededInBackground(messages, (updatedMsgs, exception) -> {
                    if (exception == null) {
                        Collections.sort(updatedMsgs, (o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                        this.runOnUiThread(() -> {
                            recyclerViewAdapter.setList(updatedMsgs);
                            messageCount = recyclerViewAdapter.getItemCount();
                            toggleMessagesVisibility(View.VISIBLE);
                            binding.messageList.smoothScrollToPosition(messageCount - 1);
                        });
                    } else {
                        Log.w("MessagesActivity", exception);
                    }
                });
            });

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
