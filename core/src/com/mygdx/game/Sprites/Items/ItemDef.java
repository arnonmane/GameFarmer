package com.mygdx.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by arnat on 4/7/2560.
 */

//ItemDef คือตำแหน่งและชนิดคลาส
public class ItemDef {

    public Vector2 position;//ตำแหน่ง
    public Class<?>type;//ชนิด คลาสไอเทม

    public ItemDef(Vector2 position , Class<?>type){//ตำแหน่ง Vector2 xกับy ,ประเภทของ class
        this.position =position;//กำหนดตำแหน่ง ไอเท็ม
        this.type=type;//สร้างคลาสอะไร
    }

}
