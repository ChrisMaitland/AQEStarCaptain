package chrismaitland.aqestarcaptain;

//import android.widget.TextView;

import java.util.Random;

public class ArithmeticExtraDivision extends ArithmeticDivision {

    int operand3;
    char operator2;

    public ArithmeticExtraDivision() {
        super();
        setOperator2('+');
        int third = generateOperand(1, 10);
        setOperand3(third);
    }
    
    public ArithmeticExtraDivision(Difficulty difficulty) {
        super(difficulty);
        setOperator2('+');
        
        int third;
        
        //Easy: An Easy Division question with a number between 1 and 10 added on
    	//Medium: A Medium Division question with a number between 1 and 50 added on
    	//Hard: A Hard Division question with a number between 1 and 100 added on
        switch (difficulty) {
	        case Easy: 
	        	third = generateOperand(1, 10);
	        	break;
	        case Medium: 
	        	third = generateOperand(1, 50);
	        	break;
	        case Hard: 
	        	third = generateOperand(1, 100);
	        	break;
	        default: 
	        	third = generateOperand(1, 10);
	        	break;
        }
        
        setOperand3(third);
    }

    /**
     * @return the 2nd operator
     */
    public char getOperator2() {
        return operator2;
    }

    /**
     * @param operator2
     */
    public void setOperator2(char operator2) {
        this.operator2 = operator2;
    }

    /**
     * @return the operand3
     */
    public int getOperand3() {
        return operand3;
    }

    /**
     * @param operand3
     */
    public void setOperand3(int operand3) {
        this.operand3 = operand3;
    }

    /**
     * Method will run the getOperand methods to randomly select numbers, and assign the calculation to a result variable
     **/
    @Override
    public void calculateResult() {

        double result =  getOperand1() / getOperand2() + getOperand3();

        setResult(result);

    }

    @Override
    public void selectElementToHide() {

        int answerIndex = new Random().nextInt(6);
        switch(answerIndex) {
            case 0: setAnswer(formatOperand1());
                hiddenElement = "operand1";
                break;
            case 1: setAnswer(Character.toString(getOperator()));
                hiddenElement = "operator";
                break;
            case 2: setAnswer(formatOperand2());
                hiddenElement = "operand2";
                break;
            case 3: setAnswer(Character.toString(getOperator2()));
                hiddenElement = "operator2";
                break;
            case 4: setAnswer(Integer.toString(getOperand3()));
                hiddenElement = "operand3";
                break;
            case 5: setAnswer(formatResult());
                hiddenElement = "result";
                break;
            default:
                break;
        }

    }
    
    @Override
    public String getQuestion() {
        String blank = "?";
        String question = "";
        switch(hiddenElement) {
            case "operand1": question = String.format("%s %s %s %s %s = %s", blank, getOperator(), formatOperand2(), operator2, operand3, formatResult());
                break;
            case "operator": question = String.format("%s %s %s %s %s = %s", formatOperand1(), blank, formatOperand2(), operator2, operand3, formatResult());
                break;
            case "operand2": question = String.format("%s %s %s %s %s = %s", formatOperand1(), getOperator(), blank, operator2, operand3, formatResult());
                break;
            case "result": question = String.format("%s %s %s %s %s = %s", formatOperand1(), getOperator(), formatOperand2(), operator2, operand3, blank);
                break;
            case "operator2": question = String.format("%s %s %s %s %s = %s", formatOperand1(), getOperator(), formatOperand2(), blank, operand3, formatResult());
                break;
            case "operand3": question = String.format("%s %s %s %s %s = %s", formatOperand1(), getOperator(), formatOperand2(), operator2, blank, formatResult());
                break;
            default:
                break;
        }
        return question;

    }

    @Override
    public String getQuestionInFull() {
        return String.format("%s %s %s %s %s = %s", formatOperand1(), getOperator(), formatOperand2(), operator2, operand3, formatResult());
    }

    @Override
    public boolean checkAnswer(String input) {
        //in case of anything other than operator, check numeric value as well as string comparison, in case of variation due to decimals etc.
        if (hiddenElement.equals("operand3")) {
            try {
                return super.checkAnswer(input) || compareDouble(Double.parseDouble(input), Double.parseDouble(getAnswer()));
            } catch (Exception e) {
                return false;
            }
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
    public ArithmeticExtraDivision(Difficulty difficulty, String question, String answer) {
        super(difficulty, question, answer);
        setOperator2('+');

        String[] components = question.split(" ");

        if (components[4].equals("?")) {
            operand3 = Integer.parseInt(answer);
            hiddenElement = "operand3";
        } else {
            operand3 = Integer.parseInt(components[4]);
        }

        if (components[3].equals("?")) {
            hiddenElement = "operator2";
        }

        this.setAnswer(answer);
    }

}
