package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Enemies.Turtle;
import com.mygdx.game.Sprites.Skills.FireBall;
import com.mygdx.game.Sprites.Skills.KickAss;

/**
 * Created by arnat on 4/7/2560.
 */

//ตัวละคร มาริโอ้
public class Mario extends Sprite {




    public enum State{FALLING, JUMPING ,STANDING ,RUNNING,GROWING,DEAD,FIREPUT,NEXTLEVEL2}//สถานะรูปภาพ
    public State currentState;//currentState สถานะปัจจุบัน
    public State previousState;//previousState สถานะก่อนหน้านี้

    public World world; // ขึ้นมาก่อน เนื่องจาก World นั้นคืออ็อปเจ็ค ที่เป็นเหมือนศูนย์รวมคอยจัดการ memory, objects รวมถึงการ simulation ต่างๆ การสร้าง World ทำได้ด้วยคำสั่งนี้
    public Body b2body;//ล่างมาริโอ
    //TextureRegion คือตำแหน่งและขนาดของภาพบน หรือง่ายกำหนดเฟรมรูปภาพ (กล่าวคือการกำหนดภาพไว้สำหรับ animation)
    private TextureRegion marioStand;
    private TextureRegion marioFire;
    private Animation<TextureRegion> marioRun; //ใว้เก้บเฟรมภาพเคลื่อนไหว
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioFire;
    private TextureRegion bigMarioJump;
    private Animation <TextureRegion> bigMarioRun;
    private Animation <TextureRegion> growMario;


    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead,marioLavel2;
    private boolean marioIsFire;

    private PlayScreen screen;
    private Array<FireBall> fireballs;//ประกาศตัวแปร
    private Array<KickAss> kickAsses;//ประกาศตัวแปร

