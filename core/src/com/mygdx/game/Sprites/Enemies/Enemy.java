package com.mygdx.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Sprites.Skills.FireBall;
import com.mygdx.game.Sprites.Skills.KickAss;

/**
 * Created by arnat on 4/7/2560.
 */

//Enemy คือศัตรู
    /*Abstract Class คือ Class ชนิดหนึ่งที่สามารถกำหนด Method ทั้งในรูปแบบที่ระบุขั้นตอนการทำงาน (Method Description Process)
     และแบบระบุเพียงแค่ชื่อ Method และให้ Class อื่น ๆ ที่เรียกใช้งานนำไปเขียนขั้นตอนการดำเนินงานเองเหมือนกับ
    Interface Class โดยเรามาดูกันถึงประโยชน์ และวิธีการเขียน Abstract Class กันครับ
    ทำให้เราสามารถเขียน Class ที่มีทั้งแบบ Method + Process และ Method แบบที่ให้ Class อื่น ๆ ที่เรียกใช้งานไประบุขั้นตอนการทำงานเอง*/

//Enemy เป็นคลาสแม่ ผู้ที่สืบทอดกเหมือนมีสายเลือดแม่
public abstract class Enemy extends Sprite {

    // protected หรือเก็บไว้ให้ลูกดังนั้นตัวแม่ ไม่สามารถใช้งานในส่วนนี้ได้ ผู้เรียกใช้ก็ไม่สามารถใช้งานได้ ยกเว้นแต่ทำการ inheritance มันแล้วเรียกจากตัวลูกจะถูกสามารถเรียกใช้งานได้
    //protected ใช้ประกาศ Variable , Method ที่สามารถเรียกใช้ได้ใน Class นั้น ๆ และก็ Class ที่ extends (Inheritance)

    protected World world;//คือโลก
    protected PlayScreen screen;
    public Body b2bodye;//ร่างกาย
    public Vector2 velocity;
    public boolean destroyed;

    public Enemy(PlayScreen screen, float x,float y){//รูปร่างศัตรู ,x ,y ตำแหน่งศัตรู

        this.world =screen.getWorld();//getWorld อ่านค่า world จาก PlayScreen
        this.screen=screen;//จอภาพ

        setPosition(x,y);//เซ็ตค่าตำแหน่ง
        defineEnemy();    //กำหนดศัตรู
        velocity =new Vector2(-1,-2);//ความเร็ว
        b2bodye.setActive(false);//ร่างกายไม่ขยับอยู่
        destroyed=false;

    }
    //อันที่มี abstract คือเมธอทที่ต้องมี
    protected abstract void defineEnemy();//กำหนดค่าศัตรู
    public abstract void update(float dt);//อัพเดทเวลา   //update คือเมธอทที่สร้างขึ้นเองไว้อัพเดตผู้เล่นจาก คลาส PlayScreen
    public abstract void hitOnHead(Mario mario);//โดนมาริโอเหยียบหัว
    public abstract void hitFire(FireBall fireBall);//โดนไฟ
    public abstract void onEnemyHit(Enemy enemy);//โดนกระทบจากศัตรู
    public abstract void hitKickAss(KickAss kickAss);//โดนไฟ


    public void reverseVelocity(boolean x,boolean y){//reverseVelocity ความเร็วย้อนกลับ

        if(x)//ถ้าจริงให้วิ่งไปมา
            velocity.x= -velocity.x; //velocity.x เท่ากับวิ่งกลับ
        if(y)//ถ้าจริงให้วิ่งขึ้นลง
            velocity.y =-velocity.y; //velocity.y เท่ากับวิ่งกลับ

    }


    public  void hitOnCOIN(Mario mario){

    }
}

