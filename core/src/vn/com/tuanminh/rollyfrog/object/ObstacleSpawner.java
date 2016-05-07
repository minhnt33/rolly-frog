package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

public abstract class ObstacleSpawner {
	protected GameMode mode;
	protected EditorWorld eWorld;
	protected World b2dWorld;
	protected Room room;
	protected Array<Obstacle> obstacles;
	protected Array<Vector2> randPoints;
	protected Vector2 p1, p2;
	protected int slot[];
	protected RevoluteJointDef revoDef;

	public ObstacleSpawner(GameMode mode) {
		this.mode = mode;
		this.b2dWorld = mode.getBox2DWorld();
		this.eWorld = mode.getEditorWorld();
		this.room = mode.getRoom();
		obstacles = new Array<Obstacle>();
		randPoints = this.room.getPoints();

		// Initializing 2 points
		p1 = new Vector2();
		p2 = new Vector2();

		// Initializing slot values
		slot = new int[Const.OBSTACLE_TOTAL];
		this.freeAllSlot();

		// Revolute joint def
		revoDef = new RevoluteJointDef();
		revoDef.enableLimit = true;
		revoDef.lowerAngle = 0;
		revoDef.upperAngle = 0;
	}

	public abstract void update(float delta);

	public abstract void spawnCase(int total, int minGroup, int maxGroup, int minPerGroup, int maxPerGroup,
			boolean isFirstTime);

	protected abstract void spawnAt(int startIndex, int numObstacle);

	protected void spawnRandom(int numObstacle) {
		int startIndex = MathUtils.random(randPoints.size - 1);
		this.spawnAt(startIndex, numObstacle);
	}

	protected void removeObstacles() {
		// size > 1 because the room is the first editor body and you need to
		// have at least 1 spike to remove
		if (obstacles.size != 0) {
			for (int i = 0; i < obstacles.size; i++) {
				if (!b2dWorld.isLocked()) {
					b2dWorld.destroyBody(((BaseObject) obstacles.get(i)).getEditorBody().getBody());
				}
			}

			eWorld.getEditorBodies().clear();
			obstacles.clear();
			this.freeAllSlot();
		}
	}

	private void freeAllSlot() {
		for (int i = 0; i < Const.OBSTACLE_TOTAL; i++) {
			slot[i] = 0;
		}
	}

	protected void statusObstacleFromTo(int from, int to, int status) {
		from = convertSlotIndex(from);
		to = convertSlotIndex(to);
		if (from < to)
			for (int i = from; i <= to; i++) {
				slot[i] = status;
			}
		else {
			for (int i = from; i < Const.OBSTACLE_TOTAL; i++) {
				slot[i] = status;
			}
			for (int i = 0; i < to; i++) {
				slot[i] = status;
			}
		}
	}

	public int convertSlotIndex(int index) {
		int conIndex = 0;
		if (index >= 40)
			conIndex = index - Const.OBSTACLE_TOTAL;
		else if (index < 0)
			conIndex = Const.OBSTACLE_TOTAL + index;
		else
			conIndex = index;
		return conIndex;
	}

	protected Vector2 getDirectionAt(int index) {
		p1.set(randPoints.get(index));
		p2.set(randPoints.get(this.convertSlotIndex(index + 1)));
		return p2.sub(p1);
	}

	protected Vector2 getCenterDir(Vector2 p1, Vector2 p2) {
		return p1.add(p2);
	}

	public void reset() {
		obstacles.clear();
		freeAllSlot();
		p1.set(Vector2.Zero);
		p2.set(Vector2.Zero);
	}

	public Array<Vector2> getCirclePoints() {
		return randPoints;
	}

	public Array<Obstacle> getObstacleArray() {
		return obstacles;
	}

	public int[] getSlot() {
		return slot;
	}

	public RevoluteJointDef getRevoluteJointDef() {
		return revoDef;
	}
}
