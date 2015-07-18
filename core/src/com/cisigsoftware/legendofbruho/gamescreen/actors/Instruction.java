/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.cisigsoftware.legendofbruho.utils.Assets;

/**
 * @author kg
 *
 */
public class Instruction extends Label {

  private static final String STYLE_NAME = "instruction";

  /**
   * @param text
   * @param gameSkin
   * @param styleName
   */
  public Instruction(String text, float x, float y) {
    super(text, Assets.gameSkin, STYLE_NAME);

    setPosition(x, y);
  }

}
