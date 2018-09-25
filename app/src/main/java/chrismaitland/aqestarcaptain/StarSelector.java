package chrismaitland.aqestarcaptain;

import java.util.Random;

public class StarSelector {

    private static Stars [] STARLIST = new Stars [100];

    static {
        Stars [] stars = Stars.values();

        int index = 0;

        for (Stars star : stars) {
            int starWeight = getStarWeight(star);
            for (int i = 0; i < starWeight; i++) {
                STARLIST[index] = star;
                index++;
            }
        }
    }

    public StarSelector() {

    }

    /**
     * Adds a value to each star type
     * The higher the value the higher the chance it will be selected
     * Total value of stars = 100
     * @param star
     * @return
     */
    private static int getStarWeight(Stars star) {
        int starWeight = 0;
        switch (star) {
            case Yellow: starWeight = 15;
                break;
            case Blue: starWeight = 15;
                break;
            case Red: starWeight = 15;
                break;
            case Green: starWeight = 15;
                break;
            case Purple: starWeight = 10;
                break;
            case Orange: starWeight = 10;
                break;
            case Pink: starWeight = 10;
                break;
            case Sparkle: starWeight = 5;
                break;
            case Rainbow: starWeight = 5;
                break;
            default: starWeight = 0;
                break;
        }
        return starWeight;
    }

    // Selects random value between 1 and 100 and returns the chosen star
    public Stars getRandomStar() {
        Random randomStar = new Random();
        return STARLIST[randomStar.nextInt(100)];
    }

}