    public Mario( PlayScreen screen){ //คอนสตัคเตอ Mario เอาส่วน PlayScreen มาใช้

        fireballs = new Array<FireBall>(); //สร้างออฟเจคอาเรย์เฟรมไฟ
        kickAsses = new Array<KickAss>(); //สร้างออฟเจคอาเรย์เฟรมแตะ
        //  super(screen.getAtlas().findRegion("little_mario"));

        // ค่าเริ่มต้นดีฟอลต์
        this.screen = screen;
        this.world=screen.getWorld();//อ่านค่า screen.getWorld มาใว้ในนี้ =world
        //world คือสิ่งที่ต่าง opject ที่อยู่บนโลก

        currentState =State.STANDING; //currentState สถานะปัจจุบัน //STANDING ยืน
        previousState =State.STANDING;//previousState สถานะก่อนหน้านี้
        stateTimer=0;////stateTime สถานะเวลา
        runningRight=true; //วิ่งขวา

        Array<TextureRegion> frames =new Array<TextureRegion>(); // เก็บ TextureRegion เป็น อาเรย์ frames ใว้สำหรับสร้างภาพเคลื่อนไหว

        //รับภาพเคลื่อนไหววิ่งและเพิ่มภาพเคลื่อนไหว
        //มาริโอวิ่ง
        for(int i = 1; i < 4;i++)//ลูปภาพไว้นำไปใส่ marioRun
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16,0,16,16));
        //findRegion(รูป , ระยะ x ,ระยะ y ,จำนวนการแบ่ง คอลัม  ,จำนวนการแบ่ง แถว  )

        //16,0,16,16// 16 คือระยะแกน x ความกว้างตำแหน่งภาพถัดไป ปัจจุบัน 16 ถึงล่าสุด 32 //0,คือ แกน y ความสูง

        // ตัวอย่าง ในส่วน 0, 0, 4, 1 // 0, 0 สองตัวแรกคือระยะ x, y ของ TextureAtlas เริ่มจากตำแหน่ง 0, 0
        // ส่วน 4, 1 คือ จำนวนการแบ่ง คอลัม และ แถวนะครับ อย่างเช่น ภาพนี้ ถูกแบ่ง column = 4 และ row = 1 มันก็จะได้ดังภาพนี้

        //column = 16 คือระยะ ความกว้างตัวละครขนาด และ row = 16 คือระยะความสูงตัวละคร
        marioRun=new Animation(0.1f, frames);//marioRun ภาพเคลื่อนไหว (ความเร็วต่อเฟรม,เฟรม)
        //Animation (เวลาระหว่างเฟรมเป็นวินาที,วัตถุที่เป็นตัวแทนของเฟรม //การเก็บเฟรมเฟรมและเฟรมหลัก array เฟรม)

        frames.clear();//ล้าง frames ให้ว่าง

        //มาริโอวิ่ง

        //มาริโอใหญ่วิ่ง
        for(int i = 1; i < 4;i++)

            //รับภาพเคลื่อนไหววิ้ง
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16,0,16,32));
        bigMarioRun=new Animation(0.1f, frames);//ต่อเฟรม
        frames.clear();
        //มาริโอใหญ่วิ่ง

        //รับภาพเคลื่อนไหวเติบโตมาริโอ
        //เติบโตมาริโอ
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));

        growMario=new Animation(0.2f,frames);
        //เติบโตมาริโอ

        // รับกรอบภาพเคลื่อนไหวกระโดดและเพิ่มพวกเขาไปที่ภาพเคลื่อนไหว marioJump
        marioJump=new TextureRegion(screen.getAtlas().findRegion("little_mario"),80 ,0 ,16,16);
        bigMarioJump=new TextureRegion(screen.getAtlas().findRegion("big_mario"),80 ,0 ,16,32);

        //สร้างเนื้อที่สำหรับยืนมาริโอ
        marioStand =new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);//มาริโอตัวเล็ก
        marioFire=new TextureRegion(screen.getAtlas().findRegion("little_mario"),16 ,0 ,16,16);

        bigMarioStand=new TextureRegion(screen.getAtlas().findRegion("big_mario"),0 ,0 ,16,32);
        bigMarioFire=new TextureRegion(screen.getAtlas().findRegion("big_mario"),16 ,0 ,16,32);
        marioDead =new TextureRegion(screen.getAtlas().findRegion("little_mario"),96 ,0 ,16,16);


        defineMario();   //กำหนดเริ่มมาริโอเล็ก
        // ตั้งค่าเริ่มต้นสำหรับ marios ตำแหน่งความกว้างและความสูง และเฟรมเริ่มต้นเป็น marioStand

        // setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด
        //คาสั่ง setBounds(int x, int y, int width, int height)
        setBounds(0,0,16 / GameFarmer.PPM, 16 / GameFarmer.PPM);
        setRegion(marioStand);//setRegion กำหนดภาพ สร้างภาพ texture



    }

    // เวลาที่ใช้ในการ rander ในแต่ละเฟรม โดยการเปรียบเทียบเวลาจากการเรียกฟังชั่น Update ของเฟรมก่อนหน้ากับเฟรมปัจจุบัน
    //update คือเมธอทที่สร้างขึ้นเองไว้อัพเดตผู้เล่นจาก คลาส PlayScreen
    public  void update(float dt){//dt อัพเดตเวลา delta = ค่าความแตกต่าง  time  = เวลา

        if (marioIsBig)//if มาริโอ ตัวใหญ่
            setPosition(b2body.getPosition().x - getWidth() / 2 ,b2body.getPosition().y - getHeight()/2 - 6 / GameFarmer.PPM);//พอตัวใหญ่ต้องดึงลงมาหน่อย - 6 / GameFarmer.PPM //กึ่งไปอยู่วงกลมอันบน
            //setPosition กำหนดตำแหน่งที่จะวาดสไปรท์ (ตำแน่งที่จะวาดแกน x, ตำแหน่งที่จะวาดแกน y)
        else //ถ้าไม่
            setPosition(b2body.getPosition().x - getWidth() / 2 ,b2body.getPosition().y - getHeight()/2);//ก็กำหนดค่าตำแหน่งเดิม

        //กำหนดภาพตามสถานะของภาพ
        setRegion(getFrame(dt));// setRegion กำหนดภาพ (อ่านเฟรมภาพ getFrame อ่านค่า region(ตามเวลา dt))

        if(timeToDefineBigMario)//ได้เวลามาริโอใหญ่
            defineBigMario(); //กำหนดมาริโอใหญ่

        if (timeToRedefineMario)//ได้เวลามาริโอย่อตัวเล็ก
            redefineMario(); //กำหนดมาริโอย่อตัวเล็ก

        for(FireBall  ball : fireballs) {//สำหรับพ่นไฟ ถ้า fireballs มีค่าให้สร้างนำมาใส่ใน ball
            ball.update(dt);//สร้างบอลตามเวลา dt


            if(ball.isDestroyed())//ถ้า ไฟถูกทำลาย จริง
                fireballs.removeValue(ball, true); //removeValue ลบอินสแตนซ์แรกของค่าที่ระบุในอาร์เรย์ //true = จะถูกใช้ false =จะถูกนำมาใช้
        }
        for(KickAss  kick : kickAsses) {//สำหรับพ่นไฟ ถ้า kickAsses มีค่าให้สร้างนำมาใส่ใน ball
            kick.update(dt);//สร้างบอลตามเวลา dt

            if(kick.isDestroyed())//ถ้า ไฟถูกทำลาย จริง
                kickAsses.removeValue(kick, true); //removeValue ลบอินสแตนซ์แรกของค่าที่ระบุในอาร์เรย์ //true = จะถูกใช้ false =จะถูกนำมาใช้
        }

    }
    //อ่านภาพแต่ เฟรม ตามสถานะ
    public TextureRegion getFrame(float dt){ //รูปภาพ

        // รับมาริโอสถานะปัจจุบัน กล่าวคือ กระโดด, วิ่ง, ยืน ..
        currentState = getState();//อ่านสถานะปัจจุบัน

        TextureRegion region;//สร้าง ตัวแปรภาพ

        //เช็คสถานะปัจจุบันเพื่อวาดรูปตามสถานะ
        switch (currentState){//ตรวจสถานะเป็นอะไร
            case DEAD://ตาย
                region =marioDead;//เซ็ตเป็นตาย
                break;
            case GROWING://กำลังโต
                region=growMario.getKeyFrame(stateTimer);//เซ็ต ภาพเฟรมตามเวลาสถานะ stateTimer เวลาแสดง
                //กำหนดเล่นเฟรมเติบโต
                //Gdx.app.log("MARIO","stateTimer"+stateTimer);
                //growMario ภาพเคลื่อนไหวเสร็จสิ้นหรือไม่(ตามเวลาแสดง)
                if (growMario.isAnimationFinished(stateTimer)) { //isAnimationFinished ไม่ว่าภาพเคลื่อนไหวจะเสร็จสิ้นหรือไม่หากเล่นโดยไม่วนรอบ(เวลาแสดง)
                    runGrowAnimation = false;//ให้มาริโอกำลังเติบโตเป็น false
                }
                break;
            case JUMPING://กำลังกระโดด
                //โอเปอเรเตอร์ ?: เป็นโอเปอเรเตอร์แบบ ternary ซึ่งต้องการ operands สามตัว มีลักษณะการใช้ดังนี้ expa ? expb : expc;
                //expa เป็น true ตัว expb จะถูกประมวณผล แต่ถ้ารีเทอร์นค่า false ล่ะก็ expc จะถูกประมวณผล แทน
                region =  marioIsBig ? bigMarioJump : marioJump;

                break;
            case RUNNING://กำลังวิ่ง
                //getKeyFrame อ่านภาพตามเวลา (เวลาที่ใช้ในสถานะที่แสดงโดยภาพเคลื่อนไหวนี้,สั่งให้วนลูปภาพซ้ำ กล่าวคือเล่น annimation วนไป)

                region =  marioIsBig ? bigMarioRun.getKeyFrame(stateTimer,true) : marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case FIREPUT:
                region =  marioIsBig ? bigMarioFire : marioFire;
                marioIsFire = false;
                break;
            case STANDING:
            default://คือยินเฉยๆ
                region=marioIsBig ? bigMarioStand : marioStand;
                break;


        }
        // ถ้ามาริโอกำลังวิ่งอยู่ด้านซ้ายและร่างกายไม่ได้หันไปทางซ้าย ... พลิกมัน

        //(getLinearVelocity รับความเร็วเชิงเส้นของศูนย์กลางของมวลของ b2body แกนx < 0 || หรือ ไม่วิ่งไปทางขวา !runningRight )
        // && //คือถ้าอันเท็จก็เท็จหมด// และ !region.isFlipX ภาพไม่ได้พลิก isFlipX คือหันภาพแกน x
        if((b2body.getLinearVelocity().x < 0 || !runningRight)&& !region.isFlipX()) {//ดำเนินการพลิกแนวนอน

            //flip ตรวจสอบ x,y ถ้าไม่ได้ตั้งก็ทำการพลิก
            region.flip(true, false);//flip(boolean x ดำเนินการพลิกแนวนอน,boolean y ดำเนินการพลิกแนวตั้ง)
            runningRight = false;//runningRight false คือพลิกซ้าย
        }
        // ถ้ามาริโอกำลังวิ่งขวาและร่างกายไม่ได้หันขวา ... พลิกมัน
        else if((b2body.getLinearVelocity().x > 0|| runningRight)&& region.isFlipX()) {


            region.flip(true, false);
            runningRight = true;//runningRight false คือพลิกขวา
        }


        //currentState เปลี่ยบเท่า State.STANDING //previousState ก็เช่นกัน
        // stateTimer = (สถานะปัจจุบัน == สถานะก่อนหน้านี้ ถ้าจิง ? ก็เท่ากับ stateTimer + dt ถ้าไม่ ก็เท่ากับ 0 )
        //เพิ่ม stateTimer + dt เพื่อให้เกิดการเปลี่ยนภาพแต่ละเฟรม ที่ส่งค่า 0 คือ
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        // ปรับปรุงสถานะ นำด่าสถานะปัจจุบันมาเป็นก่อนหน้านี้
        previousState = currentState;

        // เฟรมสุดท้ายของเรา
        return region;//ส่งค่ากลับเฟรมภาพ
    }

    //อ่านสถานะมาริโอ
    public State getState(){

        //สถานะมาริโอ้


        if (marioIsDead)//ถ้ามาริโอตาย
            return State.DEAD;//ส่งค่าสถานะตาย
        else if (marioLavel2)//หรือถ้ามาริโอเลวเล2
            return State.NEXTLEVEL2;//ส่งค่าสถานะลวเล2

        else if (runGrowAnimation)//หรือถ้ามาริโอกำลังโต
            return State.GROWING;//ส่งค่าสถานะโต

            //ถ้ามาริโอกระโดด
            //.getLinearVelocity() รับความเร็วเชิงเส้น
            //ถ้ามาริโอแกน y มากกว่า 0 || (มาริโอแกน y น้อย 0 และ สถานะเก่า = กระโดด)
        else if(b2body.getLinearVelocity().y > 0||(b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;//ส่งค่าสถานะกระโดด

            //ถ้ามาริโอล่น
            //ถ้าค่า Y-Axis mario ลดลง
        else if(b2body.getLinearVelocity().y < 0)//ล้ม
            return State.FALLING;//ล้ม

            //ถ้ามาริโอกำลังวิ่งอยู่
            //ถ้ามาริโอเป็นบวกหรือลบในแกน x ขาจะต้องกำลังวิ่งอยู่
        else if(b2body.getLinearVelocity().x !=0)
            return State.RUNNING;

            //ถ้ามาริโอพ่นไฟ
        else if (marioIsFire)//ถ้ามาริโอพ่นไฟ
            return State.FIREPUT;//ส่งค่าสถานะพ่นไฟ

            //ถ้าไม่มีเหล่านี้กลับมาแล้วเขาจะต้องยืนอยู่
        else
            return State.STANDING;


    }
    //เติบโต
    public void grow(){

        if( !isBig() ) {//ถ้ามาริโอไม่ใหญ่อยู่
            //ก็เซ็ตค่าให้มันใหญ่
            runGrowAnimation = true;//มาริโอกำลังโต
            marioIsBig = true;//มาริโอ ตัวใหญ่
            timeToDefineBigMario = true;//ได้เวลามาริโอใหญ่
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);// setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด
            GameFarmer.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        }
    }
    public void downdie() {//มาริโอตาย
        marioIsDead = true;//ก็ให่มาริโอตาย
        GameFarmer.manager.get("audio/music/mario_music.ogg", Music.class).stop();
        GameFarmer.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
        marioIsDead = true;//มาริโอตาย
        //set ตอนมาริโอตายให้ทำไร
        Filter filter = new Filter();
        filter.maskBits = GameFarmer.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);


        die();
    }

    public void Level2() {//มาริโอตาย

        marioLavel2 = true;//ก็ให่มาริโอตาย
        GameFarmer.manager.get("audio/music/mario_music.ogg", Music.class).stop();
        GameFarmer.manager.get("audio/sounds/mariodie.wav", Sound.class).play();

        //set ตอนมาริโอตายให้ทำไร
        Filter filter = new Filter();
        filter.maskBits = GameFarmer.NOTHING_BIT;
        for (Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);



    }
    public void die() {//มาริโอตาย

        if (!isDead()) {//ถ้ามาริโอไม่ตายอยู่

            GameFarmer.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            GameFarmer.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;//ก็ให่มาริโอตาย
            Filter filter = new Filter(); //filter ข้อมูลนี้มีข้อมูลการกรองข้อมูลติดต่อ

            //maskBits คือบิตการชนกัน,ระบุประเภทที่รูปร่างนี้จะยอมรับสำหรับการชนกัน
            filter.maskBits = GameFarmer.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {//getFixtureList ดูรายการอุปกรณ์ทั้งหมดที่แนบมากับร่างกาย b2body นี้ เก็บไว้ใน fixture
                fixture.setFilterData(filter);//fixture มาตั้งค่าตรวจสอบการชนใน filter
                // setFilterData ตั้งค่าข้อมูลการกรองที่อยู่ติดต่อ
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);// เมื่อตายจะกระโดดขึ้น
            //Vector2 คือ การแสดงตำแหน่งแบบ 2D ด้วยจุด x และ y โดยที่ up คือตัวแปรแบบ Static ที่จะสามารถทำให้เราเขียนโปรแกรมได้สะดวกและสั้นลง
            // b2body.applyLinearImpulse(แรงกระตุ้น,จุด - ตำแหน่งโลกของจุดของโปรแกรมประยุกต์,ปลุก-ขึ้นร่างกาย)
            /*down คือ Vector2(0, -1)
                up คือVector2(0, 1)
                left  คือ Vector2(-1, 0)
                right คือ Vector2(1, 0)
                zero คือ Vector2(0, 0)

               getWorldCenter รับตำแหน่งโลกของศูนย์กลางมวลชน
            */
            // โดยมีค่าแรงโน้มถ่วง(แรง g) อยู่ที่ 4f คือ(แรง g) แต่ว่าทำไมเป็นค่าลบ (-) เนื่องจาก LibGDX นับแกน x,y โดยเริ่มจากมุมซ้ายล่างคือจุด 0, 0
            // applyLinearImpulse ใช้ตามแรงกระตุ้นใช้แรงกระตุ้นที่จุด นี้ทันทีปรับความเร็ว


        }
    }

    public boolean isDead(){//ตรวจสอบมาริโอตายหรือไม่

        return marioIsDead;

    }
    public float getStateTimer(){//อ่านสถานะเวลา

        return stateTimer;

    }
    public boolean isBig(){//ตรวจสอบมาริโอใหญ่หรือไม่

        return marioIsBig;
    }


    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }
    //โดนโจมตี
    public void hit(Enemy enemy){//ศัตรูโจมตี

        // instanceof ใช้ตรวจสอบว่า object ถูกสร้างมาจาก class หรือไม่


        // instance หมายความว่า “ออบเจ็กต์” และ “อินสแตนซ์” คือสิ่งเดียวกัน
        // Subclass คือ class ย่อย โดยได้ Attribute และ Function จากClasss หลัก(Superclass)และสามารถเขียน Function


        //โดยที่ enemy เป็นชื่อของ object และ Turtle เป็นชื่อของ class &&  ไปที่ Turtle ถูกโจมตี getCurrentState อยู่ที่สถานะ == STANDING_SHELL เป็นกระดอง
        if (enemy instanceof Turtle && ((Turtle)enemy).getCurrentState()== Turtle.State.STANDING_SHELL){

            //แตะเต่าไปทาง (ถ้าตำแหน่งผู้<=ตำแหน่งเต่า ? true แตะเต่าไปขวา : false แตะเต่าไปทางซ้าย )
            ((Turtle)enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);

            //xมาริโอ <= xเต่า
        }else {//ถ้าไม่แปลยังเดินอยู่


            if (marioIsBig) {//ถ้ามาริโอ้ใหญ่
                // ให้ย่อตัวเล็ก
                marioIsBig = false;//มาริโอ้ไม่ใหญ่
                timeToRedefineMario = true;//ได้เวลามาริโอย่อตัวเล็ก
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);// setBounds ตั้งค่าตำแหน่งและขนาดของสไปรต์เมื่อวาด
                GameFarmer.manager.get("audio/sounds/powerdown.wav", Sound.class).play();

            } else {//ถ้า่ไม่ใหญ่
                //ให้มาริโอตาย
                GameFarmer.manager.get("audio/music/mario_music.ogg", Music.class).stop();
                GameFarmer.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;//มาริโอตาย
                //set ตอนมาริโอตายให้ทำไร
                Filter filter = new Filter();
                filter.maskBits = GameFarmer.NOTHING_BIT;
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);

                die();
            }
        }
    }




    //กำหนดมาริโอตัวย่อเล็ก
    public void redefineMario(){

        Hud.setnameskill("kick");
        Vector2 position =b2body.getPosition();  //Vector2 กำหนดแรงดึงดูด ทั้งแกน x และ y
        world.destroyBody(b2body);//destroyBody ทำลาย b2body เก่า

        // สร้างลูกบอล
        //สร้าง BodyDef
        //Def เปรียบสเหมือน Default คือค่าเรื่มต้น
        BodyDef bdef=new BodyDef();// BodyDef นิยามของร่างกายถือข้อมูลทั้งหมดที่จำเป็นในการสร้างร่างกายให้แข็ง
        bdef.position.set(position); //ตำแหน่งเกิดของมาริโอ// กำหนดตำแหน่งให้กับ.set( Body x , y )
        bdef.type=BodyDef.BodyType.DynamicBody;// ชนิดของ Body เป็น DynamicBody คือเป็นวัตถุที่สามารถเคลื่อนที่ได้
        // สร้าง Body
        b2body=world.createBody(bdef); // สร้าง Body
        //สร้าง FixtureDef
        FixtureDef fdef=new FixtureDef();// FixtureDef ขึ้นมาก่อน สำหรับกำหนดคุณสมบัติให้กับวัตถุ และค่อยใส้ shape
        // สร้าง Shape(รูปทรงต่างๆของวัตถุ)
        CircleShape shapewaist=new CircleShape();//CircleShape คลาสแสดงวัถุทรงกลม
        shapewaist.setRadius(6 / GameFarmer.PPM);//setRadius มีขนาดรัศมี 6 / GameFarmer.PPM หน่วย


        fdef.filter.categoryBits = GameFarmer.MARIO_BIT; //categoryBits บิตหมวดหมู่การชนกัน กำหนดส่วน = GameFarmer.MARIO_BIT
        fdef.filter.maskBits=GameFarmer.GROUND_BIT | //maskBits คือบิตการชนกัน,ระบุประเภทที่รูปร่างนี้จะยอมรับสำหรับการชนกัน
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT |
                GameFarmer.ENEMY_BIT |
                GameFarmer.OBJECT_BIT |
                GameFarmer.ENEMY_HEAD_BIT |
                GameFarmer.ITEM_BIT |GameFarmer.COIN_SHOW|GameFarmer.DOWNSCREEN|GameFarmer.LEVEL2;

        //จับ shapewaist ยัดใส่ FixtureDef เพื่อทำให้ FixtureDef มีรูปทรงตาม Shape
        fdef.shape=shapewaist;
        b2body.createFixture(fdef).setUserData(this);//setUserData ตั้งค่าข้อมูลผู้ใช้
        //createFixture สร้างภาพประจำตัวและแนบไปกับร่างกายนี้ ใช้ฟังก์ชั่นนี้ถ้าคุณต้องการตั้งค่าพารามิเตอร์ประจำตัว

        EdgeShape head =new EdgeShape();
        head.set(new Vector2(-2 / GameFarmer.PPM, 6 / GameFarmer.PPM),new Vector2(-2 / GameFarmer.PPM ,6 / GameFarmer.PPM));
        fdef.filter.categoryBits = GameFarmer.MARIO_HEAD_BIT;
        fdef.shape=head;
        fdef.isSensor =true;//isSensor รูปร่างเซนเซอร์จะรวบรวมข้อมูลการติดต่อ แต่ไม่สร้างการตอบสนองการชนกัน
        //สร้าง Fixture ด้วย Body
        b2body.createFixture(fdef).setUserData(this);


        timeToRedefineMario = false; //เวลามาริโอไม่ย่อตัวเล็ก

    }

    //กำหนดมาริโอใหญ่
    public void defineBigMario(){

        Hud.setnameskill("fire");
        Vector2 currentPosition =b2body.getPosition();
        world.destroyBody(b2body);//destroyBody ทำลาย b2body เก่า


        BodyDef bdef=new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / GameFarmer.PPM)); //ตำแหน่งเกิดของมาริโอ
        bdef.type=BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shapewaist=new CircleShape();
        shapewaist.setRadius(6 / GameFarmer.PPM);
        fdef.filter.categoryBits = GameFarmer.MARIO_BIT;
        fdef.filter.maskBits=GameFarmer.GROUND_BIT |
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT |
                GameFarmer.ENEMY_BIT |
                GameFarmer.OBJECT_BIT |
                GameFarmer.ENEMY_HEAD_BIT |
                GameFarmer.ITEM_BIT | GameFarmer.COIN_SHOW|GameFarmer.DOWNSCREEN|GameFarmer.LEVEL2;

        fdef.shape=shapewaist;
        b2body.createFixture(fdef).setUserData(this);
        shapewaist.setPosition(new Vector2(0, -14 / GameFarmer.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head =new EdgeShape();
        head.set(new Vector2(-2 / GameFarmer.PPM, 6 / GameFarmer.PPM),new Vector2(-2 / GameFarmer.PPM ,6 / GameFarmer.PPM));
        fdef.filter.categoryBits = GameFarmer.MARIO_HEAD_BIT;
        fdef.shape=head;
        fdef.isSensor =true;
        b2body.createFixture(fdef).setUserData(this);

        timeToDefineBigMario =false;//เวลามาริโอย่อไม่ตัวใหญ่

    }
    //กำหนดเริ่มมาริโอเล็ก
    public void defineMario() {
        Hud.setnameskill("kick");
        BodyDef bdef=new BodyDef();
        bdef.position.set(32 / GameFarmer.PPM,32/ GameFarmer.PPM); //ตำแหน่งเกิดของมาริโอ
        bdef.type=BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shapewaist=new CircleShape();
        shapewaist.setRadius(6 / GameFarmer.PPM);
        fdef.filter.categoryBits = GameFarmer.MARIO_BIT;
        fdef.filter.maskBits=GameFarmer.GROUND_BIT |
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT |
                GameFarmer.ENEMY_BIT |
                GameFarmer.OBJECT_BIT |
                GameFarmer.ENEMY_HEAD_BIT |
                GameFarmer.ITEM_BIT | GameFarmer.COIN_SHOW|GameFarmer.DOWNSCREEN|GameFarmer.LEVEL2;

        fdef.shape=shapewaist;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head =new EdgeShape();//EdgeShape ชนิดรูปร่างของเส้น (ขอบ)  เหล่านี้สามารถเชื่อมต่อในห่วงโซ่หรือลูปกับรูปร่างขอบอื่น ๆ
        head.set(new Vector2(-2 / GameFarmer.PPM, 6 / GameFarmer.PPM),new Vector2(-2 / GameFarmer.PPM ,6 / GameFarmer.PPM));
        fdef.filter.categoryBits = GameFarmer.MARIO_HEAD_BIT;
        fdef.shape=head;
        fdef.isSensor =true;
        b2body.createFixture(fdef).setUserData(this);

    }

    //สั่งพ่นไฟ
    public void fire(){

        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight));

        //พ่นไฟจากตำแหน่ง (sceen จอ ,ร่างกายมาริโอ P x, ร่างกายมาริโอ P y, ไปทางขวา จริงมั้ย)
        //getPosition ตำแหน่งโลกของต้นกำเนิดของร่างกาย
        marioIsFire = true;

    }
    public void kickass(){
        kickAsses.add(new KickAss(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight));

        //พ่นไฟจากตำแหน่ง (sceen จอ ,ร่างกายมาริโอ P x, ร่างกายมาริโอ P y, ไปทางขวา จริงมั้ย)
        //getPosition ตำแหน่งโลกของต้นกำเนิดของร่างกาย
        marioIsFire = true;

    }

    public void draw(Batch batch){ //สั่งวาดรูป batch คืออุปกรณ์วาด
        super.draw(batch);

        for(FireBall ball : fireballs)//ถ้ามีค่าให้ดึงมาวาด
            ball.draw(batch);//สั่งวาด
        for(KickAss kick : kickAsses)//ถ้ามีค่าให้ดึงมาวาด
            kick.draw(batch);//สั่งวาด
    }

    public void  getSkill() {

        if(marioIsBig){
            fire();
        }else {
            kickass();
        }


    }


}










       /*a instanceof b
โดยที่ a เป็นชื่อของ object และ b เป็นชื่อของ class
โอเปอเรเตอร์ instanceof จะรีเทอร์นค่าแบบ boolean เป็น true ถ้า object a เป็น instance ของ class b หรือไม่ก็ subclass ของ b และจะรีเทอร์นค่า false
ถ้า object a ไม่ใช่ instance ของ class b หรือ subclass ของ b*/

