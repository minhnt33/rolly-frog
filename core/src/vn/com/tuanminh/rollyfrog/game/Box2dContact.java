package vn.com.tuanminh.rollyfrog.game;

import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.LaserMode;
import vn.com.tuanminh.rollyfrog.mode.MathMode;
import vn.com.tuanminh.rollyfrog.object.Frog;
import vn.com.tuanminh.rollyfrog.object.MathSpawner;
import vn.com.tuanminh.rollyfrog.object.ObstacleSpawner;
import vn.com.tuanminh.rollyfrog.object.Room;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;

public class Box2dContact implements ContactListener {
	private GameMode mode;
	private Frog frog;
	private Room room;
	private ObstacleSpawner obSpawner;
	public static Vector2 colPoint;
	public static int oppositeIndex = 0;
	public static int collidedIndex = 0;
	public static boolean isCollided = false;
	public static boolean isMeteor = false;
	public static boolean isBoost = false;
	public static boolean isClonerScore = false;
	public static Vector2 colMeteorPoint;
	public Array<Body> dieCloner;
	private int dieCount = 0;

	public Box2dContact(GameMode mode) {
		this.mode = mode;
		frog = mode.getFrog();
		room = mode.getRoom();
		obSpawner = mode.getObstacleSpawner();
		dieCloner = new Array<Body>();
		colPoint = new Vector2();
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if ((fa == null || fb == null))
			return;

		if (mode.isRunning()) {
			// Room
			if (((fa.getFilterData().categoryBits == Const.BIT_FROG)
					&& (fb.getFilterData().categoryBits == Const.BIT_ROOM))
					|| ((fb.getFilterData().categoryBits == Const.BIT_FROG)
							&& (fa.getFilterData().categoryBits == Const.BIT_ROOM))) {
				// Play opop sound
				AudioManager.instance.playSound(Assets.instance.sound.opop);

				// Math mode must call isCollide = true after find collideIndex
				// for correct scoring
				if (!(mode instanceof MathMode))
					isCollided = true;
				WorldManifold manifold = contact.getWorldManifold();
				colPoint = manifold.getPoints()[0];
				collidedIndex = this.getCollidedIndex(colPoint);
				oppositeIndex = this.getOppositeIndex(collidedIndex);

				// Math mode checking
				if (mode instanceof MathMode) {
					MathSpawner.check = ((MathSpawner) obSpawner).checkResult(collidedIndex);
					if (!MathSpawner.check) {
						AudioManager.instance.playSound(Assets.instance.sound.die);
						frog.loseLife();
					}
					isCollided = true;
				}
			}

			// Frog die
			if (((fa.getFilterData().categoryBits == Const.BIT_FROG)
					&& (fb.getFilterData().categoryBits == Const.BIT_OBSTACLE)
					|| ((fb.getFilterData().categoryBits == Const.BIT_FROG)
							&& (fa.getFilterData().categoryBits == Const.BIT_OBSTACLE)))) {
				AudioManager.instance.playSound(Assets.instance.sound.die);
				if (frog.isShield) {
					dieCount++;
				} else {
					Assets.instance.sound.laser.stop();
					Assets.instance.sound.opop.stop();
					if (mode instanceof LaserMode)
						AudioManager.instance.playSound(Assets.instance.sound.boom);
					frog.loseLife();
				}
			}

			// Cloner die
			if ((((fa.getFilterData().categoryBits == Const.BIT_FROG_CLONER)
					&& (fb.getFilterData().categoryBits == Const.BIT_OBSTACLE)
					|| ((fb.getFilterData().categoryBits == Const.BIT_FROG_CLONER)
							&& (fa.getFilterData().categoryBits == Const.BIT_OBSTACLE))))) {
				AudioManager.instance.playSound(Assets.instance.sound.die);
				dieCloner.add(fb.getBody());
			}

			// Cloner score
			if (((fa.getFilterData().categoryBits == Const.BIT_ROOM)
					&& (fb.getFilterData().categoryBits == Const.BIT_FROG_CLONER))
					|| ((fb.getFilterData().categoryBits == Const.BIT_ROOM)
							&& (fa.getFilterData().categoryBits == Const.BIT_FROG_CLONER))) {
				AudioManager.instance.playSound(Assets.instance.sound.opop);
				isClonerScore = true;
			}

			// Meteor
			if (((fa.getFilterData().categoryBits == Const.BIT_ROOM)
					&& (fb.getFilterData().categoryBits == Const.BIT_METEOR))
					|| ((fb.getFilterData().categoryBits == Const.BIT_ROOM)
							&& (fa.getFilterData().categoryBits == Const.BIT_METEOR))) {
				AudioManager.instance.playSound(Assets.instance.sound.boomAsteroid);
				isMeteor = true;
				WorldManifold manifold = contact.getWorldManifold();
				colMeteorPoint = manifold.getPoints()[0];
			}

			// Boost
			if (((fa.getFilterData().categoryBits == Const.BIT_FROG)
					&& (fb.getFilterData().categoryBits == Const.BIT_BOOST))
					|| ((fb.getFilterData().categoryBits == Const.BIT_FROG)
							&& (fa.getFilterData().categoryBits == Const.BIT_BOOST))) {
				AudioManager.instance.playSound(Assets.instance.sound.boost);
				isBoost = true;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		if (dieCount != 0) {
			dieCount = 0;
			frog.isShield = false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
	}

	private int getCollidedIndex(Vector2 colPoint) {
		int collidedIndex = 0;
		float minLen = colPoint.dst(room.getPoints().first());
		// Loop through room points to check what point is nearest the
		// collision point
		for (int i = 0; i < Const.OBSTACLE_TOTAL - 1; i++) {
			float len = colPoint.dst(room.getPoints().get(i));
			if (len < minLen) {
				collidedIndex = i;
				minLen = len;
			}
		}
		return collidedIndex;
	}

	private int getOppositeIndex(int collidedIndex) {
		int oppositeIndex = obSpawner.convertSlotIndex(collidedIndex - Const.OBSTACLE_TOTAL / 2);
		return oppositeIndex;
	}
}
