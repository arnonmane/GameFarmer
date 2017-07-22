package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameFarmer;

/**
 * Created by arnat on 4/7/2560.
 */
//กล้องภาพนิ่ง Hud
public class Hud implements Disposable {

    public Stage stage;//Stage คือ InputProcessor เปรียบเสมือนตัวรับอินพุทต่างๆ ประมาณมันคอยตรวจจับ input เช่น หากเราต้องการสร้าง Button ปุ่มกด เราก็จำเป็นจะต้องใช้ Stage ครับ
    private Viewport viewport;

    // คะแนนมาริโอ / เวลาตัวแปรติดตาม
    private Integer worldTimer;
    private boolean timeUp; //หมดเวลา
    private float timeCount;
    private static Integer score;
    private static String nameskill;

    // เครื่องมือฉาก 2D
    private Label countdownLabel;
    private static Label skill;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb) {
        Gdx.app.log("Hud"+GameFarmer.V_WIDTH,"Hud"+GameFarmer.V_HEIGHT);
        // กำหนดตัวแปรติดตามของเรา
        worldTimer = 300;//เวลาโลก
        timeCount = 0;//นับเวลา
        score = 0;//แต้ม
        nameskill = "kick";//แต้ม

        // ตั้งค่ามุมมอง HUD โดยใช้กล้องตัวใหม่แยกจาก gamecam ของเรา


        // กำหนดระยะของเราโดยใช้ viewport และเกม spritebatch ของเรา
        //FitViewport ทำให้อัตราส่วนโดยการปรับโลกเพื่อให้พอดีกับหน้าจอสืพทอดมาจาก ScalingViewport ที่ใช้การปรับขนาด
        //สร้างวิวพอร์ตใหม่โดยใช้ OrthographicCamera ใหม่
        viewport = new FitViewport(400, 208, new OrthographicCamera());//OrthographicCamera กล้องที่มีการฉายภาพ
        // สร้างเฟรมด้วย viewport,sb ที่ระบุ นี้สามารถใช้เพื่อหลีกเลี่ยงการสร้างชุดใหม่ (ซึ่งสามารถค่อนข้างช้า) ถ้าใช้หลายขั้นตอนในช่วงชีวิตของโปรแกรมประยุกต์
        stage = new Stage(viewport,sb);


        // กำหนดตารางที่ใช้ในการจัดระเบียบฉลากของ hud ของเรา
        Table table =new Table();
        //อยู่บน
        table.top();
        // ทำให้ตารางเติมทั้งเวที
        table.setFillParent(true);

        // กำหนดป้ายกำกับของเราโดยใช้ String และสไตล์ Label ซึ่งประกอบด้วยแบบอักษรและสี
        // Label ป้ายข้อความพร้อมกับการตัดคำที่เป็นตัวเลือก
        //LabelStyle สไตล์ของ Label ป้ายกำกับให้ดูที่ป้ายกำกับ
        //BitmapFont แสดงแบบอักษรบิตแมป แบบอักษรประกอบด้วย 2 ไฟล์ ได้แก่ ไฟล์รูปภาพหรือ TextureRegion
        countdownLabel=new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        skill=new Label(nameskill,new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel=new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel=new Label("TIME",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel=new Label("1-1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel=new Label("WORLD",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel=new Label("MARIO",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // เพิ่มป้ายชื่อของเราในตารางของเรา padding ด้านบนและให้พวกเขามีความกว้างเท่ากันทั้งหมดกับ expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        // เพิ่มแถวที่สองลงในตารางของเรา
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(skill).expandX();

        // เพิ่มตารางของเราลงบน stage
        stage.addActor(table);

    }

    public  void update(float dt){//อัพเดตเวลา
        timeCount += dt; // timeCount = timeCount + dt;
        if(timeCount >= 1){//if นับเวลา >= 1
            if (worldTimer > 0) { //if เวลาโลก > 0
                worldTimer--; //ให่ worldTimer = worldTimer - 1;
            } else {//ถ้าไม่
                timeUp = true; //กำหนดหมดเวลา
            }
            countdownLabel.setText(String.format("%03d", worldTimer));//กำหนดค่าเวลาตจนนี้

            timeCount = 0;//นับเวลาเป็น 0
        }
    }

    public static void setnameskill(String value){//เพิ่ทสกอร์


        skill.setText(value); //เพิ่ม score เข้าไปโชว์
    }

    public static void addScore(int value){//เพิ่ทสกอร์

        score += value;//เพื่มค่าเข้าไปใน score
        scoreLabel.setText(String.format("%06d",score)); //เพิ่ม score เข้าไปโชว์
    }

    // กำจัด stage
    @Override
    public void dispose() {

        stage.dispose();
    }

    public boolean isTimeUp(){//มันหมดเวลา

        return timeUp;
    }

}
