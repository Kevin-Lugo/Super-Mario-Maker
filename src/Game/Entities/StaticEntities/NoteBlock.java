package Game.Entities.StaticEntities;

import Main.Handler;
import Resources.Images;

public class NoteBlock extends BaseStaticEntity {

    public NoteBlock(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height,handler, Images.RaceBlock);
    }

}
