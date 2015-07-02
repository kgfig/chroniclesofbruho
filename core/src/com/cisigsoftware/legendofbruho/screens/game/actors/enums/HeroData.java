/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors.enums;

import com.badlogic.gdx.math.Vector2;
import com.cisigsoftware.legendofbruho.screens.game.actors.Hero;

/**
 * @author kg
 *
 */
public class HeroData extends UserData {

  private Vector2 linearVelocity;

  public HeroData(float width, float height) {
    super(width, height);
    dataType = UserDataType.HERO;
    linearVelocity = Hero.LINEAR_VELOCITY;
  }

  /**
   * @return the linearVelocity
   */
  public Vector2 getLinearVelocity() {
    return linearVelocity;
  }

}
