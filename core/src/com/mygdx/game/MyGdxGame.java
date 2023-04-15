package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int scorePlayer1 = 0;
	private int scorePlayer2 = 0;

	private Rectangle paddle1, paddle2, ball;
	private Vector2 ballVelocity;
	private boolean ballActive = true;

	private static final int GAME_HEIGHT = 480;
	private static final int BUTTON_HEIGHT = 40;
	private static final float INITIAL_SPEED = 140;

	// Message
	private boolean showMessage = false;
	private long messageStartTime;
	private static final long MESSAGE_DURATION = 4 * 1000; // 6 seconds

	private Rectangle resetButton, touchesButton, spaceButton, quitButton;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, GAME_HEIGHT);

		shapeRenderer = new ShapeRenderer();

		font = new BitmapFont();

		paddle1 = new Rectangle(20, 160, 10, 60);
		paddle2 = new Rectangle(770, 160, 10, 60);

		ball = new Rectangle(Gdx.graphics.getWidth() / 2 - 5, GAME_HEIGHT / 2 - 5, 10, 10);
		ballVelocity = getRandomVelocity(INITIAL_SPEED);

		resetButton = new Rectangle(0, 0, 200, BUTTON_HEIGHT);
		touchesButton = new Rectangle(200, 0, 200, BUTTON_HEIGHT);
		spaceButton = new Rectangle(400, 0, 200, BUTTON_HEIGHT);
		quitButton = new Rectangle(600, 0, 200, BUTTON_HEIGHT);

	}

	@Override
	public void render() {
		update();
		draw();
	}

	private void update() {
		if (ballActive) {
			ball.x += ballVelocity.x * Gdx.graphics.getDeltaTime();
			ball.y += ballVelocity.y * Gdx.graphics.getDeltaTime();

			if (ball.y <= BUTTON_HEIGHT + 10 || ball.y + ball.height >= GAME_HEIGHT - 10) {
				ballVelocity.y = -ballVelocity.y;
			}

			if (ball.overlaps(paddle1) || ball.overlaps(paddle2)) {
				ballVelocity.x = -ballVelocity.x;
				Gdx.app.log("ballVelocity beforeSCL", ballVelocity.toString());

				ballVelocity.scl(1.1f);
				Gdx.app.log("ballVelocity afterSCL", ballVelocity.toString());

			}

			if (Gdx.input.isKeyPressed(Input.Keys.W) && paddle1.y + paddle1.height < GAME_HEIGHT - 10) {
				paddle1.y += 8;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.S) && paddle1.y > 50) {
				paddle1.y -= 8;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.UP) && paddle2.y + paddle2.height < GAME_HEIGHT - 10) {
				paddle2.y += 8;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && paddle2.y > 50) {
				paddle2.y -= 8;
			}

			if (ball.x + ball.width < 0) {
				ballActive = false;
				scorePlayer1++;
			}
			if (ball.x > Gdx.graphics.getWidth()) {
				ballActive = false;
				scorePlayer2++;
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || spaceButton.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())
				&& Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			ballActive = false;
			ball = new Rectangle(Gdx.graphics.getWidth() / 2 - 5, GAME_HEIGHT / 2 - 5, 10, 10);
			ballVelocity = getRandomVelocity(INITIAL_SPEED);
			ballActive = true;
		}

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			int screenX = Gdx.input.getX();
			int screenY = Gdx.input.getY();
			int y = Gdx.graphics.getHeight() - screenY; // invert Y coordinate
			if (resetButton.contains(screenX, y)) {
				scorePlayer1 = 0;
				scorePlayer2 = 0;
				Gdx.app.log("InputProcessor", "Reset button clicked");
			} else if (touchesButton.contains(screenX, y)) {
				showMessage = true;
				messageStartTime = System.currentTimeMillis();
				Gdx.app.log("InputProcessor", "Touches button clicked");
			} else if (spaceButton.contains(screenX, y)) {
				// action pour le bouton de lancement de balle
				ballActive = false;
				ball = new Rectangle(Gdx.graphics.getWidth() / 2 - 5, GAME_HEIGHT / 2 - 5, 10, 10);
				ballVelocity = getRandomVelocity(INITIAL_SPEED);
				ballActive = true;
				Gdx.app.log("InputProcessor", "Launch ball button clicked");
			} else if (quitButton.contains(screenX, y)) {
				// action pour le bouton de quitter
				Gdx.app.log("InputProcessor", "Quit button clicked");
				Gdx.app.exit();
			}
		}

		if (showMessage && System.currentTimeMillis() - messageStartTime > MESSAGE_DURATION) {
			showMessage = false;
		}
	}


			private void draw() {

		Gdx.gl.glClearColor(0, 0, 0, 1);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(paddle1.x, paddle1.y, paddle1.width, paddle1.height);
		shapeRenderer.rect(paddle2.x, paddle2.y, paddle2.width, paddle2.height);
		shapeRenderer.rect(ball.x, ball.y, ball.width, ball.height);

		// dessiner la ligne blanche en haut et en bas
		shapeRenderer.rect(0, BUTTON_HEIGHT+1, Gdx.graphics.getWidth(), 9);
		shapeRenderer.rect(0, 470, Gdx.graphics.getWidth(), 9);
		shapeRenderer.end();

		// dessiner le score en haut de l'écran
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		font.draw(batch, "Score joueur 1: " + scorePlayer1, 20, GAME_HEIGHT -20);
		font.draw(batch, "Score joueur 2: " + scorePlayer2, Gdx.graphics.getWidth() - 200, GAME_HEIGHT -20);
		batch.end();

		//position curseur
		batch.begin();
		font.draw(batch, "X: " + Gdx.input.getX() + " Y: " + (Gdx.graphics.getHeight() - Gdx.input.getY()), Gdx.graphics.getWidth() - 450, GAME_HEIGHT - 20);
		batch.end();

		// dessiner les boutons du menu
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
		shapeRenderer.rect(resetButton.x, resetButton.y, resetButton.width, resetButton.height);
		shapeRenderer.rect(touchesButton.x, touchesButton.y, touchesButton.width, touchesButton.height);
		shapeRenderer.rect(spaceButton.x, spaceButton.y, spaceButton.width, spaceButton.height);
		shapeRenderer.rect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
		shapeRenderer.end();

		// dessiner le texte des boutons
		batch.begin();
		font.draw(batch, "Réinitialiser le jeu", resetButton.x + 20, resetButton.y + BUTTON_HEIGHT / 1.6f );
		font.draw(batch, "Touches", touchesButton.x + 80, touchesButton.y + BUTTON_HEIGHT / 1.6f );
		font.draw(batch, "Lancer une nouvelle balle", spaceButton.x + 20, spaceButton.y + BUTTON_HEIGHT / 1.6f );
		font.draw(batch, "Quitter le jeu", quitButton.x + 60, quitButton.y + BUTTON_HEIGHT / 1.6f );
		batch.end();

		// message des touches
		if (showMessage) {
			batch.begin();
			String message = "Copie de PONG produite par Clément Duval.\n"
					+ "Amusez vous bien ! \n"
					+ "player1 : Z/S \n"
					+ "player2 UP/DOWN\n"
					+ "SPACE pour relancer la balle";			float messageX = 300;
			float messageY = GAME_HEIGHT / 2;

			font.draw(batch, message, messageX, messageY);
			batch.end();
		}
	}

	private Vector2 getRandomVelocity(float speed) {
		Random random = new Random();
		float x = random.nextBoolean() ? speed : -speed;
		float y = random.nextBoolean() ? speed : -speed;
		return new Vector2(x, y);
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		font.dispose();
	}


}
