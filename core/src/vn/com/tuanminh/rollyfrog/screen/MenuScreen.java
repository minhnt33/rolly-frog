package vn.com.tuanminh.rollyfrog.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import vn.com.tuanminh.frogunnerframe.screens.AbstractScreen;
import vn.com.tuanminh.frogunnerframe.screens.ScreenManager;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransition;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransitionFade;
import vn.com.tuanminh.frogunnerframe.utils.ResolutionHandler;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen extends AbstractScreen {
	private Stage stage;
	private Skin skin;
	private Button playBut;
	private Button volBut;
	private Button shopBut;
	private Button leaderboardBut;
	private Button muteBut;
	private Image imgBackground;
	private Image rollyText;
	private InputMultiplexer mulInput;

	private Button spikeLBBut;
	private Button laserLBBut;
	private Button mathLBBut;
	private Button backBut;
	private boolean isLeaderboard = false;

	private final float stageHeight = ResolutionHandler.instance
			.getGameHeightWith(480);

	public MenuScreen(ScreenManager game) {
		super(game);
	}

	private void buildStage() {
		skin = Assets.instance.skin.mySkin;

		Table layerBackground = buildBackground();
		Table layerObjects = buildObjects();

		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(480, stageHeight);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stage.addActor(rollyText);
		buildControl();
	}

	private Table buildBackground() {
		Table layer = new Table();
		// background
		layer.bottom().left();
		imgBackground = new Image(skin, "background");
		layer.add(imgBackground);
		return layer;
	}

	private Table buildObjects() {
		Table layer = new Table();
		// rolly text
		rollyText = new Image(skin, "rollyfrog");
		rollyText.setSize(460f, 150f);
		rollyText.setPosition(240f - rollyText.getWidth() / 2, stageHeight / 2
				- rollyText.getHeight() + 320f);

		// spirals
		Vector2 initPos[] = { new Vector2(32, 128), new Vector2(280, 32),
				new Vector2(100, 640), new Vector2(64, 420),
				new Vector2(250, 320), new Vector2(320, 580) };
		for (int i = 0; i < initPos.length; i++) {
			Image spiral = new Image(skin, "spiral");
			float size = MathUtils.random(100, 150);
			spiral.setSize(size, size);
			spiral.setPosition(initPos[i].x, initPos[i].y);
			spiral.setOrigin(spiral.getWidth() / 2, spiral.getHeight() / 2);
			spiral.addAction(forever(rotateBy(MathUtils.random(1, 4))));
			layer.addActor(spiral);
		}
		return layer;
	}

	private void buildControl() {
		float disPerBut = 130f;
		// Play Button
		playBut = new Button(skin, "play");
		playBut.setSize(400f, 100f);
		float playButY = stageHeight / 2 - playBut.getHeight() / 2 + 60f;
		playBut.setPosition(240f - playBut.getWidth() / 2, playButY);
		stage.addActor(playBut);
		playBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked();
			}
		});

		// rate
		shopBut = new Button(skin, "shop");
		shopBut.setSize(400f, 100f);
		shopBut.setPosition(240f - shopBut.getWidth() / 2, playButY - disPerBut);
		stage.addActor(shopBut);
		shopBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onShopClicked();
			}
		});

		// leaderboard
		leaderboardBut = new Button(skin, "leaderboard");
		leaderboardBut.setSize(400f, 100f);
		leaderboardBut.setPosition(240f - leaderboardBut.getWidth() / 2,
				playButY - disPerBut * 2);
		stage.addActor(leaderboardBut);
		leaderboardBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				onLeaderboardClicked();
			}
		});

		// mute
		muteBut = new Button(skin, "mute");
		stage.addActor(muteBut);

		if (GamePreferences.instance.enableVol)
			muteBut.setVisible(false);
		else
			muteBut.setVisible(true);

		muteBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onMuteClicked();
			}
		});

		// Vol
		volBut = new Button(skin, "vol");
		stage.addActor(volBut);
		volBut.setVisible(GamePreferences.instance.enableVol);
		volBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onVolClicked();
			}
		});

		// Leaderboard buts
		// Spike But
		spikeLBBut = new Button(skin, "spikeLB");
		spikeLBBut.setPosition(-spikeLBBut.getWidth(), playBut.getY());
		spikeLBBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.playSound(Assets.instance.sound.click);
				game.handler.showScores(Const.SPIKE_MODE_ID);
			}

		});
		stage.addActor(spikeLBBut);

		// Laser But
		laserLBBut = new Button(skin, "laserLB");
		laserLBBut.setPosition(480f, shopBut.getY());
		laserLBBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.playSound(Assets.instance.sound.click);
				game.handler.showScores(Const.LASER_MODE_ID);
			}

		});
		stage.addActor(laserLBBut);

		// Math But
		mathLBBut = new Button(skin, "mathLB");
		mathLBBut.setPosition(-mathLBBut.getWidth(), leaderboardBut.getY());
		mathLBBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.playSound(Assets.instance.sound.click);
				game.handler.showScores(Const.MATH_MODE_ID);
			}

		});
		stage.addActor(mathLBBut);

		// Back but
		backBut = new Button(skin, "back");
		backBut.setSize(volBut.getWidth(), volBut.getHeight());
		backBut.setPosition(-backBut.getWidth(), 0);
		backBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.playSound(Assets.instance.sound.click);
				leaderboardMove(-1);
			}

		});
		stage.addActor(backBut);
	}

	private void onMuteClicked() {
		AudioManager.instance.playSound(Assets.instance.sound.click);
		setVisible(true, false, true);
		AudioManager.instance.unpauseMusic();
	}

	private void onVolClicked() {
		AudioManager.instance.playSound(Assets.instance.sound.click);
		setVisible(false, true, false);
		AudioManager.instance.stopMusic();
	}

	private void setVisible(boolean volVisible, boolean muteVisible,
			boolean enableVol) {
		volBut.setVisible(volVisible);
		muteBut.setVisible(muteVisible);
		GamePreferences.instance.enableVol = enableVol;
		GamePreferences.instance.save();
	}

	private void onPlayClicked() {
		AudioManager.instance.playSound(Assets.instance.sound.click);
		ScreenTransition transition = ScreenTransitionFade.init(0.2f);
		game.setScreen(new LevelScreen(game), transition);
	}

	private void onShopClicked() {
		AudioManager.instance.playSound(Assets.instance.sound.click);
		game.setScreen(new ShopScreen(game), ScreenTransitionFade.init(0.2f));
	}

	private void onLeaderboardClicked() {
		AudioManager.instance.playSound(Assets.instance.sound.click);
		isLeaderboard = true;
		this.leaderboardMove(1);
	}

	private void leaderboardMove(int dir) {
		float timeMove = 0.5f;
		float disMove = 500f;
		// Move playBut, shopBut, leadboardBut out of view
		playBut.addAction(moveBy(dir * disMove, 0, timeMove));
		shopBut.addAction(moveBy(-dir * disMove, 0, timeMove));
		leaderboardBut.addAction(moveBy(dir * disMove, 0, timeMove));

		// Move 3 leaderboard mode button
		spikeLBBut.addAction(moveBy(dir * (240f + spikeLBBut.getWidth() / 2),
				0, timeMove));
		laserLBBut.addAction(moveBy(-dir * (240f + laserLBBut.getWidth() / 2),
				0, timeMove));
		mathLBBut.addAction(moveBy(dir * (240f + mathLBBut.getWidth() / 2), 0,
				timeMove));

		// Move volume but
		muteBut.addAction(moveBy(dir * (480 - muteBut.getWidth()), 0, timeMove));
		volBut.addAction(moveBy(dir * (480 - volBut.getWidth()), 0, timeMove));

		// Move back but
		backBut.addAction(moveBy(dir * backBut.getWidth(), 0, timeMove));
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		stage = new Stage(new StretchViewport(480, stageHeight));
		Gdx.input.setCatchBackKey(true);
		mulInput = new InputMultiplexer();
		mulInput.addProcessor(stage);
		mulInput.addProcessor(this);
		buildStage();
		AudioManager.instance.playMusic(Assets.instance.music.menu, 0.7f);
	}

	@Override
	public void hide() {
		this.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return mulInput;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
			if (isLeaderboard) {
				this.leaderboardMove(-1);
				isLeaderboard = false;
			} else {
				GamePreferences.instance.save();
				game.handler.showOrLoadInterstital();
				Gdx.app.exit();
			}
		}
		return true;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}