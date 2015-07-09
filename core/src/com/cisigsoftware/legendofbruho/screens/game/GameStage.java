/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.screens.game.actors.Block;
import com.cisigsoftware.legendofbruho.screens.game.actors.Hero;

/**
 * @author kg
 *
 */
public class GameStage extends Stage {

  static final float WORLD_WIDTH = 16f;
  static final float WORLD_HEIGHT = 9f;

  private Array<Block> blocks;
  private Hero hero;
  private GameController controller;
  private OrthographicCamera camera;

  public GameStage() {
    super();

    createActors();
    setCameraViewport();
    addActors();
    controller = new GameController();
    Gdx.input.setInputProcessor(this);
  }

  /**
   * Create the hero and world blocks
   */
  private void createActors() {
    // create our hero
    hero = new Hero(new Vector2(WORLD_WIDTH - 3, 2));

    // create the terrain
    blocks = new Array<Block>();
    int rightMostX = (int) WORLD_WIDTH - 1;
    int middleX = (int) WORLD_WIDTH / 2;

    // build the ground and ceiling
    for (int i = 0; i < (int) WORLD_WIDTH; i++) {
      blocks.add(new Block(new Vector2(i, 0)));
      blocks.add(new Block(new Vector2(i, (int) WORLD_HEIGHT - 1)));
      if (i > 2)
        blocks.add(new Block(new Vector2(i, 1)));
    }

    // build the right wall
    for (int i = 0; i < (int) WORLD_HEIGHT - 1; i++) {
      blocks.add(new Block(new Vector2(rightMostX, i)));
    }

    // build middle wall with passage at the bottom
    for (int i = 3; i < (int) WORLD_HEIGHT - 1; i++) {
      blocks.add(new Block(new Vector2(middleX, i)));
    }
  }

  /**
   * Set the camera viewport
   */
  private void setCameraViewport() {
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    camera.update();
    getViewport().setCamera(camera);
  }

  /**
   * Add the world and hero to the stage
   */
  private void addActors() {
    hero.setDebug(true);
    addActor(hero);

    for (Block block : blocks) {
      block.setDebug(true);
      addActor(block);
    }
  }

  @Override
  public boolean keyDown(int keyCode) {
    if (keyCode == Keys.DPAD_LEFT)
      controller.leftPressed();
    if (keyCode == Keys.DPAD_RIGHT)
      controller.rightPressed();
    if (keyCode == Keys.Z)
      controller.jumpPressed();
    if (keyCode == Keys.X)
      controller.firePressed();
    return true;

  }

  @Override
  public boolean keyUp(int keyCode) {
    if (keyCode == Keys.DPAD_LEFT)
      controller.leftReleased();
    if (keyCode == Keys.DPAD_RIGHT)
      controller.rightReleased();
    if (keyCode == Keys.Z)
      controller.jumpReleased();
    if (keyCode == Keys.X)
      controller.fireReleased();
    return true;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    
    if (controller.isLeftPressed()) {
      hero.walkLeft();
    }

    if (controller.isRightPressed()) {
      hero.walkRight();
    }

    if ((controller.isLeftPressed() && controller.isRightPressed())
        || (!controller.isLeftPressed() && !controller.isRightPressed())) {
      hero.stand();
    }
  }
}
