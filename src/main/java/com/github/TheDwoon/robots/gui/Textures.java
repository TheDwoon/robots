package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import javafx.scene.image.Image;

/**
 * Created by sigmar on 28.05.17.
 */
public class Textures {

    /**
     * Utility class.
     */
    private Textures() {
    }

    public static Image lookup(final Material material) {
        return new Image(Textures.class.getResourceAsStream(
                "/textures/material_" + material.toString().toLowerCase() + ".png"));
    }

    public static Image lookup(final Entity entity) {
        if (entity != null) {
            if (!(entity instanceof Robot)) {
                return new Image(Textures.class.getResourceAsStream(
                        "/textures/entity_" + entity.getType().toLowerCase() + ".png"));
            } else {
                //  TODO (sigmarw, 28.05.17): add numbering
                return new Image(Textures.class.getResourceAsStream("/textures/entity_robot.png"));
            }
        } else {
            return null;
        }
    }

    public static Image lookupInventoryBackground() {
        return new Image(Textures.class.getResourceAsStream("/textures/inventory.png"));
    }
}
