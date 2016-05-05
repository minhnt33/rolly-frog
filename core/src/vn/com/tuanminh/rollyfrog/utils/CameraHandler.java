package vn.com.tuanminh.rollyfrog.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraHandler {
	private TweenManager manager;
	private OrthographicCamera camera;
	private Value zoomAmount;
	public float duration = 1f;

	public CameraHandler(TweenManager manager) {
		this.manager = manager;
		Tween.registerAccessor(Value.class, new ValueAccessor());
		zoomAmount = new Value();
		zoomAmount.setValue(1);
	}

	public void setCamera(OrthographicCamera cam) {
		camera = cam;
	}

	public void zoom(float amount) {
		Tween.to(zoomAmount, -1, duration).target(amount)
				.ease(TweenEquations.easeNone).start(manager);
		camera.zoom = zoomAmount.getValue();
		camera.update();
	}

	public void resetCameraZoom() {
		camera.zoom = 1;
		camera.update();
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
}
