package com.github.TheDwoon.robots.client.student;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.AI;
import com.github.TheDwoon.robots.server.actions.UseItem;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;

public abstract class AbstractBasicAI implements AI {
    private Robot robot;
    private List<Field> fields;
    private List<Entity> entities;

    private Field front;
    private Field left;
    private Field right;
    private Field back;
    private Field beneath;

    public AbstractBasicAI() {
        fields = new ArrayList<>();
        entities = new ArrayList<>();
    }

    @Override
    public final void updateRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public final void updateVision(List<Field> fields, List<Entity> entities) {
        this.fields = fields;
        this.entities = entities;

        final int rx = getRobot().getX();
        final int ry = getRobot().getY();

        fields.stream().forEach(field -> {
            final int fx = field.getX();
            final int fy = field.getY();
            if (rx == fx && ry == fy)
                beneath = field;
            else if (rx == fx && ry + 1 == fy)
                front = field;
            else if (rx == fx && ry - 1 == fy)
                back = field;
            else if (rx + 1 == fx && ry == fy)
                right = field;
            else if (rx - 1 == fx && ry == fy)
                left = field;
        });
    }

    public final DriveBackward driveBackward() {
        return DriveBackward.INSTANCE;
    }

    public final DriveForward driveForward() {
        return DriveForward.INSTANCE;
    }

    public final TurnLeft trunLeft() {
        return TurnLeft.INSTANCE;
    }

    public final TurnRight turnRight() {
        return TurnRight.INSTANCE;
    }

    public final UseItem useItem(Item item) {
        return new UseItem(item);
    }

    public final Field getFront() {
        return front;
    }

    public final Field getLeft() {
        return left;
    }

    public final Field getRight() {
        return right;
    }

    public final Field getBack() {
        return back;
    }

    public final Field getBeneath() {
        return beneath;
    }

    public final List<Field> getFields() {
        return fields;
    }

    public final List<Entity> getEntities() {
        return entities;
    }

    public final Robot getRobot() {
        return robot;
    }
}
