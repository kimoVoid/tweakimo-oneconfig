package io.github.kimovoid.tweakimo.config;

import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Checkbox;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;

public class TweakimoConfig extends Config {

    public static final TweakimoConfig INSTANCE = new TweakimoConfig();

    @Checkbox(
            title = "Always Render Sky",
            description = "Renders the skybox below 4 render distance."
    )
    public static boolean alwaysRenderSky = true;

    @Checkbox(
            title = "Longer Chat Messages",
            description = "Increases the chat message limit to 256 characters.\nRequires ChatTweaks \"Bypass Command Length Limit\" setting to be off."
    )
    public static boolean longerChatMessages = true;

    @Slider(
            title = "Fog Start",
            subcategory = "Fog",
            min = 0.0f,
            max = 1.0f,
            step = 0.1f
    )
    public static float fogStart = 0.8f;

    @Checkbox(
            title = "Enable Custom Fog Color",
            description = "Best to use with a custom skybox applied.",
            subcategory = "Fog"
    )
    public static boolean fogColorToggle = false;

    @Color(
            title = "Custom Fog Color",
            subcategory = "Fog",
            alpha = false
    )
    public static PolyColor fogColor = PolyColor.Companion.getWHITE();

    public TweakimoConfig() {
        super(
                "tweakimo.json",
                "/assets/tweakimo/icon.svg",
                "Tweakimo",
                Category.QOL
        );
    }
}
