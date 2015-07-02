/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cisigsoftware.legendofbruho.screens.game.actors.enums.UserData;

/**
 * @author kg
 *
 */
public abstract class GameActor extends Actor {

  protected Body body;
  protected UserData actorData;
  
  public GameActor(Body body) {
    this.body = body;
    this.actorData = (UserData) body.getUserData();
  }
}
