package Resources;

import Main.Handler;
import jdk.internal.org.objectweb.asm.commons.Method;

/**
 * Created by AlexVR on 1/24/2020.
 */

public class ScoreManager {

    Handler handler;

    //Galaga
    private int galagaHighScore=0;
    private int galagaCurrentScore=0;

    public ScoreManager(Handler handler) {
        this.handler = handler;
    }

    public int getGalagaHighScore(){
    	// Creo que puse para que cambie el HighScore, pero en realidad no se si esta bien.
    	// Tampoco lo puedo probar por que no he podido hacer lo del current score.
    	if (galagaCurrentScore > galagaHighScore) {
    		galagaHighScore = galagaCurrentScore;
    	}
    	 
        return galagaHighScore;
    }

    public void setGalagaHighScore(int galagaHighScore) {
        this.galagaHighScore = galagaHighScore;
    }

    public int getGalagaCurrentScore() {
        return galagaCurrentScore;
    }

    public void setGalagaCurrentScore(int galagaCurrentScore) {
        this.galagaCurrentScore = galagaCurrentScore;
    }

    public void addGalagaCurrentScore(int galagaCurrentScore) {
        this.galagaCurrentScore += galagaCurrentScore;
    }

    public void removeGalagaCurrentScore(int galagaCurrentScore) {
        this.galagaCurrentScore -= galagaCurrentScore;
    }
}
