package vn.com.tuanminh.rollyfrog.object;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.mode.BaseGameMode;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

public class MathResultRegion {
	// private GameMode mode;
	private World b2dWorld;
	private Room room;
	private Array<Body> boundBodies;
	private Array<Vector2> boundPoints;
	private RevoluteJointDef revoDef;
	private boolean isRight = false;

	public MathResultRegion(BaseGameMode mode, Array<Vector2> boundPoints) {
		// this.mode = mode;
		b2dWorld = mode.getBox2DWorld();
		room = mode.getRoom();
		this.boundPoints = boundPoints;
		boundBodies = new Array<Body>();
		this.createBoundLine();
	}

	private void createBoundLine() {
		float lineWidth = 160f / Const.PPM;
		float lineHeight = 4f / Const.PPM;

		DefBody defBd = new DefBody();
		PolygonShape lineShape = new PolygonShape();

		revoDef = new RevoluteJointDef();

		for (int i = 0; i < boundPoints.size; i++) {
			Vector2 dir = boundPoints.get(i).cpy().add(Const.CENTER_POINT);
			lineShape.setAsBox(lineWidth / 2, lineHeight / 2);
			defBd.setBodyDef(BodyType.DynamicBody, new Vector2(dir.x / 2, dir.y / 2));
			defBd.setFixtureDef(lineShape, 0.1f, 0f, 0f);
			defBd.fdef.filter.maskBits = 0;
			defBd.bdef.angle = dir.angleRad();

			Body lineBd = b2dWorld.createBody(defBd.bdef);
			lineBd.createFixture(defBd.fdef);
			lineBd.setUserData(new Box2DSprite(new Sprite(Assets.instance.texture.mathLine)));
			boundBodies.add(lineBd);

			// revo joint center vs line
			revoDef.collideConnected = false;
			revoDef.bodyA = room.getCenterPinBody();
			revoDef.bodyB = lineBd;
			revoDef.localAnchorA.set(0, 0);
			revoDef.localAnchorB.set(-lineWidth / 2, 0);
			b2dWorld.createJoint(revoDef);

			// revo joint point vs line
			revoDef.bodyA = room.getBody();
			revoDef.bodyB = lineBd;
			revoDef.localAnchorA.set(boundPoints.get(i));
			revoDef.localAnchorB.set(lineWidth / 2, 0);
			b2dWorld.createJoint(revoDef);
		}
	}

	public boolean checkResult(int collideIndex) {

		return isRight;
	}
}