package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.game.Robot;

public class KI {
	Robot robot;

	public KI(final Robot robot) {
		this.robot = robot;
	}

	public void doTurn() {
		robot.turnLeft();
		robot.getWeaponModule().shoot();
	}
}
