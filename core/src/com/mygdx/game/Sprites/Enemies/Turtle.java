package com.mygdx.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Sprites.Skills.FireBall;
import com.mygdx.game.Sprites.Skills.KickAss;

/**
 * Created by arnat on 4/7/2560.
 */

public class Turtle extends Enemy {
    public static final int KICK_LEFT_SPEED = -2; //แตะไปทางซ้าย
    public static final int KICK_RIGHT_SPEED = 2;//แตะไปทางชวา
    //enum คือ ข้อมูลบางชนิดที่มีค่าเป็นไปได้เพียงไม่กี่ค่า เช่น ข้อมูล เพศ, สี, ระดับคะแนน
    public enum State{WALKING, STANDING_SHELL,MOVING_SHELL,DEAD}
    public State currentState;//สถานะปัจจุบัน
    public State previousState;//สถานะก่อนหน้า

    private float stateTime;//สถานะ เวลา
    private Animation<TextureRegion> walkAnimation; //เดินเอนิเมชัน
    private Array<TextureRegion> frames;//เฟรม
    private TextureRegion shell;//กระดอง
    private float deadRotationDegrees;//ตายองศาหมุน
    private boolean setToDestroy;
    private boolean destroyed;//ทำลาย

    public Turtle(PlayScreen screen, float x, float y) {//คอนสตัตเตอ ภาพ เต่า ,ตำแหน่ง x ,ตำแหน่ง y

        super(screen, x, y);

        frames = new Array<TextureRegion>();//สร้างออฟเจค เฟรมอาเรย์
        //เพิ่มเฟรมภาพเต่า ตาม TextureRegion
        //getAtlas ส่งกลับ TextureAtlas ผ่านไปยัง screen
        //findRegion แสดงพื้นที่แรกที่พบด้วยชื่อที่ระบุ
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),0,0,16,24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),16,0,16,24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"),64,0,16,24);
        // .findRegion("turtle") แสดงพื้นที่แรกที่พบโดยใช้ชื่อที่ระบุ วิธีนี้ใช้การเปรียบเทียบสตริงเพื่อค้นหาพื้นที่ดังนั้นควรเก็บแคชไว้มากกว่าเรียกวิธีนี้หลายครั้ง
        walkAnimation = new Animation(0.2f,frames);//เต่าเดิน
        currentState = previousState = State.WALKING;//สถานะปัจจุบัน = สถานะก่อนหน้านี้ = State.WALKING

        deadRotationDegrees =0;//เต่าหมุนเมื่อตาย

        // setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด
        setBounds(getX(),getY(),16 / GameFarmer.PPM, 24 / GameFarmer.PPM);
    }


    @Override
    //กำหนดศัตรูเต่า
    protected void defineEnemy() {
        BodyDef bdef=new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type=BodyDef.BodyType.DynamicBody;
        b2bodye=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shapeturtle=new CircleShape();
        shapeturtle.setRadius(6 / GameFarmer.PPM);
        fdef.filter.categoryBits = GameFarmer.ENEMY_BIT;
        fdef.filter.maskBits=GameFarmer.GROUND_BIT |
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT|
                GameFarmer.ENEMY_BIT |
                GameFarmer.OBJECT_BIT|
                GameFarmer.MARIO_BIT |
                GameFarmer.FIREBALL_BIT|
                GameFarmer.KICKASS_BIT;

        fdef.shape=shapeturtle;
        b2bodye.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[]vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,9).scl(1 / GameFarmer.PPM);
        vertice[1] = new Vector2(5,9).scl(1 / GameFarmer.PPM);
        vertice[2] = new Vector2(-3,3).scl(1 / GameFarmer.PPM);
        vertice[3] = new Vector2(3,3).scl(1 / GameFarmer.PPM);
        head.set(vertice);

        fdef.shape = head;
        //restitution การชดใช้ (ความยืดหยุ่น) โดยปกติจะอยู่ในช่วง [0,1]
        fdef.restitution = 1.8f;//กระเด็น
        fdef.filter.categoryBits = GameFarmer.ENEMY_HEAD_BIT;
        b2bodye.createFixture(fdef).setUserData(this);

    }

    public void onEnemyHit(Enemy enemy){//เมื่อศัตรูโดน
        //ถ้าเป็น enemy เป็นชื่อของ object และ Turtle เป็นชื่อของ class
        //  instanceof ใช้ตรวจสอบว่า object ถูกสร้างมาจาก class หรือไม่
        if (enemy instanceof  Turtle){//ถ้า enemy ถูกสร้างมาจาก Turtle

            //ถ้าเต่าที่โดนโจมตีสถานะเท่ากับวิ่ง &&และ เต่าทั่วไปไม่วิ่ง
            if (((Turtle)enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL){//ถ้าสถานะปัจจุบัน enemy เต่า=MOVING_SHELL && สถานะปัจจุบัน !=MOVING_SHELL

                killed();//สังหารเต่าทั่วไป

                //หรือถ้า เต่าทั่วไปวิ่งชน กับ เต่าที่โดนโจมตีเดิน
            }else if ( currentState == State.MOVING_SHELL && ((Turtle)enemy).currentState == State.WALKING )

                return;

            else//ถ้าไม่ทั้งหมด
                reverseVelocity(true,false);//reverseVelocity ความเร็วย้อนกลับไปมา

        }
        else if(currentState != State.MOVING_SHELL) //หรือถ้า สถานะปัจจุบัน != วิง State.MOVING_SHELL

            reverseVelocity(true,false);


    }

    @Override
    public void hitKickAss(KickAss kickAss) {
        //เต่าตาย
        currentState =State.DEAD;//สถานะตาย
        Filter filter =new Filter();
        filter.maskBits = GameFarmer.NOTHING_BIT;

        for (Fixture fixture : b2bodye.getFixtureList())
            fixture.setFilterData(filter);
        b2bodye.applyLinearImpulse(new Vector2(0, 5f), b2bodye.getWorldCenter(),true);


    }


    //อ่านภาพแต่ เฟรม ตามเวลา
    public  TextureRegion getFrame(float dt){

        TextureRegion region;//สร้าง ตัวแปรภาพ

        //ขึ้นอยู่กับสถานะคีย์เฟรมภาพเคลื่อนไหวที่ตรงกัน

        switch (currentState){//ตรวจสถานะเป็นอะไร
            case STANDING_SHELL: //กระดองอยู่นิ่ง
            case MOVING_SHELL://กระดองวิ่ง

                region = shell;//กำหนดภาพเป็นกระดอง
                break;
            case WALKING:
            default://ถ้าไม่มีทั้งหมด

                region = walkAnimation.getKeyFrame(stateTime,true);//กำหนดภาพเป็นเต่าเดิน //เซ็ต ส่งกลับเฟรมตามเวลาสถานะ stateTimer เวลาแสดง

                break;

        }

        // velocity ความเร็ว
        //ถ้าเดินไปทางซ้ายแล้วชน ให้หันกับ
        if (velocity.x > 0 && !region.isFlipX()){//!region.isFlipX ภาพไม่ได้พลิก isFlipX คือหันภาพแกน x
            region.flip(true,false);//flip(boolean x ดำเนินการพลิกแนวนอน,boolean y ดำเนินการพลิกแนวตั้ง)

        }
        //ถ้าเดินไปทางขวาแล้วชน ให้หันกับ
        if (velocity.x < 0 && region.isFlipX()){
            region.flip(true,false);

        }
        // stateTimer = (สถานะปัจจุบัน == สถานะก่อนหน้านี้ ถ้าจิง ? ก็เท่ากับ stateTimer + dt ถ้าไม่ ก็เท่ากับ 0 )
        stateTime = currentState == previousState ? stateTime + dt : 0;
        // ปรับปรุงสถานะก่อนหน้า
        previousState = currentState;

        return region;//ส่งค่ากลับเฟรมสุดท้ายของเรา

    }

    @Override
    public void update(float dt) {

        //กำหนดสร้างภาพ
        setRegion(getFrame(dt));// setRegion กำหนดภาพ (ดึงเฟรมภาพ getFrame(ตามเวลา dt))

        //ถ้า กระดองเต่าอยู่นิ่งเกิน 5 วินาที
        //ถ้า สถานะปัจจุบัน == กระดองอยู่นิ่ง &&และ เวลาปัจจุบัน > 5
        if (currentState == State.STANDING_SHELL && stateTime > 5){

            currentState = State.WALKING; //กำหนดให้มันตื่นเดิน

            velocity.x = 1;//กำหนดความเร็ว แกร x เมื่อตื่น
        }

        //กำหนดตำแหน่ง ตัวเต่า
        setPosition(b2bodye.getPosition().x - getWidth() / 2, b2bodye.getPosition().y -8 / GameFarmer.PPM);


        //ถ้าสถนะปัจจุบัน = ตาย
        if (currentState == State.DEAD){
            deadRotationDegrees +=2;//เต่าหมุนเมื่อตาย = เต่าหมุนเมื่อตาย +2
            rotate(deadRotationDegrees);//ตั้งค่าการหมุนของสไปรท์เทียบกับการหมุนปัจจุบัน//rotate หมุน

            if (stateTime > 5 && !destroyed){//ถ้าเวลาเกิน 5 วินาที และ ไม่ได้กำจัด
                world.destroyBody(b2bodye);//ทำลายร่างกายออกจาก world
                destroyed = true;//ทำลาย

            }
        }
        else//ไม่ตาย
            b2bodye.setLinearVelocity(velocity);//setLinearVelocity ตั้งค่าความเร็วเชิงเส้นของศูนย์กลางของมวลร่าง
    }


    @Override
    public void hitOnHead(Mario mario) {//hitOnHead โดนโจมตีหัว

        if (currentState != State.STANDING_SHELL){//ถ้าสถานะไม่อยู่นิ่งในกระดอง
            currentState = State.STANDING_SHELL;//ให้อยู่ในกระดอง
            velocity.x =0;//ความเร็ว0
        }else {//ถ้าอยู่ในกระดอง
            kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);//คำสั่งแตะ
            // kick(ตำแหน่งx มาริโอ <= ตำแหน่งx เต่า ถ้าจิง ? แตะไปทางขวา :ถ้าไม่ แตะไปทางซ้าย )
        }

    }


    @Override
    public void hitFire(FireBall fireBall) {//โดนโจมตีด้วยไฟ

        //เต่าตาย
        currentState =State.DEAD;//สถานะตาย
        Filter filter =new Filter();
        filter.maskBits = GameFarmer.NOTHING_BIT;

        for (Fixture fixture : b2bodye.getFixtureList())
            fixture.setFilterData(filter);
        b2bodye.applyLinearImpulse(new Vector2(0, 5f), b2bodye.getWorldCenter(),true);

    }
    //กำหนดวาด
    public void draw(Batch batch){

        if (!destroyed)
            super.draw(batch);
    }

    //กำหนดเตะ
    public void kick(int speed){

        velocity.x =speed;
        currentState = State.MOVING_SHELL;
    }
    public State getCurrentState(){//อ่านสถานะปัจจุบัน

        return currentState;
    }



    public void killed(){//เต่าโดนฆ่า

        currentState =State.DEAD;
        Filter filter =new Filter();
        filter.maskBits = GameFarmer.NOTHING_BIT;

        for (Fixture fixture : b2bodye.getFixtureList())
            fixture.setFilterData(filter);
        b2bodye.applyLinearImpulse(new Vector2(0, 5f), b2bodye.getWorldCenter(),true);


    }


    public void hitByEnemy(Enemy enemy) {

        reverseVelocity(true, false);
    }
}
