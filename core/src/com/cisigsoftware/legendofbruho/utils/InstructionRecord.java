/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.utils;

/**
 * @author kg
 *
 */
public class InstructionRecord {

  public String text;
  public float x, y;
  
  /**
   * @param text
   * @param x
   * @param y
   */
  public InstructionRecord(String text, float x, float y) {
    this.text = text;
    this.x = x;
    this.y = y;
  }
  
}
