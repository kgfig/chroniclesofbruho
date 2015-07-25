/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Instruction;
import com.cisigsoftware.legendofbruho.utils.Assets;
import com.cisigsoftware.legendofbruho.utils.Constants;

/**
 * @author kg
 *
 */
public class PixelStage extends Stage {

  private static final String TAG = PixelStage.class.getSimpleName();

  public static float SCALE = 80f;
  private static float WIDTH = Constants.SCREEN_WIDTH;
  private static float HEIGHT = Constants.SCREEN_HEIGHT;

  private Level currentLevel;
  private Collection<Instruction> instructions;

  private World world;
  private OrthographicCamera pixelCamera;

  public boolean ended;
  private Label win, lose;

  public PixelStage(World world) {
    super();
    this.world = world;
    this.ended = false;

    pixelCamera = new OrthographicCamera(WIDTH, HEIGHT);
    pixelCamera.position.set(pixelCamera.viewportWidth / 2, pixelCamera.viewportHeight / 2, 0);
    pixelCamera.update();
    getViewport().setCamera(pixelCamera);
  }

  @Override
  public void draw() {
    super.draw();

    // Update pixel camera position based on the world camera
    pixelCamera.position.x = world.camera.position.cpy().x * SCALE;
    pixelCamera.update();

    // Show auto-show instructions that are inside the viewport
    for (Instruction instruction : instructions) {
      if (instruction.getX() >= pixelCamera.position.x - WIDTH / 2
          && instruction.getX() <= pixelCamera.position.x + WIDTH / 2) {
        if (instruction.autoShow() && !instruction.shown) {
          instruction.addAction(Actions.fadeIn(0.5f));
          instruction.shown = true;
        }
      }
    }

    if (world.hero.isDying() && !lose.isVisible()) {
      lose();
    }
  }

  /**
   * Creates the actors in the pixel stage
   */
  public void create() {
    win = new Label("Yey!", Assets.gameSkin, "instruction");
    lose = new Label("Aww :(", Assets.gameSkin, "instruction");

    win.addAction(Actions.fadeOut(0));
    lose.addAction(Actions.fadeOut(0));

    addActor(win);
    addActor(lose);
  }

  /**
   * Sets the current level and its Actors to the pixel-based stage
   * 
   * @param newLevel
   */
  public void setCurrentLevel(Level newLevel) {
    this.currentLevel = newLevel;
    this.instructions = currentLevel.getInstructions();

    for (Instruction instruction : instructions) {
      Vector2 pixelPosition = new Vector2(instruction.getX(), instruction.getY()).scl(SCALE);
      instruction.setPosition(pixelPosition.x, pixelPosition.y);
      Gdx.app.log(TAG, String.format("Add instruction (%s) at position (%f,%f)",
          instruction.getText(), instruction.getX(), instruction.getY()));
      addActor(instruction);
      instruction.addAction(Actions.fadeOut(0));
    }
  }

  public void win() {
    ended = true;
    Vector3 labelPos = pixelCamera.position;
    win.setPosition(labelPos.x, labelPos.y);
    win.addAction(Actions.fadeIn(0.5f));
    Gdx.app.log(TAG, "Goal! Show at " + labelPos.x + "," + labelPos.y);
  }

  public void lose() {
    ended = true;
    Vector3 labelPos = pixelCamera.position;
    lose.setPosition(labelPos.x, labelPos.y);
    lose.addAction(Actions.fadeIn(0.5f));
  }
}
