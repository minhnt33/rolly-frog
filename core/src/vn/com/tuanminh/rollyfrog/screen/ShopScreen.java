package vn.com.tuanminh.rollyfrog.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class ShopScreen extends AbstractScreen {
	private Stage stage;
	private Skin skin;
	private Stack stack;
	private ScrollPane scroller;
	private Array<Image> tickImgs;
	private Label soonLabel;
	private boolean isBuyGoldScreen = false;

	private String nickName[] = Assets.instance.texture.nickName;
	private int frogPrice[] = Assets.instance.texture.frogPrice;
	private final int unlockFrog[] = GamePreferences.instance.unlockFrog;

	private InputMultiplexer input;

	public ShopScreen(ScreenManager game) {
		super(game);
	}

	private void buildShop() {
		stack.setSize(320f, Const.GAME_HEIGHT - 70f);
		Image bg = new Image(skin, "background");
		bg.setSize(320f, Const.GAME_HEIGHT);
		stage.addActor(bg);
		stack.add(buildCoinLabel());
		stack.add(buildFrogItems());
		stage.addActor(stack);
	}

	private Group buildCoinLabel() {
		Group group = new Group();
		Image goldImg = new Image(skin, "coin");
		String coinStr = "" + GamePreferences.instance.gold;
		float length = Utils.calStringWidth(coinStr, 80f, 0.5f);
		float totalLength = length + goldImg.getWidth() + 5;
		final Label goldNum = new Label(coinStr, skin);
		goldNum.setFontScale(0.5f);
		goldImg.setPosition(320f - totalLength - 10f,
				Const.GAME_HEIGHT - goldImg.getHeight() - 10f);
		goldNum.setPosition(goldImg.getX() + goldImg.getWidth() + 5f,
				goldImg.getY() - goldImg.getHeight() / 2 + 5f);
		goldNum.addAction(forever(run(new Runnable() {
			@Override
			public void run() {
				goldNum.setText("" + GamePreferences.instance.gold);
			}
		})));

		Button backBut = new Button(skin, "back");
		backBut.setScale(80f, 80f);
		backBut.setPosition(10f, Const.GAME_HEIGHT - backBut.getHeight() - 5f);
		backBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.setScreen(new MenuScreen(game),
						ScreenTransitionFade.init(0.2f));
			}

		});

		group.addActor(backBut);
		group.addActor(goldImg);
		group.addActor(goldNum);
		return group;
	}

	private Table buildFrogItems() {
		tickImgs = new Array<Image>();
		Table container = new Table();
		container.setFillParent(true);

		Table shop = new Table();
		scroller = new ScrollPane(shop, skin);
		scroller.setSmoothScrolling(true);
		scroller.setFlingTime(0.1f);
		shop.defaults().expand();

		// Button ghostBut = new Button(skin, "button");
		// ghostBut.setVisible(false);
		// shop.add(ghostBut);
		shop.row().pad(10).padLeft(10).space(40f);
		shop.add(buildMoreGoldBut());

		// Frog buttons
		for (int i = 0; i < nickName.length; i++) {
			shop.row().pad(10).padLeft(30).space(40f).fill().expand();
			shop.add(buildFrogBut(frogPrice[i], i, unlockFrog[i] == 0 ? true
					: false));
		}

		// Comming soon Label
		shop.row();
		soonLabel = new Label("Comming Soon", skin);
		soonLabel.setFontScale(0.5f);
		shop.add(soonLabel);

		container.add(scroller).expand().fill();
		return container;
	}

	private Group buildFrogBut(final int price, final int index,
			final boolean isLocked) {
		final Group group = new Group();
		final Image frogImg = new Image(
				Assets.instance.texture.frogRegions[index]);
		final Image lockImg = new Image(skin, "help");
		final Image tickImg = new Image(skin, "yes-up");
		final Label frogPrice = new Label("" + price, skin);
		final Label nameFrog = new Label("" + nickName[index], skin);

		// Button
		Button frogBut = new Button(skin, "button");
		frogBut.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				AudioManager.instance.playSound(Assets.instance.sound.click);
				if (isLocked) {
					if (GamePreferences.instance.gold >= price) {
						if (unlockFrog[index] == 0)
							GamePreferences.instance.gold -= price;
						unlockFrog[index] = 1;
						tickImgs.get(GamePreferences.instance.curFrog).setVisible(false);
						GamePreferences.instance.curFrog = index;
						GamePreferences.instance.save();
						group.addActor(nameFrog);
						// lockImg.setVisible(false);
						frogImg.setVisible(true);
						// frogPrice.setVisible(false);
						tickImg.setVisible(true);
						lockImg.remove();
						frogPrice.remove();
					}
				} else {
					tickImgs.get(GamePreferences.instance.curFrog).setVisible(false);
					GamePreferences.instance.curFrog = index;
					GamePreferences.instance.save();
					// frogPrice.setVisible(false);
					tickImg.setVisible(true);
				}
			}
		});

		// Image
		frogImg.setTouchable(null);
		frogImg.setPosition(
				frogBut.getX() + frogBut.getWidth() - frogImg.getWidth() - 30f,
				frogBut.getY() + 10f);

		lockImg.setTouchable(null);
		lockImg.setSize(44f, 44f);
		lockImg.setPosition(
				frogBut.getX() + frogBut.getWidth() - lockImg.getWidth() - 30f,
				frogBut.getY() + 10f);

		if (isLocked) {
			lockImg.setVisible(true);
			frogImg.setVisible(false);
		} else {
			lockImg.setVisible(false);
			frogImg.setVisible(true);
		}

		tickImg.setTouchable(null);
		tickImg.setSize(30f, 30f);
		tickImg.setPosition(frogBut.getX() + tickImg.getWidth() / 2,
				frogBut.getY() + 20f);
		if (index != GamePreferences.instance.curFrog)
			tickImg.setVisible(false);

		// Label
		frogPrice.setTouchable(null);
		frogPrice.setFontScale(0.5f);
		frogPrice.setPosition(frogBut.getX() + 20f, frogBut.getY());
		nameFrog.setTouchable(null);
		nameFrog.setFontScale(0.4f);
		nameFrog.setPosition(frogBut.getX() + 60f, frogBut.getY());

		group.addActor(frogBut);
		group.addActor(frogImg);
		group.addActor(lockImg);
		if (unlockFrog[index] == 0)
			group.addActor(frogPrice);
		else
			group.addActor(nameFrog);
		group.addActor(tickImg);
		tickImgs.add(tickImg);
		return group;
	}

	private Group buildGoldBut() {
		Group group = new Group();

		Button gold400 = new Button(skin, "gold400");
		float posX = 160f - gold400.getWidth() / 2;
		float posY = Const.GAME_HEIGHT / 2 - 20f;
		float gap = 100f;
		gold400.setPosition(posX, posY);
		gold400.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.handler.buyGold(400);
			}

		});

		Button gold1000 = new Button(skin, "gold1000");
		gold1000.setPosition(posX, posY - gap);
		gold1000.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.handler.buyGold(1000);
			}

		});

		Button gold3000 = new Button(skin, "gold3000");
		gold3000.setPosition(posX, posY - 2 * gap);
		gold3000.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.handler.buyGold(3000);
			}

		});

		Image adsTxt = new Image(skin, "adsTxt");
		adsTxt.setSize(300f, 70f);
		adsTxt.setPosition(160f - adsTxt.getWidth() / 2, gold400.getY()
				+ gold400.getHeight() + 5f);

		group.setColor(group.getColor().r, group.getColor().b,
				group.getColor().g, 0);
		group.addAction(sequence(run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isBuyGoldScreen = true;
				scroller.setVisible(false);
			}

		}), fadeIn(0.4f)));

		group.addActor(adsTxt);
		group.addActor(gold400);
		group.addActor(gold1000);
		group.addActor(gold3000);
		return group;
	}

	private Button buildMoreGoldBut() {
		Button moreGoldBut = new Button(skin, "moregold");
		moreGoldBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if (!isBuyGoldScreen) {
					stack.add(buildGoldBut());
				}
			}

		});
		return moreGoldBut;
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
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
		stage = new Stage(new StretchViewport(320f, Const.GAME_HEIGHT));
		skin = Assets.instance.skin.mySkin;
		stack = new Stack();
		input = new InputMultiplexer();
		input.addProcessor(stage);
		input.addProcessor(this);
		buildShop();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		stage.dispose();
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
