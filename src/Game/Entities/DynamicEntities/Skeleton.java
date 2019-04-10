package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;

public class Skeleton extends BaseDynamicEntity {

    public Animation anim;
    public Animation dies;

    public Skeleton(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.skeleton[0]);
        anim = new Animation(160,Images.skeleton);
        dies = new Animation(160,Images.skeletonDies);
    }

    @Override
    public void tick(){
        if(!ded && dedCounter==0) {
            super.tick();
            anim.tick();
            if (falling) {
                y = (int) (y + velY);
                velY = velY + gravityAcc;
                checkFalling();
            }
            checkHorizontal();
            move();
        }else if(ded&&dedCounter==0){
            y++;
            height--;
            setDimension(new Dimension(width,height));
            if (height==0){
                dedCounter=1;
                y=-10000;
            }
        }
    }

    @Override
    public void kill() {
        sprite = Images.skeletonDies[1];
        ded=true;
    }
}
