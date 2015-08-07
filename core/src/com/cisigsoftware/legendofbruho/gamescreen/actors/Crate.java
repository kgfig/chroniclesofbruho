/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.base.BoundedActor;

/**
 * @author kg
 *
 */
public class Crate extends BoundedActor {

  private static float SIZE = 1;

  private boolean open;
  private Array<Item> boxedItems;
  
  private Level level;
  private Hero user;

  public Crate(Level level, Array<Item> boxedItems, float x, float y) {
    super(x, y, SIZE, SIZE);
    
    this.boxedItems = boxedItems;
    for (Item boxedItem : boxedItems) {
      boxedItem.setEnabled(false);
    }
    
    setOpen(false);
    setLevel(level);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    // Open the crate when the user slashes it with his weapon
    if (user != null && user.isSlashing() && this.collidesWith(user.getMeleeWeapon()) && !open) {
      open();
    }
  }

  /**
   * Opens the crate
   */
  public void open() {
    // Note that the crate has been opened
    setOpen(true);
    
    // Show and enable items boxed in the crate
    for (Item boxedItem : boxedItems) {
      boxedItem.setEnabled(true);
    }
    
    // Remove this from the collidables in the level
    level.removeCollidable((int) getX(), (int) getY());
    remove();
  }

  /**
   * Sets the actor who can interact with the crate
   * 
   * @param user the hero who can interact with the crate
   */
  public void setUser(Hero user) {
    this.user = user;
  }

  /**
   * @return open true if the crate has been opened
   */
  public boolean isOpen() {
    return open;
  }

  /**
   * Marks the crate open
   * 
   * @param open true if the item is unlocked
   */
  private void setOpen(boolean open) {
    this.open = open;
  }

  /**
   * @return the level this crate is part of
   */
  public Level getLevel() {
    return level;
  }

  /**
   * @param level the level this crate is part of
   */
  public void setLevel(Level level) {
    this.level = level;
  }

}
