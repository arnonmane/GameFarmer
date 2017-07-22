package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameFarmer;

/**
 * Created by arnat on 4/7/2560.
 */

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public GameOverScreen(Game game){//คอนสตัคเตอ
        this.game = game;
        //สร้างวิวสกีน FitViewport เพื่อรักษาสัดส่วนภาพที่แท้จริงแม้ว่าจะมีขนาดหน้าจอก็ตาม
        viewport =new FitViewport(GameFarmer.V_WIDTH,GameFarmer.V_HEIGHT,new OrthographicCamera());
        stage =new Stage(viewport, ((GameFarmer)game).batch);

        Label.LabelStyle font =new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table =new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER",font);
        Label playAgainLabel = new Label("Click to Play Again",font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);


        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //เมื่อสัมผัสให้เริ่มไหม่
        if (Gdx.input.justTouched()) {
            //เริ่มไหม่
            game.setScreen(new PlayScreen((GameFarmer) game, true));
            dispose();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            game.setScreen(new PlayScreen((GameFarmer) game, true));
            dispose();
        }



        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
