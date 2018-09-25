package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText name, password, password2, email;
    String Name, Password, Password2, Email;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.register_name);
        password = findViewById(R.id.register_password);
        password2 = findViewById(R.id.register_password2);
        email = findViewById(R.id.register_email);
    }

    public void register_register(View v) {
        Name = name.getText().toString();
        Password = password.getText().toString();
        Password2 = password2.getText().toString();
        Email = email.getText().toString();

        BackGround b = new BackGround();
        if (Password == null || Password.trim().isEmpty()) {
            Toast.makeText(ctx, "Password is empty", Toast.LENGTH_LONG).show();
        } else if (Password.length() < 8) {
            Toast.makeText(ctx, "Password must be at least 8 characters long", Toast.LENGTH_LONG).show();
        } else if (!Password.equals(Password2)) {
            Toast.makeText(ctx, "Passwords do not match", Toast.LENGTH_LONG).show();
        } else if (Email == null || Email.length() < 1 || Email.equals("")) {
            Toast.makeText(ctx, "No Email included", Toast.LENGTH_LONG).show();
        }  else if (Name == null || Name.length() < 3 || Name.equals("")) {
            Toast.makeText(ctx, "No Username included", Toast.LENGTH_LONG).show();
        } else {
            try {
                Password = Encryption.encrypt(Password);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            b.execute(Name, Password, Email);
        }

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String email = params[2];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/register.php");
                String urlParams = "name=" + name + "&password=" + password + "&email=" + email;
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
            if (s.equals("")) {
                s = "Data saved successfully.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
