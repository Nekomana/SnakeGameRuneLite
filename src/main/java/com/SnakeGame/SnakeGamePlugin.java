package com.SnakeGame;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.input.KeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@PluginDescriptor(
		name = "Snake Game",
		description = "Play Snake game in RuneLite",
		tags = {"minigame", "overlay"}
)
public class SnakeGamePlugin extends Plugin implements KeyListener {
	@Inject
	private Client client;

	@Inject
	private SnakeGameConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private SnakeGameOverlay overlay;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ClientToolbar clientToolbar;

	private boolean gameRunning = false;
	private List<Point> snake = new ArrayList<>();
	private Point food;
	private int direction = KeyEvent.VK_RIGHT;
	private Random random = new Random();
	private int score = 0;

	private NavigationButton navButton;
	private SnakeGamePanel panel;

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);
		keyManager.registerKeyListener(this);
		panel = injector.getInstance(SnakeGamePanel.class);
		panel.setPlugin(this);

		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/snake-icon.png");
		navButton = NavigationButton.builder()
				.tooltip("Snake Game")
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(this);
		clientToolbar.removeNavigation(navButton);
		gameRunning = false;
	}

	@Provides
	SnakeGameConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(SnakeGameConfig.class);
	}

	public void reset() {
		snake.clear();
		snake.add(new Point(config.gameWidth() / 2, config.gameHeight() / 2));
		spawnFood();
		gameRunning = true;
		score = 0;
		direction = KeyEvent.VK_RIGHT;
	}

	private void spawnFood() {
		int x, y;
		do {
			x = random.nextInt(config.gameWidth());
			y = random.nextInt(config.gameHeight());
		} while (snake.contains(new Point(x, y)));
		food = new Point(x, y);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameRunning) {
			return;
		}

		int key = e.getKeyCode();
		if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && (direction != KeyEvent.VK_RIGHT)) {
			direction = KeyEvent.VK_LEFT;
		} else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && (direction != KeyEvent.VK_LEFT)) {
			direction = KeyEvent.VK_RIGHT;
		} else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (direction != KeyEvent.VK_DOWN)) {
			direction = KeyEvent.VK_UP;
		} else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (direction != KeyEvent.VK_UP)) {
			direction = KeyEvent.VK_DOWN;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Subscribe
	public void onGameTick(GameTick tick) {
		if (client.getGameState() != GameState.LOGGED_IN || !gameRunning) {
			return;
		}

		gameLogic();
		panel.update();
	}

	private void gameLogic() {
		Point head = snake.get(0);
		Point newHead = new Point(head);

		switch (direction) {
			case KeyEvent.VK_LEFT:
				newHead.x = (newHead.x - 1 + config.gameWidth()) % config.gameWidth();
				break;
			case KeyEvent.VK_RIGHT:
				newHead.x = (newHead.x + 1) % config.gameWidth();
				break;
			case KeyEvent.VK_UP:
				newHead.y = (newHead.y - 1 + config.gameHeight()) % config.gameHeight();
				break;
			case KeyEvent.VK_DOWN:
				newHead.y = (newHead.y + 1) % config.gameHeight();
				break;
		}

		if (newHead.equals(food)) {
			snake.add(0, newHead);
			spawnFood();
			score++;
		} else {
			snake.add(0, newHead);
			snake.remove(snake.size() - 1);
		}

		// Check for collisions with self
		if (snake.subList(1, snake.size()).contains(newHead)) {
			gameRunning = false;
		}
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean running) {
		this.gameRunning = running;
	}

	public List<Point> getSnake() {
		return snake;
	}

	public Point getFood() {
		return food;
	}

	public int getScore() {
		return score;
	}
}