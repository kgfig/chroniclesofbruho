/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

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

  protected Hero target;
  private State state;
  private float nearThreshold;
  protected boolean attacked;

  public Enemy(Level level, float x, float y, float width, float height) {
    super(level, x, y, width, height);
  }
  
  /**
   * Sets the target GameActor of the enemy
   * 
   * @param target
   */

  public void setTarget(Hero target) {
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
   * 
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
   * 
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
   * 
   * @return true if the enemy's state is attacking
   */
  public boolean isStateAttacking() {
    return getState() == State.ATTACKING;
  }

  /**
   * Returns true if the enemy's state is dying
   * 
   * @return true if the enemy's state is dying
   */
  public boolean isStateDying() {
    return getState() == State.DYING;
  }
  
  /**
   * Sets the state of the enemy to dying
   */
  public void setStateDying() {
    setState(State.DYING);
  }
  
  /**
   * @return true if the enemy has attacked and harmed the target
   */
  public boolean attacked() {
    return attacked;
  }

  /**
   * Sets if the enemy has attacked and harmed the target
   * @param flag to indicate that the enemy has attacked and harmed the target
   */
  public void setAttacked(boolean attacked) {
    this.attacked = attacked;
  }

  /**
   * Returns true if the velocity of the enemy is greater or less than 0
   * 
   * @return
   */
  protected boolean isMoving() {
    return velocity.x != 0 || velocity.y != 0;
  }

  /**
   * Returns true if the target is within the distance threshold of the enemy
   * 
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
