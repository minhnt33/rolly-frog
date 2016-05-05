package vn.com.tuanminh.rollyfrog.mode;

import vn.com.tuanminh.rollyfrog.boost.BoostHandler;
import vn.com.tuanminh.rollyfrog.event.EventHandler;
import vn.com.tuanminh.rollyfrog.object.LaserSpawner;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.LaserHandler;

public class LaserMode extends GameMode {
	private LaserHandler handler;

	public enum LEVEL {
		LV1, LV2, LV3, LV4, LV5, LV6, LV7, LV8
	};

	public LEVEL level;

	@Override
	protected void createSpecificStuff() {
		handler = new LaserHandler(manager, 160f);
		obSpawner = new LaserSpawner(this);
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
		((LaserSpawner) obSpawner).update(delta);

		// Level checking
		if (score < 6) {
			level = LEVEL.LV1;
		} else if (score >= 6 && score < 20) {
			level = LEVEL.LV2;
		} else if (score >= 20 && score < 40) {
			level = LEVEL.LV3;
		} else if (score >= 40 && score < 70) {
			level = LEVEL.LV4;
		} else if (score >= 70 && score < 90) {
			level = LEVEL.LV5;
		} else if (score >= 90) {
			level = LEVEL.LV6;
		}

		if (score >= 6 && score < 40)
			frog.setMaxSpeed(Const.FROG_MAX_SPEED_2);
		else if (score >= 40)
			frog.setMaxSpeed(Const.FROG_MAX_SPEED_3);
	}

	@Override
	protected void updateGameOver(float delta) {
		frog.update(delta);
		obSpawner.update(delta);
		manager.update(delta);
	}

	@Override
	public void dispose() {
		b2dWorld.dispose();
		eventHandler.getEvent().dispose();
	}

	@Override
	public int getID() {
		return Const.LASER_MODE_ID;
	}

	public LaserHandler getLaserHandler() {
		return handler;
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

	public boolean isLevel7() {
		return level == LEVEL.LV7;
	}

	public boolean isLevel8() {
		return level == LEVEL.LV8;
	}
}
