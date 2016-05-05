package vn.com.tuanminh.rollyfrog.event;

import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.utils.CameraHandler;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public abstract class Event implements Disposable{
	protected CameraHandler camHandler;
	protected TimeHelper timer;
	public boolean isFinished = false;
	public boolean isCreated = true;

	public Event(TweenManager manager) {
		camHandler = new CameraHandler(manager);
		timer = new TimeHelper();
	}

	public abstract void createEvent();

	public abstract void updateEvent(Batch batch, float delta);

	public abstract void resetEvent();

	public CameraHandler getCameraHandler() {
		return camHandler;
	}
}
