/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kg
 *
 */
public class Controls {

  public enum Keys {
    LEFT, RIGHT, JUMP, ATTACK, SWITCH
  }

  private static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

  static {
    keys.put(Keys.LEFT, false);
    keys.put(Keys.RIGHT, false);
    keys.put(Keys.JUMP, false);
    keys.put(Keys.ATTACK, false);
    keys.put(Keys.SWITCH, false);
  }

  public Controls() {}

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

  public void attackedPressed() {
    setPressed(Keys.ATTACK, true);
  }
  
  public void switchPressed() {
    setPressed(Keys.SWITCH, true);
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

  public void attackedReleased() {
    setPressed(Keys.ATTACK, false);
  }
  
  public void switchReleased() {
    setPressed(Keys.SWITCH, false);
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

  public boolean isAttackPressed() {
    return isPressed(Keys.ATTACK);
  }

  public boolean isSwitchPressed() {
    return isPressed(Keys.SWITCH);
  }
}
