/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors.enums;

/**
 * @author kg
 *
 */
public abstract class UserData {
  
  protected UserDataType dataType;
  protected float width;
  protected float height;
  
  public UserData() {
    
  }
  
  public UserData(float width, float height) {
    this.width = width;
    this.height = height;
  }

  /**
   * @return the userDataType
   */
  public UserDataType getDataType() {
    return dataType;
  }

  /**
   * @return the width
   */
  public float getWidth() {
    return width;
  }

  /**
   * @param width the width to set
   */
  public void setWidth(float width) {
    this.width = width;
  }

  /**
   * @return the height
   */
  public float getHeight() {
    return height;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(float height) {
    this.height = height;
  }
  
}
