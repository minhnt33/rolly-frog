package vn.com.tuanminh.rollyfrog.utils;

import vn.com.tuanminh.frogunnerframe.tweens.SpriteAccessor;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.LaserMode;
import vn.com.tuanminh.rollyfrog.object.Gun;
import vn.com.tuanminh.rollyfrog.object.LaserBody;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MovableLaser {
	private LaserMode mode;
	private LaserHandler handler;
	private Sprite bg;
	private Sprite over;
	private final float width;
	private final float height;
	private Vector2 pos;
	private float angle;
	private float duration = 0.15f;
	private Gun gun;
	private LaserBody laserBd;

	public MovableLaser(GameMode mode, LaserHandler handler,
			TextureRegion bgReg, TextureRegion overReg, Color color, Gun gun) {
		this.mode = (LaserMode) mode;
		this.handler = handler;
		bg = new Sprite(bgReg);
		over = new Sprite(overReg);
		width = bgReg.getRegionWidth() / handler.PPM;
		height = bgReg.getRegionHeight() / handler.PPM;
		this.gun = gun;
		pos = new Vector2(gun.getEBodyX() - width / 2, gun.getEBodyY() - width
				/ 2);

		// Set size
		bg.setSize(width, height);
		over.setSize(width, height);

		// Set origin
		bg.setOrigin(width / 2, width / 2);
		over.setOrigin(width / 2, width / 2);

		// Set color
		bg.setColor(color.r, color.g, color.b, 0);
		over.setAlpha(0);

		tween();

		// Laser body
		laserBd = new LaserBody(mode.getBox2DWorld(), 160f,
				Const.GAME_HEIGHT / 2, 4f / Const.PPM, 260f / Const.PPM,
				gun.getEBodyAngle());
		// Revolute joint
		this.mode
				.getObstacleSpawner()
				.getRevoluteJointDef()
				.initialize(gun.getEditorBody().getBody(), laserBd.getBody(),
						gun.getEBodyPosition());
		mode.getBox2DWorld().createJoint(
				this.mode.getObstacleSpawner().getRevoluteJointDef());

		// Add laser into handler
		handler.lasers.add(this);
	}

	public void createLaser() {
		angle = (Const.CENTER_POINT.sub(gun.getEBodyPosition())).angle() - 90;
		pos.set(gun.getEBodyX() - width / 2, gun.getEBodyY() - width / 2);
		bg.setRotation(angle);
		over.setRotation(angle);
		bg.setPosition(pos.x, pos.y);
		over.setPosition(pos.x, pos.y);
		Const.CENTER_POINT.set(160f / Const.PPM, Const.GAME_HEIGHT / 2
				/ Const.PPM);
	}

	public void tween() {
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.to(bg, SpriteAccessor.ALPHA, duration).target(1)
				.ease(TweenEquations.easeOutExpo).repeatYoyo(1, 0)
				.start(handler.manager);
		Tween.to(over, SpriteAccessor.ALPHA, duration).target(1)
				.ease(TweenEquations.easeOutExpo).repeatYoyo(1, 0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (!mode.getBox2DWorld().isLocked()) {
							mode.getBox2DWorld().destroyBody(laserBd.getBody());
						}
						int maxOneTime = 0;
						int maxGroup = 0;
						int minGroup = 0;
						int maxPerGroup = 0;
						int minPerGroup = 0;

						if (mode.isLevel1()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_1;
							maxGroup = Const.LASER_MAX_GROUP_1;
							minGroup = Const.LASER_MIN_GROUP_1;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_1;
							minPerGroup = Const.LASER_MIN_PER_GROUP_1;
						} else if (mode.isLevel2()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_2;
							maxGroup = Const.LASER_MAX_GROUP_2;
							minGroup = Const.LASER_MIN_GROUP_2;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_2;
							minPerGroup = Const.LASER_MIN_PER_GROUP_2;
						} else if (mode.isLevel3()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_3;
							maxGroup = Const.LASER_MAX_GROUP_3;
							minGroup = Const.LASER_MIN_GROUP_3;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_3;
							minPerGroup = Const.LASER_MIN_PER_GROUP_3;
						} else if (mode.isLevel4()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_4;
							maxGroup = Const.LASER_MAX_GROUP_4;
							minGroup = Const.LASER_MIN_GROUP_4;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_4;
							minPerGroup = Const.LASER_MIN_PER_GROUP_4;
						} else if (mode.isLevel5()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_5;
							maxGroup = Const.LASER_MAX_GROUP_5;
							minGroup = Const.LASER_MIN_GROUP_5;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_5;
							minPerGroup = Const.LASER_MIN_PER_GROUP_5;
						} else if (mode.isLevel6()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_6;
							maxGroup = Const.LASER_MAX_GROUP_6;
							minGroup = Const.LASER_MIN_GROUP_6;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_6;
							minPerGroup = Const.LASER_MIN_PER_GROUP_6;
						} else if (mode.isLevel7()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_7;
							maxGroup = Const.LASER_MAX_GROUP_7;
							minGroup = Const.LASER_MIN_GROUP_7;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_7;
							minPerGroup = Const.LASER_MIN_PER_GROUP_7;
						} else if (mode.isLevel8()) {
							maxOneTime = Const.LASER_MAX_ONE_TIME_8;
							maxGroup = Const.LASER_MAX_GROUP_8;
							minGroup = Const.LASER_MIN_GROUP_8;
							maxPerGroup = Const.LASER_MAX_PER_GROUP_8;
							minPerGroup = Const.LASER_MIN_PER_GROUP_8;
						}

						mode.getObstacleSpawner().spawnCase(maxOneTime,
								minGroup, maxGroup, minPerGroup, maxPerGroup,
								false);
					}
				}).start(handler.manager);
	}

	public void updateAndRender(float delta, Batch batch) {
		this.createLaser();
		bg.draw(batch);
		over.draw(batch);
	}

	public float getAngle() {
		return angle;
	}

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}
}
