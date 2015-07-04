/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.EnemyData;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.UserData;

/**
 * @author kg
 *
 */
public class Enemy extends GameActor {

  public static final float X = 30f;
  public static final float Y = Ground.Y + Ground.HEIGHT;
  public static final float WIDTH = 2f;
  public static final float HEIGHT = 2f;
  public static final float DENSITY = 1.5f;
  public static final Vector2 ATTACK_LINEAR_IMPULSE = new Vector2(-30, 65);
  public static final Vector2 READY_LINEAR_IMPULSE = new Vector2(0, 55);
  public static final Vector2 ATTACK_DISTANCE = new Vector2(15, 0);

  private boolean bouncing;
  private boolean attacking;

  public Enemy(Body body) {
    super(body);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (!isAttacking() && !isBouncing()) {
      bounceInPlace();
    }
  }

  @Override
  public EnemyData getUserData() {
    return (EnemyData) userData;
  }

  /**
   * Returns true if the enemy body is bouncing off the ground
   * 
   * @return true if the enemy body is bouncing off the ground
   */
  public boolean isBouncing() {
    return bouncing;
  }

  /**
   * Sets the bouncing state
   * 
   * @param bouncing set to true if the enemy body is bouncing off the ground
   */
  private void setBouncing(boolean bouncing) {
    this.bouncing = bouncing;
  }

  /**
   * Applies a linear impulse on the body to make it bounce in place
   */
  public void bounceInPlace() {
    Gdx.app.log("Enemy", "Bounce in place!");
    bounce(getUserData().getReadyLinearImpulse());
  }

  /**
   * Applies a linear impulse on the body to make it bounce
   * 
   * @param linearImpulse
   */
  public void bounce(Vector2 linearImpulse) {
    Gdx.app.log("Enemy", "Bounce!");
    setBouncing(true);
    body.applyLinearImpulse(linearImpulse, body.getWorldCenter(), true);
  }

  /**
   * Disables the bouncing state
   */
  public void landed() {
    Gdx.app.log("Enemy", "Landed.");
    setBouncing(false);
  }

  /**
   * Returns true if the enemy is attacking
   * 
   * @return true if the enemy is attacking
   */
  public boolean isAttacking() {
    return attacking;
  }

  /**
   * Sets the attacking state
   * 
   * @param true if enemy is attacking
   */
  private void setAttacking(boolean attacking) {
    this.attacking = attacking;
  }

  /**
   * Applies a linear impulse to the body to attack
   */
  public void attack() {
    Gdx.app.log("Enemy", "Attack!");
    setAttacking(true);
    bounce(getUserData().getLinearImpulse());
  }

  /**
   * Returns true if the enemy is near the target based on the given threshold
   * 
   * @param target
   * @param threshold
   * @return
   */
  public boolean isNear(GameActor target) {
    Vector2 targetPos = target.getBodyPosition();
    UserData targetData = target.getUserData();
    Vector2 attackDistance = getUserData().getAttackDistance();

    float targetX = targetPos.x + targetData.getWidth();
    float distanceX = Math.abs(targetX - body.getPosition().x);

    return distanceX <= attackDistance.x;
  }
}
