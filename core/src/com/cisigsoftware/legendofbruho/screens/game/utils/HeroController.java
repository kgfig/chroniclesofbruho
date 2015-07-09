/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kg
 *
 */
public class HeroController {

  public enum Keys {
    LEFT, RIGHT, JUMP, FIRE
  }

  private static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

  static {
    keys.put(Keys.LEFT, false);
    keys.put(Keys.RIGHT, false);
    keys.put(Keys.JUMP, false);
    keys.put(Keys.FIRE, false);
  }

  public HeroController() {}

  private void setPressed(Keys key, boolean pressed) {
    keys.put(key, pressed);
  }

  private boolean isPressed(Keys key) {
    return keys.get(key);
  }

  public void leftPressed() {
    setPressed(Keys.LEFT, true);
  }

  public void rightPressed() {
    setPressed(Keys.RIGHT, true);
  }

  public void jumpPressed() {
    setPressed(Keys.JUMP, true);
  }

  public void firePressed() {
    setPressed(Keys.FIRE, true);
  }

  public void leftReleased() {
    setPressed(Keys.LEFT, false);
  }

  public void rightReleased() {
    setPressed(Keys.RIGHT, false);
  }

  public void jumpReleased() {
    setPressed(Keys.JUMP, false);
  }

  public void fireReleased() {
    setPressed(Keys.FIRE, false);
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

}
