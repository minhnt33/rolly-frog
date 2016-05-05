package vn.com.tuanminh.rollyfrog.game;

import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.GameMode.STATE;
import vn.com.tuanminh.rollyfrog.object.Room;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

public class InputHandler implements InputProcessor {
	private GameWorld gameWorld;
	private GameRenderer renderer;
	private GameMode mode;
	private World b2dWorld;
	// private Frog frog;
	private Room room;
	// private ObstacleSpawner obSpawner;
	private MouseJointDef jointDef;
	private Array<MouseJoint> mouseJoints;
	private final int numJoint = 20;
	private Vector3 testPoint;
	private QueryCallback cb;

	public InputHandler(GameWorld gameWorld, GameRenderer renderer) {
		this.gameWorld = gameWorld;
		this.renderer = renderer;
		mode = gameWorld.getGameMode();
		b2dWorld = mode.getBox2DWorld();
		// frog = mode.getFrog();
		room = mode.getRoom();
		// obSpawner = mode.getObstacleSpawner();
		testPoint = new Vector3();
		mouseJoints = new Array<MouseJoint>();
		cb = new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				if (fixture.testPoint(testPoint.x, testPoint.y)) {
					jointDef.bodyB = room.getGhostBody();
					for (int i = 0; i < numJoint; i++) {
						jointDef.target.set(
								testPoint.x + MathUtils.random(-10f, 10f)
										/ Const.PPM,
								testPoint.y + MathUtils.random(-10f, 10f)
										/ Const.PPM);
						MouseJoint joint = (MouseJoint) b2dWorld
								.createJoint(jointDef);
						mouseJoints.add(joint);
					}
					return false;
				} else
					return true;
			}
		};

		// mouse joint
		jointDef = new MouseJointDef();
		jointDef.bodyA = room.getCenterPinBody();
		jointDef.frequencyHz = 1 / Const.TIME_STEP;
		jointDef.maxForce = 500 * room.getGhostBody().getMass();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (mode.isGuide()) {
			AudioManager.instance.playSound(Assets.instance.sound.letsgo);
			mode.setState(STATE.RUNNING);
		}

		if (mode.isRunning()) {
			// if (Gdx.input.getX() > 160f) {
			// room.rotateRoom(-Const.ROOM_OMEGA);
			// } else {
			// room.rotateRoom(Const.ROOM_OMEGA);
			// }

			room.freeRotate();
			renderer.getGameCamera().unproject(
					testPoint.set(screenX, screenY, 0));
			gameWorld
					.getGameMode()
					.getBox2DWorld()
					.QueryAABB(cb, testPoint.x - 0.01f, testPoint.y - 0.01f,
							testPoint.x + 0.01f, testPoint.y + 0.01f);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (mode.isRunning()) {
			room.stopRotate();
			if (mouseJoints.size != 0) {
				for (int i = 0; i < numJoint; i++) {
					// if (!b2dWorld.isLocked())
					b2dWorld.destroyJoint(mouseJoints.get(i));
				}
				mouseJoints.clear();
			}
		}
		return false;
	}

	Vector2 target = new Vector2();

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		// Start game
		if (mode.isGuide())
			mode.setState(STATE.RUNNING);

		renderer.getGameCamera().unproject(testPoint.set(screenX, screenY, 0));
		if (mouseJoints.size != 0) {
			for (int i = 0; i < numJoint; i++) {
				mouseJoints.get(i).setTarget(
						target.set(testPoint.x, testPoint.y));
			}

			// if (target.dst2(Const.CENTER_POINT) > Math.pow(Const.ROOM_RADIUS
			// / Const.PPM, 2))
			// room.stopRotate();
			// else
			// room.freeRotate();
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		renderer.getGameCamera().zoom -= amount;
		renderer.getGameCamera().update();
		return false;
	}
}
