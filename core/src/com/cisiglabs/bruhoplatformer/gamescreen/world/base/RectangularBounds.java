/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world.base;

/**
 * @author kg
 *
 */
public class RectangularBounds extends Bounds {

  private float width, height;

  /**
   * Constructor for a rectangular bounds with origin at (0,0)
   * 
   * @param x the bottom-left x-coordinate
   * @param y the bottom-left y-coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public RectangularBounds(float x, float y, float width, float height) {
    this(x, y, width, height, 0, 0);
  }

  /**
   * Constructor for a rectangular bounds with origin at the specified origin
   * 
   * @param x the bottom-left x-coordinate
   * @param y the bottom-left y-coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param originX the origin along the x-axis
   * @param originY the origin along the y-axis
   */
  public RectangularBounds(float x, float y, float width, float height, float originX,
      float originY) {
    super(new float[] {0, 0, 0, height, width, height, width, 0, 0, 0});
    setPosition(x, y);
    setOrigin(originX, originY);
    this.width = width;
    this.height = height;
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
