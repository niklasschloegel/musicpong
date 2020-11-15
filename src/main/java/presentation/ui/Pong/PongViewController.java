
package presentation.ui.Pong;

import business.MP3Player;
import business.PongLogic;
import data.Track;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.MinimizedAudioBar.MinimizedAudioBarController;
import presentation.ui.PauseModal.PauseModalController;
import presentation.ui.Scenes;
import presentation.utils.StyleAssist;

import static presentation.utils.FadeAssist.fadeInPane;
import static presentation.utils.FadeAssist.fadeOutPane;

public class PongViewController extends AbstractViewController<FXMain> {
    private Timeline tl;
    private Canvas canvas;
    private PongLogic pong;
    private PongView view;
    private PongHUD info;
    private ScorePane score;

    private int hard = 3;
    private int normal = 2;
    private int easy = 1;
    private double sleep;
    private static SimpleIntegerProperty fps = new SimpleIntegerProperty();
    private static SimpleBooleanProperty hudShow = new SimpleBooleanProperty();
    private PauseModalController pauseModalController;
    private StackPane hud;

//    private Thread bpmAvgPulse;
//    private Thread scoreThread;
//    private Thread infoOverAndOut;

    private ChangeListener<Boolean> bpmBeatDet;

    /**
     * instatiates the timeline the game is realized on
     *
     * @param application is needed so that everything operates over the same object
     */
    public PongViewController(FXMain application, MP3Player mp3Player) {
        super(application, mp3Player);

        rootView = new StackPane();
        BorderPane bp = new BorderPane();

        pauseModalController = new PauseModalController(application, mp3Player);


        bp.setBottom(new MinimizedAudioBarController(application, mp3Player).getRootView());

        canvas = new Canvas();
        canvas.setFocusTraversable(true);

		view = new PongView(canvas);

		info = new PongHUD();

		score = new ScorePane();

		hud = new StackPane();

		hud.getChildren().addAll(view, score, info);

        bp.setCenter(hud);

        pong = new PongLogic();

        bpmBeatDet = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> oV, Boolean old_val, Boolean new_val) {
                if (new_val.booleanValue()) {
                    pong.ballPulse(true);
                }
            }
        };
        
        rootView.getChildren().add(bp);
        
        

        initialize();
    }

    /**
     * defines the coordinates for the objects of the given view: 2 rectangles, 1
     * circle starts the pong logic at the end of the method where the new
     * coordinates for the objects are set
     *
     * @param view  contains the objects that need to be referenced to
     * @param logic contains the coordinates for the objects and game logic
     */
    public void run(PongView view, PongLogic logic) {

        view.rec1.setFill(Color.web(StyleAssist.UIColor.MAIN_COLOR.getValue()));
        view.rec1.setX(logic.getPlayerOneX());
        view.rec1.setY(logic.getPlayerOneY());
        view.rec1.setWidth(logic.getPlayerW());
        view.rec1.setHeight(logic.getPlayerH());

        view.rec2.setFill(Color.web(StyleAssist.UIColor.MAIN_COLOR.getValue()));
        view.rec2.setX(logic.getPlayerTwoX());
        view.rec2.setY(logic.getPlayerTwoY());
        view.rec2.setWidth(logic.getPlayerW());
        view.rec2.setHeight(logic.getPlayerH());

        view.oval.setFill(Color.web(StyleAssist.UIColor.SECONDARY_COLOR.getValue()));
        view.oval.setCenterX(logic.getBallX());
        view.oval.setCenterY(logic.getBallY());
        view.oval.setRadiusX(logic.getBallRadius());
        view.oval.setRadiusY(logic.getBallRadius());

        view.ovalShadow.setFill(Color.web(StyleAssist.UIColor.SECONDARY_COLOR_DARK.getValue()));
        view.ovalInner.setFill(Color.web(StyleAssist.UIColor.SECONDARY_COLOR.getValue()));

        logic.ballPulse(false);

        pong.gameStart();
    }

