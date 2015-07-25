/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
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
  private Level currentLevel;

  private World world;
  private OrthographicCamera pixelCamera;

  private Label win, lose;

  public PixelStage(World world) {
    super();
    this.world = world;

    pixelCamera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    pixelCamera.position.set(pixelCamera.viewportWidth / 2, pixelCamera.viewportHeight / 2, 0);
    pixelCamera.update();
    getViewport().setCamera(pixelCamera);
  }

  @Override
  public void draw() {
    super.draw();
    pixelCamera.position.x = world.camera.position.cpy().x * SCALE;
    pixelCamera.update();

    if (world.hero.isDying() && !lose.isVisible()) {
      lose();
    }
  }

  /**
   * Creates the actors in the pixel stage
   */
  public void create() {
    win = new Label("You win!", Assets.gameSkin, "instruction");
    lose = new Label("Aww :(", Assets.gameSkin, "instruction");

    win.setVisible(false);
    lose.setVisible(false);
    lose.setVisible(false);

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

    Array<Instruction> instructions = currentLevel.getInstructions();

    for (Instruction instruction : instructions) {
      Vector2 pixelPosition = new Vector2(instruction.getX(), instruction.getY()).scl(SCALE);
      instruction.setPosition(pixelPosition.x, pixelPosition.y);
      Gdx.app.log("PixelStage", String.format("Add instruction (%s) at position (%f,%f)",
          instruction.getText(), instruction.getX(), instruction.getY()));
      addActor(instruction);
    }
  }

  public void win() {
    Vector3 labelPos = world.camera.position.cpy().scl(SCALE);
    win.setPosition(labelPos.x, labelPos.y);
    win.addAction(Actions.parallel(Actions.fadeIn(1f), Actions.show()));
    Gdx.app.log(TAG, "Goal! Show at " + labelPos.x + "," + labelPos.y);
  }

  public void lose() {
    Vector3 labelPos = world.camera.position.cpy().scl(SCALE);
    lose.setPosition(labelPos.x, labelPos.y);
    lose.addAction(Actions.parallel(Actions.fadeIn(1f), Actions.show()));
  }
}
