/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cisigsoftware.legendofbruho.LegendOfBruhoGame;
import com.cisigsoftware.legendofbruho.utils.Constants;

/**
 * @author kg
 *
 */
public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = Constants.SCREEN_WIDTH;
    config.height = Constants.SCREEN_HEIGHT;
    new LwjglApplication(new LegendOfBruhoGame(), config);
  }
}
