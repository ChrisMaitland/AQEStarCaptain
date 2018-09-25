package chrismaitland.aqestarcaptain;

import junit.framework.TestCase;

import org.junit.Test;

public class ArithmeticMultiplicationTest extends TestCase {

    ArithmeticMultiplication easyQuestion;
    ArithmeticMultiplication mediumQuestion;
    ArithmeticMultiplication hardQuestion;

    public void setUp() throws Exception {
        super.setUp();
        easyQuestion = new ArithmeticMultiplication(Difficulty.Easy);
        easyQuestion.generateAnswer();
        mediumQuestion = new ArithmeticMultiplication(Difficulty.Medium);
        mediumQuestion.generateAnswer();
        hardQuestion = new ArithmeticMultiplication(Difficulty.Hard);
        hardQuestion.generateAnswer();
    }

    public void tearDown() throws Exception {

    }

    @Test
    public void testConstructorGenerateAnswerAndGetters_Easy() throws Exception {
        validateEasy(easyQuestion);
    }

    @Test
    public void testConstructorGenerateAnswerAndGetters_Medium() throws Exception {
        validateMedium(mediumQuestion);
    }

    @Test
    public void testConstructorGenerateAnswerAndGetters_Hard() throws Exception {
        validateHard(hardQuestion);
    }

    @Test
    public void testGenerateOperandDivisibleBy_EasyQuestion() throws Exception {
        double first = easyQuestion.generateOperand(1, 12);
        double second = easyQuestion.generateOperand(1, 12);

        validateEasyOperands(first, second);
    }

    @Test
    public void testGenerateOperandDivisibleBy_MediumQuestion() throws Exception {
        double first = mediumQuestion.generateOperand(10, 999);
	    double second = mediumQuestion.generateOperand(10, 99);

        validateMediumOperands(first, second);
    }

    @Test
    public void testGenerateOperandWith2DecimalPlaces_HardQuestion() throws Exception {
        double first = hardQuestion.generateOperandWith2DecimalPlaces(10, 999);
        double second = hardQuestion.generateOperand(10, 99);

        //check for digit digit
        validateHardOperands(first, second);

        //check for digit.digit
        second = second / 10;
        validateHardOperands(first, second);
    }

    @Test
    public void testCalculateResult() throws Exception {
        easyQuestion.calculateResult();
        assertEquals(easyQuestion.getResult(), easyQuestion.getOperand1() * easyQuestion.getOperand2());
    }

    @Test
    public void testCheckAnswer_correct() throws Exception {
        boolean correct = easyQuestion.checkAnswer(easyQuestion.getAnswer());
        assertTrue(correct);
    }

    @Test
    public void testCheckAnswer_incorrect() throws Exception {
        boolean correct = easyQuestion.checkAnswer(easyQuestion.getAnswer() + 1);
        assertFalse(correct);
    }

    @Test
    public void testCheckAnswerOperatorVariations() {
        //in normal case only accept *
        easyQuestion.setOperand1(10);
        easyQuestion.setOperand2(5);
        easyQuestion.calculateResult();
        easyQuestion.hiddenElement = "operator";
        easyQuestion.setAnswer(Character.toString(easyQuestion.getOperator()));
        assertTrue(easyQuestion.checkAnswer("*"));
        assertFalse(easyQuestion.checkAnswer("+"));
        assertFalse(easyQuestion.checkAnswer("/"));

        //in case of multiplication by 1 where operand is the missing value, also accept / as the answer as it gives same result
        easyQuestion.setOperand2(1);
        easyQuestion.calculateResult();
        easyQuestion.hiddenElement = "operator";
        easyQuestion.setAnswer(Character.toString(easyQuestion.getOperator()));
        assertTrue(easyQuestion.checkAnswer("*"));
        assertFalse(easyQuestion.checkAnswer("+"));
        assertTrue(easyQuestion.checkAnswer("/"));

        //in case of 2 * 2 where operand is the missing value, also accept + as the answer as it gives same result
        easyQuestion.setOperand1(2);
        easyQuestion.setOperand2(2);
        easyQuestion.calculateResult();
        easyQuestion.hiddenElement = "operator";
        easyQuestion.setAnswer(Character.toString(easyQuestion.getOperator()));
        assertTrue(easyQuestion.checkAnswer("*"));
        assertTrue(easyQuestion.checkAnswer("+"));
        assertFalse(easyQuestion.checkAnswer("/"));
    }

