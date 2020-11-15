package business;

import javafx.beans.property.SimpleBooleanProperty;
import java.util.Random;

public class PongLogic {
	private int PLAYER_WIDTH;
	private int PLAYER_HEIGTH;
	private double winWidth;
	private double winHeigth;
	private double BALL_R;
	private double BALL_P;
	private double tmpBALL;
	private double ballXSpeed;
	private double ballYSpeed;
	private double ballXPos;
	private double ballYPos;
	private double P1velY;
	private double P2velY;
	private double velOff1;
	private double velOff2;
	private double playerOneY;
	private double playerTwoY;
	private double playerOneX;
	private double playerTwoX;
	private int scoreP1;
	private int scoreP2;
	private double contactXPos;
	private double contactYPos;
	private int difficulty;
	private double easy;
	private double normal;
	private double hard;
	private SimpleBooleanProperty contact = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty gameStarted = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty goal = new SimpleBooleanProperty(false);
	private boolean multiplayer = false;
	private boolean angle; 
	private int pulseCount = 0;
	private double addAngle = 0;

	/**
	 * instantiates the minimal values needed to start the game
	 *
	 */
	public PongLogic() {
		scoreP1 = 0;
		scoreP2 = 0;
		P1velY = 0;
		P2velY = 1;
		ballYSpeed = 0;
		ballXSpeed = 0;
		difficulty = 0;
		easy = 5;
		normal = 7;
		hard = 10;
		playerOneY = winHeigth / 2;
		playerTwoY = winHeigth / 2;
		playerOneX = winWidth * 0.02;
		playerTwoX = winWidth - (winWidth * 0.02) - PLAYER_WIDTH;
	}

	/**
	 * 1.sets at the beginning the postion of the player 2.increases the ball speed
	 * 3.sets the playerTwoAI
	 * 
	 */
	public void gameStart() {
		if (gameStarted.get() && !paused.get()) {
			posSetter();
			offSet();
			if (multiplayer) {
				hitboxing();
			} else {
				playerTwoAi();
				hitboxing();
			}
		} else {
			if(!gameStarted.get()) {
				goal.set(false);
				ballXPos = winWidth / 2;
				ballYPos = winHeigth / 2;
				playerOneY = winHeigth / 2;
				playerTwoY = winHeigth / 2;
				ballXSpeed = new Random().nextInt(3) == 0 ? 2 : -2;
				ballYSpeed = new Random().nextInt(3) == 0 ? 2 : -2;
			}
		}
	}

