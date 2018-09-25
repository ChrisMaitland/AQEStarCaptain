package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.jar.Attributes;

public class ProfileActivity extends AppCompatActivity {

    EditText profileName, PINeditText;
    Button submit;
    Context ctx=this;
    String ProfileName, UserName, name, PIN;
    TextView reminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MainActivity.session.checkLogin();

        HashMap<String, String> user = MainActivity.session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);

        profileName = findViewById(R.id.ETprofileName);
        PINeditText = findViewById(R.id.EtPIN);
        reminder = findViewById(R.id.reminder);

        submit = findViewById(R.id.submit); {
            submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        profile_register(v);
                    }
                });
        }
    }

    /**
     * Method to create a new profile within an account.
     * @param v
     */
    public void profile_register(View v) {
        ProfileName = profileName.getText().toString();
        PIN = PINeditText.getText().toString();

        BackGround b = new BackGround();
        if (ProfileName == null || ProfileName.trim().isEmpty()) {
            Toast.makeText(ctx, "Enter a Profile Name", Toast.LENGTH_LONG).show();
        } else if (ProfileName.length() < 3) {
            Toast.makeText(ctx, "Name must be at least 3 characters long", Toast.LENGTH_LONG).show();
        } else if (PIN == null || PIN.trim().isEmpty() || PIN.length() != 4) {
            Toast.makeText(ctx, "PIN must be 4 digits", Toast.LENGTH_LONG).show();
        } else {

            // encrypt the users PIN before sending to DB.
            try {
                PIN = Encryption.encrypt(PIN);
            } catch (Exception e) {
                e.printStackTrace();
            }

            b.execute(ProfileName, name, PIN);
            Intent i = new Intent(getApplicationContext(), UserActivity.class);
            HashMap<String, String> user = MainActivity.session.getUserDetails();
            String email = user.get(SessionManager.KEY_EMAIL);
            i.putExtra("name", name);
            i.putExtra("email", email);
            startActivity(i);
            finish();
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String profilename = params[0];
            String username = params[1];
            String pin = params[2];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/profile.php");
                String urlParams = "&name=" + username + "&profileName=" + profilename + "&pin=" + pin;
                urlParams = urlParams.replace("\n", "");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String message = "";
            if (s.equals("")) {
                message = "New profile created!";
            } else {

                try {
                    JSONObject root = new JSONObject(s);

                    message = root.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                    message = "Exception: " + e.getMessage();
                }
            }

            Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();

        }
    }
}
