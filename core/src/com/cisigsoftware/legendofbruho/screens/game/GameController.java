/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kg
 *
 */
public class GameController {

  public enum Keys {
    LEFT, RIGHT, JUMP, FIRE
  }

  static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

  static {
    keys.put(Keys.LEFT, false);
    keys.put(Keys.RIGHT, false);
    keys.put(Keys.JUMP, false);
    keys.put(Keys.FIRE, false);
  }

  public GameController() {
  }

  public void leftPressed() {
    keys.put(Keys.LEFT, true);
  }

  public void rightPressed() {
    keys.put(Keys.RIGHT, true);
  }

  public void jumpPressed() {
    keys.put(Keys.JUMP, true);
  }

  public void firePressed() {
    keys.put(Keys.FIRE, true);
  }

  public void leftReleased() {
    keys.put(Keys.LEFT, false);
  }

  public void rightReleased() {
    keys.put(Keys.RIGHT, false);
  }

  public void jumpReleased() {
    keys.put(Keys.JUMP, false);
  }

  public void fireReleased() {
    keys.put(Keys.FIRE, false);
  }
  
  public boolean isLeftPressed() {
    return isPressed(Keys.LEFT);
  }
  
  public boolean isRightPressed() {
    return isPressed(Keys.RIGHT);
  }
  
  public boolean isJumpPressed() {
    return isPressed(Keys.JUMP);
  }
  
  public boolean isFirePressed() {
    return isPressed(Keys.FIRE);
  }

  public boolean isPressed(Keys key) {
    return keys.get(key);
  }
}
