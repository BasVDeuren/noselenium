package com.gunit.spacecrack;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class HomeActivity extends FragmentActivity {

    private HomeFragment homeFragment;
    private UiLifecycleHelper fbUiLifecycleHelper;
    private GraphUser user;

    private static final String TAG = HomeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Instantiate the fbUiLifecycleHelper and call onCreate() on it
        fbUiLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state,
                             Exception exception) {
                // Add code here to accommodate session changes
                Log.d(TAG, "Session state changed: " + state);


            }
        });
        fbUiLifecycleHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            homeFragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, homeFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }


    public UiLifecycleHelper getFbUiLifecycleHelper() {
        return fbUiLifecycleHelper;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }
    }

}
