package com.rest.state;

import android.util.Log;

/**
 * Created on 03/02/2017
 * This class learns the users
 * cycle length using calculated weight
 * with the following formulas:
 * Jump value = (MAX_RATING - rating) * Alignment direction / Confidence
 * Cycle length += Cycle length * Shift value
 * Confidence = (previous) Confidence + (rating - AVG_RATING)
 * where
 * Alignment direction{-1,1}
 * Jump value is a float between (-1;1) and determines how much
 * of a jump will be made as the jump either to a shorter
 * or longer cycle length (determined by the alignment direction).
 * It is 0 when the rating is maximum
 * and the jump is inversely-proportional to the confidence.
 */

public class CycleController {
    public static final String CYCLE_CTRL = CycleController.class.getSimpleName();

    //Initialized by PersistenceController at start
    public static int CYCLE_LENGTH;

    //Cycle variables - initialized on rate event
    public static int ALIGN_DIRECTION;
    public static int CONFIDENCE;

    public static void onReceiveRating(float mRating) {
        int rating = (int) mRating;

        // Update variables from SharedPreferences
        App.getpController().loadCycleVariables();

        float jumpValue = getJumpValue(rating);
        CYCLE_LENGTH = ensureRange(CYCLE_LENGTH + CYCLE_LENGTH * jumpValue,
                Constants.ABSOLUTE_MIN_CYCLE_LENGTH,
                Constants.ABSOLUTE_MAX_CYCLE_LENGTH);

        CONFIDENCE = ensureRange(CONFIDENCE + (rating - Constants.AVG_RATING),
                Constants.MIN_CONFIDENCE,
                Constants.MAX_CONFIDENCE);

        ALIGN_DIRECTION *= -1; // Flip the direction

        Log.d(CYCLE_CTRL, "Cycle length recalculated: " + CYCLE_LENGTH);
        Log.d(CYCLE_CTRL, "Confidence recalculated: " + CONFIDENCE);

        // Save the variables along with the cycle length
        App.getpController().saveCycleVariables();

    }

    //ToDo Test
    private static int ensureRange(float value, int min, int max) {
        return Math.min(Math.max((int) value, min), max);
    }

    private static float getJumpValue(int rating) {
        return (Constants.MAX_RATING - rating) * ALIGN_DIRECTION / (float) CONFIDENCE;
    }

    public static class Constants {
        public static final int DEFAULT_ALIGN_DIRECTION = 1;
        public static final int DEFAULT_CONFIDENCE = 22;
        public static final int MAX_RATING = 5;
        public static final int AVG_RATING = 3;
        public static final int ABSOLUTE_MIN_CYCLE_LENGTH = 70;
        public static final int ABSOLUTE_MAX_CYCLE_LENGTH = 120;
        public static final int MIN_CONFIDENCE = 4;
        public static final int MAX_CONFIDENCE = 40;
        public static final String ALIGN_DIRECTION_KEY = "align_direction";

        public static final String CONFIDENCE_KEY = "confidence";
        public static final String CYCLE_LENGTH_KEY = "cycle_length";
    }
}
