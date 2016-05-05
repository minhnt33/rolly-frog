package vn.com.tuanminh.rollyfrog.object;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef;
import vn.com.tuanminh.frogunnerframe.box2dhelper.B2dDef.DefBody;
import vn.com.tuanminh.frogunnerframe.tweens.SpriteAccessor;
import vn.com.tuanminh.rollyfrog.game.Assets;
import vn.com.tuanminh.rollyfrog.utils.Const;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Anubis extends GeneralObjects implements Obstacle {
	private float angle;
	private Sprite sprite;

	public Anubis(World world, float x, float y, float radius,
			float angle, EditorWorld eWorld, TweenManager manager) {
		super(world, x, y, radius);
		this.angle = angle;
		this.createObstacleBody();

		// Tweening
		sprite.setAlpha(0);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.to(sprite, SpriteAccessor.ALPHA, 0.5f).target(1)
				.start(manager);
	}

	@Override
	public void createObstacleBody() {
		CircleShape shape = new CircleShape();
		shape.setRadius(radius / Const.PPM);
		B2dDef.DefBody defBd = new DefBody();
		defBd.setBodyDef(BodyType.DynamicBody, initPos);
		defBd.bdef.angle = angle * MathUtils.degreesToRadians;
		defBd.setFixtureDef(shape, 1, 0, 1);
		defBd.fdef.isSensor = true;
		body = b2dWorld.createBody(defBd.bdef);
		body.createFixture(defBd.fdef);
		sprite = new Sprite(Assets.instance.texture.anubis);
		body.setUserData(new Box2DSprite(sprite));
		shape.dispose();
	}
}
