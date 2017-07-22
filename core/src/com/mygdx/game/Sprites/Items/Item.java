package com.mygdx.game.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

/**
 * Created by arnat on 4/7/2560.
 */

//abstract class คือ คลาสที่มีคียเวร์ด abstract อยู่หน้า class เป็นคลาสที่ถูกสร้างขึ้นมาเพื่อเป็นคลาสแม่ (super class)
// ใช้ในการกำหนดสเปคของคลาสลูก (sub class) ที่จะถูกสร้างเพื่อใช้งานจริง หรือคลาสที่ไม่ต้องการให้นำไปสร้างเป็นออบเจ็กต์ (object) ก็สามารทำให้เป็น abstract class ได้


public abstract class Item extends Sprite {

    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity; //ความเร็ว
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x,float y) {//รูปไอเทม  (จอภาพ,  x, y)

        this.screen = screen;//จอภาพ
        this.world = screen.getWorld();//getWorld อ่านค่า world จาก PlayScreen
        setPosition(x, y);//เซ็ตค่าตำแหน่ง
        setBounds(getX(), getY(), 16 / GameFarmer.PPM, 16 / GameFarmer.PPM);// setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด
        defineItem();//defineItem กำหนดไอเทม
        toDestroy = false;//ที่จะทำลาย
        destroyed = false;//ทำลาย

    }

    public abstract void defineItem();//defineItem กำหนดไอเทม
    public abstract void use(Mario mario);// mario ใช้use


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
    public void reverseVelocity(boolean x,boolean y){//reverseVelocity ความเร็วย้อนกลับ

        if(x)//ถ้าจริงให้วิ่งไปมา
            velocity.x= -velocity.x;//velocity.x เท่ากับวิ่งกลับ
        if(y)//ถ้าจริงให้วิ่งขึ้นลง
            velocity.y =-velocity.y;//velocity.y เท่ากับวิ่งกลับ

    }
}
