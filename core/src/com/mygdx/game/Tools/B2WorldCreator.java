package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Enemies.Goomba;
import com.mygdx.game.Sprites.Enemies.Turtle;
import com.mygdx.game.Sprites.Items.CoinShow;
import com.mygdx.game.Sprites.Skills.skill;
import com.mygdx.game.Sprites.TileObjects.Brick;
import com.mygdx.game.Sprites.TileObjects.Coin;
import com.mygdx.game.Sprites.TileObjects.DownScreen;
import com.mygdx.game.Sprites.TileObjects.LevelPass;

/**
 * Created by arnat on 4/7/2560.
 */

//สร้างโลก B2 ที่ใช้คือโลกกับ แผนที่
//B2WorldCreator คลาสสร้างสิ่งต่างๆ

public class B2WorldCreator {

    private static Array<CoinShow> coinShow;//เก็บจำนวน CoinShow หลายตัว
    private Array<Goomba> goombas;//เก็บจำนวน goombas หลายตัว
    private static Array<Turtle> turtles;//เก็บจำนวน turtles หลายตัว

    public B2WorldCreator(PlayScreen screen){ //PlayScreen สร้าง โลกจอแสดงต่างๆ

        World world =screen.getWorld();//อ่านค่า world เก็บไว้ที่นี้ world คือทุกสิ่งอย่างบนโลกเกมนั้นคืออ็อปเจ็ค ที่เป็นเหมือนศูนย์รวมคอยจัดการ
        TiledMap map = screen.getMap();//อ่านค่า map คือแผนที่เราโหลดมาเก็บไว้ใน PlayScreen อันนี้ mapLoader.load("level1.tmx")


        //สร้างตัวและตัวแปรประจำตัวสิ่งของ
        BodyDef bdef = new BodyDef();
        PolygonShape shape=new PolygonShape();//สร้าง รูปร่าง ตามหลากหลาย
        FixtureDef fdef= new FixtureDef();// FixtureDef ขึ้นมาก่อน สำหรับกำหนดคุณสมบัติให้กับวัตถุ และค่อยใส้ shape
        Body body;


        //สร้างพื้นดิน / ติดตั้ง

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){ //ตรวจสอบออฟเจต ของ map อ่านบรรทัดที่ 2 นับแบบ index คือเริ่มจาก 0
            //get(2)คือ ออฟเจค Ground จาก level1.tmx
            //object :ดึงค่า map.getLayers.แถวที่2.อ่านออฟเจค.อ่านอาร์เรย์กับทุกชั้นที่ตรงกับประเภท(วัตถุแผนที่สี่เหลี่ยมผืนผ้า คลาส)
            //RectangleMapObject สร้างวัตถุสี่เหลี่ยมผืนผ้าที่มีพิกัด X และ Y ที่ระบุพร้อมกับความกว้างและความสูงที่ระบุ
            Rectangle rect =((RectangleMapObject)object).getRectangle();//

            //กำหนดร่างกาย
            bdef.type = BodyDef.BodyType.StaticBody;//StaticBody ขยับไม่ได้ // BodyDef เพื่อกำหนดตำแหน่งให้กับ Body
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/ GameFarmer.PPM ,(rect.getY()+rect.getHeight()/2)/ GameFarmer.PPM );

            //เซ็ด position .set (rect พื้นที่ร่างกาย)
            body =world.createBody(bdef);//สร้าง Body บนโลก

            shape.setAsBox(rect.getWidth() /2 / GameFarmer.PPM ,rect.getHeight()/2 / GameFarmer.PPM );//สร้างจุดยอดเพื่อแสดงกล่องแกนชิด setAsBox(ครึ่งความกว้าง hx, ครึ่งความสูง hy)

