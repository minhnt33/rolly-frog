package vn.com.tuanminh.rollyfrog.object;

import vn.com.tuanminh.rollyfrog.game.Box2dContact;
import vn.com.tuanminh.rollyfrog.mode.GameMode;
import vn.com.tuanminh.rollyfrog.mode.MathMode;

import com.badlogic.gdx.math.MathUtils;

public class MathSpawner extends ObstacleSpawner {
	public static boolean check;
	private MathMode mathMode;
	private boolean result = false;
	private int[] formula;
	private int[] inE;
	// private float chanceType = 0.5f;
	// private float chanceResult = 0.5f;
	private int from = 0;
	private int to = 3;

	private enum MATH_TYPE {
		ADD, MUL
	};

	private MATH_TYPE type;

	private enum MATH_SIGN {
		SMALLER, EQUAL, GREATER
	};

	private MATH_SIGN sign;

	private final int epsilon = 4;

	public MathSpawner(GameMode mode) {
		super(mode);
		mathMode = (MathMode) mode;
		// formula = this.randomFormula(1, 10, 1f, 0.5f);
		inE = this.randomInequilvalance(from, to, true);
	}

	@Override
	public void update(float delta) {
		if (Box2dContact.isCollided && check) {
			// formula = this.randomFormula(1, 10, 0.5f, 0.5f);
			if (mathMode.isLevel2()) {
				from = 3;
				to = 10;
			} else if (mathMode.isLevel3()) {
				from = 10;
				to = 20;
			} else if (mathMode.isLevel4()) {
				from = -3;
				to = 3;
			} else if (mathMode.isLevel5()) {
				from = -6;
				to = 6;
			} else if (mathMode.isLevel6()) {
				from = -20;
				to = 20;
			}
			
			inE = this.randomInequilvalance(from, to, false);
			mode.score(1);
			Box2dContact.isCollided = false;
		}
	}

	@Override
	public void spawnCase(int total, int minGroup, int maxGroup,
			int minPerGroup, int maxPerGroup, boolean isFirstTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void spawnAt(int startIndex, int numObstacle) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	private int[] randomFormula(int from, int to, float chanceResult,
			float chanceType) {
		int formula[] = new int[3];
		formula[0] = MathUtils.random(from, to);
		formula[1] = MathUtils.random(from, to);
		// random result yes or no
		result = MathUtils.randomBoolean(chanceResult);

		// random type of formula
		if (MathUtils.randomBoolean(chanceType))
			type = MATH_TYPE.ADD;
		else
			type = MATH_TYPE.MUL;

		// The value of formula depends on what value result is
		if (type == MATH_TYPE.ADD) {
			formula[2] = formula[0]
					+ formula[1]
					+ (result ? 0 : (MathUtils.randomBoolean() ? MathUtils
							.random(-epsilon, -1) : MathUtils
							.random(1, epsilon)));
		} else if (type == MATH_TYPE.MUL) {
			formula[2] = formula[0]
					* formula[1]
					+ (result ? 0 : (MathUtils.randomBoolean() ? MathUtils
							.random(-epsilon, -1) : MathUtils
							.random(1, epsilon)));
		}
		return formula;
	}

	private int[] randomInequilvalance(int from, int to, boolean isFirstTime) {
		int inE[] = new int[2];
		inE[0] = MathUtils.random(from, to);
		inE[1] = MathUtils.random(from, to);

		MATH_SIGN tmpSign = null;
		if (inE[0] > inE[1])
			tmpSign = MATH_SIGN.GREATER;
		if (inE[0] == inE[1])
			tmpSign = MATH_SIGN.EQUAL;
		if (inE[0] < inE[1])
			tmpSign = MATH_SIGN.SMALLER;

		if (!isFirstTime) {
			int rand = MathUtils.random(1, 3);
			if (rand == 1)
				sign = MATH_SIGN.SMALLER;
			if (rand == 2)
				sign = MATH_SIGN.EQUAL;
			if (rand == 3)
				sign = MATH_SIGN.GREATER;
		} else {
			sign = tmpSign;
		}

		if (sign == tmpSign)
			result = true;
		else
			result = false;
		return inE;
	}

	public boolean checkResult(int collidedIndex) {
		// default answer is yes
		boolean answer = true;
		// change if answer is no
		if ((collidedIndex >= 0 && collidedIndex <= 10) || (collidedIndex > 30))
			answer = false;
		// if answer is the same with result -> correct
		if (answer == result)
			return true;
		// else the answer is wrong
		return false;
	}

	public int[] getFormula() {
		return formula;
	}

	public int[] getInequilvalance() {
		return inE;
	}

	public MATH_TYPE getFormulaType() {
		return type;
	}

	public MATH_SIGN getMathSign() {
		return sign;
	}

	public boolean isADD() {
		return type == MATH_TYPE.ADD;
	}

	public boolean isMUL() {
		return type == MATH_TYPE.MUL;
	}

	public boolean isGREATER() {
		return sign == MATH_SIGN.GREATER;
	}

	public boolean isSMALLER() {
		return sign == MATH_SIGN.SMALLER;
	}

	public boolean isEQUAL() {
		return sign == MATH_SIGN.EQUAL;
	}
}
