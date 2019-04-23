package Game.Entities.StaticEntities;

import Main.Handler;
import Resources.Images;

public class Pipe extends BaseStaticEntity {
	
	public Pipe(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height,handler, Images.pipe);
    }

}
