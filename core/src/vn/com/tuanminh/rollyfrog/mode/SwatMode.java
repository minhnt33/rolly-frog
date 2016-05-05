package vn.com.tuanminh.rollyfrog.mode;

import vn.com.tuanminh.rollyfrog.object.SwatSpawner;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.Gdx;

public class SwatMode extends GameMode {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createSpecificStuff() {
		// TODO Auto-generated method stub
		obSpawner = new SwatSpawner(this);
	}

	@Override
	protected void updateGuide(float delta) {
		// TODO Auto-generated method stub
		if (Gdx.input.isTouched()) {
			curState = STATE.RUNNING;
		}
	}

	@Override
	protected void updateRunning(float delta) {
		// TODO Auto-generated method stub
		frog.update(delta);
		room.update(delta);
		obSpawner.update(delta);
	}

	@Override
	protected void updateGameOver(float delta) {
		// TODO Auto-generated method stub
		frog.update(delta);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return Const.SWAT_MODE_ID;
	}

}
