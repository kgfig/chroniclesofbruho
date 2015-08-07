/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world;

import com.cisiglabs.bruhoplatformer.gamescreen.world.base.BoundedActor;

/**
 * @author kg
 *
 */
public abstract class Weapon extends BoundedActor {

  protected enum Type {
    MELEE, RANGE
  }
  
  protected Type type;
  
  public Weapon(Type type, float x, float y, float width, float height) {
    super(x, y, width, height);
    this.type = type;
  }

  public abstract void use();
}
