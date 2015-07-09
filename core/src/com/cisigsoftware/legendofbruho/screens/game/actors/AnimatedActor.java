/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.cisigsoftware.legendofbruho.screens.game.actors.Hero.State;

/**
 * @author kg
 *
 */
public class AnimatedActor extends GameActor {

  private float stateTime = 0;
  private Map<State, Animation> animationStates;

  public AnimatedActor() {
    this(0, 0, 0, 0);
  }

  public AnimatedActor(float x, float y, float width, float height) {
    super(x, y, width, height);
    
    stateTime = 0;
    animationStates = new HashMap<State,Animation>();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    stateTime += delta;
  }
}
