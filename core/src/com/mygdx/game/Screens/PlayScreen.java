package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ButtonController.Controller;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Items.Item;
import com.mygdx.game.Sprites.Items.ItemDef;
import com.mygdx.game.Sprites.Items.Mushroom;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by arnat on 4/7/2560.
 */
//PlayScreen จอการเล่น หลัก




    /*Body เป็นออปเจ็คหนึ่ง ใน Box2D ที่เอาไว้จัดการเกี่ยวกับฟิสิกส์ เช่น น้ำหนัก, ความเร็ว, ตำแหน่งของวัตถุ
    Fixture ก็คล้ายๆกับ Body แต่ต่างกันที่มันคือวัตถุที่สามารถจับต้องได้ มองเห็นได้ เช่น รูปทรงของวัตถุ สี่เหลี่ยม, สามเหลี่ยม หรือวงกลม, แรง (ฺBody กับ Fixture มักจะมาคู่กันครับ)
    World ผมเรียกทับศัพท์ไปเลย ตามชื่อเลยครับ มันก็คือโลกนั้นเอง โลกของ Box2D ตัวเกม เราจำเป็นจะต้องมี World เพื่อเก็บ Body, Fixture หรือค่าต่างๆที่เกี่ยวกับฟิสิกส์เอาไว้ ถือเป็นหัวใจของ Box2D เลยก็ว่าได้*/








//Gdx.input.* //Input เป็นโมดูลที่ช่วยให้เราสามารถที่จะ Handler Input ได้ทุก Platform รองรับทั้ง Keyboard, Touch Screen, Accelerometer และ Mouse โมดูลทั้งหมด จะเก็บอยู่ที่่ package
//เช็คว่ามีการกด ตัวอักษร D บนแป้น Keyboard หรือไม่ ก็จะใช้โค๊ดนี้  boolean isKeyPressed = Gdx.input.isKeyPressed(Keys.D);

    /*เช็คว่ามีการแตะที่หน้าจอหรือไม่ พร้อมกับระบุตำแหน่ง
    boolean isTouched = Gdx.input.isTouched();
Gdx.input.getX();
Gdx.input.getY();*/


//เป็น interface เป็นค่าคงที่

//PlayScreen จอการเล่น
public class PlayScreen implements Screen {

    // อ้างอิงถึงเกมของเราใช้เพื่อกำหนดหน้าจอ
    private GameFarmer game;
    private TextureAtlas atlas; //พื้นผิวแผนที่
    public static boolean alreadyDestroyed=false;

    // ตัวแปรตัวละครที่เป็นขั้นพื้นฐาน
    private OrthographicCamera gamecam;
    private Viewport gamePort;//สัดส่วนภาพวิว
    private Hud hud;

    //ตัวแปรแผนที่ปูด้วยกระเบื้อง
    private TmxMapLoader mapLoader;
    private TiledMap map; //TiledMap แผนที่ของ Tile
    private OrthogonalTiledMapRenderer renderer; //เครื่องเรนเดอร์แผนที TiledMap แบบ Orthogonal เป็นมุมฉาก


    //ตัวแปร Box2d
    private World world; // World นั้นคืออ็อปเจ็ค ที่เป็นเหมือนศูนย์รวมคอยจัดการ memory, objects รวมถึงการ simulation ต่างๆ
    private Box2DDebugRenderer b2dr;//ใช้สำหรับวาดเป็นโครงร่าง
    private B2WorldCreator creator;////B2WorldCreator คลาสสร้างสิ่งต่างๆ

    //เทพดา
    private Mario player;

    private Music music;

    private Array<Item> items;//อาเรย์ไอเทม
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;//รายการวางไอเทม


    //control
    Controller controller;
    //control


