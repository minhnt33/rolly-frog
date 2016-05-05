package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.rollyfrog.object.EditorBody;
import vn.com.tuanminh.rollyfrog.utils.Const;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GeneralObjects {
	protected World b2dWorld;
	protected Body body;
	protected EditorBody eBody;
	protected float width;
	protected float height;
	protected float radius;
	protected Vector2 initPos;
	
	public GeneralObjects(World world, float x, float y) {
		this.b2dWorld = world;
		initPos = new Vector2(x / Const.PPM, y / Const.PPM);
	}
	
	public GeneralObjects(World world, float x, float y, float width, float height) {
		this(world, x, y);
		this.width = width;
		this.height = height;
	}
	
	public GeneralObjects(World world, float x, float y, float radius) {
		this(world, x, y);
		this.radius = radius;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Body getBody() {
		return body;
	}
	
	public EditorBody getEditorBody() {
		return eBody;
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public float getX() {
		return body.getPosition().x;
	}
	
	public float getY() {
		return body.getPosition().y;
	}
	
	public float getAngle() {
		return body.getAngle();
	}
	
	public Vector2 getEBodyPosition() {
		return eBody.getBody().getPosition();
	}
	
	public float getEBodyX() {
		return eBody.getBody().getPosition().x;
	}
	
	public float getEBodyY() {
		return eBody.getBody().getPosition().y;
	}
	
	public float getEBodyAngle() {
		return eBody.getBody().getAngle();
	}
}
