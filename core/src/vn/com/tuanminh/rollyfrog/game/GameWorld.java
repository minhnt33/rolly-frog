package vn.com.tuanminh.rollyfrog.game;

import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.utils.Const;

public class GameWorld {
	private GameRenderer renderer;
	private GameMode mode;

	public GameWorld(GameMode mode) {
		this.mode = mode;
	}

	public void update(float delta) {
		mode.update(delta);
	}

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}

	public void resetWorld() {
		mode.reset();
	}

	public GameRenderer getGameRenderer() {
		return renderer;
	}

	public GameMode getGameMode() {
		return mode;
	}

	public boolean isSpikeMode() {
		return mode.getID() == Const.SPIKE_MODE_ID;
	}

	public boolean isLaserMode() {
		return mode.getID() == Const.LASER_MODE_ID;
	}

	public boolean isMummyMode() {
		return mode.getID() == Const.MUMMY_MODE_ID;
	}

	public boolean isMathMode() {
		return mode.getID() == Const.MATH_MODE_ID;
	}

	public boolean isSwatMode() {
		return mode.getID() == Const.SWAT_MODE_ID;
	}
}
