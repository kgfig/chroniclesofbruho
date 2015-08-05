/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Enemy;

/**
 * @author kg
 *
 */
public class TimedBombEnemy extends Enemy {

  private static final String TAG = TimedBombEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 0.5f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 10;

  private static final float NEAR_DISTANCE = 5.5f;
  private static final int COUNTDOWN_IN_SEC = 3;

//  private Action swellInPlace, explode;
  private Action currentAction;
  private Runnable explodeRunnable;
  private boolean swelling, triggered;

  public TimedBombEnemy(Level level, float x, float y) {
    super(Type.BOMB, x, y, SIZE, SIZE);

    setGrounded(true);
    setNearThreshold(NEAR_DISTANCE);
    setState(State.IDLE);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);
    setOrigin(SIZE / 2, 0);
    bounds.setOrigin(SIZE / 2, 0);
    setSwelling(false);
    setTriggered(false);

    explodeRunnable = new Runnable() {
      public void run() {
        removeAction(currentAction);
        // show explosion animation
        // shots bits
        // Deal damage to the target on collision
        Gdx.app.log(TAG, "Exploded!");
        remove();
      }
    };

    Gdx.app.log(TAG,
        String.format("Created TimedBombEnemy with HP=%f\tDamage=%f.", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = gravity;

    if (!level.isComplete()) {

      if (isNearTarget() && !isStateAttacking()) {
        // Start countdown if target is near and swell big time as if it were about to explode
        Gdx.app.log(TAG, "Swell to explode");
        setState(State.ATTACKING);
        // startCountdown();
        swellToExplode();

      } else if (isStateMoving() && !swelling) {
        Gdx.app.log(TAG, "Swell in place");
        // swell in place
        swelling = true;
        swellInPlace();
      }

      // Deal damage to the target on collision
      if (!target.isDying() && this.collidesWith(target) && !attacked) {
        setAttacked(true);
        target.hurt(damage);
        Gdx.app.log(TAG, "Collided with target. Attacked with " + getDamage() + " damage.");
      }

      if (!target.isDying() && this.collidesWith(target.getMeleeWeapon()) && !triggered) {
        setTriggered(true);
        explode();
        Gdx.app.log(TAG, "Collided with melee weapon. Explode!");
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
  protected void collideAlongY(float delta, boolean left) {
    if (velocity.y < 0)
      setGrounded(true);
    velocity.y = 0;
  }

  /**
   * @param swelling true if the enemy is swelling
   */
  private void setSwelling(boolean swelling) {
    this.swelling = swelling;
  }

  /**
   * Adds the action for the enemy to swell in place
   */
  private void swellInPlace() {
    currentAction = Actions.forever(
        Actions.sequence(Actions.scaleBy(0.5f, 0.5f, 0.5f), Actions.scaleBy(-0.5f, -0.5f, 0.5f)));
    addAction(currentAction);
  }

  /**
   * Adds the action for the enemy to swell then explode when countdown is over
   */
  private void swellToExplode() {
    // Remove previous actions
    removeAction(currentAction);

    Action swellToExplode = Actions.repeat(COUNTDOWN_IN_SEC,
        Actions.sequence(Actions.scaleBy(0.65f, 0.65f, 0.5f), Actions.scaleBy(-0.5f, -0.5f, 0.5f)));
    currentAction =
        Actions.sequence(swellToExplode, Actions.run(new Runnable() {
          public void run() {
            setTriggered(true);
          }
        }), Actions.scaleBy(4, 4, 0.2f), Actions.run(explodeRunnable));

    addAction(currentAction);
  }

  private void explode() {
    // Create explosion action
    if (currentAction!=null)
      removeAction(currentAction);
    
    currentAction = Actions.sequence(Actions.scaleBy(4, 4, 0.2f), Actions.run(explodeRunnable));
    addAction(currentAction);
  }

  private void die() {
    setState(State.DYING);
    remove();
  }

  /**
   * @param exploded the exploded to set
   */
  public void setTriggered(boolean exploded) {
    this.triggered = exploded;
  }
  
}
