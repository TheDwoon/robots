package com.github.TheDwoon.robots.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.TheDwoon.robots.client.student.RandomDriveAI;
import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.broadcaster.BoardBroadcaster;
import com.github.TheDwoon.robots.server.broadcaster.EntityBroadcaster;
import com.github.TheDwoon.robots.server.broadcaster.InventoryBroadcaster;
import com.github.TheDwoon.robots.server.entity.ServerBomb;
import com.github.TheDwoon.robots.server.entity.ServerEntity;
import com.github.TheDwoon.robots.server.entity.ServerItem;
import com.github.TheDwoon.robots.server.entity.ServerRobot;

public final class RobotsServer implements Runnable {
	private final BoardBroadcaster boardBroadcaster;
	private final EntityBroadcaster entityBroadcaster;
	private final InventoryBroadcaster inventoryBroadcaster;
	
	private ServerBoard board;
	
	private RobotsServer() {
		boardBroadcaster = new BoardBroadcaster();
		entityBroadcaster = new EntityBroadcaster();
		inventoryBroadcaster = new InventoryBroadcaster();
		
		ServerBoard board = null;
		try {
			board = MapFileParser.parseBoard(this, getClass().getResourceAsStream("/map/simple.map"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		setBoard(board);
		
		final ServerRobot randomRobot = new ServerRobot(this, 2, 2);
		randomRobot.setAI(new RandomDriveAI());
		board.spawnEntity(randomRobot);
		ServerRobot robot1 = new ServerRobot(this, 11, 2);
		robot1.setFacing(Facing.EAST);
		board.spawnEntity(robot1);
		ServerRobot robot2 = new ServerRobot(this, 13, 8);
		robot2.setFacing(Facing.WEST);
		board.spawnEntity(robot2);
		
		new Thread(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			
			ServerItem inventoryItem = null;
			ServerItem spawnedItem = null;
			ServerRobot visitor = null;
			int slot = -1;
			while (true) {				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				
				if (inventoryItem == null) {
					inventoryItem = new ServerBomb(RobotsServer.this);
					slot = randomRobot.getInventory().addItem(inventoryItem);
				} else {
					randomRobot.getInventory().removeItem(slot);
					inventoryItem = null;
				}
				
				if (spawnedItem == null) {
					spawnedItem = new ServerBomb(RobotsServer.this);
					getBoard().spawnEntity(spawnedItem, 5, 5);
				} else {
					getBoard().removeEntity(spawnedItem);
					spawnedItem = null;
				}
				
				if (visitor == null) {
					visitor = new ServerRobot(RobotsServer.this, 1, 4);
					getBoard().spawnEntity(visitor);
				} else {
					getBoard().removeEntity(visitor);
					visitor = null;
				}
			}
		}).start();
	}
	
	public static void main(final String[] args) throws IOException {
		RobotsServer server = new RobotsServer();
		server.run();
		System.out.println("EXIT");
	}

	@Override
	public void run() {
		KryoNetLoggerProxy.setAsKryoLogger();

		try (AIServer aiServer = new AIServer(this); UIServer uiServer = new UIServer(this)) {
			Thread.sleep(5000);
			while (true) {
				// TODO (danielw, 30.05.2017): maybe a server "shell" here
				
				List<ServerEntity> entities = board.getEntities();
				synchronized (entities) {
					List<ServerEntity> despawnEntities = new LinkedList<>();
					Iterator<ServerEntity> it = entities.iterator();
					while (it.hasNext()) {
						ServerEntity e = it.next();
						try {
							e.update();
						} catch (Exception e1) {
							despawnEntities.add(e);
						}
					}
					
					despawnEntities.forEach(e -> board.removeEntity(e));
				}
				
				Thread.sleep(1000);				
			}
		} catch (IOException e) {
			// ignore
		} catch (InterruptedException e) {
			// ignore
		}
	}
	
	public void transmitAll(BoardObserver bObserver, EntityObserver eObserver, InventoryObserver iObserver) {
		if (bObserver != null && board != null) {
			bObserver.setSize(board.getUUID(), board.getWidth(), board.getHeight());
			for (int x = 0; x < board.getWidth(); x++) {
				for (int y = 0; y < board.getHeight(); y++) {
					bObserver.updateField(board.getUUID(), board.getField(x, y));			
				}
			}
		}
		
		if (eObserver != null && board != null) {
			List<ServerEntity> entities = board.getEntities();
			synchronized (entities) {
				for (ServerEntity e : entities) {
					if (e instanceof ServerRobot) {
						eObserver.spawnRobot(((ServerRobot) e).getRobot());
					} else {
						eObserver.spawnEntity(e.getEntity());
					}
				}			
			}
		}
		
		if (iObserver != null) {
			List<ServerEntity> entities = board.getEntities();
			synchronized (entities) {
				for (ServerEntity e : entities) {					
					if (e.getEntity() instanceof InventoryHolder) {
						Inventory inv = ((InventoryHolder) e.getEntity()).getInventory();
						iObserver.createInventory(inv);
					}
				}
			}
		}
	}
	
	public ServerBoard getBoard() {
		return board;
	}
	
	public void setBoard(ServerBoard board) {
		if (this.board == board || board == null)
			return;
		
		this.board = board;
		
		transmitAll(boardBroadcaster, null, null);
	}
	
	public BoardBroadcaster getBoardBroadcaster() {
		return boardBroadcaster;
	}
	
	public EntityBroadcaster getEntityBroadcaster() {
		return entityBroadcaster;
	}
	
	public InventoryBroadcaster getInventoryBroadcaster() {
		return inventoryBroadcaster;
	}
}
