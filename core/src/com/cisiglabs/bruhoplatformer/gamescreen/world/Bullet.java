/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world;

import com.cisiglabs.bruhoplatformer.gamescreen.Level;
import com.cisiglabs.bruhoplatformer.gamescreen.world.base.PhysicsActor;

/**
 * @author kg
 *
 */
public class Bullet extends PhysicsActor {

  private static final float WIDTH = 0.35f;
  private static final float HEIGHT = 0.25f;

  private static float VX = 12f;
  
  private float damage;

  public Bullet(Level level, float x, float y, float damage) {
    super(x, y, WIDTH, HEIGHT);

    setGrounded(false);
    setLevel(level);
    setDamage(damage);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    velocity.x = VX;
    
    for (Enemy enemy : level.getEnemies()) {
      if (this.collidesWith(enemy)) {
        enemy.hurt(damage);
      }
    }
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

  /**
   * @return the damage
   */
  public float getDamage() {
    return damage;
  }

  /**
   * @param damage the damage that can be dealt to the enemy
   */
  private void setDamage(float damage) {
    this.damage = damage;
  }

  
}
