package chrismaitland.aqestarcaptain;

import java.util.Random;

/**
 * @author Chris
 * As a User I can see an automatically generated Operations question,
 * which has 2 operands, an operator (Randomly selected) and an answer.
 * One of which is replaced with a '?'.
 * The User must guess the value of the '?'.
 */
public class ArithmeticDivision extends Arithmetic {

    public ArithmeticDivision() {
        setOperator('/');
        double second = generateOperand(1, 10);
        double first = generateOperandDivisibleBy((int)second, 1, 10);
        setOperand1(first);
        setOperand2(second);
    }
    
    public ArithmeticDivision(Difficulty difficulty) {
    	super(difficulty);
        setOperator('/');
        double first;
        double second;
        //Easy: 12 times tables
    	//Medium: division of 2 - 4 digit number by a single digit number where numbers divide exactly
        //Hard: a number with 3 - 4 digits before the decimal point and 0 - 2 digits after divided by a single digit with answer having up to 2 decimal places
        switch (difficulty) {
	        case Easy: 
	        	second = generateOperand(1, 12);
	            first = generateOperandDivisibleBy((int)second, (int)second, (int)second*12);
	        	break;
	        case Medium: 
	        	second = generateOperand(1, 9);
	            first = generateOperandDivisibleBy((int)second, 10, 9999);
	        	break;
	        case Hard: 
	        	second = generateOperand(1, 9);
	            first = generateOperandWith2DPDivisibleTo2DP((int)second, 100, 9999);
	        	break;
	        default: 
	        	second = generateOperand(1, 12);
	            first = generateOperandDivisibleBy((int)second, (int)second, (int)second*12);
	        	break;
	    }
        setOperand1(first);
        setOperand2(second);
    }
    
    /**
     * Method to ensure the generated operands will be divisible
     * @param divisibleBy
     * @return
     */
    public double generateOperandDivisibleBy(int divisibleBy, int min, int max) {

        if (min < divisibleBy) {
            min = divisibleBy;
        }

        int multiplierMin = (int)Math.ceil(min/divisibleBy);
        int multiplierMax = (int)Math.floor(max/divisibleBy);

        int multiplier = generateOperand(multiplierMin, multiplierMax);

        return divisibleBy * multiplier;
    }

    public double generateOperandWith2DPDivisibleTo2DP(int divisibleBy, int min, int max) {

        return generateOperandDivisibleBy(divisibleBy, min*100, max*100) / 100;

    }

    /**
     * Method will run the getOperand methods to randomly select numbers, and assign the calculation to a result variable
     **/
    public void calculateResult() {

        double result =  getOperand1() / getOperand2();

        setResult(result);

    }
    
    @Override
    public boolean checkAnswer(String input) {
    	//in case of division by 1 where operand is the missing value, also accept * as the answer as it gives same result
    	if ("operator".equals(hiddenElement) && getOperand2() == 1) {
    		return input.equals(getAnswer()) || input.equals("*");
    	} else {
    		return super.checkAnswer(input);
    	}
    }

    /*
     * Constructor to allow for replaying questions
     * @param difficulty
     * @param the question
     * @param the answer
     */
    public ArithmeticDivision(Difficulty difficulty, String question, String answer) {
        super(difficulty, question, answer);
        this.setOperator('/');
    }

}
