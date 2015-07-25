/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.cisigsoftware.legendofbruho.utils.Assets;
import com.cisigsoftware.legendofbruho.utils.InstructionRecord;

/**
 * @author kg
 *
 */
public class Instruction extends Label {

  private static final String STYLE_NAME = "instruction";
  private InstructionRecord model;
  public boolean shown;

  /**
   * @param text
   * @param gameSkin
   * @param styleName
   */
  public Instruction(InstructionRecord model) {
    super(model.text, Assets.gameSkin, STYLE_NAME);
    this.model = model;
    this.shown = false;
    
    setPosition(model.x, model.y);
  }

  /**
   * Returns true if the instruction will be automatically displayed it is within the camera viewport
   * @return true if the instruction will be automatically displayed
   */
  public boolean autoShow() {
    return model.autoShow;
  }
}
