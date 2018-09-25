package chrismaitland.aqestarcaptain;

import java.util.Random;

/**
 * @author Chris
 * As a User I can see an automatically generated English question,
 */
public class English extends Question {
	
    // creating variables
    String sentence, subjectNoun, verb, predicateVerb, objectNoun, adjective, adverb, target;
    String[] subjectNouns = {"Chris", "Caroline", "Adam", "Jessica", "The boy", "The girl", "The man", "The woman"};
    String[] verbs = {"ran", "jumped", "shouted"};
    String[][] predicateVerbsAndObjectNouns = new String[][]{{"threw", "ball"},{"built", "house"},{"drove","car"}};
    String[] adjectives = {"red", "big", "blue", "small"};
    String[] adverbs = {"slowly", "quickly"};
    String[] components;

    /**
     * Constructor with arg
     */
    public English(Difficulty difficulty) {
        super(difficulty);
        switch (difficulty) {
	        case Easy: 
	        	subjectNoun = selectRandom(subjectNouns);
	        	verb = selectRandom(verbs);
	        	sentence = String.format("%s %s.", subjectNoun, verb);
	        	components = new String[]{"subject noun", "verb"};
	        	break;
	        case Medium: 
	        	subjectNoun = selectRandom(subjectNouns);
	        	String [] predicateVerbAndObjectNounMed = selectRandom(predicateVerbsAndObjectNouns);
	        	predicateVerb = predicateVerbAndObjectNounMed[0];
	        	objectNoun = predicateVerbAndObjectNounMed[1];
	        	sentence = String.format("%s %s the %s.", subjectNoun, predicateVerb, objectNoun);
	        	components = new String[]{"subject noun", "predicate verb", "object noun"};
	        	break;
	        case Hard: 
	        	subjectNoun = selectRandom(subjectNouns);
	        	String [] predicateVerbAndObjectNounHard = selectRandom(predicateVerbsAndObjectNouns);
	        	predicateVerb = predicateVerbAndObjectNounHard[0];
	        	objectNoun = predicateVerbAndObjectNounHard[1];
	        	adjective = selectRandom(adjectives);
	        	adverb = selectRandom(adverbs);
	        	sentence = String.format("%s %s the %s %s %s.", subjectNoun, predicateVerb, adjective, objectNoun, adverb);
	        	components = new String[]{"subject noun", "predicate verb", "adjective", "object noun", "adverb"};
	        	break;
	        default: 
	        	subjectNoun = selectRandom(subjectNouns);
	        	verb = selectRandom(verbs);
	        	sentence = String.format("%s %s.", subjectNoun, verb);
	        	components = new String[]{"subject noun", "verb"};
	        	break;
        }
	
    }

    /**
     * Default Constructor
     */
    public English () {
    	subjectNoun = selectRandom(subjectNouns);
    	verb = selectRandom(verbs);
    	sentence = String.format("%s %s", subjectNoun, verb);
    	components = new String[]{"subject noun", "verb"};

    }
    
    private String selectRandom(String[] array) {
    	int rand = new Random().nextInt(array.length);
    	return array[rand];
    }
    
    private String[] selectRandom(String[][] array) {
    	int rand = new Random().nextInt(array.length);
    	return array[rand];
    }

    
	public void generateAnswer() {
		target = selectRandom(components);
		switch (target) {
			case "subject Noun":
				setAnswer(subjectNoun);
				break;
			case "verb":
				setAnswer(verb);
				break;
			case "predicate verb":
				setAnswer(predicateVerb);
				break;
			case "object noun":
				setAnswer(objectNoun);
				break;
			case "adjective":
				setAnswer(adjective);
				break;
			case "adverb":
				setAnswer(adverb);
				break;
			default:
				setAnswer(subjectNoun);
				break;
		}
	}
    
    public String getQuestion() {
        String question = String.format("What is the %s in the following sentence: \"%s\"?", target, sentence);
        return question;
    }

    public String getQuestionInFull() {
        return String.format("The %s in the sentence: \"%s\" is %s.", target, sentence, getAnswer());
    }
    
    @Override
    public boolean checkAnswer(String input) {
    	if (input.toLowerCase().equals(getAnswer().toLowerCase())) {
    		return true;
    	} else {
    		String ans = getAnswer().trim();
    		String in = input.trim();
	    	if (ans.toLowerCase().startsWith("the ")) {
	    		ans = getAnswer().toLowerCase().substring(4);
	    	}
	    	if (input.toLowerCase().startsWith("the ")) {
	    		in = in.toLowerCase().substring(4);
	    	}
            return in.equals(ans);
    	}
    }

	/*
	 * Constructor to allow for replaying questions
	 * @param difficulty
	 * @param the question
	 * @param the answer
	 */
	public English(Difficulty difficulty, String question, String answer) {
		super(difficulty);
		this.target = question.split(" ")[3];
		this.sentence = question.split("\"")[1];
		this.setAnswer(answer);
	}

}
