package vn.com.tuanminh.rollyfrog.game;

import vn.com.tuanminh.frogunnerframe.core.AssetHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets extends AssetHelper {
	public static final Assets instance = new Assets();
	public AssetFont font;
	public AssetTexture texture;
	public AssetSound sound;
	public AssetMusic music;
	public AssetSkin skin;

	private Assets() {

	}

	public class AssetFont {
		public final BitmapFont small;
		public final BitmapFont normal;

		// public final BitmapFont large;
		// public final BitmapFont chalkSmall;
		// public final BitmapFont chalkNormal;

		public AssetFont() {
			normal = loadBitmapFont("font/label.fnt", 1f, 1f);
			small = loadBitmapFont("font/label.fnt", 0.5f, 0.5f);
			// large = loadBitmapFont("font/label.fnt", 1.5f, 1.5f);
			// chalkNormal = loadBitmapFont("font/chalkfont.fnt", 1f, 1f);
			// chalkSmall = loadBitmapFont("font/chalkfont.fnt", 0.5f, 0.5f);
		}
	}

	public class AssetTexture {
		// Frog alive
		public final TextureRegion frogSpike;
		public final TextureRegion frogMum;
		public final TextureRegion frogLaser;
		public final TextureRegion frogMath;
		public final TextureRegion frogSwat;
		public final TextureRegion frogGaoRed;
		public final TextureRegion frogGaoGreen;
		public final TextureRegion frogGaoYellow;
		public final TextureRegion frogJeremi;
		public final TextureRegion frogLaserPink;
		public final TextureRegion frogLaserRed;
		public final TextureRegion frogLaserYellow;
		public final TextureRegion frogOld;
		public final TextureRegion frogWarrior;
		public final TextureRegion frogNurse;
		public final TextureRegion frogAlien;
		public final TextureRegion frogColorDress;
		public final TextureRegion frogLolipop;
		public final TextureRegion frogMinion;
		public final TextureRegion frogRedMinion;
		public final TextureRegion frogPinkDress;
		public final TextureRegion frogPrince;
		public final TextureRegion frogTerror;

		// Frog die
		public final TextureRegion spikeDie;
		public final TextureRegion mumDie;
		public final TextureRegion laserDie;
		public final TextureRegion mathDie;
		public final TextureRegion swatDie;
		public final TextureRegion frogGaoDie;
		public final TextureRegion frogJeremiDie;
		public final TextureRegion frogLaserPinkDie;
		public final TextureRegion frogLaserRedDie;
		public final TextureRegion frogLaserYellowDie;
		public final TextureRegion frogOldDie;
		public final TextureRegion frogWarriorDie;
		public final TextureRegion frogNurseDie;
		public final TextureRegion frogAlienDie;
		public final TextureRegion frogColorDressDie;
		public final TextureRegion frogMinionDie;
		public final TextureRegion frogRedMinionDie;
		public final TextureRegion frogPinkDressDie;
		public final TextureRegion frogPrinceDie;
		public final TextureRegion frogTerrorDie;

		// Room
		public final TextureRegion roomSpike;
		public final TextureRegion roomMum;
		public final TextureRegion roomLaser;
		public final TextureRegion roomMath;
		// Obstacle
		public final TextureRegion spike;
		public final TextureRegion gun;
		// Laser
		public final TextureRegion laser_bg;
		public final TextureRegion laser_over;
		// Meteor
		public final TextureRegion meteor;
		// Background
		public final TextureRegion roomSpikeBg;
		public final TextureRegion spikebackground;
		public final TextureRegion laserbackground;
		public final TextureRegion mummybackground;
		public final TextureRegion mathbackground;
		public final TextureRegion swatbackground;
		public final TextureRegion iceScreen;
		// Boost
		public final TextureRegion boostShield;
		public final TextureRegion shieldGlow;
		public final TextureRegion boost2X;
		public final TextureRegion boostSlow;
		// Mummy Hand
		public final TextureRegion anubis;
		// Other
		public final TextureRegion mathLine;
		public final TextureRegion mathThink;
		public final TextureRegion scorepane;

		public final TextureRegion frogRegions[];
		public final TextureRegion frogDieRegions[];
		public final String nickName[];
		public final int frogPrice[];

		public AssetTexture(TextureAtlas atlas, TextureAtlas atlas2) {
			frogSpike = atlas2.findRegion("frogSpike");
			frogMum = atlas2.findRegion("frogMum");
			frogLaser = atlas2.findRegion("frogLaser");
			frogMath = atlas2.findRegion("frogMath");
			frogSwat = atlas2.findRegion("frogSwat");
			frogGaoRed = atlas2.findRegion("frogGaoRed");
			frogGaoYellow = atlas2.findRegion("frogGaoYellow");
			frogGaoGreen = atlas2.findRegion("frogGaoGreen");
			frogJeremi = atlas2.findRegion("frogJeremi");
			frogOld = atlas2.findRegion("frogOld");
			frogLaserPink = atlas2.findRegion("frogLaserPink");
			frogLaserYellow = atlas2.findRegion("frogLaserYellow");
			frogLaserRed = atlas2.findRegion("frogLaserRed");
			frogWarrior = atlas2.findRegion("frogWarrior");
			frogNurse = atlas2.findRegion("frogNurse");
			frogAlien = atlas2.findRegion("frogAlien");
			frogColorDress = atlas2.findRegion("frogColorDress");
			frogLolipop = atlas2.findRegion("frogLolipop");
			frogMinion = atlas2.findRegion("frogMinion");
			frogRedMinion = atlas2.findRegion("frogRedMinion");
			frogPinkDress = atlas2.findRegion("frogPinkDress");
			frogPrince = atlas2.findRegion("frogPrince");
			frogTerror = atlas2.findRegion("frogTerror");

			mathDie = atlas.findRegion("mathDie");
			spikeDie = atlas.findRegion("spikeDie");
			mumDie = atlas.findRegion("mumDie");
			laserDie = atlas.findRegion("laserDie");
			swatDie = atlas.findRegion("swatDie");
			frogGaoDie = atlas.findRegion("frogGaoDie");
			frogJeremiDie = atlas.findRegion("frogJeremiDie");
			frogOldDie = atlas.findRegion("frogOldDie");
			frogLaserPinkDie = atlas.findRegion("laserDiePink");
			frogLaserYellowDie = atlas.findRegion("laserDieYellow");
			frogLaserRedDie = atlas.findRegion("laserDieRed");
			frogWarriorDie = atlas.findRegion("frogWarriorDie");
			frogNurseDie = atlas.findRegion("frogNurseDie");
			frogAlienDie = atlas.findRegion("frogAlienDie");
			frogColorDressDie = atlas.findRegion("frogColorDressDie");
			frogMinionDie = atlas.findRegion("frogMinionDie");
			frogRedMinionDie = atlas.findRegion("frogRedMinionDie");
			frogPinkDressDie = atlas.findRegion("frogPinkDressDie");
			frogPrinceDie = atlas.findRegion("frogPrinceDie");
			frogTerrorDie = atlas.findRegion("frogTerrorDie");

			roomSpike = atlas.findRegion("roomSpike");
			roomMum = atlas.findRegion("roomMum");
			roomLaser = atlas.findRegion("roomLaser");
			roomMath = atlas.findRegion("roomMath");
			spike = atlas.findRegion("spike");
			gun = atlas.findRegion("gun");
			laser_bg = atlas.findRegion("laser_bg");
			laser_over = atlas.findRegion("laser_over");
			meteor = atlas.findRegion("meteor");
			spikebackground = atlas.findRegion("spikebackground");
			laserbackground = atlas.findRegion("laserbackground");
			mummybackground = atlas.findRegion("mummybackground");
			mathbackground = atlas.findRegion("mathbackground");
			swatbackground = atlas.findRegion("swatbackground");
			roomSpikeBg = atlas.findRegion("roomSpikeBg");

			iceScreen = atlas.findRegion("icescreen");
			boostShield = atlas.findRegion("shield");
			shieldGlow = atlas.findRegion("shieldGlow");
			boost2X = atlas.findRegion("2x");
			boostSlow = atlas.findRegion("slow");

			anubis = atlas.findRegion("anubis");
			mathLine = atlas.findRegion("line");
			mathThink = atlas.findRegion("think");
			scorepane = atlas.findRegion("scorepane");

			TextureRegion tmpFrogDieRegions[] = { spikeDie, laserDie, mumDie,
					mathDie, swatDie, frogGaoDie, frogGaoDie, frogGaoDie,
					frogJeremiDie, frogLaserPinkDie, frogLaserRedDie,
					frogLaserYellowDie, frogOldDie, frogWarriorDie,
					frogNurseDie, frogAlienDie, frogColorDressDie, frogLolipop,
					frogMinionDie, frogRedMinionDie, frogPinkDressDie,
					frogPrinceDie, frogTerrorDie };

			TextureRegion tmpFrogRegions[] = { frogSpike, frogLaser, frogMum,
					frogMath, frogSwat, frogGaoGreen, frogGaoRed,
					frogGaoYellow, frogJeremi, frogLaserPink, frogLaserRed,
					frogLaserYellow, frogOld, frogWarrior, frogNurse,
					frogAlien, frogColorDress, frogLolipop, frogMinion,
					frogRedMinion, frogPinkDress, frogPrince, frogTerror };

			// Nick name of frog in shop
			String tmpNickName[] = { "Stuny", "Spacy", "Fromy", "Profy",
					"SWATy", "Gao Green", "Gao Red", "Gao Yellow", "Jeremi",
					"Pinky", "Redy", "Yellowy", "Olda Fact", "Frorrior",
					"Nursy", "Frolien", "Sweetie", "Frolipop", "MiniFrog",
					"SurfMini", "Dressy", "Princo", "Terrofy" };

			int tmpFrogPrice[] = { 50, 50, 100, 100, 150, 150, 150, 200, 200,
					200, 250, 250, 250, 300, 300, 300, 400, 400, 400, 500, 500,
					500, 500 };

			frogRegions = tmpFrogRegions;
			frogDieRegions = tmpFrogDieRegions;
			nickName = tmpNickName;
			frogPrice = tmpFrogPrice;
		}
	}

	public class AssetSkin {
		public final Skin mySkin;

		public AssetSkin(TextureAtlas atlas) {
			mySkin = new Skin(Gdx.files.internal("skin/ui.json"), atlas);
			mySkin.getFont("default-font").getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Important:
																			// smooth
																			// font
		}
	}

	public class AssetSound {
		public final Sound die;
		public final Sound opop;
		public final Sound laser;
		public final Sound boom;
		public final Sound click;
		public final Sound alert;
		public final Sound boomAsteroid;
		public final Sound garling;
		public final Sound boost;
		public final Sound letsgo;

		public AssetSound() {
			boom = getSound("boom.mp3");
			laser = getSound("laser.mp3");
			opop = getSound("opop.mp3");
			die = getSound("die.mp3");
			click = getSound("click.wav");
			alert = getSound("alert.mp3");
			boomAsteroid = getSound("boomAsteroid.mp3");
			garling = getSound("gargling.mp3");
			boost = getSound("boost.mp3");
			letsgo = getSound("go.mp3");
		}
	}

	public class AssetMusic {
		public final Music menu;
		public final Music spikeMode;
		public final Music laserMode;
		public final Music mathMode;

		public AssetMusic() {
			menu = getMusic("menu.mp3");
			menu.setLooping(true);
			spikeMode = getMusic("spikebg.mp3");
			spikeMode.setLooping(true);
			laserMode = getMusic("laserbg.mp3");
			laserMode.setLooping(true);
			mathMode = getMusic("mathbg.mp3");
			mathMode.setLooping(true);
		}
	}

	@Override
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		sounds.add("go.mp3");
		sounds.add("boom.mp3");
		sounds.add("laser.mp3");
		sounds.add("die.mp3");
		sounds.add("opop.mp3");
		sounds.add("click.wav");
		sounds.add("boomAsteroid.mp3");
		sounds.add("alert.mp3");
		sounds.add("gargling.mp3");
		sounds.add("boost.mp3");
		musics.add("menu.mp3");
		musics.add("spikebg.mp3");
		musics.add("laserbg.mp3");
		musics.add("mathbg.mp3");
		loadSoundsAndMusics();
		assetManager.load("texture/rolly_frog.pack", TextureAtlas.class);
		assetManager.load("skin/ui.pack", TextureAtlas.class);
		assetManager.finishLoading();

		TextureAtlas atlas = assetManager.get("texture/rolly_frog.pack");
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		TextureAtlas atlasSkin = assetManager.get("skin/ui.pack");
		for (Texture t : atlasSkin.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		font = new AssetFont();
		texture = new AssetTexture(atlas, atlasSkin);
		skin = new AssetSkin(atlasSkin);
		sound = new AssetSound();
		music = new AssetMusic();
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		font.normal.dispose();
		font.small.dispose();
		// font.large.dispose();
	}

	public void stopAllMusic() {
		music.laserMode.stop();
		music.mathMode.stop();
		music.spikeMode.stop();
		music.menu.pause();
	}

	public void stopAllSound() {
		for (String name : sounds) {
			Sound sound = getSound(name);
			sound.stop();
		}
	}

	public boolean isDone() {
		return assetManager.update();
	}
}