	/**
	 * sets the new position of player two
	 * moves slowly if the ball is in the 1/3 part of the game field
	 * else it moves faster
	 */
	public void playerTwoAi() {
		if (gameStarted.get()) {
			switch (difficulty) {
			case 1:
				if (ballXPos <= winWidth - (winWidth / 4)) {
					if (ballYPos > playerTwoY + PLAYER_HEIGTH * 1.5) {
						playerTwoY += P2velY;
					}
					if (ballYPos < playerTwoY - PLAYER_HEIGTH * 1.5) {
						playerTwoY -= P2velY;
					}
				} else {
					if (ballYPos > playerTwoY + PLAYER_HEIGTH) {
						playerTwoY += P2velY + 2;
					}
					if (ballYPos < playerTwoY) {
						playerTwoY -= P2velY + 2;
					}
				}
				break;
			case 2:
				if (ballXPos < winWidth - (winWidth / 3)) {
					if (ballYPos > (playerTwoY + PLAYER_HEIGTH * 1.5)) {
						playerTwoY += P2velY + 2;
					}
					if (ballYPos < playerTwoY - PLAYER_HEIGTH * 1.5) {
						playerTwoY -= P2velY + 2;
					}
				} else {
					if (ballYPos > playerTwoY + PLAYER_HEIGTH) {
						playerTwoY += P2velY + 4;
					}
					if (ballYPos < playerTwoY) {
						playerTwoY -= P2velY + 4;
					}
				}
				break;
			case 3:
				if (ballXPos < winWidth - (winWidth / 2)) {
					if (ballYPos > (playerTwoY + PLAYER_HEIGTH * 1.5)) {
						playerTwoY += P2velY + 3;
					}
					if (ballYPos  < playerTwoY - PLAYER_HEIGTH * 1.5) {
						playerTwoY -= P2velY + 3;
					}
				} else {
					if (ballYPos > playerTwoY + PLAYER_HEIGTH) {
						playerTwoY += P2velY + 5;
					}
					if (ballYPos < playerTwoY) {
						playerTwoY -= P2velY + 5;
					}
				}
				break;
			}
		}
	}
	/**
	 * sets default game physics (boundaries)
	 */
	public void hitboxing() {
		// if the ball hits the bottomside of the window we invert the travelspeed
		if ((ballYPos + BALL_R) >= winHeigth) {
			ballYPos = winHeigth-BALL_R;
			ballYSpeed *= -1;
		}
		// same as above but only with the topside of the window
		if (ballYPos - BALL_R <= 0) {
			ballYPos = BALL_R;
			ballYSpeed *= -1;
		}
		// counts a point and restarts the game if the ball
		if (ballXPos < playerOneX) {
			gameStarted.set(false);
			scoreP2++;
			goal.set(true);
		}
		// same as above
		if (ballXPos > (playerTwoX + PLAYER_WIDTH)) {
			gameStarted.set(false);
			scoreP1++;
			goal.set(true);
		}
		// seperating the left gamefield from the right
		if (ballXPos <= winWidth / 2) {
			// if the ball hits the top/bottom of the rectangle it should bounce of
			// inverts the direction of the Y axis and speeds up the travel speed
			if ((ballXPos < playerOneX + PLAYER_WIDTH) && (ballYPos - BALL_R <= playerOneY + PLAYER_HEIGTH) 
					&& (ballYPos > playerOneY + PLAYER_HEIGTH) && (ballYSpeed < 0)) {
				contactXPos = ballXPos;
				contactYPos = ballYPos - BALL_R;
				contact.set(true);

				ballYPos += P1velY;
				ballXPos -= P1velY;
				ballYSpeed += P1velY * Math.signum(ballYSpeed);
				ballYSpeed *= -1;
			} else {
				if ((ballXPos < playerOneX + PLAYER_WIDTH) && (ballYPos + BALL_R >= playerOneY) 
						&& (ballYPos < playerOneY) && (ballYSpeed > 0)) {
					contactXPos = ballXPos;
					contactYPos = ballYPos + BALL_R;
					contact.set(true);

					ballYPos -= P1velY;
					ballXPos -= P1velY;
					ballYSpeed += P1velY * Math.signum(ballYSpeed);
					ballYSpeed *= -1;
				}
			}
			
			// if the ball hits the corner it should bounce back where it came from
			// 1.if for the top corner
			// 2.if for the bottom corner
			if ((ballXPos - BALL_R < playerOneX + PLAYER_WIDTH) && (ballXPos > playerOneX + PLAYER_WIDTH)
					&& (ballYPos + BALL_R >= playerOneY) && (ballYPos < playerOneY) && (ballXSpeed < 0)
					&& (ballYSpeed > 0) && !(ballXPos <= playerOneX + PLAYER_WIDTH) ) {
				contactXPos = ballXPos - BALL_R;
				contactYPos = ballYPos;
				contact.set(true);
				
				ballXPos -= P1velY;
				ballYSpeed += 1 * Math.signum(ballYSpeed);
				ballXSpeed += 1 * Math.signum(ballXSpeed);
				ballYSpeed *= -1;
				ballXSpeed *= -1;
			} else {
				if ((ballXPos - BALL_R < playerOneX + PLAYER_WIDTH) && (ballXPos > playerOneX + PLAYER_WIDTH)
						&& (ballYPos - BALL_R <= playerOneY + PLAYER_HEIGTH) && (ballYPos > playerOneY + PLAYER_HEIGTH) && (ballXSpeed < 0) 
						&& (ballYSpeed < 0) && !(ballXPos <= playerOneX + PLAYER_WIDTH)) {
					contactXPos = ballXPos - BALL_R;
					contactYPos = ballYPos;
					contact.set(true);

					ballXPos += P1velY;
					ballYSpeed += 1 * Math.signum(ballYSpeed);
					ballXSpeed += 1 * Math.signum(ballXSpeed);
					ballYSpeed *= -1;
					ballXSpeed *= -1;
				}
			}
			// inverts the direction of the X axis and speeds up the travelspeed by 1 if the
			// ball hits the rectangle side
			if ((ballXPos - BALL_R <= playerOneX + PLAYER_WIDTH) && (ballYPos - BALL_R < playerOneY + PLAYER_HEIGTH)
					&& (ballYPos + BALL_R > playerOneY) && (ballXSpeed < 0) && (ballYPos <= playerOneY + PLAYER_HEIGTH) 
					&& (ballYPos >= playerOneY)) {
				
				// changes the angle the ball will travel(Steigungsfunktion)
				if ((ballYPos < (playerOneY + PLAYER_HEIGTH / 2) + (PLAYER_HEIGTH/8)) 
								&& (ballYPos >= (playerOneY + PLAYER_HEIGTH/ 2) - (PLAYER_HEIGTH/8))) {
					contactXPos = ballXPos - BALL_R;
					contactYPos = ballYPos;
					contact.set(true);

					ballXSpeed += 1 * Math.signum(ballXSpeed);
					ballYSpeed += 0.125 * Math.signum(ballYSpeed);
					ballXSpeed *= -1;
					angle = true;
					addAngle = 0.125;
				} else {
					if (ballYPos < (playerOneY + PLAYER_HEIGTH / 8) && ballYPos >= playerOneY ) {
						contactXPos = ballXPos - BALL_R;
						contactYPos = ballYPos;
						contact.set(true);
						
						ballYSpeed += 1 * Math.signum(ballYSpeed);
						ballXSpeed *= -1;
						angle = true;
						addAngle = 1;
					} else {
						if (ballYPos > (playerOneY + PLAYER_HEIGTH) - (PLAYER_HEIGTH / 8) && ballYPos <= (playerOneY + PLAYER_HEIGTH)) {
							
							contactXPos = ballXPos - BALL_R;
							contactYPos = ballYPos;
							contact.set(true);

							ballYSpeed += 1 * Math.signum(ballYSpeed);
							ballXSpeed *= -1;
							angle = true;
							addAngle = 1;
						} else {
							contactXPos = ballXPos - BALL_R;
							contactYPos = ballYPos;
							contact.set(true);

							ballXSpeed += 1 * Math.signum(ballXSpeed);
							ballYSpeed += 1 * Math.signum(ballYSpeed);
							ballXSpeed *= -1;
						}
					}
				}
			}
		} else {
			// if the ball hits the top/bottom of the rectangle it should bounce of
			// inverts the direction of the Y axis and speeds up the travel speed
			if ((ballXPos > playerTwoX) && (ballYPos + BALL_R >= playerTwoY) && (ballYPos < playerTwoY) && (ballYSpeed > 0)) {
				contactXPos = ballXPos;
				contactYPos = ballYPos + BALL_R;
				contact.set(true);

				ballYPos -= P2velY;
				ballXPos += P2velY;
				ballYSpeed += 1 * Math.signum(ballYSpeed);
				ballYSpeed *= -1;
			} else {
				if ((ballXPos > playerTwoX) && (ballYPos - BALL_R <= playerTwoY + PLAYER_HEIGTH) && (ballYPos > playerTwoY + PLAYER_HEIGTH) && (ballYSpeed < 0)) {
					contactXPos = ballXPos;
					contactYPos = ballYPos - BALL_R;
					contact.set(true);

					ballYPos += P2velY;
					ballXPos += P2velY;
					ballYSpeed += 1 * Math.signum(ballYSpeed);
					ballYSpeed *= -1;
				}
			}
			// if the ball hits the corner it should bounce back where it came from
			// 1.if for the top corner
			// 2.if for the bottom corner
			if ((ballXPos + BALL_R > playerTwoX) && (ballXPos < playerTwoX)
					&& (ballYPos - BALL_R <= playerTwoY + PLAYER_HEIGTH) && (ballYPos > playerTwoY + PLAYER_HEIGTH)
					&& (ballXSpeed > 0) && (ballYSpeed < 0) && !(ballXPos >= playerTwoX)) {
				contactXPos = playerTwoX;
				contactYPos = ballYPos - BALL_R;
				contact.set(true);

				ballYSpeed += 1 * Math.signum(ballYSpeed);
				ballXSpeed += 1 * Math.signum(ballXSpeed);
				ballYSpeed *= -1;
				ballXSpeed *= -1;
			} else {
				if ((ballXPos + BALL_R > playerTwoX) && (ballXPos < playerTwoX) 
						&& (ballYPos + BALL_R >= playerTwoY) && (ballYPos < playerTwoY) 
						&& (ballXSpeed > 0) && (ballYSpeed > 0) && !(ballXPos >= playerTwoX)) {
					contactXPos = playerTwoX;
					contactYPos = ballYPos + BALL_R;
					contact.set(true);

					ballYSpeed += 1 * Math.signum(ballYSpeed);
					ballXSpeed += 1 * Math.signum(ballXSpeed);
					ballYSpeed *= -1;
					ballXSpeed *= -1;
				}
			}
			// inverts the direction of the X axis and speeds up the travelspeed by 1 if the
			// ball hits the rectangle side
			if ((ballXPos + BALL_R >= playerTwoX) && (ballYPos - BALL_R < playerTwoY + PLAYER_HEIGTH) 
					&& (ballYPos + BALL_R > playerTwoY) && (ballXSpeed > 0) && (ballYPos <= playerTwoY + PLAYER_HEIGTH) 
					&& (ballYPos >= playerTwoY)) {
				// changes the angle the ball will travel(Steigungsfunktion)
				if (ballYPos < (playerTwoY + PLAYER_HEIGTH / 2) + (PLAYER_HEIGTH/8) && ballYPos >= (playerTwoY + PLAYER_HEIGTH/ 2) - (PLAYER_HEIGTH/8)) {
					contactXPos = ballXPos + BALL_R;
					contactYPos = ballYPos;

					ballXSpeed += 1 * Math.signum(ballXSpeed);
					ballYSpeed += 0.125 *Math.signum(ballYSpeed);
					ballXSpeed *= -1;
					angle = true;
					addAngle = 0.125;
				} else {
					if ((ballYPos > playerTwoY) && (ballYPos < (playerTwoY + PLAYER_HEIGTH / 8))) {
						contactXPos = ballXPos + BALL_R;
						contactYPos = ballYPos;
						contact.set(true);

						ballYSpeed += 1 * Math.signum(ballYSpeed);
						ballXSpeed *= -1;
						angle = true;

						addAngle = 1;
					} else {
						if ((ballYPos < (playerTwoY + PLAYER_HEIGTH)) && (ballYPos > (PLAYER_HEIGTH - PLAYER_HEIGTH / 8))) {
							contactXPos = ballXPos + BALL_R;
							contactYPos = ballYPos;
							contact.set(true);

							ballYSpeed += 1 * Math.signum(ballYSpeed);
							ballXSpeed *= -1;
							angle = true;
							addAngle = 1;
						} else {
							contactXPos = ballXPos + BALL_R;
							contactYPos = ballYPos;
							contact.set(true);

							ballXSpeed += 1 * Math.signum(ballXSpeed);
							ballYSpeed += 1 * Math.signum(ballYSpeed);
							ballXSpeed *= -1;
						}
					}
				}
			}
		}
	}
	
