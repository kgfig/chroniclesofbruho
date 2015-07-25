/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.base;

/**
 * @author kg
 *
 */
public abstract class Weapon extends BoundedActor {

  public Weapon(float x, float y, float width, float height) {
    super(x, y, width, height);
  }

  public abstract void use();
}
