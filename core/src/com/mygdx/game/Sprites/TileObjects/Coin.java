package com.mygdx.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Items.ItemDef;
import com.mygdx.game.Sprites.Items.Mushroom;
import com.mygdx.game.Sprites.Mario;

/**
 * Created by arnat on 4/7/2560.
 */

//เหรียญ
public class Coin extends InteractiveTileObject { //InteractiveTileObject วัตถุกระเบื่องแบบโต้ตอบ
    private Array<TextureRegion> frames;
    private static TiledMapTileSet tileSet; //สร้างคอลเล็กชันที่ว่างเปล่าของ tilesets
    private final int BLANK_CION=28;//id 28 เหรียญว่าง

    //58 คือid เหร๊ยญ

    public Coin(PlayScreen screen, MapObject object){//คอนสตัคเตอร์

        super(screen,object);//ส่งค่า screen,object ไปยัง class แม่
        frames = new Array<TextureRegion>();
        //getTileSets คอลเลกชันของ tilesets สำหรับแผนที่นี้
        //getTileSet การจับคู่ tileset กับชื่อ("tileset_gutter")

        tileSet =map.getTileSets().getTileSet("tileset_gutter");

        //fixture มาจากคลาสแม่
        fixture.setUserData(this);// Fixture ก็คล้ายๆกับ Body แต่ต่างกันที่มันคือวัตถุที่สามารถจับต้องได้ มองเห็นได้
        //setUserData เซ็ตข้อมูลยูเซอร์นี้ this
        setCategoryFilter(GameFarmer.COIN_BIT);//เซ็ทค่าตัวกรองหมวดหมู่เป็น GameFarmer.COIN_BIT
        //setCategoryFilter ตั้งค่าตัวกรองหมวดหมู่ GameFarmer.COIN_BIT

    }

    //เมธอทหัวโหม่ง แสดงว่ามาริโอโหม่ง
    @Override
    public void onHeadHit(Mario mario) {// ต้องการ ออฟเจค Mario

        Gdx.app.log("Coin","Collision");

        //getCell คืออ่านพิกัด getTile ดึงข้อมูลของ TiledMapTile ตรง getId อ่านไอดีที่จะดึง = BLANK_CION มั้ย
        if (getCell().getTile().getId() == BLANK_CION)//ถ้าโหม่งกล่องแข็ง BLANK_CION คือโหม่งไม่เข้า
            GameFarmer.manager.get("audio/sounds/bump.wav",Sound.class).play();
        else {//ถ้าไม่
            //ถ้าเป็นเห็ด
            if (object.getProperties().containsKey("mushroom")) { //ตรวจสอบ Properties ใน lavel1.tmx เจอข้อความนี้ "mushroom" หรือไม่
                //เซ็ตรูปเห็ดขึ้นมา
                //spawnItem คือคลอดไอเทม
                //screen กับ object นี้ มาจากคลาส InteractiveTileObject
                //ส่งค่า spawnItem (ตำแหน่ง x ,ตำแหน่ง y ,ชนิด class)ง่ายๆคือส่งค่าที่ ItemDef ต้องการ
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / GameFarmer.PPM),
                        Mushroom.class));

                GameFarmer.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();

            }else//ถ้าไม่ใช่ให้เป็นเหรียญ
                GameFarmer.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }

        //กำหนดตำแหน่งนั้น == BLANK_CION โหม่งไม่เข้า่
        getCell().setTile(tileSet.getTile(BLANK_CION));//getCell อ่านตำแหน่งเซล setTile และเซ็ตคอลเล็กชันใน tileSet อ่านรูปค่ารูป BLANK_CION
        Hud.addScore(100);
    }
}
