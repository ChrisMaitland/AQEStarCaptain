package chrismaitland.aqestarcaptain;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // The number of questions that will be generated
    public static final int NUMBER_OF_QUESTIONS = 10;

    // The value of each question difficulty
    public static final int EASYPOINTS = 1;
    public static final int MEDIUMPOINTS = 3;
    public static final int HARDPOINTS = 5;

    // Values required to increase or decrease rating
    public static final int RATING_INCREASE_THRESHOLD = 70;
    public static final int RATING_DECREASE_THRESHOLD = 50;

    LinkedList<Question> correctList = new LinkedList<Question>();
    LinkedList<Question> incorrectList = new LinkedList<Question>();
    LinkedList<Question> questionList = new LinkedList<Question>();
    TextView textView, responseTextView, YouEarned, Congrats;
    EditText editText;
    Button submitButton, continueButton, finishedButton;
    String roundID, profileName, userName, profile, rating;
    int pointsAvailable, pointsAchieved = 0;
    ImageView Coins, Star;
    Stars starReward;
    boolean reward = false;
    MediaPlayer coinSound, starSound;

    static String[] questionTypes = {"division", "addition", "subtraction", "multiplication", "divisionExtra", "multiplicationExtra", "triangle", "rightAngleTriangle", "quadrilateral", "english", "time"};
    int userRating = 1;
    String coins;

    static Difficulty[] questionDifficulties = new Difficulty[NUMBER_OF_QUESTIONS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MainActivity.session.checkLogin();

        roundID = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        HashMap<String, String> user = MainActivity.session.getUserDetails();
        profileName = user.get(SessionManager.KEY_PROFILE);
        userRating = Integer.parseInt(user.get(SessionManager.KEY_RATING));
        coins = user.get(SessionManager.KEY_COINS);

        textView = findViewById(R.id.question);
        responseTextView = findViewById(R.id.response);
        editText = findViewById(R.id.editText);
        Star = findViewById(R.id.Star);
        Star.setVisibility(View.INVISIBLE);
        YouEarned = findViewById(R.id.youEarned);
        YouEarned.setVisibility(View.INVISIBLE);
        Coins = findViewById(R.id.coins);
        Coins.setVisibility(View.INVISIBLE);
        Congrats = findViewById(R.id.congrats);
        Congrats.setVisibility(View.INVISIBLE);

        coinSound = MediaPlayer.create(GameActivity.this, R.raw.coinsound);
        starSound = MediaPlayer.create(GameActivity.this, R.raw.star);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitButton.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.INVISIBLE);
                finishedButton.setVisibility(View.INVISIBLE);
                continueButton.setVisibility(View.VISIBLE);
                processAnswer();
            }
        });
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                responseTextView.setVisibility(View.INVISIBLE);
                continueButton.setVisibility(View.INVISIBLE);
                finishedButton.setVisibility(View.INVISIBLE);
                submitButton.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                editText.getText().clear();
                displayQuestion();
            }
        });

        finishedButton = findViewById(R.id.finished);
        finishedButton.setVisibility(View.INVISIBLE);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlayActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                HashMap<String, String> user = MainActivity.session.getUserDetails();
                profile = user.get(SessionManager.KEY_PROFILE);
                rating = user.get(SessionManager.KEY_RATING);
                coins =user.get(SessionManager.KEY_COINS);

                i.putExtra("profile", profile);
                i.putExtra("rating", rating);
                i.putExtra("coins", coins);
                startActivity(i);
                finish();
            }
        });
        responseTextView.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
        generateQuestions(NUMBER_OF_QUESTIONS);
        displayQuestion();
    }

    /**
     * Core method that generates each question.
     * @param numberOfQuestions
     */
    public void generateQuestions(int numberOfQuestions) {

        initialiseDifficulties();

        // setting how many questions will be generated
        for (int i = 0; i < numberOfQuestions; i++) {

            // randomly selecting a question type
            int rand = new Random().nextInt(questionTypes.length);

            String questionType = questionTypes[rand];

            Question question = null;

            // generating the selected question type
            switch (questionType) {
                case "division":
                    question = new ArithmeticDivision(questionDifficulties[i]);
                    //editText.setInputType("textVisiblePassword");
                    break;
                case "addition":
                    question = new ArithmeticAddition(questionDifficulties[i]);
                    break;
                case "subtraction":
                    question = new ArithmeticSubtraction(questionDifficulties[i]);
                    break;
                case "multiplication":
                    question = new ArithmeticMultiplication(questionDifficulties[i]);
                    break;
                case "divisionExtra":
                    question = new ArithmeticExtraDivision(questionDifficulties[i]);
                    break;
                case "multiplicationExtra":
                    question = new ArithmeticExtraMultiplication(questionDifficulties[i]);
                    break;
                case "triangle":
                    question = new AnglesTriangle(questionDifficulties[i]);
                    break;
                case "rightAngleTriangle":
                    question = new AnglesRightAngledTriangle(questionDifficulties[i]);
                    break;
                case "quadrilateral":
                    question = new AnglesQuadrilateral(questionDifficulties[i]);
                    break;
                case "english":
                    question = new English(questionDifficulties[i]);
                    break;
                case "time" :
                    question = new TimeManipulation((questionDifficulties[i]));
                    break;
                default:
                    break;
            }

            question.generateAnswer();
            questionList.add(question);

        }
    }

    /**
     * This method sets the rules of how many Easy, Medium & Hard questions
     * the user will face, depending on their userRating.
     */
    private void initialiseDifficulties() {

        int numEasy = 2;
        int numMedium = 7;
        int numHard = 1;

        switch (userRating) {
            //1: 7 easy, 3 medium
            case 1:
                numEasy = 7;
                numMedium = 3;
                numHard = 0;
                break;
            //2: 6 easy, 4 medium
            case 2:
                numEasy = 6;
                numMedium = 4;
                numHard = 0;
                break;
            //3: 5 easy, 5 medium
            case 3:
                numEasy = 5;
                numMedium = 5;
                numHard = 0;
                break;
            //4: 3 easy, 7 medium
            case 4:
                numEasy = 3;
                numMedium = 7;
                numHard = 0;
                break;
            //5: 2 easy, 7 medium, 1 hard
            case 5:
                numEasy = 2;
                numMedium = 7;
                numHard = 1;
                break;
            //6: 1 easy, 7 medium, 2 hard
            case 6:
                numEasy = 1;
                numMedium = 7;
                numHard = 2;
                break;
            //7: 7 medium, 3 hard
            case 7:
                numEasy = 0;
                numMedium = 7;
                numHard = 3;
                break;
            //8: 5 medium, 5 hard
            case 8:
                numEasy = 0;
                numMedium = 5;
                numHard = 5;
                break;
            //9: 4 medium, 6 hard
            case 9:
                numEasy = 0;
                numMedium = 4;
                numHard = 6;
                break;
            //10: 3 medium, 7 hard
            case 10:
                numEasy = 0;
                numMedium = 3;
                numHard = 7;
                break;
            default:
                break;
        }

        // If I need to change the number of questions for another feature
        if (NUMBER_OF_QUESTIONS != 10) {
            numEasy = numEasy / 10 * NUMBER_OF_QUESTIONS;
            numMedium = numMedium / 10 * NUMBER_OF_QUESTIONS;
            numHard = numHard / 10 * NUMBER_OF_QUESTIONS;
        }

        int position = 0;

        for (int i = 0; i < numEasy; i++) {
            questionDifficulties[position] = Difficulty.Easy;
            position++;
        }
        for (int i = 0; i < numMedium; i++) {
            questionDifficulties[position] = Difficulty.Medium;
            position++;
        }
        for (int i = 0; i < numHard; i++) {
            questionDifficulties[position] = Difficulty.Hard;
            position++;
        }

    }

    public void displayQuestion() {

        int currentQuestionIndex = incorrectList.size() + correctList.size();
        if (currentQuestionIndex >= questionList.size()) {
            displaySummary();
        } else {
            Question question = questionList.get(currentQuestionIndex);

            if (question instanceof Arithmetic) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            }

            question.printQuestion(textView);
        }

    }

    /**
     * Method adds answers to relevant LinkedLists &
     * calculates value of each question.
     */
    public void processAnswer() {

        int currentQuestionIndex = incorrectList.size() + correctList.size();
        Question question = questionList.get(currentQuestionIndex);

        String input = editText.getText().toString();
        String correct = "0";

        int questionPoints;

        switch (question.getDifficulty()) {
            case Easy: questionPoints = EASYPOINTS;
                break;
            case Medium: questionPoints = MEDIUMPOINTS;
                break;
            case Hard: questionPoints = HARDPOINTS;
                break;
            default: questionPoints = EASYPOINTS;
                break;
        }

        pointsAvailable += questionPoints;

        if (question.checkAnswer(input)) {
            Toast.makeText(getBaseContext(), "Correct", Toast.LENGTH_SHORT).show();
            correctList.add(question);
            correct = "1";
            pointsAchieved += questionPoints;
        } else {
            Toast.makeText(getBaseContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            incorrectList.add(question);
        }

        textView.setText(question.getQuestionInFull());

        saveQuestion(question, question.getClass().getSimpleName(), question.getDifficulty().toString(), input, correct);
    }

    /**
     * Method produces a summary of the users Results.
     */
    public void displaySummary() {

        editText.setVisibility(View.INVISIBLE);
        submitButton.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        coinSound.start();
        YouEarned.setText(String.format("You earned: \n %d \n Space Coins!", pointsAchieved));

        // initialise animations
        final Animation FadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation FadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        Coins.startAnimation(FadeIn);
        Coins.setVisibility(View.VISIBLE);
        YouEarned.startAnimation(FadeIn);
        YouEarned.setVisibility(View.VISIBLE);

        YouEarned.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                YouEarned.startAnimation(FadeOut);
                YouEarned.setVisibility(View.INVISIBLE);
                Coins.startAnimation(FadeOut);
                Coins.setVisibility(View.INVISIBLE);

                double score = calculateScorePercentage(pointsAchieved, pointsAvailable);

                int oldRating = userRating;
                int newRating = calculateRating(score);

                if (score >= RATING_DECREASE_THRESHOLD) {
                    StarSelector starSelector = new StarSelector();
                    starReward = starSelector.getRandomStar();
                    reward = true;
                } else {
                    reward = false;
                }

                if (newRating != oldRating) {
                    MainActivity.session.setRating(Integer.toString(newRating));
                    userRating = newRating;
                }

                Congrats.setAnimation(FadeIn);
                Congrats.setVisibility(View.VISIBLE);

                if (reward && starReward == Stars.Blue) {
                    starSound.start();
                    Star.setImageResource(R.drawable.blue);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Green) {
                    starSound.start();
                    Star.setImageResource(R.drawable.green);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Orange) {
                    starSound.start();
                    Star.setImageResource(R.drawable.orange);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Pink) {
                    starSound.start();
                    Star.setImageResource(R.drawable.pink);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Purple) {
                    starSound.start();
                    Star.setImageResource(R.drawable.purple);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Rainbow) {
                    starSound.start();
                    Star.setImageResource(R.drawable.rainbow);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Red) {
                    starSound.start();
                    Star.setImageResource(R.drawable.red);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Sparkle) {
                    starSound.start();
                    Star.setImageResource(R.drawable.sparkle);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else if (reward && starReward == Stars.Yellow) {
                    starSound.start();
                    Star.setImageResource(R.drawable.yellow);
                    Star.setAnimation(FadeIn);
                    Star.animate().rotation(360).start();
                    Star.setVisibility(View.VISIBLE);
                    Star.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Congrats.setAnimation(FadeOut);
                            Congrats.setVisibility(View.INVISIBLE);
                            Star.setAnimation(FadeOut);
                            Star.setVisibility(View.INVISIBLE);
                            resultsSummary();
                        }
                    });

                } else {
                    Congrats.setAnimation(FadeOut);
                    Congrats.setVisibility(View.INVISIBLE);
                    resultsSummary();
                }

                HashMap<String, String> user = MainActivity.session.getUserDetails();
                userName = user.get(SessionManager.KEY_NAME);

                int newCoins = Integer.parseInt(coins) + pointsAchieved;
                MainActivity.session.setCoins(Integer.toString(newCoins));

                String starRewardString = "";

                if (null != starReward) {
                    starRewardString = starReward.toString();
                }

                BackGround b = new BackGround();
                b.execute("saveRound", userName, profileName, Integer.toString(oldRating), Integer.toString(newRating), Integer.toString(pointsAvailable), Integer.toString(pointsAchieved), roundID, starRewardString);
            }
        });

    }

    private void resultsSummary(){
        Animation FadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        textView.setText(String.format("Well done! You got %d questions correct!", correctList.size()));
        textView.startAnimation(FadeIn);
        textView.setVisibility(View.VISIBLE);
        if (incorrectList.size() > 0) {
            responseTextView.setText("These questions need a little more work:");
            responseTextView.startAnimation(FadeIn);
            responseTextView.setVisibility(View.VISIBLE);
            for (Question question : incorrectList) {
                responseTextView.append("\n" + question.getQuestionInFull());
            }
        }
        finishedButton.setVisibility(View.VISIBLE);
    }

    /**
     * Method takes the calculated score and calculates if
     * the userRating with increase, decrease or remain as is.
     * @param score
     * @return
     */
    private int calculateRating(double score) {
        int newRating = userRating;

        if (score >= RATING_INCREASE_THRESHOLD && userRating < 10) {
            newRating = userRating + 1;
        } else if (score < RATING_DECREASE_THRESHOLD && userRating > 1) {
            newRating = userRating - 1;
        }

        return newRating;
    }

    /**
     * Method takes the users points from the round and with the total available points,
     * returns a % total.
     * @param pointsAchieved
     * @param pointsAvailable
     * @return
     */
    public double calculateScorePercentage(double pointsAchieved, double pointsAvailable) {
        double score = (pointsAchieved / pointsAvailable) * 100;
        return score;
    }

    /**
     * Method to save the question to the DB
     * @param question
     * @param questionType
     * @param difficulty
     * @param answerGiven
     * @param correct
     */
    public void saveQuestion(Question question, String questionType, String difficulty, String answerGiven, String correct) {
        // Get profile
        HashMap<String, String> user = MainActivity.session.getUserDetails();
        profileName = user.get(SessionManager.KEY_PROFILE);
        userName = user.get(SessionManager.KEY_NAME);
        String answer = question.getAnswer();
        BackGround b = new BackGround();
        b.execute("saveQuestion", question.getQuestion(), questionType, difficulty, answer, answerGiven, profileName, userName, roundID, correct);
    }


    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String task = params[0];
            String data = "";
            int tmp;
            String urlString = "";
            String urlParams = "";

            if (task.equals("saveQuestion")) {

                String question = params[1];
                String questionType = params[2];
                String difficulty = params[3];
                String answer = params[4];
                String answerGiven = params[5];
                String profile = params[6];
                String userName = params[7];
                String roundID = params[8];
                String correct = params[9];

                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/storeQuestion.php";
                urlParams = "question=" + question + "&questionType=" + questionType + "&difficulty=" + difficulty + "&answer=" + answer + "&answerGiven=" + answerGiven + "&profile=" + profile + "&userName=" + userName + "&roundID=" + roundID + "&correct=" + correct;

            } else if (task.equals("saveRound")) {

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

            } else if (task.equals("calculateTrophies")) {

                String userName = params[1];
                String profileName = params[2];
                String retryRound = params[3];

                urlString = "http://cmaitland02.web.eeecs.qub.ac.uk/AQEStarCaptain/Data/trophyCalculation.php";
                urlParams = "userName=" + userName + "&profileName=" + profileName + "&retryRound=" + retryRound;

            }

            if (urlString.length() > 0) {
                try {
                    URL url = new URL(urlString);
                    //replace + with ASCII value as spaces get converted to + when passed to the url and then automatically converted back to a space at the other end, so need to avoid actual + values being treated as a space
                    urlParams = urlParams.replace("+", "%2B");

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

            System.out.println("s: " + s);

            if (null != s && s.length() > 0) {
                try {
                    JSONObject root = new JSONObject(s);

                    String message = root.optString("message");
                    if (message != null && message.length() > 0) {

                        if (message.equals("Round Saved")) {

                            BackGround b = new BackGround();
                            b.execute("calculateTrophies", userName, profileName, "false");

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // err = "Exception: " + e.getMessage();
                }
            }
        }
    }

}