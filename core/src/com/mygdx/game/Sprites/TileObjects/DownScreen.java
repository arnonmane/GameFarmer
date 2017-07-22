package com.mygdx.game.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

/**
 * Created by arnat on 6/7/2560.
 */

public class DownScreen extends InteractiveTileObject { //InteractiveTileObject วัตถุกระเบื่องแบบโต้ตอบ

    public  DownScreen(PlayScreen screen, MapObject object){

        super(screen, object);
        fixture.setUserData(this);//setUserData เซ็ตกำหนดชื่อข้อมูลให้กับยูเซอร์นี้ this

        setCategoryFilter(GameFarmer.DOWNSCREEN);
        //setCategoryFilter ตั้งค่าตัวกรองหมวดหมู่ GameFarmer.COIN_BIT
    }


    //เมธอทหัวโหม่ง
    @Override
    public void onHeadHit(Mario mario) {


    }
}
