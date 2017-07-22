package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class GameFarmer extends Game {
	// this  ใช้เพื่อเรียก ตัวแปร หรือ method ของ Class เราเอง
	// super ใช้เพื่อเรียก ตัวแปร หรือ method ของ Class แม่ที่เราสืบทอดมา

	// ขนาดหน้าจอเสมือนจริงและขนาด Box2D (Pixels per Meter)
	public static final int V_WIDTH=400;
	public static final int V_HEIGHT=208;

	/*// fixtureDef.restitution = 0.5f; restitution คือค่าการเด้งของวัตถุ ค่า 0.5f
	Note! เรื่องที่คุณยังไม่รู้เกี่ยวกับ หน่วย unit ใน Box2D จะมีค่าเท่ากับ 1 เมตรนะครับ ฉะนั้น ที่สร้างลูกบอลไป มีขนาดรัศมี 50 เมตร
	โอ้ว ใหญ่ทับสนามฟุตบอลได้เลย :D แต่ว่าหน่วยใน LibGDX เป็น pixel ฉะนั้น เวลาเรากำหนดหน่วยทุกๆอย่าง ใน Box2D เราก็ต้อง scale ให้เป็นหน่วยเมตร ซะก่อนครับ
	*/
	//สร้างตัวแปรขึ้นมาใหม่ ชื่อ PPM โดยกำหนดไว้ที่ 100 คือ หน่วยใน Box2D จะถูกหารด้วย 100 ฉะนั้นลูกบอล ลูกใหม่ จะมีขนาด 50 เซนติเมตร อ่า กำลังดีละครับ ไม่ใหญ่มาก
	public static final float PPM=100;



	//การชนกันของวัสดุต่างๆ

	//BIT เปรียบเป๋นโครงร่างที่ชนได้
	//เลขขึ้นตาม Bit 4 ไป 8 ไป 16
	//ใช้เพราะเป็น 16 บิต
	//short  x31 = 32767; เป็นการประกาศตัวแปรชนิด short ชื่อ x31  เก็บข้อมูลเป็นตัวเลขฐานสิบมีค่าข้อมูลเท่ากับ 32,767


//ที่ short เรียงเลขไม่ได้เพราะเลขมันซ้ำ

	public static final short NOTHING_BIT = 0;//ไม่มีอะไร
	public static final short GROUND_BIT=1; //พื้น
	public static final short MARIO_BIT=2;//มาริโอ
	public static final short BRICK_BIT=4;//อิฐ
	public static final short COIN_BIT=8;//เหรียญ
	public static final short DESTROYED_BIT=16;//ทำลายบิต
	public static final short OBJECT_BIT=32;//ออฟเจคท่อต่าง
	public static final short ENEMY_BIT=64;//ศัตรู
	public static final short ENEMY_HEAD_BIT = 128;//หัวศัตรู
	public static final short ITEM_BIT = 256;//ไอเท็ม
	public static final short MARIO_HEAD_BIT = 512;//มาริโอโหม่ง
	public static final short FIREBALL_BIT = 1024;//ไฟบอล
	public static final short KICKASS_BIT = 16284;//แตะ
	public static final short COIN_SHOW = 2048;//ไฟบอล
	public static final short DOWNSCREEN = 4096;//ไฟบอล
    public static final short LEVEL2 = 8192;//ไฟบอล



	public SpriteBatch batch;//มาช่วยในการวาดรูปนั่นเอง


/* คำเตือนการใช้ AssetManager แบบคงที่อาจทำให้เกิดปัญหาโดยเฉพาะอย่างยิ่งบน Android
	คุณอาจต้องการส่งผ่าน Assetmanager ไปยังชั้นเรียนที่ต้องการ
	เราจะใช้มันในบริบทคงที่เพื่อประหยัดเวลาสำหรับตอนนี้ */




	public static AssetManager manager;



	@Override
	public void create () {
		batch = new SpriteBatch();//ใช้สำหรับเรนเดอร์ออปเจ็คบนหน้าจอ เช่น Texture ส่วน BitmapFont ใช้สำหรับเรนเดอร์ข้อความบนหน้าจอครับ โดยใช้ร่วมกับ SpriteBatch

		manager=new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);

		manager.finishLoading();


		setScreen(new PlayScreen(this, true)); //setScreen ส่งไป PlayScreen

	}
	//สุดท้าย อย่าลืมเมธอด dispose()
	public void dispose(){
		super.dispose();
		manager.dispose();
		batch.dispose();

	}


	//อย่าลืมเรียก super.render() ในเมธอด render ที่เรา implement คลาส Game มาด้วยนะครับ ไม่อย่างนั้น Screen ที่เราทำการสร้างตรงเมธอด create() จะไม่เรนเดอร์อะไรออกมาเลย
	@Override
	public void render () {

		super.render();
	}

	public static String bitToText(short bit){

		switch (bit){
			case 1:
				return "DEFAULT";
			case 2:
				return "MARIO";
			case 4:
				return "BRICK";
			case 8:
				return "COIN";
			case 16:
				return "DESTROYED";
			case 32:
				return "ENEMY";
			case 64:
				return "GROUND";
			default:
				return "DEFAULT";
		}
	}


}


/*การสร้างตัวละคร
	world ในโลก
		bodies ร่างกาย
			mass มวล ค่าน้ำหนักของตัววัตถุ โดยใช้หน่วยเป็น kg
			velocity ความเร็ว
			location ที่ตั้ง
			angles มุม
			fixtures ติดตั้งภาพ
				shape รูปร่าง
				density ความหนาแน่น
				friction ค่าแรงเสียดทานที่เกิดขึ้นกับตัววัตถุ เมื่อมีการสัมผัสกับวั
				restitution คือค่าบ่งบอกถึงการทดแทนแรงที่สุญเสียไปในการชน หาค่านั้นมีค่าน้อย จะมีการทดแทนแรงได้น้อย (Default = 0)โดยใช้หน่วยเป็น kg



	การกำหนดให้ค่าแรงฟิสิกส์ให้กับวัตถุ ภายในระบบของ Farseer
	ApplyLinearImpulse โดยใช้หน่วยเป็น kg-m/s


	รูปทรง (Shape) ของวัตถุ ที่ Farseer กำหนดไว้ ในบางกรณีอาจจะไม่มีรูปทรงตามต้องการ จำเป็นต้องมีการสร้าง Vertices กำหนดจุดรูปร่างของวัตถุ



//คำสั่งที่ใช่ในการสร้างรูปทรงคือ
m_jewel_02 = BodyFactory.CreatePolygon(m_world, _vertices_02, 1f);

 */























