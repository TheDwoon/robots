package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.server.actions.PlayerAction;

import java.util.Random;

public class RandomItemCollectorAI extends AbstractBasicAI {
    private final Random random = new Random();
    private final PlayerAction[] movementActions = new PlayerAction[]{driveForward(), driveBackward(), turnLeft(), turnRight()};

    public RandomItemCollectorAI() {

    }

    @Override
    public PlayerAction makeTurn() {
        if (getBeneath().hasItem()) {
            return pickUpItem();
        }
        return movementActions[random.nextInt(movementActions.length)];
    }
}
