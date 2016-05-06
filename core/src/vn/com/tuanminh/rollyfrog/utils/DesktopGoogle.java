package vn.com.tuanminh.rollyfrog.utils;

import vn.com.tuanminh.frogunnerframe.utils.GoogleActivity;

public class DesktopGoogle implements GoogleActivity {

	@Override
	public void signIn() {
		// TODO Auto-generated method stub
		System.out.println("sign in");
	}

	@Override
	public void signOut() {
		// TODO Auto-generated method stub
		System.out.println("sign out");
	}

	@Override
	public void rateGame() {
		// TODO Auto-generated method stub
		System.out.println("rate");
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
		System.out.println("show ad");
	}

	@Override
	public boolean isNetworkConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void submitScore(long score, int modeID) {
		// TODO Auto-generated method stub
		System.out.println("submit score");
	}

	@Override
	public void showScores(int modeID) {
		// TODO Auto-generated method stub
		System.out.println("show score");
	}

	@Override
	public void buyGold(long gold) {
		// TODO Auto-generated method stub
		System.out.println("Buy " + gold + " gold");
	}
}
