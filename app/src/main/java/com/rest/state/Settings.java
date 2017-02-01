package com.rest.state;

/**
 * Created by Bilal on 8.1.2017 г..
 */
public class Settings {
    public static final int CYCLE_LENGTH = 90;
    public static final int REST_DELAY = 15;
    public static final int SLEEP_DELAY = 15;
    public static int OPTIMAL_SLEEP = 480;

    public static class Default {
        public static Settings build() {
            return new Settings();
        }
    }
}
