package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Material;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.game_loader.GameLoader;
import com.github.TheDwoon.robots.server.managers.GameManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractBasicAITest {

	private GameManager gameManager;
	private TestAI testAI;

	@Before
	public void load() {
		gameManager = GameLoader.loadFromFile("facing_test");
		testAI = new TestAI();
		gameManager.spawnAi(testAI);
		gameManager.makeTurn();
		testAI.getRobot().setFacing(Facing.NORTH);
		gameManager.makeTurn();
	}

	@Test
	public void testFacing() {
		assertEquals(testAI.getBeneath().getMaterial(), Material.SPAWN);
		assertEquals(testAI.getFront().getMaterial(), Material.ROCK);
		assertEquals(testAI.getBack().getMaterial(), Material.VOID);
		assertEquals(testAI.getLeft().getMaterial(), Material.TREE);
		assertEquals(testAI.getRight().getMaterial(), Material.WATER);
	}

	private class TestAI extends AbstractBasicAI {

		@Override
		public PlayerAction makeTurn() {
			return null;
		}

		@Override
		public String getRobotName() {
			return null;
		}
	}
}
