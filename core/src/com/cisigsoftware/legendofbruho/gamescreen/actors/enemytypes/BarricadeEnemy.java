/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Enemy;

/**
 * @author kg
 *
 */
public class BarricadeEnemy extends Enemy {

  private static final String TAG = BarricadeEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 1f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 5;

  private Vector2 cellPosition;

  public BarricadeEnemy(Level level, float x, float y) {
    super(Type.BARRICADE, x, y, SIZE, SIZE);
    cellPosition = new Vector2(x, y);

    setNearThreshold(0);
    setGrounded(true);
    setState(State.IDLE);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setMaxHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);

    Gdx.app.log(TAG,
        String.format("Created BarricadeEnemy with HP=%f\tDamage=%f.", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = gravity;

    if (!level.isComplete()) {
      // Unset attacked flag
      if (!target.isDying() && attacked && !this.collidesWith(target)) {
        setAttacked(false);
        Gdx.app.log(TAG, "Collided with target. Unset attacked " + attacked());
      }

      if (!target.isDying() && this.collidesWith(target) && !attacked) {
        setAttacked(true);
        target.hurt(damage);
        Gdx.app.log(TAG, "Collided with target. Attacked with " + getDamage() + " damage.");
      }

      if (target.isSlashing() && target.getMeleeWeapon().collidesWith(this)) {
        Gdx.app.log(TAG, "Collided with weapon. Enemy hit by " + target.getDamage() + " damage!");
        this.hurt(target.getDamage());

        if (!this.hasHp() && !this.isStateDying()) {
          Gdx.app.log(TAG, "Enemy died.");
          die();
        }
      }
    }
  }

  @Override
  protected void collideAlongX(float delta, boolean left) {
    velocity.x = 0;
  }

  @Override
  protected void collideAlongY(float delta, boolean top) {
    if (velocity.y < 0)
      setGrounded(true);
    velocity.y = 0;
  }

  private void die() {
    setState(State.DYING);
    remove();
    level.removeCollidable((int) cellPosition.x, (int) cellPosition.y);
  }
}
