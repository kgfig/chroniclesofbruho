/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author kg
 *
 */
public class GameScreen implements Screen {

  protected World world;
  protected Stage pixelStage;

  public GameScreen() {
    world = new World();
    pixelStage = new Stage();
  }

  @Override
  public void show() {
    world.create();
    
//    Array<Instruction> instructions = world.currentLevel.getInstructions();
//    Vector3 screenPos = world.camera.project(new Vector3(3, 5, 0));
//    instructions.get(0).setPosition(screenPos.x, screenPos.y);
//    pixelStage.addActor(instructions.get(0));
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

    world.act(delta);
    world.draw();
    
    pixelStage.act(delta);
    pixelStage.draw();
  }

  @Override
  public void resize(int width, int height) {}

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
