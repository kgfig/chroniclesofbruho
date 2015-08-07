/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cisiglabs.bruhoplatformer.gamescreen.World;

/**
 * @author kg
 *
 */
public class RangeWeapon extends Weapon {

  private static final String TAG = RangeWeapon.class.getSimpleName();
  
  private static final int DAMAGE = 5;

  private enum State {
    IDLE, DRAWN
  }

  private static float WIDTH = 1f;
  private static float HEIGHT = 0.25f;
  private static float DRAWN_ANGLE = 0;

  private State state;
  private boolean firing;
  private float fireDelay;
  private World world;
  private Hero hero;

  /*
   * You can use a delay mechanism by having a variable which counts down the time and every time it
   * hits 0, you make one shot and reset the time, for example to 0.2f when you want the player to
   * shoot every 0.2s:
   */

  public RangeWeapon(Hero hero, float x, float y) {
    super(Type.RANGE, x, y, WIDTH, HEIGHT);
    setOrigin(0, 0);

    this.hero = hero;
    stopFiring();
    cover();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (firing && world != null) {
      fireDelay -= delta;

      if (fireDelay <= 0) {
        Gdx.app.log(TAG, "Fire a bullet");
        Bullet bullet = new Bullet(hero.getLevel(), getX() + getWidth(), getY(), DAMAGE);
        bullet.setDebug(true);
        bullet.setLevel(world.getCurrentLevel());
        world.addActor(bullet);
        fireDelay += 0.07f;
      }
    }
  }

  @Override
  public void use() {
    Gdx.app.log(TAG, "Use weapon!");

    if (isDrawn()) {
      Gdx.app.log(TAG, "Start firing!");
      fire();
    }

  }

  /**
   * @return the state of the weapon
   */
  public State getState() {
    return state;
  }

  /**
   * @param state the state of the range weapon
   */
  private void setState(State state) {
    this.state = state;
  }

  /**
   * Returns true if the range weapon is idle
   * 
   * @return true if the range weapon is idle
   */
  public boolean isIdle() {
    return getState() == State.IDLE;
  }

  /**
   * Returns true if the range weapon is drawn
   * 
   * @return true if the range weapon is drawn
   */
  public boolean isDrawn() {
    return getState() == State.DRAWN;
  }

  public void setFiring(boolean firing) {
    this.firing = firing;
  }

  /**
   * Draws the weapon from the sheath
   */
  public void draw() {
    setState(State.DRAWN);
    addAction(Actions.sequence(Actions.show(), Actions.rotateTo(DRAWN_ANGLE, 0.25f)));
  }

  /**
   * Moves the weapon for a slash attack
   */
  public void fire() {
    setFiring(true);
  }

  /**
   * Moves the weapon for a slash attack
   */
  public void stopFiring() {
    setFiring(false);
  }

  public void cover() {
    Gdx.app.log(TAG, "Cover RANGE weapon");
    setState(State.IDLE);
    addAction(Actions.sequence(Actions.rotateTo(-90, 0.1f), Actions.hide(), Actions.run(new Runnable() {
      public void run() {
        hero.setWeaponReady();
        Gdx.app.log(TAG, "Hero done switching weapons!");
      }
    })));
  }

  /**
   * @param world
   */
  public void setWorld(World world) {
    this.world = world;
  }
  
}
