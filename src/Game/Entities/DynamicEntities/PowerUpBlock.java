package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;


public class PowerUpBlock extends BaseDynamicEntity {

	public Animation anim;

	public PowerUpBlock(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.powerUpBlock[2]);
		anim = new Animation(250,Images.powerUpBlock);
	}

	@Override
	public void tick(){
		super.tick();
		anim.tick();
	}
}
