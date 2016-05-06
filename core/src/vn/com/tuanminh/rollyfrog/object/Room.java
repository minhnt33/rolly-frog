package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dUtils;
import vn.com.tuanminh.frogunnerframe.utils.Algorithms;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

public class Room extends GeneralObjects {
	private GameMode mode;
	private Array<Vector2> roomPoints;
	private Body handBd;
	private Body ghostBd;
	private Body fixedChain;
	private Body centerPin;
	private Vector2 tmp;
	private MassData roomMass;

	public Room(World world, float x, float y, float radius, GameMode mode) {
		super(world, x, y, radius);
		this.mode = mode;
		tmp = new Vector2();
		createBody();
	}

	private void createBody() {
		B2dDef.DefBody defBd = new DefBody();

		// get initial vertices
		roomPoints = Algorithms.MakingCircle.useTrigonometry(
				Const.OBSTACLE_TOTAL, initPos, Const.SPIKE_RADIUS / Const.PPM);

		// Create room body outside
		ChainShape chain = new ChainShape();
		chain.createLoop((Vector2[]) roomPoints.toArray(Vector2.class));

		defBd.bdef.type = BodyType.DynamicBody;
		defBd.setFixtureDef(chain, Const.ROOM_DENSITY, Const.ROOM_FRICTION,
				Const.ROOM_RESTITUTION);
		defBd.fdef.filter.categoryBits = Const.BIT_ROOM;
		defBd.fdef.filter.maskBits = 0;
		body = b2dWorld.createBody(defBd.bdef);
		body.createFixture(defBd.fdef);
		roomMass = B2dUtils.calculateMassData(roomPoints, Const.ROOM_DENSITY);
		body.setMassData(roomMass);
		chain.dispose();

		// Create room body inside
		ChainShape staticChain = new ChainShape();
		staticChain.createLoop((Vector2[]) Algorithms.MakingCircle
				.useTrigonometry(Const.OBSTACLE_TOTAL, initPos,
						(Const.SPIKE_RADIUS - 6) / Const.PPM).toArray(
						Vector2.class));
		defBd.bdef.type = BodyType.StaticBody;
		defBd.setFixtureDef(staticChain, Const.ROOM_DENSITY,
				Const.ROOM_FRICTION, Const.ROOM_RESTITUTION);
		defBd.fdef.filter.categoryBits = Const.BIT_ROOM;
		defBd.fdef.filter.maskBits = Const.BIT_FROG | Const.BIT_FROG_CLONER
				| Const.BIT_METEOR;
		fixedChain = b2dWorld.createBody(defBd.bdef);
		fixedChain.createFixture(defBd.fdef);
		staticChain.dispose();

		// Create hand body
		// PolygonShape p = new PolygonShape();
		// p.setAsBox(Const.ROOM_RADIUS / Const.PPM, 2f / Const.PPM, new
		// Vector2(
		// 0, 0), 0);
		// defBd.setBodyDef(BodyType.DynamicBody, new Vector2(160f / Const.PPM,
		// Const.GAME_HEIGHT / 2 / Const.PPM));
		// defBd.setFixtureDef(p, 0.5f, 0f, 0f);
		// defBd.fdef.filter.maskBits = 0;
		// handBd = b2dWorld.createBody(defBd.bdef);
		// handBd.createFixture(defBd.fdef);
		// p.dispose();

		// Revolute joint for connecting hand and room
		RevoluteJointDef revoDef = new RevoluteJointDef();
		// revoDef.initialize(handBd, body, roomPoints.first());
		// b2dWorld.createJoint(revoDef);
		// revoDef.initialize(handBd, body, roomPoints.get(roomPoints.size /
		// 2));
		// b2dWorld.createJoint(revoDef);

		// Ghost body for draging room
		CircleShape ghostShape = new CircleShape();
		ghostShape.setRadius((Const.ROOM_RADIUS + 120f) / Const.PPM);
		defBd.setBodyDef(BodyType.DynamicBody, initPos);
		defBd.setFixtureDef(ghostShape, 0.5f, 0f, 0f);
		defBd.fdef.filter.maskBits = 0;
		ghostBd = b2dWorld.createBody(defBd.bdef);
		ghostBd.createFixture(defBd.fdef);
		ghostBd.setUserData("ghostRoom");
		ghostShape.dispose();

		// Create joint to pin the ghost with room
		revoDef.initialize(ghostBd, body, roomPoints.first());
		b2dWorld.createJoint(revoDef);
		revoDef.initialize(ghostBd, body, roomPoints.get(roomPoints.size / 2));
		b2dWorld.createJoint(revoDef);

		// revoDef.initialize(handBd, ghostBd, roomPoints.first());
		// b2dWorld.createJoint(revoDef);
		// revoDef.initialize(handBd, ghostBd, roomPoints.get(roomPoints.size /
		// 2));
		// b2dWorld.createJoint(revoDef);

		// revoDef.initialize(handBd, ghostBd, roomPoints.get(roomPoints.size /
		// 4));
		// b2dWorld.createJoint(revoDef);
		// revoDef.initialize(handBd, ghostBd,
		// roomPoints.get(3 * roomPoints.size / 4));
		// b2dWorld.createJoint(revoDef);

		defBd.setBodyDef(BodyType.StaticBody, initPos);
		centerPin = b2dWorld.createBody(defBd.bdef);

		// revoDef.initialize(handBd, centerPin, Const.CENTER_POINT);
		// b2dWorld.createJoint(revoDef);
		revoDef.initialize(ghostBd, centerPin, Const.CENTER_POINT);
		b2dWorld.createJoint(revoDef);
		revoDef.initialize(body, centerPin, Const.CENTER_POINT);
		b2dWorld.createJoint(revoDef);
	}

	public void update(float delta) {
		for (int i = 0; i < roomPoints.size; i++) {
			ChainShape chain = (ChainShape) body.getFixtureList().first()
					.getShape();
			chain.getVertex(i, tmp);
			roomPoints.get(i).set(body.getWorldPoint(tmp));
		}

		if (mode.getFrog().isDied)
			this.stopRotate();
	}

	public void rotateRoom(float omega) {
		handBd.setAngularVelocity(omega);
	}

	public void stopRotate() {
		// handBd.setFixedRotation(true);
		ghostBd.setFixedRotation(true);
		// body.setFixedRotation(true);
	}

	public void freeRotate() {
		// body.setMassData(roomMass);
		// handBd.setFixedRotation(false);
		ghostBd.setFixedRotation(false);
		// body.setFixedRotation(false);
	}

	public Body getHandBody() {
		return handBd;
	}

	public Body getGhostBody() {
		return ghostBd;
	}

	public Body getStaticChainInsideRoom() {
		return fixedChain;
	}

	public Body getCenterPinBody() {
		return centerPin;
	}

	public Array<Vector2> getPoints() {
		return roomPoints;
	}
}
