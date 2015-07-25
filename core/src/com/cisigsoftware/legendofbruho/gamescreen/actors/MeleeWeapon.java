/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cisigsoftware.legendofbruho.gamescreen.actors.base.Weapon;

/**
 * @author kg
 *
 */
public class MeleeWeapon extends Weapon {

  private static final String TAG = MeleeWeapon.class.getSimpleName();
  
  private enum State {
    DRAWN, SWINGING
  }

  private static float WIDTH = 1f;
  private static float HEIGHT = 0.25f;
  private static float DRAWN_ANGLE = 30;

  private State state;

  public MeleeWeapon(float x, float y) {
    setPosition(x, y);
    setSize(WIDTH, HEIGHT);

    setState(State.DRAWN);
    draw();
  }
  
  @Override
  public void act(float delta) {
    super.act(delta);

    if (isSwinging() && getRotation() == DRAWN_ANGLE) {
      setState(State.DRAWN);
      Gdx.app.log(TAG, "Set drawn!");
    }
  }

  @Override
  public void use() {
    Gdx.app.log(TAG, "Use weapon!");
    
    if (isDrawn()) {
      swing();
      setState(State.SWINGING);
      Gdx.app.log(TAG, "Weapon swinging!");
    }
  }

  /**
   * @return the state
   */
  private State getState() {
    return state;
  }

  /**
   * @param state the state of the weapon
   */
  public void setState(State state) {
    this.state = state;
  }

  /**
   * Returns true if the melee weapon is drawn
   * 
   * @return true if the melee weapon is drawn
   */
  public boolean isDrawn() {
    return getState() == State.DRAWN;
  }

  /**
   * Returns true if the melee weapon is swinging
   * 
   * @return true if the melee weapon is swinging
   */
  public boolean isSwinging() {
    return getState() == State.SWINGING;
  }

  /**
   * Draws the weapon from the sheath
   */
  public void draw() {
    rotateBy(DRAWN_ANGLE);
  }

  /**
   * Moves the weapon for a slash attack
   */
  public void swing() {
    addAction(Actions.sequence(Actions.rotateTo(-30, 0.25f), Actions.rotateTo(30, 0.25f)));
  }

}
