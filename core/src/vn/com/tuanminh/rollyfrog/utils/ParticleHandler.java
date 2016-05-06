package vn.com.tuanminh.rollyfrog.utils;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class ParticleHandler implements Disposable {
	private HashMap<String, ParticleEffect> effectLoader;
	private Array<ParticleEffect> effects;

	public ParticleHandler() {
		effectLoader = new HashMap<String, ParticleEffect>();
		effects = new Array<ParticleEffect>();
	}

	public void loadEffect(String particlePath, String imagesDir, String key) {
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal(particlePath),
				Gdx.files.internal(imagesDir));
		effectLoader.put(key, effect);
	}

	public void addEffect(String key, float x, float y, int duration) {
		effects.add(effectLoader.get(key));
		effectLoader.get(key).setPosition(x, y);
		effectLoader.get(key).setDuration(duration);
	}

	public void update(Batch batch, float delta) {
		for (int i = 0; i < effects.size; i++) {
			effects.get(i).draw(batch, delta);
		}
	}

	@Override
	public void dispose() {

	}
}