	/*
	 * sets the ball speed by set difficulty and increments the ball position
	 */
	private void difficultyCheck() {
		switch (difficulty) {
		case 3:
			if(ballXSpeed * Math.signum(ballXSpeed) >= hard) {
				ballXSpeed = hard * Math.signum(ballXSpeed);
			}
			if(ballYSpeed *  Math.signum(ballYSpeed) >= hard) {
				if(angle) {
					ballYSpeed = (hard + addAngle) * Math.signum(ballYSpeed);
				} else {
					ballYSpeed = hard * Math.signum(ballYSpeed);
				}
			}
			ballXPos += ballXSpeed;
			ballYPos += ballYSpeed;
			break;
		case 2:
			if(ballXSpeed * Math.signum(ballXSpeed) >= normal) {
				ballXSpeed = normal * Math.signum(ballXSpeed);
			}
			if(ballYSpeed * Math.signum(ballYSpeed) >= normal) {
				if(angle) {
					ballYSpeed = (normal + addAngle) * Math.signum(ballYSpeed);
				} else {
					ballYSpeed = normal * Math.signum(ballYSpeed);
				}
			}
			ballXPos += ballXSpeed; 
			ballYPos += ballYSpeed;
			break;
		case 1:
			if(ballXSpeed * Math.signum(ballXSpeed) >= easy) {
				ballXSpeed = easy * Math.signum(ballXSpeed);
			}
			if(ballYSpeed * Math.signum(ballYSpeed) >= easy) {
				if(angle) {
					ballYSpeed = (easy + addAngle) * Math.signum(ballYSpeed);
				} else {
					ballYSpeed = easy * Math.signum(ballYSpeed);
				}
			}
			ballXPos += ballXSpeed;
			ballYPos += ballYSpeed;
			break;
		default:
			ballXPos += ballXSpeed;
			ballYPos += ballYSpeed;
		}
		angle = false;
	}

