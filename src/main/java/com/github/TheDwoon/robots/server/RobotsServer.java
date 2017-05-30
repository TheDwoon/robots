package com.github.TheDwoon.robots.server;

import java.io.IOException;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.broadcaster.BoardBroadcaster;
import com.github.TheDwoon.robots.server.broadcaster.EntityBroadcaster;
import com.github.TheDwoon.robots.server.broadcaster.InventoryBroadcaster;

public final class RobotsServer implements Runnable {
	private final BoardBroadcaster boardBroadcaster;
	private final EntityBroadcaster entityBroadcaster;
	private final InventoryBroadcaster inventoryBroadcaster;
	
	private Board board;
	
	private RobotsServer() {
		boardBroadcaster = new BoardBroadcaster();
		entityBroadcaster = new EntityBroadcaster();
		inventoryBroadcaster = new InventoryBroadcaster();
		
		Board board = null;
		try {
			board = MapFileParser.parseBoard(getClass().getResourceAsStream("/map/simple.map"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		setBoard(board);
		board.spawnEntity(new Robot(2, 2, new Inventory(12), null));
		board.spawnEntity(new Robot(10, 2, new Inventory(12), null));
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
			while (true) {
				// TODO (danielw, 30.05.2017): maybe a server "shell" here
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (IOException e) {
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
			for (Entity e : board.getEntities()) {
				if (e instanceof Robot) {
					eObserver.spawnRobot((Robot) e);
				} else {
					eObserver.spawnEntity(e);
				}
			}
		}
		
		if (iObserver != null) {
			// TODO (danielw, 30.05.2017): implement
		}
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board board) {
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
