package chrismaitland.aqestarcaptain;

/**
 * @author Chris
 * As a User I can see an automatically generated Measures question,
 * which has 3 given angles and 1 hidden angle.
 * The User must guess the value of the missing Angle.
 */
public class AnglesQuadrilateral extends Angles {

	int angle4;
	
	public void setAngle4(int angle4) {
		this.angle4 = angle4;
	}
	
	public int getAngle4() {
		return angle4;
	}
	
    /**
     * Generates a basic quadrilateral question,
     * 4 angles that add to the sum of 360
     */
    public AnglesQuadrilateral() {
        int first = generateAngle(5, 175);
        int second = generateAngle(5, 350 - first);
        int third = generateAngle(5, 350 - first - second);
        int fourth = 360 - first - second - third;
        setAngle1(first);
        setAngle2(second);
        setAngle3(third);
        setAngle4(fourth);
    }
    
    public AnglesQuadrilateral(Difficulty difficulty) {
    	
    	super(difficulty);
    	
    	int first;
    	int second;
    	int third;
    	int fourth;
    	
    	//Easy: 1 missing angle with angles divisible by 10
        //Medium: 1 missing angle
        //Hard: Find exterior angle of missing angle
    	switch (difficulty) {
	        case Easy: 
	        	first = generateAngle(1, 17) * 10;
	            second = generateAngle(1, 34 - (first/10)) * 10;
	            third = generateAngle(5, 350 - first - second);
	            fourth = 360 - first - second - third;
	            setFindInterior(true);
	        	break;
	        case Medium: 
	        	first = generateAngle(5, 175);
	            second = generateAngle(5, 350 - first);
	            third = generateAngle(5, 355 - first - second);
	            fourth = 360 - first - second - third;
	            setFindInterior(true);
	        	break;
	        case Hard: 
	        	first = generateAngle(5, 175);
	            second = generateAngle(5, 350 - first);
	            third = generateAngle(5, 355 - first - second);
	            fourth = 360 - first - second - third;
	            setFindInterior(false);
	        	break;
	        default: 
	        	first = generateAngle(1, 17) * 10;
	            second = generateAngle(1, 34 - (first/10)) * 10;
	            third = generateAngle(5, 350 - first - second);
	            fourth = 360 - first - second - third;
	            setFindInterior(true);
	        	break;
	    }
    	
        setAngle1(first);
        setAngle2(second);
        setAngle3(third);
        setAngle4(fourth);
    }
    
    public void generateAnswer() {
    	if(findInterior) {
			setAnswer(Integer.toString(getAngle4()));
        } else {
        	int exteriorAngle = 360 - getAngle4();
        	setAnswer(Integer.toString(exteriorAngle));
        }
    }
    
    public String getQuestion() {
        String blank = "?";
        String question = "";
        if(findInterior) {
            question = String.format("Find the missing interior angle in the following quadrilateral: %s, %s, %s & %s", getAngle1(), getAngle2(), getAngle3(), blank);
        } else {
        	question = String.format("Given a quadrilateral with known angles of %s, %s and %s, calculate the exterior angle of the missing angle.", getAngle1(), getAngle2(), getAngle3());
        }
        return question;

    }

    public String getQuestionInFull() {
    	if(findInterior) {
            return String.format("The angles for the quadrilateral are: %s, %s, %s & %s", getAngle1(), getAngle2(), getAngle3(), getAngle4());
        } else {
        	return String.format("A quadrilateral with angles %s, %s and %s has a fourth angle of %s with an exterior angle of %s", getAngle1(), getAngle2(), getAngle3(), getAngle4(), getAnswer());
        }
    }

	/*
	 * Constructor to allow for replaying questions
	 * @param difficulty
	 * @param the question
	 * @param the answer
	 */
	public AnglesQuadrilateral(Difficulty difficulty, String question, String answer) {
		super(difficulty);
		String[] components = question.split(" ");
		String first, second, third;
		if (question.contains("interior")) {
			setFindInterior(true);
			//"Find the missing interior angle in the following quadrilateral: %s, %s, %s & %s"
			first = components[9].replace(",", "");
			second = components[10].replace(",", "");
			third = components[11];
		} else {
			setFindInterior(false);
			//"Given a quadrilateral with known angles of %s, %s and %s, calculate the exterior angle of the missing angle."
			first = components[7].replace(",", "");
			second = components[8];
			third = components[10].replace(",", "");
		}
		setAngle1(Integer.parseInt(first));
		setAngle2(Integer.parseInt(second));
		setAngle3(Integer.parseInt(third));
		setAngle4(Integer.parseInt(answer));
		setAnswer(answer);
	}

}
