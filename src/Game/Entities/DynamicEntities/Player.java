package Game.Entities.DynamicEntities;

import Game.Entities.StaticEntities.BaseStaticEntity;
import Game.Entities.StaticEntities.BoundBlock;
import Game.Entities.StaticEntities.NoteBlock;
import Game.Entities.StaticEntities.RaceBlock;
import Game.GameStates.MenuState;
import Game.GameStates.SelectionState;
import Game.GameStates.State;
import Main.Handler;
import Resources.Animation;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends BaseDynamicEntity {
	private boolean hit = false;
	public boolean grabbed = false;
	private int jumpcounter = 0;

	protected double velX, velY;

	public String facing = "Left";
	public boolean moving = false;
	public Animation playerSmallLeftAnimation, playerSmallRightAnimation, playerBigLeftWalkAnimation,
	playerBigRightWalkAnimation, playerBigLeftRunAnimation, playerBigRightRunAnimation;
	public boolean falling = true, jumping = false, isBig = false, running = false, changeDirrection = false;
	public double gravityAcc = 0.38;
	int changeDirectionCounter = 0;

	public Player(int x, int y, int width, int height, Handler handler, BufferedImage sprite, Animation PSLA,
			Animation PSRA, Animation PBLWA, Animation PBRWA, Animation PBLRA, Animation PBRRA) {
		super(x, y, width, height, handler, sprite);
		playerBigLeftRunAnimation = PBLRA;
		playerBigLeftWalkAnimation = PBLWA;
		playerBigRightRunAnimation = PBRRA;
		playerBigRightWalkAnimation = PBRWA;
		playerSmallLeftAnimation = PSLA;
		playerSmallRightAnimation = PSRA;
	}

	@Override
	public void tick() {

		if (changeDirrection) {
			changeDirectionCounter++;
		}
		if (changeDirectionCounter >= 10) {
			changeDirrection = false;
			changeDirectionCounter = 0;
		}

		checkBottomCollisions();
		checkMarioHorizontalCollision();
		checkTopCollisions();
		checkItemCollision();



		if (!isBig) {
			if (facing.equals("Left") && moving) {
				playerSmallLeftAnimation.tick();
			} else if (facing.equals("Right") && moving) {
				playerSmallRightAnimation.tick();
			}
		} else {
			if (facing.equals("Left") && moving && !running) {
				playerBigLeftWalkAnimation.tick();
			} else if (facing.equals("Left") && moving && running) {
				playerBigLeftRunAnimation.tick();
			} else if (facing.equals("Right") && moving && !running) {
				playerBigRightWalkAnimation.tick();
			} else if (facing.equals("Right") && moving && running) {
				playerBigRightRunAnimation.tick();
			}
		}
	}

	private void checkItemCollision() {

		for (BaseDynamicEntity entity : handler.getMap().getEnemiesOnMap()) {
			if (entity != null && getBounds().intersects(entity.getBounds()) && entity instanceof Item && !isBig) {
				isBig = true;
				this.y -= 8;
				this.height += 8;
				setDimension(new Dimension(width, this.height));
				((Item) entity).used = true;
				entity.y = -100000;
			}
		}
	}

	public void checkBottomCollisions() {
		Player mario = this;
		ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
		ArrayList<BaseDynamicEntity> enemies = handler.getMap().getEnemiesOnMap();

		Rectangle marioBottomBounds = getBottomBounds();

		if (!mario.jumping) {
			falling = true;
		}

		for (BaseStaticEntity brick : bricks) {

			Rectangle brickTopBounds = brick.getTopBounds();
			if (marioBottomBounds.intersects(brickTopBounds) && !(brick instanceof NoteBlock)) {
				mario.setY(brick.getY() - mario.getDimension().height + 1);
				falling = false;
				velY = 0;
				jumpcounter = 0;

			}
			if (brick instanceof BoundBlock) {
				if(SelectionState.multiP) {
					if (marioBottomBounds.intersects(brickTopBounds)) {
						if(this instanceof Mario) {
							Mario.wins = false;
							State.setState(handler.getGame().winState);
						}
						else{
							Mario.wins = true;
							State.setState(handler.getGame().winState);
						}
					}
				}

				else if (marioBottomBounds.intersects(brickTopBounds) && !SelectionState.multiP) {
					State.setState(handler.getGame().gameOverState);
				}
			}
			if (brick instanceof NoteBlock) {
				if (marioBottomBounds.intersects(brickTopBounds)) {
					falling = false;
					mario.jump();
				}
			}

		}

		for (BaseDynamicEntity enemy : enemies) {
			Rectangle enemyTopBounds = enemy.getTopBounds();
			if (marioBottomBounds.intersects(enemyTopBounds) && !(enemy instanceof Item)&& !(enemy instanceof PowerUpBlock)) {
				if (!enemy.ded) {
					if(!mario.isBig) {
						if(enemy.getRightBounds().intersects(mario.getLeftBounds()) || enemy.getLeftBounds().intersects(mario.getRightBounds())) {
							mario.setHit(true);
							if(SelectionState.multiP) {
								if(this instanceof Mario) {
									Mario.wins = false;
									State.setState(handler.getGame().winState);
								}
								else{
									Mario.wins = true;
									State.setState(handler.getGame().winState);
								}
							}
							else {
								State.setState(handler.getGame().gameOverState);
							}
						}

					}
				}
				else if(mario.isBig){
					mario.isBig = false;
				}
				handler.getGame().getMusicHandler().playStomp();
				enemy.kill();
				falling = false;
				velY = 0;
				jumpcounter = 0;
			}

			if (enemy instanceof PowerUpBlock) {
				if (marioBottomBounds.intersects(enemyTopBounds)) {
					mario.setY(enemy.getY() - mario.getDimension().height + 1);
					falling = false;
					velY = 0;
				}
			}
		}
	}



	public void checkTopCollisions() {
		Player mario = this;
		ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
		ArrayList<BaseDynamicEntity> enemies = handler.getMap().getEnemiesOnMap();

		Rectangle marioTopBounds = mario.getTopBounds();
		for (BaseStaticEntity brick : bricks) {
			Rectangle brickBottomBounds = brick.getBottomBounds();
			if (marioTopBounds.intersects(brickBottomBounds)) {
				velY = 0;
				mario.setY(brick.getY() + brick.height);
			}
			if (brick instanceof RaceBlock) {
				if(this instanceof Mario) {
					
					if (marioTopBounds.intersects(brickBottomBounds)) {
						Mario.wins = true;
						State.setState(handler.getGame().winState);
					}
				}
				else {
					
					if(marioTopBounds.intersects(brickBottomBounds)) {
						Mario.wins = false;
						State.setState(handler.getGame().winState);
					}
				}
			}
		}
		for (BaseDynamicEntity block : enemies) {
			Rectangle blockBottomBounds = block.getBottomBounds();
			if (block instanceof PowerUpBlock) {
				if (marioTopBounds.intersects(blockBottomBounds)) {
					velY = 0;
					mario.setY(block.getY() + block.height);
					this.isBig = true;
				}
			}
		}
	}



	public void checkMarioHorizontalCollision() {
		Player mario = this;
		ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
		ArrayList<BaseDynamicEntity> enemies = handler.getMap().getEnemiesOnMap();

		boolean marioDies = false;
		boolean toRight = moving && facing.equals("Right");

		Rectangle marioBounds = toRight ? mario.getRightBounds() : mario.getLeftBounds();

		for (BaseStaticEntity brick : bricks) {
			Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
			if (marioBounds.intersects(brickBounds)) {
				velX = 0;
				if (toRight)
					mario.setX(brick.getX() - mario.getDimension().width);
				else
					mario.setX(brick.getX() + brick.getDimension().width);
			}
		}

		for (BaseDynamicEntity enemy : enemies) {
			Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
			if (marioBounds.intersects(enemyBounds) && !(enemy instanceof PowerUpBlock) && !(enemy instanceof Item) && !(enemy instanceof Player)) {
				if (!isBig) {
					if(SelectionState.multiP) {
						if(this instanceof Mario) {
							Mario.wins = false;
							State.setState(handler.getGame().winState);
						}
						else {
							Mario.wins = true;
							State.setState(handler.getGame().winState);
						}
					}
					else {
						marioDies = true;
						State.setState(handler.getGame().gameOverState);
					}
				}
				else {
					mario.isBig = false;
				}

			}
			if (enemy instanceof PowerUpBlock) {
				if (marioBounds.intersects(enemyBounds)) {
					velX = 0;
					if (toRight)
						mario.setX(enemy.getX() - mario.getDimension().width);
					else
						mario.setX(enemy.getX() + enemy.getDimension().width);
				}
			}
		}

		if (marioDies) {
			handler.getMap().reset();
		}
	}
	public void jump() {
		if (!jumping && !falling) {
			jumping = true;
			velY = 10;
			handler.getGame().getMusicHandler().playJump();
		}

		if (this instanceof Mario) {

			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) && jumping && jumpcounter < 2) {
				jumping = true;
				velY = 10;
				handler.getGame().getMusicHandler().playJump();
				jumpcounter++;

			}
		}

	}

	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}

	public boolean getHit() {
		return this.hit;
	}

	public void setHit(Boolean hit) {
		this.hit = hit;
	}

}
