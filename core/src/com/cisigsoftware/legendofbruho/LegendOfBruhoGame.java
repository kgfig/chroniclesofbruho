/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.cisigsoftware.legendofbruho.screens.GameScreen;

/**
 * @author kg
 *
 */
public class LegendOfBruhoGame extends Game {

  private Screen currentScreen;
  
  @Override
  public void create() {
    currentScreen = new GameScreen();
    setScreen(currentScreen);
  }

}
