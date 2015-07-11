/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.Gdx;
import com.cisigsoftware.legendofbruho.screens.game.Level;

/**
 * @author kg
 *
 */
public abstract class Enemy extends GameActor {

  protected enum State {
    IDLE, MOVING, ATTACKING, DYING;
  }

  private static final String TAG = Enemy.class.getSimpleName();

  protected BoundedActor target;
  private State state;
  private float nearThreshold;

  public Enemy(Level level, float x, float y, float width, float height) {
    super(level, x, y, width, height);
    
    state = State.MOVING;
  }
  
  /**
   * Override this method to implement the standby movement of the enemy
   * @param delta
   */
  protected abstract void move(float delta);
  
  /**
   * Override this method to implement how the enemy will attack the target
   * @param delta
   */
  protected abstract void attack(float delta);
  
  /**
   * Override this method to implement how the enemy will die when he is hit
   * @param delta
   */
  protected abstract void die(float delta);
  
  @Override
  public void act(float delta) {
    super.act(delta);
    
//    // Die if the enemy collides with the target and it's still alive
//    if (target != null && this.collidesBeside(target) && !isStateDying()) {
//      die(delta);
//    }
//    
    // Start idle movement of the enemy
    if (isStateMoving() && !isMoving()) {
      Gdx.app.log(TAG, "Start moving v.x="+velocity.x+"\tv.y="+velocity.y);
      move(delta);
    }
    
//    if (isStateIdle() && isMoving()) { // If he is supposed to be idle, clear all actions
//      Gdx.app.log(TAG, "Be idle");
//      clearActions();
//    }
    
//    // Attack the target if he is near and not yet attacking
//    if (isNearTarget() && !isStateAttacking()) {
//      Gdx.app.log(TAG, "Start attacking");
//      attack(delta);
//    }
  }

  /**
   * Sets the target GameActor of the enemy
   * @param target
   */
  
  public void setTarget(BoundedActor target) {
    this.target = target;
  }

  /**
   * @return the state
   */
  protected State getState() {
    return state;
  }

  /**
   * @param state the state to set
   */
  protected void setState(State state) {
    this.state = state;
  }

  /**
   * Returns true if the enemy's state is idle
   * @return true if the enemy's state is idle
   */
  public boolean isStateIdle() {
    return getState() == State.IDLE;
  }
  
  /**
   * Sets the state of the enemy to idle
   */
  public void setStateIdle() {
    setState(State.IDLE);
  }
  
  /**
   * Returns true if the enemy's state is moving
   * @return true if the enemy's state is moving
   */
  public boolean isStateMoving() {
    return getState() == State.MOVING;
  }
  
  /**
   * Sets the state of the enemy to moving
   */
  public void setStateMoving() {
    setState(State.MOVING);
  }
  
  /**
   * Returns true if the enemy's state is attacking
   * @return true if the enemy's state is attacking
   */
  public boolean isStateAttacking() {
    return getState() == State.ATTACKING;
  }
  
  /**
   * Returns true if the enemy's state is dying
   * @return true if the enemy's state is dying
   */
  public boolean isStateDying() {
    return getState() == State.DYING;
  }
  
  /**
   * Returns true if the velocity of the enemy is greater or less than 0
   * @return
   */
  protected boolean isMoving() {
    return velocity.x != 0 || velocity.y != 0;
  }
  
  /**
   * Returns true if the target is within the distance threshold of the enemy
   * @return
   */
  protected boolean isNearTarget() {
    return Math.abs(getX() - target.getX()) <= nearThreshold;
  }

  /**
   * @param nearThreshold the nearThreshold to set
   */
  public void setNearThreshold(float nearThreshold) {
    this.nearThreshold = nearThreshold;
  }
  
}
