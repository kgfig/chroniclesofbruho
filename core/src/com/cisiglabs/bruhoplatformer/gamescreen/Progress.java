/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author kg
 *
 */
public class Progress {

  public static String FILENAME = "bruhogame.config";
  private static String COINS = "COINS";
  private static String LIVES = "LIVES";
  private static String ENEMIES = "ENEMIES";
  private static String LEVEL = "LEVEL";

  private static Properties properties = new Properties();

  public Progress() {
    try {
      FileInputStream in = new FileInputStream(FILENAME);
      properties.load(in);
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setCoins(int value) {
    properties.setProperty(COINS, String.format("%d", value));
  }

  public void setLives(int value) {
    properties.setProperty(LIVES, String.format("%d", value));
  }

  public void setEnemies(int value) {
    properties.setProperty(ENEMIES, String.format("%d", value));
  }

  public void setLevel(int value) {
    properties.setProperty(LEVEL, String.format("%d", value));
  }

  public int getCoins() {
    return Integer.valueOf(properties.getProperty(COINS));
  }

  public int getLives() {
    return Integer.valueOf(properties.getProperty(LIVES));
  }

  public int getEnemies() {
    return Integer.valueOf(properties.getProperty(ENEMIES));
  }

  public int getLevel() {
    return Integer.valueOf(properties.getProperty(LEVEL));
  }

  public void save() {
    try {
      FileOutputStream out = new FileOutputStream(FILENAME);
      properties.store(out, "Last saved: " + System.currentTimeMillis());
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
