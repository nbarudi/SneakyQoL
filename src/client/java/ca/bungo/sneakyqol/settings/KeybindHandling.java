package ca.bungo.sneakyqol.settings;

import ca.bungo.sneakyqol.settings.keybindings.*;


public class KeybindHandling {

    public static final String sneakyKeybindSection = "category.sneakyqol.main";

    public static void initializeKeybindHandler(){
        new ShowNamesKeybind();
        new LeanKeybind();
        new VoiceToTextKeybind();
        new SimpleCommandKeybinds();
        new BetterSneakKeybind();


        //Things the Admins Prob Wont Like
        new RadarKeybind();


        //Things I know The Admins Dont Like
        //new YoinkSkinKeybind();






        //new TestKeybind();
    }

}
