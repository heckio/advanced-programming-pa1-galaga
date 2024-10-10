package Game.Galaga.Entities;

import Game.GameStates.GameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class EnemyGoei extends BaseEntity {
    int row, col;//row 1-2, col 1-6
    boolean justSpawned = true, attacking = false, positioned = false, hit = false, centered = false;
    Animation idle, turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX, formationY, speed, centerCoolDown = 60;
    int timeAlive = 0, attackCoolDown = random.nextInt(20)*60,direction;

    public EnemyGoei(int x, int y, int width, int height, Handler handler, int row, int col) {
        super(x, y, width, height, Images.galagaEnemyGoei[0], handler);
        this.row = row;
        this.col = col;
        BufferedImage[] idleAnimList = new BufferedImage[2];
        idleAnimList[0] = Images.galagaEnemyGoei[0];
        idleAnimList[1] = Images.galagaEnemyGoei[1];
        idle = new Animation(512, idleAnimList);
        turn90Left = new Animation(128, Images.galagaEnemyGoei);
        spawn();
        speed = 4;
        formationX = (handler.getWidth() / 4) + (col * ((handler.getWidth() / 2) / 8)) + 8;
        formationY = (row * (handler.getHeight() / 10)) + 8;
    }
    private void spawn() {
        spawnPos = random.nextInt(3);
        switch (spawnPos) {
            case 0://left
                x = (handler.getWidth() / 4) - width;
                y = random.nextInt(handler.getHeight() - handler.getHeight() / 8);
                break;
            case 1://top
                x = random.nextInt((handler.getWidth() - handler.getWidth() / 2)) + handler.getWidth() / 4;
                y = -height;
                break;
            case 2://right
                x = (handler.getWidth() / 2) + width + (handler.getWidth() / 4);
                y = random.nextInt(handler.getHeight() - handler.getHeight() / 8);
                break;
            case 3://down
                x = random.nextInt((handler.getWidth() / 2)) + handler.getWidth() / 4;
                y = handler.getHeight() + height;
                break;
        }
        bounds.x = x;
        bounds.y = y;
    }

    @Override
    public void tick() {
        //boolean spwn = false;
        super.tick();
        idle.tick();

        if (hit) {
            if (enemyDeath.end) {
                handler.getScoreManager().setGalagaCurrentScore(handler.getScoreManager().getGalagaCurrentScore()+100);
                remove = true;
                return;
            }
            handler.getGalagaState().deathrow=this.row;      //sends coordinates of enemy goei and removes it from my array of positions
            handler.getGalagaState().deathcol=this.col;
            handler.getGalagaState().removegoei=true;
            enemyDeath.tick();
        }
        if (justSpawned&&!hit) {
            timeAlive++;
            if (!centered && Point.distance(x, y, handler.getWidth() / 2, handler.getHeight() / 2) > speed+5) {//reach center of screen
                switch (spawnPos) {
                    case 0://left
                        x += speed;
                        if (Point.distance(x, y, x, handler.getHeight() / 2) > speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else if(y < handler.getHeight() / 2){
                                y += speed;
                            }
                        }
                        break;
                    case 1://top
                        y += speed;
                        if (Point.distance(x, y, handler.getWidth() / 2, y) > speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else if(x <= handler.getWidth() / 2){
                                x += speed;
                            }
                        }
                        break;
                    case 2://right
                        x -= speed;
                        if (Point.distance(x, y, x, handler.getHeight() / 2) > speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else if(y <= handler.getHeight() / 2){
                                y += speed;
                            }
                        }
                        break;
                    case 3://down
                        y -= speed;
                        if (Point.distance(x, y, handler.getWidth() / 2, y) > speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else if(x <= handler.getWidth() / 2){
                                x += speed;
                            }
                        }
                        break;
                }
                if (timeAlive >= 60 * 60 * 2) {
                    //more than 2 minutes in this state then die
                    //60 ticks in a second, times 60 is a minute, times 2 is a minute
                    damage(new PlayerLaser(0, 0, 0, 0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
                }

            } else {//move to formation
                if (!centered) {
                    centered = true;
                    timeAlive = 0;
                }
                if (centerCoolDown <= 0) {
                    if (Point.distance(x, y, formationX, formationY)-2 > speed) {//reach formation spot
                        if (Math.abs(y - formationY) > 6) {
                            y -= speed;
                        }
                        if (Point.distance(x, y, formationX, y) > speed / 2) {
                            if (x > formationX) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                    } else {
                        positioned = true;
                        justSpawned = false;
                    }
                } else {
                    centerCoolDown--;
                }
                if (timeAlive >= 60 * 60 * 2) {
                    //more than 2 minutes in this state then die
                    //60 ticks in a second, times 60 is a minute, times 2 is a minute
                    damage(new PlayerLaser(0, 0, 0, 0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
                }
            }
        } else if (positioned) {

        } else if (attacking) {

        }

        if(positioned) {
            if(attackCoolDown < 0){//  set cooldown
                attacking=true;
                positioned = false;
                direction = random.nextInt(3);
            }
            else{
                attackCoolDown--;
            }
        }

        if(attacking){
            handler.getGalagaState().entityManager.entitie.add(new EnemyLaser(x,y,width/5,height/2,Images.galagaPlayerLaser,handler,handler.getGalagaState().entityManager));
            attacking=false;
            attackCoolDown = random.nextInt(20)*60;
            positioned=true;

        }
        bounds.x=x;
        bounds.y=y;
    }

    @Override
    public void render(Graphics g) {

        ((Graphics2D)g).draw(new Rectangle(formationX,formationY,32,32));
        if (arena.contains(bounds)) {
            if (hit){
                g.drawImage(enemyDeath.getCurrentFrame(), x, y, width, height, null);
            }else{
                g.drawImage(idle.getCurrentFrame(), x, y, width, height, null);
            }

        }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof PlayerLaser){
            hit=true;
            handler.getMusicHandler().playEffect("explosion.wav");
            damageSource.remove = true;
        }
    }
}
