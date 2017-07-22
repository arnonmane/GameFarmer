package com.mygdx.game.ButtonController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by arnat on 4/7/2560.
 */


public class Controller {


    Image upImg,downImg,leftImg,righImg;
    Skin skin;
    TextureAtlas buttonAtlas;
    private Rectangle ButtonScreenDown;//รัศมีพื้นซ้าย
    private Rectangle ButtonScreenRight;//รัศมีพื้นขวา
    private Rectangle ButtonScreenLeft;//รัศมีพื้นซ้าย
    private Rectangle ButtonScreenUp;//รัศมีพื้นขวา

    private Vector3 touchPoint;//จุดที่สัมผัส

    Viewport viewport;//Viewport จัดการกล้องและกำหนดวิธีการจับคู่พิกัดของโลกกับและจากหน้าจอ
    Stage stage;//Stage กราฟภาพ 2D ที่มีลำดับชั้นของ actors นักแสดง Stage จัดการมุมมองและกระจายกิจกรรมการป้อนข้อมูล

    OrthographicCamera cam;//OrthographicCamera กล้องที่มีการฉายภาพ

    int screenWidth;
    int screenHeight;
    public Controller(SpriteBatch batch, int width, int height){
//สร้างออฟเจค
        Gdx.app.log("Controller"+width,"Controller"+height);
        this.screenWidth=width;
        this.screenHeight=height;

        cam = new OrthographicCamera();
        viewport = new FitViewport(width,height,cam); //FitViewport ( ขนาดความกว้างงจอ, ขนาดความสูงจอ, กล้องที่ฉาย)
        stage = new Stage(viewport,batch);//Stage(วิวกล้อง, สิ่งที่วาดลงมา)//สร้างเฟรมด้วยวิวพอร์ตและแบทช์ที่ระบุ
        Gdx.input.setInputProcessor(stage);//ตั้งค่า InputProcessor ที่จะรับเหตุการณ์การป้อนข้อมูลและการป้อนข้อมูลทั้งหมด

        setUpTouchControlAreas();

        //

         /*
        int firstX = Gdx.input.getX();//.input.getX()จุดสัมผัส x
        int firstY = Gdx.input.getY();//.input.getY()จุดสัมผัส y
        int secondX = Gdx.input.getX(1);
        int secondY = Gdx.input.getY(1);
        Gdx.app.log("P1"+"x"+ secondX+"y"+secondY,"");
        Gdx.app.log("P2"+"x"+ firstX+"y"+firstY,"");*/




        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("ButonControl.pack"));
        skin.addRegions(buttonAtlas);//addRegions เพิ่มเนื้อที่ที่มีชื่อทั้งหมดจาก Atlas สมุดแผนที่




//ค่าเริ่มเกม
        //กำหนดรูปปุ่ม
         upImg =new Image(); //สร้างรูป Image(new Texture(เส้นทาง texture//"flatDark25.png"))
        // upImg.setSize(70,70);//ตั้งค่ารูป setSize(กว้าง,สูง)

        upImg.setBounds(screenWidth-100, 0, 100, 100);//กำหนดขอบเขต
        upImg.setDrawable(skin.getDrawable("flatDark25"));

            stage.addActor(upImg);





         downImg = new Image();

        downImg.setBounds(screenWidth-100, 120, 100, 100);//กำหนดขอบเขต
        downImg.setDrawable(skin.getDrawable("flatDark26"));
        stage.addActor(downImg);


         righImg = new Image();
        righImg.setBounds(100, 0, 100, 100);//กำหนดขอบเขต
        righImg.setDrawable(skin.getDrawable("flatDark24"));
        stage.addActor(righImg);



