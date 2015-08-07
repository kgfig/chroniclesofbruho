/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Block;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Crate;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Enemy;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Instruction;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Item;
import com.cisiglabs.bruhoplatformer.gamescreen.world.Item.ItemType;
import com.cisiglabs.bruhoplatformer.gamescreen.world.base.BoundedActor;
import com.cisiglabs.bruhoplatformer.gamescreen.world.enemy.BarricadeEnemy;
import com.cisiglabs.bruhoplatformer.gamescreen.world.enemy.CrawlingEnemy;
import com.cisiglabs.bruhoplatformer.gamescreen.world.enemy.TimedBombEnemy;
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
  private Array<Crate> crates;
  private Array<Item> items;
  private Map<Integer, Instruction> instructions;
  private BoundedActor[][] collidables;
  private boolean complete;

  public Level(int[][] matrix, InstructionRecord[] instructionText) {
    // Not complete by default
    complete = false;

    // Create enemies and build the terrain
    width = matrix[0].length;
    height = matrix.length;
    blocks = new Block[width][height];
    collidables = new BoundedActor[width][height];
    enemies = new Array<Enemy>();
    items = new Array<Item>();
    crates = new Array<Crate>();

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        collidables[x][y] = null;
        Gdx.app.log(TAG, "Create actor at position=(" + x + "," + y + ")");

        switch (matrix[height - 1 - y][x]) {
          case 1:
            blocks[x][y] = new Block(x, y);
            collidables[x][y] = blocks[x][y];
            break;
          case 3:
            Enemy crawler = new CrawlingEnemy(this, x, y);
            enemies.add(crawler);
            break;
          case 4:
            enemies.add(new TimedBombEnemy(this, x, y));
            break;
          case 5:
            BarricadeEnemy barricade = new BarricadeEnemy(this, x, y);
            enemies.add(barricade);
            collidables[x][y] = barricade;
            break;
          case 99:
            blocks[x][y] = new Block(x, y);
            blocks[x][y].setGoal(true);
            break;
          case 100: // Boxed Items
            // Create item
            Item item = new Item(ItemType.EQUIP, this, x, y);
            item.setInstructionId(11);
            items.add(item);
            
            // Put item inside a crate
            Array<Item> crateItems =new Array<Item> ();
            crateItems.add(item);
            
            Crate crate = new Crate(this, crateItems, x, y);
            crates.add(crate);
            collidables[x][y] = crate;
            break;
          default:
            blocks[x][y] = null;
            break;
        }
      }
    }

    // Add level instructions
    instructions = new HashMap<Integer, Instruction>();

    for (InstructionRecord model : instructionText) {
      Instruction instruction = new Instruction(model);
      instruction.setWidth(4);
      instruction.setAlignment(Align.center, Align.center);
      instructions.put(model.id, instruction);
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
   * Returns the collidable actor at the specified location
   * 
   * @param col x-coordinate
   * @param row y-coordinate
   * @return
   */
  public BoundedActor getCollidable(int col, int row) {
    return collidables[col][row];
  }
  
  /**
   * Removes the collidable actor at the specified location 
   * @param col
   * @param row
   */
  public void removeCollidable(int col, int row) {
    collidables[col][row] = null;
  }

  /**
   * @return the enemies
   */
  public Array<Enemy> getEnemies() {
    return enemies;
  }

  /**
   * @return the instructions in the level
   */
  public Collection<Instruction> getInstructions() {
    return instructions.values();
  }

  /**
   * 
   * @param id
   * @return the instruction with the given id
   */
  public Instruction getInstruction(int id) {
    return instructions.get(id);
  }

  /**
   * @return the items
   */
  public Array<Item> getItems() {
    return items;
  }

  /**
   * @return the crates
   */
  public Array<Crate> getCrates() {
    return crates;
  }

  /**
   * Returns the collidable blocks based on the given coordinates
   * 
   * @param startX left x
   * @param startY bottom y
   * @param endX right x
   * @param endY top y
   */
  public Array<BoundedActor> getCollidableBlocks(Array<BoundedActor> collidable, int startX, int startY, int endX,
      int endY) {
    collidable.clear();

    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        // Add block to collidable list and just make sure that it's within the level bounds
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
          collidable.add(getCollidable(x, y));
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

  /**
   * Removes the world actors from their stages
   */
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

    for (Instruction instruction : instructions.values()) {
      instruction.remove();
    }

    for (Item item : items) {
      item.remove();
    }
    
    for (Crate crate : crates) {
      crate.remove();
    }
  }
}
