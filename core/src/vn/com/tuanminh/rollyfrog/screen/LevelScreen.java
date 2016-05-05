package vn.com.tuanminh.rollyfrog.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;
import vn.com.tuanminh.frogunnerframe.screens.AbstractScreen;
import vn.com.tuanminh.frogunnerframe.screens.ScreenManager;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransition;
import vn.com.tuanminh.frogunnerframe.transitions.ScreenTransitionFade;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;
import vn.com.tuanminh.rollyfrog.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class LevelScreen extends AbstractScreen {
	private InputMultiplexer input;
	private Stage stage;
	private Stack stack;
	private Table container;
	private Image bg;
	private Skin skin;
	private Label priceLabel;
	private Label soonLabel;

	public static int modeID = 0;
	// The level name is the name of level button in ui.json
	private final String levelName[] = { "stinkyMouth", "crazySpace",
			"fatherOfMath" };
	// The index of price = mod id - 3 because
	private final int levelPrice[] = { Const.MATH_MODE_PRICE };
	// 2 first mode are ready to play
	private int unlockMode[] = GamePreferences.instance.unlockMode;

	public LevelScreen(ScreenManager game) {
		super(game);
	}

	private void buildStage() {
		skin = Assets.instance.skin.mySkin;
		stack = new Stack();
		stack.setSize(320f, Const.GAME_HEIGHT - 75f); // -75 space for coin
														// label
		container = new Table();
		// Background
		bg = new Image(skin, "background");
		stage.addActor(bg);
		stage.addActor(buildCoinLabel());

		// Scroller
		stack.add(container);
		stage.addActor(stack);
		container.setFillParent(true);

		Table level = new Table();
		level.defaults().expand();
		ScrollPane scroller = new ScrollPane(level, skin);
		scroller.setFlingTime(0.1f);
		scroller.setScrollingDisabled(true, false);

		// loop to create level arcording to id
		float spaceLevelBut = 10f;
		for (int i = 0; i < levelName.length; i++) {
			level.row().pad(10).padLeft(10).spaceBottom(spaceLevelBut);
			level.add(getLevelButton(levelName[i], i + 1)).fill().expandX();
		}

		// Comming soon Label
		level.row().pad(10).padLeft(10).spaceBottom(spaceLevelBut);
		soonLabel = new Label("Comming Soon", skin);
		soonLabel.setFontScale(0.5f);
		level.add(soonLabel);

		container.add(scroller).expand().fill();

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
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		if (!AudioManager.instance.getPlayingMusic().equals(
				Assets.instance.music.menu))
			AudioManager.instance.playMusic(Assets.instance.music.menu, 0.7f);
		Gdx.input.setCatchBackKey(true);
		stage = new Stage(new StretchViewport(320, Const.GAME_HEIGHT));
		input = new InputMultiplexer();
		input.addProcessor(stage);
		input.addProcessor(this);
		buildStage();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private Group buildCoinLabel() {
		Group group = new Group();
		Image goldImg = new Image(skin, "coin");
		String coinStr = "" + GamePreferences.instance.gold;
		float length = Utils.calStringWidth(coinStr, 80f, 0.5f);
		float totalLength = length + goldImg.getWidth() + 5;
		final Label goldNum = new Label(coinStr, skin);
		goldNum.setFontScale(0.5f);
		goldImg.setPosition(320f - totalLength - 10f, Const.GAME_HEIGHT
				- goldImg.getHeight() - 10f);
		goldNum.setPosition(goldImg.getX() + goldImg.getWidth() + 5,
				goldImg.getY() - goldImg.getHeight() / 2);
		goldNum.addAction(forever(run(new Runnable() {
			@Override
			public void run() {
				goldNum.setText("" + GamePreferences.instance.gold);
			}
		})));

		// Back but
		Button backBut = new Button(skin, "back");
		backBut.setScale(80f, 80f);
		backBut.setPosition(10f, Const.GAME_HEIGHT - backBut.getHeight() - 5f);
		backBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.playSound(Assets.instance.sound.click);
				game.setScreen(new MenuScreen(game),
						ScreenTransitionFade.init(0.2f));
			}

		});

		group.addActor(backBut);
		group.addActor(goldImg);
		group.addActor(goldNum);
		return group;
	}

	public Group getLevelButton(String name, final int id) {
		final Group group = new Group();
		final Button button = new Button(skin, name);
		button.setSize(320f, 175f);
		group.setSize(button.getWidth(), button.getHeight());
		group.addActor(button);
		// Set id for easily mode identifying (the same with mode id)
		button.setUserObject(id);

		if (!checkLock(id)) {
			final Button lockBut = new Button(skin, "lock");
			float posX = button.getX() + button.getWidth() / 2
					- lockBut.getWidth() / 2;
			float posY = button.getY() + button.getHeight() / 2
					- lockBut.getHeight() / 2;
			lockBut.setPosition(posX, posY);
			lockBut.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					AudioManager.instance
							.playSound(Assets.instance.sound.click);
					Group window = buildUnlockWindow(button, lockBut, id);
					stack.add(window);
				}
			});

			group.addActor(lockBut);

			// Price Label
			String priceStr = "$" + levelPrice[id - 3];
			// float priceStrLength = Utils.calStringWidth(priceStr, 80f, 0.4f);
			priceLabel = new Label(priceStr, skin);
			priceLabel.setColor(Color.YELLOW);
			priceLabel.setFontScale(0.4f);
			priceLabel.setPosition(button.getX() + 20f, button.getY() + 110f);
			group.addActor(priceLabel);
		} else {
			button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					modeID = (Integer) button.getUserObject();
					ScreenTransition tran = ScreenTransitionFade.init(0.2f);
					game.setScreen(new GameScreen(game), tran);
				}
			});
		}

		return group;
	}

	private boolean checkLock(int id) {
		boolean isUnlocked = false;
		for (int i = 0; i < unlockMode.length; i++) {
			if (unlockMode[i] == id)
				isUnlocked = true;
		}
		return isUnlocked;
	}

	private Group buildUnlockWindow(final Button levelBut,
			final Button lockBut, final int id) {
		final Group table = new Group();
		table.setSize(300f, 160f);
		table.getColor().a = 0;
		table.addAction(fadeIn(0.2f));
		// Background window
		Image winBg = new Image(skin, "panel");
		winBg.setPosition(160f - winBg.getWidth() / 2, Const.GAME_HEIGHT / 2
				- winBg.getHeight() / 2);
		table.addActor(winBg);

		// Label
		String notEnoughStr = "Not enough gold";
		float notEnoughL = notEnoughStr.length() * 17.5f;
		String buyStr = "Unlock for\n" + levelPrice[id - 3] + " gold ?";
		float buyStrL = buyStr.length() * 8f;

		Label label = new Label("", skin);
		label.setText(notEnoughStr);
		label.setPosition(160f - notEnoughL / 2, Const.GAME_HEIGHT / 2);
		table.addActor(label);

		float offsetX = 100f;
		float offsetY = 80f;

		// unlock only if have enough gold
		if (GamePreferences.instance.gold >= levelPrice[id - 3]) {
			// Label setup
			label.setText(buyStr);
			label.setPosition(160f - buyStrL / 2, Const.GAME_HEIGHT / 2);

			// Yes Button
			Button yesBut = new Button(skin, "yes");
			yesBut.setPosition(160f - yesBut.getWidth() / 2 - offsetX,
					Const.GAME_HEIGHT / 2 - yesBut.getHeight() / 2 - offsetY);
			yesBut.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					AudioManager.instance
							.playSound(Assets.instance.sound.click);
					// Remove the price label
					priceLabel.remove();
					// unlock preference
					GamePreferences.instance.unlockLevel(id);
					// decreasing gold
					GamePreferences.instance.gold -= levelPrice[id - 3];
					GamePreferences.instance.save();
					table.addAction(sequence(fadeOut(0.2f), visible(false),
							removeActor()));
					lockBut.remove();
					levelBut.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							AudioManager.instance
									.playSound(Assets.instance.sound.click);
							modeID = (Integer) levelBut.getUserObject();
							ScreenTransition tran = ScreenTransitionFade
									.init(0.2f);
							game.setScreen(new GameScreen(game), tran);
						}
					});
				}
			});
			table.addActor(yesBut);
		}
		// No Button
		Button noBut = new Button(skin, "no");
		noBut.setPosition(160f - noBut.getWidth() / 2
				+ ((GamePreferences.instance.gold >= levelPrice[id - 3]) ? offsetX : 0),
				Const.GAME_HEIGHT / 2 - noBut.getHeight() / 2 - offsetY);
		noBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioManager.instance.playSound(Assets.instance.sound.click);
				table.addAction(sequence(fadeOut(0.3f), visible(false),
						removeActor()));
			}
		});
		table.addActor(noBut);
		label.setFontScale(0.5f);
		return table;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE) {
			ScreenTransition tran = ScreenTransitionFade.init(0.2f);
			game.setScreen(new MenuScreen(game), tran);
		}
		return false;
	}
}
