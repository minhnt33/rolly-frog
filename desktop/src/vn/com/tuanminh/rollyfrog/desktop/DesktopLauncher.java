package vn.com.tuanminh.rollyfrog.desktop;

import vn.com.tuanminh.frogunnerframe.utils.GoogleActivity;
import vn.com.tuanminh.rollyfrog.RollyFrog;
import vn.com.tuanminh.rollyfrog.utils.DesktopGoogle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher implements GoogleActivity {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new RollyFrog(new DesktopGoogle()), config);

		config.width = 480;
		config.height = 800;
	}

	@Override
	public void openURL() {
		// TODO Auto-generated method stub

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
	public boolean isSignedIn() {
		// TODO Auto-generated method stub
		return false;
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
	public void showScores(int modeID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buyGold(long gold) {
		// TODO Auto-generated method stub

	}
}
