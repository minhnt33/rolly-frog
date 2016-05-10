package vn.com.tuanminh.rollyfrog.boost;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.frogunnerframe.utils.TimeHelper;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.mode.BaseGameMode;
import vn.com.tuanminh.rollyfrog.object.Frog;
import vn.com.tuanminh.rollyfrog.object.BaseObject;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class SlowBoost extends BaseObject implements Boost {
	private BoostHandler handler;
	private Frog frog;
	private TimeHelper timer;
	public float duration = Const.SLOW_TIME_MILLIS;
	private Vector2 tmpVel;

	public SlowBoost(BaseGameMode mode, BoostHandler handler, World world, float x,
			float y, float radius) {
		super(world, x, y, radius);
		this.handler = handler;
		frog = handler.getFrog();
		timer = new TimeHelper();
		tmpVel = new Vector2();
		this.createBoost();
	}

	@Override
	public void createBoost() {
		B2dDef.DefBody defBd = new DefBody();
		CircleShape shape = new CircleShape();
		shape.setRadius(radius / Const.PPM);
		defBd.setBodyDef(BodyType.StaticBody, initPos);
		defBd.fdef.isSensor = true;
		defBd.setFixtureDef(shape, 1f, 0f, 1f);
		defBd.fdef.filter.categoryBits = Const.BIT_BOOST;
		body = b2dWorld.createBody(defBd.bdef);
		body.createFixture(defBd.fdef);
		body.setUserData(new Box2DSprite(new Sprite(
				Assets.instance.texture.boostSlow)));
	}

	@Override
	public void tween() {
		// TODO Auto-generated method stub

	}

	@Override
	public void boost() {
		tmpVel.set(frog.getBody().getLinearVelocity());
		frog.isSlow = true;
	}

	@Override
	public void update(Batch batch, float delta) {
		if (Box2dContact.isBoost) {
			if (!b2dWorld.isLocked()) {
				b2dWorld.destroyBody(body);
				body = null;
				handler.hasBoost = false;
			}
			boost();
			Box2dContact.isBoost = false;
		}

		if (frog.isSlow) {
			timer.startRunTime();
			if (timer.getRunTimeMillis() > duration) {
				Vector2 vel = frog.getBody().getLinearVelocity().nor()
						.scl(frog.getMaxSpeed());
				frog.getBody().applyForceToCenter(vel, true);
				timer.resetTime();
				frog.isSlow = false;
				frog.getBody().setLinearVelocity(tmpVel);
			}
		}
	}
}