    public PlayScreen(GameFarmer game, boolean b){//constructor เราก็รับออปเจ็ค GameFarmer มาอีกที ตรง create#setScreen







        atlas = new TextureAtlas("level2/Mario_and_Enemies.pack");//atlas สมุดแผนที่


        //กำค่า game ให้เท่ากับ คลาสนี้
        this.game =game;

        //สร้างกล้องที่ใช้ในการปฏิบัติตามมาริโอผ่านโลกแคม
        gamecam =new OrthographicCamera();

        //สร้างวิวสกีน FitViewport เพื่อรักษาสัดส่วนภาพที่แท้จริงแม้ว่าจะมีขนาดหน้าจอก็ตาม
      // วิวพอร์ตที่ปรับขนาดโลกโดยใช้การปรับขนาด ScalingViewport( Scaling scaling,  float worldWidth, float worldHeight,Camera camera)
        gamePort =new ScalingViewport(Scaling.stretch,GameFarmer.V_WIDTH / GameFarmer.PPM ,GameFarmer.V_HEIGHT / GameFarmer.PPM ,gamecam);

        //Scaling.stretch //การปรับขนาด stretch คือแบบยืด วัดแหล่งที่มาเพื่อเติมเต็มเป้าหมาย
        //สร้างเกม HUD เพื่อทำคะแนน / เวลา / ระดับ
        hud=new Hud(game.batch);//ส่งค่ากลับไป คลาส Hud

        //โหลดแผนที่ของเราและตั้งค่า renderer แผนที่ของเรา
        mapLoader= new TmxMapLoader();//TmxMapLoader คลาสสำหรับโหลดแผนที่ในไฟล์ .tmx

       if(b) {
           map = mapLoader.load("level1/level1.tmx"); //โหลดมาเก็บใว้ map แผนที่ ของ TiledMap
           renderer = new OrthogonalTiledMapRenderer(map, 1 / GameFarmer.PPM );//OrthogonalTiledMapRenderer(map, 1 )โหลด map คือแผนที่ , 1 คือขยายภาพ


       }else {
           map = mapLoader.load("level2/level2.tmx"); //โหลดมาเก็บใว้ map แผนที่ ของ TiledMap
           renderer = new OrthogonalTiledMapRenderer(map, 1 / GameFarmer.PPM );//OrthogonalTiledMapRenderer(map, 1 )โหลด map คือแผนที่ , 1 คือขยายภาพ

       }


        //ตั้ง gamcam ของเราให้เป็นศูนย์กลางได้อย่างถูกต้องเมื่อเริ่มต้นของแผนที่

        //gamecam.position พิกัดกล้อง ตำแหน่งกล้อง.set(x,y,z)
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);


        //สร้างโลก Box2D ของเราไม่มีแรงโน้มถ่วงใน X, -10 แรงโน้มถ่วงใน Y และช่วยให้ร่างกายนอนหลับได้
        world=new World(new Vector2(0,-10  ),true);


        b2dr=new Box2DDebugRenderer();//Box2DDebugRenderer สำหรับร่างโครงร่าง


        //สร้างมอนเตอร์ กับท่อ
        creator = new B2WorldCreator(this);//B2WorldCreator คลาสสร้างสิ่งต่างๆ

        //สร้างมาริโอในโลกของเกมของเรา
        player=new Mario(this);

        // setContactListener กำหนดฟังกิจกรรมเหตุการการชน(ใน WorldContactListener)
        world.setContactListener(new WorldContactListener());//กำหนดให้ ตรวจสอบการชนของ Contact  //กำหนดการชนกัน

        music= GameFarmer.manager.get("audio/music/mario_music.ogg",Music.class);
        music.setLooping(true);//setLooping ตั้งค่าว่าสตรีมเพลงกำลังวนสายหรือไม่
        music.setVolume(0.3f);
        music.play();

        items = new Array<Item>();//สร้าง items
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>(); //LinkedBlockingDeque สร้างรายการว่างไอเทม items


        //  goomba=new Goomba(this, 5.64f ,.16f);//ตำแหน่งที่อยู่ของ goomba

    }

    //เมธอทกำหนดให้คลอด เห็ด
    public void spawnItem(ItemDef idef){ //คลอดไอเท็ม ItemDef คือตำแหน่งชนิดไอเทม

        itemsToSpawn.add(idef); //เพิ่มไอเท็ม

    }
    public void handleSpawningItems(){//จัดการออกไอเทม

        if (!itemsToSpawn.isEmpty()){//ถ้ามันไม่ว่างปล่าวจริง (แสดงว่ามรไปเทม)
            ItemDef idef = itemsToSpawn.poll(); //สำรวจมีรายการไหม ให้ดึง
            if(idef.type == Mushroom.class){//ถ้าเป็นชนิดเห็ด
                items.add(new Mushroom(this, idef.position.x,idef.position.y));//ให้สร้างเห็ด
            }
        }
    }


    public  TextureAtlas getAtlas(){//ส่งกลับ TextureAtlas ส่งผ่านไปยังตัวสร้างผิวนี้

        return atlas;//สมดแผนที่
    }


    //เมธอด show และ hide ที่จะถูกเรียกเมื่อเวลาโฟกัสและไม่ได้โฟกัสหน้าจอ
    @Override
    public void show() {

    }
    //ปุ่มกด
    //ควบคุมผู้เล่นของเราโดยใช้แรงกระตุ้นในทันที
    public void handleInput(float dt){

        int firstX = Gdx.input.getX(0);//.input.getX()จุดสัมผัสที่1 x
        int firstY = Gdx.input.getY(0);//.input.getY()จุดสัมผัสที่1 y
        int secondX = Gdx.input.getX(1);
        int secondY = Gdx.input.getY(1);
        boolean firstFingerTouching = Gdx.input.isTouched(0);//เช็คว่าสัมผัสจุดที่ 1 อยู่หรือไม่
        boolean secondFingerTouching = Gdx.input.isTouched(1);//เช็คว่าสัมผัสจุดที่ 2 อยู่หรือไม่
        //Gdx.app.log("P1"+"x"+ firstX+"y",""+firstY);
       // Gdx.app.log("P2"+"x"+ secondX+"y",""+secondY);

        //ควบคุมผู้เล่นของเราโดยใช้แรงกระตุ้นในทันที
        if (player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)//isKeyPressed กดค้าง
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))//isKeyJustPressed กดทีละครั้ง
                player.getSkill();

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z))//isKeyJustPressed กดทีละครั้ง
                player.kickass();

