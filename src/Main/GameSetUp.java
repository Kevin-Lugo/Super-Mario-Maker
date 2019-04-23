package Main;

import Display.DisplayScreen;
import Display.UI.UIPointer;
import Game.Entities.DynamicEntities.Mario;
import Game.Entities.DynamicEntities.Player;
import Game.Entities.StaticEntities.BreakBlock;
import Game.GameStates.GameOverState;
import Game.GameStates.GameState;
import Game.GameStates.MenuState;
import Game.GameStates.PauseState;
import Game.GameStates.State;
import Game.World.Map;
import Game.World.MapBuilder;
import Input.Camera;
import Input.KeyManager;
import Input.MouseManager;
import Resources.Images;
import Resources.MusicHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by AlexVR on 7/1/2018.
 */

public class GameSetUp implements Runnable {
	private static int Screencount = 0;
	public DisplayScreen display;
	public DisplayScreen display2;
	public String title;

	private boolean running = false;
	private Thread thread;
	public static boolean threadB;

	private BufferStrategy bs;
	private BufferStrategy bs2;

	private Graphics g;
	private Graphics gL;

	public UIPointer pointer;

	// Input
	public KeyManager keyManager;
	public MouseManager mouseManager;
	public MouseManager initialmouseManager;

	// Handler
	private Handler handler;

	// States
	public State gameState;
	public State menuState;
	public State pauseState;
	public State gameOverState;

	// Res.music
	private MusicHandler musicHandler;

	public GameSetUp(String title, Handler handler) {
		this.handler = handler;
		this.title = title;
		threadB = false;

		keyManager = new KeyManager();
		mouseManager = new MouseManager();
		initialmouseManager = mouseManager;
		musicHandler = new MusicHandler(handler);
		handler.setCamera(new Camera());
		handler.setCamera2(new Camera());
	}

	private void init() {

		display = new DisplayScreen(title, handler.width, handler.height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);

		Images img = new Images();

		musicHandler.restartBackground();

		gameState = new GameState(handler);
		menuState = new MenuState(handler);
		pauseState = new PauseState(handler);
		gameOverState = new GameOverState(handler);

		State.setState(menuState);
	}


	public void reStart() {
		gameState = new GameState(handler);
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		// this runs the run method in this class
		thread = new Thread(this);
		thread.start();
	}

	public void run() {

		// initiallizes everything in order to run without breaking
		init();
		// Screen2();

		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (running) {
			// makes sure the games runs smoothly at 60 FPS
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				// re-renders and ticks the game around 60 times per second
				tick();
				render();
				// render2();
				ticks++;
				delta--;
			}
			if (timer >= 1000000000) {
				ticks = 0;
				timer = 0;
			}
		}

