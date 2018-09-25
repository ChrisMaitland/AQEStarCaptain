package chrismaitland.aqestarcaptain;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Random;

public class TimeManipulation extends Question {

	private static final int MINUTES_PER_DAY = 1440;
	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final SimpleDateFormat df24hr = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat df12hr = new SimpleDateFormat("h:mm aa");
	Calendar time;
	boolean twelveHour = false;
	Calendar start;
	Calendar end;
	int hourDifference;
	int minuteDifference;
	String question;
	String questionInFull;
    
    public TimeManipulation(Difficulty difficulty) {
    	
    	super(difficulty);
    	try {
	    	Random random = new Random();
	        //Easy: convert 12 hr to 24 hr or 24 hr to 12 hr
	    	//Medium: given a start time, add an amount of time
	    	//Hard: given an end time, subtract an amount of time
	    	int rand12Or24 = random.nextInt(2);
	    	String str12Or24 = "24";
	    	if (rand12Or24 == 0) {
        		twelveHour = true;
        		str12Or24 = "12";
	    	} else {
	    		twelveHour = false;
	    		str12Or24 = "24";
	    	}
	        switch (difficulty) {
		        case Easy: 
		        	time = Calendar.getInstance();
		        	time.setTimeInMillis(random.nextInt(MINUTES_PER_DAY)*MILLISECONDS_PER_MINUTE);
		        	if (twelveHour) {
		        		question = String.format("Convert %s to the 12 hour clock.", df24hr.format(time.getTime()));
		        		questionInFull = String.format("%s in the 12 hour clock is %s.", df24hr.format(time.getTime()), df12hr.format(time.getTime()));
		        		this.setAnswer(df12hr.format(time.getTime()));
		        	} else {
		        		question = String.format("Convert %s to the 24 hour clock.", df12hr.format(time.getTime()));
		        		questionInFull = String.format("%s in the 24 hour clock is %s.", df12hr.format(time.getTime()), df24hr.format(time.getTime()));
		        		this.setAnswer(df24hr.format(time.getTime()));
		        	}
		        	break;
		        case Medium: 
		        	start = Calendar.getInstance();
		        	start.setTimeInMillis(random.nextInt(MINUTES_PER_DAY)*MILLISECONDS_PER_MINUTE);
		        	
		        	hourDifference = random.nextInt(24);
		        	minuteDifference = random.nextInt(60);
		        	
		        	end = (Calendar) start.clone();
		        	
		        	end.add(Calendar.HOUR, hourDifference);
		        	end.add(Calendar.MINUTE, minuteDifference);
		        	
		        	if (twelveHour) {
		        		this.setAnswer(df12hr.format(end.getTime()));
		        	} else {
		        		this.setAnswer(df24hr.format(end.getTime()));
		        	}
		        	
		        	question = String.format("An event starts at %s and lasts %s hours and %s minutes.  What time does the event finish?  Give your answer in the %s hour clock.", df12hr.format(start.getTime()), hourDifference, minuteDifference, str12Or24);
		        	questionInFull = String.format("An event that starts at %s and lasts %s hours and %s minutes will finish at %s in the %s hour clock.", df12hr.format(start.getTime()), hourDifference, minuteDifference, this.getAnswer(), str12Or24);
		        	
		        	break;
		        case Hard:
		        	start = Calendar.getInstance();
		        	start.setTimeInMillis(random.nextInt(MINUTES_PER_DAY)*MILLISECONDS_PER_MINUTE);
		        	
		        	hourDifference = random.nextInt(24);
		        	minuteDifference = random.nextInt(60);
		        	
		        	end = (Calendar) start.clone();
		        	
		        	end.add(Calendar.HOUR, hourDifference);
		        	end.add(Calendar.MINUTE, minuteDifference);
		        	
		        	if (twelveHour) {
		        		this.setAnswer(df12hr.format(start.getTime()));
		        	} else {
		        		this.setAnswer(df24hr.format(start.getTime()));
		        	}
		        	
		        	question = String.format("An event finished at %s and lasted %s hours and %s minutes.  What time did the event start?  Give your answer in the %s hour clock.", df12hr.format(end.getTime()), hourDifference, minuteDifference, str12Or24);
		        	questionInFull = String.format("An event that finished at %s and lasted %s hours and %s minutes started at %s in the %s hour clock.", df12hr.format(end.getTime()), hourDifference, minuteDifference, this.getAnswer(), str12Or24);
		        	
		        	break;
		        default: 
		        	break;
	        }
    	} catch (Exception e) {
    		
    	}
        
    }

    /**
     * 
     **/
    public void generateAnswer() {
    	

    }
    
    public String getQuestion() {
        return question;
    }

    public String getQuestionInFull() {
        return questionInFull;
    }
    
    @Override
    public boolean checkAnswer(String input) {
    	//account for slightly different formats with AM, A.M., am, a.m. etc for 12 hour clock
    	//TODO: in 24 hour clock should there always be 2 digits for the hour or is 1 digit valid?
    	//TODO: in 12 hour clock should there be no leading 0?
    	if (twelveHour) {
    		return input.replace(".", "").replace(" ", "").toUpperCase().trim().equals(getAnswer().replace(".", "").replace(" ", "").toUpperCase().trim());
    	} else {
    		return input.trim().equals(getAnswer().trim());
    	}
    }

	/*
	 * Constructor to allow for replaying questions
	 * @param difficulty
	 * @param the question
	 * @param the answer
	 */
	public TimeManipulation(Difficulty difficulty, String question, String answer) {
		super(difficulty);
		this.question = question;
		this.questionInFull = "Q: " + question + "A: " + answer;
		this.setAnswer(answer);
		if (question.contains("12 hour clock")) {
			this.twelveHour = true;
		} else {
			this.twelveHour = false;
		}
	}

}