	/**
	 * sets the position of the players so that they won't go out of bounds and
	 * overlap eachother
	 */
	private void posSetter() {
		if (gameStarted.get()) {
			
			difficultyCheck();
			
			contact.set(false);
		}
		playerOneY += P1velY;

		if (multiplayer)
			playerTwoY += P2velY;

		if (playerOneY >= winHeigth - PLAYER_HEIGTH) {
			playerOneY = winHeigth - PLAYER_HEIGTH;
		}
		if (playerTwoY >= winHeigth - PLAYER_HEIGTH) {
			playerTwoY = winHeigth - PLAYER_HEIGTH;
		}
		if (playerTwoY <= 0) {
			playerTwoY = 0;
		}
		if (playerOneY <= 0) {
			playerOneY = 0;
		}
	}

	/*
	 * sets changed velocity of player movement
	 */
	private void offSet() {
		P1velY = velOff1;
		P2velY = velOff2;
	}
	/*
	 * increases the ball radius or shrinks it back to its original size 
	 * to simulate a pulse
	 */
	public void ballPulse(boolean pulse) {
		if(pulse) 
			BALL_R = BALL_P;
		if(!pulse && pulseCount == 8){
			BALL_R = tmpBALL;
			pulseCount = 0;
		} else 
			if(pulseCount < 8)
				++pulseCount;
	}
	
	
////////////////////////////////////GETTER///////////////&///////////////SETTER//////////////////////////////////////
	