//	public void recAnimation(double x, double y) {
//		view.stRec1 = new ScaleTransition(Duration.millis(800), view.rec1Scale);
//		view.stRec1.setByX(pong.getPlayerOneX());
//		view.stRec1.setByY(pong.getPlayerOneY());
//		view.stRec1.setToX(1.0F);
//		view.stRec1.setToY(1.0F);
//		view.stRec1.setFromX(x);
//		view.stRec1.setFromY(y);
//		view.stRec1.setCycleCount(1);
//		view.stRec1.setAutoReverse(false);
//		
//		view.ftRec1 = new FadeTransition(Duration.millis(800), view.rec1Fade);
//		view.ftRec1.setToValue(0.4);
//		view.ftRec1.setFromValue(1);
//		view.ftRec1.setCycleCount(1);
//		view.ftRec1.setAutoReverse(false);
//		
//		view.pT2 = new ParallelTransition(view.stRec1, view.ftRec1);
//		
//		view.pT2.play();
//	}
    
	public void score(int p1, int p2) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				score.score1.setText(Integer.toString(p1));
				score.score2.setText(Integer.toString(p2));
			}
		});
	}


    @Override
    public void initialize() {
        // eventlistener for pressed keys to move the player
        view.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S && pong.hasStarted()) {
                pong.setPlayer1Vel(5);
            }
            if (e.getCode() == KeyCode.W && pong.hasStarted()) {
                pong.setPlayer1Vel(-5);
            }
            if (e.getCode() == KeyCode.I && pong.hasStarted() && pong.isMultiP()) {
                pong.setPlayer2Vel(-5);
            }
            if (e.getCode() == KeyCode.K && pong.hasStarted() && pong.isMultiP()) {
                pong.setPlayer2Vel(5);
            }
            if (e.getCode() == KeyCode.SPACE && !pong.hasStarted()) {
            		pong.setStarted(true);
            	if(info.getOpacity() == 1) {
                    fadeOutPane(score);
                    fadeOutPane(info);
            	}
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                pong.setPaused(!pong.isPaused());
            }
            if (e.getCode() == KeyCode.R && !pong.isPaused()) {
                pong.setStarted(false);
            }
            if (e.getCode() == KeyCode.DIGIT2) {
            	if(pong.isMultiP()) {
            		fadeOutPane(info.p2Info);
            		pong.setMultiP(false);
            	} else {
	                pong.setMultiP(true);
	                pong.setPlayer2Vel(0);
            	}
            	if(info.p2Info.getOpacity() == 0) {
            		fadeInPane(info.p2Info);
            	}
            }
        });

        
        pong.pausedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                rootView.getChildren().add(pauseModalController.getRootView());
            } else {
                rootView.getChildren().remove(pauseModalController.getRootView());
            }
        });

        pauseModalController.resumeTriggeredProperty().addListener((observableValue, aBoolean, t1) -> pong.setPaused(false));

        pauseModalController.wantsToEndGameProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                pong.setPaused(false);
                pong.setStarted(false);
                pong.setScoreP1(0);
                pong.setScoreP2(0);
                score.score1.setText("0");
                score.score2.setText("0");
                if (!hud.getChildren().contains(info)) {
                    hud.getChildren().addAll(info);
                }
                info.setOpacity(1);
                score.setOpacity(1);
                rootView.getChildren().remove(pauseModalController.getRootView());
                application.switchScene(Scenes.HOME_VIEW, Scenes.PONG_VIEW);
                mp3Player.stop();
                mp3Player.setPlaylist(mp3Player.getDefaultPlaylist());
                mp3Player.play(mp3Player.getCurPlaylist().getTrack(0).getSoundFile());
            }
        });

        // eventlistener needed so that the player won't move even though we released
        view.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.S && pong.hasStarted()) {
                pong.setPlayer1Vel(0);
            }
            if (e.getCode() == KeyCode.W && pong.hasStarted()) {
                pong.setPlayer1Vel(0);
            }
            if (e.getCode() == KeyCode.I && pong.hasStarted()) {
                pong.setPlayer2Vel(0);
            }
            if (e.getCode() == KeyCode.K && pong.hasStarted()) {
                pong.setPlayer2Vel(0);
            }
        });
        
        
        fpsProperty().addListener(new ChangeListener<Number>() {
        	public void changed(ObservableValue<? extends Number> oV, Number old_val, Number new_val) {
        		double f = new_val.doubleValue()/5;

        		tl.stop();

        		tl = new Timeline(new KeyFrame(Duration.millis(f), e -> run(view, pong)));

    			tl.setCycleCount(Timeline.INDEFINITE);

    			tl.play();
        		
        	}
        });
        
        hudShowProperty().addListener(new ChangeListener<Boolean>() {
        	public void changed(ObservableValue<? extends Boolean> oV, Boolean old_val, Boolean new_val) {
        		if(new_val && !hud.getChildren().contains(info))
        			hud.getChildren().add(info);
        		 else
                    hud.getChildren().remove(info);
			}
        });
        
		mp3Player.trackPropProperty().addListener(new ChangeListener<Track>() {
			public void changed(ObservableValue<? extends Track> oV, Track old_val, Track new_val) {
				if(new_val.getBPM() <= 107) {
					pong.setDifficulty(1);
				} else
					if(new_val.getBPM() > 107 && new_val.getBPM() <= 134) {
						pong.setDifficulty(2);
				} else
					if(new_val.getBPM() > 134) {
						pong.setDifficulty(3);
				}

				if(new_val.isSpotified()) {
					if(mp3Player.boomProperty().isBound()) {
						mp3Player.boomProperty().removeListener(bpmBeatDet);
					}

					sleep = 60000/new_val.getBPM();
					new Thread() {
						 public void run() {
				               while(mp3Player.isPlaying() && !isInterrupted()) {
				                    pong.ballPulse(true);
				                    try {
				                        Thread.sleep((long) sleep);
				                    } catch (InterruptedException e) {
				                      //  System.out.println("Couldn't sleep");
				                        Thread.currentThread().interrupt();
				                        e.printStackTrace();
				                    }
				               }
				            }
					}.start();

					view.pT.play();

				} else {
					mp3Player.boomProperty().addListener(bpmBeatDet);

					view.pT.play();
				}
			}
		});

		pong.propStarted().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> oV, Boolean old_val, Boolean new_val) {
				pong.setPlayer1Vel(0);
				pong.setPlayer2Vel(0);
			}
		});
		
		
