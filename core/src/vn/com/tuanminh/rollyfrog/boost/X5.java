package vn.com.tuanminh.rollyfrog.boost;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.object.Frog;
import vn.com.tuanminh.rollyfrog.object.GeneralObjects;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class X5 extends GeneralObjects implements Boost {
	private GameMode mode;
	private BoostHandler handler;
	private ParticleEffect effect;
	private Array<Frog> cloners;
	private final int cloneNum = 2;
	private Array<Body> dieCloner;
	private int dieCount = 0;
	private final float initForce = 2.5f;

	public X5(GameMode mode, BoostHandler handler, World world, float x,
			float y, float radius) {
		super(world, x, y, radius);
		this.mode = mode;
		this.handler = handler;
		cloners = new Array<Frog>();
		dieCloner = mode.getContactListener().dieCloner;
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
				Assets.instance.texture.boost2X)));

		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particle/cloner.p"),
				Gdx.files.internal("particle"));
		effect.scaleEffect(1 / Const.PPM);
		effect.start();
	}

	@Override
	public void tween() {
		// TODO Auto-generated method stub

	}

	@Override
	public void boost() {
		Filter filter = new Filter();
		filter.categoryBits = Const.BIT_FROG_CLONER;
		filter.maskBits = Const.BIT_BOOST | Const.BIT_ROOM | Const.BIT_OBSTACLE;

		for (int i = 0; i < cloneNum; i++) {
			Frog frogClone = new Frog(mode, b2dWorld, mode.getFrog().getX()
					* Const.PPM, mode.getFrog().getY() * Const.PPM,
					Const.FROG_RADIUS);
			frogClone.getBody().setUserData(i);
			frogClone.getBody().getFixtureList().first().setFilterData(filter);
			frogClone.getBody().applyForceToCenter(
					new Vector2(initForce * MathUtils.cosDeg(i * 90), initForce
							* MathUtils.sinDeg(i * 90)), true);
			cloners.add(frogClone);
		}
	}

	@Override
	public void update(Batch batch, float delta) {
		if (Box2dContact.isBoost) {
			if (!b2dWorld.isLocked())
				b2dWorld.destroyBody(body);
			boost();
			// handler.hasBoost = false;
			Box2dContact.isBoost = false;
		}

		// Remove cloner body
		for (int i = 0; i < dieCloner.size; i++) {
			if (dieCloner.get(i).getUserData() != null) {
				effect.setPosition(dieCloner.get(i).getPosition().x, dieCloner
						.get(i).getPosition().y);
				effect.draw(batch, delta);
				if (effect.isComplete()) {
					effect.reset();
					b2dWorld.destroyBody(dieCloner.get(i));
					dieCloner.removeIndex(i);
				}
			} else
				dieCloner.removeIndex(i);
		}

		// Remove cloner object
		for (int j = 0; j < cloners.size; j++) {
			if (cloners.get(j).getBody().getUserData() == null) {
				cloners.removeIndex(j);
				dieCount++;
			}
		}

		// No other boost when cloners still be alive
		if (dieCount == cloneNum) {
			handler.hasBoost = false;
			dieCount = 0;
		}
	}

	public Array<Frog> getFrogCloneArray() {
		return cloners;
	}
}
