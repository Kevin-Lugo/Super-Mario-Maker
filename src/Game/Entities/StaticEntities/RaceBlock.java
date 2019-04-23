package Game.Entities.StaticEntities;

import Main.Handler;
import Resources.Images;

public class RaceBlock extends BaseStaticEntity{
	public RaceBlock ( int x, int y, int width, int height, Handler handler) {
		  super(x, y, width, height,handler, Images.boundBlock);
	}

}