		stop();

	}

	private void tick() {
		// checks for key types and manages them
		keyManager.tick();

		if (musicHandler.ended()) {
			musicHandler.restartBackground();
		}

		if (MenuState.multiP && Screencount == 0) {
			display2 = new DisplayScreen("Player 2", handler.width, handler.height);
			display2.getFrame().setLocation(handler.width, handler.getHeight()/2 -300);
			handler.getGame().display.getFrame().setLocation(0, handler.getHeight()/2 -300);

			Screencount++;
		}

		// game states are the menus
		if (State.getState() != null) {
			State.getState().tick();
		}

		if (handler.isInMap()) {
			if (!MenuState.multiP) {
				updateCamera();
			} else {
				updateCamera();
				updateCamera2();
			}
		}

	}

	private void updateCamera() {
		Player mario = handler.getMario();
		double marioVelocityX = mario.getVelX();
		double marioVelocityY = mario.getVelY();
		double shiftAmount = 0;
		double shiftAmountY = 0;

		if (marioVelocityX > 0 && mario.getX() - 2 * (handler.getWidth() / 3) > handler.getCamera().getX()) {
			shiftAmount = marioVelocityX;
		}
		if (marioVelocityX < 0
				&& mario.getX() + 2 * (handler.getWidth() / 3) < handler.getCamera().getX() + handler.width) {
			shiftAmount = marioVelocityX;
		}
		if (marioVelocityY > 0 && mario.getY() - 2 * (handler.getHeight() / 3) > handler.getCamera().getY()) {
			shiftAmountY = marioVelocityY;
		}
		if (marioVelocityX < 0
				&& mario.getY() + 2 * (handler.getHeight() / 3) < handler.getCamera().getY() + handler.height) {
			shiftAmountY = -marioVelocityY;
		}
		handler.getCamera().moveCam(shiftAmount, shiftAmountY);
	}

	private void updateCamera2() {
		Player luigi = handler.getLuigi();
		double luigiVelocityX = luigi.getVelX();
		double luigiVelocityY = luigi.getVelY();
		double shiftAmount = 0;
		double shiftAmountY = 0;

		if (luigiVelocityX > 0 && luigi.getX() - 2 * (handler.getWidth() / 3) > handler.getCamera2().getX()) {
			shiftAmount = luigiVelocityX;
		}
		if (luigiVelocityX < 0
				&& luigi.getX() + 2 * (handler.getWidth() / 3) < handler.getCamera2().getX() + handler.width) {
			shiftAmount = luigiVelocityX;
		}
		if (luigiVelocityY > 0 && luigi.getY() - 2 * (handler.getHeight() / 3) > handler.getCamera2().getY()) {
			shiftAmountY = luigiVelocityY;
		}
		if (luigiVelocityX < 0
				&& luigi.getY() + 2 * (handler.getHeight() / 3) < handler.getCamera2().getY() + handler.height) {
			shiftAmountY = -luigiVelocityY;
		}
		handler.getCamera2().moveCam(shiftAmount, shiftAmountY);
	}

	private void render() {
		if (!MenuState.multiP) {
			bs = display.getCanvas().getBufferStrategy();

			if (bs == null) {
				display.getCanvas().createBufferStrategy(3);
				return;
			}
			g = bs.getDrawGraphics();
			g.clearRect(0, 0, handler.width, handler.height);
			Graphics2D g2 = (Graphics2D) g.create();
			if (State.getState() != null) {
				State.getState().render(g);
			}
			bs.show();
			g.dispose();
		} else {
			bs = display.getCanvas().getBufferStrategy();
			bs2 = display2.getCanvas().getBufferStrategy();

			if (bs == null || bs2 == null) {
				display.getCanvas().createBufferStrategy(3);
				display2.getCanvas().createBufferStrategy(3);
				return;
			}
			g = bs.getDrawGraphics();
			gL = bs2.getDrawGraphics();
			// Clear Screen
			g.clearRect(0, 0, handler.width, handler.height);
			gL.clearRect(0, 0, handler.width, handler.height);

			// Draw Here!
			Graphics2D g2 = (Graphics2D) g.create();
			// Draw Here!
			Graphics2D gL2 = (Graphics2D) gL.create();

			if (State.getState() != null) {
				State.getState().render(g);
				if (State.getState() instanceof GameState && MenuState.multiP) {
					handler.getMap().drawMap2(gL2);
				}
			}

			// End Drawing!
			bs.show();
			bs2.show();
			g.dispose();
			gL.dispose();
		}



	}

	public Map getMap() {
		Map map = new Map(this.handler);
		Images.makeMap(0, MapBuilder.pixelMultiplier, 31, 200, map, this.handler);
		for (int i = 195; i < 200; i++) {
			map.addBlock(new BreakBlock(0, i * MapBuilder.pixelMultiplier, 48, 48, this.handler));
			map.addBlock(new BreakBlock(30 * MapBuilder.pixelMultiplier, i * MapBuilder.pixelMultiplier, 48, 48,
					this.handler));
		}
		Mario mario = new Mario(24 * MapBuilder.pixelMultiplier, 196 * MapBuilder.pixelMultiplier, 48, 48,
				this.handler);
		map.addEnemy(mario);
		map.addEnemy(pointer);
		threadB = true;
		return map;
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public MusicHandler getMusicHandler() {
		return musicHandler;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

}
