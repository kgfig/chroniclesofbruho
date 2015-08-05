/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes;

import com.badlogic.gdx.Gdx;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Enemy;

/**
 * @author kg
 *
 */
public class CrawlingEnemy extends Enemy {

  private static final String TAG = CrawlingEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 0.5f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 5;
  private static final float VX = 1f;

  private boolean crawling;

  public CrawlingEnemy(Level level, float x, float y) {
    super(Type.CRAWLING, x, y, SIZE, SIZE);

    setNearThreshold(0);
    setGrounded(false);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);
    setState(State.IDLE);
    setAttacked(false);
    setCrawling(false);
    setFacingLeft(true);
    setOriginX(SIZE / 2);

    Gdx.app.log(TAG,
        String.format("Created StaticEnemy with HP=%f\tDamage=%f", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    // Enemy is just lying around. Apply gravity.
    acceleration.y = gravity;

    if (!level.isComplete()) {

      // Start crawling as soon as it gets into the player's view
      if (isStateMoving()) {
        if (!crawling) {
          Gdx.app.log(TAG, "Crawl");
          setCrawling(true);

          if (target.getX() + target.getWidth() < getX()) // Set initial direction of crawl based on target position
            setFacingLeft(true);
          else
            setFacingLeft(false);
        }

        if (facingLeft) // Crawl to the left
          velocity.x = -VX;
        else // crawl to the right
          velocity.x = VX;
      }

      // Unset attacked flag
      if (!target.isDying() && attacked && !this.collidesWith(target)) {
        setAttacked(false);
        Gdx.app.log(TAG, "Collided with target. Unset attacked " + attacked());
      }

      // Deal damage to the target on collision
      if (!target.isDying() && this.collidesWith(target) && !attacked) {
        setAttacked(true);
        target.hurt(damage);
        Gdx.app.log(TAG, "Collided with target. Attacked with " + getDamage() + " damage. attacked?" + attacked);
      }
      
    }

  }
  
  @Override
  protected void collideAlongX(float delta, boolean left) {
    setFacingLeft(!left);
  }
  
  @Override
  protected void collideAlongY(float delta, boolean top) {
    if (velocity.y < 0)
      setGrounded(true);
    velocity.y = 0;
  }

  /**
   * @param crawling true if the enemy is crawling
   */
  private void setCrawling(boolean crawling) {
    this.crawling = crawling;
  }

}
