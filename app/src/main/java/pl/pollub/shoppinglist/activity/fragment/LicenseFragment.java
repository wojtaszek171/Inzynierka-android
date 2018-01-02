package pl.pollub.shoppinglist.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.AboutActivity;
import pl.pollub.shoppinglist.databinding.FragmentLicenseBinding;

/**
 * @author Adrian
 * @since 2017-12-25
 */
public class LicenseFragment extends Fragment {
    private FragmentLicenseBinding binding;
    private String fileName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_license, container, false);

        if (!fileName.trim().isEmpty()) {
            try {
                binding.licenseView.setText(getLicense(fileName));
            } catch (IOException exception) {
                binding.licenseView.setText("Nie udało się załadować licencji");
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle upstreamArguments = getArguments();
        if (upstreamArguments != null) {
            fileName = upstreamArguments.getString(AboutActivity.LICENSE_FILE_NAME);
        }
    }

    public StringBuilder getLicense(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getActivity().getAssets().open(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        }

        return sb;
    }
}
