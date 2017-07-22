package com.mygdx.game.Sprites.Skills;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by arnat on 4/7/2560.
 */

public class FireBall extends Sprite { //Sprite คือรูปภาพที่เราจะนำมาใช้ในเกมเป็นภาพของวัตถุ (Object) นำมากำหนดการกระทำหรือแสดงไว้เฉยๆเพื่อประกอบในเกม

    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    Animation<TextureRegion> fireAnimation;
    float stateTime;
    boolean destroyed;//ทำลาย
    boolean setToDestroy;//กำหนดไปทำลาย
    boolean fireRight;//ไฟไปขวา

    Body b2body;//ร่างกาย

    public FireBall(PlayScreen screen, float x, float y, boolean fireRight){//คอนสตัคเตอร์ FireBall รูป,ตำแหน่งx,ตำแหน่ง y, วิ่งไปซ้ายหรืขวา

        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();//ค่า word โลกใน PlayScreen สกีน
        frames = new Array<TextureRegion>();//ที่เก็บภาพแต่ละเฟรม
        for(int i = 0; i < 4; i++){ //สร้าง frames 4 frames

            //ในส่วน 0, 0, 4, 1 : 0, 0 สองตัวแรกคือระยะ x, y ของ TextureAtlas เริ่มจากตำแหน่ง 0, 0
            // ส่วน 4, 1 คือ จำนวนการแบ่ง คอลัม และ แถวนะครับ อย่างเช่น ภาพนี้ ถูกแบ่ง column = 4 และ row = 1 มันก็จะได้ดังภาพนี้
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
        }
        fireAnimation = new Animation(0.2f, frames);//fireAnimation ภาพเอนิเมชัน Animation เครื่อนไหว(เวลาเปลี่ยนภาพ,ภาพ)
        setRegion(fireAnimation.getKeyFrame(0));
        //setRegion เซ็ตรูปเป็น fireAnimation
        setBounds(x, y, 6 / GameFarmer.PPM, 6 / GameFarmer.PPM);
        // setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด
        //  setBounds(getX(),getY(),ความกว้างของภาพ, ความสูงของภาพ)
        defineFireBall();//กำหนดลูกไฟ
    }

    public void defineFireBall(){//กำหนดลูกไฟ

        // ใช้ world.setGravity(new Vector2(0, 0));//เซ็ตแรงดึงดูดของโลกได้

        //สร้าง บอดี้
        BodyDef bdef = new BodyDef();// BodyDef นิยามของร่างกายถือข้อมูลทั้งหมดที่จำเป็นในการสร้างร่างกายให้แข็ง
        bdef.position.set(fireRight ? getX() + 12 /GameFarmer.PPM : getX() - 12 /GameFarmer.PPM, getY());//ตำแหน่งปล่อยไฟ
        bdef.type = BodyDef.BodyType.DynamicBody;//ขนิดเคลื่อนที่ได้//KinematicBody แบบลอย

     //   bdef. gravityScale=0; แรงดึงดูดหรือแรงโน้มถ้วง
        if(!world.isLocked())//โลกถูกล็อก (อยู่ตรงกลางของช่วงเวลา) หรือไม่


            b2body = world.createBody(bdef);//สร้าง body ใน โลก

        //Fixture ร่างกายที่จับต้องได้
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();//สร้างรูปร่างทรงกลม
        shape.setRadius(3 / GameFarmer.PPM); //ขนาดลูกบอล
        fdef.filter.categoryBits = GameFarmer.FIREBALL_BIT;//categoryBits บิตหมวดหมู่การชนกัน กำหนดส่วน = GameFarmer.MARIO_BIT
        fdef.filter.maskBits = GameFarmer.GROUND_BIT |//maskBits คือบิตการชนกัน,ระบุประเภทที่รูปร่างนี้จะยอมรับสำหรับการชนกัน
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT |
                GameFarmer.ENEMY_BIT |
                GameFarmer.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;//ค่าการเด้ง
        fdef.friction = 0;//แรงเสียดทาน
        b2body.createFixture(fdef).setUserData(this);//createFixture สร้างร่างกายที่จับต้องได้ ตามคุณสมบัติ fdef
        b2body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 1.5f)); //กำหนดการเคลื่อนไหวของไฟ//setLinearVelocity กำหนดความเร็วเชิงเส้น
        //new Vector2(fireRight ? 2 ความเร็ววิ่งขวา : -2 ความเร็ววิ่งซ้าย, 1.5f ตำแหน่งความสูง));


    }

    public void update(float dt){
        stateTime += dt; //stateTime เวลาแสดงไฟ จะเพิ่มขึ้นเรือย ๆ

        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        //setRegion เซ็ตรูปเป็น fireAnimation ตาม getKeyFrame(จำนวนวินาทีที่ออบเจกต์ใช้ในสถานะ, โหมดระบุเป็นภาพเครื่อนไหวหรือไม่)
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);//เซ็ตตำแหน่ง ร่างกาย

        if((stateTime > 3 || setToDestroy) && !destroyed) {// (ถ้าเวลาเเกิน >3 หรือ || กำหนดไปทำลาย) && ไม่ได้ทำลาย

            world.destroyBody(b2body);//destroyBody ทำลายร่างกายแข็งให้คำนิยาม

            destroyed = true;
        }
        //กำหนดไม่ให้เด้งมากเกินไป
        if(b2body.getLinearVelocity().y > 2f)//ถ้าตำแหน่งแกน Y > มากกว่า 2
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);//ก็กำหนดแกน y = 2

        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))//ถ้าไฟพ่นไปทางซ้ายหรือ || ไฟพ่นไปทางขวา

            setToDestroy();//กำหนดไปทำลายไฟจะหาย
    }

    //ไฟจะหาย
    public void setToDestroy(){

        setToDestroy = true; //ตั้งค่าเพื่อทำลาย
    }

    //ไฟถูกทำลาย
    public boolean isDestroyed(){

        return destroyed;
    }



}
