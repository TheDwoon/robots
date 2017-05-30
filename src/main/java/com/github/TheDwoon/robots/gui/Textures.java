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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
            ImageView image = new ImageView(new Image(Textures.class.getResourceAsStream(
                    "/textures/entity_" + entity.getType().toLowerCase() + ".png")));
            if (entity instanceof Robot) {
                addColorFilter(image, RobotPaintManager.instance().getPaint((Robot) entity));
            }
            return image;
        } else {
            return new ImageView();
        }
    }

    public static void removeRobot(Robot robot) {
        RobotPaintManager.instance().releasePaint(robot);
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

    private static final class RobotPaintManager {
        private static RobotPaintManager instance;

        public static RobotPaintManager instance() {
            if (instance == null) {
                synchronized (RobotPaintManager.class) {
                    if (instance == null) {
                        instance = new RobotPaintManager();
                    }
                }
            }
            return instance;
        }


        // Google Material Design color palette [https://material.io/guidelines/style/color.html#color-color-palette]
        private static final Paint[] AVAILABLE_ROBOT_PAINTS = {
                // 500
                Color.web("#F44336"), Color.web("#009688"), Color.web("#3F51B5"), Color.web("#FFEB3B"),
                Color.web("#E91E63"), Color.web("#4CAF50"), Color.web("#2196F3"), Color.web("#FFC107"),
                Color.web("#9C27B0"), Color.web("#8BC34A"), Color.web("#03A9F4"), Color.web("#FF9800"),
                Color.web("#673AB7"), Color.web("#CDDC39"), Color.web("#00BCD4"), Color.web("#FF5722"),

                // 200
                Color.web("#EF9A9A"), Color.web("#80CBC4"), Color.web("#9FA8DA"), Color.web("#FFF59D"),
                Color.web("#F48FB1"), Color.web("#A5D6A7"), Color.web("#90CAF9"), Color.web("#FFE082"),
                Color.web("#CE93D8"), Color.web("#C5E1A5"), Color.web("#81D4FA"), Color.web("#FFCC80"),
                Color.web("#B39DDB"), Color.web("#E6EE9C"), Color.web("#80DEEA"), Color.web("#FFAB91"),

                // 900
                Color.web("#B71C1C"), Color.web("#004D40"), Color.web("#1A237E"), Color.web("#F57F17"),
                Color.web("#880E4F"), Color.web("#1B5E20"), Color.web("#0D47A1"), Color.web("#FF6F00"),
                Color.web("#4A148C"), Color.web("#33691E"), Color.web("#01579B"), Color.web("#E65100"),
                Color.web("#311B92"), Color.web("#827717"), Color.web("#006064"), Color.web("#BF360C")
        };

        private final Map<Robot, Integer> robotPaintMap;
        private final LinkedList<Integer> availablePaints;

        private RobotPaintManager() {
            robotPaintMap = new HashMap<>();
            availablePaints = new LinkedList<>();
            for (int i = 0; i < AVAILABLE_ROBOT_PAINTS.length; i++) {
                availablePaints.add(i);
            }
        }

        public Paint getPaint(Robot robot) {
            Integer paintIndex = robotPaintMap.computeIfAbsent(robot, k -> getNextAvailablePaintIndex());
            return AVAILABLE_ROBOT_PAINTS[paintIndex];
        }

        private synchronized int getNextAvailablePaintIndex() {
            if (availablePaints.isEmpty()) {
                throw new IllegalStateException("No more Robot colors left!");
            }
            return availablePaints.pollFirst();
        }

        public void releasePaint(Robot robot) {
            Integer paintIndex = robotPaintMap.get(robot);
            if (paintIndex != null) {
                synchronized (this) {
                    availablePaints.addFirst(paintIndex);
                    availablePaints.sort(Integer::compareTo);
                }
            }
        }
    }

    /**
     * Utility class.
     */
    private Textures() {
    }
}
