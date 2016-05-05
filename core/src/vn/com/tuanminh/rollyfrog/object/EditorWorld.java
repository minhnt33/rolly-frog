package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.frogunnerframe.box2dhelper.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class EditorWorld {
	private World b2dWorld;
	private BodyEditorLoader loader;
	private Array<EditorBody> eBodies;
	private final float PPM;

	public EditorWorld(World b2dWorld, String path, float ppm) {
		this.b2dWorld = b2dWorld;
		loader = new BodyEditorLoader(Gdx.files.internal(path));
		eBodies = new Array<EditorBody>();
		PPM = ppm;
	}

	public void renderAll(Batch batch, boolean isLocked) {
		for (int i = 0; i < eBodies.size; i++) {
			eBodies.get(i).render(batch, isLocked);
		}
	}

	public void renderName(Batch batch, boolean isLocked, String name) {
		for (int i = 0; i < eBodies.size; i++) {
			if (eBodies.get(i).getBody().getUserData().equals(name)) {
				eBodies.get(i).render(batch, isLocked);
			}
		}
	}

	public World getB2dWorld() {
		return b2dWorld;
	}

	public BodyEditorLoader getLoader() {
		return loader;
	}

	public Array<EditorBody> getEditorBodies() {
		return eBodies;
	}

	public float getPPM() {
		return PPM;
	}
}
