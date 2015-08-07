/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.cisiglabs.bruhoplatformer.gamescreen.GameScreen;
import com.cisigsoftware.legendofbruho.utils.Assets;

/**
 * @author kg
 *
 */
public class LegendOfBruhoGame extends Game {

  private LoadingScreen loadingScreen;
  private Screen currentScreen;
  private boolean loaded;

  @Override
  public void create() {
    Assets.loadSplashAssets();
    loadingScreen = new LoadingScreen();

    loaded = false;
    Assets.loadGameAssets();
    currentScreen = new GameScreen();
  }

  @Override
  public void render() {
    if (Assets.loading()) {
      loadingScreen.setProgress(Assets.getProgress());
      loadingScreen.render(Gdx.graphics.getDeltaTime());
      
    } else {
      if (!loaded) {
        loaded = true;
        Assets.gameAssetsLoaded();

        super.screen = currentScreen;
        screen.show();
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      }

      if (loaded) {
        screen.render(Gdx.graphics.getDeltaTime());
      }
    }
  }

}
