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
		super.tick();
		anim.tick();
	}
	
	public void kill() {
		ded = true;
	}
}
