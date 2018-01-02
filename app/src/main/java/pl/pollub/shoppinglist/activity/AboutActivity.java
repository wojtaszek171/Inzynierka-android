package pl.pollub.shoppinglist.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.graphics.drawable.AnimationUtilsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.LicenseFragment;
import pl.pollub.shoppinglist.databinding.ActivityAboutBinding;
import pl.pollub.shoppinglist.util.MiscUtils;

import static android.view.animation.AnimationUtils.loadAnimation;

/**
 * @author Adrian
 * @since 2017-12-26
 */
public class AboutActivity extends AppCompatActivity {
    public static final String LICENSE_FILE_NAME = "LICENSE_FILE_NAME";
    private ActivityAboutBinding binding;
    private Fragment licenseFragment = new LicenseFragment();
    private Bundle fragmentBundle = new Bundle();
    private View aboutPageView;
    private SparseArray<Animation> animations = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        setSupportActionBar(binding.toolbar.toolbar);
        setTitle("O programie");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aboutPageView = getAboutPage();
        binding.aboutLayout.addView(aboutPageView);
        animations.put(R.anim.enter_from_left, loadAnimation(this, R.anim.enter_from_left));
        animations.put(R.anim.enter_from_right, loadAnimation(this, R.anim.enter_from_right));
        animations.put(R.anim.exit_to_left, loadAnimation(this, R.anim.exit_to_left));
        animations.put(R.anim.exit_to_right, loadAnimation(this, R.anim.exit_to_right));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!binding.licenseContainer.isShown() && binding.aboutLayout.isShown()) {
            return;
        }

        binding.licenseContainer.setVisibility(View.GONE);
        binding.licenseContainer.startAnimation(loadAnimation(this, R.anim.exit_to_right));
        binding.aboutLayout.setVisibility(View.VISIBLE);
        binding.aboutLayout.startAnimation(animations.get(R.anim.enter_from_left));
        setTitle("O programie");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!binding.licenseContainer.isShown() && binding.aboutLayout.isShown()) {
                    finish();
                } else {
                    binding.licenseContainer.setVisibility(View.GONE);
                    binding.licenseContainer.startAnimation(loadAnimation(this, R.anim.exit_to_right));
                    binding.aboutLayout.setVisibility(View.VISIBLE);
                    binding.aboutLayout.startAnimation(animations.get(R.anim.enter_from_left));
                    setTitle("O programie");
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View getAboutPage() {
        return new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ls_banner_small)
                .setDescription("Wersja 1.0")
                .addGroup("Wykorzystywane oprogramowanie otwartoźródłowe:")
                .addItem(new Element("Android Support Libraries", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("Android Architecture Libraries", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("Project Lombok", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_lombok.txt")))
                .addItem(new Element("ThreeTen Android Backport", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("Parse SDK for Android", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_parse_sdk.txt")))
                .addItem(new Element("Parse LiveQuery Client for Android", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_parse_livequery.txt")))
                .addItem(new Element("Android Saripaar", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("UsefulViews", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("fab-speed-dial", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("android-circlebutton", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("Noto Emoji", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_apache2.txt")))
                .addItem(new Element("Android About Page", null)
                        .setOnClickListener(view -> onLicenseDetailsClick(view, "license_about_page.txt")))
                .create();
    }

    private void onLicenseDetailsClick(View view, String fileName) {
        fragmentBundle.putString(LICENSE_FILE_NAME, fileName);
        licenseFragment.setArguments(fragmentBundle);
        MiscUtils.attachFragment(licenseFragment, getSupportFragmentManager(),
                binding.licenseContainer.getId(), true);

        binding.aboutLayout.setVisibility(View.GONE);
        binding.aboutLayout.startAnimation(animations.get(R.anim.exit_to_left));
        binding.licenseContainer.setVisibility(View.VISIBLE);
        binding.licenseContainer.startAnimation(animations.get(R.anim.enter_from_right));
        setTitle("Treść licencji");
    }
}
