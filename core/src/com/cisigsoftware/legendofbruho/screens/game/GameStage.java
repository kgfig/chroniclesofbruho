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
import com.cisigsoftware.legendofbruho.screens.game.actors.Enemy;
import com.cisigsoftware.legendofbruho.screens.game.actors.Hero;

/**
 * @author kg
 *
 */
public class GameStage extends Stage {

  private static final String TAG = GameStage.class.getSimpleName(); 
  public static final float WORLD_WIDTH = 16f;
  public static final float WORLD_HALF = WORLD_WIDTH / 2f;
  public static final float WORLD_HEIGHT = 9f;
  private static final long LONG_JUMP_PRESS = 200l; // cut off jump propulsion after 200ms

  private OrthographicCamera camera;
  private Level level;
  private Array<Enemy> enemies;

  private Hero hero;
  private HeroController controller;
  private long jumpingPressedTime;
  private boolean jumpingPressed;

//  private Array<Rectangle> collisionBoxes;
//  private ShapeRenderer debugRenderer = new ShapeRenderer();

  public GameStage() {
    super();

    createLevel();
    createActors();
    setCameraViewport();
    setDebugAll(true);

    controller = new HeroController();
    Gdx.input.setInputProcessor(this);
  }

//  private void initDataStructures() {
//    collidable = new Array<Block>();
//    collisionBoxes = new Array<Rectangle>();
//  }

  /**
   * Creates the hero and assigns to enemies
   */
  private void createActors() {
    hero = new Hero(level, new Vector2(WORLD_WIDTH - 3, 2));
    hero.setDebug(true);
    addActor(hero);

    enemies = level.getEnemies();
    
    for (Enemy enemy : enemies) {
      enemy.setTarget(hero);
      enemy.setDebug(true);
      addActor(enemy);
    }
  }

  /**
   * Creates the current level
   */
  private void createLevel() {
    level = new Level((int) WORLD_WIDTH * 2, (int) WORLD_HEIGHT);
    
    Block[][] blocks = level.getBlocks();

    for (Block[] blockCol : blocks) {
      for (Block block : blockCol) {
        if (block != null) {
          block.setDebug(true);
          addActor(block);
        }
      }
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
    }
    if (keyCode == Keys.X)
      controller.fireReleased();
    return true;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    
    for (Enemy enemy : enemies) {
      if (enemy.getX() >= camera.position.x - WORLD_HALF && enemy.getX() <= camera.position.x + WORLD_HALF && !enemy.isStateMoving()) {
        enemy.setStateMoving();
      } else {
        enemy.setStateIdle();
      }
    }
    
    if (controller.isJumpPressed()) {
      if (!hero.isJumping()) {
        jumpingPressed = true;
        jumpingPressedTime = System.currentTimeMillis();
        hero.jump();
        hero.setGrounded(false);
      } else {
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
        hero.idle();
      }
      hero.stopWalking();
    }
  }

  @Override
  public void draw() {
    super.draw();

    // Set camera to follow the hero's movement
    if (hero.getX() + WORLD_HALF < level.getWidth() && hero.getX() - WORLD_HALF >= 0)
      camera.position.x = hero.getX();

    camera.update();

    // Draw collision boxes for debugging
//    drawCollisionBoxes();
  }

//  private void drawCollisionBoxes() {
//    debugRenderer.setProjectionMatrix(camera.combined);
//    debugRenderer.setAutoShapeType(true);
//    debugRenderer.begin();
//    debugRenderer.setColor(new Color(1, 0, 0, 1));
//    for (Rectangle rect : collisionBoxes) {
//      debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
//    }
//    debugRenderer.end();
//  }

  /**
   * Returns the blocks that are in the camera’s window and will be rendered
   * 
   * @param width
   * @param height
   */
  public void getDrawableBlocks(int width, int height) {
    int x = (int) hero.getX() - width;
    int y = (int) hero.getY() - height;

    // Clamp left x to 0
    if (x < 0)
      x = 0;

    // Clamp bottom y to 0
    if (y < 0)
      y = 0;

    int x2 = x + 2 * width;
    int y2 = y + 2 * height;

    // Clamp right x to the level width
    if (x2 >= level.getWidth()) {
      x2 = level.getWidth() - 1;
    }

    // Clamp top y to the level height
    if (y2 >= level.getHeight()) {
      y2 = level.getHeight() - 1;
    }
  }

//  /**
//   * Clears the collision boxes
//   */
//  public void clearCollisionBoxes() {
//    collisionBoxes.clear();
//  }
//
//  /**
//   * Shows block bounds to check for collision
//   * 
//   * @param blockBox
//   */
//  public void addCollisionBox(Rectangle blockBox) {
//    collisionBoxes.add(blockBox);
//  }

}
