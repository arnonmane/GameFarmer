package com.mygdx.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

/**
 * Created by arnat on 4/7/2560.
 */

//Sprites สไปรท์ก็คือรูปภาพที่เราจะนำมาใช้ในเกมเป็นภาพของวัตถุ (Object) นำมากำหนดการกระทำหรือแสดง

//อิฐ
public class Brick extends InteractiveTileObject { //InteractiveTileObject วัตถุกระเบื่องแบบโต้ตอบ
    public  Brick(PlayScreen screen, MapObject object){

        super(screen, object);
        fixture.setUserData(this);//setUserData เซ็ตกำหนดชื่อข้อมูลให้กับยูเซอร์นี้ this

        setCategoryFilter(GameFarmer.BRICK_BIT);
        //setCategoryFilter ตั้งค่าตัวกรองหมวดหมู่ GameFarmer.COIN_BIT
    }


    //เมธอทหัวโหม่ง
    @Override
    public void onHeadHit(Mario mario) {

        if (mario.isBig()) {//ถ้ามาริโอใหญ่

            Gdx.app.log("Brick", "Collision");
            //ตั้งค่าตัวกรองหมวดหมู่
            setCategoryFilter(GameFarmer.DESTROYED_BIT);
            //setCategoryFilter ตั้งค่าตัวกรองหมวดหมู่ เป็น GameFarmer.DESTROYED_BIT
            getCell().setTile(null);//getCell อ่านค่าตำแหน่งเชลแล้วกำหนด setTile ให้เท่ากับว่าง คือไม่มีรูป

            Hud.addScore(200);//ได้คะแนน
            GameFarmer.manager.get("audio/sounds/breakblock.wav", Sound.class).play();

        }
        GameFarmer.manager.get("audio/sounds/bump.wav", Sound.class).play();

    }
}
