package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.server.actions.PlayerAction;

public class RepeatingAI extends AbstractBasicAI {
    private final PlayerAction[] actions;
    private int nextActionIndex;

    public RepeatingAI(PlayerAction... actions) {
        this.actions = actions;
        nextActionIndex = 0;
    }

    @Override
    public PlayerAction makeTurn() {
        PlayerAction action = actions[nextActionIndex];
        nextActionIndex = (nextActionIndex + 1) % actions.length;
        return action;
    }
}
