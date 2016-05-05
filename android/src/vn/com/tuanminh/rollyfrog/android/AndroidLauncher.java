package vn.com.tuanminh.rollyfrog.android;

import vn.com.tuanminh.frogunnerframe.utils.GoogleActivity;
import vn.com.tuanminh.rollyfrog.RollyFrog;
import vn.com.tuanminh.rollyfrog.utils.Const;
import vn.com.tuanminh.rollyfrog.utils.GamePreferences;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Purchase;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class AndroidLauncher extends AndroidApplication implements
		GoogleActivity {
	private final static int REQUEST_CODE_UNUSED = 9002;
	// Chartboost
	private static final String CHARTBOOST_ID = "54fbba1904b01654534f2a5f";
	private static final String CHARTBOOST_SIGNATURE = "bf19d133dfec973d15c00e83ba09ef782dc857ea";

	// Startapp
	private static final String STARTAPP_DEVELOPER_ID = "103641833";
	private static final String STARTAPP_APP_ID = "203361304";
	private StartAppAd startAppAd;

	// Google play services
	private GameHelper gameHelper;

	// Google billing
	private static final String GOLD_400 = "coin400";
	private static final String GOLD_1000 = "coin1000";
	private static final String GOLD_3000 = "coin3000";
	private IabHelper mHelper;
	private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new RollyFrog(this), config);
		config.useAccelerometer = false;
		config.useCompass = false;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initGooglePlayServices();
		initGoogleBilling();
		if (!GamePreferences.instance.removeAds) {
			initStartapp();
			initChartBoost();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// Code for Startapp
	private void initStartapp() {
		StartAppSDK.init(this, STARTAPP_DEVELOPER_ID, STARTAPP_APP_ID, false);
		startAppAd = new StartAppAd(this);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// Code for Google Billing
	private void initGoogleBilling() {
		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(
				this,
				new String("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjW9")
						.concat("Dmm1KG5vegSLo593Ot7e8K6Nhn002v+gufFRHmwYuh0Sh4gi")
						.concat("jNkoH18Z/QzC6UbzSlS7Dx9qK7TU7wojSUilxTRdTnGsB+UQggKIUVGSNm2U/j2Yd40u9Feh5WyP5hkr+Co")
						.concat("/81sIyIygHoeSkHiA5TyfSKtPe8YNdSZXJTSVuWRCUATHilK0V9KwkvnQdTRcAU9OVwQyWe8gaL4ty1DK3JZUesKdUMLx")
						.concat("/g8kqA9iX/slRrCDjbl+")
						.concat("Oa4Z3SIY+DCmMx1pUiVmeEps6ts6KNHlohAyT3FAJrmEpB96hgZhRwjp")
						.concat("pnQpuZo+mFzmjOEQNf+cDcoNPBMl6Dq+TU1c3QQIDAQAB"));

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					// Printing problem to device screen

				} else {

				}
				// Hooray, IAB is fully set up!
			}
		});

		// Callback for when a purchase is finished
		mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result,
					Purchase purchase) {
				if (purchase == null)
					return;
				// Printing problem to device screen

				// if we were disposed of in the meantime, quit.
				if (mHelper == null)
					return;

				if (result.isFailure()) {
					return;
				}

				if (purchase.getSku().equals(GOLD_400)) {
					// Add 400 gold
					GamePreferences.instance.gold += 400;
				}

				if (purchase.getSku().equals(GOLD_1000)) {
					// Add 1000 gold
					GamePreferences.instance.gold += 1000;
				}

				if (purchase.getSku().equals(GOLD_3000)) {
					// Add 3000 gold
					GamePreferences.instance.gold += 3000;
				}

				// set a flag to indicate that ads shouldn't show
				// anymore. And save gold data
				if (!GamePreferences.instance.removeAds)
					GamePreferences.instance.removeAds = true;
				GamePreferences.instance.save();

				// Immediately consume item so we can purchase multiple time
				mHelper.consumeAsync(purchase, null);
			}
		};
	}

	@Override
	public void showOrLoadInterstital() {
		if (isNetworkConnected()) {
			try {
				runOnUiThread(new Runnable() {
					public void run() {
						startAppAd.loadAd(new AdEventListener() {
							@Override
							public void onReceiveAd(Ad ad) {
								startAppAd.showAd();
							}

							@Override
							public void onFailedToReceiveAd(Ad ad) {
								Chartboost
										.showInterstitial(CBLocation.LOCATION_GAMEOVER);
							}
						});
					}

				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// Code for Chartboost
	private void initChartBoost() {
		Chartboost.startWithAppId(this, CHARTBOOST_ID, CHARTBOOST_SIGNATURE);
		Chartboost.onCreate(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
		Chartboost.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
		Chartboost.onStop(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Chartboost.onResume(this);
		startAppAd.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		Chartboost.onPause(this);
		startAppAd.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Chartboost.onDestroy(this);

		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	@Override
	public void onBackPressed() {
		// If an interstitial is on screen, close it.
		if (Chartboost.onBackPressed())
			return;
		else
			super.onBackPressed();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// Code for Google play services
	private void initGooglePlayServices() {
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);
		gameHelper.setMaxAutoSignInAttempts(0);
		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {

			@Override
			public void onSignInFailed() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSignInSucceeded() {
				// TODO Auto-generated method stub

			}

		};

		gameHelper.setup(gameHelperListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);

		if (mHelper != null) {
			// Pass on the activity result to the helper for handling
			if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
				Log.d("IAB", "onActivityResult handled by IABUtil.");
			}
		}
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage()
					+ ".");
		}
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage()
					+ ".");
		}
	}

	@Override
	public void rateGame() {
		// Replace the end of the URL with the package of your game
		String str = "https://play.google.com/store/apps/details?id="
				+ getPackageName();
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitScore(long score, int modeID) {
		if (isSignedIn() == true) {
			if (modeID == Const.SPIKE_MODE_ID)
				this.addScoreToLeaderboard(
						R.string.leaderboard_id_best_stinker, score);
			else if (modeID == Const.LASER_MODE_ID)
				this.addScoreToLeaderboard(
						R.string.leaderboard_id_best_galaxy_roller, score);
			else if (modeID == Const.MATH_MODE_ID)
				this.addScoreToLeaderboard(
						R.string.leaderboard_id_best_math_killer, score);
		} else {
			// Maybe sign in here then redirect to submitting score?
			signIn();
			if (isSignedIn())
				submitScore(score, modeID);
		}
	}

	private void addScoreToLeaderboard(int leaderboardID, long score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(),
				getString(leaderboardID), score);
		startActivityForResult(
				Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(), getString(leaderboardID)),
				REQUEST_CODE_UNUSED);
	}

	@Override
	public void showScores(int modeID) {
		if (isSignedIn() == true) {
			if (modeID == Const.SPIKE_MODE_ID)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(),
						getString(R.string.leaderboard_id_best_stinker)),
						REQUEST_CODE_UNUSED);
			else if (modeID == Const.LASER_MODE_ID)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(),
						getString(R.string.leaderboard_id_best_galaxy_roller)),
						REQUEST_CODE_UNUSED);
			else if (modeID == Const.MATH_MODE_ID)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(),
						getString(R.string.leaderboard_id_best_math_killer)),
						REQUEST_CODE_UNUSED);
		} else {
			// Maybe sign in here then redirect to showing scores?
			signIn();
			if (isSignedIn())
				showScores(modeID);
		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void openURL() {
		// TODO Auto-generated method stub
	}

	@Override
	public void buyGold(long gold) {
		String goldID = null;
		if (gold == 400)
			goldID = GOLD_400;
		if (gold == 1000)
			goldID = GOLD_1000;
		if (gold == 3000)
			goldID = GOLD_3000;
		mHelper.launchPurchaseFlow(this, goldID, REQUEST_CODE_UNUSED,
				mPurchaseFinishedListener, "HANDLE_PAYLOADS");
	}

	@Override
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileData = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isConnected = false;
		if (wifi.isConnected() || mobileData.isConnected())
			isConnected = true;
		return isConnected;
	}
}
