package Game.GameStates;

import Display.UI.UIStringButton;
import Main.Handler;
import Resources.Images;
import Display.UI.UIManager;

import java.awt.*;


/**
 * Created by AlexVR on 7/1/2018.
 */
public class WinState extends State {

    private UIManager uiManager;

    public WinState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.addObjects(new UIStringButton(400,350,128,64, "Quit", () -> {
            handler.getMouseManager().setUimanager(null);
            System.exit(0);
        },handler,Color.WHITE));


        uiManager.addObjects(new UIStringButton(256,350,128,64, "Title", () -> {
            handler.getMouseManager().setUimanager(null);
            handler.setIsInMap(false);
            State.setState(handler.getGame().menuState);
        },handler,Color.WHITE));

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
    }

    @Override
    public void render(Graphics g) {
    	if(handler.getMario().wins) {
    		g.drawImage(Images.MarioWinState,0,0,handler.getWidth(),handler.getHeight(),null);
    	}
        g.drawImage(Images.LuigiWinState,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
    }
}