	public void setDifficulty(int x) {
		difficulty = x;
	}
	public double getContactX() {
		return contactXPos;
	}

	public  double getContactY() {
		return contactYPos;
	}

	public SimpleBooleanProperty propContact() {
		return contact;
	}

	public void setMultiP(boolean multi) {
		multiplayer = multi;
	}
	
	public boolean isMultiP() {
		return multiplayer;
	}
	
	/*
	 * sets the player 1 velocity input from keypressed
	 * 
	 * @param double v input velocity
	 */
	public void setPlayer1Vel(double v) {
		velOff1 = v;
	}
	
	/*
	 * sets the player 2 velocity input from keypressed
	 * 
	 * @param double v input velocity
	 */
	public void setPlayer2Vel(double v) {
		velOff2 = v;
	}

	public void setStarted(boolean start) {
		gameStarted.set(start);
	}

	public boolean hasStarted() {
		return gameStarted.get();
	}

	public void setPaused(boolean pause) {
		paused.set(pause);
	}

	public boolean isPaused() {
		return paused.get();
	}

	public SimpleBooleanProperty propGoal() {
		return goal;
	}

	public SimpleBooleanProperty propStarted() {
		return gameStarted;
	}

	public SimpleBooleanProperty propPaused() {
		return paused;
	}

