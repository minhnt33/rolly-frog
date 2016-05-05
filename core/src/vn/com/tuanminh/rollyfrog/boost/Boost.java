package vn.com.tuanminh.rollyfrog.boost;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Boost {
	public void createBoost();
	public void tween();
	public void boost();
	public void update(Batch batch, float delta);
}
