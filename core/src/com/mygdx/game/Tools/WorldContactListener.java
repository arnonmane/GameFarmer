package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.GameFarmer;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Items.Item;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Sprites.Skills.FireBall;
import com.mygdx.game.Sprites.Skills.KickAss;
import com.mygdx.game.Sprites.TileObjects.InteractiveTileObject;

/**
 * Created by arnat on 4/7/2560.
 */

//ผู้ติดต่อผู้ติดต่อทั่วโลก
//กำหนดการชนกัน

public class WorldContactListener implements ContactListener {
    // beginContact เรียกว่าเมื่อเริ่มจับคู่สองชนกัน
    @Override
    public void beginContact(Contact contact) {//contact ชั้นจัดการการติดต่อระหว่างสองรูปร่าง
        //contact ชั้นจัดการการติดต่อระหว่างสองรูปร่าง มีที่อยู่ติดต่อสำหรับ AABB ที่ทับซ้อนกันในเฟสกว้าง (ยกเว้นหากได้รับการกรอง) ดังนั้นวัตถุติดต่ออาจมีอยู่ซึ่งไม่มีจุดติดต่อ

        // getFixtureA อ่าน fixture แรกของ contact
        // getFixtureB อ่าน fixture รองของ contact
        // Fixture ก็คล้ายๆกับ Body แต่ต่างกันที่มันคือวัตถุที่สามารถจับต้องได้ มองเห็นได้ เช่น รูปทรงของวัตถุ สี่เหลี่ยม, สามเหลี่ยม หรือวงกลม, แรง
        Fixture fixA = contact.getFixtureA();//รับงานประจำครั้งแรกในรายชื่อติดต่อนี้
        Fixture fixB = contact.getFixtureB();//รับอุปกรณ์ติดตั้งที่สองในรายชื่อติดต่อนี้
        //  Gdx.app.log("MARIO","fixA"+fixA.getFilterData().categoryBits); //ไว้เช็ค
        //Gdx.app.log("MARIO","fixB"+fixB.getFilterData().categoryBits);//ไว้เช็ค
       /*

        int p = GameFarmer.FIREBALL_BIT | GameFarmer.OBJECT_BIT;
        String b =Integer.toString(p);
        String a =Integer.toString(cDef);
        Gdx.app.log("ssMARIO"+b,a);

        Filter
        การกรองการชนช่วยให้คุณสามารถป้องกันความขัดแย้งระหว่างการแข่งขันได้ ตัวอย่างเช่นสมมติว่าคุณทำ a
        ตัวละครที่ขี่จักรยาน คุณต้องการให้จักรยานชนกับภูมิประเทศและตัวละคร*/
        //getFilterData แปลงค่า Fixture อ่านเป็นเลข
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits; //categoryBits จัดบิตหมวดหมู่การชนกัน
        // Gdx.app.log("MARIO","cDef"+cDef);//ไว้เช็ค
        //fix A 64 | fix B 64 = cDef 64
        //fix A 1 | fix B 2 = cDef 3
        //getFilterData() เอาค่าข้อมูลการกรองที่อยู่การติดต่อ


        //กำหนดค่าชนกันเคลื่อนไหวตัวละคร
        //cDef ตรวจสอบว่าค่าสองค่าเป็นอะไร ชนกัน
        //MARIO_HEAD_BIT เป็น public static final short คือเลข 512 จาก GameFarmer
        switch (cDef){
            case GameFarmer.MARIO_HEAD_BIT | GameFarmer.BRICK_BIT:
            case GameFarmer.MARIO_HEAD_BIT | GameFarmer.COIN_BIT:

                if (fixA.getFilterData().categoryBits == GameFarmer.MARIO_HEAD_BIT)//ถ้า fixA == MARIO_HEAD_BIT โหม่ง 512
                    //คลาส InteractiveTileObject ปฏิกิยาตอบโต้ออปเจค Tile
                    //getUserData อ่านค่าข้อมูลผู้ใช้ หรือ รับข้อมูลผู้ใช้
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());//=fix B คือ COIN_BIT โดนมาริโอ้ fix A คือ MARIO_HEAD_BIT โหม่ง

                else//แสดงว่า fixB == MARIO_HEAD_BIT โหม่ง 512
                    ( (InteractiveTileObject)fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());//=fix A คือ COIN_BIT  โดนมาริโอ (fix B คือ MARIO_BIT)โหม่ง
                break;
            case GameFarmer.ENEMY_HEAD_BIT | GameFarmer.MARIO_BIT: // or | ถ้าอย่างน้อยหนึ่งในสอง บิตนั้นเป็น 1 ผลลัพธ์ของการ or ในบิตนั้นก็จะเป็น 1
                Gdx.app.log("MARIO","HIT_HEAD");
                if (fixA.getFilterData().categoryBits == GameFarmer.ENEMY_HEAD_BIT)

                    ((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());

                break;
            case GameFarmer.ENEMY_BIT | GameFarmer.OBJECT_BIT: //ศัตรู ชน สิ่งของ

                //  Gdx.app.log("MARIO","go");
                if (fixA.getFilterData().categoryBits == GameFarmer.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);

                break;
            case GameFarmer.MARIO_BIT | GameFarmer.ENEMY_BIT: //มาริโอ ชน ศัตรู
                Gdx.app.log("MARIO","TOUCH_ENEMY");
                if (fixA.getFilterData().categoryBits == GameFarmer.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case GameFarmer.MARIO_BIT | GameFarmer.DOWNSCREEN: //มาริโอ ชน พื้นล่าง
                Gdx.app.log("MARIO","DOWNSCREEN");
                if (fixA.getFilterData().categoryBits == GameFarmer.MARIO_BIT)
                    ((Mario)fixA.getUserData()).downdie();
                else
                     ((Mario)fixB.getUserData()).downdie();
                break;
            case GameFarmer.MARIO_BIT | GameFarmer.LEVEL2: //มาริโอ ชน พื้นล่าง
                Gdx.app.log("MARIO","DOWNSCREEN");
                if (fixA.getFilterData().categoryBits == GameFarmer.MARIO_BIT)
                    ((Mario)fixA.getUserData()).Level2();
                else
                    ((Mario)fixB.getUserData()).Level2();
                break;
            case GameFarmer.ENEMY_BIT: //ศัตรู ชน ศัตรู
                //  Gdx.app.log("ENEMY","TOUCH");
                ((Enemy)fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
                break;
            case GameFarmer.ITEM_BIT | GameFarmer.OBJECT_BIT:
                Gdx.app.log("ITEM_BIT","TOUCH_OBJECT");
                if (fixA.getFilterData().categoryBits == GameFarmer.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true,false);

                break;
            case GameFarmer.ITEM_BIT | GameFarmer.MARIO_BIT: //ไอเทม ชน มาริโอ
                Gdx.app.log("ITEM_BIT","TOUCH_MARIO");
                if (fixA.getFilterData().categoryBits == GameFarmer.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());

                break;

            case GameFarmer.FIREBALL_BIT | GameFarmer.OBJECT_BIT:// ไฟ ชน สิ่งของ

                Gdx.app.log("MARIO","fire");
                if(fixA.getFilterData().categoryBits == GameFarmer.FIREBALL_BIT) {
                    ((FireBall) fixA.getUserData()).setToDestroy();

                }
                else{
                    ((FireBall)fixB.getUserData()).setToDestroy();

                }
                break;
            case GameFarmer.FIREBALL_BIT | GameFarmer.ENEMY_BIT  :// ไฟ ชน ศัตรู
                Gdx.app.log("MARIO","fire");

                if(fixA.getFilterData().categoryBits == GameFarmer.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).hitFire((FireBall) fixB.getUserData());

                    ((FireBall)fixB.getUserData()).setToDestroy();

                }

                else {
                    ((Enemy) fixB.getUserData()).hitFire((FireBall) fixA.getUserData());

                    ((FireBall)fixA.getUserData()).setToDestroy();

                }
                break;
            case GameFarmer.KICKASS_BIT | GameFarmer.OBJECT_BIT:// ไฟ ชน สิ่งของ


                if(fixA.getFilterData().categoryBits == GameFarmer.KICKASS_BIT) {

                    ((KickAss) fixA.getUserData()).setToDestroy();
                }
                else{

                    ((KickAss)fixB.getUserData()).setToDestroy();
                }
                break;
            case GameFarmer.KICKASS_BIT | GameFarmer.ENEMY_BIT  :// ไฟ ชน ศัตรู
                Gdx.app.log("MARIO","fire");

                if(fixA.getFilterData().categoryBits == GameFarmer.ENEMY_BIT) {

                    ((Enemy) fixA.getUserData()).hitKickAss((KickAss) fixB.getUserData());

                    ((KickAss)fixB.getUserData()).setToDestroy();
                }

                else {

                    ((Enemy) fixB.getUserData()).hitKickAss((KickAss) fixA.getUserData());

                    ((KickAss)fixA.getUserData()).setToDestroy();
                }
                break;


            case GameFarmer.COIN_SHOW | GameFarmer.MARIO_BIT: //มาริโอ ชน ศัตรู
                Gdx.app.log("MARIO","COIN_BIT");

                if (fixA.getFilterData().categoryBits == GameFarmer.COIN_SHOW)

                    ((Enemy)fixA.getUserData()).hitOnCOIN((Mario) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnCOIN((Mario) fixA.getUserData());

                break;




        }


        // Gdx.app.log("Begin Contact",""); //เริ่มการติดต่อ


    }
//เมื่อตัวละครออกจากการสัมผัสกัน
    @Override
    public void endContact(Contact contact) {//จบการติดต่อ
        // Gdx.app.log("End Contact","");//จบการติดต่อ

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { //ก่อนแก้

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {//โพสแก้

    }
}
