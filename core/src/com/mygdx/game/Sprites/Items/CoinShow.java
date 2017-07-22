package com.mygdx.game.Sprites.Items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Enemies.Turtle;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Sprites.Skills.FireBall;
import com.mygdx.game.Sprites.Skills.KickAss;

/**
 * Created by arnat on 4/7/2560.
 */

public class CoinShow extends Enemy
{

    private float stateTime;//สถานะเวลา
    private Animation<TextureRegion> walkAnimation;//Animation แอนิเมชั่นจะจัดเก็บรายการออบเจ็กต์ที่แสดงลำดับภาพเคลื่อนไหวเช่น
    private Array<TextureRegion> frames; //Array<TextureRegion> เรียก updateTexture สำหรับแต่ละหน้าและเพิ่มพื้นที่ลงในอาร์เรย์ที่ระบุสำหรับเนื้อกระดาษแต่ละหน้า
    private boolean setToDestroy;//ตั้งค่าเพื่อทำลาย
    private boolean destroyed;//ทำลาย

    //คอนสทัคเตอ Enemy สิ่งต้องทั้งหมดจาก class Enemy
    public CoinShow(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        //ทำลูปเฟรม กุมบ้า ให้ animation
        for(int i = 0; i < 3; i ++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Coin_show"), i * 18, 0, 20, 20));

        //findRegion(รูป , ระยะ x ,ระยะ y ,จำนวนการแบ่ง คอลัม  ,จำนวนการแบ่ง แถว  )
        walkAnimation = new Animation(0.2f,frames); //0.4f ความเร็วต่อ frames การขยับเดิน
        stateTime = 0;
        setBounds(getX(),getY(),16/ GameFarmer.PPM, 16 / GameFarmer.PPM); //setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด, ก่อนที่จะใช้การปรับขนาดและการหมุน
        //  setBounds(getX(),getY(),ความกว้างของภาพ, ความสูงของภาพ)


        setToDestroy = false;
        destroyed = false;
    }

    //ปรับปรุงสถานะตอนนี้เป้นอย่งไร
    public  void  update(float dt){

        stateTime+= dt;
        //ถ้าเซ็ตค่าทำลาย และ ยังไม่โดนทำลาย
        if(setToDestroy && !destroyed){//ถ้าตั้งค่าทำลาย  และ ไม่โดนทำลาย

            world.destroyBody(b2bodye);//ทำลายร่างกายบนโลก
            destroyed = true;//โดนทำลาย



        }
        else if(!destroyed) {//ไม่โดนทำลาย

            b2bodye.setLinearVelocity(velocity);//สร้างร่างกายและความเร็ว


            setPosition(b2bodye.getPosition().x - getWidth() / 2, b2bodye.getPosition().y - getHeight() / 2);//ตำแหน่ง
            setRegion(walkAnimation.getKeyFrame(stateTime, true));//getKeyFrame ส่งกลับเฟรมตามเวลาที่เรียกว่าสถานะ(นี่คือจำนวนวินาทีที่ออบเจกต์ใช้ในสถานะที่อินสแตนซ์ Animation,โหมดระบุเป็นภาพเครื่อนไหวหรือไม่)
            //setRegion ตั้งค่าสถานะและพิกัดไปยังพื้นที่ที่ระบุ
        }
    }

    //กำหนดเหรียญ
    @Override
    protected void defineEnemy() {

        BodyDef bdef=new BodyDef();
        bdef.position.set(getX(),getY()); //ตำแหน่งเกิดของ Coin show
        bdef.type=BodyDef.BodyType.StaticBody;
        b2bodye=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();//FixtureDef คือกำหนดคำนิยาม สร้าง fixture
        CircleShape shape=new CircleShape();
        shape.setRadius(6 / GameFarmer.PPM);
        fdef.filter.categoryBits = GameFarmer.COIN_SHOW; //filter ติดต่อการกรองข้อมูล categoryBits บิตหมวดหมู่การชนกัน ปกติคุณจะตั้งค่าเพียงเ 1 บิต ของคุณ
        fdef.filter.maskBits=GameFarmer.MARIO_BIT | GameFarmer.GROUND_BIT | //maskBits บิตการชนกันของหน้ากาก ระบุประเภทที่รูปร่างนี้จะยอมรับสำหรับการชนกัน
                GameFarmer.OBJECT_BIT |
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT;

        fdef.shape=shape;

        fdef.isSensor = true;//กำหนดโดนแล้วไม่เด้ง //คือไม่ตอบสนองการชน

        b2bodye.createFixture(fdef).setUserData(this);



    }

    //วาด CoinShow
    public void draw(Batch batch){

        if(!destroyed || stateTime <1) //ถ้ายังไม่ลำลาย หรือ สถานะเวลา <1

            super.draw(batch); //วาด goomba

    }

    //เมื่อศัตรูโดน
    public void onEnemyHit(Enemy enemy){

        if (enemy instanceof Turtle && ((Turtle)enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestroy =true;
        else
            reverseVelocity(true,false);
    }

    @Override
    public void hitKickAss(KickAss kickAss) {
        setToDestroy =true;
        GameFarmer.manager.get("audio/sounds/coin.wav", Sound.class).play();

    }

    @Override
    public void hitOnCOIN(Mario mario) {
        Hud.addScore(200);//ได้คะแนน
        setToDestroy =true;
        GameFarmer.manager.get("audio/sounds/coin.wav", Sound.class).play();
    }

    //instanceofจะใช้สำหรับการตรวจสอบว่า ตัวแปรตัวนั้น instantiate หรือ new object ขึ้นมาจาก Class นั้นรึเปล่า
    // ดังนั้น instanceof จะเป็น operator ที่เราจะใช้ในการแยกว่า object ตัวนั้นเกิดจาก Class ใด



    //โดนเหยียบหัว
    @Override
    public void hitOnHead(Mario mario) {
        // setToDestroy =true;
        // GameFarmer.manager.get("audio/sounds/coin.wav", Sound.class).play();

    }
    //โดนไฟ
    public void hitFire(FireBall fireBall) {

        setToDestroy =true;
        GameFarmer.manager.get("audio/sounds/coin.wav", Sound.class).play();

    }

    public void hitByEnemy(Enemy enemy) {
        if(enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestroy = true;
        else
            reverseVelocity(true, false);
    }
}