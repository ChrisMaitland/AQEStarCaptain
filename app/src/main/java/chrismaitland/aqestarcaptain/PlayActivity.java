package chrismaitland.aqestarcaptain;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class PlayActivity extends AppCompatActivity {

    String profileName, coins;
    TextView Welcome, CoinsTotal;
    Button PlayButton, Rewards;
    ImageButton LogOut;
    MediaPlayer Rocket;
    ImageView Trophy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        MainActivity.session.checkLogin();

        Welcome = findViewById(R.id.Welcome);
        CoinsTotal = findViewById(R.id.coinsTotal);
        Bundle extras = getIntent().getExtras();
        profileName = extras.getString("profile");
        coins = extras.getString("coins");
        Welcome.setText("Let's Play, " + profileName + "!");
        CoinsTotal.setText(coins);

        Trophy = (ImageView)findViewById(R.id.trophy);
        {
            Trophy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), TrophyActivity.class);
                    startActivity(i);
                }
            });
        }

        Rocket = MediaPlayer.create(PlayActivity.this, R.raw.rocket);

        PlayButton = findViewById(R.id.PlayButton); {
            PlayButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Rocket.start();
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(i);
                }
            });
        }

        Rewards = findViewById(R.id.rewardButton); {
            Rewards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), RewardsActivity.class);
                    Rocket.start();
                    startActivity(i);
                }
            });
        }

        LogOut = findViewById(R.id.logout); {
            LogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.session.logoutUser();
                    finish();
                }
            });
        }
    }

    public void getUsersSpaceCoinValue(){
        HashMap<String, String> coinValue = MainActivity.session.getUserDetails();
        coins = coinValue.get(SessionManager.KEY_COINS);
    }

}