/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.base;

import com.badlogic.gdx.Gdx;

/**
 * @author kg
 *
 */
public abstract class GameActor extends PhysicsActor {

  private static final String TAG = GameActor.class.getSimpleName();

  protected float hp, maxHp;
  protected float damage;

  public GameActor() {
    super();
  }

  public GameActor(float x, float y, float width, float height) {
    super(x, y, width, height);

    initStats();
  }

  private void initStats() {
    hp = 0;
    maxHp = 0;
    damage = 0;
  }

  /**
   * Returns true if the actor has remaining HP
   * 
   * @return true if the actor has remaining HP
   */
  public boolean hasHp() {
    return getHp() > 0;
  }

  /**
   * Called when actor is hurt to reduce his HP
   * 
   * @param damage
   */
  public void hurt(float damage) {
    if (hp - damage >= 0)
      hp = hp - damage;
    else
      hp = 0;
    Gdx.app.log(TAG, "Got hurt with " + damage + " damage. New hp=" + hp);
  }

  /**
   * @return the hp
   */
  public float getHp() {
    return hp;
  }

  /**
   * @param hp the hp to set
   */
  public void setHp(float hp) {
    this.hp = hp;
  }

  /**
   * @return the maxHp
   */
  public float getMaxHp() {
    return maxHp;
  }

  /**
   * @param maxHp the maxHp to set
   */
  public void setMaxHp(float maxHp) {
    this.maxHp = maxHp;
  }

  /**
   * @return the damage
   */
  public float getDamage() {
    return damage;
  }

  /**
   * @param damage the damage to set
   */
  public void setDamage(float damage) {
    this.damage = damage;
  }

}
