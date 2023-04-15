package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

	private Rectangle paddle1, paddle2, ball;
	private Vector2 ballVelocity;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		shapeRenderer = new ShapeRenderer();

		paddle1 = new Rectangle(20, 240, 10, 60);
		paddle2 = new Rectangle(770, 240, 10, 60);

		ball = new Rectangle(400, 240, 10, 10);
		ballVelocity = new Vector2(-5, 5);
	}

	@Override
	public void render() {
		update();
		draw();
	}

	private void update() {
		ball.x += ballVelocity.x;
		ball.y += ballVelocity.y;

		if (ball.y <= 0 || ball.y + ball.height >= 480) {
			ballVelocity.y = -ballVelocity.y;
		}

		if (ball.overlaps(paddle1) || ball.overlaps(paddle2)) {
			ballVelocity.x = -ballVelocity.x;
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

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}
}
