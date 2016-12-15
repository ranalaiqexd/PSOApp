package com.inspections.pso.view;

import java.util.Random;

/**
 * Created by mobiweb on 20/9/16.
 */
public class GenerateRandomString {
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+=?><.abcdefghijklmnopqrstuvwxyz`~";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
}
