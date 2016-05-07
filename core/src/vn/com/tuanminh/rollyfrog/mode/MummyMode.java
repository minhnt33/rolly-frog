package vn.com.tuanminh.rollyfrog.mode;

import vn.com.tuanminh.rollyfrog.boost.BoostHandler;
import vn.com.tuanminh.rollyfrog.event.EventHandler;
import vn.com.tuanminh.rollyfrog.object.MummySpawner;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.Gdx;

public class MummyMode extends BaseGameMode {

	@Override
	protected void createSpecificStuff() {
		obSpawner = new MummySpawner(this);
		boostHandler = new BoostHandler(this);
		eventHandler = new EventHandler(this, manager);
	}

	@Override
	protected void updateGuide(float delta) {
		if (Gdx.input.isTouched())
			curState = STATE.RUNNING;
	}

	@Override
	protected void updateRunning(float delta) {
		frog.update(delta);
		room.update(delta);
		obSpawner.update(delta);
	}

	@Override
	protected void updateGameOver(float delta) {
		frog.update(delta);
	}

	@Override
	public void reset() {

	}

	@Override
	public int getID() {
		return Const.MUMMY_MODE_ID;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
