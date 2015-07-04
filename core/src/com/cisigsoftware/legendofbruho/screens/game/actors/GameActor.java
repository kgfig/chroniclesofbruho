/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.UserData;

/**
 * @author kg
 *
 */
public abstract class GameActor extends Actor {

  protected Body body;
  protected UserData userData;
  
  public GameActor(Body body) {
    this.body = body;
    this.userData = (UserData) body.getUserData();
  }

  /**
   * Override this method to get the specific user data instance
   * @return the user data of the body
   */
  public abstract UserData getUserData();

  /**
   * Returns the position of the body
   * @return position of the body
   */
  public Vector2 getBodyPosition() {
    return body.getPosition();
  }
  
  /**
   * Removes the body from the world
   */
  public void destroy() {
    Gdx.app.log("GameActor", "Destroy body of type " + userData.getDataType());
    body.getWorld().destroyBody(body);
  }
}
