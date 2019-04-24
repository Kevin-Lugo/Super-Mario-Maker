package Game.GameStates;

import Display.DisplayScreen;
import Display.UI.UIStringButton;
import Game.World.MapBuilder;
import Input.KeyManager;
import Input.MouseManager;
import Main.Handler;
import Main.GameSetUp;
import Resources.Images;
import Display.UI.UIAnimationButton;
import Display.UI.UIImageButton;
import Display.UI.UIManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SelectionState extends State {

	public static boolean singleP = false;
	public static boolean multiP = false;

	public UIManager uiManager;
	private int background;
	private String mode = "Selection";

	public int GridWidthPixelCount, GridHeightPixelCount, DiplayHeight, DisplayWidth;
	public int GridPixelsize;
	int colorSelected = MapBuilder.boundBlock;
	Color[][] blocks;
	// Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	private boolean clicked = true;

	public SelectionState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUimanager(uiManager);
		background = new Random().nextInt(9);

		DisplayWidth = (handler.getWidth()) + (handler.getWidth() / 2);
		DiplayHeight = handler.getHeight();
		GridPixelsize = 20;
		GridHeightPixelCount = DiplayHeight / GridPixelsize;
		GridWidthPixelCount = DisplayWidth / GridPixelsize;
		blocks = new Color[GridWidthPixelCount][GridHeightPixelCount];
		keyManager = handler.getGame().keyManager;
		mouseManager = new MouseManager();
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
		handler.getMouseManager().setUimanager(uiManager);
		uiManager.tick();
		
		uiManager.addObjects(new UIStringButton(handler.getWidth() / 2 - 64,
				(handler.getHeight() / 2) + (handler.getHeight() / 10) - (64), 128, 64, "Single Player", () -> {
					if (!handler.isInMap()) {
						mode = "Selecting";
						singleP = true;
						State.setState(handler.getGame().menuState);
					}

				}, handler, Color.BLACK));

		uiManager.addObjects(new UIStringButton(handler.getWidth() / 2 - 64,
				(handler.getHeight() / 2) + (handler.getHeight() / 10) + (64), 128, 64, "Multiplayer", () -> {
					if (!handler.isInMap()) {
						mode = "Selecting";
						multiP = true;
						State.setState(handler.getGame().menuState);
					}

				}, handler, Color.BLACK));

		if (mode.equals("Selecting") && handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)
				&& (!handler.isInMap())) {
			mode = "Menu";
			uiManager = new UIManager(handler);
			handler.getMouseManager().setUimanager(uiManager);
			uiManager.addObjects(new UIImageButton(handler.getWidth() / 2 - 64,
					handler.getHeight() / 2 + (handler.getHeight() / 8), 128, 64, Images.butstart, () -> {
						mode = "Select";
					}));
		}
	

	}

	@Override
	public void render(Graphics g) {

		g.setColor(Color.GREEN);
		g.drawImage(Images.backgrounds[background], 0, 0, handler.getWidth(), handler.getHeight(), null);
		g.drawImage(Images.title, 0, 0, handler.getWidth(), handler.getHeight(), null);
		uiManager.Render(g);

	}

}
