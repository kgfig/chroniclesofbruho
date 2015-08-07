/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.cisigsoftware.legendofbruho.utils.Assets;
import com.cisigsoftware.legendofbruho.utils.Constants;

/**
 * @author kg
 *
 */
public class LoadingScreen implements Screen {

  private static final String STYLE_NAME = "loading";
  private static final float BAR_Y = Constants.SCREEN_HEIGHT * 0.4f;
  private static final float TEXT_Y = BAR_Y + 50;
  private static final float X = Constants.SCREEN_WIDTH / 2;
  private static final float BAR_WIDTH = Constants.SCREEN_WIDTH * 0.6f;

  private int progress;

  private Stage stage;
  private Label loadingText;
  private ProgressBar progressBar;

  public LoadingScreen() {
    stage = new Stage();
    progress = 0;
    
    loadingText = new Label("Loading ... 0%", Assets.loadingSkin, STYLE_NAME);
    loadingText.setPosition(X - (loadingText.getWidth() / 2), TEXT_Y);

    progressBar = new ProgressBar(0, 100, 1, false, Assets.loadingSkin, STYLE_NAME);
    progressBar.setWidth(BAR_WIDTH);
    progressBar.setPosition(X - progressBar.getWidth() / 2, BAR_Y);
  }

  @Override
  public void show() {
    stage.addActor(loadingText);
    stage.addActor(progressBar);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act();
    stage.draw();
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

  public void setProgress(float percentDone) {
    progress = (int) (percentDone * 100);
    loadingText.setText(String.format("Loading ... %d%%", progress));
    progressBar.setValue(percentDone);
  }

}
