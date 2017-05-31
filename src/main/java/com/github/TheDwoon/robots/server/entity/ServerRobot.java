package com.github.TheDwoon.robots.server.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.client.student.AbstractBasicAI;
import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryImpl;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.entity.RobotImpl;
import com.github.TheDwoon.robots.server.RobotsServer;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public class ServerRobot extends ServerLivingEntity implements Robot {
    private final Robot robot;
    private final Inventory inventory;
    private AbstractBasicAI ai;
    
    public ServerRobot(RobotsServer server, int x, int y) {
    	this(server, new RobotImpl(x, y, new InventoryImpl(ServerInventory.DEFAULT_SIZE)));    	
    }
    
    public ServerRobot(RobotsServer server, Robot robot) {
    	this(server, robot, null);
    }
    
    // AI may be null
    public ServerRobot(RobotsServer server, Robot robot, AbstractBasicAI ai) {
    	super(server, robot);
    	
    	// wrapping the robots Inventory with the tracked ServerInventory.
    	this.inventory = new ServerInventory(server, robot.getInventory());
        this.robot = robot;
        this.ai = ai;
    }

    @Override
    public void update() {    	
    	super.update();
    	
    	if (ai == null) {
    		return;
    	}
    	
    	final List<Field> visableFields = new ArrayList<>(16);
    	final List<Entity> visableEntities = new ArrayList<>(8);
    	
    	final int posX = robot.getX();
    	final int posY = robot.getY();
    	final Board board = getServer().getBoard();
    	
    	final int xStart = Math.max(0, posX - 2), xEnd = Math.min(posX + 2, board.getWidth());
    	final int yStart = Math.max(0, posY - 2), yEnd = Math.min(posY + 2, board.getHeight()); 
    	
    	for (int x = xStart; x < xEnd; x++) {
    		for (int y = yStart; y < yEnd; y++) {
    			if (Math.abs(posX - x) + Math.abs(posY - y) <= 2) {
    				visableFields.add(board.getField(x, y));
    			}    				
    		}
    	}
    	
    	for (ServerEntity e : board.getEntities()) {
    		if (Math.abs(posX - e.getX()) + Math.abs(posY - e.getY()) <= 2) {
    			// only add data holding entity
    			visableEntities.add(e.getEntity());
    		}
    	}
    	
    	ai.updateRobot(getRobot());
    	ai.updateVision(visableFields, visableEntities);
    	
    	// TODO (danielw, 01.06.2017): maybe "think" in another thread.
    	ai.makeTurn();
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean equals(Object obj) {
        return robot.equals(obj);
    }

    @Override
    public int hashCode() {
        return robot.hashCode();
    }

    public final Robot getRobot() {
        return robot;
    }
    
    public final AbstractBasicAI getAI() {
		return ai;
	}
    
    public final void setAI(AbstractBasicAI ai) {
    	this.ai = ai;
    }
}
