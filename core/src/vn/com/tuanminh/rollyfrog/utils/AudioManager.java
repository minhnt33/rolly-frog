package vn.com.tuanminh.rollyfrog.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	public static final AudioManager instance = new AudioManager();

	private Music playingMusic;

	// singleton: prevent instantiation from other classes
	private AudioManager() {
	}

	public void playSound(Sound sound) {
		if (GamePreferences.instance.enableVol)
			sound.play();
	}

	public void playSound(Sound sound, float volume) {
		if (GamePreferences.instance.enableVol)
			sound.play(volume);
	}

	public void playMusic(Music music, float volume) {
		// Pause current playing music if has
		if (playingMusic != null)
			playingMusic.pause();
		// Set cur music to this music
		playingMusic = music;
		if (GamePreferences.instance.enableVol) {
			music.setLooping(true);
			music.setVolume(volume);
			music.play();
		}
	}

	public void stopMusic() {
		if (playingMusic != null)
			playingMusic.stop();
	}

	public void pauseMusic() {
		if (playingMusic != null)
			playingMusic.pause();
	}

	public void unpauseMusic() {
		if (playingMusic != null && GamePreferences.instance.enableVol)
			playingMusic.play();
	}

	public Music getPlayingMusic() {
		return playingMusic;
	}
}
