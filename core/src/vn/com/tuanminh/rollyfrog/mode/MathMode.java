package vn.com.tuanminh.rollyfrog.mode;

import vn.com.tuanminh.rollyfrog.object.MathSpawner;
import vn.com.tuanminh.rollyfrog.utils.Const;

public class MathMode extends BaseGameMode {
	public enum LEVEL {
		LV1, LV2, LV3, LV4, LV5, LV6
	};

	public LEVEL level;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createSpecificStuff() {
		obSpawner = new MathSpawner(this);
		frog.setMaxSpeed(Const.FROG_MAX_SPEED_1);
		level = LEVEL.LV1;
	}

	@Override
	protected void updateGuide(float delta) {

	}

	@Override
	protected void updateRunning(float delta) {
		frog.update(delta);
		room.update(delta);
		obSpawner.update(delta);

		if (score <= 20)
			level = LEVEL.LV1;
		else if (score > 20 && score <= 40)
			level = LEVEL.LV2;
		else if (score > 40 && score <= 60)
			level = LEVEL.LV3;
		else if (score > 60 && score <= 80)
			level = LEVEL.LV4;
		else if (score > 80 && score <= 100)
			level = LEVEL.LV5;
		else
			level = LEVEL.LV6;

	}

	@Override
	protected void updateGameOver(float delta) {
		frog.update(delta);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return Const.MATH_MODE_ID;
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
