/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.utils.Constants;

/**
 * @author kg
 *
 */
public class GameScreen implements Screen {

  private Array<Level> levels;
  public Level currentLevel;
  private int levelIndex;

  protected World world;
  protected PixelStage pixelStage;
  protected Vector3 pixelPos;


  public GameScreen() {
    world = new World();
    pixelStage = new PixelStage(world);
    pixelPos = new Vector3(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2, 0);
  }

  @Override
  public void show() {
    createLevels();
    nextLevel();

    world.create();
    pixelStage.create();

    world.setCurrentLevel(currentLevel);
    pixelStage.setCurrentLevel(currentLevel);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (currentLevel.isComplete()) {
      if (hasNextLevel()) {
        resetWorld();
      } else {
        pixelStage.win();
      }
    }

    world.act(delta);
    pixelStage.act(delta);

    world.draw();
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

  /**
   * Creates the current level
   */
  private void createLevels() {
    levelIndex = 0;
    levels = new Array<Level>();
    levels.add(new Level(Constants.DEMO_LEVEL, Constants.LVL_INSTRUCTIONS));
//    levels.add(new Level(Constants.DEMO_LEVEL1, Constants.LVL1_INSTRUCTIONS));
//    levels.add(new Level(Constants.DEMO_LEVEL2, Constants.LVL2_INSTRUCTIONS));
//    levels.add(new Level(Constants.DEMO_LEVEL3, Constants.LVL3_INSTRUCTIONS));
//    levels.add(new Level(Constants.DEMO_LEVEL4, Constants.LVL4_INSTRUCTIONS));
//    levels.add(new Level(Constants.DEMO_LEVEL5, Constants.LVL5_INSTRUCTIONS));
//    levels.add(new Level(Constants.DEMO_LEVEL5, Constants.LVL6_INSTRUCTIONS));
  }

  /**
   * Returns true if there's a pending level
   * 
   * @return true if there's a pending level
   */
  private boolean hasNextLevel() {
    return levelIndex < levels.size - 1;
  }


  /**
   * Sets the next unfinished level
   */
  private void nextLevel() {
    currentLevel = levels.get(levelIndex++);
  }

  /**
   * Removes the current level from the world and sets the next level
   */
  private void resetWorld() {
    world.clearWorld();
    pixelStage.clear();

    nextLevel();
    world.setCurrentLevel(currentLevel);
    pixelStage.setCurrentLevel(currentLevel);
    world.resetCameraPosition();
  }

}
