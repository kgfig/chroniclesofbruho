/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author kg
 *
 */
public class HUD extends Stage {

  private OrthographicCamera hudCamera;
  
  public HUD(float width, float height) {
    super();
    
    hudCamera = new OrthographicCamera(width, height);
    hudCamera.position.set(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2, 0);
    hudCamera.update();
    getViewport().setCamera(hudCamera);    
  }
  
  @Override
  public void draw() {
    super.draw();
    hudCamera.update();
  }
  
}
