package vn.com.tuanminh.rollyfrog.utils;

import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

public class Box2DRope {
	private World b2dWorld;
	private RevoluteJointDef revoDef;
	private Array<Body> parts;
	private Array<RevoluteJoint> revos;
	private float totalLength;
	private float perPartLength;
	private float disFirstLast = 0;
	private int numPart;
	private float density;
	private float friction;
	private float restitution;
	private Vector2 firstPoint;
	private Vector2 lastPoint;

	public Box2DRope(World world, Vector2 first, Vector2 last, float length, float perLength) {
		b2dWorld = world;
		parts = new Array<Body>();
		firstPoint = first;
		lastPoint = last;
		totalLength = length;
		perPartLength = perLength;
		numPart = (int) (length / perLength);
		revoDef = new RevoluteJointDef();
		revoDef.collideConnected = false;
	}

	public void setRopeProperties(float density, float friction, float restitution) {
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
	}

	private void createRope() {
		float width = perPartLength / Const.PPM;
		float height = 2f / Const.PPM;
		// Create first part
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);
		DefBody defBd = new DefBody();
		defBd.setBodyDef(BodyType.DynamicBody, firstPoint);
		defBd.setFixtureDef(poly, density, friction, restitution);

		for (int i = 0; i < numPart; i++) {
			parts.add(b2dWorld.createBody(defBd.bdef));
			parts.get(i).createFixture(defBd.fdef);
		}
		poly.dispose();

		revoDef.localAnchorA.y = -height / 2;
		revoDef.localAnchorB.y = height / 2;

		for (int i = 0; i < numPart - 1; i++) {
			revoDef.bodyA = parts.get(i);
			revoDef.bodyB = parts.get(i + 1);
			b2dWorld.createJoint(revoDef);
		}
	}
}
