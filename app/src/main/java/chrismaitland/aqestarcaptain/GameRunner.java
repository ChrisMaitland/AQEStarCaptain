package chrismaitland.aqestarcaptain;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class GameRunner {

    static String [] questionTypes = {"division", "addition", "subtraction", "multiplication", "divisionExtra", "multiplicationExtra", "triangle", "rightAngleTriangle"};

    
    static int userRating = 5;
    
    static int totalQuestions = 10;
    
    static Difficulty [] questionDifficulties = new Difficulty[totalQuestions];
    
    public static void main(String[] args) {

        LinkedList<Question> questionList = new LinkedList<Question>();
        
        initialiseDifficulties();

        // setting how many questions will be generated
        for (int i = 0; i < totalQuestions; i++) {

            // randomly selecting a question type
            int rand = new Random().nextInt(questionTypes.length);

            String questionType = questionTypes[rand];
            
            Question question = null;

            // generating the selected question type
            switch(questionType) {
	            case "division" : question = new ArithmeticDivision(questionDifficulties[i]);
	                break;
	            case "addition" : question = new ArithmeticAddition(questionDifficulties[i]);
	                break;
	            case "subtraction" : question = new ArithmeticSubtraction(questionDifficulties[i]);
	                break;
	            case "multiplication" : question = new ArithmeticMultiplication(questionDifficulties[i]);
	                break;
	            case "divisionExtra" : question = new ArithmeticExtraDivision(questionDifficulties[i]);
	                break;
	            case "multiplicationExtra" : question = new ArithmeticExtraMultiplication(questionDifficulties[i]);
	                break;
	            case "triangle" : question = new AnglesTriangle(questionDifficulties[i]);
	                break;
	            case "rightAngleTriangle" : question = new AnglesRightAngledTriangle(questionDifficulties[i]);
	                break;
	            default : break;
	        }
            question.generateAnswer();
            questionList.add(question);

        }

        Scanner read = new Scanner(System.in);

        LinkedList<Question> correctList = new LinkedList<Question>();
        LinkedList<Question> incorrectList = new LinkedList<Question>();

        for (Question question: questionList) {

            System.out.println();

            System.out.println(question.getQuestion());

            String input = read.nextLine();
            if(question.checkAnswer(input)) {
                System.out.println("Correct");
                correctList.add(question);
            } else {
                System.out.println("Nope");
                incorrectList.add(question);
            }

        }

        System.out.printf("Well done!  You answered %d questions!  You got %d questions correct and %d incorrect.", correctList.size() + incorrectList.size(), correctList.size(), incorrectList.size());

        if (incorrectList.size() > 0) {
            System.out.println();
            System.out.println("These questions need a little more work:");
            for (Question question: incorrectList) {
                System.out.println();
                System.out.println(question.getQuestionInFull());
            }
        }
        read.close();

    }
    
    private static void initialiseDifficulties() {
    	
    	int numEasy = 2;
    	int numMedium = 7;
    	int numHard = 1;
    	
        switch(userRating) {
	        //1: 7 easy, 3 medium
	        case 1 : 
	        		numEasy = 7;
	        		numMedium = 3;
	        		numHard = 0;
	        	break;
		    //2: 6 easy, 4 medium
	        case 2 : 
	        	numEasy = 6;
        		numMedium = 4;
        		numHard = 0;
	        	break;
		    //3: 5 easy, 5 medium
	        case 3 : 
	        	numEasy = 5;
        		numMedium = 5;
        		numHard = 0;
	        	break;
	        //4: 3 easy, 7 medium
	        case 4 : 
	        	numEasy = 3;
        		numMedium = 7;
        		numHard = 0;
        	break;
	        //5: 2 easy, 7 medium, 1 hard
	        case 5 : 
	        	numEasy = 2;
        		numMedium = 7;
        		numHard = 1;
	        	break;
	        //6: 1 easy, 7 medium, 2 hard
	        case 6 : 
	        	numEasy = 1;
        		numMedium = 7;
        		numHard = 2;
        		break;
	        //7: 7 medium, 3 hard
	        case 7 : 
	        	numEasy = 0;
        		numMedium = 7;
        		numHard = 3;
        		break;
	        //8: 5 medium, 5 hard
	        case 8 : 
	        	numEasy = 0;
        		numMedium = 5;
        		numHard = 5;
        		break;
	        //9: 4 medium, 6 hard
	        case 9 : 
	        	numEasy = 0;
        		numMedium = 4;
        		numHard = 6;
        		break;
	        //10: 3 medium, 7 hard
	        case 10 : 
	        	numEasy = 0;
        		numMedium = 3;
        		numHard = 7;
        		break;
	        default: 
	            break;
        }
        
        int position = 0;
        
        for (int i = 0; i < numEasy; i++) {
        	questionDifficulties[position] = Difficulty.Easy;
        	position++;
        }
		for (int i = 0; i < numMedium; i++) {
			questionDifficulties[position] = Difficulty.Medium;
        	position++;
		}
		for (int i = 0; i < numHard; i++) {
			questionDifficulties[position] = Difficulty.Hard;
        	position++;
		}
        
    }

}
