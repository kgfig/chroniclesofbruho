/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cisigsoftware.legendofbruho.screens.game.Level;

/**
 * @author kg
 *
 */
public class BouncingEnemy extends Enemy {

  private static final String TAG = BouncingEnemy.class.getSimpleName();

  private static final float SIZE = 0.5f;
  private static final float NEAR_DISTANCE = 5f;
  private static final Vector2 MOVING_VEL = new Vector2(0, 1.5f);
  private static final Vector2 ATTACK_VEL = new Vector2(3, 1.5f);

  public BouncingEnemy(Level level, float x, float y) {
    super(level, x, y, SIZE, SIZE);
    setNearThreshold(NEAR_DISTANCE);
  }


  @Override
  public void move(float delta) {
    velocity.set(MOVING_VEL);
    Gdx.app.log(TAG, "set velocity! " + velocity);
    //
    // Action bouncingAction = Actions.forever(Actions.sequence(
    // Actions.delay(0.125f, Actions.moveBy(velocity.x, velocity.y, 0.5f, Interpolation.pow2)),
    // Actions.moveBy(velocity.x, -velocity.y, 0.4f, Interpolation.pow2)));
    //
    // addAction(bouncingAction);
  }

  @Override
  public void attack(float delta) {
    clearActions();

    setState(State.ATTACKING);
    velocity.set(ATTACK_VEL);

    Action attackAction =
        Actions.repeat(3,
            Actions.sequence(
                Actions.delay(0.125f,
                    Actions.moveBy(velocity.x, velocity.y, 0.5f, Interpolation.pow2)),
            Actions.moveBy(velocity.x, -velocity.y, 0.4f, Interpolation.pow2)));
    addAction(attackAction);
  }

  @Override
  public void die(float delta) {
    clearActions();

    setState(State.DYING);
    Gdx.app.log(TAG, "Bouncing enemy died");

    remove();
  }


}
