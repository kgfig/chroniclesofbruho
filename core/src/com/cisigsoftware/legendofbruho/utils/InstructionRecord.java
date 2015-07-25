/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.utils;

/**
 * @author kg
 *
 */
public class InstructionRecord {

  public int id;
  public String text;
  public float x, y;
  public boolean autoShow;
  
  /**
   * @param text
   * @param x
   * @param y
   */
  public InstructionRecord(int id, String text, float x, float y, boolean autoShow) {
    this.id = id;
    this.text = text;
    this.x = x;
    this.y = y;
    this.autoShow = autoShow; 
  }
  
}
