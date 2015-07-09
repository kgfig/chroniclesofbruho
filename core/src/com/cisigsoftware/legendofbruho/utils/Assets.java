/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author kg
 *
 */
public class Assets extends AssetManager {

  public Assets() {
    super();
  }

  public void loadTexture(String filename) {
    load(filename, Texture.class);
  }
  
  public void loadAtlas(String filename) {
    load(filename, TextureAtlas.class);
  }
  
  public void loadSkin(String filename) {
    load(filename, Skin.class);
  }
  
  public void loadPixmap(String filename) {
    load(filename, Pixmap.class);
  }
  
  public void loadParticlEffect(String filename) {
    load(filename, ParticleEffect.class);
  }
  
  public void loadFont(String filename) {
    load(filename, BitmapFont.class);
  }
  
  public void loadMusic(String filename) {
    load(filename, Music.class);
  }
  
  public void loadSound(String filename) {
    load(filename, Sound.class);
  }
}
