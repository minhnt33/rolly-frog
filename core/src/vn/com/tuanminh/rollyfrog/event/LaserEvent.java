package vn.com.tuanminh.rollyfrog.event;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class LaserEvent extends Event {
	private World b2dWorld;
	private Body bd;
	private ParticleEffect effect;
	private Vector2 pos = randomInitPos();
	private Vector2 dir;
	private final int timeShake = 4000; // ms
	private final float zoomDuration;
	private final int numSmallMeteor = 10;
	private Sprite sprite;
	private Box2DSprite b2dSprite;
	private Array<Body> smalls;
	private Vector3 initCamPos;

	public LaserEvent(World world, TweenManager manager) {
		super(manager);
		this.b2dWorld = world;
		zoomDuration = camHandler.duration * 1000;
		smalls = new Array<Body>();
	}

	@Override
	public void createEvent() {
		sprite = new Sprite(Assets.instance.texture.meteor);
		b2dSprite = new Box2DSprite(sprite);

		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particle/explosion.p"),
				Gdx.files.internal("particle"));
		effect.scaleEffect(1 / Const.PPM);
		effect.setDuration(5);
		effect.start();

		// Create core meteor
		B2dDef.DefBody bdDef = new DefBody();
		CircleShape meteor = new CircleShape();
		pos = this.randomInitPos();
		meteor.setRadius(Const.METEOR_RADIUS / Const.PPM);
		bdDef.setBodyDef(BodyType.DynamicBody, pos);
		bdDef.setFixtureDef(meteor, 1f, 1f, 1f);
		bdDef.fdef.filter.maskBits = Const.BIT_ROOM;
		bdDef.fdef.filter.categoryBits = Const.BIT_METEOR;
		bd = b2dWorld.createBody(bdDef.bdef);
		bd.createFixture(bdDef.fdef);
		dir = Const.CENTER_POINT.cpy().sub(pos).scl(6f);
		bd.applyForceToCenter(dir, true);
		bd.setUserData(b2dSprite);

		// Create small pieces of meteor
		for (int i = 0; i < numSmallMeteor; i++) {
			float randX = MathUtils.random(-100f, 100f) / Const.PPM;
			Vector2 v = new Vector2(pos.x + randX, pos.y);

			meteor.setRadius(Const.METEOR_RADIUS / MathUtils.random(2, 4)
					/ Const.PPM);
			bdDef.setBodyDef(BodyType.DynamicBody, v);
			bdDef.setFixtureDef(meteor, 1f, 1f, 1f);
			Body small = b2dWorld.createBody(bdDef.bdef);
			Vector2 smallDir = Const.CENTER_POINT.cpy().sub(v).scl(1.3f);
			small.createFixture(bdDef.fdef);
			small.setUserData(b2dSprite);
			small.applyForceToCenter(smallDir.x + randX, smallDir.y, true);
			small.setBullet(true);
			smalls.add(small);
		}

		initCamPos = camHandler.getCamera().position;
	}

	@Override
	public void updateEvent(Batch batch, float delta) {
		if (isCreated) {
			this.createEvent();
			AudioManager.instance.pauseMusic();
			AudioManager.instance.playSound(Assets.instance.sound.alert);
			isCreated = false;
		}

		if (!isFinished) {
			timer.startRunTime();
			if (timer.getRunTimeMillis() < zoomDuration)
				this.camZoomOut();
			if (Box2dContact.isMeteor) {
				effect.setPosition(Box2dContact.colMeteorPoint.x,
						Box2dContact.colMeteorPoint.y);
				effect.draw(batch, delta);
				this.camZoomIn();
				if (timer.getRunTimeMillis() < timeShake + zoomDuration) {
					this.shake();
				} else {
					if (timer.getRunTimeMillis() > timeShake + 2 * zoomDuration) {
						AudioManager.instance.unpauseMusic();
						isFinished = true;
						Box2dContact.isMeteor = false;
					}
				}
			}
		}
	}

	@Override
	public void resetEvent() {
		if (!b2dWorld.isLocked()) {
			b2dWorld.destroyBody(bd);
			for (int i = 0; i < smalls.size; i++)
				b2dWorld.destroyBody(smalls.get(i));
		}
		this.resetCamPos();
		timer.resetTime();
		effect.reset();
		smalls.clear();
		isCreated = true;
		isFinished = false;
	}

	public void camZoomIn() {
		camHandler.zoom(1f);
	}

	public void camZoomOut() {
		camHandler.zoom(1.5f);
	}

	public void resetCamPos() {
		camHandler.getCamera().position.set(initCamPos);
		camHandler.getCamera().update(true);
	}

	public void shake() {
		camHandler.getCamera().position.set(MathUtils.random(-8f, 8f)
				/ Const.PPM + 1, MathUtils.random(-8f, 8f) / Const.PPM
				+ Const.GAME_HEIGHT / 320, 0);
		camHandler.getCamera().update();
	}

	public Vector2 randomInitPos() {
		Vector2 initPos[] = {
				new Vector2(-300f / Const.PPM, Const.CENTER_POINT.y * 2),
				new Vector2(600f / Const.PPM, Const.CENTER_POINT.y * 2),
				new Vector2(Const.CENTER_POINT.x, -300f / Const.PPM),
				new Vector2(Const.CENTER_POINT.x, (Const.GAME_HEIGHT + 200f)
						/ Const.PPM),
				new Vector2(-300f / Const.PPM, -Const.CENTER_POINT.y * 2),
				new Vector2(600f / Const.PPM, -Const.CENTER_POINT.y * 2) };
		return initPos[(MathUtils.random(0, initPos.length - 1))];
	}

	@Override
	public void dispose() {
		if (effect != null)
			effect.dispose();
	}
}
