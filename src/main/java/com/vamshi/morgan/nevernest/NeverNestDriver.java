package com.vamshi.morgan.nevernest;

public class NeverNestDriver {

    public static void main(String[] args) {

        NeverNestBefore neverNestBefore = new NeverNestBefore();

        neverNestBefore.start();

        neverNestBefore.appendDownload("https://somewebsite/download");

    }

}
