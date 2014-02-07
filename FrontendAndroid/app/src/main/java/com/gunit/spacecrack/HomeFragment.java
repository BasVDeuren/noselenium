package com.gunit.spacecrack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;

/**
 * Created by Dimi on 7/02/14.
 */
public class HomeFragment extends Fragment {
    private ProfilePictureView profilePictureView;
    private TextView name;
    private SpaceCrackApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        profilePictureView = (ProfilePictureView) view.findViewById(R.id.ppv_home_profilepicture);
        name = (TextView) view.findViewById(R.id.txt_home_welcome);

        application = (SpaceCrackApplication) getActivity().getApplication();
        if (application.getGraphUser() != null) {
            updateAccount();
        }

        return view;
    }

    private void updateAccount() {
        profilePictureView.setProfileId(application.getGraphUser().getId());
        name.setText(application.getGraphUser().getFirstName() + " " + application.getGraphUser().getLastName());
    }
}
