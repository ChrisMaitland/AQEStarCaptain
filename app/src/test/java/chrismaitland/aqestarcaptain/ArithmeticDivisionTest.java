package chrismaitland.aqestarcaptain;

import junit.framework.TestCase;

import org.junit.Test;

public class ArithmeticDivisionTest extends TestCase {

    ArithmeticDivision easyQuestion;
    ArithmeticDivision mediumQuestion;
    ArithmeticDivision hardQuestion;

    public void setUp() throws Exception {
        super.setUp();
        easyQuestion = new ArithmeticDivision(Difficulty.Easy);
        easyQuestion.generateAnswer();
        mediumQuestion = new ArithmeticDivision(Difficulty.Medium);
        mediumQuestion.generateAnswer();
        hardQuestion = new ArithmeticDivision(Difficulty.Hard);
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
        double second = easyQuestion.generateOperand(1, 12);
        double first = easyQuestion.generateOperandDivisibleBy((int)second, (int)second, 144);

        validateEasyOperands(first, second);
    }

    @Test
    public void testGenerateOperandDivisibleBy_MediumQuestion() throws Exception {
        double second = mediumQuestion.generateOperand(1, 9);
        double first = mediumQuestion.generateOperandDivisibleBy((int)second, 10, 9999);

        validateMediumOperands(first, second);
    }

    @Test
    public void testGenerateOperandWith2DPDivisibleTo2DP_HardQuestion() throws Exception {
        double second = hardQuestion.generateOperand(1, 9);
        double first = hardQuestion.generateOperandWith2DPDivisibleTo2DP((int)second, 100, 9999);

        validateHardOperands(first, second);
    }

    @Test
    public void testCalculateResult() throws Exception {
        easyQuestion.calculateResult();
        assertEquals(easyQuestion.getResult(), easyQuestion.getOperand1() / easyQuestion.getOperand2());
    }

    @Test
    public void testCheckAnswer_correct() throws Exception {
        boolean correct = easyQuestion.checkAnswer(easyQuestion.getAnswer());
        assertTrue(correct);
    }

    @Test
    public void testCheckAnswer_incorrect() throws Exception {
        boolean correct = easyQuestion.checkAnswer(easyQuestion.getAnswer() + 1);
        assertEquals(correct, false);
    }

    @Test
    public void testCheckAnswerOperatorVariations() {
        //in normal case only accept /
        easyQuestion.setOperand1(10);
        easyQuestion.setOperand2(5);
        easyQuestion.calculateResult();
        easyQuestion.hiddenElement = "operator";
        easyQuestion.setAnswer(Character.toString(easyQuestion.getOperator()));
        assertTrue(easyQuestion.checkAnswer("/"));
        assertFalse(easyQuestion.checkAnswer("*"));

        //in case of division by 1 where operand is the missing value, also accept * as the answer as it gives same result
        easyQuestion.setOperand2(1);
        easyQuestion.calculateResult();
        easyQuestion.hiddenElement = "operator";
        easyQuestion.setAnswer(Character.toString(easyQuestion.getOperator()));
        assertTrue(easyQuestion.checkAnswer("/"));
        assertTrue(easyQuestion.checkAnswer("*"));
    }

    @Test
    public void testFormatOperand1_Easy() {
        assertEquals(Double.parseDouble(easyQuestion.formatOperand1()), (easyQuestion.getOperand1()));
    }

    @Test
    public void testFormatOperand2_Easy() {
        assertEquals(Double.parseDouble(easyQuestion.formatOperand2()), (easyQuestion.getOperand2()));
    }

    @Test
    public void testFormatResult_Easy() {
        assertEquals(Double.parseDouble(easyQuestion.formatResult()), (easyQuestion.getResult()));
    }

    @Test
    public void testFormatOperand1_Medium() {
        assertEquals(Double.parseDouble(mediumQuestion.formatOperand1()), (mediumQuestion.getOperand1()));
    }

    @Test
    public void testFormatOperand2_Medium() {
        assertEquals(Double.parseDouble(mediumQuestion.formatOperand2()), (mediumQuestion.getOperand2()));
    }

