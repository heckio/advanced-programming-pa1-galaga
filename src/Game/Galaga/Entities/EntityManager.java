package Game.Galaga.Entities;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EntityManager {


    public ArrayList<BaseEntity> entities;
    public ArrayList<BaseEntity> entitie;
    public PlayerShip playerShip;

    public EntityManager(PlayerShip playerShip) {
        entities = new ArrayList<>();
        entitie = new ArrayList<>();
        this.playerShip = playerShip;
    }

    public void tick(){
        for(BaseEntity mi :entitie){
            entities.add(mi);
        }
        entitie.clear();
        playerShip.tick();
        ArrayList<BaseEntity> toRemove = new ArrayList<>();
        for (BaseEntity entity: entities){
            if (entity.remove){
                toRemove.add(entity);
                continue;
            }
            entity.tick();
            if (entity.bounds.intersects(playerShip.bounds)){
                playerShip.damage(entity);
            }
        }
        for (BaseEntity toErase:toRemove){
            entities.remove(toErase);
        }

    }

    public void render(Graphics g){
        for (BaseEntity entity: entities){
            entity.render(g);
        }
        playerShip.render(g);

    }

}
