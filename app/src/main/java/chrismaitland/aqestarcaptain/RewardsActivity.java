package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class RewardsActivity extends AppCompatActivity {

    ImageView Blue, Green, Orange, Pink, Purple, Red, Yellow, Sparkle, Rainbow;
    TextView BlueTV, GreenTV, OrangeTV, PinkTV, PurpleTV, RedTV, YellowTV, SparkleTV, RainbowTV, Collect;
    String name, profile;
    ImageButton Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        MainActivity.session.checkLogin();

        HashMap<String, String> user = MainActivity.session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        profile = user.get(SessionManager.KEY_PROFILE);

        Back = findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Collect = findViewById(R.id.collect);
        Blue = findViewById(R.id.blue);
        Green = findViewById(R.id.green);
        Orange = findViewById(R.id.orange);
        Pink = findViewById(R.id.pink);
        Purple = findViewById(R.id.purple);
        Red = findViewById(R.id.red);
        Yellow = findViewById(R.id.yellow);
        Sparkle = findViewById(R.id.sparkle);
        Rainbow = findViewById(R.id.rainbow);

        BlueTV = findViewById(R.id.blueCount);
        GreenTV = findViewById(R.id.greenCount);
        OrangeTV = findViewById(R.id.orangeCount);
        PinkTV = findViewById(R.id.pinkCount);
        PurpleTV = findViewById(R.id.purpleCount);
        RedTV = findViewById(R.id.redCount);
        YellowTV = findViewById(R.id.yellowCount);
        SparkleTV = findViewById(R.id.sparkleCount);
        RainbowTV = findViewById(R.id.rainbowCount);

        Blue.setImageResource(R.drawable.white);
        Green.setImageResource(R.drawable.white);
        Orange.setImageResource(R.drawable.white);
        Pink.setImageResource(R.drawable.white);
        Purple.setImageResource(R.drawable.white);
        Red.setImageResource(R.drawable.white);
        Yellow.setImageResource(R.drawable.white);
        Sparkle.setImageResource(R.drawable.white);
        Rainbow.setImageResource(R.drawable.white);

        BlueTV.setText("0");
        GreenTV.setText("0");
        OrangeTV.setText("0");
        PinkTV.setText("0");
        PurpleTV.setText("0");
        RedTV.setText("0");
        YellowTV.setText("0");
        SparkleTV.setText("0");
        RainbowTV.setText("0");

        BackGround b = new BackGround();
        b.execute(name, profile);

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String profile = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/starRewards.php");
                String urlParams = "&userName=" + userName + "&profile=" + profile;
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
            String err = null;
            try {

                JSONObject root = new JSONObject(s);

                String message = root.optString("message");

                JSONArray starCounts = root.getJSONArray("7057_round_tracker");

                for (int j = 0; j < starCounts.length(); j++) {
                    JSONObject starCount = starCounts.getJSONObject(j);
                    String starGained = starCount.getString("StarGained");
                    String total = starCount.getString("Total");

                    switch (starGained) {
                        case "Blue":
                            Blue.setImageResource(R.drawable.blue);
                            BlueTV.setText(total);
                            break;
                        case "Green":
                            Green.setImageResource(R.drawable.green);
                            GreenTV.setText(total);
                            break;
                        case "Orange":
                            Orange.setImageResource(R.drawable.orange);
                            OrangeTV.setText(total);
                            break;
                        case "Pink":
                            Pink.setImageResource(R.drawable.pink);
                            PinkTV.setText(total);
                            break;
                        case "Purple":
                            Purple.setImageResource(R.drawable.purple);
                            PurpleTV.setText(total);
                            break;
                        case "Red":
                            Red.setImageResource(R.drawable.red);
                            RedTV.setText(total);
                            break;
                        case "Yellow":
                            Yellow.setImageResource(R.drawable.yellow);
                            YellowTV.setText(total);
                            break;
                        case "Sparkle":
                            Sparkle.setImageResource(R.drawable.sparkle);
                            SparkleTV.setText(total);
                            break;
                        case "Rainbow":
                            Rainbow.setImageResource(R.drawable.rainbow);
                            RainbowTV.setText(total);
                            break;
                        default:
                            break;
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
