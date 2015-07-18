/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author kg
 *
 */
public class Assets {

  private static final String TAG = Assets.class.getSimpleName();

  /*
   * Assets
   */
  public static Skin gameSkin, loadingSkin;
  
  /*
   * Asset descriptors
   */
  private static AssetManager manager;
  private static final AssetDescriptor<Skin> gameSkinDesc =
      new AssetDescriptor<Skin>("assets/skin/game.json", Skin.class);
  private static final AssetDescriptor<Skin> loadingSkinDesc =
      new AssetDescriptor<Skin>("assets/skin/loading.json", Skin.class);

  public Assets() {}

  public static void loadSplashAssets() {
    if (manager == null)
      manager = new AssetManager();
    
    load(loadingSkinDesc);
    manager.finishLoading();
    
    loadingSkin = get(loadingSkinDesc);
  }

  public static void loadGameAssets() {
    if (manager == null)
      manager = new AssetManager();
    
    load(gameSkinDesc);
  }

  public static void gameAssetsLoaded() {
    gameSkin = get(gameSkinDesc);
  }

  public static boolean loading() {
    return !manager.update();
  }

  public static float getProgress() {
    return manager.getProgress();
  }

  private static <T> T get(AssetDescriptor<T> assetDescriptor) {
    return manager.get(assetDescriptor);
  }

  private static <T> void load(AssetDescriptor<T> assetDescriptor) {
    Gdx.app.log(TAG, String.format("Load: %s",
        Gdx.files.internal(assetDescriptor.fileName).file().getAbsolutePath()));
    manager.load(assetDescriptor);
  }

}
