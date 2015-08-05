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
public class Item extends BoundedActor {

  private static float SIZE = 1;

  private Level level;
  private Hero user;
  private boolean unlocked;
  private int instructionId;

  public Item(Level level, float x, float y) {
    super(x, y, SIZE, SIZE);
    unlocked = false;
    setLevel(level);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (user != null && user.isSlashing() && this.collidesWith(user.getMeleeWeapon())
        && !unlocked) {
      unlock();
      setUnlocked(true);
      // then if any item was unlocked during the level, show the popups after exit
    }
  }

  /**
   * Unlocks the item
   */
  private void unlock() {
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
   * @return the unlocked
   */
  public boolean isUnlocked() {
    return unlocked;
  }

  /**
   * Sets unlocked flag
   * 
   * @param unlocked true if the item is unlocked
   */
  private void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
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