//controller
            //ถ้านิ้วที่ 1 สัมผัส ลง
           if(firstFingerTouching) {

               if (controller.upTouched(firstX, firstY)&& player.b2body.getLinearVelocity().y == 0) {

                   player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);


               }   if (controller.downTouched(firstX, firstY)) {
                   player.getSkill();

               }  if(controller.leftTouched(firstX, firstY) && player.b2body.getLinearVelocity().x >= -2) {
                   player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
               }  if (controller.rightTouched(firstX, firstY)&& player.b2body.getLinearVelocity().x <= 2) {//ถ้าสัมผัสด้านขวา
                   player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);



               }

           }else { //ถ้านิ้วที่ 1 ไม่สัมผัส ลง


           }
            //ถ้านิ้วที่ 2 สัมผัส ลง
            if(secondFingerTouching) {
                if (controller.upTouched(secondX, secondY)&& player.b2body.getLinearVelocity().y == 0) {
                    player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);

                } if (controller.downTouched(secondX, secondY)) {//หรือถ้าสัมผัสด้านซ้าย
                    player.getSkill();


                }    if(controller.leftTouched(secondX, secondY) && player.b2body.getLinearVelocity().x >= -2) {
                     player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                 } if (controller.rightTouched(secondX, secondY)&& player.b2body.getLinearVelocity().x <= 2) {//ถ้าสัมผัสด้านขวา
                    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);


                }

            }else {   //ถ้านิ้วที่ 2 ไม่สัมผัส ลง



            }


//controller




        }
        // game.batch.begin();
        //img = new Texture("badlogic.jpg");
        // batch.begin(); batch.draw(img, 0, 0); batch.end();
        //จะเห็นได้ว่า SpriteBatch ทำการวาดรูป Texture ที่โหลดมาจาก `badlogic.jpg’ ที่ตำแหน่ง x = 0 และ y = 0



