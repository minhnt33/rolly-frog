package vn.com.tuanminh.rollyfrog.utils;

import vn.com.tuanminh.frogunnerframe.utils.ResolutionHandler;

import com.badlogic.gdx.math.Vector2;

public class Const {
	public static int gameCount = 0;
	public static final int AD_COUNTER = 6;
	public static final int PAUSED_TIME = 3000; // ms

	// General setup
	public static final boolean isDebug = false;
	public static final float GAME_HEIGHT = ResolutionHandler.instance
			.getGameHeightWith(320f);
	public static float PPM = 160f;
	public static final float TIME_STEP = 1 / 60f;
	public static final int VELOCITY_ITERATIONS = 20;
	public static final int POSITION_ITERATIONS = 20;
	public static final Vector2 CENTER_POINT = new Vector2(160f / PPM,
			GAME_HEIGHT / 2 / PPM);

	// Amount of mode and frog
	public static final int NUMBER_OF_MODE = 3;
	public static final int NUMBER_OF_FROG = 23;

	// Mode id
	public static final int SPIKE_MODE_ID = 1;
	public static final int LASER_MODE_ID = 2;
	public static final int SWAT_MODE_ID = 5;
	public static final int MATH_MODE_ID = 3;
	public static final int MUMMY_MODE_ID = 4;

	// Mode price
	public static final int SWAT_MODE_PRICE = 500;
	public static final int MATH_MODE_PRICE = 1000;
	public static final int MUMMY_MODE_PRICE = 1000;

	// Frog properties
	public static final float FROG_RADIUS = 15f;
	public static final float FROG_DENSITY = 1f;
	public static final float FROG_FRICTION = 1f;
	public static final float FROG_RESTITUTION = 1f;
	public static final float FROG_MAX_SPEED_1 = 1.2f;
	public static final float FROG_MAX_SPEED_2 = 1.4f;
	public static final float FROG_MAX_SPEED_3 = 1.55f;
	public static final float FROG_MAX_SPEED_4 = 1.6f;
	public static final float FROG_MAX_SPEED_5 = 1.65f;

	// Room properties
	public static final float ROOM_RADIUS = 160f;
	public static final float ROOM_DENSITY = 1f;
	public static final float ROOM_FRICTION = 1f;
	public static final float ROOM_RESTITUTION = 1f;
	public static final float ROOM_OMEGA = 2f;

	// Spike properties
	public static final float SPIKE_DENSITY = 1f;
	public static final float SPIKE_FRICTION = 0f;
	public static final float SPIKE_RESTITUTION = 1f;
	public static final float SPIKE_RADIUS = 160f;

	// Obstacle
	public static final int OBSTACLE_TOTAL = 40;

	// Spike
	public static final int SPIKE_MAX_ONE_TIME_1 = 18;
	public static final int SPIKE_MIN_GROUP_1 = 4;
	public static final int SPIKE_MAX_GROUP_1 = 6;
	public static final int SPIKE_MIN_PER_GROUP_1 = 2;
	public static final int SPIKE_MAX_PER_GROUP_1 = 3;

	public static final int SPIKE_MAX_ONE_TIME_2 = 22;
	public static final int SPIKE_MIN_GROUP_2 = 5;
	public static final int SPIKE_MAX_GROUP_2 = 5;
	public static final int SPIKE_MIN_PER_GROUP_2 = 3;
	public static final int SPIKE_MAX_PER_GROUP_2 = 4;

	public static final int SPIKE_MAX_ONE_TIME_3 = 24;
	public static final int SPIKE_MIN_GROUP_3 = 4;
	public static final int SPIKE_MAX_GROUP_3 = 6;
	public static final int SPIKE_MIN_PER_GROUP_3 = 4;
	public static final int SPIKE_MAX_PER_GROUP_3 = 4;

	public static final int SPIKE_MAX_ONE_TIME_4 = 26;
	public static final int SPIKE_MIN_GROUP_4 = 5;
	public static final int SPIKE_MAX_GROUP_4 = 6;
	public static final int SPIKE_MIN_PER_GROUP_4 = 4;
	public static final int SPIKE_MAX_PER_GROUP_4 = 4;

