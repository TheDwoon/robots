package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.server.actions.PlayerAction;

import java.util.Random;

public class RandomDriveAI extends AbstractBasicAI {
	private final Random random = new Random();
	private final PlayerAction[] options = new PlayerAction[] {driveForward(), driveBackward(), turnLeft(), turnRight(), noAction()};
	
	public RandomDriveAI() {
		
	}
	
	@Override
	public PlayerAction makeTurn() {
		 return options[random.nextInt(options.length)];
	}
}
