package bfst19.danmarkskort.model;

import bfst19.danmarkskort.utils.ThemeLoader;

public class Themes { //todo rename this to BuiltInThemes
    public static final Theme DefaultTheme = ThemeLoader.loadTheme("rs:config/themes/default.yaml");
    public static final Theme HDGraphics = ThemeLoader.loadTheme(
            "rs:config/themes/hdgraphics.yaml",
            ThemeLoader.loadTheme("rs:config/themes/default.yaml")
    );
}
