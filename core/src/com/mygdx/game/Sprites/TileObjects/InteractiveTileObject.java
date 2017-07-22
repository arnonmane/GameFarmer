package com.mygdx.game.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

/**
 * Created by arnat on 4/7/2560.
 */

//abstract class คือ คลาสที่มีคียเวร์ด abstract อยู่หน้า class เป็นคลาสที่ถูกสร้างขึ้นมาเพื่อเป็นคลาสแม่ (super class)
// ใช้ในการกำหนดสเปคของคลาสลูก (sub class) ที่จะถูกสร้างเพื่อใช้งานจริง หรือคลาสที่ไม่ต้องการให้นำไปสร้างเป็นออบเจ็กต์ (object) ก็สามารทำให้เป็น abstract class ได้
//คลาสตอบโต้ต่อวัตถุ
public abstract class InteractiveTileObject { //InteractiveTileObject วัตถุกระเบื่องแบบโต้ตอบ
    protected World world;//โลก
    protected TiledMap map;//แผนที่ใน Tiled
    protected TiledMapTile tile; //กระเบื่อง
    protected Rectangle bounds; //ขอบเขต
    protected Body body;//ร่างกาย
    protected PlayScreen screen;//ภาพวาด
    protected MapObject object;//ออพเจคแผนที่

    protected Fixture fixture;// Fixture ก็คล้ายๆกับ Body แต่ต่างกันที่มันคือวัตถุที่สามารถจับต้องได้ มองเห็นได้

    public InteractiveTileObject(PlayScreen screen, MapObject object){ //คอนสตัคเตอ

        //MapObject แผนที่นิติบุคคลทั่วไปที่มีคุณลักษณะพื้นฐานเช่นชื่อ
        this.object = object;
        this.screen = screen;
        this.world=screen.getWorld();
        this.map=screen.getMap();
        this.bounds=((RectangleMapObject)object).getRectangle();//กำหนดรัศมี ออพเจคแผนที่ = bounds

        // สร้างกล่อง
        //สร้าง BodyDef
        BodyDef bdef= new BodyDef();
        FixtureDef fdef=new FixtureDef();
        PolygonShape shape = new PolygonShape();//PolygonShape เป็นรูปหลายเหลี่ยม


        bdef.type = BodyDef.BodyType.StaticBody;//ชนิด StaticBody ร่างกายคงที่
        bdef.position.set((bounds.getX()+bounds.getWidth()/2)/ GameFarmer.PPM ,(bounds.getY()+bounds.getHeight()/2)/ GameFarmer.PPM );// set ตำแหน่งรัศมีกล่อง

        body =world.createBody(bdef);// สร้าง Body บนโลก
        shape.setAsBox(bounds.getWidth()/2 / GameFarmer.PPM ,bounds.getHeight()/2 / GameFarmer.PPM );//setAsBox(ครึ่งความกว้าง hx, ครึ่งความสูง hy)
        fdef.shape=shape;
        fixture = body.createFixture(fdef);





    }

    public  abstract  void  onHeadHit(Mario mario);//mario เอาหัวคี //ไว้ใช้ในคลาส WorldContactListener ตรวจสอบการชนและสั่งค่า
    //คลาสที่สืบทอดก็จะโดนสั่ง

    public  void  setCategoryFilter(short filterBit){ //setCategoryFilter ตั้งค่าตัวกรองหมวดหมู่ filterBit เช่น GameFarmer.COIN_BIT

        Filter filter = new Filter(); //filter ข้อมูลนี้มีข้อมูลการกรองข้อมูลติดต่อ
        filter.categoryBits= filterBit;//categoryBits บิตหมวดหมู่การชนกัน กำหนดส่วน = GameFarmer.MARIO_BIT
        fixture.setFilterData(filter);//fixture มาตั้งค่าตรวจสอบการชนใน filter

    }
    public TiledMapTileLayer.Cell getCell(){//อ่านพิกัดแผนที่ TiledMap

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);////MapObject ออฟเจคแผนที่ เก็บค่า  map อ่านบรรทัดที่ 1 นับเริ่มจาก 0 จากล่าง
        // map.getLayers.get (แถวที่1)//คือ Graphic Layer


        return  layer.getCell((int)(body.getPosition().x * GameFarmer.PPM /16 ),
                (int)(body.getPosition().y * GameFarmer.PPM / 16));
        //อ่านพิกัดแผนที่ layer getcell(พิกัด x, พิกัด y)
    }




}
