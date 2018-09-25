package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class UserActivity extends AppCompatActivity {

    String name, email, Err, profile, rating, coins;
    EditText ETPIN;
    TextView nameTV, emailTV, err, TVprofile1, TVprofile2, TVprofile3, TVprofile4, CreateNotice, WebLink;
    Button Button1, Button2, Button3, Button4, submit;
    FloatingActionButton FAB1;
    Context context=this;
    LinkedList<String> profileNames = new LinkedList<>();
    LinkedList<String> ratings = new LinkedList<>();
    LinkedList<String> coinValues = new LinkedList<>();
    ImageButton Settings, Settings2, Settings3, Settings4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        MainActivity.session.checkLogin();

        nameTV = findViewById(R.id.home_name);
        emailTV = findViewById(R.id.home_email);
        WebLink = findViewById(R.id.home_name2);
        WebLink.setMovementMethod(LinkMovementMethod.getInstance());
        WebLink.setText(Html.fromHtml("<a href=http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/index.php>Statistics</a>"));
        err = findViewById(R.id.err);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        Err = getIntent().getStringExtra("err");


        nameTV.setText("Welcome " + name);
        emailTV.setText("Select from the profiles below to play.");
        err.setText(Err);

        Settings4 = findViewById(R.id.settings4);
        Settings4.setVisibility(View.INVISIBLE);
        Settings3 = findViewById(R.id.settings3);
        Settings3.setVisibility(View.INVISIBLE);
        Settings2 = findViewById(R.id.settings2);
        Settings2.setVisibility(View.INVISIBLE);
        Settings = findViewById(R.id.settings);
        Settings.setVisibility(View.INVISIBLE);

        CreateNotice = findViewById(R.id.createNotice);
        CreateNotice.setVisibility(View.INVISIBLE);
        ETPIN = findViewById(R.id.ETPIN);
        ETPIN.setVisibility(View.INVISIBLE);
        submit = findViewById(R.id.submit);
        submit.setVisibility(View.INVISIBLE);

        Button1 = findViewById(R.id.button1);
        Button1.setVisibility(View.INVISIBLE);
        Button2 = findViewById(R.id.button2);
        Button2.setVisibility(View.INVISIBLE);
        Button3 = findViewById(R.id.button3);
        Button3.setVisibility(View.INVISIBLE);
        Button4 = findViewById(R.id.button4);
        Button4.setVisibility(View.INVISIBLE);

        BackGround b = new BackGround();

        b.execute("profileList",name);


    }

    /**
     * If the user clicks to add a new profile, this will open the new profile page
     * and close the current page (Refreshed upon return to show the new profile in the list)
     */
    public void addProfile() {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
        finish();
    }

    class BackGround extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {
            String task = params[0];
            String data = "";
            int tmp;
            String urlString = "";
            String urlParams = "";

            if (task.equals("profileList")) {

                String name = params[1];
                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/profileList.php";
                urlParams = "username=" + name;

            } else if (task.equals("PINcheck")) {

                String name = params[1];
                String profileName = params[2];
                String pin = params[3];
                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/profileLogin.php";
                urlParams = "username=" + name + "&profileName=" + profileName + "&pin=" + pin;
                urlParams = urlParams.replace("\n", "");
                System.out.println("urlParams: " + urlParams);

            }

            if (urlString.length() > 0) {
                try {
                    URL url = new URL(urlString);

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
            } else {
                return "No task found";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            try {
                JSONObject root = new JSONObject(s);

                String message = root.optString("message");
                if (message != null && message.length() > 0) {

                    if (message.equals("PIN accepted!")) {

                        Intent i = new Intent(getApplicationContext(), PlayActivity.class);

                        i.putExtra("profile", profile);
                        i.putExtra("rating", rating);
                        i.putExtra("coins", coins);

                        startActivity(i);
                        finish();
                    } else if (message.equals("PIN incorrect!")) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    } else if (message.equals("No profiles found!")) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }

                } else {
                    JSONArray profiles = root.getJSONArray("7057_profiles");

                    if (profiles != null) {

                        for (int i = 0; i < profiles.length(); i++) {
                            JSONObject profile = profiles.getJSONObject(i);
                            String profileName = profile.getString("ProfileName");
                            String rating = profile.getString("Rating");
                            String coins = profile.getString("StarCoins");
                            profileNames.add(profileName);
                            ratings.add(rating);
                            coinValues.add(coins);

                            System.out.println("GOT PROFILE: " + profileName);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                // err = "Exception: " + e.getMessage();
            }

            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.activity_user, profileNames);
            //list.setAdapter(adapter);

            // Assigning TextViews with the profile names, or invisible if empty
            TVprofile1 = findViewById(R.id.TVprofile1);
            if (profileNames.size() > 0) {
                TVprofile1.setText(profileNames.get(0));
            } else {
                TVprofile1.setVisibility(View.INVISIBLE);
            }
            TVprofile2 = findViewById(R.id.TVprofile2);
            if (profileNames.size() > 1) {
                TVprofile2.setText(profileNames.get(1));
            } else {
                TVprofile2.setVisibility(View.INVISIBLE);
            }
            TVprofile3 = findViewById(R.id.TVprofile3);
            if (profileNames.size() > 2) {
                TVprofile3.setText(profileNames.get(2));
            } else {
                TVprofile3.setVisibility(View.INVISIBLE);
            }
            TVprofile4 = findViewById(R.id.TVprofile4);
            if (profileNames.size() > 3) {
                TVprofile4.setText(profileNames.get(3));
            } else {
                TVprofile4.setVisibility(View.INVISIBLE);
            }

            // Setting buttons to invisible if no profile exists

            if (profileNames.size()==0) {
                CreateNotice.setVisibility(View.VISIBLE);
            } else if (profileNames.size()==1) {
                Button1.setVisibility(View.VISIBLE);
                Settings.setVisibility(View.VISIBLE);
            } else if (profileNames.size()==2) {
                Button1.setVisibility(View.VISIBLE);
                Settings.setVisibility(View.VISIBLE);
                Button2.setVisibility(View.VISIBLE);
                Settings2.setVisibility(View.VISIBLE);
            } else if (profileNames.size()==3) {
                Button1.setVisibility(View.VISIBLE);
                Settings.setVisibility(View.VISIBLE);
                Button2.setVisibility(View.VISIBLE);
                Settings2.setVisibility(View.VISIBLE);
                Button3.setVisibility(View.VISIBLE);
                Settings3.setVisibility(View.VISIBLE);
            } else if (profileNames.size()==4) {
                Button1.setVisibility(View.VISIBLE);
                Settings.setVisibility(View.VISIBLE);
                Button2.setVisibility(View.VISIBLE);
                Settings2.setVisibility(View.VISIBLE);
                Button3.setVisibility(View.VISIBLE);
                Settings3.setVisibility(View.VISIBLE);
                Button4.setVisibility(View.VISIBLE);
                Settings4.setVisibility(View.VISIBLE);
            }

            Button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    MainActivity.session.selectProfile(profileNames.get(0));
                    MainActivity.session.setRating(ratings.get(0));
                    MainActivity.session.setCoins(coinValues.get(0));
                    profile = profileNames.get(0);
                    rating = ratings.get(0);
                    coins = coinValues.get(0);
                    validateProfileLogin(name, profile);

                }
            });

            Button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    MainActivity.session.selectProfile(profileNames.get(1));
                    MainActivity.session.setRating(ratings.get(1));
                    MainActivity.session.setCoins(coinValues.get(1));
                    profile = profileNames.get(1);
                    rating = ratings.get(1);
                    coins = coinValues.get(1);
                    validateProfileLogin(name, profile);

                }
            });

            Button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    MainActivity.session.selectProfile(profileNames.get(2));
                    MainActivity.session.setRating(ratings.get(2));
                    MainActivity.session.setCoins(coinValues.get(2));
                    profile = profileNames.get(2);
                    rating = ratings.get(2);
                    coins = coinValues.get(2);
                    validateProfileLogin(name, profile);
                }
            });

            Button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    MainActivity.session.selectProfile(profileNames.get(3));
                    MainActivity.session.setRating(ratings.get(3));
                    MainActivity.session.setCoins(coinValues.get(3));
                    profile = profileNames.get(3);
                    rating = ratings.get(3);
                    coins = coinValues.get(3);
                    validateProfileLogin(name, profile);
                }
            });

            Settings.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    profile = profileNames.get(0);
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    i.putExtra("profile", profile);
                    startActivity(i);
                }
            });

            Settings2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    profile = profileNames.get(1);
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    i.putExtra("profile", profile);
                    startActivity(i);
                }
            });

            Settings3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    profile = profileNames.get(2);
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    i.putExtra("profile", profile);
                    startActivity(i);
                }
            });

            Settings4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    profile = profileNames.get(3);
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    i.putExtra("profile", profile);
                    startActivity(i);
                }
            });

            // Floating Button to add a new profile
            FAB1 = findViewById(R.id.FAB1);
            {
                FAB1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        addProfile();
                    }
                });
            }

        }

        /**
         * This will ask the User for a PIN & call the DB to check the PIN is correct.
         * @param profile
         */
        public void validateProfileLogin(final String name, final String profile) {
            ETPIN.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            submit.setEnabled(false);

            ETPIN.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    enableSubmit();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    enableSubmit();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    enableSubmit();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String PIN = ETPIN.getText().toString();

                    try {
                        PIN = Encryption.encrypt(PIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    BackGround b = new BackGround();
                    b.execute("PINcheck", name, profile, PIN);
                }
            });



        }

        /**
         * Method to disable the submit button unless 4 digits are entered into the EditText field.
         */
        public void enableSubmit() {
            boolean isReady = ETPIN.getText().toString().length() == 4;
            submit.setEnabled(isReady);
        }

    }

}