//		pong.propContact().addListener(new ChangeListener<Boolean>() {
//			public void changed(ObservableValue<? extends Boolean> oV, Boolean old_val, Boolean new_val) {
//				if (new_val.booleanValue()) {
//					System.out.println("CONTACT X: "+pong.getContactX() + " CONTACT Y: "+ pong.getContactY());
//					System.out.println("XSPEED: "+ pong.getBallXSpeed() + " YSPEED: " + pong.getBallYSpeed());
//					new Thread() {
//						public void run() {
//							recAnimation(pong.getContactX(), pong.getContactY());
//						}
//					}.start();
//				}
//			}
//		});

		//hud info
		pong.propGoal().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> oV, Boolean old_val, Boolean new_val) {
				if(new_val.booleanValue()) {
					fadeInPane(score);
					score(pong.getScoreP1(), pong.getScoreP2());
				}
			}
		});

		view.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> oV, Number old_val, Number new_val) {
				pong.setWidth(new_val.doubleValue());
			}
		});

		view.heightProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> oV, Number old_val, Number new_val) {
				pong.setHeigth(new_val.doubleValue());
			}
		});

		// starts the timeline only if the window is shown
		application.getStage().setOnShowing(e -> {

			canvas.setHeight(view.getHeight());
			canvas.setWidth(view.getWidth());

			pong.setWidth(view.getWidth());
			pong.setHeigth(view.getHeight());

			tl = new Timeline(new KeyFrame(Duration.millis(12), f -> run(view, pong)));

			tl.setCycleCount(Timeline.INDEFINITE);

			tl.play();

		});
	}

    public static int getFps() {
        return fps.get();
    }

    public static SimpleIntegerProperty fpsProperty() {
        return fps;
    }

    public static void setFps(int fps) {
       PongViewController.fps.set(fps);
       
    }

    public static boolean isHudShow() {
        return hudShow.get();
    }

    public static SimpleBooleanProperty hudShowProperty() {
        return hudShow;
    }

    public static void setHudShow(boolean hudShow) {
        PongViewController.hudShow.set(hudShow);
    }
}