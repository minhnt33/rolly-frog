package vn.com.tuanminh.rollyfrog.screen;

import vn.com.tuanminh.frogunnerframe.screens.AbstractScreen;
import vn.com.tuanminh.frogunnerframe.screens.ScreenManager;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransition;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransitionFade;
import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.game.GameRenderer;
import vn.com.tuanminh.rollyfrog.game.GameWorld;
import vn.com.tuanminh.rollyfrog.game.InputHandler;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.GameMode.STATE;
import vn.com.tuanminh.rollyfrog.mode.LaserMode;
import vn.com.tuanminh.rollyfrog.mode.MathMode;
import vn.com.tuanminh.rollyfrog.mode.MummyMode;
import vn.com.tuanminh.rollyfrog.mode.SpikeMode;
import vn.com.tuanminh.rollyfrog.mode.SwatMode;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;
import vn.com.tuanminh.rollyfrog.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameScreen extends AbstractScreen {
	private GameMode mode;
	private GameWorld gameWorld;
	private GameRenderer renderer;
	private InputHandler gameInput;
	private Stage stageUI;
	private InputMultiplexer input;

	// Back Dialog
	private Label dialogLabel;
	private Image dialogPanel;
	private Button yesBut;
	private Button noBut;
	private TimeHelper timer;
	private Skin skin = Assets.instance.skin.mySkin;
	private Label reCountLabel;

	public GameScreen(ScreenManager game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float deltaTime) {
		if (gameWorld.getGameMode().isReset) {
			ScreenTransition tran = ScreenTransitionFade.init(0.2f);
			game.setScreen(new GameScreen(game), tran);
			gameWorld.getGameMode().isReset = false;
		}

		gameWorld.update(deltaTime);
		renderer.render(deltaTime);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Const.gameCount++;
		Gdx.input.setCatchBackKey(true);
		Music bgMusic = null;
		if (LevelScreen.modeID == Const.SPIKE_MODE_ID) {
			mode = new SpikeMode();
			bgMusic = Assets.instance.music.spikeMode;
		} else if (LevelScreen.modeID == Const.LASER_MODE_ID) {
			mode = new LaserMode();
			bgMusic = Assets.instance.music.laserMode;
		} else if (LevelScreen.modeID == Const.MATH_MODE_ID) {
			mode = new MathMode();
			bgMusic = Assets.instance.music.mathMode;
		} else if (LevelScreen.modeID == Const.MUMMY_MODE_ID)
			mode = new MummyMode();
		else if (LevelScreen.modeID == Const.SWAT_MODE_ID)
			mode = new SwatMode();

		AudioManager.instance.playMusic(bgMusic, 0.7f);

		stageUI = new Stage(new StretchViewport(320f, Const.GAME_HEIGHT));
		gameWorld = new GameWorld(mode);
		renderer = new GameRenderer(gameWorld, stageUI, game);
		gameInput = new InputHandler(gameWorld, renderer);
		input = new InputMultiplexer();
		input.addProcessor(this);
		input.addProcessor(stageUI);
		input.addProcessor(gameInput);

		timer = new TimeHelper();
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
		return input;
	}

	@Override
	public void resume() {
		// Assets.instance.init(new AssetManager());
	}

	@Override
	public void dispose() {
		renderer.dispose();
		gameWorld.getGameMode().dispose();
		// Assets.instance.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
			if (mode.isRunning()) {
				AudioManager.instance.pauseMusic();
				mode.setState(STATE.PAUSE);
				this.createBackDialog();
			}
		}
		return false;
	}

	private void createBackDialog() {
		final Group group = new Group();
		// Countdown label
		reCountLabel = new Label("" + timer.getRunTimeSeconds(), skin);
		reCountLabel.setColor(Color.YELLOW);
		reCountLabel.setVisible(false);
		reCountLabel.setFontScale(0.8f);
		reCountLabel.setPosition(160f - 10f, Const.GAME_HEIGHT / 2 - 10f);

		// Panel
		dialogPanel = new Image(skin, "panel");
		dialogPanel.setSize(280f, 200f);
		float panelX = 160f - dialogPanel.getWidth() / 2;
		float panelY = Const.GAME_HEIGHT / 2 - dialogPanel.getHeight() / 2;
		dialogPanel.setPosition(panelX, panelY);

		// Yes
		yesBut = new Button(skin, "yes");
		yesBut.setPosition(panelX + 30f, panelY + 10f);
		yesBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				GamePreferences.instance.gold += mode.getScore();
				GamePreferences.instance.save();
				ScreenTransition tran = ScreenTransitionFade.init(0.2f);
				game.setScreen(new MenuScreen(game), tran);
			}

		});

		// No
		noBut = new Button(skin, "no");
		noBut.setPosition(panelX + dialogPanel.getWidth() - noBut.getWidth()
				- 30f, panelY + 10f);
		noBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				dialogPanel.setVisible(false);
				dialogLabel.setVisible(false);
				yesBut.setVisible(false);
				noBut.setVisible(false);
				reCountLabel.setVisible(true);

				timer.startRunTime();
				reCountLabel.addAction(forever(run(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						reCountLabel.setText(""
								+ (3 - timer.getRunTimeSeconds()));
						if (timer.getRunTimeMillis() > Const.PAUSED_TIME) {
							AudioManager.instance.unpauseMusic();
							mode.setState(STATE.RUNNING);
							timer.resetTime();
							group.remove();
						}
					}

				})));
			}

		});

		// Quit message
		String quit = "Really quit?";
		float length = Utils.calStringWidth(quit, 80f, 0.5f);
		dialogLabel = new Label(quit, skin);
		dialogLabel.setFontScale(0.65f);
		dialogLabel.setPosition(160f - length / 2, Const.GAME_HEIGHT / 2);

		group.addActor(reCountLabel);
		group.addActor(dialogPanel);
		group.addActor(yesBut);
		group.addActor(noBut);
		group.addActor(dialogLabel);
		stageUI.addActor(group);
	}
}
