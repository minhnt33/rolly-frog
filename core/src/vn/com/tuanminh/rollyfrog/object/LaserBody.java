package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class LaserBody extends GeneralObjects {
	private float angle;

	public LaserBody(World world, float x, float y, float width, float height,
			float angle) {
		super(world, x, y, width, height);
		this.angle = angle;
		this.createLaserBody();
	}

	public void createLaserBody() {
		B2dDef.DefBody defBd = new DefBody();
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);
		defBd.setBodyDef(BodyType.DynamicBody, initPos);
		defBd.bdef.angle = angle;
		defBd.setFixtureDef(poly, 1f, 0f, 1f);
		defBd.fdef.isSensor = true;
		defBd.fdef.filter.categoryBits = Const.BIT_OBSTACLE;
		defBd.fdef.filter.maskBits = Const.BIT_FROG | Const.BIT_FROG_CLONER;
		body = b2dWorld.createBody(defBd.bdef);
		body.createFixture(defBd.fdef);
	}
}
