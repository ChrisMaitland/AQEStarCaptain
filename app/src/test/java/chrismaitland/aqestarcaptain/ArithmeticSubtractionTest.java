package chrismaitland.aqestarcaptain;

import junit.framework.TestCase;

import org.junit.Test;

public class ArithmeticSubtractionTest extends TestCase {

    ArithmeticSubtraction easyQuestion;
    ArithmeticSubtraction mediumQuestion;
    ArithmeticSubtraction hardQuestion;

    public void setUp() throws Exception {
        super.setUp();
        easyQuestion = new ArithmeticSubtraction(Difficulty.Easy);
        easyQuestion.generateAnswer();
        mediumQuestion = new ArithmeticSubtraction(Difficulty.Medium);
        mediumQuestion.generateAnswer();
        hardQuestion = new ArithmeticSubtraction(Difficulty.Hard);
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
        double first = easyQuestion.generateOperand(1, 99);
        double second = easyQuestion.generateOperand(1, (int) first);

        validateEasyOperands(first, second);
    }

    @Test
    public void testGenerateOperandDivisibleBy_MediumQuestion() throws Exception {
        double first = mediumQuestion.generateOperand(100, 9999);
        double second = mediumQuestion.generateOperand(10, (int) first);

        validateMediumOperands(first, second);
    }

    @Test
    public void testGenerateOperandWith2DecimalPlaces_HardQuestion() throws Exception {
        double first = hardQuestion.generateOperandWith2DecimalPlaces(10, 9999);
        double second = hardQuestion.generateOperandWith2DecimalPlaces(10, (int) first);

        validateHardOperands(first, second);
    }

    @Test
    public void testCalculateResult() throws Exception {
        easyQuestion.calculateResult();
        assertEquals(easyQuestion.getResult(), easyQuestion.getOperand1() - easyQuestion.getOperand2());
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
        //in all cases should only accept -
        easyQuestion.setOperand1(10);
        easyQuestion.setOperand2(5);
        easyQuestion.calculateResult();
        easyQuestion.hiddenElement = "operator";
        easyQuestion.setAnswer(Character.toString(easyQuestion.getOperator()));
        assertTrue(easyQuestion.checkAnswer("-"));
        assertFalse(easyQuestion.checkAnswer("+"));
        assertFalse(easyQuestion.checkAnswer("*"));
        assertFalse(easyQuestion.checkAnswer("/"));
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
        assertEquals(Double.parseDouble(hardQuestion.formatOperand1()), (hardQuestion.getOperand1()));
    }

    @Test
    public void testFormatOperand2_Hard() {
        assertEquals(Double.parseDouble(hardQuestion.formatOperand2()), (hardQuestion.getOperand2()));
    }

    @Test
    public void testFormatResult_Hard() {
        assertEquals(Double.parseDouble(hardQuestion.formatResult()), (hardQuestion.getResult()));
    }

    private void validateEasy(ArithmeticSubtraction question) {
        //difficulty Easy
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Easy);

        //Easy: 1 or 2 digit numbers
        double operand1 = question.getOperand1();
        double operand2 = question.getOperand2();
        double result = question.getResult();
        validateEasyOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateEasyOperands(double operand1, double operand2, double result) {
        //Easy: 1 or 2 digit numbers

        validateEasyOperands(operand1, operand2);

        //result between 0 and 98 and is operand1 - operand2
        assertTrue(result >= 0 && result <= 98 && result == operand1 - operand2);
    }

    private void validateEasyOperands(double operand1, double operand2) {
        //Easy: 1 or 2 digit numbers

        //operand1 between 1 and 99
        assertTrue(operand1 >= 1 && operand1 <= 99);

        //operand2 between 1 and operand1
        assertTrue(operand2 >= 1 && operand2 <= operand1);
    }

    private void validateMedium(ArithmeticSubtraction question) {
        //difficulty Easy
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Medium);

        //Medium: 3 or 4 digit number and a 2 - 4 digit number
        double operand1 = question.getOperand1();
        double operand2 = question.getOperand2();
        double result = question.getResult();
        validateMediumOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateMediumOperands(double operand1, double operand2, double result) {
        //Medium: 3 or 4 digit number and a 2 - 4 digit number
        validateMediumOperands(operand1, operand2);

        //result between 0 and 9989 and is operand1 - operand2
        assertTrue(result >= 0 && result <= 9989 && result == operand1 - operand2);
    }

    private void validateMediumOperands(double operand1, double operand2) {
        ///Medium: 3 or 4 digit number and a 2 - 4 digit number

        //operand1 between 100 and 9999
        assertTrue(operand1 >= 100 && operand1 <= 9999);

        //operand2 10 and operand1
        assertTrue(operand2 >= 10 && operand2 <= operand1);
    }

    private void validateHard(ArithmeticSubtraction question) {
        //difficulty Hard
        Difficulty difficulty = question.getDifficulty();
        assertEquals(difficulty, Difficulty.Hard);

        //Hard: 2 - 4 digit numbers with 2 decimal places
        double operand1 = question.getOperand1();
        double operand2 = question.getOperand2();
        double result = question.getResult();
        validateHardOperands(operand1, operand2, result);

        //validate shared details
        validate(question, operand1, operand2, result);
    }

    private void validateHardOperands(double operand1, double operand2, double result) {
        //Hard: 2 - 4 digit numbers with 2 decimal places

        validateHardOperands(operand1, operand2);

        //result between 0 and 9989 and is operand1 - operand2
        //and the result has 2 decimal places or fewer
        assertTrue(result >= 0 && result <= 9989 && result == operand1 - operand2);
        String[] resultNumberParts = (String.valueOf(operand1)).split("[.]");
        assertTrue(resultNumberParts.length == 1 || resultNumberParts[1].length() <= 2);
    }

    private void validateHardOperands(double operand1, double operand2) {
        //Hard: 2 - 4 digit numbers with 2 decimal places

        //operand1 between 10 and 9999 with up to 2 decimal places
        //and the number has 2 - 4 digits before the decimal point and 0 - 2 after
        assertTrue(operand1 >= 10 && operand1 <= 9999);
        String[] operand1NumberParts = (String.valueOf(operand1)).split("[.]");
        assertTrue(operand1NumberParts[0].length() >= 2 && operand1NumberParts[0].length() <= 4 && (operand1NumberParts.length == 1 || operand1NumberParts[1].length() <= 2));

        //operand2 between 10 and operand1 with up to 2 decimal places
        assertTrue(operand2 >= 10 && operand2 <= operand1);
        String[] operand2NumberParts = (String.valueOf(operand2)).split("[.]");
        assertTrue(operand2NumberParts[0].length() >= 2 && operand2NumberParts[0].length() <= 4 && (operand2NumberParts.length == 1 || operand2NumberParts[1].length() <= 2));
    }

    private void validate(ArithmeticSubtraction question, double operand1, double operand2, double result) {

        //operator -
        char operator = question.getOperator();
        assertEquals(operator, '-');

        //full question is in format "operand1 - operand2 = result" with one element replaced by ?
        String questionInFull = question.getQuestionInFull();
        assertEquals(questionInFull, String.format("%s %s %s = %s", question.formatOperand1(), operator, question.formatOperand2(), question.formatResult()));

        //hidden element one of "operand1", "operator", "operand2", "result"
        String hiddenElement = question.hiddenElement;
        assertTrue("operand1".equals(hiddenElement) || "operator".equals(hiddenElement) || "operand2".equals(hiddenElement) || "result".equals(hiddenElement));

        //answer equal to the value of the hidden element
        //and question is in format "operand1 + operand2 = result"
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