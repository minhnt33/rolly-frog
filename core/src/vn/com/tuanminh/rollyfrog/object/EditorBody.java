package vn.com.tuanminh.rollyfrog.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;

public class EditorBody implements Disposable {
	private EditorWorld eWorld;
	private Body eBody;
	private Sprite bdSprite;
	private String bdName;
	private Texture texture;

	public EditorBody(EditorWorld eWorld, String name) {
		this.eWorld = eWorld;
		bdName = name;
		this.eWorld.getEditorBodies().add(this);
	}

	public void setSpriteTextureRegion(TextureRegion region) {
		if (bdSprite == null) {
			bdSprite = new Sprite(region);
		}
	}

	public void setSpriteTexture(String path) {
		if (bdSprite == null) {
			texture = new Texture(Gdx.files.internal(path));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			bdSprite = new Sprite(texture);
		}
	}

	public void createEditorBody(BodyDef bdef, FixtureDef fdef) {
		eBody = eWorld.getB2dWorld().createBody(bdef);
		eWorld.getLoader().attachFixture(eBody, bdName, fdef,
				bdSprite.getWidth() / eWorld.getPPM());
		eBody.setUserData(bdName);
	}

	public void createBodyNoSprite(BodyDef bdef, FixtureDef fdef, float width) {
		eBody = eWorld.getB2dWorld().createBody(bdef);
		eWorld.getLoader().attachFixture(eBody, bdName, fdef,
				width / eWorld.getPPM());
		eBody.setUserData(bdName);
	}

	public void attachSprite() {
		bdSprite.setCenter(this.getX(), this.getY());
		bdSprite.setRotation(eBody.getAngle() * MathUtils.radiansToDegrees);
		bdSprite.setScale(1 / eWorld.getPPM());
	}

	public void render(Batch batch, boolean isLocked) {
		if (bdSprite != null) {
			if (isLocked) {
				this.attachSprite();
			}
			bdSprite.draw(batch);
		}
	}

	public Sprite getSprite() {
		return bdSprite;
	}

	public Body getBody() {
		return eBody;
	}

	public float getX() {
		return eBody.getPosition().x;
	}

	public float getY() {
		return eBody.getPosition().y;
	}

	public float getWidth() {
		return bdSprite.getWidth() / eWorld.getPPM();
	}

	public float getHeight() {
		return bdSprite.getHeight() / eWorld.getPPM();
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
