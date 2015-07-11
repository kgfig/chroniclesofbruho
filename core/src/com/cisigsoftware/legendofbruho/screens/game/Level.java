/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.screens.game.actors.Block;
import com.cisigsoftware.legendofbruho.screens.game.actors.BouncingEnemy;
import com.cisigsoftware.legendofbruho.screens.game.actors.Enemy;

/**
 * from:
 * https://github.com/obviam/star-assault/blob/part4/star-assault/src/net/obviam/starassault/model/
 * Level.java
 * 
 * @author kg
 *
 */
public class Level {

  private int width;
  private int height;
  private Block[][] blocks;
  private Array<Enemy> enemies;

  public Level(int width, int height) {
    this.width = width;
    this.height = height;
    blocks = new Block[width][height];
    enemies = new Array<Enemy>();

    createDemoTerrain();
    createDemoEnemies();
  }

  private void createDemoTerrain() {
    int rightMostX = width - 1;
    int middleX = height / 2;

    // nullify all blocks
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        blocks[col][row] = null;
      }
    }

    // build the ground and ceiling
    for (int col = 0; col < width; col++) {
      blocks[col][0] = new Block(new Vector2(col, 0));
      blocks[col][height - 1] = new Block(new Vector2(col, height - 1));

      if (col > 2)
        blocks[col][1] = new Block(new Vector2(col, 1));
    }

    // build the right wall
    for (int row = 2; row < height - 1; row++) {
      blocks[rightMostX][row] = new Block(new Vector2(rightMostX, row));
    }

    // build middle wall with passage at the bottom
    for (int row = 3; row < height - 1; row++) {
      blocks[middleX][row] = new Block(new Vector2(middleX, row));
    }
  }

  private void createDemoEnemies() {
    enemies.add(new BouncingEnemy(this, 6f, 2f));
  }

  /**
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * @param width the width to set
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * @return the blocks
   */
  public Block[][] getBlocks() {
    return blocks;
  }

  /**
   * @param blocks the blocks to set
   */
  public void setBlocks(Block[][] blocks) {
    this.blocks = blocks;
  }

  /**
   * Returns the block at the specified location
   * 
   * @param col x-coordinate
   * @param row y-coordinate
   * @return
   */
  public Block getBlock(int col, int row) {
    return blocks[col][row];
  }

  /**
   * @return the enemies
   */
  public Array<Enemy> getEnemies() {
    return enemies;
  }

  /**
   * @param enemies the enemies to set
   */
  public void setEnemies(Array<Enemy> enemies) {
    this.enemies = enemies;
  }

  /**
   * Returns the collidable blocks based on the given coordinates
   * 
   * @param startX left x
   * @param startY bottom y
   * @param endX right x
   * @param endY top y
   */
  public Array<Block> getCollidableBlocks(Array<Block> collidable, int startX, int startY, int endX, int endY) {
    collidable.clear();

    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        // Add block to collidable list and just make sure that it's within the level bounds
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
          collidable.add(getBlock(x, y));
      }
    }

    return collidable;
  }
}
