package com.mygdx.game.Sprites.Skills;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by arnat on 4/7/2560.
 */

public abstract class skill extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity; //ความเร็ว
    protected boolean toDestroy;
    protected boolean destroyed;
    protected boolean fireRight;
    protected Body body;

    public skill(PlayScreen screen, float x,float y, boolean fireRight ) {//รูปไอเทม  (จอภาพ,  x, y)

        this.fireRight = fireRight;//วิ่งซ้ายขวา
        this.screen = screen;//จอภาพ
        this.world = screen.getWorld();//getWorld อ่านค่า world จาก PlayScreen
        setPosition(x, y);//เซ็ตค่าตำแหน่ง

        defineFireBall();//defineSkill กำหนดทักษะ
        toDestroy = false;//ที่จะทำลาย
        destroyed = false;//ทำลาย

    }

    public abstract void defineFireBall();//defineSkill กำหนดทักษะ



    public void update(float dt){

        if(toDestroy && !destroyed){//ถ้า ที่จะทำลาย && ไม่ทำลาย
            world.destroyBody(body);//ทำลายร่างกายบนโลก
            destroyed =true;//เซ็ตทำลาย

        }

    }

    public void draw(Batch batch){//วาดไอเทม

        if (!destroyed)//ถ้ายังไม่ทำลายให่วาด
            super.draw(batch);//สั้งวาด
    }

    public void destroy(){//ทำลาย

        toDestroy=true;//ที่จะทำลาย
    }


}