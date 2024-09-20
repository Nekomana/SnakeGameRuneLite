package com.SnakeGame;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

public class SnakeGameOverlay extends Overlay {
    private final SnakeGamePlugin plugin;
    private final SnakeGameConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private SnakeGameOverlay(SnakeGamePlugin plugin, SnakeGameConfig config) {
        super(plugin);
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isGameRunning()) {
            return null;
        }

        panelComponent.getChildren().clear();

        // Set up the game area
        int width = config.gameWidth() * config.cellSize();
        int height = config.gameHeight() * config.cellSize();
        panelComponent.setPreferredSize(new Dimension(width, height));

        // Draw the background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);

        // Draw the snake
        graphics.setColor(Color.decode(config.snakeColor()));
        for (Point p : plugin.getSnake()) {
            int x = p.x * config.cellSize();
            int y = p.y * config.cellSize();
            graphics.fillRect(x, y, config.cellSize(), config.cellSize());
        }

        // Draw the food
        graphics.setColor(Color.decode(config.foodColor()));
        Point food = plugin.getFood();
        int foodX = food.x * config.cellSize();
        int foodY = food.y * config.cellSize();
        graphics.fillOval(foodX, foodY, config.cellSize(), config.cellSize());

        // Draw grid lines (optional)
        graphics.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= config.gameWidth(); i++) {
            int x = i * config.cellSize();
            graphics.drawLine(x, 0, x, height);
        }
        for (int i = 0; i <= config.gameHeight(); i++) {
            int y = i * config.cellSize();
            graphics.drawLine(0, y, width, y);
        }

        return panelComponent.render(graphics);
    }
}