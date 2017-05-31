package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Item;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.floor;
import static java.lang.Math.min;

public final class GameDisplay extends HBox
        implements BoardObserver, EntityObserver, InventoryObserver {

    private static final Logger log = LogManager.getLogger();

    @FXML
    private Pane gameBoardContainer;
    @FXML
    private GridPane gameBoard;
    @FXML
    private VBox robotsContainer;

    private DoubleBinding fieldSize;

    private BoardFieldDisplay[][] boardFieldDisplays;
    private Map<Long, Entity> entities;
    private Map<Long, RobotDisplay> inventories;
    private Map<Robot, RobotDisplay> robotDisplays;

    public GameDisplay() throws IOException {
        FXMLUtils.loadFxRoot(this);
        fieldSize = new DoubleBinding() {

            {
                super.bind(gameBoardContainer.widthProperty(), gameBoardContainer.heightProperty());
            }

            @Override
            public void dispose() {
                super.unbind(gameBoardContainer.widthProperty(),
                        gameBoardContainer.heightProperty());
            }

            @Override
            protected double computeValue() {
                return floor(min(
                        gameBoardContainer.widthProperty().getValue() / boardFieldDisplays.length,
                        gameBoardContainer.heightProperty().getValue()
                                / boardFieldDisplays[0].length));
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.observableArrayList(gameBoardContainer.widthProperty(),
                        gameBoardContainer.heightProperty());
            }
        };

        boardFieldDisplays = new BoardFieldDisplay[0][0];
        entities = new HashMap<>();
        inventories = new HashMap<>();
        robotDisplays = new HashMap<>();
    }

    @Override
    public void setSize(long uuid, int width, int height) {
        boardFieldDisplays = new BoardFieldDisplay[width][height];

        gameBoard.getChildren().clear();
        try {
            for (int x = 0; x < boardFieldDisplays.length; x++) {
                for (int y = 0; y < boardFieldDisplays[x].length; y++) {
                    // fill with default values
                    BoardFieldDisplay boardFieldDisplay =
                            new BoardFieldDisplay(new Field(x, y, Material.VOID), null, fieldSize);
                    boardFieldDisplays[x][y] = boardFieldDisplay;
                }
            }
            for (int x = 0; x < boardFieldDisplays.length; x++) {
                gameBoard.addColumn(x, boardFieldDisplays[x]);
            }
        } catch (IOException e) {
            log.catching(e);
        }
    }

    @Override
    public void updateField(long uuid, Field field) {
        boardFieldDisplays[field.getX()][field.getY()].update(field);
    }

    @Override
    public void createInventory(Inventory inventory) {
        RobotDisplay robotDisplay = robotDisplays.entrySet().parallelStream()
                .filter(entry -> entry.getKey().getInventory().equals(inventory))
                .map(Map.Entry::getValue).findAny().orElse(null);
        inventories.put(inventory.getUUID(), robotDisplay);
        if (robotDisplay != null) {
            try {
                robotDisplay.setInventory(inventory);
            } catch (IOException e) {
                log.catching(e);
            }
        }
    }

    @Override
    public void deleteInventory(long uuid) {
        RobotDisplay robotDisplay = inventories.remove(uuid);
        if (robotDisplay != null) {
            try {
                robotDisplay.setInventory(null);
            } catch (IOException e) {
                log.catching(e);
            }
        }
    }

    @Override
    public void updateInventory(long uuid, int slot, Item item) {
        RobotDisplay robotDisplay = inventories.get(uuid);
        if (robotDisplay != null) {
            robotDisplay.updateItem(slot, item);
        }
    }

    @Override
    public void spawnEntity(Entity entity) {
        entities.put(entity.getUUID(), entity);
        boardFieldDisplays[entity.getX()][entity.getY()].setEntity(entity);
    }

    @Override
    public void spawnRobot(Robot robot) {
        RobotDisplay robotDisplay;
        try {
            robotDisplay = new RobotDisplay(robot);
        } catch (IOException e) {
            log.catching(e);
            return;
        }
        robotDisplays.put(robot, robotDisplay);
        robotsContainer.getChildren().add(robotDisplay);
        spawnEntity(robot);
    }

    @Override
    public void removeEntity(long uuid) {
        Entity entity = entities.get(uuid);
        if (entity != null) {
            boardFieldDisplays[entity.getX()][entity.getY()].setEntity(null);
            entities.remove(uuid);
            if (entity instanceof Robot) {
                Robot robot = (Robot) entity;
                RobotDisplay robotDisplay = robotDisplays.remove(robot);
                robotsContainer.getChildren().remove(robotDisplay);
                Textures.removeRobot(robot);

            }
        }
    }

    @Override
    public void updateEntity(Entity entity) {
        entities.put(entity.getUUID(), entity);
    }

    @Override
    public void updateLocation(long uuid, int x, int y) {
        Entity entity = entities.get(uuid);
        if (entity != null) {
            boardFieldDisplays[entity.getX()][entity.getY()].setEntity(null);
            entity.setPosition(x, y);
            boardFieldDisplays[x][y].setEntity(entity);
        }
    }

}
