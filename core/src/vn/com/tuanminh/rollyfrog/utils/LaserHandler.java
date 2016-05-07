package vn.com.tuanminh.rollyfrog.utils;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class LaserHandler {
	public TweenManager manager;
	public Array<MovableLaser> lasers;
	public final float PPM;

	public LaserHandler(TweenManager manager, float PPM) {
		this.manager = manager;
		this.PPM = PPM;
		lasers = new Array<MovableLaser>();
	}

	public void updateAndRender(float delta, Batch batch) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		for (int i = 0; i < lasers.size; i++) {
			lasers.get(i).updateAndRender(delta, batch);
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void removeAll() {
		lasers.clear();
	}
}
