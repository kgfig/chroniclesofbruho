/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.screens.game.actors.GameActor;

/**
 * @author kg
 *
 */
public class WorldStage extends Stage {

  public static final int PPM = 32;
  public static final Vector2 GRAVITY = new Vector2(0, -10);
  public static final float DEFAULT_GRAVITY_SCALE = 1;

  // private final float TIME_STEP = 1 / 300f;
  // private float accumulator = 0f;
  public static final int VELOCITY_ITERATIONS = 8;
  public static final int POSITION_ITERATIONS = 3;

  protected World world;
  protected OrthographicCamera camera;
  protected Array<GameActor> deletionList;

  public WorldStage() {
    deletionList = new Array<GameActor>();
  }
  
  @Override
  public void act(float delta) {
    super.act(delta);
    world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

    // Process bodies and actors for deletion
    for (GameActor actor : deletionList) {
      actor.destroy();
      int index = deletionList.indexOf(actor, true);

      Gdx.app.log("WorldStage", "Delete actor at index " + index);
      deletionList.removeIndex(index);
    }
  }

  /**
   * Creates a world with the specified gravity
   * 
   * @param gravity
   * @return World
   */
  public World createWorld(Vector2 gravity) {
    return new World(gravity, true);
  }

  /**
   * Creates a camera positioned at the center of the screen
   */
  protected void setupCamera() {
    float viewportWidth = Gdx.graphics.getWidth() / PPM;
    float viewportHeight = Gdx.graphics.getHeight() / PPM;
    camera = new OrthographicCamera(viewportWidth, viewportHeight);
    camera.position.set(viewportWidth / 2, viewportHeight / 2, 0f);
    camera.update();

    System.out.println("Screen resolution: " + Gdx.graphics.getWidth() + " px by "
        + Gdx.graphics.getHeight() + " px");
    System.out
        .println("Camera viewport: " + viewportWidth + " meters by " + viewportHeight + " meters");
  }

  /**
   * Creates a static body
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   * @param density
   * @return a static body
   */
  public Body createStaticBody(float x, float y, float width, float height, float density) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(new Vector2(x, y));

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(width / 2, height / 2);

    Body body = world.createBody(bodyDef);
    body.createFixture(shape, density);
    shape.dispose();

    return body;
  }

  /**
   * Creates a dynamic body with the default gravity scale of 1
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   * @param density
   * @return a dynamic body
   */
  public Body createDynamicBody(float x, float y, float width, float height, float density) {
    return createDynamicBody(x, y, width, height, density, DEFAULT_GRAVITY_SCALE);
  }

  /**
   * Creates a dynamic body
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   * @param density
   * @return a dynamic body
   */
  public Body createDynamicBody(float x, float y, float width, float height, float density,
      float gravityScale) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(new Vector2(x, y));

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(width / 2, height / 2);

    Body body = world.createBody(bodyDef);
    body.setGravityScale(gravityScale);
    body.createFixture(shape, density);
    body.resetMassData();
    shape.dispose();

    return body;
  }

  /**
   * Creates a kinematic body
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   * @param density
   * @return
   */
  public Body createKinematicBody(float x, float y, float width, float height, float density) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.KinematicBody;
    bodyDef.position.set(new Vector2(x, y));

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(width / 2, height / 2);

    Body body = world.createBody(bodyDef);
    body.createFixture(shape, density);
    body.resetMassData();
    shape.dispose();

    return body;
  }


}