/*
        if(Gdx.input.isTouched())
         // gamecam.position.x +=100 * dt;

        player.b2body.applyLinearImpulse(0,4f,0,0,true);





           if (Gdx.input.getX()==73 && Gdx.input.getY()==372) {

               player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

           }

           // rect1 = new Rect(Button0.getLeft(), Button0.getTop(), Button0.getRight(), Button0.getBottom()); //รัศมี แกนx แกนyButton0

            int a=  Gdx.input.getX();
            int b=  Gdx.input.getY();

            String aString = Integer.toString(a);
            String aString1 = Integer.toString(b);

            Gdx.app.log(aString, aString1);//จบการติดต่อ
*/
    }

    //จัดการการป้อนข้อมูลของผู้ใช้ก่อน
    public  void update(float dt){ //update สั้งมาจากเมธอท render

        //จัดการการป้อนข้อมูลของผู้ใช้ก่อน
        handleInput(dt);
        handleSpawningItems();//ว่างคลอดไอเท็ม

        // ใช้เวลา 1 ขั้นตอนในการจำลองทางฟิสิกส์ (60 ครั้งต่อวินาที)
        //world.step(dt, 8, 3); ส่วนนี้คือ step สำหรับเช็คพวก collision (การชนกันของวัตถุ)
        // ส่วนค่า 8, 3 คือ velocityIterations(ความเร็ว) และ positionIterations(ตำแหน่ง) ส่วนนี้ อาจจะเปลี่ยนให้อยู่ระหว่าง 1-10 ดูครับ

        world.step(1 / 60f, 6, 2);//step(1 / 60fเร็วปกติ คือเร่งความภาพต่อเฟรม,การวนซ้ำความเร็ว - ข้อ จำกัด ความเร็ว, การวนซ้ำตำแหน่ง - ข้อ จำกัด ตำแหน่ง);ไม่แน่ใจ
        player.update(dt);//มาริโอ

        for(Enemy enemy : creator.getEnemies()) {//เก็บไว้ใน enemy ศัตรู :ดึงค่า creator มีตัวศัตรูให้นำมาใส่ใน enemy
            enemy.update(dt);//อัพเดตศัตรูตามเวลา

            if (!enemy.destroyed && enemy.getX() < player.getX() + 224 / GameFarmer.PPM) //กำหนดถ้าตัวผู้เล่นเข้าใกล่ระยะ enemy.getX() ให้ศัตรูขยับ
                enemy.b2bodye.setActive(true); //กำหนดให้สร้างกล้ามเนื้อศัตรู ตั้งค่าสถานะที่ใช้งานของร่างกาย
        }

        for (Item item : items) {//ถ้า items มีค่าให้นำมาใส่ใน item
            item.update(dt);// item.update ค่า เพื่อวาด dt ส่งค่าเวลาเฟรม

        }
        // goomba.update(dt);
        hud.update(dt);//อัพเดตค่า hud ตามเวลา dt
        controller.update(dt);//อัพเดตค่า hud ตามเวลา dt

        if (player.currentState != Mario.State.DEAD){//ถ้าสถานะผู้เล่นปัจจุบัน ไม่เท่ากับ มาริโอ State.DEAD

            //แนบ Gamecam ของเราเข้ากับพิกัด players.x ของเรา
            gamecam.position.x=player.b2body.getPosition().x;//กำหนดพิกัดกล้อง x = พิกัดร่างผู้เล่น x
            //   gamecam.position.y=player.b2body.getPosition().y;//กำหนดพิกัดกล้อง y = พิกัดร่างผู้เล่น


        }


        // อัปเดต gamecam ของเราด้วยพิกัดที่ถูกต้องหลังจากทำการเปลี่ยนแปลง
        gamecam.update();//อัพเดตกล้อง

        // แจ้งให้ renderer ของเราที่จะวาดเฉพาะสิ่งที่กล้องของเราสามารถมองเห็นในโลกของเราเกม
        renderer.setView(gamecam);//setView เซ็ดฉากที่กล้องมองเห็น

    }

    //render หัวใจของ Application เลยก็ว่าได้ เพราะมันคือ Game
    // Loop เมธอดนี้จะถูกเรียก 50-60 ครั้งต่อวินาที
    @Override
    public void render(float delta) { //parameter เป็น float ครับ ซึ่งก็คือ deltaTime


        // ทุกครั้งที่เราจะสั่งให้ SpriteBatch วาดรูปนั้น เราต้องให้วาดระหว่างเมธอด SpriteBatch.begin() และ SpriteBatch.end() ดังตัวอย่างนี้
        //แยกตรรกะการอัปเดตออกจากการแสดงผล

        update(delta);//ไว้อัพสถานะผู้ตามเวลา

        //ล้างหน้าจอเกมด้วยสีดำ
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //แสดงแผนที่เกมของเรา  แสดงการกระทำของเรา
        renderer.render();


        //gamecam สำหรับ render , camera ควรจะมีการอัพเดท ทุกๆครั้งเมื่อมีการเปลี่ยน frame
        //โหมดแสดงภาพ renderer our Box2DDebugLines

        b2dr.render(world,gamecam.combined);//กำหนดโครงร่าง ไว้ให้มองโครง Box2d

        //batch ชุด //setProjectionMatrix การฉาย //gamecam.combined คือสั่งให้ SpriteBatch ทำการ render ทุกๆอย่าง

        //batch คือ SpriteBatch จาก คลาส GameFarmer
        //game คือ คลาส GameFarmer
        game.batch.setProjectionMatrix(gamecam.combined);//ฉายการวาดภาพ combined คือภาพรวม

        game.batch.begin();//เริ่มวาด

        //  player.draw(game.batch)วาด ผู้เล่น
        player.draw(game.batch);//วาด draw โดยรับพารามิเตอร์เป็น SpriteBatch, ข้อความ, ตำแหน่งแกนx, ตำแหน่งแกน y ตามลำดับครับ

        //วาดศตรู
        for(Enemy enemy : creator.getEnemies())//สำหรับศัตรู ถ้า creator มีค่าให้นำมาใส่ใน enemy

            enemy.draw(game.batch);//สำหรับวาดศัตรู

        //วาดไอเทม
        for (Item item : items)//ถ้า items มีค่าให้นำมาใส่ใน item
            item.draw(game.batch);//สำหรับวาดไอเทม



        game.batch.end();//จบ

        //game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);ก็คือ game.font.draw(SpriteBatch, String, float, float) คือเมธอดที่ใช้สำหรับแสดงข้อความบทหน้าจอ

        //ตั้งค่าแบทช์เพื่อวาดภาพที่กล้อง hud เห็น
        controller.draw();//วาดปุ่ม controller

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        if (gameOver()){//ตรวจสอบ เกมจบหรือไม่

            game.setScreen(new GameOverScreen(game));//โดยส่งอ็อปเจ็ค game ไปเป็น parameter ด้วย ครับ (ลองนึกถึง เวลาเข้าเกมมาครับ หน้าแรก จะโชว์หน้าจอนี้ แล้วเมื่อผู้เล่นทำการแตะหน้าจอ ก็จะไปหน้าจอเล่นเกมครับ)
//setScreen ตั้งค่าหน้าจอปัจจุบัน
            dispose();

        }
        if (gameLevel2()){//ตรวจสอบ เกมเวล2หรือไม่

            game.setScreen(new Level2(game));//โดยส่งอ็อปเจ็ค game ไปเป็น parameter ด้วย ครับ (ลองนึกถึง เวลาเข้าเกมมาครับ หน้าแรก จะโชว์หน้าจอนี้ แล้วเมื่อผู้เล่นทำการแตะหน้าจอ ก็จะไปหน้าจอเล่นเกมครับ)
//setScreen ตั้งค่าหน้าจอปัจจุบัน
            dispose();

        }


/*
        batch.begin();
        batch.draw(img, x, y);สั้งเลื่อน
        batch.end();

        x += 100 * Gdx.graphics.getDeltaTime();

        Delta Time ทีนี้มาดูทำไมถึงเป็น Gdx.graphics.getDeltaTime();
         อันนี้เป็น Module ของทาง LibGDX อีกเช่นกันครับ ไว้สำหรับแสดงเวลา DeltaTime มันคือเวลา จากเฟรมล่าสุด ถึงเฟรมปัจจุบันครับ
          เนื่องจาก มันถูกเพิ่ม 100 แค่ 8 ครั้ง มันก็สุดจอไปแล้ว โดยใช้เวลา ไม่ถึง 0.1 วินาทีด้วยซ้ำ ตามองไม่ทัน ฉะนั้น ก็เลยต้องใช้วิธี x += 100 * ค่า deltaTime
           ครับ ก็คือ เพิ่มที 1 - 2 pixel เท่านั้น ทีนี้ก็มองทันแล้ว
        */
    }

    public boolean gameOver(){//ส่งค่าถ้าเกมจบ
        if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3|| hud.isTimeUp()){//สถานะมาริโอตาย && สถานะเวลาเดินไป > 3 หรือ|| เวลาเกมหมด
            music.stop();
            return true;
        }

        return false;
    }
    public boolean gameLevel2(){//ส่งค่าถ้าLevel2
        if (player.currentState == Mario.State.NEXTLEVEL2 && player.getStateTimer() > 3){//สถานะมาริโอตาย && สถานะเวลาเดินไป > 3 หรือ|| เวลาเกมหมด
            music.stop();
            return true;
        }

        return false;
    }

    // resize เมธอดนี้จะถูกเรียกทุกครั้งเมื่อมีการเปลี่ยนขนาดหน้าจอ
    @Override
    public void resize(int width, int height) {
        //  Gdx.app.log("GameScreen resize", width+"and"+height);//จบการติดต่อ
        //อัปเดตวิวพอร์ตของเกมเราแล้ว


        controller = new Controller(game.batch,width, height);
        gamePort.update(width, height);//นำค่า resize มาอัพเดต gamePort สัดส่วนภาพวิว
        controller.resize(width,height);////นำค่า resize มา resize ปุ่ม

    }

    public TiledMap getMap(){//อ่าน แผ่นที่ TiledMap

        return map; //ส่งค่า map
    }
    public World getWorld(){

        return world;
    }


    // pause Android เมธอดนี้จะถูกเรียกเมื่อมีสายเข้า หรือว่าเวลายูเซอร์กดปุ่ม Home แต่ถ้าใน
    @Override
    public void pause() {


    }
    //ถูกเรียกเฉพาะบน Android เมื่อแอพพลิเคชันถูกเรียกกลับจาก pause state.
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    //dispose กำจัด
    // dispose() : เมธอดนี้จะถูกเรียกเมื่อกำลังจะปิดแอพพลิเคชัน สิ้นสุดโปรแกรม
    @Override
    public void dispose() {
        //หน้าจอเล่น
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();






    }

    public Hud getHud(){ return hud; }

}











/*
implements
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

*/