	public static final int SPIKE_MAX_ONE_TIME_5 = 28;
	public static final int SPIKE_MIN_GROUP_5 = 5;
	public static final int SPIKE_MAX_GROUP_5 = 7;
	public static final int SPIKE_MIN_PER_GROUP_5 = 4;
	public static final int SPIKE_MAX_PER_GROUP_5 = 4;

	public static final int SPIKE_MAX_ONE_TIME_6 = 32;
	public static final int SPIKE_MIN_GROUP_6 = 6;
	public static final int SPIKE_MAX_GROUP_6 = 8;
	public static final int SPIKE_MIN_PER_GROUP_6 = 4;
	public static final int SPIKE_MAX_PER_GROUP_6 = 4;

	// Laser
	public static final int LASER_MAX_ONE_TIME_1 = 1;
	public static final int LASER_MAX_GROUP_1 = 1;
	public static final int LASER_MIN_GROUP_1 = 1;
	public static final int LASER_MIN_PER_GROUP_1 = 1;
	public static final int LASER_MAX_PER_GROUP_1 = 1;

	public static final int LASER_MAX_ONE_TIME_2 = 2;
	public static final int LASER_MAX_GROUP_2 = 2;
	public static final int LASER_MIN_GROUP_2 = 2;
	public static final int LASER_MIN_PER_GROUP_2 = 1;
	public static final int LASER_MAX_PER_GROUP_2 = 1;

	public static final int LASER_MAX_ONE_TIME_3 = 3;
	public static final int LASER_MAX_GROUP_3 = 2;
	public static final int LASER_MIN_GROUP_3 = 2;
	public static final int LASER_MIN_PER_GROUP_3 = 1;
	public static final int LASER_MAX_PER_GROUP_3 = 2;

	public static final int LASER_MAX_ONE_TIME_4 = 4;
	public static final int LASER_MAX_GROUP_4 = 3;
	public static final int LASER_MIN_GROUP_4 = 2;
	public static final int LASER_MIN_PER_GROUP_4 = 1;
	public static final int LASER_MAX_PER_GROUP_4 = 2;

	public static final int LASER_MAX_ONE_TIME_5 = 5;
	public static final int LASER_MAX_GROUP_5 = 3;
	public static final int LASER_MIN_GROUP_5 = 2;
	public static final int LASER_MIN_PER_GROUP_5 = 2;
	public static final int LASER_MAX_PER_GROUP_5 = 2;

	public static final int LASER_MAX_ONE_TIME_6 = 6;
	public static final int LASER_MAX_GROUP_6 = 3;
	public static final int LASER_MIN_GROUP_6 = 3;
	public static final int LASER_MIN_PER_GROUP_6 = 2;
	public static final int LASER_MAX_PER_GROUP_6 = 2;

	public static final int LASER_MAX_ONE_TIME_7 = 7;
	public static final int LASER_MAX_GROUP_7 = 3;
	public static final int LASER_MIN_GROUP_7 = 2;
	public static final int LASER_MIN_PER_GROUP_7 = 2;
	public static final int LASER_MAX_PER_GROUP_7 = 3;

	public static final int LASER_MAX_ONE_TIME_8 = 8;
	public static final int LASER_MAX_GROUP_8 = 4;
	public static final int LASER_MIN_GROUP_8 = 3;
	public static final int LASER_MIN_PER_GROUP_8 = 2;
	public static final int LASER_MAX_PER_GROUP_8 = 2;

	// Event stuff
	public static final float METEOR_RADIUS = 40f;

	// Category Bits
	public static final short BIT_FROG = 2;
	public static final short BIT_ROOM = 4;
	public static final short BIT_OBSTACLE = 8;
	public static final short BIT_BOOST = 16;
	public static final short BIT_METEOR = 32;
	public static final short BIT_FROG_CLONER = 64;
	public static final short BIT_LASER = 128;

	// Boosts
	public static final float SHIELD_TIME_MILLIS = 6500;
	public static final float SLOW_TIME_MILLIS = 2000;
}