	public double getPlayerOneX() {
		return playerOneX;
	}

	public double getPlayerOneY() {
		return playerOneY;
	}

	public double getPlayerTwoX() {
		return playerTwoX;
	}

	public double getPlayerTwoY() {
		return playerTwoY;
	}

	public double getWidth() {
		return winWidth;
	}

	public double getHeigth() {
		return winHeigth;
	}

	public double getPlayerW() {
		return PLAYER_WIDTH;
	}

	public double getPlayerH() {
		return PLAYER_HEIGTH;
	}

	public double getBallX() {
		return ballXPos;
	}

	public double getBallY() {
		return ballYPos;
	}
	
	public double getBallXSpeed() {
		return ballXSpeed;
	}
	
	public double getBallYSpeed() {
		return ballYSpeed;
	}

	public double getBallRadius() {
		return BALL_R;
	}
	
	public int getScoreP1() {
		return scoreP1;
	}
	
	public int getScoreP2() {
		return scoreP2;
	}

	public void setWidth(double width) {
		ballXPos = scaleSetterWidth(ballXPos, width);
		playerOneX = scaleSetterWidth(playerOneX, width);
		playerTwoX = scaleSetterWidth(playerTwoX, width);
		winWidth = width;
		PLAYER_WIDTH = (int) (winWidth * 0.03);
		BALL_R = PLAYER_WIDTH/1.5;
		tmpBALL = BALL_R;
		BALL_P = PLAYER_WIDTH/1.25;
		if(!gameStarted.get()) {
			playerOneX = winWidth * 0.0125;
			playerTwoX = winWidth - winWidth * 0.0125 - PLAYER_WIDTH;
		}
	}

	public void setHeigth(double heigth) {
		ballYPos = scaleSetterHeigth(ballYPos, heigth);
		playerOneY = scaleSetterHeigth(playerOneY, heigth);
		playerTwoY = scaleSetterHeigth(playerTwoY, heigth);
		
		winHeigth = heigth;
		PLAYER_HEIGTH = (int) (winHeigth * 0.12);
		if(!gameStarted.get()) {
			playerOneY = winHeigth / 2;
			playerTwoY = winHeigth / 2;
		}
	}
	
	private double scaleSetterWidth(double ref, double newMax) {
		double faktor;
		faktor = ref/winWidth;
		ref = newMax * faktor;
		return ref;
	}
	private double scaleSetterHeigth(double ref, double newMax) {
		double faktor;
		faktor = ref/winHeigth;
		ref = newMax * faktor;
		return ref;
	}

	public SimpleBooleanProperty pausedProperty() {
		return paused;
	}

	public void setScoreP1(int scoreP1) {
		this.scoreP1 = scoreP1;
	}

	public void setScoreP2(int scoreP2) {
		this.scoreP2 = scoreP2;
	}
}