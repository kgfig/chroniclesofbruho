/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.cisigsoftware.legendofbruho.screens.game.actors.Enemy;
import com.cisigsoftware.legendofbruho.screens.game.actors.Ground;
import com.cisigsoftware.legendofbruho.screens.game.actors.Hero;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.EnemyData;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.GroundData;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.HeroData;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.UserData;
import com.cisigsoftware.legendofbruho.screens.game.actors.data.UserDataType;

/**
 * @author kg
 *
 */
public class GameStage extends WorldStage implements ContactListener {

  public static final int CAMERA_OFFSET_PX = 320;
  private Ground ground;
  private Hero hero;
  private Enemy enemy;

  private Box2DDebugRenderer renderer;

  /**
   * Initialize world in stage
   */
  public GameStage() {
    super();
    setupWorld();
    setupCamera();

    renderer = new Box2DDebugRenderer();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    Vector2 enemyPos = enemy.getBodyPosition();
    Vector2 heroPos = hero.getBodyPosition();
    Gdx.app.log("GameStage", String.format("Hero(x)=(%2f)", heroPos.x) + "\t" + String.format("Enemy(x,y)=(%2f,%2f)", enemyPos.x, enemyPos.y));

    if (enemy.isNear(hero) && !enemy.isAttacking() && !enemy.isBouncing()) {
      Gdx.app.log("GameStage", "Hero is near. bouncing? " + enemy.isBouncing());
      enemy.attack();
    }
    
  }

  @Override
  public void draw() {
    super.draw();

    // Follow the hero by setting the camera to the position of the Hero, offset by 10m (320px) to
    // his right
    camera.position.x = hero.getBodyPosition().x + (CAMERA_OFFSET_PX / PPM);
    camera.update();

//    int numContacts = world.getContactCount();
//    if (numContacts > 0) {
//      Gdx.app.log("contact", "start of contact list");
//      for (Contact contact : world.getContactList()) {
//        UserData userDataA = (UserData) contact.getFixtureA().getBody().getUserData();
//        UserData userDataB = (UserData) contact.getFixtureB().getBody().getUserData();
//        Gdx.app.log("contact",
//            "between " + userDataA.getDataType() + " and " + userDataB.getDataType());
//      }
//      Gdx.app.log("contact", "end of contact list");
//    }

    renderer.render(world, camera.combined);
  }
  
  @Override
  public void beginContact(Contact contact) {
    Gdx.app.log("ContactListener", "Contact!");
    Body a = contact.getFixtureA().getBody();
    Body b = contact.getFixtureB().getBody();

    // TODO get the actual enemy actor before checking for bouncing and deletion
    if (enemy.isBouncing() && enemyOnGround(a, b)) {
      Gdx.app.log("ContactListener", "Enemy landed!");
      enemy.landed();
    }

    if (enemyHitsHero(a,b) && !hero.isHit()) {
      Gdx.app.log("ContactListener", "Hero is hit!");
      hero.hit();
      Gdx.app.log("ContactListener", "Remove enemy from world");
      deletionList.add(enemy);
    }
  }

  @Override
  public void endContact(Contact contact) {

  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }

  /**
   * Creates the world and the bodies for the game actors
   */
  private void setupWorld() {
    world = createWorld(GRAVITY);

    world.setContactListener(this);
    ground = createGround();
    hero = createHero();
    enemy = createEnemy();

    // Add actors
    addActor(ground);
    addActor(hero);
    addActor(enemy);
  }

  /**
   * Creates the ground with a static body
   * 
   * @return ground actor
   */
  private Ground createGround() {
    Body body = createStaticBody(Ground.X, Ground.Y, Ground.WIDTH, Ground.HEIGHT, Ground.DENSITY);
    body.setUserData(new GroundData(Ground.WIDTH, Ground.HEIGHT));
    return new Ground(body);
  }

  /**
   * Creates a hero with a dynamic body
   * 
   * @return hero actor
   */
  private Hero createHero() {
    Body body = createDynamicBody(Hero.X, Hero.Y, Hero.WIDTH, Hero.HEIGHT, Hero.DENSITY);
    body.setUserData(new HeroData(Hero.WIDTH, Hero.HEIGHT));
    return new Hero(body);
  }

  /**
   * Creates an enemy with a kinematic body
   * 
   * @return enemy actor
   */
  private Enemy createEnemy() {
    Body body = createDynamicBody(Enemy.X, Enemy.Y, Enemy.WIDTH, Enemy.HEIGHT, Enemy.DENSITY);
    body.setUserData(new EnemyData(Enemy.WIDTH, Enemy.HEIGHT));
    return new Enemy(body);
  }
  
  /**
   * Returns true if the bodies in contact are that of the enemy and the ground
   * 
   * @param a
   * @param b
   * @return true if the bodies in contact are that of the enemy and the ground
   */
  public static boolean enemyOnGround(Body a, Body b) {
    boolean cond1 = (isGround(a) && isEnemy(b));
    Gdx.app.log("ContactHandler", "cond1=" + cond1);

    boolean cond2 = (isEnemy(a) && isGround(b));
    Gdx.app.log("ContactHandler", "cond2=" + cond2);

    return (cond1 || cond2);
  }
  
  /**
   * Returns true if the bodies in contact are that of the enemy and the hero
   * @param a
   * @param b
   * @return true if the bodies in contact are that of the enemy and the hero
   */
  public static boolean enemyHitsHero(Body a, Body b) {
    boolean cond1 = (isHero(a) && isEnemy(b));
    Gdx.app.log("ContactHandler", "cond1=" + cond1);
    
    boolean cond2 = (isEnemy(a) && isHero(b));
    Gdx.app.log("ContactHandler", "cond2=" + cond2);
    
    return (cond1 || cond2);
  }

  /**
   * Returns true if the user data type given is of
   * 
   * @param body the body
   */
  public static boolean isGround(Body body) {
    UserData userData = (UserData) body.getUserData();
    Gdx.app.log("GameStage", "Is ground? " + userData.getDataType());
    return userData.getDataType() == UserDataType.GROUND;
  }

  /**
   * Returns true if the user data type given is of
   * 
   * @param body the body
   */
  public static boolean isHero(Body body) {
    UserData userData = (UserData) body.getUserData();
    Gdx.app.log("GameStage", "Is hero? " + userData.getDataType());
    return userData.getDataType() == UserDataType.HERO;
  }

  /**
   * Returns true if the user data type given is of
   * 
   * @param body the body
   */
  public static boolean isEnemy(Body body) {
    UserData userData = (UserData) body.getUserData();
    Gdx.app.log("GameStage", "Is enemy? " + userData.getDataType());
    return userData.getDataType() == UserDataType.ENEMY;
  }

  /**
   * Returns the enemy game actor
   * 
   * @return the enemy
   */
  public Enemy getEnemy() {
    return enemy;
  }

}
