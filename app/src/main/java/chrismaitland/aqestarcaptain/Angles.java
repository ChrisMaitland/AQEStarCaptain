package chrismaitland.aqestarcaptain;

import java.util.Random;

public abstract class Angles extends Question {
	
    private int angle1, angle2, angle3;
    boolean findInterior = true;

    public int getAngle1() {
        return angle1;
    }

    public void setAngle1(int angle1) {
        this.angle1 = angle1;
    }

    public int getAngle2() {
        return angle2;
    }

    public void setAngle2(int angle2) {
        this.angle2 = angle2;
    }

    public int getAngle3() {
        return angle3;
    }

    public void setAngle3(int angle3) {
        this.angle3 = angle3;
    }
    
    public boolean getFindInterior() {
        return findInterior;
    }

    public void setFindInterior(boolean findInterior) {
        this.findInterior = findInterior;
    }

    /**
     * Default Constructor
     */
    public Angles() {

    }
    
    public Angles(Difficulty difficulty) {
    	super(difficulty);
    }

    /**
     * Generates a random angle between min and max
     * @return
     */
    public int generateAngle(int min, int max) {

        Random rand = new Random();

        int output = rand.nextInt(max - min + 1) + min;

        return output;
    }

}
