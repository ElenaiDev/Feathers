package com.elenai.feathers.compat;

public class ClassicBarsCompat {

    public static void registerClassicBarOverlay() {
        tfar.classicbar.EventHandler.register(new com.elenai.feathers.compat.FeathersBarWrapper().getBar());
    }

}