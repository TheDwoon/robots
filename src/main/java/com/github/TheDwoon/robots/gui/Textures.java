package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sigmar on 28.05.17.
 */
public class Textures {

    public static ImageView lookup(final Material material) {
        return new ImageView(new Image(Textures.class.getResourceAsStream(
                "/textures/material_" + material.toString().toLowerCase() + ".png")));
    }

    public static ImageView lookup(final Entity entity) {
        if (entity != null) {
            if (!(entity instanceof Robot)) {
                return new ImageView(new Image(Textures.class.getResourceAsStream(
                        "/textures/entity_" + entity.getType().toLowerCase() + ".png")));
            } else {
                ImageView robotImage = new ImageView(new Image(Textures.class.getResourceAsStream(
                        "/textures/entity_" + entity.getType().toLowerCase() + ".png")));
                addColorFilter(robotImage, nextRobotColor());
                return robotImage;
            }
        } else {
            return new ImageView();
        }
    }

    private static final Paint[] ROBOT_PAINTS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
    };
    private static AtomicInteger robotCount = new AtomicInteger(0);

    private static Paint nextRobotColor() {
        int colorId = robotCount.getAndIncrement() % ROBOT_PAINTS.length;
        return ROBOT_PAINTS[colorId];
    }

    public static ImageView lookupInventoryBackground() {
        return new ImageView(new Image(Textures.class.getResourceAsStream("/textures/inventory.png")));
    }

    private static void addColorFilter(ImageView imageView, Paint paint) {
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1.0);

        ImageView clipImage = new ImageView(imageView.getImage());
        clipImage.fitHeightProperty().bind(imageView.fitHeightProperty());
        clipImage.fitWidthProperty().bind(imageView.fitWidthProperty());
        imageView.setClip(clipImage);
        Blend blush = new Blend(
                BlendMode.MULTIPLY,
                monochrome,
                new ColorInput(
                        0,
                        0,
                        imageView.getImage().getWidth(),
                        imageView.getImage().getHeight(),
                        paint
                )
        );
        imageView.setEffect(blush);
    }

    public static void bindSize(Region container, ImageView imageView) {
        imageView.fitWidthProperty().bind(container.widthProperty());
        imageView.fitHeightProperty().bind(container.heightProperty());
    }

    /**
     * Utility class.
     */
    private Textures() {
    }
}
