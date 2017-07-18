package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.items.Gun;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class DuellingAI extends AbstractBasicAI {
    private final Facing facing;

    public DuellingAI(Facing facing) {
        this.facing = facing;
    }

    @Override
    public PlayerAction makeTurn() {
        // turn to opponent
        Facing currentFacing = getRobot().getFacing();
        if (currentFacing != this.facing) {
            if (currentFacing.left() == facing) {
                return turnLeft();
            } else {
                return turnRight();
            }
        }

        // pick up weapon
        Field beneath = getBeneath();
        if (beneath.hasItem()) {
            return pickUpItem();
        }

        // spy for enemy
        int gunSlot = getInventory().getFirstMatchingSlot(item -> item instanceof Gun);
        if (gunSlot >= 0) {
            Gun gun = (Gun) getInventory().getItem(gunSlot);
            Stream<Field> fieldStream = getFields().stream();
            switch (this.facing) {
                case NORTH:
                    fieldStream = fieldStream.filter(field -> field.getX() == getRobot().getX() && field.getY() > getRobot().getY());
                    break;
                case WEST:
                    fieldStream = fieldStream.filter(field -> field.getX() < getRobot().getX() && field.getY() == getRobot().getY());
                    break;
                case SOUTH:
                    fieldStream = fieldStream.filter(field -> field.getX() == getRobot().getX() && field.getY() < getRobot().getY());
                    break;
                case EAST:
                    fieldStream = fieldStream.filter(field -> field.getX() > getRobot().getX() && field.getY() == getRobot().getY());
                    break;
            }
            List<LivingEntity> opponents = fieldStream.filter(Field::isOccupied)
                    .filter(field -> abs(field.getX() - getRobot().getX() + field.getY() - getRobot().getY()) <= gun.getRange())
                    .map(Field::getOccupant).collect(Collectors.toList());
            if (!opponents.isEmpty()) {
                return useItem(gunSlot);
            }
        }

        // move forward
        return driveForward();
    }
}
