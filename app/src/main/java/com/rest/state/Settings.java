package com.rest.state;

/**
 * Created by Bilal on 8.1.2017 г..
 */
public class Settings {
    //Initialized by PersistenceController at start
    public static int CYCLE_LENGTH;
    public static int REST_DELAY;
    public static int SLEEP_DELAY;
    /////////////////////////////////////

    public static int MIN_REST_DELAY = 5;
    public static int OPTIMAL_SLEEP = 480;

    public static class Default {
        public static final int CYCLE_LENGTH = 90;
        public static final int REST_DELAY = 15;
        public static final int SLEEP_DELAY = 20;
    }

    public static class Key {
        public static final String CYCLE_LENGTH_KEY = "cycle_length";
        public static final String REST_DELAY_KEY = "rest_delay";
        public static final String SLEEP_DELAY_KEY = "sleep_delay";
    }
}