    @Test
    public void testFormatOperand1_Easy() {
        assertEquals(Double.parseDouble(easyQuestion.formatOperand1()), easyQuestion.getOperand1());
    }

    @Test
    public void testFormatOperand2_Easy() {
        assertEquals(Double.parseDouble(easyQuestion.formatOperand2()), easyQuestion.getOperand2());
    }

    @Test
    public void testFormatResult_Easy() {
        assertEquals(Double.parseDouble(easyQuestion.formatResult()), easyQuestion.getResult());
    }

    @Test
    public void testFormatOperand1_Medium() {
        assertEquals(Double.parseDouble(mediumQuestion.formatOperand1()), mediumQuestion.getOperand1());
    }

    @Test
    public void testFormatOperand2_Medium() {
        assertEquals(Double.parseDouble(mediumQuestion.formatOperand2()), mediumQuestion.getOperand2());
    }

    @Test
    public void testFormatResult_Medium() {
        assertEquals(Double.parseDouble(mediumQuestion.formatResult()), mediumQuestion.getResult());
    }

    @Test
    public void testFormatOperand1_Hard() {
        assertTrue(Arithmetic.compareDouble(Double.parseDouble(hardQuestion.formatOperand1()), hardQuestion.getOperand1()));
    }

    @Test
    public void testFormatOperand2_Hard() {
        assertTrue(Arithmetic.compareDouble(Double.parseDouble(hardQuestion.formatOperand2()), hardQuestion.getOperand2()));
    }

    @Test
    public void testFormatResult_Hard() {
        assertTrue(Arithmetic.compareDouble(Double.parseDouble(hardQuestion.formatResult()), hardQuestion.getResult()));
    }

    private void validateEasy(ArithmeticMultiplication question) {
        //difficulty Easy
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Easy);

        //Easy: 12 times tables
        double operand1 = question.getOperand1();
        double operand2 = question.getOperand2();
        double result = question.getResult();
        validateEasyOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateEasyOperands(double operand1, double operand2, double result) {
        //Easy: 12 times tables

        validateEasyOperands(operand1, operand2);

        //result between 1 and 144 and is operand1 * operand2
        assertTrue(result >= 1 && result <= 144 && result == operand1 * operand2);
    }

    private void validateEasyOperands(double operand1, double operand2) {
        //Easy: 12 times tables

        //operand1 between 1 and 12
        assertTrue(operand1 >= 1 && operand1 <= 12);

        //operand2 between 1 and 12
        assertTrue(operand2 >= 1 && operand2 <= 12);
    }

    private void validateMedium(ArithmeticMultiplication question) {
        //difficulty Easy
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Medium);

        //Medium: multiplication of 2 or 3 digit number with 2 digit number
        double operand1 = question.getOperand1();
        double operand2 = question.getOperand2();
        double result = question.getResult();
        validateMediumOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateMediumOperands(double operand1, double operand2, double result) {
        //Medium: multiplication of 2 or 3 digit number with 2 digit number
        validateMediumOperands(operand1, operand2);

        //result between 100 and 98901 and is operand1 * operand2
        assertTrue(result >= 100 && result <= 98901 && result == operand1 * operand2);
    }

    private void validateMediumOperands(double operand1, double operand2) {
        //Medium: multiplication of 2 or 3 digit number with 2 digit number

        //operand1 between 10 and 999
        assertTrue(operand1 >= 10 && operand1 <= 999);

        //operand2 10 and 99
        assertTrue(operand2 >= 10 && operand2 <= 99);
    }

    private void validateHard(ArithmeticMultiplication question) {
        //difficulty Hard
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Hard);

