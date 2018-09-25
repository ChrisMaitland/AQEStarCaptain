package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity {

    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String NAME=null, EMAIL=null;
    Button loginButton;
    CheckBox remember;
    TextView Desc2;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private final String DefaultUsernameValue = "";
    private String userNameValue;
    private final String DefaultPasswordValue = "";
    private String passwordValue;

    static SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.main_name);
        password = findViewById(R.id.main_password);

        loginButton = findViewById(R.id.main_login);
        remember = findViewById(R.id.Remember);
        Desc2 = findViewById(R.id.desc2);

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (name.getText().toString().length() == 0 || password.getText().toString().length() < 8) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (name.getText().toString().length() == 0 || password.getText().toString().length() < 8) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * If 'remember' checkbox is checked, login credentials will be stored.
         */
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        loadPreferences();

    }

    // Methods to remember the users login

    @Override
    public void onPause() {
        super.onPause();
        //savePreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadPreferences();
    }

    /**
     * Method to store the user preferences
     */
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        userNameValue = String.valueOf(name.getText());
        passwordValue = String.valueOf(password.getText());
        if (remember.isChecked()) {
            editor.putString(PREF_UNAME, userNameValue);
            editor.putString(PREF_PASSWORD, passwordValue);
        } else {
            editor.putString(PREF_UNAME, DefaultUsernameValue);
            editor.putString(PREF_PASSWORD, DefaultPasswordValue);
        }

        editor.commit();
    }

    /**
     * Method to load the users preferences
     */
    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        userNameValue = settings.getString(PREF_UNAME, DefaultUsernameValue);
        passwordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        name.setText(userNameValue);
        password.setText(passwordValue);
        remember.setChecked(true);
    }

    /**
     * If Register button clicked, user will be directed to the register screen
     * @param v
     */
    public void main_register(View v){
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void login(View v){
        savePreferences();
        Name = name.getText().toString();
        Password = password.getText().toString();
        BackGround b = new BackGround();

        if (Name == null || Name.trim().isEmpty()) {
            Toast.makeText(ctx, "Name is empty", Toast.LENGTH_LONG).show();
        } else if (Password == null || Password.trim().isEmpty()) {
            Toast.makeText(ctx, "Password is empty", Toast.LENGTH_LONG).show();
        } else {
            try {
                Password = Encryption.encrypt(Password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            b.execute(Name, Password);
        }
    }

    /**
     * BackGround passes parameters to login.php to check the login credentials
     */
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/login.php");
                String urlParams = "name="+name+"&password="+password;
                urlParams = urlParams.replace("\n", "");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();

                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        // returned values are either assigned to the session(Intent) or a toast is displayed
        @Override
        protected void onPostExecute(String s) {
            String err=null;
            String message="Please enter a correct username & password (Case sensitive)";
            if (null == s || s.isEmpty()) {
                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject user_data = root.getJSONObject("7057_users");
                    if (user_data != null) {
                        NAME = user_data.getString("Username");
                        EMAIL = user_data.getString("Email");

                        Intent intent = new Intent(ctx, UserActivity.class);
                        intent.putExtra("name", NAME);
                        intent.putExtra("email", EMAIL);
                        intent.putExtra("err", err);

                        session = new SessionManager(getApplicationContext());

                        // creating a session for the user with their details
                        session.createLoginSession(NAME, EMAIL);

                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }

            }

        }
    }
}
