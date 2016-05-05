package vn.com.tuanminh.rollyfrog.boost;

import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.object.Frog;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BoostHandler {
	private GameMode mode;
	private Boost curBoost;
	private World b2dWorld;
	private TimeHelper timer;
	private Frog frog;
	private Vector2 pos;
	public float timeGap = 20000; // ms
	public boolean hasBoost = false;

	public BoostHandler(GameMode mode) {
		this.mode = mode;
		b2dWorld = mode.getBox2DWorld();
		frog = mode.getFrog();
		timer = new TimeHelper();
		pos = new Vector2();
	}

	public void update(Batch batch, float delta) {
		timer.startRunTime();
		if (timer.getRunTimeMillis() > timeGap)
			if (!hasBoost) {
				randomBoost();
			} else {
				timer.resetTime();
				randomTimeGap();
			}

		if (curBoost != null) {
			curBoost.update(batch, delta);
		}
	}

	public void randomTimeGap() {
		timeGap += MathUtils.random(-5000, 5000);
		if (timeGap > 30000)
			timeGap = 30000;
		if (timeGap < 10000)
			timeGap = 10000;
	}

	public void randomBoost() {
		pos.set(Const.CENTER_POINT.x * Const.PPM + MathUtils.random(-70f, 70f),
				Const.CENTER_POINT.y * Const.PPM + MathUtils.random(-70f, 70f));
		int i = MathUtils.random(1, 3);
		if (i == 1) {
			curBoost = new Shield(mode, this, b2dWorld, pos.x, pos.y, 20f);
		} else if (i == 2) {
			curBoost = new X5(mode, this, b2dWorld, pos.x, pos.y, 20f);
		} else {
			curBoost = new Slow(mode, this, b2dWorld, pos.x, pos.y, 20f);
		}
		hasBoost = true;
	}

	public void reset() {
		hasBoost = false;
		timer.resetTime();
		curBoost = null;
		timeGap = 20000;
	}

	public Boost getCurrentBoost() {
		return curBoost;
	}

	public Frog getFrog() {
		return frog;
	}
}
