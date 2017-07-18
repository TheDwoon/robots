package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.GameManager;
import com.github.TheDwoon.robots.server.managers.InventoryManager;

import static java.lang.Math.round;

/**
 * Created by sigma_000 on 18.07.2017.
 */
public class Gun extends Weapon {

    private static final int DEFAULT_RANGE = 1;
    private static final int DEFAULT_DAMAGE = 2;
    private static final double DEFAULT_PIERCING_LOSS = 1;


    public Gun() {
        super(DEFAULT_RANGE, DEFAULT_DAMAGE, DEFAULT_PIERCING_LOSS);
    }

    public Gun(int x, int y) {
        super(x, y, DEFAULT_RANGE, DEFAULT_DAMAGE, DEFAULT_PIERCING_LOSS);
    }

    public Gun(long uuid, int x, int y, int range, int damage, double piercingLoss) {
        super(uuid, x, y, range, damage, piercingLoss);
    }

    @Override
    public void use(Robot robot, GameManager gameManager, BoardManager boardManager, InventoryManager inventoryManager) {
        Facing facing = robot.getFacing();

        double remainingDamage = getDamage();
        for (int i = 1; i <= getRange(); i++) {
            Field field = boardManager.getFieldChecked(robot.getX() + i * facing.dx, robot.getY() + i * facing.dy);
            if (field == null) {
                break;
            }

            if (field.isOccupied()) {
                boardManager.damageLivingEntity(field.getX(), field.getY(), (int) round(remainingDamage));
                remainingDamage *= (1 - getPiercingLoss());
                if (round(remainingDamage) <= 0) {
                    break;
                }
            }
        }
    }
}