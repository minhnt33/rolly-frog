package vn.com.tuanminh.rollyfrog.object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.utils.Const;

public class MummySpawner extends ObstacleSpawner {

	public MummySpawner(GameMode mode) {
		super(mode);
		// Spawn gun for the first time
		this.spawnCase(Const.LASER_MAX_ONE_TIME_1, Const.LASER_MIN_GROUP_1,
				Const.LASER_MAX_GROUP_1, Const.LASER_MIN_PER_GROUP_1,
				Const.LASER_MAX_PER_GROUP_1, true);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
	}

	@Override
	public void spawnCase(int total, int minGroup, int maxGroup,
			int minPerGroup, int maxPerGroup, boolean isFirstTime) {
		// Remove all old spike before spawning
		this.removeObstacles();
		// Be sure that no spawned spike at collided position
		if (!isFirstTime)
			this.statusObstacleFromTo(Box2dContact.collidedIndex - 3,
					Box2dContact.collidedIndex + 3, 1);
		// No spike opposite frog at first
		if (isFirstTime)
			this.statusObstacleFromTo(18, 22, 1);

		// Random spike at opposite position for challenge
		int oppoNum = MathUtils.random(minPerGroup, maxPerGroup);
		this.spawnAt(Box2dContact.oppositeIndex - oppoNum / 2, oppoNum);

		// Divide all spikes into groups. Loop throught groups to random number
		// of spike per group
		int curNumSpike = total - oppoNum;
		if (curNumSpike > 0) {
			int curNumSpikeGroup = MathUtils.random(minGroup, maxGroup);
			for (int i = 0; i < curNumSpikeGroup; i++) {
				int curSpikePerGroup = MathUtils.random(minPerGroup,
						maxPerGroup);
				curNumSpike -= curSpikePerGroup;

				if (curNumSpike < 0) {
					this.spawnRandom(curNumSpike + curSpikePerGroup);
				} else {
					this.spawnRandom(curSpikePerGroup);
				}
			}
		}

		this.statusObstacleFromTo(Box2dContact.collidedIndex - 3,
				Box2dContact.collidedIndex + 3, 0);
		if (isFirstTime)
			this.statusObstacleFromTo(18, 22, 0);
	}

	@Override
	public void spawnAt(int startIndex, int numObstacle) {
		for (int i = 0; i < numObstacle; i++) {
			int index = this.convertSlotIndex(startIndex + i);
			if (slot[index] == 0) {
				Vector2 point = randPoints.get(index);
				// Save center point to fire laser later

				Anubis hand = new Anubis(b2dWorld, point.x * Const.PPM, point.y
						* Const.PPM, 40f,
						(Const.CENTER_POINT.sub(point)).angle() - 90, eWorld,
						mode.getTweenManager());

				// Create revolute joint
				revoDef.initialize(room.body, hand.getBody(), hand.getBody()
						.getWorldCenter());
				b2dWorld.createJoint(revoDef);

				// Add spike to spikes
				obstacles.add(hand);

				// Set slot value equals to 1. It means that this slot is full
				slot[index] = 1;

				// Reset center point
				Const.CENTER_POINT.set(160f / Const.PPM, Const.GAME_HEIGHT / 2
						/ Const.PPM);
			}
		}
	}

}
