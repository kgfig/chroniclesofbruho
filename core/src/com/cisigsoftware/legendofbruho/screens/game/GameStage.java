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
import com.cisigsoftware.legendofbruho.screens.game.utils.HeroController;

/**
 * @author kg
 *
 */
public class GameStage extends Stage {

  public static final float WORLD_WIDTH = 16f;
  public static final float WORLD_HEIGHT = 9f;
  private static final long LONG_JUMP_PRESS = 200l; // cut off jump propulsion after 150ms

  private Array<Block> blocks;
  private Hero hero;

  private OrthographicCamera camera;
  private HeroController controller;
  private long jumpingPressedTime;
  private boolean jumpingPressed;


  public GameStage() {
    super();

    createActors();
    setCameraViewport();
    addActors();
    controller = new HeroController();
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

    // for (Block block : blocks) {
    // block.setDebug(true);
    // addActor(block);
    // }
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
    if (keyCode == Keys.Z) {
      controller.jumpReleased();
      jumpingPressed = false;
      Gdx.app.log("GameStage", "Jump released! jumping?" + jumpingPressed);
    }
    if (keyCode == Keys.X)
      controller.fireReleased();
    return true;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (controller.isJumpPressed()) {
      Gdx.app.log("GameStage", "Jump pressed! jumping?" + jumpingPressed);
      if (!hero.isJumping()) {
        Gdx.app.log("GameStage", "Make hero jump!");
        jumpingPressed = true;
        jumpingPressedTime = System.currentTimeMillis();
        hero.jump();
      } else {
        Gdx.app.log("GameStage", "Hero is still jumping.");
        long pressedDuration = System.currentTimeMillis() - jumpingPressedTime;

        if (jumpingPressed && pressedDuration >= LONG_JUMP_PRESS) {
          jumpingPressed = false;
        } else if (jumpingPressed) {
          hero.propelUp();
        }
      }
    }

    if (controller.isLeftPressed()) {
      if (!hero.isJumping())
        hero.walkLeft();
      else
        hero.moveLeft();
    } else if (controller.isRightPressed()) {
      if (!hero.isJumping())
        hero.walkRight();
      else
        hero.moveRight();
    } else {
      if (!hero.isJumping()) {
        hero.stand();
      }
      hero.stopWalking();
    }

  }
}
