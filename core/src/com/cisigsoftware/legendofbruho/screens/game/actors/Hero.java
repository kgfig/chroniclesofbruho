/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.HeroData;

/**
 * @author kg
 *
 */
public class Hero extends GameActor {

  public static final float X = 0;
  public static final float Y = Ground.Y + Ground.HEIGHT;
  public static final float WIDTH = 3f;
  public static final float HEIGHT = 7f;
  public static final float DENSITY = 3f;
  public static final float GRAVITY_SCALE = 3f;
  public static final Vector2 LINEAR_VELOCITY = new Vector2(3, 0);
  
  private boolean hit;

  public Hero(Body body) {
    super(body);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    HeroData heroData = (HeroData) userData;
    body.setLinearVelocity(heroData.getLinearVelocity());
  }

  @Override
  public HeroData getUserData() {
    return (HeroData) userData;
  }

  /**
   * Returns true if the hero was hit
   * @return the hit
   */
  public boolean isHit() {
    return hit;
  }

  /**
   * Sets the flag that hero was hit
   * @param true if hero was hit
   */
  public void setHit(boolean hit) {
    this.hit = hit;
  }
  
  /**
   * Shows that the hero was hit
   */
  public void hit() {
    setHit(true);
  }
}
