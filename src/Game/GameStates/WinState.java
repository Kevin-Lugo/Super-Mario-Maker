package Game.GameStates;

import Display.UI.UIStringButton;
import Game.Entities.DynamicEntities.Mario;
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
		uiManager.addObjects(new UIStringButton(500,550,128,64, "Quit", () -> {
			handler.getMouseManager().setUimanager(null);
			System.exit(0);
		},handler,Color.BLACK));


		uiManager.addObjects(new UIStringButton(150,550,128,64, "Title", () -> {
			handler.getMouseManager().setUimanager(null);
			handler.setIsInMap(false);
			State.setState(handler.getGame().menuState);
		},handler,Color.BLACK));

	}

	@Override
	public void tick() {
		handler.getMouseManager().setUimanager(uiManager);
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		if(Mario.wins){
			g.drawImage(Images.MarioWinState,0,0,handler.getWidth(),handler.getHeight(),null);
			uiManager.Render(g);
		}
		else {
			g.drawImage(Images.LuigiWinState,0,0,handler.getWidth(),handler.getHeight(),null);
			uiManager.Render(g);
		}
	}
}
