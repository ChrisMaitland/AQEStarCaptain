package chrismaitland.aqestarcaptain;

public class ArithmeticAddition extends Arithmetic {

    public ArithmeticAddition() {
        setOperator('+');
        int first = generateOperand(1, 10);
        int second = generateOperand(1, 10);
        setOperand1(first);
        setOperand2(second);
    }
    
    public ArithmeticAddition(Difficulty difficulty) {
    	super(difficulty);
        setOperator('+');
        double first;
        double second;
        //Easy: 1 or 2 digit numbers
    	//Medium: 3 or 4 digit number and a 2 - 4 digit number
    	//Hard: 2 - 4 digit numbers with 2 decimal places
        switch (difficulty) {
	        case Easy: 
	        	first = generateOperand(1, 99);
	        	second = generateOperand(1, 99);
	        	break;
	        case Medium: 
	        	first = generateOperand(100, 9999);
	        	second = generateOperand(10, 9999);
	        	break;
	        case Hard: 
	        	first = generateOperandWith2DecimalPlaces(10, 9999);
	        	second = generateOperandWith2DecimalPlaces(10, 9999);
	        	break;
	        default: 
	        	first = generateOperand(1, 99);
	        	second = generateOperand(1, 99);
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

        double result =  getOperand1() + getOperand2();

        setResult(result);

    }

    @Override
    public boolean checkAnswer(String input) {
        //in case of 2 + 2 where operand is the missing value, also accept * as the answer as it gives same result
        if ("operator".equals(hiddenElement) && getOperand1() == 2 && getOperand2() == 2) {
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
    public ArithmeticAddition(Difficulty difficulty, String question, String answer) {
        super(difficulty, question, answer);
        this.setOperator('+');
    }

}
