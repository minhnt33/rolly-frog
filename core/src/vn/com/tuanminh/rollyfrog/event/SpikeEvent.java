package vn.com.tuanminh.rollyfrog.event;

import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SpikeEvent extends Event {
	private Array<ParticleEffect> bubbles;
	private TimeHelper timer;
	private boolean isCreated = true;
	public final int numEffects = 2;
	public final int duration = 2000;
	private final float zoomDuration;

	public SpikeEvent(TweenManager manager) {
		super(manager);
		bubbles = new Array<ParticleEffect>();
		timer = new TimeHelper();
		zoomDuration = camHandler.duration * 1000;
	}

	@Override
	public void createEvent() {
		for (int i = 0; i < numEffects; i++) {
			ParticleEffect effect = new ParticleEffect();
			effect.load(Gdx.files.internal("particle/bubble.p"),
					Gdx.files.internal("particle"));
			effect.scaleEffect(1 / Const.PPM);
			float randX = Const.CENTER_POINT.x + MathUtils.random(-80f, 80f)
					/ Const.PPM;
			float randY = Const.CENTER_POINT.y + MathUtils.random(-80f, 80f)
					/ Const.PPM;
			effect.setPosition(randX, randY);
			effect.start();
			bubbles.add(effect);
		}
	}

	@Override
	public void updateEvent(Batch batch, float delta) {
		if (isCreated) {
			createEvent();
			AudioManager.instance.playSound(Assets.instance.sound.garling);
			isCreated = false;
		}

		if (!isFinished) {
			timer.startRunTime();
			if (timer.getRunTimeMillis() < zoomDuration)
				camHandler.zoom(2f);
			if (timer.getRunTimeMillis() < duration + 2 * zoomDuration) {
				for (int i = 0; i < numEffects; i++) {
					bubbles.get(i).draw(batch, delta);
				}
				camHandler.zoom(1f);
			} else {
				isFinished = true;
			}
		}
	}

	@Override
	public void resetEvent() {
		for (int i = 0; i < numEffects; i++) {
			bubbles.get(i).reset();
		}
		timer.resetTime();
		isFinished = false;
	}

	@Override
	public void dispose() {
		if (bubbles.size != 0)
			for (int i = 0; i < numEffects; i++) {
				bubbles.get(i).dispose();
			}
	}

}
