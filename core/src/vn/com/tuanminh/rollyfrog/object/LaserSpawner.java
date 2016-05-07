package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.mode.BaseGameMode;
import vn.com.tuanminh.rollyfrog.mode.LaserMode;
import vn.com.tuanminh.rollyfrog.utils.AudioManager;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.LaserHandler;
import vn.com.tuanminh.rollyfrog.utils.MovableLaser;
import vn.com.tuanminh.rollyfrog.utils.Utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class LaserSpawner extends ObstacleSpawner {
	private LaserMode laserMode;
	private LaserHandler handler;

	public LaserSpawner(BaseGameMode mode) {
		super(mode);
		laserMode = (LaserMode) this.mode;
		handler = laserMode.getLaserHandler();

		// Spawn gun for the first time
		this.spawnCase(Const.LASER_MAX_ONE_TIME_1, Const.LASER_MIN_GROUP_1, Const.LASER_MAX_GROUP_1,
				Const.LASER_MIN_PER_GROUP_1, Const.LASER_MAX_PER_GROUP_1, true);
	}

	@Override
	public void update(float delta) {
		// Cloner score
		if (Box2dContact.isClonerScore) {
			mode.score();
			Box2dContact.isClonerScore = false;
		}

		// Origin score
		if (Box2dContact.isCollided && !mode.isGameOver()) {
			this.fireLaser();
			mode.score();
			Box2dContact.isCollided = false;
		}
	}

	@Override
	public void spawnCase(int total, int minGroup, int maxGroup, int minPerGroup, int maxPerGroup,
			boolean isFirstTime) {
		// Remove all old spike before spawning
		this.removeObstacles();

		if (isFirstTime) {
			this.statusObstacleFromTo(36, 3, 1);
			this.statusObstacleFromTo(16, 24, 1);
		}

		// Random spike at opposite position for challenge
		int oppoNum = 0;
		oppoNum = MathUtils.random(minPerGroup, maxPerGroup);
		this.spawnAt(Box2dContact.oppositeIndex - oppoNum / 2, oppoNum);

		// Divide all spikes into groups. Loop throught groups to random number
		// of spike per group
		int curNumSpike = total - oppoNum;
		if (curNumSpike > 0) {
			int curNumSpikeGroup = MathUtils.random(minGroup, maxGroup);
			for (int i = 0; i < curNumSpikeGroup; i++) {
				int curSpikePerGroup = MathUtils.random(minPerGroup, maxPerGroup);
				curNumSpike -= curSpikePerGroup;

				if (curNumSpike < 0) {
					this.spawnRandom(curNumSpike + curSpikePerGroup);
				} else {
					this.spawnRandom(curSpikePerGroup);
				}
			}
		}

		if (isFirstTime) {
			this.statusObstacleFromTo(36, 3, 0);
			this.statusObstacleFromTo(16, 24, 0);
		}
	}

	@Override
	public void spawnAt(int startIndex, int numObstacle) {
		for (int i = 0; i < numObstacle; i++) {
			int index = this.convertSlotIndex(startIndex + i);

			if (slot[index] != 0)
				while (slot[index] != 0) {
					index = this.convertSlotIndex(index + numObstacle);
				}

			Vector2 point = randPoints.get(index);

			Gun gun = new Gun(b2dWorld, point.x * Const.PPM, point.y * Const.PPM,
					(Const.CENTER_POINT.sub(point)).angle() - 90, eWorld, mode.getTweenManager());

			// Create revolute joint
			revoDef.initialize(room.body, gun.getEditorBody().getBody(), point);
			b2dWorld.createJoint(revoDef);

			// Add spike to spikes
			obstacles.add(gun);

			// Set slot value equals to 1. It means that this slot is full
			slot[index] = 1;

			// Reset center point
			Const.CENTER_POINT.set(160f / Const.PPM, Const.GAME_HEIGHT / 2 / Const.PPM);
		}
	}

	@Override
	protected void removeObstacles() {
		super.removeObstacles();
		handler.lasers.clear();
	}

	private void fireLaser() {
		for (int i = 0; i < obstacles.size; i++) {
			AudioManager.instance.playSound(Assets.instance.sound.laser);
			MovableLaser laser = new MovableLaser(mode, handler, Assets.instance.texture.laser_bg,
					Assets.instance.texture.laser_over, Utils.randomColor(), ((Gun) obstacles.get(i)));
		}
	}
}
