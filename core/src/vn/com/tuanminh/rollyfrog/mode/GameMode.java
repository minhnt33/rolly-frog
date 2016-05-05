package vn.com.tuanminh.rollyfrog.mode;

import vn.com.tuanminh.rollyfrog.boost.BoostHandler;
import vn.com.tuanminh.rollyfrog.event.EventHandler;
import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.object.EditorWorld;
import vn.com.tuanminh.rollyfrog.object.Frog;
import vn.com.tuanminh.rollyfrog.object.ObstacleSpawner;
import vn.com.tuanminh.rollyfrog.object.Room;
import vn.com.tuanminh.rollyfrog.utils.Const;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameMode implements Disposable {
	// Shared Objects
	protected World b2dWorld;
	protected EditorWorld eWorld;
	protected TweenManager manager;
	protected Frog frog;
	protected Room room;
	protected BoostHandler boostHandler;
	protected EventHandler eventHandler;
	protected Box2dContact contact;
	public boolean isReset = false; 

	// Spike Mode
	protected ObstacleSpawner obSpawner;

	private float runTime = 0;
	private float accumulator = 0;
	private float curGravity = 0;

	public enum STATE {
		GUIDE, RUNNING, PAUSE, GAME_OVER, HIGH_SCORE
	};

	protected STATE curState;
	protected int score;

	public GameMode() {
		this.createGeneralStuff();
		this.createSpecificStuff();
		frog.getBody().applyForceToCenter(new Vector2(-frog.getMaxSpeed(), 0),
				true);
		// Set contact listener
		contact = new Box2dContact(this);
		b2dWorld.setContactListener(contact);
	}

	private void createGeneralStuff() {
		b2dWorld = new World(new Vector2(0, curGravity), true);
		eWorld = new EditorWorld(b2dWorld, "texture/rollyfrog.json", Const.PPM);
		curState = STATE.GUIDE;
		room = new Room(b2dWorld, 160f, Const.GAME_HEIGHT / 2,
				Const.ROOM_RADIUS, this);
		frog = new Frog(this, b2dWorld, 160f, Const.GAME_HEIGHT / 2,
				Const.FROG_RADIUS);
		manager = new TweenManager();
		this.score = 0;
	}

	public void update(float delta) {
		runTime += delta;
		switch (curState) {
		case GUIDE:
			updateGuide(delta);
			break;
		case RUNNING:
			this.doPhysicsStep(delta, true);
			break;
		case PAUSE:
			break;
		case GAME_OVER:
			updateGameOver(delta);
			this.doPhysicsStep(delta, false);
			break;
		case HIGH_SCORE:
			updateGameOver(delta);
			this.doPhysicsStep(delta, false);
			break;
		default:
			break;
		}
	}

	private void doPhysicsStep(float delta, boolean isUpdateRunning) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		while (accumulator >= Const.TIME_STEP) {
			b2dWorld.step(Const.TIME_STEP, Const.VELOCITY_ITERATIONS,
					Const.POSITION_ITERATIONS);
			if (isUpdateRunning) {
				manager.update(delta);
				updateRunning(delta);
			}
			accumulator -= Const.TIME_STEP;
		}
	}

	public void setState(STATE state) {
		curState = state;
	}

	public void setCurGravity(float newGravity) {
		this.curGravity = newGravity;
		b2dWorld.setGravity(new Vector2(0, newGravity));
	}

	public void score(int increment) {
		score += increment;
	}

	public void reset() {
		isReset = true;
	}

	protected abstract void createSpecificStuff();

	protected abstract void updateGuide(float delta);

	protected abstract void updateRunning(float delta);

	protected abstract void updateGameOver(float delta);

	public abstract int getID();

	public int getScore() {
		return score;
	}

	public World getBox2DWorld() {
		return b2dWorld;
	}

	public EditorWorld getEditorWorld() {
		return eWorld;
	}

	public Frog getFrog() {
		return frog;
	}

	public Room getRoom() {
		return room;
	}

	public ObstacleSpawner getObstacleSpawner() {
		return obSpawner;
	}

	public float getCurGravity() {
		return curGravity;
	}

	public float getRunTime() {
		return runTime;
	}

	public STATE getCurState() {
		return curState;
	}

	public TweenManager getTweenManager() {
		return manager;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public BoostHandler getBoostHandler() {
		return boostHandler;
	}

	public Box2dContact getContactListener() {
		return contact;
	}

	public boolean isGuide() {
		return curState == STATE.GUIDE;
	}

	public boolean isRunning() {
		return curState == STATE.RUNNING;
	}

	public boolean isGameOver() {
		return curState == STATE.GAME_OVER;
	}

	public boolean isHighScore() {
		return curState == STATE.HIGH_SCORE;
	}

	public boolean isPaused() {
		return curState == STATE.PAUSE;
	}
}
