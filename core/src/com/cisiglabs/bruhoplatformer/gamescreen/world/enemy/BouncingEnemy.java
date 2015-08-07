/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world.enemy;

import com.badlogic.gdx.Gdx;
import com.cisiglabs.bruhoplatformer.gamescreen.Level;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Enemy;

/**
 * @author kg
 *
 */
public class BouncingEnemy extends Enemy {

  private static final String TAG = BouncingEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 0.5f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 5;

  private static final float NEAR_DISTANCE = 3f;
  private static final float VY = 9f;
  private static final float VX = 2f;

  public BouncingEnemy(Level level, float x, float y) {
    super(Type.BOUNCING, x, y, SIZE, SIZE);

    setNearThreshold(NEAR_DISTANCE);
    setGrounded(true);
    setState(State.IDLE);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setMaxHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);

    Gdx.app.log(TAG,
        String.format("Created BouncingEnemy with HP=%f\tDamage=%f.", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = gravity;

    if (!level.isComplete()) {
      /**
       * Attack if the target is near Otherwise, do standby movement
       */
      if (isNearTarget() && !isStateAttacking()) {
        // If the enemy is on the ground, toggle the flag and bounce up to attack.
        // While suspended in the air, move to the left or to the right depending on the target's
        // direction
        if (isGrounded()) {
          velocity.y = VY;
          setState(State.ATTACKING);
          setGrounded(false);
          Gdx.app.log(TAG, "Bounce up attack");
        } else {
          if (target.getX() > getX() + getWidth()) {
            velocity.x = VX;
            Gdx.app.log(TAG, "Bounce right attack");
          } else if (target.getX() + target.getWidth() < getX()) {
            velocity.x = -VX;
            Gdx.app.log(TAG, "Bounce left attack");
          }
        }
      } else if (isStateMoving() && isGrounded()) {
        // Bounce in place
        velocity.y = VY;
        setGrounded(false);
        Gdx.app.log(TAG, "Bounce in place");
      }

      /**
       * Deal damage on the target upon collision
       */
      if (!isGrounded()) {

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
      }
      
      if (!this.hasHp() && !this.isStateDying()) {
        Gdx.app.log(TAG, "Enemy died.");
        die();
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
  }
}
