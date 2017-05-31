package com.github.TheDwoon.robots.client.student;

import java.util.Random;

import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;

public class RandomDriveAI extends AbstractBasicAI {
	private final Random random = new Random();
	private final PlayerAction[] options = new PlayerAction[] {DriveForward.INSTANCE, DriveBackward.INSTANCE, TurnLeft.INSTANCE, TurnRight.INSTANCE, NoAction.INSTANCE};
	
	public RandomDriveAI() {
		
	}
	
	@Override
	public PlayerAction makeTurn() {
		 return options[random.nextInt(options.length)];
	}
}
