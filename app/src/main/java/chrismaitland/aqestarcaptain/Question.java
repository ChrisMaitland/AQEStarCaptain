package chrismaitland.aqestarcaptain;

import android.widget.TextView;

public abstract class Question {

    private String answer;

    private Difficulty difficulty;

    /**
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    // The following abstract methods are relevant to all questions

    public abstract void generateAnswer();
    
    public void printQuestion(TextView textView) {
        String blank = "?";
        textView.setText(getQuestion());
    }
    
    public abstract String getQuestion();

    public abstract String getQuestionInFull();

    // This method may or may not be overridden in child classes
    public boolean checkAnswer(String input) {
    	return input.equals(getAnswer());
    }
    
    /**
     * Constructor to allow generation of a question with difficulty specified
     */
    public Question(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Constructor to allow generation of a question without difficulty specified
     */
    public Question() {

    }

}
