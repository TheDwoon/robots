package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.server.actions.PlayerAction;

public interface AI {
	PlayerAction makeTurn(BoardInformation updates);
}
