package com.gunit.spacecrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import java.util.concurrent.CountDownLatch;

public class LoginActivity extends FragmentActivity {

    private FacebookLoginFragment facebookFragment;
    private Context context;
    private SpaceCrackApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_login);

        context = this;
        application = (SpaceCrackApplication) getApplication();

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            facebookFragment = new FacebookLoginFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, facebookFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            facebookFragment = (FacebookLoginFragment) getFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        //start Facebook Login
//        Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//            // callback when session changes state
//            @Override
//            public void call(Session session, SessionState state, Exception exception) {
//                if (session.isOpened()) {
//                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//                        // callback after Graph API response with user object
//                        @Override
//                        public void onCompleted(GraphUser user, Response response) {
//                            if (user != null) {
//                                application.setGraphUser(user);
//                                Intent intent = new Intent(context, HomeActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                }
//            }
//        });

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//    }


}
