package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;

import java.time.Clock;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

	private Rectangle paddle1, paddle2, ball;
	private Vector2 ballVelocity;
	private boolean ballActive = true;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		shapeRenderer = new ShapeRenderer();

		paddle1 = new Rectangle(20, 240, 10, 60);
		paddle2 = new Rectangle(770, 240, 10, 60);

		ball = new Rectangle(Gdx.graphics.getWidth() / 2 - 5, Gdx.graphics.getHeight() / 2 - 5, 10, 10);
		ballVelocity = getRandomVelocity(120);
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

			if (ball.y <= 0 || ball.y + ball.height >= 480) {
				ballVelocity.y = -ballVelocity.y;
			}

			if (ball.overlaps(paddle1) || ball.overlaps(paddle2)) {
				ballVelocity.x = -ballVelocity.x;
				// augmenter la vitesse de la balle de 10% à chaque impact
				ballVelocity.scl(1.06f);
			}

			// Contrôle des palettes avec les touches du clavier
			if (Gdx.input.isKeyPressed(Input.Keys.W) && paddle1.y + paddle1.height < 480) {
				paddle1.y += 5;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.S) && paddle1.y > 0) {
				paddle1.y -= 5;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.UP) && paddle2.y + paddle2.height < 480) {
				paddle2.y += 5;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && paddle2.y > 0) {
				paddle2.y -= 5;
			}

			// Si la balle sort de l'écran à droite ou à gauche, on la supprime et on lance une nouvelle balle avec espace
			if (ball.x + ball.width < 0 || ball.x > Gdx.graphics.getWidth()) {
				int screenWidth = Gdx.graphics.getWidth();
				int screenHeight = Gdx.graphics.getHeight();
				System.out.println(" ball.width : " +  ball.width);
				System.out.println("ball.x : " + ball.x);
				System.out.println("largeur : " + Gdx.graphics.getWidth());
				System.out.println("hauteur : " + Gdx.graphics.getHeight());
				System.out.println("paddle2.x : " + paddle2.x);

				ballActive = false;
			}
		} else {
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				// on lance une nouvelle balle avec une nouvelle position et une vitesse aléatoire plus rapide
				ball = new Rectangle(Gdx.graphics.getWidth() / 2 - 5, Gdx.graphics.getHeight() / 2 - 5, 10, 10);
				ballVelocity = getRandomVelocity(120); // augmenter la valeur pour une vitesse initiale plus rapide
				ballActive = true;
			}
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

		shapeRenderer.end();
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
	}
}
