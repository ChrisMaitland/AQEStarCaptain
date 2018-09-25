package chrismaitland.aqestarcaptain;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TrophyActivity extends AppCompatActivity {

    String profileName, userName;
    ImageView Trophy, NoTrophy;
    ListView listView;
    ArrayList<String> achievementGained = new ArrayList<String>();
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);

        MainActivity.session.checkLogin();

        listView = (ListView)findViewById(R.id.dynamiclist);

        getAchievements();

        backButton = (ImageButton)findViewById(R.id.back); {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        //Trophy = (ImageView)findViewById(R.id.trophy);
        //NoTrophy = (ImageView)findViewById(R.id.trophyblack);

    }

    public void getAchievements() {
        HashMap<String, String> user = MainActivity.session.getUserDetails();
        profileName = user.get(SessionManager.KEY_PROFILE);
        userName = user.get(SessionManager.KEY_NAME);
        TrophyActivity.BackGround b = new TrophyActivity.BackGround();
        b.execute("getAchievements", userName, profileName);
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String task = params[0];
            String data = "";
            int tmp;
            String urlString = "";
            String urlParams = "";

            //if (task.equals("getAchievements")) {

                String username = params[1];
                String profile = params[2];

                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/trophyList.php";
                urlParams = "username=" + username + "&profile=" + profile;

            /*} else if (task.equals("saveRound")) {

                String userName = params[1];
                String profileName = params[2];
                String oldRating = params[3];
                String newRating = params[4];
                String pointsAvailable = params[5];
                String pointsAchieved = params[6];
                String roundID = params[7];
                String star = params[8];

                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/storeRoundAndRating.php";
                urlParams = "userName=" + userName + "&profileName=" + profileName + "&oldRating=" + oldRating + "&newRating=" + newRating + "&pointsAvailable=" + pointsAvailable + "&pointsAchieved=" + pointsAchieved + "&roundID=" + roundID + "&star=" + star;

            }
            */
            if (urlString.length() > 0) {
                try {
                    URL url = new URL(urlString);
                    //replace + with ASCII value as spaces get converted to + when passed to the url and then automatically converted back to a space at the other end, so need to avoid actual + values being treated as a space
                    //urlParams = urlParams.replace("+", "%2B");

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
            System.out.print("s: " + s);
            try {
                JSONObject root = new JSONObject(s);
                JSONArray achievementList = root.getJSONArray("7057_achievements_won");

                String message = root.optString("message");
                if (message != null && message.length() > 0) {

                    if (message.equals("No achievements found!")) {
                        System.out.println("No achievements yet.");
                    }

                } else if (achievementList != null) {

                    for (int i = 0; i < achievementList.length(); i++) {
                        JSONObject achievement = achievementList.getJSONObject(i);
                        String achievementsWon = achievement.getString("AchievementName");
                        String achievementDesc = achievement.getString("AchievementDesc");
                        achievementGained.add(achievementsWon +"\n"+ achievementDesc);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TrophyActivity.this, android.R.layout.simple_expandable_list_item_1, achievementGained);
                        listView.setAdapter(arrayAdapter);
                        System.out.println("GOT ACHIEVEMENT: " + achievementsWon);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // err = "Exception: " + e.getMessage();
            }
        }
    }
}
