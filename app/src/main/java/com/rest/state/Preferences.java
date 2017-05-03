package com.rest.state;

/**
 * Created by Bilal on 8.1.2017 г..
 */
public class Preferences {
    public static int REST_DELAY;
    public static int SLEEP_DELAY;
    public static int MIN_REST_DELAY = 5;
    public static boolean FEEDBACK_ON = true;

    /////////////////////////////////////

    public static final String RESET_CYCLE_LENGTH = "reset_cycle_length";
    public static final int OPTIMAL_CYCLES = 6;
    public static final int OPTIMAL_CYCLES_MIN = 5;
    public static final int OPTIMAL_CYCLES_MAX = 7;
    public static int OPTIMAL_SLEEP = 480;

    public static class Default {
        public static final int CYCLE_LENGTH = 90;
        public static final int REST_DELAY = 15;
        public static final int SLEEP_DELAY = 20;
        public static final boolean FEEDBACK_ON = true;
    }

    public static class Key {
        public static final String REST_DELAY_KEY = "rest_delay";
        public static final String SLEEP_DELAY_KEY = "sleep_delay";
        public static final String FEEDBACK_ON_KEY = "feedback_on";
    }
}
