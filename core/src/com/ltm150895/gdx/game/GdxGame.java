package com.ltm150895.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.ltm150895.gdx.game.utils.Constants.PPM; // if get then *, if set then /

public class GdxGame extends ApplicationAdapter {

	private boolean DEBUG = false;
	private float gravity_y = -9.8f;
	private float gravity_x = 0f;

	private Box2DDebugRenderer b2dr;
	private OrthographicCamera camera;
	private World world;
	private Body player, platform;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/2 , h/2);

		world = new World(new Vector2(gravity_x, gravity_y), false);
		b2dr = new Box2DDebugRenderer();

		player = createBox(8, 10, 16, 16, false);
		platform = createBox(0, 0, 64, 32, true);
	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b2dr.render(world,camera.combined.scl(PPM));
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public void dispose(){
		world.dispose();
		b2dr.dispose();
	}

	private void update(float deltaTime) {
		world.step(1 / 60f, 6, 2);

		cameraUpdate(deltaTime);
		inputUpdate(deltaTime);
	}

	private void cameraUpdate(float deltaTime){
		Vector3 position = camera.position;
		/*position.x = player.getPosition().x * PPM ;
		position.y = player.getPosition().y * PPM ;*/
		position.x = platform.getPosition().x * PPM;
        position.y = platform.getPosition().y * PPM;
		camera.position.set(position);

		camera.update();
	}

	private void inputUpdate(float deltaTime){
		int horizontalforce = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			horizontalforce -= 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			horizontalforce += 1;
		}

		if(Gdx.input.isTouched()){
			player.applyForceToCenter(0, 5, false);
		}

		player.setLinearVelocity(horizontalforce * 5 , player.getLinearVelocity().y);
	}

	private Body createBox(int x, int y, int width, int height, boolean isStatic){
		Body pbody;
		BodyDef def = new BodyDef();

		if(isStatic){
			def.type = BodyDef.BodyType.StaticBody;
		}
		else {
			def.type = BodyDef.BodyType.DynamicBody;
		}

		def.position.set(x / PPM  , y / PPM);
		def.fixedRotation = true;
		pbody = world.createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width /2 / PPM , height /2 / PPM);

		pbody.createFixture(shape, 1.0f);
		shape.dispose();

		return pbody;
	}


}
