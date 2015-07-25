/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Block;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Enemy;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Instruction;
import com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes.BarricadeEnemy;
import com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes.CrawlingEnemy;
import com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes.TimedBombEnemy;
import com.cisigsoftware.legendofbruho.utils.InstructionRecord;

/**
 * @author kg
 *
 */
public class Level {

  private static final String TAG = Level.class.getSimpleName();

  private int width;
  private int height;
  private Block[][] blocks;
  private Array<Enemy> enemies;
  private Array<Instruction> instructions;
  private boolean complete;

  public Level(int[][] matrix, InstructionRecord[] instructionText) {
    // Not complete by default
    complete = false;

    // Create enemies and build the terrain
    width = matrix[0].length;
    height = matrix.length;
    blocks = new Block[width][height];
    enemies = new Array<Enemy>();

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Gdx.app.log(TAG, "Create actor at position=(" + x + "," + y + ")");

        switch (matrix[height - 1 - y][x]) {
          case 1:
            blocks[x][y] = new Block(x, y);
            break;
          case 3:
            Enemy enemy = new CrawlingEnemy(this, x, y);
            enemies.add(enemy);
            break;
          case 4:
            enemies.add(new TimedBombEnemy(this, x, y));
            break;
          case 5:
            enemies.add(new BarricadeEnemy(this, x, y));
            break;
          case 99:
            blocks[x][y] = new Block(x, y);
            blocks[x][y].setGoal(true);
            break;
          default:
            blocks[x][y] = null;
            break;
        }
      }
    }

    // Add level instructions
    instructions = new Array<Instruction>();

    for (InstructionRecord inst : instructionText) {
      Instruction instruction = new Instruction(inst.text, inst.x, inst.y);
      instruction.setWidth(4);
      instruction.setAlignment(Align.center, Align.center);
      instructions.add(instruction);
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
   * @return the instructions in the level
   */
  public Array<Instruction> getInstructions() {
    return instructions;
  }

  /**
   * @param instructions the instructions for the level
   */
  public void setInstructions(Array<Instruction> instructions) {
    this.instructions = instructions;
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
   * 
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

  public void remove() {
    for (Enemy enemy : enemies) {
      enemy.remove();
    }

    for (Block[] blockRow : blocks) {
      for (Block block : blockRow) {
        if (block != null)
          block.remove();
      }
    }

    // for (Instruction instruction : instructions) {
    // instruction.remove();
    // }
  }
}
