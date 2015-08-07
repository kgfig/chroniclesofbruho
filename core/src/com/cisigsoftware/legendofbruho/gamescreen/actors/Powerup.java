/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.base.BoundedActor;

/**
 * @author kg
 *
 */
public class Powerup extends BoundedActor {

  private static float SIZE = 1;

  private Level level;
  private Hero user;
  private boolean activated;
  private int instructionId;

  public Powerup(Level level, float x, float y) {
    super(x, y, SIZE, SIZE);
    activated = false;
    setLevel(level);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (user != null && user.isSlashing() && this.collidesWith(user) && !activated) {
      activate();
      setActivated(true);
      // then if any item was unlocked during the level, show the popups after exit
    }
  }

  /**
   * Unlocks the item
   */
  private void activate() {
    level.getInstruction(instructionId).addAction(
        Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(2f, Actions.fadeOut(0.5f))));
    level.removeCollidable((int) getX(), (int) getY());
    remove();
  }

  /**
   * Sets the actor who will use the item
   * 
   * @param user the hero who will use the item
   */
  public void setUser(Hero user) {
    this.user = user;
  }

  /**
   * Sets unlocked flag
   * 
   * @param activated true if the powerup is activated
   */
  private void setActivated(boolean activated) {
    this.activated = activated;
  }

  /**
   * @return the instructionId
   */
  public int getInstructionId() {
    return instructionId;
  }

  /**
   * @param instructionId the instructionId to be displayed on collision
   */
  public void setInstructionId(int instructionId) {
    this.instructionId = instructionId;
  }

  /**
   * @return the level
   */
  public Level getLevel() {
    return level;
  }

  /**
   * @param level the level to set
   */
  public void setLevel(Level level) {
    this.level = level;
  }


}