    @Test
    public void testFormatResult_Medium() {
        assertEquals(Double.parseDouble(mediumQuestion.formatResult()), (mediumQuestion.getResult()));
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

    private void validateEasy(ArithmeticDivision question) {
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

        //result between 1 and 12 and is operand1 / operand2
        assertTrue(result >= 1 && result <= 12 && result == operand1 / operand2);
    }

    private void validateEasyOperands(double operand1, double operand2) {
        //Easy: 12 times tables

        //operand1 between 1 and 144
        assertTrue(operand1 >= 1 && operand1 <= 144);

        //operand2 between 1 and 12
        assertTrue(operand2 >= 1 && operand2 <= 12);
    }

    private void validateMedium(ArithmeticDivision question) {
        //difficulty Easy
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Medium);

        //Medium: division of 2 - 4 digit number by a single digit number where numbers divide exactly
        double operand2 = question.getOperand2();
        double operand1 = question.getOperand1();
        double result = question.getResult();
        validateMediumOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateMediumOperands(double operand1, double operand2, double result) {
        //Medium: division of 2 - 4 digit number by a single digit number where numbers divide exactly

        validateMediumOperands(operand1, operand2);

        //result between 2 and 9999 and is operand1 / operand2
        assertTrue(result >= 2 && result <= 9999 && result == operand1 / operand2);
    }

    private void validateMediumOperands(double operand1, double operand2) {
        //Medium: division of 2 - 4 digit number by a single digit number where numbers divide exactly

        //operand2 1 and 9
        assertTrue(operand2 >= 1 && operand2 <= 9);

        //operand1 between 10 and 9999 and divisible by operand2
        assertTrue(operand1 >= 10 && operand1 <= 9999 && operand1%operand2==0);
    }

    private void validateHard(ArithmeticDivision question) {
        //difficulty Hard
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Hard);

        //Hard: a number with 3 - 4 digits before the decimal point and 0 - 2 digits after divided by a single digit with answer having up to 2 decimal places
        double operand2 = question.getOperand2();
        double operand1 = question.getOperand1();
        double result = question.getResult();
        validateHardOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateHardOperands(double operand1, double operand2, double result) {
        //Hard: a number with 3 - 4 digits before the decimal point and 0 - 2 digits after divided by a single digit with answer having up to 2 decimal places

        validateHardOperands(operand1, operand2);

        //result between 11 and 9999 and is operand1 / operand2
        //and the result has 2 decimal places or fewer
        assertTrue(result >= 11 && result <= 9999 && result == operand1 / operand2);
        String[] resultNumberParts = (String.valueOf(operand1)).split("[.]");
        assertTrue(resultNumberParts.length == 1 || resultNumberParts[1].length() <= 2);
    }

    private void validateHardOperands(double operand1, double operand2) {
        //Hard: a number with 3 - 4 digits before the decimal point and 0 - 2 digits after divided by a single digit with answer having up to 2 decimal places

        //operand2 between 1 and 9
        assertTrue(operand2 >= 1 && operand2 <= 9);

        //operand1 between 100 and 9999
        //and the value when multiplied by 100 will be divisible by operand2 (i.e. the result of the question will have at most 2 decimal places)
        //and the number has 3 - 4 digits before the decimal point and 0 - 2 after
        assertTrue(operand1 >= 100 && operand1 <= 9999 && (Arithmetic.compareDouble((operand1*100)%operand2, 0) || Arithmetic.compareDouble((operand1*100)%operand2, operand2)));
        String[] operand1NumberParts = (String.valueOf(operand1)).split("[.]");
        assertTrue(operand1NumberParts[0].length() >= 3 && operand1NumberParts[0].length() <= 4 && (operand1NumberParts.length == 1 || operand1NumberParts[1].length() <= 2));
    }

    private void validate(ArithmeticDivision question, double operand1, double operand2, double result) {

        //operator /
        char operator = question.getOperator();
        assertEquals(operator, '/');

        //full question is in format "operand1 / operand2 = result" with one element replaced by ?
        String questionInFull = question.getQuestionInFull();
        assertEquals(questionInFull, String.format("%s %s %s = %s", question.formatOperand1(), operator, question.formatOperand2(), question.formatResult()));

        //hidden element one of "operand1", "operator", "operand2", "result"
        String hiddenElement = question.hiddenElement;
        assertTrue("operand1".equals(hiddenElement) || "operator".equals(hiddenElement) || "operand2".equals(hiddenElement) || "result".equals(hiddenElement));

        //answer equal to the value of the hidden element
        //and question is in format "operand1 / operand2 = result"
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