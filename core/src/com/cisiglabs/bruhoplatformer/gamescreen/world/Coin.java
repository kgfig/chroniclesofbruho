/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world;

import com.cisiglabs.bruhoplatformer.gamescreen.Level;

/**
 * @author kg
 *
 */
public class Coin extends Item {

  private int value;
  
  /**
   * @param level
   * @param x
   * @param y
   */
  public Coin(Level level, float x, float y) {
    super(ItemType.COIN, level, x, y);
  }

  /**
   * @return the value of the coin
   */
  public int getValue() {
    return value;
  }

  /**
   * @param value the value of the coin
   */
  public void setValue(int value) {
    this.value = value;
  }
  
}
