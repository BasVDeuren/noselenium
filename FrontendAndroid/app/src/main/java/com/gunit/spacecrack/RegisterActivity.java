package com.gunit.spacecrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RegisterActivity extends Activity {

    private Button register;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private EditText email;

    private Context context;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        context = this;
        username = (EditText) findViewById(R.id.edt_register_username);
        password = (EditText) findViewById(R.id.edt_register_password);
        confirmPassword = (EditText) findViewById(R.id.edt_register_password_confirm);
        email = (EditText) findViewById(R.id.edt_register_email);
        register = (Button) findViewById(R.id.btn_register_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPasswords(password.getText().toString(), confirmPassword.getText().toString())) {
                    new RegisterTask(username.getText().toString(), password.getText().toString(), email.getText().toString()).execute("url");
                }
            }
        });
    }

    public boolean checkPasswords(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public class RegisterTask extends AsyncTask<String, Void, Boolean> {

        private String username = "";
        private String password = "";
        private String email = "";
        private final CountDownLatch signal = new CountDownLatch(1);

        public RegisterTask (String username, String password, String email)
        {
            super();
            this.username = username;
            this.password = password;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            register.setEnabled(false);
        }

        @Override
        protected Boolean  doInBackground (String...url)
        {
            boolean good = false;

//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url[0]);
//
//            try {
//                // Add your data
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
//                nameValuePairs.add(new BasicNameValuePair("username", username));
//                nameValuePairs.add(new BasicNameValuePair("password", password));
//                nameValuePairs.add(new BasicNameValuePair("email", email));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                // Execute HTTP Post Request
//                HttpResponse response = httpclient.execute(httppost);
//
//
//            } catch (ClientProtocolException e) {
//                good = false;
//            } catch (IOException e) {
//                good = false;
//            }
            if (!username.equals("") && !password.equals("") && !email.equals("")) {
                good = true;
                Log.i(TAG, "Registration succeeded");
            } else {
                good = false;
                Log.i(TAG, "Registration failed");
            }
            return good;
        }

        @Override
        protected void onPostExecute (Boolean result)
        {
            Toast.makeText(RegisterActivity.this, result ? getResources().getString(R.string.register_succes) : getResources().getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
            if (result) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
            register.setEnabled(true);
        }
    }

}
