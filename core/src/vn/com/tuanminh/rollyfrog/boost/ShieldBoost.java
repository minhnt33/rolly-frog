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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class ShieldBoost extends BaseObject implements Boost {
	// private GameMode mode;
	private BoostHandler handler;
	private Frog frog;
	private TimeHelper timer;

	public ShieldBoost(BaseGameMode mode, BoostHandler handler, World world, float x,
			float y, float radius) {
		super(world, x, y, radius);
		// this.mode = mode;
		this.handler = handler;
		frog = handler.getFrog();
		timer = new TimeHelper();
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
				Assets.instance.texture.boostShield)));
	}

	@Override
	public void boost() {
		frog.setMaxLife(2);
	}

	@Override
	public void update(Batch batch, float delta) {
		if (Box2dContact.isBoost) {
			if (!b2dWorld.isLocked()) {
				b2dWorld.destroyBody(body);
				body = null;
				handler.hasBoost = false;
				frog.isShield = true;
			}
			timer.startRunTime();
			boost();
			Box2dContact.isBoost = false;
		}

		if (timer.getRunTimeMillis() > Const.SHIELD_TIME_MILLIS) {
			frog.isShield = false;
			frog.setMaxLife(1);
			timer.resetTime();
		} else {
			if (!frog.isShield) {
				frog.setMaxLife(1);
				timer.resetTime();
			}
		}
	}

	@Override
	public void tween() {

	}

	public TimeHelper getShieldTimer() {
		return timer;
	}
}
