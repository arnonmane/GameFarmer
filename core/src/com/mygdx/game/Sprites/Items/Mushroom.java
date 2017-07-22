package com.mygdx.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

/**
 * Created by arnat on 4/7/2560.
 */

//เห็ดกินแล้วโต
public class Mushroom extends  Item {
    //คอนสทัคเตอ Item สิ่งต้องทั้งหมดจาก class Item
    public Mushroom(PlayScreen screen, float x, float y) { //PlayScreen จอการเล่น , ตำแหน่ง x ,ตำแหน่ง y

        super(screen, x, y);//เรียกค่า แอทติบิว คลาสแม่

        setRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);//แสดงภาพ
        velocity = new Vector2(0.7f,0);//สร้าง velocity ความเร็ว (x,y)

    }

    @Override
    public void defineItem() { //กำหนดรายการเห็ด ร่างกายคุณสมบัติ
        BodyDef bdef=new BodyDef();
        bdef.position.set(getX(),getY()); //ตำแหน่งเกิดของเห็ด
        bdef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(6 / GameFarmer.PPM);
        fdef.filter.categoryBits = GameFarmer.ITEM_BIT;
        fdef.filter.maskBits= GameFarmer.MARIO_BIT |
                GameFarmer.OBJECT_BIT |
                GameFarmer.GROUND_BIT |
                GameFarmer.COIN_BIT |
                GameFarmer.BRICK_BIT;


        fdef.shape=shape;
        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void use(Mario mario) {

        destroy();//ทำลายเห็ด //จาก class Item
        mario.grow();//มาริโอโต


    }

    @Override
    public void update(float dt) {
        super.update(dt);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);//กำหนดตำแหน่ง วาดภาพให้ร่างกาย

        velocity.y=body.getLinearVelocity().y; //.getLinearVelocity() รับความเร็วเชิงเส้นความเร็วลงพื้น
        //velocity.y ความเร็วลงพื้น

        body.setLinearVelocity(velocity);//เซ็ตความเร็วและตำแหน่ง เห็ด x,y ให้กับเห็ด เพื่อให้วิ่ง
    }
}
