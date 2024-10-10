package Game.GameStates;

import Game.Galaga.Entities.*;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;




/**
 * Created by AlexVR on 1/24/2020.
 */

public class GalagaState extends State {

    public EntityManager entityManager;
    public String Mode = "Menu";
    private Animation titleAnimation;
    public int selectPlayers = 1;
    public int startCooldown = 60*7;//seven seconds for the music to finish
    public boolean spwnbee = false;
    public boolean spwngoei = false;
    public boolean esponeame = false;
    public boolean removebee = false,spawnall=false;
    public boolean removegoei = false,attack=false;
    public int deathrow,deathcol;
    boolean rowThird[] = new boolean[8];
    boolean rowFourth[] = new boolean[8];
    boolean rowSecond[] = new boolean[8];
    boolean rowFirst[] = new boolean[8];
    int spawncooldown = 5*60;
    int enemiesspawned =0;
    int level1enemies = 32;


    public GalagaState(Handler handler){
        super(handler);
        refresh();
        entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));
        titleAnimation = new Animation(256,Images.galagaLogo);
    }


    @Override
    public void tick() {
        Random random;
        random = new Random();
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
            spwnbee=true;
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_O)){
            spwngoei=true;
        }
        if (enemiesspawned==level1enemies){
            Mode = "Menu";
        }
        if(spawncooldown<0&&esponeame){
            int pio = random.nextInt(2);
            spawncooldown=3*60;
            enemiesspawned++;
            switch(pio){
            case 0:
                spwnbee=true;
                break;
            case 1:
                spwngoei=true;
                break;
            }
        }else{
            spawncooldown--;
        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)){
            spawnall=true;
            for (int i = 0; i <8 ; i++) {
                if(rowFirst[i]){
                    System.out.print("o");
                }else{
                    System.out.print("+");
                }
                System.out.print(" ");
            }
            System.out.println(" ");
            for (int i = 0; i <8 ; i++) {
                if(rowSecond[i]){
                    System.out.print("o");
                }else{
                    System.out.print("+");
                }
                System.out.print(" ");
            }
            System.out.println(" ");
            for (int i = 0; i <8 ; i++) {
                if(rowThird[i]){
                    System.out.print("O");
                }else{
                    System.out.print("*");
                }
                System.out.print(" ");
            }
            System.out.println(" ");
            for (int i = 0; i <8 ; i++) {
                if(rowFourth[i]){
                    System.out.print("O");
                }else{
                    System.out.print("*");
                }
                System.out.print(" ");
            }
            System.out.println(" ");
        }


        //System.out.println(attack);



        if(removebee) {//for bees
            if (deathrow == 3 && rowThird[deathcol] == true) {
                rowThird[deathcol] = false;
            } else if (deathrow == 4 && rowFourth[deathcol] == true) {
                rowFourth[deathcol] = false;
            }
            removebee = false;
        }

        if(removegoei){//for goeis
            //int roww = (deathcol - 8) / (handler.getHeight() / 10);
            //int coll = (deathrow - (handler.getWidth() / 4) - 8) / ((handler.getWidth() / 2) / 8);
            //System.out.println("this is " + rowww + "this col " + colll);
            if (deathrow == 1 && rowFirst[deathcol] == true) {
                rowFirst[deathcol] = false;
            } else if (deathrow == 2 && rowSecond[deathcol] == true) {
                rowSecond[deathcol] = false;
            }
            removegoei = false;
        }

        if(spwnbee){
            if(!areAllTrue(rowFourth)||!areAllTrue(rowThird)) {//is there space in the formation
                GetFormationBee();
            }
            else{
                esponeame=false;
            }
            spwnbee = false;
        }

        if(spwngoei){
            if(!areAllTrue(rowFirst)||!areAllTrue(rowSecond)) {//is there space in the formation
                GetFormationGoei();
            }
            else {esponeame=false;}
            spwngoei = false;
        }

        if (Mode.equals("Stage")){
            if (startCooldown<=0) {
                entityManager.tick();
                //spawnall=true;
            }else{
                startCooldown--;
            }

        }else{
            titleAnimation.tick();
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
                selectPlayers=1;
            }else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
                selectPlayers=2;
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Stage";
                handler.getMusicHandler().playEffect("Galaga.wav");
            }


        }

    }
    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }


    private void GetFormationBee() {
        Random random;
        random = new Random();
        int row = random.nextInt(2) + 3;
        int col = random.nextInt(8);
        switch (row) {
            case 3:
                if (rowThird[col] == false) {
                    rowThird[col] = true;
                    handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0, 32, 32, handler, row, col));
                }
                else{
                    GetFormationBee();
                }
                break;
            case 4:
                if (rowFourth[col] == false) {
                    rowFourth[col] = true;
                    handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0, 32, 32, handler, row, col));
                }
                else{
                    GetFormationBee();
                }
                break;
        }


    }
    private void GetFormationGoei() {
        Random random;
        random = new Random();
        int row = random.nextInt(2) + 1;
        int col = random.nextInt(8);
        switch (row) {
            case 1:
                if (rowFirst[col] == false) {
                    rowFirst[col] = true;
                    handler.getGalagaState().entityManager.entities.add(new EnemyGoei(0, 0, 32, 32, handler, row, col));
                }
                else{
                    GetFormationGoei();
                }
                break;
            case 2:
                if (rowSecond[col] == false) {
                    rowSecond[col] = true;
                    handler.getGalagaState().entityManager.entities.add(new EnemyGoei(0, 0, 32, 32, handler, row, col));
                }
                else{
                    GetFormationGoei();
                }
                break;
        }


    }
    @Override
    public void render(Graphics g) {
        Color light_blue = new Color(51 ,153,255);
        g.setColor(light_blue);
        g.fillRect(0,0,handler.getWidth(),handler.getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(handler.getWidth()/4,0,handler.getWidth()/2,handler.getHeight());
        Random random = new Random(System.nanoTime());

        for (int j = 1;j < random.nextInt(15)+60;j++) {
            switch (random.nextInt(6)) {
                case 0:
                    g.setColor(Color.RED);
                    break;
                case 1:
                    g.setColor(Color.BLUE);
                    break;
                case 2:
                    g.setColor(Color.YELLOW);
                    break;
                case 3:
                    g.setColor(Color.GREEN);
                    break;
                case 4:
                    g.setColor(Color.MAGENTA);
                    break;
                case 5:
                    g.setColor(Color.WHITE);
            }
            int randX = random.nextInt(handler.getWidth() - handler.getWidth() / 2) + handler.getWidth() / 4;
            int randY = random.nextInt(handler.getHeight());
            g.fillRect(randX, randY, 2, 2);

        }
        if (Mode.equals("Stage")) {
            g.setColor(Color.magenta);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 62));
            g.drawString("HIGH",handler.getWidth()-handler.getWidth()/4,handler.getHeight()/16);    //high score
            g.drawString("SCORE",handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/8);    //high score

            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/5);      //draws high score value


            g.setColor(Color.RED);
            g.drawString("score",handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/3); //current score
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaCurrentScore()),(handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48),handler.getHeight()/2);    //draws current score
            //draws current score value
            for (int i = 0; i< entityManager.playerShip.getHealth();i++) {//spawns player ship
                g.drawImage(Images.galagaPlayer[0], (handler.getWidth() - handler.getWidth() / 4 + handler.getWidth() / 48) + ((entityManager.playerShip.width*2)*i), handler.getHeight()-handler.getHeight()/4, handler.getWidth() / 18, handler.getHeight() / 18, null);
            }
            if (startCooldown<=0) {
                entityManager.render(g);
                esponeame=true;
            }else{
                g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
                g.setColor(Color.WHITE);
                g.drawString("Start",handler.getWidth()/2-handler.getWidth()/18,handler.getHeight()/2);
            }
        }else{

            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));

            g.setColor(Color.RED);
            g.drawString("HIGH-SCORE:",handler.getWidth()/2-handler.getWidth()/18,32);

            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()/2-32,64);

            g.drawImage(titleAnimation.getCurrentFrame(),handler.getWidth()/2-(handler.getWidth()/12),handler.getHeight()/2-handler.getHeight()/3,handler.getWidth()/6,handler.getHeight()/7,null);

            g.drawImage(Images.galagaCopyright,handler.getWidth()/2-(handler.getWidth()/8),handler.getHeight()/2 + handler.getHeight()/3,handler.getWidth()/4,handler.getHeight()/8,null);

            g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
            g.drawString("1   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2);
            g.drawString("2   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2+handler.getHeight()/12);
            if (selectPlayers == 1){
                g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2-handler.getHeight()/32,32,32,null);
            }else{
                g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2+handler.getHeight()/18,32,32,null);
            }


        }
    }

    @Override
    public void refresh() {



    }
}
