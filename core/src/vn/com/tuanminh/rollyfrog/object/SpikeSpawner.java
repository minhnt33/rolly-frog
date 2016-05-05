package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.SpikeMode;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpikeSpawner extends ObstacleSpawner {
	private SpikeMode mode;
	private int maxOneTime = 0;
	private int maxGroup = 0;
	private int minGroup = 0;
	private int minPerGroup = 0;
	private int maxPerGroup = 0;

	public SpikeSpawner(GameMode mode) {
		super(mode);
		this.mode = (SpikeMode) mode;
		// Spawn for first time
		this.spawnCase(Const.SPIKE_MAX_ONE_TIME_1, Const.SPIKE_MIN_GROUP_1,
				Const.SPIKE_MAX_GROUP_1, Const.SPIKE_MIN_PER_GROUP_1,
				Const.SPIKE_MAX_PER_GROUP_1, true);
	}

	@Override
	public void update(float delta) {
		if (Box2dContact.isCollided) {
			if (mode.isLevel1()) {
				maxOneTime = Const.SPIKE_MAX_ONE_TIME_1;
				maxGroup = Const.SPIKE_MAX_GROUP_1;
				minGroup = Const.SPIKE_MIN_GROUP_1;
				maxPerGroup = Const.SPIKE_MAX_PER_GROUP_1;
				minPerGroup = Const.SPIKE_MIN_PER_GROUP_1;
			} else if (mode.isLevel2()) {
				maxOneTime = Const.SPIKE_MAX_ONE_TIME_2;
				maxGroup = Const.SPIKE_MAX_GROUP_2;
				minGroup = Const.SPIKE_MIN_GROUP_2;
				maxPerGroup = Const.SPIKE_MAX_PER_GROUP_2;
				minPerGroup = Const.SPIKE_MIN_PER_GROUP_2;
			} else if (mode.isLevel3()) {
				maxOneTime = Const.SPIKE_MAX_ONE_TIME_3;
				maxGroup = Const.SPIKE_MAX_GROUP_3;
				minGroup = Const.SPIKE_MIN_GROUP_3;
				maxPerGroup = Const.SPIKE_MAX_PER_GROUP_3;
				minPerGroup = Const.SPIKE_MIN_PER_GROUP_3;
			} else if (mode.isLevel4()) {
				maxOneTime = Const.SPIKE_MAX_ONE_TIME_4;
				maxGroup = Const.SPIKE_MAX_GROUP_4;
				minGroup = Const.SPIKE_MIN_GROUP_4;
				maxPerGroup = Const.SPIKE_MAX_PER_GROUP_4;
				minPerGroup = Const.SPIKE_MIN_PER_GROUP_4;
			} else if (mode.isLevel5()) {
				maxOneTime = Const.SPIKE_MAX_ONE_TIME_5;
				maxGroup = Const.SPIKE_MAX_GROUP_5;
				minGroup = Const.SPIKE_MIN_GROUP_5;
				maxPerGroup = Const.SPIKE_MAX_PER_GROUP_5;
				minPerGroup = Const.SPIKE_MIN_PER_GROUP_5;
			} else {
				maxOneTime = Const.SPIKE_MAX_ONE_TIME_6;
				maxGroup = Const.SPIKE_MAX_GROUP_6;
				minGroup = Const.SPIKE_MIN_GROUP_6;
				maxPerGroup = Const.SPIKE_MAX_PER_GROUP_6;
				minPerGroup = Const.SPIKE_MIN_PER_GROUP_6;
			}

			this.spawnCase(maxOneTime, minGroup, maxGroup, minPerGroup,
					maxPerGroup, false);

			mode.score(1);
			Box2dContact.isCollided = false;
		}

		if (Box2dContact.isClonerScore) {
			mode.score(1);
			Box2dContact.isClonerScore = false;
		}
	}

	@Override
	public void spawnCase(int total, int minGroup, int maxGroup,
			int minPerGroup, int maxPerGroup, boolean isFirstTime) {
		int safeRadius = 2;
		// Remove all old spike before spawning
		this.removeObstacles();
		// Be sure that no spawned spike at collided position
		this.statusObstacleFromTo(Box2dContact.collidedIndex - safeRadius,
				Box2dContact.collidedIndex + safeRadius, 1);
		// No spike opposite frog at first
		if (isFirstTime)
			this.statusObstacleFromTo(14, 26, 1);

		// Random spike at opposite position for challenge
		int oppoNum = 0;
		// if (!isFirstTime) {
		oppoNum = MathUtils.random(minPerGroup, maxPerGroup);
		this.spawnAt(Box2dContact.oppositeIndex - oppoNum / 2, oppoNum);
		// }

		// Divide remaining spikes into groups. Loop throught groups to random
		// number
		// of spike per group
		int curNumSpike = total - oppoNum;
		if (curNumSpike > 0) {
			int curNumSpikeGroup = MathUtils.random(minGroup, maxGroup);

			for (int i = 0; i < curNumSpikeGroup; i++) {
				int curSpikePerGroup = MathUtils.random(minPerGroup,
						maxPerGroup);
				curNumSpike -= curSpikePerGroup;

				if (curNumSpike < 0)
					this.spawnRandom(curNumSpike + curSpikePerGroup);
				else
					this.spawnRandom(curSpikePerGroup);
			}
		}

		this.statusObstacleFromTo(Box2dContact.collidedIndex - safeRadius,
				Box2dContact.collidedIndex + safeRadius, 0);
		// No spike opposite frog at first
		if (isFirstTime)
			this.statusObstacleFromTo(14, 26, 0);
	}

	@Override
	public void spawnAt(int startIndex, int numObstacle) {
		// int startIndex = Box2dContact.desiredIndex;
		// Loop throught point by point to arrange spikes' position
		for (int i = 0; i < numObstacle; i++) {
			int index = this.convertSlotIndex(startIndex + i);
			if (slot[index] != 0) {
				// while (slot[index] != 0 ) {
				// index = this.convertSlotIndex(index + i);
				// }
				index = this.randomAPossibleSlot();
			}

			Vector2 dir = this.getDirectionAt(index);
			Vector2 center = this.getCenterDir(p1, p2);

			// Create a spike
			Spike spike = new Spike(b2dWorld, center.x * Const.PPM, center.y
					* Const.PPM, dir.angle(), eWorld, mode.getTweenManager());

			// Create revolute joint
			revoDef.initialize(room.body, spike.getEditorBody().getBody(),
					center);
			b2dWorld.createJoint(revoDef);

			// Add spike to spikes
			obstacles.add(spike);

			// Set slot value equals to 1. It means that this slot is full
			slot[index] = 1;

		}
	}

	private int randomAPossibleSlot() {
		Array<Integer> possibleSlots = new Array<Integer>();
		for (int i = 0; i < Const.OBSTACLE_TOTAL; i++) {
			if (slot[i] == 0) {
				possibleSlots.add(i);
			}
		}
		return possibleSlots.random();
	}
}