            //จับ shape ยัดใส่ FixtureDef เพื่อทำให้ FixtureDef มีรูปทรงตาม Shape
            fdef.shape=shape;
            body.createFixture(fdef);// สร้างภาพประจำตัวและแนบไปกับ Body ร่างกายนี้

        }

        //pipe สร้างท่อ / ติดตั้ง

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){//MapObject ออฟเจคแผนที่ เก็บค่า  map อ่านบรรทัดที่ 3

            Rectangle rect =((RectangleMapObject)object).getRectangle(); //Rectangle ใช้สร้าง object rectangle และเก็บค่าแกน x, y และขนาดความกว้าง ความสูง ไว้ที่ 0 ทั้งหมด
            //.getRectangle() อ่านค่ารูปสี่เหลี่ยมผื้นผ้า

            //สร้าง body
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ GameFarmer.PPM ,(rect.getY()+rect.getHeight()/2)/ GameFarmer.PPM );

            body =world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / GameFarmer.PPM ,rect.getHeight()/2 / GameFarmer.PPM );
            fdef.shape=shape;

            fdef.filter.categoryBits = GameFarmer.OBJECT_BIT;//categoryBits บิตหมวดหมู่การชนกัน กำหนดส่วน = GameFarmer.MARIO_BIT
            body.createFixture(fdef);


        }
        //สร้างเหรียญ Coin/ ติดตั้ง

        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //  Rectangle rect =((RectangleMapObject)object).getRectangle();

            new Coin(screen,object); //กำหนดสร้างเหรียญ

        }

        //สร้างพื้นอิฐ / ติดตั้ง

        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen,object); //กำหนดสร้างอิฐ Brick ส่งค่า(screen,object)


        }  //สร้างพื้นอิฐ / ติดตั้ง

        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            new DownScreen(screen,object); //กำหนดสร้างอิฐ Brick ส่งค่า(screen,object)


        }  for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            new LevelPass(screen,object); //กำหนดสร้างอิฐ Brick ส่งค่า(screen,object)


        }
        //create all coinShow เหรียญ
        coinShow = new Array<CoinShow>();

        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect =((RectangleMapObject)object).getRectangle();//rect คือตำแหน่ง กุมบา มีทั้ง x , y ,w,h
            coinShow.add(new CoinShow(screen, rect.getX() / GameFarmer.PPM, rect.getY() / GameFarmer.PPM));
            //เพิ่มศัตรู เหรียญเข้าไปในอาเรย์ coinShow โดย .add(new Goomba(screen ภาพ,ตำแหน่ง x ,ตำแหน่ง y ))

        }






        //create all turtles เต่า
        turtles = new Array<Turtle>();
        //for ถ้ามีเต่าให้เพิ่มเข้าไปใน object คือ object : อ่านค่าใช่ออพเจคเต่า
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect =((RectangleMapObject)object).getRectangle();//เก็บค่ารัศมี มี x,y,w,h เอาไว้ใน rect
            turtles.add(new Turtle(screen, rect.getX() / GameFarmer.PPM, rect.getY() / GameFarmer.PPM));
            //เพิ่มศัตรู เต่าเข้าไปในอาเรย์ goombas โดย .add(new Goomba(screen ภาพ,ตำแหน่ง x ,ตำแหน่ง y ))
        }


        //create all goombas กุมบ้า
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){//เปรียบคือหา object กุมบา มากำหนดให้มีชีวิต

            Rectangle rect =((RectangleMapObject)object).getRectangle();//rect คือตำแหน่ง กุมบา มีทั้ง x , y ,w,h
            goombas.add(new Goomba(screen, rect.getX() / GameFarmer.PPM, rect.getY() / GameFarmer.PPM));
            //เพิ่มศัตรู กุมบ้าเข้าไปในอาเรย์ goombas โดย .add(new Goomba(screen ภาพ,ตำแหน่ง x ,ตำแหน่ง y ))
        }

    }

    public Array<CoinShow> getCoinShow() {

        return coinShow;
    }
    public Array<Goomba> getGoombas() {

        return goombas;
    }

    public static void removeTurtle(Turtle turtle){
        turtles.removeValue(turtle,true);

    }
    public Array<Enemy> getEnemies() { //อ่านค่า อาเรย์ศัตรู

        Array<Enemy>enemies = new Array<Enemy>();//สร้างออฟเจค อาเรย์ ศัตรู
        enemies.addAll(goombas);//เพิ่ม goombas ทั้งหมด ไว้ใน enemies
        enemies.addAll(turtles);//เพิ่ม turtles ทั้งหมด ไว้ใน enemies
        enemies.addAll(coinShow);//เพิ่ม coinShow ทั้งหมด ไว้ใน enemies

        return enemies;//ส่งค่าศัตรูกลับไป


    }
    public Array<skill> getSkill() { //อ่านค่า อาเรย์ศัตรู

        Array<skill>skill = new Array<skill>();//สร้างออฟเจค อาเรย์ ศัตรู
      //  skill.addAll(f);//เพิ่ม goombas ทั้งหมด ไว้ใน enemies


        return skill;//ส่งค่าศัตรูกลับไป


    }
}
