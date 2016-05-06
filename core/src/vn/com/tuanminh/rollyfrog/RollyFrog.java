package vn.com.tuanminh.rollyfrog;

import vn.com.tuanminh.frogunnerframe.screens.ScreenManager;
import vn.com.tuanminh.frogunnerframe.utils.GoogleActivity;
import vn.com.tuanminh.rollyfrog.screen.SplashScreen;

public class RollyFrog extends ScreenManager implements GoogleActivity {

	public RollyFrog(GoogleActivity handler) {
		super(handler);
	}

	@Override
	public void create() {
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void signIn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void signOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rateGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showScores(int modeID) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSignedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openURL() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showOrLoadInterstital() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNetworkConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void submitScore(long score, int modeID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buyGold(long gold) {
		// TODO Auto-generated method stub

	}
}
