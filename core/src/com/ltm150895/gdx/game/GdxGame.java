package com.ltm150895.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.ltm150895.gdx.game.utils.Constants.PPM; // if get then *, if set then /

public class GdxGame extends ApplicationAdapter implements InputProcessor{

	private boolean DEBUG = false;
	private float gravity_y = -9.8f;
	private float gravity_x = 0;
    private int horizontalforce = 0;
    private SpriteBatch batch;
    private BitmapFont font;
    private float h , w;

	private Box2DDebugRenderer b2dr;
	private OrthographicCamera camera;
	private World world;
	public Body player, platform, obstacule;

	@Override
	public void create () {
        Gdx.input.setInputProcessor(this);
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/2 , h/2);

		world = new World(new Vector2(gravity_x, gravity_y), false);
		b2dr = new Box2DDebugRenderer();

		player = createBox(-150, 10, 16, 16, false);
        obstacule = createBox(0,-100 +32  , 50, 50 , true);
		platform = createBox(0, - 100 , Gdx.graphics.getWidth() , 32, true);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.CYAN);
        font.getData().setScale(PPM/8);

	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()) + " h = " + String.valueOf(player.getPosition() +  "floor = " + String.valueOf(platform.getPosition())) ,50, Gdx.graphics.getHeight() - 50  );
        batch.end();

		if(player.getPosition().x < platform.getPosition().y)
			platform = createBox( player.getPosition().x + Gdx.graphics.getWidth() , player.getPosition().y - 100 , Gdx.graphics.getWidth() , 32, true);

		b2dr.render(world,camera.combined.scl(PPM));
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public void dispose(){
		world.dispose();
		b2dr.dispose();
        font.dispose();
        batch.dispose();
	}

	private void update(float deltaTime) {
		world.step(1 / 60f, 6, 2);

		cameraUpdate(deltaTime);
	}

	private void cameraUpdate(float deltaTime){
		Vector3 position = camera.position;
		position.x = player.getPosition().x * PPM ;
		position.y = player.getPosition().y * PPM ;
		/*position.x = platform.getPosition().x * PPM;
        position.y = platform.getPosition().y * PPM;*/
		camera.position.set(position);

		camera.update();
	}

	private Body createBox(float x, float y, int width, int height, boolean isStatic){
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

    //InputHandler or InputProcessor
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		player.applyForceToCenter(50, 50, false);
        player.setLinearVelocity(horizontalforce * 5 , player.getLinearVelocity().y);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
