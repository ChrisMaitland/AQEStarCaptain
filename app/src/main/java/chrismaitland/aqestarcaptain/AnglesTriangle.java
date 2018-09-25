package chrismaitland.aqestarcaptain;

/**
 * @author Chris
 * As a User I can see an automatically generated Measures question,
 * which has 2 given angles and 1 hidden angle.
 * The User must guess the value of the missing Angle.
 */
public class AnglesTriangle extends Angles {

    /**
     * Generates a basic triangle question,
     * 3 angles that add to the sum of 180
     */
    public AnglesTriangle() {
        int first = generateAngle(5, 170);
        int second = generateAngle(5, 175 - first);
        int third = 180 - first - second;
        setAngle1(first);
        setAngle2(second);
        setAngle3(third);
    }
    
    public AnglesTriangle(Difficulty difficulty) {
    	
    	super(difficulty);
    	
    	int first;
    	int second;
    	int third;
    	
    	//Easy: 1 missing angle with angles divisible by 10
        //Medium: 1 missing angle
        //Hard: Find exterior angle of missing angle
    	switch (difficulty) {
	        case Easy: 
	        	first = generateAngle(1, 16) * 10;
	            second = generateAngle(1, 17 - (first/10)) * 10;
	            third = 180 - first - second;
	            setFindInterior(true);
	        	break;
	        case Medium: 
	        	first = generateAngle(5, 170);
	            second = generateAngle(5, 175 - first);
	            third = 180 - first - second;
	            setFindInterior(true);
	        	break;
	        case Hard: 
	        	first = generateAngle(5, 170);
	            second = generateAngle(5, 175 - first);
	            third = 180 - first - second;
	            setFindInterior(false);
	        	break;
	        default: 
	        	first = generateAngle(1, 16) * 10;
	            second = generateAngle(1, 17 - (first/10)) * 10;
	            third = 180 - first - second;
	            setFindInterior(true);
	        	break;
	    }
    	
        setAngle1(first);
        setAngle2(second);
        setAngle3(third);
    }
    
    public void generateAnswer() {
    	if(findInterior) {
			setAnswer(Integer.toString(getAngle3()));
        } else {
        	int exteriorAngle = 360 - getAngle3();
        	setAnswer(Integer.toString(exteriorAngle));
        }
    }
    
    public String getQuestion() {
        String blank = "?";
        String question = "";
        if(findInterior) {
            question = String.format("Find the missing interior angle in the following triangle: %s, %s & %s", getAngle1(), getAngle2(), blank);
        } else {
        	question = String.format("Given a triangle with known angles of %s and %s, calculate the exterior angle of the missing angle.", getAngle1(), getAngle2());
        }
        return question;

    }

    public String getQuestionInFull() {
    	if(findInterior) {
            return String.format("The angles for the triangle are: %s, %s & %s", getAngle1(), getAngle2(), getAngle3());
        } else {
        	return String.format("A triangle with angles %s and %s has a third angle of %s with an exterior angle of %s", getAngle1(), getAngle2(), getAngle3(), getAnswer());
        }
    }

	/*
	 * Constructor to allow for replaying questions
	 * @param difficulty
	 * @param the question
	 * @param the answer
	 */
	public AnglesTriangle(Difficulty difficulty, String question, String answer) {
		super(difficulty);
		String[] components = question.split(" ");
		String first, second;
		if (question.contains("interior")) {
			setFindInterior(true);
			//"Find the missing interior angle in the following triangle: %s, %s & %s"
			first = components[9].replace(",", "");
			second = components[10];
		} else {
			setFindInterior(false);
			//"Given a triangle with known angles of %s and %s, calculate the exterior angle of the missing angle."
			first = components[7];
			second = components[9].replace(",", "");
		}
		setAngle1(Integer.parseInt(first));
		setAngle2(Integer.parseInt(second));
		setAngle3(Integer.parseInt(answer));
		setAnswer(answer);
	}

}
