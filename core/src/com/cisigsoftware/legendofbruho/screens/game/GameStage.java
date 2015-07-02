/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.cisigsoftware.legendofbruho.screens.game.actors.Ground;
import com.cisigsoftware.legendofbruho.screens.game.actors.Hero;
import com.cisigsoftware.legendofbruho.screens.game.actors.enums.GroundData;
import com.cisigsoftware.legendofbruho.screens.game.actors.enums.HeroData;

/**
 * @author kg
 *
 */
public class GameStage extends WorldStage {

  public static final int CAMERA_OFFSET_PX = 320;
  private Ground ground;
  private Hero hero;

  private Box2DDebugRenderer renderer;

  /**
   * Initialize world in stage
   */
  public GameStage() {
    setupWorld();
    setupCamera();

    renderer = new Box2DDebugRenderer();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  @Override
  public void draw() {
    super.draw();

    // Follow the hero by setting the camera to the position of the Hero, offset by 10m (320px) to his right
    camera.position.x = hero.getBodyX() + (CAMERA_OFFSET_PX / PPM);
    camera.update();
    renderer.render(world, camera.combined);
  }

  /**
   * Creates the world and the bodies for the game actors
   */
  private void setupWorld() {
    world = createWorld(GRAVITY);
    ground = createGround();
    hero = createHero();

    // Add actors
    addActor(ground);
    addActor(hero);
  }

  /**
   * Creates the ground with a static body
   * 
   * @return ground body
   */
  private Ground createGround() {
    Body body = createStaticBody(Ground.X, Ground.Y, Ground.WIDTH, Ground.HEIGHT, Ground.DENSITY);
    body.setUserData(new GroundData(Ground.WIDTH, Ground.HEIGHT));
    return new Ground(body);
  }

  /**
   * Creates a hero with a dynamic body
   * 
   * @return hero body
   */
  private Hero createHero() {
    Body body = createDynamicBody(Hero.X, Hero.Y, Hero.WIDTH, Hero.HEIGHT, Hero.DENSITY);
    body.setUserData(new HeroData(Hero.WIDTH, Hero.HEIGHT));
    return new Hero(body);
  }
}
