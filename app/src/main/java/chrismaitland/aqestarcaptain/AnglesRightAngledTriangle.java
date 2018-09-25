package chrismaitland.aqestarcaptain;

public class AnglesRightAngledTriangle extends AnglesTriangle {

    /**
     * Method to simulate a right angled triangle, so one angle must be 90 degrees
     */
    public AnglesRightAngledTriangle() {
    	    	
        int first = 90;
        int second = generateAngle(5, 80 + 1);
        int third = 180 - first - second;
        setAngle1(first);
        setAngle2(second);
        setAngle3(third);
    }
    
    public AnglesRightAngledTriangle(Difficulty difficulty) {
    	
    	super(difficulty);
    	
    	int first;
    	int second;
    	int third;
    	
    	//Easy: 1 missing angle with angles divisible by 10
        //Medium: 1 missing angle
        //Hard: Find exterior angle of missing angle
    	switch (difficulty) {
	        case Easy: 
	        	first = 90;
	            second = generateAngle(1, 8) * 10;
	            third = 180 - first - second;
	            setFindInterior(true);
	        	break;
	        case Medium: 
	        	first = 90;
	            second = generateAngle(5, 85);
	            third = 180 - first - second;
	            setFindInterior(true);
	        	break;
	        case Hard: 
	        	first = 90;
	            second = generateAngle(5, 85);
	            third = 180 - first - second;
	            setFindInterior(false);
	        	break;
	        default: 
	        	first = 90;
	            second = generateAngle(1, 8) * 10;
	            third = 180 - first - second;
	            setFindInterior(true);
	        	break;
	    }
    	
        setAngle1(first);
        setAngle2(second);
        setAngle3(third);
    }

	/*
	 * Constructor to allow for replaying questions
	 * @param difficulty
	 * @param the question
	 * @param the answer
	 */
	public AnglesRightAngledTriangle(Difficulty difficulty, String question, String answer) {
		super(difficulty, question, answer);
	}

}