        //Hard: 2 or 3 digit number with 2 decimal places is multiplied by a number with 2 significant figures either a whole number or with 1 digit before the decimal point and one after
        double operand1 = question.getOperand1();
        double operand2 = question.getOperand2();
        double result = question.getResult();
        validateHardOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateHardOperands(double operand1, double operand2, double result) {
        //Hard: 2 or 3 digit number with 2 decimal places is multiplied by a number with 2 significant figures either a whole number or with 1 digit before the decimal point and one after

        validateHardOperands(operand1, operand2);

        //result between 1 and 98901 and is operand1 * operand2
        //and the result has 3 decimal places or fewer
        assertTrue(result >= 1 && result <= 98901 && result == operand1 * operand2);
        String[] resultNumberParts = String.valueOf(operand1).split("[.]");
        assertTrue(resultNumberParts.length == 1 || resultNumberParts[1].length() <= 3);
    }

    private void validateHardOperands(double operand1, double operand2) {
        //Hard: 2 or 3 digit number with 2 decimal places is multiplied by a number with 2 significant figures either a whole number or with 1 digit before the decimal point and one after

        //operand1 between 10 and 999 with up to 2 decimal places
        //and the number has 2 - 3 digits before the decimal point and 0 - 2 after
        assertTrue(operand1 >= 10 && operand1 <= 999);
        String[] operand1NumberParts = (String.valueOf(operand1)).split("[.]");
        assertTrue(operand1NumberParts[0].length() >= 2 && operand1NumberParts[0].length() <= 3 && (operand1NumberParts.length == 1 || operand1NumberParts[1].length() <= 2));

        //operand2 between 0 and 99 with 2 digits, either as digit digit or digit.digit
        assertTrue(operand2 >= 0 && operand2 <= 99);
        String[] operand2NumberParts = (String.valueOf(operand2)).split("[.]");
        assertTrue((operand2NumberParts[0].length() == 1 && operand2NumberParts[1].length() == 1) ||
                (operand2NumberParts[0].length() == 2 && (operand1NumberParts.length == 1 || operand2NumberParts[1].length() == 0 || operand2NumberParts[1].equals("0"))));
    }

    private void validate(ArithmeticMultiplication question, double operand1, double operand2, double result) {

        //operator *
        char operator = question.getOperator();
        assertEquals(operator, '*');

        //full question is in format "operand1 * operand2 = result" with one element replaced by ?
        String questionInFull = question.getQuestionInFull();
        assertEquals(questionInFull, String.format("%s %s %s = %s", question.formatOperand1(), operator, question.formatOperand2(), question.formatResult()));

        //hidden element one of "operand1", "operator", "operand2", "result"
        String hiddenElement = question.hiddenElement;
        assertTrue("operand1".equals(hiddenElement) || "operator".equals(hiddenElement) || "operand2".equals(hiddenElement) || "result".equals(hiddenElement));

        //answer equal to the value of the hidden element
        //and question is in format "operand1 * operand2 = result"
        String answer = question.getAnswer();
        String questionString = question.getQuestion();
        switch(hiddenElement) {
            case "operand1":
                assertEquals(answer, question.formatOperand1());
                assertEquals(questionString, String.format("%s %s %s = %s", "?", operator, question.formatOperand2(), question.formatResult()));
                break;
            case "operator":
                assertEquals(answer, String.valueOf(operator));
                assertEquals(questionString, String.format("%s %s %s = %s", question.formatOperand1(), "?", question.formatOperand2(), question.formatResult()));
                break;
            case "operand2":
                assertEquals(answer, question.formatOperand2());
                assertEquals(questionString, String.format("%s %s %s = %s", question.formatOperand1(), operator, "?", question.formatResult()));
                break;
            case "result":
                assertEquals(answer, question.formatResult());
                assertEquals(questionString, String.format("%s %s %s = %s", question.formatOperand1(), operator, question.formatOperand2(), "?"));
                break;
            default:
                break;
        }

    }

}