package chrismaitland.aqestarcaptain;

import java.util.Random;

public class ArithmeticMultiplication extends Arithmetic {

    public ArithmeticMultiplication() {
        setOperator('*');
        int first = generateOperand(1, 10);
        int second = generateOperand(1, 10);
        setOperand1(first);
        setOperand2(second);
    }
    
    public ArithmeticMultiplication(Difficulty difficulty) {
    	super(difficulty);
        setOperator('*');
        double first;
        double second;
        //Easy: 12 times tables
    	//Medium: multiplication of 2 or 3 digit number with 2 digit number
    	//Hard: 2 or 3 digit number with 2 decimal places is multiplied by a number with 2 significant figures either a whole number or with 1 digit before the decimal point and one after
        switch (difficulty) {
	        case Easy: 
	        	first = generateOperand(1, 12);
	        	second = generateOperand(1, 12);
	        	break;
	        case Medium: 
	        	first = generateOperand(10, 999);
	        	second = generateOperand(10, 99);
	        	break;
	        case Hard: 
	        	first = generateOperandWith2DecimalPlaces(10, 999);
	        	second = generateOperand(10, 99);
                //half the time use digit digit, half the time use digit.digit
                if (new Random().nextInt(2) == 1) {
                    second = second / 10;
                }
	        	break;
	        default: 
	        	first = generateOperand(1, 12);
	        	second = generateOperand(1, 12);
	        	break;
        }
        setOperand1(first);
        setOperand2(second);
    }

    /**
     * Method will run the getOperand methods to randomly select numbers, and assign the calculation to a result variable
     **/
    @Override
    public void calculateResult() {

        double result =  getOperand1() * getOperand2();

        setResult(result);

    }

    @Override
    public boolean checkAnswer(String input) {
        //in case of multiplication by 1 where operand is the missing value, also accept / as the answer as it gives same result
        //in case of 2 * 2 where operand is the missing value, also accept + as the answer as it gives same result
        if ("operator".equals(hiddenElement) && getOperand2() == 1) {
            return input.equals(getAnswer()) || input.equals("/");
        } else if ("operator".equals(hiddenElement) && getOperand1() == 2 && getOperand2() == 2) {
            return input.equals(getAnswer()) || input.equals("+");
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
    public ArithmeticMultiplication(Difficulty difficulty, String question, String answer) {
        super(difficulty, question, answer);
        this.setOperator('*');
    }

}
