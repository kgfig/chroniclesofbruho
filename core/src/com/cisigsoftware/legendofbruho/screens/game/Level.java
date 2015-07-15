/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game;

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

  private static int[][] demoLevel = new int[][] {
    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    {1,0,1,1,1,1,0,1,1,0,0,1,1,0,0,1,1,1,1,1,0,1,1,1,1,0,1,0,1,1,1,1,1,1,0,0,1},
    {1,0,0,1,1,1,0,0,1,0,0,1,0,0,0,0,1,1,1,0,0,0,1,1,0,0,0,0,0,1,1,1,1,1,0,0,1},
    {1,0,0,0,1,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,1},
    {1,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,3,0,0,0,0,0,0,4,1},
    {1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,3,1,1},
    {1,0,0,0,0,0,0,3,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,1,1,0,0,0,0,1,1,1},
    {1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,3,0,1,1,1,0,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1},
    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

  private int width;
  private int height;
  private Block[][] blocks;
  private Array<Enemy> enemies;
  private boolean complete;

  public Level() {
    createDemoLevel();
    complete = false;
  }

  private void createDemoLevel() {
    this.width = demoLevel[0].length;
    this.height = demoLevel.length;
    blocks = new Block[width][height];
    enemies = new Array<Enemy>();

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        switch (demoLevel[height - 1 - y][x]) {
          case 1:
            blocks[x][y] = new Block(x, y);
            break;
          case 3:
            enemies.add(new BouncingEnemy(this, x, y));
            break;
          case 4:
            blocks[x][y] = new Block(x, y);
            blocks[x][y].setGoal(true);
            break;
          default:
            blocks[x][y] = null;
            break;
        }
      }
    }
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
  public Array<Block> getCollidableBlocks(Array<Block> collidable, int startX, int startY, int endX,
      int endY) {
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

  /**
   * Returns the block marked as goal
   * @return the block marked as goal
   */
  public Block getGoal() {
    for (Block[] blockRow : blocks) {
      for (Block block : blockRow) {
        if (block != null && block.isGoal()) {
          return block;
        }
      }
    }

    return null;
  }

  /**
   * @return true if the level has been completed
   */
  public boolean isComplete() {
    return complete;
  }

  /**
   * @param complete true if the level has been completed
   */
  public void setComplete(boolean complete) {
    this.complete = complete;
  }
}
