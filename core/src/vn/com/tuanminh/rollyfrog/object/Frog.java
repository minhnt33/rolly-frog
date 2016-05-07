package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.GameMode.STATE;
import vn.com.tuanminh.rollyfrog.mode.LaserMode;
import vn.com.tuanminh.rollyfrog.mode.MathMode;
import vn.com.tuanminh.rollyfrog.mode.SpikeMode;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Frog extends BaseObject {
	private GameMode mode;
	private float maxSpeed;
	private int maxLife = 1;
	public boolean isDied = false;
	public boolean isSlow = false;
	public boolean isShield = false;

	public Frog(GameMode mode, World world, float x, float y, float radius) {
		super(world, x, y, radius);
		this.mode = mode;
		createBody();
	}

	private void createBody() {
		B2dDef.DefBody defBd = new DefBody();
		CircleShape frogShape = new CircleShape();
		frogShape.setRadius(radius / Const.PPM);
		defBd.setBodyDef(BodyType.DynamicBody, initPos);
		defBd.setFixtureDef(frogShape, Const.FROG_DENSITY, Const.FROG_FRICTION,
				Const.FROG_RESTITUTION);
		defBd.fdef.filter.categoryBits = Const.BIT_FROG;
		defBd.fdef.filter.maskBits = Const.BIT_ROOM | Const.BIT_OBSTACLE
				| Const.BIT_BOOST;
		body = b2dWorld.createBody(defBd.bdef);
		body.createFixture(defBd.fdef);
		frogShape.dispose();
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public void loseLife() {
		this.maxLife -= 1;
	}

	public void slow() {
		body.setLinearVelocity(0, 0);
	}

	public void update(float delta) {
		if (maxLife <= 0) {
			die();
		}

		if (isSlow)
			slow();

		if (mode.isRunning())
			limitSpeedByLevel();
	}

	public void limitSpeedByLevel() {
		Vector2 dir = body.getLinearVelocity();
		float curSpeed = dir.len();
		dir.nor();
		if (curSpeed < maxSpeed) {
			body.setLinearVelocity(dir.scl(maxSpeed));
		}
	}

	public void reset() {
		isDied = false;
		isSlow = false;
		maxLife = 1;
		body.setTransform(initPos, 0);
		body.setAwake(false);
		body.applyForceToCenter(new Vector2(-maxSpeed, 0), true);
	}

	public void die() {
		if (!isDied) {
			b2dWorld.setGravity(new Vector2(0, -9.8f));

			int preHighScore = GamePreferences.instance.highscores[mode.getID() - 1];
			if (mode.getScore() > preHighScore || preHighScore == 0) {
				mode.setState(STATE.HIGH_SCORE);
				GamePreferences.instance.highscores[mode.getID() - 1] = mode
						.getScore();
				GamePreferences.instance.save();
			}
			if (mode.getScore() <= preHighScore && preHighScore != 0) {
				mode.setState(STATE.GAME_OVER);
			}

			if (mode instanceof SpikeMode) {
				Assets.instance.music.spikeMode.stop();
			} else if (mode instanceof LaserMode) {
				Assets.instance.music.laserMode.stop();
			} else if (mode instanceof MathMode) {
				Assets.instance.music.mathMode.stop();
			}

		}
		isDied = true;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}
}
