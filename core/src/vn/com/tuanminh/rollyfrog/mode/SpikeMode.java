package vn.com.tuanminh.rollyfrog.mode;

import vn.com.tuanminh.rollyfrog.boost.BoostHandler;
import vn.com.tuanminh.rollyfrog.event.EventHandler;
import vn.com.tuanminh.rollyfrog.object.SpikeSpawner;
import vn.com.tuanminh.rollyfrog.utils.Const;

public class SpikeMode extends GameMode {
	public enum LEVEL {
		LV1, LV2, LV3, LV4, LV5, LV6
	};

	public LEVEL level;

	@Override
	public void createSpecificStuff() {
		obSpawner = new SpikeSpawner(this);
		boostHandler = new BoostHandler(this);
		eventHandler = new EventHandler(this, manager);
		frog.setMaxSpeed(Const.FROG_MAX_SPEED_1);
	}

	@Override
	protected void updateGuide(float delta) {

	}

	@Override
	protected void updateRunning(float delta) {
		frog.update(delta);
		room.update(delta);
		obSpawner.update(delta);

		// Level checking
		if (score < 20)
			level = LEVEL.LV1;
		else if (score >= 20 && score < 40) {
			level = LEVEL.LV2;
		} else if (score >= 40 && score < 60) {
			level = LEVEL.LV3;
		} else if (score >= 60 && score < 100) {
			level = LEVEL.LV4;
		} else if (score >= 100 && score < 200) {
			level = LEVEL.LV5;
		} else {
			level = LEVEL.LV6;
		}

		if (score >= 6 && score < 30)
			frog.setMaxSpeed(Const.FROG_MAX_SPEED_2);
		else if (score >= 30 && score < 100)
			frog.setMaxSpeed(Const.FROG_MAX_SPEED_3);
		else if (score >= 100 && score < 200)
			frog.setMaxSpeed(Const.FROG_MAX_SPEED_4);
		else if (score >= 200)
			frog.setMaxSpeed(Const.FROG_MAX_SPEED_5);
	}

	@Override
	protected void updateGameOver(float delta) {
		frog.update(delta);
		manager.update(delta);
	}

	@Override
	public void dispose() {
		b2dWorld.dispose();
		eventHandler.getEvent().dispose();
	}

	@Override
	public void reset() {
		super.reset();
		level = LEVEL.LV1;
	}

	@Override
	public int getID() {
		return Const.SPIKE_MODE_ID;
	}

	public boolean isLevel1() {
		return level == LEVEL.LV1;
	}

	public boolean isLevel2() {
		return level == LEVEL.LV2;
	}

	public boolean isLevel3() {
		return level == LEVEL.LV3;
	}

	public boolean isLevel4() {
		return level == LEVEL.LV4;
	}

	public boolean isLevel5() {
		return level == LEVEL.LV5;
	}

	public boolean isLevel6() {
		return level == LEVEL.LV6;
	}
}
