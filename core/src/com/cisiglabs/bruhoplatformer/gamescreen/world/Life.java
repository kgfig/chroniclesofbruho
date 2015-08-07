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
public class Life extends Item {

  /**
   * @param level
   * @param x
   * @param y
   */
  public Life(Level level, float x, float y) {
    super(ItemType.LIFE, level, x, y);
  }
  
}
