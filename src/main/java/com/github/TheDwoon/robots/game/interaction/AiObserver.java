package com.github.TheDwoon.robots.game.interaction;

import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public interface AiObserver {
	void spawnAi(Robot robot, Inventory inventory);

	void despawnAi(long robotUuid, long inventoryUuid);

	void updateScore(long robotUuid, int score);
}
