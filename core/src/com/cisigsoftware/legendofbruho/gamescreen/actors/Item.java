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

  private static float SIZE = 0.5f;
  private static int NONE = -1;

  private Level level;
  private Hero user;
  private boolean enabled;
  private boolean pickedUp;
  private int instructionId;

  public Item(Level level, float x, float y) {
    super(x, y, SIZE, SIZE);

    setInstructionId(NONE);
    setPickedUp(false);
    setEnabled(true);
    setLevel(level);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (enabled && user != null && this.collidesWith(user) && !pickedUp) {
      pickUp();
      // then if any item was picked up during the level, show the popups after exit
    }
  }

  /**
   * Notes that the item has been picked up, shows related instructions and removes it from the
   * world
   */
  private void pickUp() {
    // Note that this item has been picked up
    setPickedUp(true);

    // Show instructions when item is picked up
    if (instructionId != NONE) {
      level.getInstruction(instructionId).addAction(
          Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(2f, Actions.fadeOut(0.5f))));
    }

    // Remove this item from the world
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
  public boolean isPickedUp() {
    return pickedUp;
  }

  /**
   * Marks that the item has been picked up
   * 
   * @param pickedUp true if the item was picked up
   */
  private void setPickedUp(boolean pickedUp) {
    this.pickedUp = pickedUp;
  }

  /**
   * @return enabled true if the item has been enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param enabled true if the item has been enabled
   */
  public void setEnabled(boolean boxed) {
    this.enabled = boxed;
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
