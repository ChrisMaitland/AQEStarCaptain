package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ImageButton DeleteButton, Back;
    EditText enterPW;
    Button confirmButton;
    Context context=this;
    String password, Profile;
    TextView Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MainActivity.session.checkLogin();

        Bundle extras = getIntent().getExtras();
        Profile = extras.getString("profile");

        enterPW = findViewById(R.id.enterPW);
        confirmButton = findViewById(R.id.confirmButton);
        enterPW.setVisibility(View.INVISIBLE);
        confirmButton.setVisibility(View.INVISIBLE);

        Delete = findViewById(R.id.delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPW.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);

                confirmButton = findViewById(R.id.confirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        password = enterPW.getText().toString();
                        if (password == null || password.equals("")) {
                            Toast.makeText(context, "Please enter Password", Toast.LENGTH_LONG).show();
                        } else if (password.length() < 8) {
                            Toast.makeText(context, "Password is over 8 characters long.", Toast.LENGTH_LONG).show();
                        } else {
                            deleteProfile(Profile);
                        }
                    }
                });
            }
        });

        DeleteButton = findViewById(R.id.deleteButton);
        DeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                enterPW.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);

                confirmButton = findViewById(R.id.confirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        password = enterPW.getText().toString();
                        if (password == null || password.equals("")) {
                            Toast.makeText(context, "Please enter Password", Toast.LENGTH_LONG).show();
                        } else if (password.length() < 8) {
                            Toast.makeText(context, "Password is over 8 characters long.", Toast.LENGTH_LONG).show();
                        } else {
                            deleteProfile(Profile);
                        }
                    }
                });
            }
        });

        Back = findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Method to check if PIN & details are correct, and then delete users profile.
     */
    public void deleteProfile(String Profile) {
        HashMap<String, String> user = MainActivity.session.getUserDetails();
        String userName = user.get(SessionManager.KEY_NAME);
        BackGround b = new BackGround();
        try {
            password = Encryption.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        b.execute(Profile, userName, password);
        Intent i = new Intent(getApplicationContext(), UserActivity.class);
        i.putExtra("name", userName);
        i.putExtra("email", user.get(SessionManager.KEY_EMAIL));
        //user.remove(SessionManager.KEY_PROFILE);
        startActivity(i);
        finish();
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int tmp;
            String urlString = "";
            String urlParams = "";

                String profileName = params[0];
                String userName = params[1];
                String password = params[2];

                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/deleteProfile.php";
                urlParams = "profileName=" + profileName + "&userName=" + userName + "&password=" + password;
                urlParams = urlParams.replace("\n", "");

            if (urlString.length() > 0) {
                try {
                    URL url = new URL(urlString);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();

                    os.write(urlParams.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    InputStream is = httpURLConnection.getInputStream();

                    while ((tmp = is.read()) != -1) {
                        data += (char) tmp;
                    }

                    is.close();
                    httpURLConnection.disconnect();
                    System.out.print("data: " + data);
                    return data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }
            } else {
                return "No task found";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            String message="";
            try {
                JSONObject root = new JSONObject(s);
                message = root.getString("message");
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
        }
    }

}
