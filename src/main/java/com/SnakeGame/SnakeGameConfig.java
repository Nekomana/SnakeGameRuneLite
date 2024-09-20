package com.SnakeGame;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("snakegame")
public interface SnakeGameConfig extends Config
{
    @ConfigItem(
            keyName = "gameWidth",
            name = "Game Width",
            description = "The width of the game area in cells"
    )
    default int gameWidth()
    {
        return 20;
    }

    @ConfigItem(
            keyName = "gameHeight",
            name = "Game Height",
            description = "The height of the game area in cells"
    )
    default int gameHeight()
    {
        return 20;
    }

    @ConfigItem(
            keyName = "cellSize",
            name = "Cell Size",
            description = "The size of each cell in pixels"
    )
    default int cellSize()
    {
        return 20;
    }

    @ConfigItem(
            keyName = "gameSpeed",
            name = "Game Speed",
            description = "The speed of the game (lower is faster)"
    )
    default int gameSpeed()
    {
        return 150;
    }

    @ConfigItem(
            keyName = "snakeColor",
            name = "Snake Color",
            description = "The color of the snake"
    )
    default String snakeColor()
    {
        return "#00FF00";
    }

    @ConfigItem(
            keyName = "foodColor",
            name = "Food Color",
            description = "The color of the food"
    )
    default String foodColor()
    {
        return "#FF0000";
    }
}