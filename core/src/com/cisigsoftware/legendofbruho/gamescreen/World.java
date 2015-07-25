/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Block;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Enemy;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Hero;

/**
 * @author kg
 *
 */
public class World extends Stage {

  private static final String TAG = World.class.getSimpleName();

  public static final float WORLD_WIDTH = 16f;
  public static final float WORLD_HEIGHT = 9f;
  public static final float WORLD_HALF = WORLD_WIDTH / 2f;

  private static final long LONG_JUMP_PRESS = 200l; // cut off jump propulsion after 200ms

  OrthographicCamera camera;
  Level currentLevel;
  Hero hero;
  private Array<Enemy> enemies;
  private Block goal;

  private Controls controller;
  private long jumpingPressedTime;
  private boolean jumpingPressed, attackPressed;
  private ShapeRenderer shapeRenderer;

  public World() {
    super();
    createCamera();
    shapeRenderer = new ShapeRenderer();
  }

  public void create() {
    createHero();
    setDebugAll(true);

    controller = new Controls();
    Gdx.input.setInputProcessor(this);

    Gdx.app.log(TAG, "Created World.");
  }

  /**
   * Creates the hero and assigns to enemies
   */
  private void createHero() {
    hero = new Hero(new Vector2(1, WORLD_HEIGHT - 1));
    hero.setDebug(true);
  }

  /**
   * Sets the current level for the game actors and builds the level terrain
   */
  public void setCurrentLevel(Level newLevel) {
    currentLevel = newLevel;
    goal = currentLevel.getGoal();

    // Add the enemies
    enemies = currentLevel.getEnemies();
    for (Enemy enemy : enemies) {
      enemy.setTarget(hero);
      enemy.setDebug(true);
      addActor(enemy);
    }

    // Build the terrain
    Block[][] blocks = currentLevel.getBlocks();

    for (Block[] blockCol : blocks) {
      for (Block block : blockCol) {
        if (block != null) {
          block.setDebug(true);
          addActor(block);
        }
      }
    }

    // Set the level for the hero
    hero.setLevel(currentLevel);
    hero.setPosition(1, WORLD_HEIGHT - 1);
    addActor(hero);
    addActor(hero.getMeleeWeapon());
  }

  public void clearWorld() {
    hero.remove();
    currentLevel.remove();
    enemies.clear();
  }

  public void resetCameraPosition() {
    camera.position.x = WORLD_HALF;
  }

  /**
   * Set the camera viewport
   */
  private void createCamera() {
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
    if (keyCode == Keys.S)
      controller.jumpPressed();
    if (keyCode == Keys.A)
      controller.attackedPressed();
    return true;

  }

  @Override
  public boolean keyUp(int keyCode) {
    if (keyCode == Keys.DPAD_LEFT)
      controller.leftReleased();
    if (keyCode == Keys.DPAD_RIGHT)
      controller.rightReleased();
    if (keyCode == Keys.S) {
      controller.jumpReleased();
      jumpingPressed = false;
    }
    if (keyCode == Keys.A) {
      controller.attackedReleased();
      attackPressed = false;
    }
    return true;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    // Show enemies on standby when they are within the camera's viewport
    for (Enemy enemy : enemies) {
      if (enemy.getX() >= camera.position.x - WORLD_HALF
          && enemy.getX() <= camera.position.x + WORLD_HALF) {
        if (enemy.isStateIdle()) {
          enemy.setStateMoving();
        }
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

    if (controller.isAttackPressed()) {
      Gdx.app.log(TAG, "Pressed attack");
      if (!hero.isAttacking()) {
        Gdx.app.log(TAG, "Hero attacks!");
        hero.attack();
        attackPressed = true;
      } else if (attackPressed) {
        attackPressed = false;
      }
    }
  }

  @Override
  public void draw() {
    super.draw();

    // Set camera to follow the hero's movement
    if (hero.getX() + WORLD_HALF < currentLevel.getWidth() && hero.getX() - WORLD_HALF >= 0)
      camera.position.x = hero.getX();

    camera.update();

    // Check if bounds is correctly updated according to the actor's movement
    shapeRenderer.setProjectionMatrix(camera.combined);

    if (hero.hasHp()) {
      shapeRenderer.begin(ShapeType.Line);
      shapeRenderer.setColor(1, 1, 0, 1);
      shapeRenderer.polyline(hero.getBounds().getTransformedVertices());
      shapeRenderer.end();

      shapeRenderer.begin(ShapeType.Line);
      shapeRenderer.setColor(1, 1, 0, 1);
      shapeRenderer.polyline(hero.getMeleeWeapon().getBounds().getTransformedVertices());
      shapeRenderer.end();
    }

    if (!currentLevel.isComplete()) {
      for (Enemy enemy : enemies) {
        if (enemy.hasHp()) {
          shapeRenderer.begin(ShapeType.Line);
          shapeRenderer.setColor(1, 0, 0, 1);
          shapeRenderer.polyline(enemy.getBounds().getTransformedVertices());
          shapeRenderer.end();
        }
      }
    }

    shapeRenderer.begin(ShapeType.Line);
    shapeRenderer.setColor(0, 0, 1, 1);
    shapeRenderer.polyline(goal.getBounds().getTransformedVertices());
    shapeRenderer.end();

    // for (Block[] blockRow : currentLevel.getBlocks()) {
    // for (Block block : blockRow) {
    // if (block != null) {
    // shapeRenderer.begin(ShapeType.Line);
    // shapeRenderer.setColor(0, 0, 1, 1);
    // shapeRenderer.polyline(block.getBounds().getTransformedVertices());
    // shapeRenderer.end();
    // }
    // }
    // }
  }

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
    if (x2 >= currentLevel.getWidth()) {
      x2 = currentLevel.getWidth() - 1;
    }

    // Clamp top y to the level height
    if (y2 >= currentLevel.getHeight()) {
      y2 = currentLevel.getHeight() - 1;
    }
  }

  /**
   * @return the currentLevel
   */
  public Level getCurrentLevel() {
    return currentLevel;
  }

}
