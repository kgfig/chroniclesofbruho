/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors.data;

import com.badlogic.gdx.math.Vector2;
import com.cisigsoftware.legendofbruho.screens.game.actors.Enemy;

/**
 * @author kg
 *
 */
public class EnemyData extends UserData {

  private Vector2 readyLinearImpulse;
  private Vector2 attackLinearImpulse;
  private Vector2 attackDistance;
  
  public EnemyData(float width, float height) {
    super(width, height);
    dataType = UserDataType.ENEMY;
    readyLinearImpulse = Enemy.READY_LINEAR_IMPULSE;
    attackLinearImpulse = Enemy.ATTACK_LINEAR_IMPULSE;
    attackDistance = Enemy.ATTACK_DISTANCE;
  }
  
  /**
   * @return the readyLinearImpulse
   */
  public Vector2 getReadyLinearImpulse() {
    return readyLinearImpulse;
  }

  /**
   * Returns the bouncing / bumping impulse of the enemy
   * @return the linearImpulse
   */
  public Vector2 getLinearImpulse() {
    return attackLinearImpulse;
  }

  /**
   * Returns the distance at which the enemy will start attacking
   * @return the attackDistance
   */
  public Vector2 getAttackDistance() {
    return attackDistance;
  }

}
