package chrismaitland.aqestarcaptain;


import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author Chris
 * As a User I can see an automatically generated Operations question,
 * which has 2 operands, an operator (Randomly selected) and an answer.
 * One of which is replaced with a '?'.
 * The User must guess the value of the '?'.
 */
public abstract class Arithmetic extends Question {

    // creating variables
    private double operand1, operand2, result;
    private char operator;
    String hiddenElement;
    DecimalFormat decimalFormat2DecimalPlaces = new DecimalFormat("#.00"); 
    DecimalFormat decimalFormatNoDecimalPlaces = new DecimalFormat("#");
    DecimalFormat decimalFormat3DecimalPlaces = new DecimalFormat("#.000");

    /**
     * @return the operand1
     */
    public double getOperand1() {
        return operand1;
    }

    /**
     * @param operand1
     */
    public void setOperand1(double operand1) {
        this.operand1 = operand1;
    }

    /**
     * @return the operand2
     */
    public double getOperand2() {
        return operand2;
    }

    /**
     * @param operand2
     */
    public void setOperand2(double operand2) {
        this.operand2 = operand2;
    }

    /**
     * @return the operator
     */
    public char getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(char operator) {
        this.operator = operator;
    }

    /**
     * @return the result
     */
    public double getResult() {
        return result;
    }

    /**
     * @param result
     */
    public void setResult(double result) {
        this.result = result;
    }

    /**
     * Constructor with arg
     */
    public Arithmetic(Difficulty diff) {
        super(diff);
    }

    /**
     * Default Constructor
     */
    public Arithmetic () {

    }

    /**
     * Generates a random Operand within a given range
     * @return
     */
    public int generateOperand(int min, int max) {

        int range = max - min + 1;

        Random rand = new Random();

        int output = rand.nextInt(range) + min;

        return output;
    }

    /**
     * Generates a random Operand with 2 decimal places
     * @return
     */
    public double generateOperandWith2DecimalPlaces(int min, int max) {

        return generateOperand(min*100, max*100) / 100;

    }

    public abstract void calculateResult();

    public void generateAnswer() {
        calculateResult();
        selectElementToHide();
    }

    public String formatOperand1() {
    	if (operand1 % 1 == 0) {
    		return decimalFormatNoDecimalPlaces.format(operand1);
    	} else {
    		return decimalFormat2DecimalPlaces.format(operand1);
    	}
    }
    
    public String formatOperand2() {
    	if (operand2 % 1 == 0) {
    		return decimalFormatNoDecimalPlaces.format(operand2);
    	} else {
    		return decimalFormat2DecimalPlaces.format(operand2);
    	}
    }
    
    public String formatResult() {
        //if neither result nor the operands have decimal places show no decimal places in the result
    	if (result % 1 == 0 && operand1 % 1 == 0 && operand2 % 1 == 0) {
    		return decimalFormatNoDecimalPlaces.format(result);
    	} else {
            //if the result has 1 or 2 decimal places, show 2 decimal places
            if(result*100 % 1 == 0) {
                return decimalFormat2DecimalPlaces.format(result);
            } else {
                //if the result has more then show 3 decimal places (3 decimal places are currently possible in some multiplications)
                return decimalFormat3DecimalPlaces.format(result);
            }
    	}
    }
    
    public void selectElementToHide() {

        int answerIndex = new Random().nextInt(4);
        switch(answerIndex) {
            case 0: setAnswer(formatOperand1());
                hiddenElement = "operand1";
                break;
            case 1: setAnswer(Character.toString(operator));
                hiddenElement = "operator";
                break;
            case 2: setAnswer(formatOperand2());
                hiddenElement = "operand2";
                break;
            case 3: setAnswer(formatResult());
                hiddenElement = "result";
                break;
            default:
                break;
        }

    }
    
    public String getQuestion() {
        String blank = "?";
        String question = "";
        switch(hiddenElement) {
            case "operand1": question = String.format("%s %s %s = %s", blank, operator, formatOperand2(), formatResult());
                break;
            case "operator": question = String.format("%s %s %s = %s", formatOperand1(), blank, formatOperand2(), formatResult());
                break;
            case "operand2": question = String.format("%s %s %s = %s", formatOperand1(), operator, blank, formatResult());
                break;
            case "result": question = String.format("%s %s %s = %s", formatOperand1(), operator, formatOperand2(), blank);
                break;
            default:
                break;
        }
        return question;

    }

    public String getQuestionInFull() {
        return String.format("%s %s %s = %s", formatOperand1(), operator, formatOperand2(), formatResult());
    }

    @Override
    public boolean checkAnswer(String input) {
        //in case of anything other than operator, check numeric value as well as string comparison, in case of variation due to decimals etc.
        if (hiddenElement.equals("operand1") || hiddenElement.equals("operand2") || hiddenElement.equals("result")) {
            try {
                return super.checkAnswer(input) || compareDouble(Double.parseDouble(input), Double.parseDouble(getAnswer()));
            } catch (Exception e) {
                return false;
            }
        } else {
            return super.checkAnswer(input);
        }
    }

    // Utility method to allow tolerance in double comparison - used when checking answer and in tests
    public static boolean compareDouble(double double1, double double2) {
        return Math.abs(double1 - double2) < 0.00001;
    }

    /*
     * Constructor to allow for replaying questions
     * @param difficulty
     * @param the question
     * @param the answer
     */
    public Arithmetic(Difficulty difficulty, String question, String answer) {
        super(difficulty);
        String[] components = question.split(" ");

        if (components[0].equals("?")) {
            operand1 = Double.parseDouble(answer);
            hiddenElement = "operand1";
        } else {
            operand1 = Double.parseDouble(components[0]);
        }

        if (components[2].equals("?")) {
            operand2 = Double.parseDouble(answer);
            hiddenElement = "operand2";
        } else {
            operand2 = Double.parseDouble(components[2]);
        }

        int resultIndex = components.length - 1;
        if (components[resultIndex].equals("?")) {
            result = Double.parseDouble(answer);
            hiddenElement = "result";
        } else {
            result = Double.parseDouble(components[resultIndex]);
        }

        if (components[1].equals("?")) {
            hiddenElement = "operator";
        }

        this.setAnswer(answer);
    }

}
