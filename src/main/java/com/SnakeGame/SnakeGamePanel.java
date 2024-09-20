package com.SnakeGame;

import lombok.Setter;
import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import java.awt.*;

public class SnakeGamePanel extends PluginPanel {
    @Setter
    private SnakeGamePlugin plugin;
    private JButton startStopButton;
    private JLabel scoreLabel;

    public SnakeGamePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        startStopButton = new JButton("Start Game");
        startStopButton.addActionListener(e -> toggleGame());
        startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(startStopButton);
        add(Box.createVerticalStrut(10));
        add(scoreLabel);
    }

    private void toggleGame() {
        if (plugin.isGameRunning()) {
            plugin.setGameRunning(false);
            startStopButton.setText("Start Game");
        } else {
            plugin.reset();
            startStopButton.setText("Stop Game");
        }
    }

    public void update() {
        scoreLabel.setText("Score: " + plugin.getScore());
    }
}