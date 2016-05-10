package vn.com.tuanminh.rollyfrog.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {

	public static final GamePreferences instance = new GamePreferences();

	public int unlockMode[];
	public int unlockFrog[];
	public boolean enableVol;
	public int highscores[];
	public int curFrog;
	public int gold;
	public boolean removeAds;

	private Preferences prefs;

	private GamePreferences() {
		prefs = Gdx.app.getPreferences("rollyfrog.pref");
	}

	public void load() {
		enableVol = prefs.getBoolean("enableVol", true);
		gold = prefs.getInteger("gold", 0);
		highscores = getHighScores();
		unlockMode = getPrices();
		unlockFrog = getFrog();
		curFrog = prefs.getInteger("curFrog", 0);
		removeAds = prefs.getBoolean("removeAds", false);
	}

	public void save() {
		prefs.putBoolean("enableVol", enableVol);
		prefs.putInteger("gold", gold);
		putArray(highscores, "highscore");
		putArray(unlockMode, "price");
		putArray(unlockFrog, "frog");
		prefs.putInteger("curFrog", curFrog);
		prefs.putBoolean("removeAds", removeAds);
		prefs.flush();
	}

	public void unlockLevel(int modeID) {
		unlockMode[modeID - 1] = modeID;
	}

	private void putArray(int array[], String keyword) {
		for (int i = 0; i < array.length; i++) {
			prefs.putInteger(keyword + i, array[i]);
		}
	}

	private int[] getPrices() {
		int array[] = new int[Const.NUMBER_OF_MODE];
		array[0] = prefs.getInteger("price" + 0, 1);
		array[1] = prefs.getInteger("price" + 1, 2);
		for (int i = 2; i < Const.NUMBER_OF_MODE; i++) {
			array[i] = prefs.getInteger("price" + i, 0);
		}
		return array;
	}

	private int[] getHighScores() {
		int array[] = new int[Const.NUMBER_OF_MODE];
		for (int i = 0; i < Const.NUMBER_OF_MODE; i++) {
			array[i] = prefs.getInteger("highscore" + i, 0);
		}
		return array;
	}

	private int[] getFrog() {
		int array[] = new int[Const.NUMBER_OF_FROG];
		for (int i = 0; i < Const.NUMBER_OF_FROG; i++) {
			if (i == 0 || i == 1)
				array[i] = prefs.getInteger("frog" + i, 1); // Frog 1, 2 is free
			else
				array[i] = prefs.getInteger("frog" + i, 0);
		}
		return array;
	}

	public void clearAll() {
		prefs.clear();
		prefs.flush();
		load();
	}
}