         leftImg = new Image();
        leftImg.setBounds(0, 0, 100, 100);//กำหนดขอบเขต
        leftImg.setDrawable(skin.getDrawable("flatDark23"));
        stage.addActor(leftImg);






    }
    public void draw(){ //วาด

        stage.draw();//วาด stage เฟรมภาพ

    }


    public void resize(int width, int height){//ปรับขนาด

        viewport.update(width ,height);//ปรับขนาดอัพเดต วิวกล้อง
    }




    private void setUpTouchControlAreas() {//ตั้งค่าแตะพื้นที่ควบคุม ซ้าย/ขวา

        ButtonScreenLeft = new Rectangle(0, screenHeight-100, 100, 100);//รํศมีพื้นที่ ปุ่มซ้าย
        ButtonScreenRight = new Rectangle(100, screenHeight-100, 100, 100);//รํศมีพื้นที่ ปุ่มขวา
        ButtonScreenDown = new Rectangle(screenWidth-100, screenHeight-220, 100, 100);//รํศมีพื้นที่ซ้าย // กว้างครึ่งของจอ
        ButtonScreenUp = new Rectangle(screenWidth-100, screenHeight-100, 100, 100);//รํศมีพื้นที่ ปุ่มบน

    }

    public boolean rightTouched(float x, float y) {//สัมผัสด้านขวา

        return ButtonScreenRight.contains(x, y);//มีค่า(x,y)ตรงกับใน ButtonScreenRight หรือไม่
    }

    public boolean leftTouched(float x, float y) {//สัมผัสด้านขวา

        return ButtonScreenLeft.contains(x, y);//มีค่า(x,y)ตรงกับใน ButtonScreenLeft หรือไม่
    }


    public boolean upTouched(float x, float y) {//สัมผัสด้านขึ้น


        return ButtonScreenUp.contains(x, y);
    }


    public boolean downTouched(float x, float y) {//สัมผัสด้านลง

        return ButtonScreenDown.contains(x, y);
    }



    public  void update(float dt){//อัพเดตเวลา


        int firstX = Gdx.input.getX(0);//.input.getX()จุดสัมผัสที่1 x
        int firstY = Gdx.input.getY(0);//.input.getY()จุดสัมผัสที่1 y
        int secondX = Gdx.input.getX(1);
        int secondY = Gdx.input.getY(1);
        boolean firstFingerTouching = Gdx.input.isTouched(0);//เช็คว่าสัมผัสจุดที่ 1 อยู่หรือไม่
        boolean secondFingerTouching = Gdx.input.isTouched(1);//เช็คว่าสัมผัสจุดที่ 2 อยู่หรือไม่

        if(firstFingerTouching) {
          //  Gdx.app.log("firstFinger","in");
            if (rightTouched(firstX, firstY)) {//ถ้าสัมผัสด้านขวา
                righImg.setDrawable(skin.getDrawable("flatDark242"));

                stage.addActor(righImg);



            }else {
                righImg.setDrawable(skin.getDrawable("flatDark24"));

                stage.addActor(righImg);
            }

            if (downTouched(firstX, firstY)) {
                downImg.setDrawable(skin.getDrawable("flatDark262"));

                stage.addActor(downImg);


            }else {
                downImg.setDrawable(skin.getDrawable("flatDark26"));

                stage.addActor(downImg);


            }

            if (upTouched(firstX, firstY)) {

                upImg.setDrawable(skin.getDrawable("flatDark252"));

                stage.addActor(upImg);


            } else {
                upImg.setDrawable(skin.getDrawable("flatDark25"));

                stage.addActor(upImg);

            }


            if(leftTouched(firstX, firstY)) {
                leftImg.setDrawable(skin.getDrawable("flatDark232"));
                stage.addActor(leftImg);
            }else {

                leftImg.setDrawable(skin.getDrawable("flatDark23"));
                stage.addActor(leftImg);
            }

        }else { //ถ้านิ้วที่ 1 ไม่สัมผัส ลง
           // Gdx.app.log("firstFinger","out");


            if (rightTouched(firstX, firstY)) {//ถ้าสัมผัสด้านขวา
                righImg.setDrawable(skin.getDrawable("flatDark24"));

                stage.addActor(righImg);



            }

            if (downTouched(firstX, firstY)) {
                downImg.setDrawable(skin.getDrawable("flatDark26"));

                stage.addActor(downImg);


            }

            if (upTouched(firstX, firstY)) {

                upImg.setDrawable(skin.getDrawable("flatDark25"));

                stage.addActor(upImg);


            }


            if(leftTouched(firstX, firstY)) {
                leftImg.setDrawable(skin.getDrawable("flatDark23"));
                stage.addActor(leftImg);
            }

        }

        if(secondFingerTouching) {
          //  Gdx.app.log("secondFinger","in");
            if (rightTouched(secondX, secondY)) {//ถ้าสัมผัสด้านขวา

                righImg.setDrawable(skin.getDrawable("flatDark242"));

                stage.addActor(righImg);
            }  if (downTouched(secondX, secondY)) {//หรือถ้าสัมผัสด้านซ้าย
                downImg.setDrawable(skin.getDrawable("flatDark262"));

                stage.addActor(downImg);


            }   if (upTouched(secondX, secondY)) {
                upImg.setDrawable(skin.getDrawable("flatDark252"));

                stage.addActor(upImg);

            }  if(leftTouched(secondX, secondY)) {
                leftImg.setDrawable(skin.getDrawable("flatDark232"));
                stage.addActor(leftImg);
            }

        }else {   //ถ้านิ้วที่ 2 ไม่สัมผัส ลง
            //Gdx.app.log("secondFinger","out");
            if(!firstFingerTouching) {
                downImg.setDrawable(skin.getDrawable("flatDark26"));

                stage.addActor(downImg);


                upImg.setDrawable(skin.getDrawable("flatDark25"));

                stage.addActor(upImg);

                leftImg.setDrawable(skin.getDrawable("flatDark23"));
                stage.addActor(leftImg);


                righImg.setDrawable(skin.getDrawable("flatDark24"));

                stage.addActor(righImg);
            }

        }





    }





}



/*  int firstX = Gdx.input.getX();
        int firstY = Gdx.input.getY();
        int secondX = Gdx.input.getX(1);
        int secondY = Gdx.input.getY(1);
        Gdx.app.log("one x"+firstX,"y"+firstY);
        Gdx.app.log("two x"+secondX,"y"+secondY);*/