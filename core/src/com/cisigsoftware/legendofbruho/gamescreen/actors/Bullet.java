/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.cisigsoftware.legendofbruho.gamescreen.actors.base.PhysicsActor;

/**
 * @author kg
 *
 */
public class Bullet extends PhysicsActor {

  private static final float WIDTH = 0.35f;
  private static final float HEIGHT = 0.25f;

  private static float VX = 12f;

  public Bullet(float x, float y) {
    super(x, y, WIDTH, HEIGHT);

    setGrounded(false);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    velocity.x = VX;
  }
  
  @Override
  protected void collideAlongX(float delta, boolean left) {
    velocity.x = 0;
    remove();
  }

  @Override
  protected void collideAlongY(float delta, boolean top) {
    if (velocity.y < 0)
      setGrounded(true);
    velocity.y = 0;
    remove();
  }

}
