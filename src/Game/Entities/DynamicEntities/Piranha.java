package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;

public class Piranha extends BaseDynamicEntity {

	public Animation anim;

	public Piranha(int x, int y, int width, int height, Handler handler) {
		super(x, y + 6, width, height, handler, Images.piranha[0]);
		anim = new Animation(300,Images.piranha);
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
	
	public void kill() {
		ded = true;
	}
}
