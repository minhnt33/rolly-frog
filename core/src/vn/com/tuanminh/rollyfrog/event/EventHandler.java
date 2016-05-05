package vn.com.tuanminh.rollyfrog.event;

import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.utils.Const;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

public class EventHandler {
	private GameMode mode;
	private Event event;
	private TimeHelper timer;
	public int timeGap = 30000; // ms

	public EventHandler(GameMode mode, TweenManager manager) {
		this.mode = mode;
		timer = new TimeHelper();
		if (mode.getID() == Const.SPIKE_MODE_ID)
			event = new SpikeEvent(mode.getTweenManager());
		else if (mode.getID() == Const.LASER_MODE_ID)
			event = new LaserEvent(mode.getBox2DWorld(), manager);
		else if (mode.getID() == Const.MUMMY_MODE_ID)
			event = new MummyEvent(mode.getTweenManager());
		else if (mode.getID() == Const.MATH_MODE_ID)
			event = new MathEvent(mode.getTweenManager());
	}

	public void updateEvent(Batch batch, float delta) {
		if (mode.getScore() <= 100) {
			timer.startRunTime();
			if (timer.getRunTimeMillis() > timeGap) {
				event.updateEvent(batch, delta);
				if (event.isFinished) {
					event.resetEvent();
					timer.resetTime();
					randomTimeGap();
				}
			}
		}
	}

	public void randomTimeGap() {
		timeGap += MathUtils.random(-10000, 10000);
		if (timeGap < 15000)
			timeGap = 15000;
		if (timeGap > 40000)
			timeGap = 40000;
	}

	public void reset() {
		event.resetEvent();
		timer.resetTime();
		timeGap = 35000;
	}

	public Event getEvent() {
		return event;
	}

	public boolean isFinished() {
		return event.isFinished;
	}

	public boolean isCreated() {
		return event.isCreated;
	}
}
