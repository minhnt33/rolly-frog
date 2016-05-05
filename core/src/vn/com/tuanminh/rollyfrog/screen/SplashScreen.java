package vn.com.tuanminh.rollyfrog.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import vn.com.tuanminh.frogunnerframe.screens.AbstractScreen;
import vn.com.tuanminh.frogunnerframe.screens.ScreenManager;
import vn.com.tuanminh.frogunnerframe.utils.ResolutionHandler;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SplashScreen extends AbstractScreen {
	private Stage stage;
	private Image splash;

	public SplashScreen(ScreenManager game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
		stage.act(deltaTime);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		float stageWidth = 480f;
		float stageHeight = ResolutionHandler.instance
				.getGameHeightWith(stageWidth);
		stage = new Stage(new FitViewport(stageWidth, stageHeight));
		Texture splashTex = new Texture(
				Gdx.files.internal("texture/splash.png"));
		splashTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		splash = new Image(splashTex);
		splash.setPosition(stageWidth / 2 - splash.getWidth() / 2, stageHeight
				/ 2 - splash.getHeight() / 2);
		splash.getColor().a = 0;
		splash.addAction(sequence(fadeIn(0.3f)));
		stage.addActor(splash);

		splash.addAction(sequence(fadeIn(0.3f), run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GamePreferences.instance.load();
				Assets.instance.init(new AssetManager());
			}

		}), delay(1f), fadeOut(0.3f), run(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				game.setScreen(new MenuScreen(game));
			}

		})));
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
		return null;
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
