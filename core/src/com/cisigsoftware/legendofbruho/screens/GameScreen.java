/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.cisigsoftware.legendofbruho.screens.game.GameStage;

/**
 * @author kg
 *
 */
public class GameScreen implements Screen {

  protected GameStage stage;
  
  public GameScreen() {
    stage = new GameStage();
  }
  
  @Override
  public void show() {
    
  }

  @Override
  public void render(float delta) {
     Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
     
     stage.act(delta);
     stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    
  }

  @Override
  public void pause() {
    
  }

  @Override
  public void resume() {
    
  }

  @Override
  public void hide() {
    
  }

  @Override
  public void dispose() {
    
  }

  
}
