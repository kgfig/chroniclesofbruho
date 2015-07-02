/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cisigsoftware.legendofbruho.screens.game.actors.enums.HeroData;

/**
 * @author kg
 *
 */
public class Hero extends GameActor {

  public static final float WIDTH = 3f;
  public static final float X = 0;
  public static final float HEIGHT = 7f;
  public static final float Y = Ground.Y + Ground.HEIGHT;
  public static final float DENSITY = 4f;
  public static final Vector2 LINEAR_VELOCITY = new Vector2(3, 0);

  public Hero(Body body) {
    super(body);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    HeroData heroData = (HeroData) actorData;
    body.setLinearVelocity(heroData.getLinearVelocity());
  }

  /**
   * Returns position of the body in the x-axis
   * 
   * @return
   */
  public float getBodyX() {
    return body.getPosition().x;
  }
}
