package vn.com.tuanminh.rollyfrog.game;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogunnerframe.screens.ScreenManager;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransitionFade;
import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.boost.BoostHandler;
import vn.com.tuanminh.rollyfrog.boost.Shield;
import vn.com.tuanminh.rollyfrog.boost.Slow;
import vn.com.tuanminh.rollyfrog.boost.X5;
import vn.com.tuanminh.rollyfrog.event.EventHandler;
import vn.com.tuanminh.rollyfrog.mode.BaseGameMode;
import vn.com.tuanminh.rollyfrog.mode.BaseGameMode.STATE;
import vn.com.tuanminh.rollyfrog.mode.LaserMode;
import vn.com.tuanminh.rollyfrog.object.Frog;
import vn.com.tuanminh.rollyfrog.object.MathSpawner;
import vn.com.tuanminh.rollyfrog.object.ObstacleSpawner;
import vn.com.tuanminh.rollyfrog.object.Room;
import vn.com.tuanminh.rollyfrog.screen.MenuScreen;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;
import vn.com.tuanminh.rollyfrog.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {
	// General stuffs
	private ScreenManager game;
	private GameWorld gameWorld;
	private BaseGameMode mode;
	private World b2dWorld;
	private OrthographicCamera camGame;
	private Box2DDebugRenderer debug;
	private SpriteBatch batch;
	private ShapeRenderer shapeRen;
	private EventHandler eventHandler;
	private BoostHandler boostHandler;

	// UI stuffs
	private OrthographicCamera camUI;
	private Stage stageUI;
	private Skin skin;
	// Guide screen
	private Group guideGroup;
	private Image dragText;
	private Image tapImg;
	private int dragNum = 0;
	// Over screen
	private Group overGroup;
	private Button reBut;
	private Button leadBut;
	private Button reMenuBut;
	private Image scorePanel;
	private Label scoreLabel;
	private Label highLabel;
	private Label coinLabel;
	private Image coinImg;
	private Image overImg;
	private Image highImg;
	boolean isOverCreated = false;
	boolean isGuideCreated = false;

	// Pause window
	private Image panel;
	private Button pauseBut;
	private Button menuBut;
	private Button rateBut;
	private Button resumeBut;
	private Label reCountLabel;
	private TimeHelper timer;

	// Score label
	private Image scoreBoard;
	private Label curScore;
	private String score;
	private float length;

	// Shared objects
	private Room room;
	private Frog frog;

	// Shared Assets
	private TextureRegion frogRegions[] = Assets.instance.texture.frogRegions;
	private TextureRegion frogDieRegions[] = Assets.instance.texture.frogDieRegions;

	private TextureRegion frogRegion, dieRegion, roomRegion, bgRegion, iceScreen, shieldGlow;
	private ObstacleSpawner obSpawner;

	// Particle
	private ParticleEffect laserBoom;

	// Math mode
	private TextureRegion thinkRegion;

	public GameRenderer(GameWorld gameWorld, Stage stageUI, ScreenManager game) {
		this.game = game;
		skin = Assets.instance.skin.mySkin;
		this.stageUI = stageUI;
		this.gameWorld = gameWorld;
		mode = gameWorld.getGameMode();
		// Resume timer
		timer = new TimeHelper();

		// Camera
		camGame = new OrthographicCamera();
		camGame.setToOrtho(false, 320f / Const.PPM, Const.GAME_HEIGHT / Const.PPM);
		camUI = new OrthographicCamera();
		camUI.setToOrtho(false, 320f, Const.GAME_HEIGHT);
		batch = new SpriteBatch();

		// B2dWorld
		b2dWorld = gameWorld.getGameMode().getBox2DWorld();

		// Event
		eventHandler = mode.getEventHandler();
		if (eventHandler != null)
			eventHandler.getEvent().getCameraHandler().setCamera(camGame);

		// Boost
		boostHandler = mode.getBoostHandler();

		// Debug
		if (Const.isDebug) {
			shapeRen = new ShapeRenderer();
			debug = new Box2DDebugRenderer();
		}

		// Initiating for all modes
		this.initObjects();
		this.initAssets();
	}

	private void createPauseMenu() {
		final Group pauseMenu = new Group();
		// Panel image
		panel = new Image(skin, "panel");
		panel.setVisible(false);
		panel.setSize(240f, 280f);
		panel.setPosition(160f - panel.getWidth() / 2, Const.GAME_HEIGHT / 2 - panel.getHeight() / 2);

		// Menu but
		menuBut = new Button(skin, "menu");
		menuBut.setVisible(false);
		menuBut.setPosition(panel.getX() + panel.getWidth() / 2 - menuBut.getWidth() / 2, panel.getY() + 20f);
		menuBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				GamePreferences.instance.gold += mode.getScore();
				GamePreferences.instance.save();
				game.setScreen(new MenuScreen(game), ScreenTransitionFade.init(0.2f));
			}

		});

		// Countdown label
		reCountLabel = new Label("" + timer.getRunTimeSeconds(), skin);
		reCountLabel.setColor(Color.YELLOW);
		reCountLabel.setVisible(false);
		reCountLabel.setFontScale(0.8f);
		reCountLabel.setPosition(160f - 10f, Const.GAME_HEIGHT / 2 - 10f);

		// Resume but
		resumeBut = new Button(skin, "resume");
		resumeBut.setVisible(false);
		resumeBut.setPosition(menuBut.getX(), menuBut.getY() + menuBut.getHeight() + 30f);
		resumeBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				timer.startRunTime();
				reCountLabel.setVisible(true);
				panel.setVisible(false);
				menuBut.setVisible(false);
				resumeBut.setVisible(false);
				rateBut.setVisible(false);
				resumeBut.addAction(forever(run(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						reCountLabel.setText("" + (3 - timer.getRunTimeSeconds()));
						if (timer.getRunTimeMillis() > Const.PAUSED_TIME) {
							AudioManager.instance.unpauseMusic();
							mode.setState(STATE.RUNNING);
							timer.resetTime();
							reCountLabel.setVisible(false);
							resumeBut.removeAction(resumeBut.getActions().first());
						}
					}

				})));
			}

		});

		// Rate but
		rateBut = new Button(skin, "rate");
		rateBut.setVisible(false);
		rateBut.setPosition(resumeBut.getX(), resumeBut.getY() + resumeBut.getHeight() + 30f);
		rateBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.handler.rateGame();
			}

		});

		// Pause but
		pauseBut = new Button(skin, "pause");
		pauseBut.setVisible(true);
		pauseBut.setSize(60f, 60f);
		pauseBut.setPosition(320f - pauseBut.getWidth() - 18f, Const.GAME_HEIGHT - pauseBut.getHeight() - 16f);
		pauseBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.pauseMusic();
				if (!mode.isHighScore() && !mode.isGameOver()) {
					mode.setState(STATE.PAUSE);
					panel.setVisible(true);
					menuBut.setVisible(true);
					resumeBut.setVisible(true);
					rateBut.setVisible(true);
				}
			};

		});

		pauseMenu.addActor(panel);
		pauseMenu.addActor(pauseBut);
		pauseMenu.addActor(menuBut);
		pauseMenu.addActor(resumeBut);
		pauseMenu.addActor(rateBut);
		pauseMenu.addActor(reCountLabel);
		stageUI.addActor(pauseMenu);
	}

	private void createScoreLabel() {
		Group scoreGroup = new Group();
		scoreBoard = new Image(skin, "scoreboard");
		scoreBoard.setPosition(0f, Const.GAME_HEIGHT - scoreBoard.getHeight() - 13f);

		// score number
		curScore = new Label("", skin);
		curScore.setFontScale(0.6f);
		curScore.addAction(forever(run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				score = "" + mode.getScore();
				length = Utils.calStringWidth(score, 80f, 0.6f);
				curScore.setText(score);
				curScore.setPosition(scoreBoard.getX() + scoreBoard.getWidth() / 2 - length / 2,
						scoreBoard.getY() + 40f);
			}

		})));

		scoreGroup.addActor(scoreBoard);
		scoreGroup.addActor(curScore);
		stageUI.addActor(scoreGroup);
	}

	private void createGuideScreen() {
		this.createScoreLabel();
		this.createPauseMenu();

		guideGroup = new Group();
		// Drag text
		dragText = new Image(skin, "drag");
		dragText.setSize(100f, 50f);

		// Tap image
		tapImg = new Image(skin, "tap");

		tapImg.addAction(forever(sequence(repeat(10, sequence(run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tapImg.setPosition(room.getPoints().get(20 + dragNum).x * Const.PPM,
						room.getPoints().get(20 + dragNum).y * Const.PPM);
				dragText.setPosition(tapImg.getX() + 20f, tapImg.getY() + 60f);
				dragNum++;
			}

		}), delay(0.03f))), run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dragNum = 0;
			}

		}), delay(0.5f))));

		guideGroup.addActor(tapImg);
		guideGroup.addActor(dragText);
		guideGroup.setName("guide");
		stageUI.addActor(guideGroup);
	}

	private void createOverScreen() {
		// Keep track of old coin for effect
		final float oldCoin = GamePreferences.instance.gold;
		// Add coin rigth after game over
		GamePreferences.instance.gold += mode.getScore();

		overGroup = new Group();

		final Group scorePane = new Group();
		float delayAll = 1f;
		// Score panel
		scorePanel = new Image(skin, "scorePanel");
		float posX = 160f - scorePanel.getWidth() / 2;
		float posY = Const.GAME_HEIGHT / 2f - scorePanel.getHeight() / 2 + 20f;
		scorePanel.setPosition(posX, Const.GAME_HEIGHT + 30f);
		scorePanel.addAction(sequence(delay(delayAll), moveTo(posX, posY, 0.5f)));
		scorePane.addActor(scorePanel);

		// Image game over
		overImg = new Image(skin, "gameover");
		overImg.setName("gameover");
		overImg.setSize(280f, 100f);
		posX = 160f - overImg.getWidth() / 2;
		posY = Const.GAME_HEIGHT / 2 + 120f;
		overImg.setPosition(posX, scorePanel.getY());
		overImg.addAction(sequence(delay(delayAll), moveTo(posX, posY, 0.9f)));
		overImg.setVisible(false);
		scorePane.addActor(overImg);

		// Image highscore
		highImg = new Image(skin, "highscore");
		highImg.setName("highscore");
		highImg.setSize(280f, 100f);
		posX = 160f - highImg.getWidth() / 2;
		highImg.setPosition(posX, scorePanel.getY());
		highImg.addAction(sequence(delay(delayAll), moveTo(posX, posY, 0.9f)));
		highImg.setVisible(false);
		scorePane.addActor(highImg);

		// Label Score
		String scoreStr = "" + mode.getScore();
		float scoreStrLength = Utils.calStringWidth(scoreStr, 80, 0.6f);
		posX = 160f - scoreStrLength / 2;
		posY = Const.GAME_HEIGHT / 2f - 5f + 20f;
		scoreLabel = new Label(scoreStr, skin);
		scoreLabel.setFontScale(0.6f);
		scoreLabel.setPosition(posX, scorePanel.getY());
		scoreLabel.setColor(Color.MAGENTA);
		scoreLabel.addAction(sequence(delay(delayAll), moveTo(posX, posY, 0.7f)));
		scorePane.addActor(scoreLabel);

		// Label High score
		String highStr = "" + GamePreferences.instance.highscores[mode.getID() - 1];
		float highStrLength = Utils.calStringWidth(highStr, 80, 0.6f);
		posX = 160f - highStrLength / 2;
		posY = Const.GAME_HEIGHT / 2f - 110f + 20f;
		highLabel = new Label(highStr, skin);
		highLabel.setFontScale(0.6f);
		highLabel.setPosition(posX, scorePanel.getY());
		highLabel.setColor(Color.MAGENTA);
		highLabel.addAction(sequence(delay(delayAll), moveTo(posX, posY, 0.7f)));
		scorePane.addActor(highLabel);
		// Add score pane group
		stageUI.addActor(scorePane);

		// Label coin
		float gap = 10f;
		Group coinGroup = new Group();
		coinImg = new Image(skin, "coin");
		coinLabel = new Label("" + oldCoin, skin);
		final Label addCoin = new Label("+ " + mode.getScore(), skin);
		float groupLength = coinImg.getWidth() + Utils.calStringWidth("" + oldCoin, 80, 0.7f) + gap;

		posX = 160f - groupLength / 2;
		posY = Const.GAME_HEIGHT / 2;
		coinImg.setPosition(posX, posY);

		coinLabel.setFontScale(0.7f);
		posX = coinImg.getX() + coinImg.getWidth() + gap;
		posY = coinImg.getY() - 10f;
		coinLabel.setPosition(posX, posY);

		addCoin.setFontScale(0.7f);
		addCoin.setPosition(coinLabel.getX(), coinLabel.getY() - 50f);

		coinGroup.getColor().a = 0;
		float delayToDisplay = delayAll + 2f;
		coinGroup.addAction(sequence(delay(delayToDisplay), run(new Runnable() {
			@Override
			public void run() {
				// fade out score pane to display coin
				scorePane.addAction(fadeOut(0.2f));
				// animate coin group
				addCoin.addAction(
						sequence(delay(0.5f), parallel(moveBy(0f, 120f, 1.5f), scaleTo(1.5f, 1.5f), fadeOut(1.5f))));
				coinLabel.setText("" + GamePreferences.instance.gold);
			}
		}), fadeIn(0.2f)));

		coinGroup.addActor(coinImg);
		coinGroup.addActor(coinLabel);
		coinGroup.addActor(addCoin);

		// Buttons
		Group butGroup = new Group();
		// replay button
		reBut = new Button(skin, "replay");
		reBut.setSize(280f, 50f);
		posX = 160f - reBut.getWidth() / 2;
		float reY = 100f;
		reBut.setPosition(-reBut.getWidth(), reY);
		reBut.addAction(sequence(delay(delayAll), moveTo(posX, reY, 0.5f)));
		reBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if ((Const.gameCount % Const.AD_COUNTER == 0) && Const.gameCount != 0) {
					mode.reset();
					game.handler.showOrLoadInterstital();
					Const.gameCount = 0;
				} else {
					mode.reset();
				}
			}
		});
		butGroup.addActor(reBut);

		// leadBut
		float leadY = 50f;
		leadBut = new Button(skin, "leaderboardtxt");
		leadBut.setSize(280f, 50f);
		posX = 160f - leadBut.getWidth() / 2;
		leadBut.setPosition(320 + leadBut.getWidth(), leadY);
		leadBut.addAction(sequence(delay(delayAll), moveTo(posX, leadY, 0.5f)));
		butGroup.addActor(leadBut);
		leadBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.handler.submitScore(mode.getScore(), mode.getID());
			}

		});

		// Menu
		float reMenuY = 0f;
		reMenuBut = new Button(skin, "reMenu");
		reMenuBut.setSize(280f, 50f);
		posX = 160f - reMenuBut.getWidth() / 2;
		reMenuBut.setPosition(posX, reMenuY - reMenuBut.getHeight());
		reMenuBut.addAction(sequence(delay(delayAll), moveTo(posX, reMenuY, 0.5f)));
		butGroup.addActor(reMenuBut);
		reMenuBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.setScreen(new MenuScreen(game), ScreenTransitionFade.init(0.2f));
			}

		});

		overGroup.addActor(scorePane);
		overGroup.addActor(coinGroup);
		overGroup.addActor(butGroup);
		stageUI.addActor(overGroup);
	}

	private void initAssets() {
		shieldGlow = Assets.instance.texture.shieldGlow;
		iceScreen = Assets.instance.texture.iceScreen;
		frogRegion = frogRegions[GamePreferences.instance.curFrog];
		dieRegion = frogDieRegions[GamePreferences.instance.curFrog];
		// Initiating specific mode
		if (gameWorld.isSpikeMode()) {
			this.initSpikeMode();
		} else if (gameWorld.isLaserMode()) {
			this.initLaserMode();
		} else if (gameWorld.isMummyMode()) {
			this.initMummyMode();
		} else if (gameWorld.isMathMode()) {
			this.initMathMode();
		} else if (gameWorld.isSwatMode()) {
			this.initSwatMode();
		}
	}

	private void initObjects() {
		room = gameWorld.getGameMode().getRoom();
		frog = gameWorld.getGameMode().getFrog();
		obSpawner = gameWorld.getGameMode().getObstacleSpawner();
	}

	private void initSpikeMode() {
		roomRegion = Assets.instance.texture.roomSpike;
		bgRegion = Assets.instance.texture.spikebackground;
	}

	private void initLaserMode() {
		roomRegion = Assets.instance.texture.roomLaser;
		bgRegion = Assets.instance.texture.laserbackground;
		laserBoom = new ParticleEffect();
		laserBoom.load(Gdx.files.internal("particle/smoke.p"), Gdx.files.internal("particle"));
		laserBoom.start();
		laserBoom.scaleEffect(1 / Const.PPM);
	}

	private void initMummyMode() {
		roomRegion = Assets.instance.texture.roomMum;
		bgRegion = Assets.instance.texture.mummybackground;
	}

	private void initMathMode() {
		roomRegion = Assets.instance.texture.roomMath;
		bgRegion = Assets.instance.texture.mathbackground;
		thinkRegion = Assets.instance.texture.mathThink;
	}

	private void initSwatMode() {
		roomRegion = Assets.instance.texture.roomLaser;
		bgRegion = Assets.instance.texture.swatbackground;
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Game drawing
		if (!Const.isDebug) {
			batch.setProjectionMatrix(camGame.combined);
			batch.begin();
			drawBackground(batch);

			if (gameWorld.isSpikeMode()) {
				renderSpikeMode(delta);
			} else if (gameWorld.isLaserMode()) {
				renderLaserMode(delta);
			} else if (gameWorld.isMummyMode()) {
				renderMummyMode(delta);
			} else if (gameWorld.isMathMode()) {
				renderMathMode(delta);
			} else if (gameWorld.isSwatMode()) {
				renderSwatMode(delta);
			}

			Box2DSprite.draw(batch, b2dWorld);

			// Event and Boost update
			if (mode.isRunning()) {
				if (eventHandler != null)
					eventHandler.updateEvent(batch, delta);
				if (boostHandler != null) {
					drawBoost(batch);
					boostHandler.update(batch, delta);
				}
			}

			// UI drawing
			batch.setProjectionMatrix(camUI.combined);
			renderUI(delta);
			batch.end();
		} else {
			debug.render(b2dWorld, camGame.combined);
			shapeRen.setProjectionMatrix(camGame.combined);
			shapeRen.setAutoShapeType(true);
			shapeRen.begin(ShapeType.Line);
			shapeRen.setColor(Color.WHITE);
			int len = obSpawner.getCirclePoints().size;
			// draw circle room
			for (int i = 0; i < len; i++) {
				Vector2 p = obSpawner.getCirclePoints().get(i);
				shapeRen.circle(p.x, p.y, 2f / Const.PPM, 10);
				// draw len col point to room point
				shapeRen.line(Box2dContact.colPoint, obSpawner.getCirclePoints().get(Box2dContact.oppositeIndex));
			}
			// draw collision point
			shapeRen.circle(Box2dContact.colPoint.x, Box2dContact.colPoint.y, 10f / Const.PPM, 10);
			shapeRen.end();
		}
	}

	// Spike Mode rendering
	private void renderSpikeMode(float delta) {
		if (gameWorld.isSpikeMode()) {
			float halfWidthSpikeBg = Assets.instance.texture.roomSpikeBg.getRegionWidth() / 2 / Const.PPM;
			float halfHeightSpikeBg = Assets.instance.texture.roomSpikeBg.getRegionHeight() / 2 / Const.PPM;
			batch.draw(Assets.instance.texture.roomSpikeBg, 160f / Const.PPM - halfWidthSpikeBg,
					Const.GAME_HEIGHT / 2 / Const.PPM - halfHeightSpikeBg, halfWidthSpikeBg, halfHeightSpikeBg,
					2 * halfWidthSpikeBg, 2 * halfHeightSpikeBg, 1, 1, 0);
		}
		gameWorld.getGameMode().getEditorWorld().renderAll(batch, true);
		drawRoom(batch);
		drawFrog(batch);
	}

	// Laser Mode rendering
	private void renderLaserMode(float delta) {
		drawRoom(batch);
		gameWorld.getGameMode().getEditorWorld().renderAll(batch, true);
		drawFrog(batch);
		((LaserMode) mode).getLaserHandler().updateAndRender(delta, batch);
		if (frog.isDied) {
			laserBoom.setPosition(frog.getX(), frog.getY());
			laserBoom.draw(batch, delta);
		}
	}

	// Mummy Mode rendering
	private void renderMummyMode(float delta) {
		drawRoom(batch);
		drawFrog(batch);
	}

	// Math Mode rendering
	private void renderMathMode(float delta) {
		drawRoom(batch);
		drawFrog(batch);
		drawFormula();
	}

	// Swat Mode rendering
	private void renderSwatMode(float delta) {
		drawRoom(batch);
		drawFrog(batch);
	}

	private void renderUI(float delta) {
		if (mode.isGameOver() || mode.isHighScore()) {
			// Hide pause button
			pauseBut.setVisible(false);

			if (!isOverCreated) {
				this.createOverScreen();
				isOverCreated = true;
			} else {
				// Gdx.input.setInputProcessor(stageUI);
				scoreLabel.setText("" + mode.getScore());
				highLabel.setText("" + GamePreferences.instance.highscores[mode.getID() - 1]);

				if (mode.isGameOver()) {
					overImg.setVisible(true);
				}

				if (mode.isHighScore()) {
					highImg.setVisible(true);
				}
			}
		} else {
			if (pauseBut != null)
				pauseBut.setVisible(true);
			if (overGroup != null)
				overGroup.remove();
		}

		if (mode.isGuide()) {
			if (!isGuideCreated) {
				this.createGuideScreen();
				isGuideCreated = true;
			}
		} else
			guideGroup.remove();

		stageUI.act(delta);
		stageUI.draw();
	}

	// Draw frog
	private void drawFrog(Batch batch) {
		float halfWidth = frogRegion.getRegionWidth() / 2 / Const.PPM;
		float halfHeight = frogRegion.getRegionHeight() / 2 / Const.PPM;

		if (!frog.isDied)
			batch.draw(frogRegion, frog.getX() - halfWidth, frog.getY() - halfHeight, halfWidth, halfHeight,
					2 * halfWidth, 2 * halfHeight, 1, 1, frog.getAngle() * MathUtils.radiansToDegrees);
		else
			batch.draw(dieRegion, frog.getX() - halfWidth, frog.getY() - halfHeight, halfWidth, halfHeight,
					2 * halfWidth, 2 * halfHeight, 1, 1, frog.getAngle() * MathUtils.radiansToDegrees);
	}

	// Boost drawing
	private void drawBoost(Batch batch) {
		drawFrogCloner(batch);
		drawShield(batch);
		drawSlow(batch);
	}

	private void drawFrogCloner(Batch batch) {
		if (boostHandler.getCurrentBoost() instanceof X5) {
			X5 x5 = (X5) boostHandler.getCurrentBoost();

			float halfWidth = frogRegion.getRegionWidth() / 2 / Const.PPM;
			float halfHeight = frogRegion.getRegionHeight() / 2 / Const.PPM;

			if (x5.getFrogCloneArray().size != 0)
				for (int i = 0; i < x5.getFrogCloneArray().size; i++) {
					if (x5.getFrogCloneArray().get(i).getBody().getUserData() != null) {
						Frog cloner = x5.getFrogCloneArray().get(i);
						batch.setColor(Color.GREEN);
						batch.draw(frogRegion, cloner.getX() - halfWidth, cloner.getY() - halfHeight, halfWidth,
								halfHeight, 2 * halfWidth, 2 * halfHeight, 1, 1,
								cloner.getAngle() * MathUtils.radiansToDegrees);
						batch.setColor(Color.WHITE);
					}
				}
		}
	}

	private void drawShield(Batch batch) {
		if (boostHandler.getCurrentBoost() instanceof Shield) {

			float halfWidth = shieldGlow.getRegionWidth() / 2 / Const.PPM;
			float halfHeight = shieldGlow.getRegionHeight() / 2 / Const.PPM;

			Shield shield = (Shield) boostHandler.getCurrentBoost();

			if (frog.getMaxLife() == 2) {
				batch.draw(shieldGlow, frog.getX() - halfWidth, frog.getY() - halfHeight, halfWidth, halfHeight,
						2 * halfWidth, 2 * halfHeight, 1, 1, frog.getAngle() * MathUtils.radiansToDegrees);

				batch.setProjectionMatrix(camUI.combined);
				batch.draw(Assets.instance.texture.boostShield,
						160f - Assets.instance.texture.boostShield.getRegionWidth() / 2,
						Const.GAME_HEIGHT / 2 + Assets.instance.texture.boostShield.getRegionHeight() * 2.5f, 40f, 40f);

				Assets.instance.font.small.draw(batch, "" + (6 - shield.getShieldTimer().getRunTimeSeconds()), 180f,
						Const.GAME_HEIGHT / 2 + Assets.instance.texture.boostShield.getRegionHeight() * 3f);
				batch.setProjectionMatrix(camGame.combined);
			}
		}
	}

	private void drawSlow(Batch batch) {
		if (boostHandler.getCurrentBoost() instanceof Slow) {
			if (frog.isSlow) {
				batch.setProjectionMatrix(camUI.combined);
				batch.draw(iceScreen, 0, 0, 320f, Const.GAME_HEIGHT);
				batch.setProjectionMatrix(camGame.combined);
			}
		}
	}

	// Draw room
	private void drawRoom(Batch batch) {
		float halfWidth = roomRegion.getRegionWidth() / 2 / Const.PPM;
		float halfHeight = roomRegion.getRegionHeight() / 2 / Const.PPM;
		batch.draw(roomRegion, 160f / Const.PPM - halfWidth, Const.GAME_HEIGHT / 2 / Const.PPM - halfHeight, halfWidth,
				halfHeight, 2 * halfWidth, 2 * halfHeight, 1, 1, room.getAngle() * MathUtils.radiansToDegrees);
	}

	private void drawBackground(Batch batch) {
		batch.disableBlending();
		batch.draw(bgRegion, (160 - bgRegion.getRegionWidth() / 2) / Const.PPM,
				(Const.GAME_HEIGHT / 2 - bgRegion.getRegionHeight() / 2) / Const.PPM,
				bgRegion.getRegionWidth() / Const.PPM, bgRegion.getRegionHeight() / Const.PPM);
		batch.enableBlending();
	}

	// Draw formula math mode
	private void drawFormula() {
		BitmapFont font = Assets.instance.font.normal;
		MathSpawner spawner = (MathSpawner) obSpawner;
		float width = 100f / Const.PPM;
		float height = 80f / Const.PPM;
		batch.draw(thinkRegion, frog.getX() - 30f / Const.PPM, frog.getY() + 10f / Const.PPM, 0, 0, width, height, 1, 1,
				0);
		batch.setProjectionMatrix(camUI.combined);
		font.setColor(Color.BLACK);
		font.setScale(0.3f);
		// String formula = "" + spawner.getFormula()[0] + (spawner.isADD() ?
		// " + " : " x ")
		// + spawner.getFormula()[1] + " = " + spawner.getFormula()[2];
		String formula = "" + spawner.getInequilvalance()[0] + (spawner.isEQUAL() ? " = " : "")
				+ (spawner.isGREATER() ? " > " : "") + (spawner.isSMALLER() ? " < " : "")
				+ spawner.getInequilvalance()[1];
		float length = Utils.calStringWidth(formula, 80f, 0.3f);
		font.draw(batch, formula, (frog.getX() + width / 2) * Const.PPM - length / 2 - 20f,
				frog.getY() * Const.PPM + 65f);
		batch.setProjectionMatrix(camGame.combined);
	}

	public OrthographicCamera getGameCamera() {
		return camGame;
	}

	public OrthographicCamera getUICamera() {
		return camUI;
	}

	@Override
	public void dispose() {
		batch.dispose();
		if (Const.isDebug) {
			shapeRen.dispose();
			debug.dispose();
		}
		if (laserBoom != null)
			laserBoom.dispose();
	}
}
