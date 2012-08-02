package com.rushfusion.weather.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;
public class WeatherService {
	File databaseDir;
	private String DBNAME = "weather_rushfunsion.db";
	private Context mContext;
	public WeatherService(Context context) {
		this.mContext=context;
	}
	
	/**
	 * 判断数据库是否存在于应用所在包的databases文件夹下
	 * @return
	 */
	public boolean isDBExist() {
		File file = mContext.getDatabasePath(DBNAME);//获取数据库文件的File对象，会在应用所在包的databases文件夹下寻找该文件
		return file.exists();
	}
	/**
	 * 拷内数据库至应用所在包的databases文件夹下
	 */
	public void copyDB() throws Exception {
		 databaseDir = new File(mContext.getFilesDir().getParentFile(), "databases");// <包>/databases
		
		if(!databaseDir.exists()) databaseDir.mkdirs();
		File dbFile = new File(databaseDir, DBNAME);
		FileOutputStream outStream = new FileOutputStream(dbFile);
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(DBNAME);
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
	}
	/**
	 *拿到对于city的编码
	 * @param city
	 * @return
	 */
	public String  getCityCode(String city){
		String  cityKey=null;
	   SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
		Log.d("isOpen", "db:>>>"+db.isOpen()) ;
		String path = db.getPath();
		Cursor cursor = db.query("city", new String[]{"cityKey"},
				"cityName=?", new String[]{city}, null, null, null);
		System.out.println("getCityCode==我已经被方法调用打开");
	//	Cursor cursor = db.rawQuery("select cityKey from city where cityName = '"+city+"'", null) ;
		//select cityKey from city where cityName =　？
		if(cursor.moveToFirst()){
			cityKey = cursor.getString(cursor.getColumnIndex("cityKey"));
			
		}
		if(!cursor.isClosed()){
		cursor.close();
		}
		db.close();
		return cityKey;
	}
	/**
	 * 查询所有的省份
	 * @return
	 */
	public List<String> getAllProv(){
		List<String> prov = new ArrayList<String>();
		   SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
      Cursor cursor = db.rawQuery("select ProvName from  prov order by pinyinforprov",null);
      for(cursor.moveToNext();!cursor.isAfterLast();cursor.moveToNext()){
			String  provname = cursor.getString(cursor.getColumnIndex("ProvName"));
			prov.add(provname);
		}
      if(!cursor.isClosed()){
  		cursor.close();
  		}
  		db.close();
		return prov;
	}
	/**
	 * 通过省份查询id
	 * @param name
	 * @return
	 */
	public int getproid(String name){
		int  id =0;
		List<String> prov = new ArrayList<String>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
		Cursor cursor = db.rawQuery("select id from  prov where ProvName =? ",new String[]{name});
		if(cursor.moveToFirst()){
			id = cursor.getInt(cursor.getColumnIndex("id"));
			
		}
		if(!cursor.isClosed()){
			cursor.close();
			}
			db.close();
			return id;
	}
	/**
	 * 通过地区名查询自己所有的县区
	 * @param name
	 * @return
	 */
	public List<String> getCityname(String name){
		List<String> cityname = new ArrayList<String>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
		Cursor cursor = db.rawQuery("select cityname from city where city.districtid=(select id from district where districtname=?) order by pinyinforcity ",new String[]{name});
		for(cursor.moveToNext();!cursor.isAfterLast();cursor.moveToNext()){
			String  distname = cursor.getString(cursor.getColumnIndex("cityName"));
			cityname.add(distname);
		}
    if(!cursor.isClosed()){
		cursor.close();
		}
		db.close();
		return cityname;
	}
	/**
	 * 通过相应的省id查询地区
	 * @param provid
	 * @return
	 */
	 public List<String> getDist(int provid){
		 List<String> dist = new ArrayList<String>();
		   SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
    Cursor cursor = db.rawQuery("select districtName from district  where proId ="+provid+" order by pinyinfordistrict",null);
    for(cursor.moveToNext();!cursor.isAfterLast();cursor.moveToNext()){
			String  distname = cursor.getString(cursor.getColumnIndex("districtName"));
			dist.add(distname);
		}
    if(!cursor.isClosed()){
		cursor.close();
		}
		db.close();
		return dist;
	 }
	/**
	 * 
	 * @param cityCode
	 * @return
	 * @throws Exception
	 */
     public Weatherinfo getWeatherInfo(String cityCode) throws Exception{
    	   /*     "city":"成都",
         "city_en":"chengdu",
         "date_y":"2011年11月30日",
         "date":"辛卯年",
         "week":"星期三",
         "fchh":"11",           //预报发布时间
         "cityid":"101270101",
         "temp1":"13℃~10℃",
         "temp2":"14℃~6℃",
         "temp3":"13℃~5℃",
         "temp4":"14℃~8℃",
         "temp5":"10℃~8℃",
         "temp6":"11℃~6℃",
         "tempF1":"55.4℉~50℉",
         "tempF2":"57.2℉~42.8℉",
         "tempF3":"55.4℉~41℉",
         "tempF4":"57.2℉~46.4℉",
         "tempF5":"50℉~46.4℉",
         "tempF6":"51.8℉~42.8℉",
         "weather1":"阴转多云",
         "weather2":"多云转晴",
         "weather3":"多云转阴",
         "weather4":"阵雨",
         "weather5":"阵雨转小雨",
         "weather6":"小雨转阴",
         "img1":"2",
         "img2":"1",
         "img3":"1",
         "img4":"0",
         "img5":"1",
         "img6":"2",
         "img7":"3",
         "img8":"99",
         "img9":"3",
         "img10":"7",
         "img11":"7",
         "img12":"2",
         "img_single":"2",
         "img_title1":"阴",
         "img_title2":"多云",
         "img_title3":"多云",
         "img_title4":"晴",
         "img_title5":"多云",
         "img_title6":"阴",
         "img_title7":"阵雨",
         "img_title8":"阵雨",
         "img_title9":"阵雨",
         "img_title10":"小雨",
         "img_title11":"小雨",
         "img_title12":"阴",
         "img_title_single":"阴",
         "wind1":"北风小于3级",
         "wind2":"北风小于3级",
         "wind3":"北风小于3级",
         "wind4":"南风转北风小于3级",
         "wind5":"北风小于3级",
         "wind6":"北风小于3级",
         "fx1":"北风",
         "fx2":"北风",
         "fl1":"小于3级",
         "fl2":"小于3级",
         "fl3":"小于3级",
         "fl4":"小于3级",
         "fl5":"小于3级",
         "fl6":"小于3级",
         "index":"舒适",
         "index_d":"建议着薄型套装或牛仔衫裤等春秋过渡装。年老体弱者宜着套装、夹克衫等。",
         "index48":"舒适",
         "index48_d":"建议着薄型套装或牛仔衫裤等春秋过渡装。年老体弱者宜着套装、夹克衫等。",
         "index_uv":"最弱",
         "index48_uv":"弱",
         "index_xc":"适宜",
         "index_tr":"很适宜",
         "index_co":"较舒适",
         "st1":"14",
         "st2":"10",
         "st3":"14",
         "st4":"6",
         "st5":"13",
         "st6":"4",
         "index_cl":"较适宜",
         "index_ls":"不太适宜",
         "index_ag":"较易发"*/
    	 SourceDownLoader sd = new SourceDownLoader();
    	 String path = "http://m.weather.com.cn/data/"+cityCode+".html";
    	  String weatherinfo = sd.getallString(path);
    	  JSONObject jswea = new JSONObject(weatherinfo);
    	  JSONObject js =jswea.getJSONObject("weatherinfo");
    	  Weatherinfo wi = new Weatherinfo();
    	  wi.setCity(js.getString("city"));
    	  wi.setCity_en(js.getString("city_en"));
    	  wi.setCtiyid(js.getString("cityid"));
    	  wi.setDate_y(js.getString("date_y"));
    	  wi.setDate(js.getString("date"));
    	  wi.setFchh(js.getInt("fchh"));
    	  wi.setTemp1(js.getString("temp1"));
    	  wi.setTemp2(js.getString("temp2"));
    	  wi.setTemp3(js.getString("temp3"));
    	  wi.setTemp4(js.getString("temp4"));
    	  wi.setTemp5(js.getString("temp5"));
    	  wi.setTemp6(js.getString("temp6"));
    	  wi.setWeather1(js.getString("weather1"));
    	  wi.setWeather2(js.getString("weather2"));
    	  wi.setWeather3(js.getString("weather3"));
    	  wi.setWeather4(js.getString("weather4"));
    	  wi.setWeather5(js.getString("weather5"));
    	  wi.setWeather6(js.getString("weather6"));
    	  wi.setWind1(js.getString("wind1"));
    	  wi.setWind2(js.getString("wind2"));
    	  wi.setWind3(js.getString("wind3"));
    	  wi.setWind4(js.getString("wind4"));
    	  wi.setWind5(js.getString("wind5"));
    	  wi.setWind6(js.getString("wind6"));
    	  wi.setImg_title1(js.getString("img_title1"));
    	  wi.setImg_title2(js.getString("img_title2"));
    	  wi.setImg_title3(js.getString("img_title3"));
    	  wi.setImg_title4(js.getString("img_title4"));
    	  wi.setImg_title5(js.getString("img_title5"));
    	  wi.setImg_title6(js.getString("img_title6"));
    	  wi.setImg_title7(js.getString("img_title7"));
    	  wi.setImg_title8(js.getString("img_title8"));
    	  wi.setImg_title9(js.getString("img_title9"));
    	  wi.setImg_title10(js.getString("img_title10"));
    	  wi.setImg_title11(js.getString("img_title11"));
    	  wi.setImg_title12(js.getString("img_title12"));
    	  wi.setIndex_co(js.getString("index_co"));
    	 return wi;
     }
     
     /**
      * 这个是辅助类，自用，用来增加pinyin这个列
      * 
      */
     public ArrayList<String> getname(String name){
    	 ArrayList<String> cityname = new ArrayList<String>();
 		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
 		Cursor cursor = db.rawQuery("select "+name+"Name from "+name+" ",new String[]{});
 		for(cursor.moveToNext();!cursor.isAfterLast();cursor.moveToNext()){
 			String  distname = cursor.getString(cursor.getColumnIndex(""+name+"Name"));
 			cityname.add(distname);
 		}
     if(!cursor.isClosed()){
 		cursor.close();
 		}
 		db.close();
 		return cityname;
 	}
     //汉字转为拼音
     public String hanziToPinyin(long rawContactId){  
         String result="";  
       
         String Where = ContactsContract.RawContacts.CONTACT_ID+ " ="+rawContactId;  
         String[] projection = {"sort_key" };  
         Cursor cur = mContext.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, Where, null, null);  
         int pinyin1=cur.getColumnIndex("sort_key");  
         cur.moveToFirst();  
         String pinyin=cur.getString(pinyin1); 
         if(!cur.isClosed()){
        	 cur.close();
         }
         System.out.println("rawContactId==="+rawContactId+"pinyin=====>"+pinyin);
         //因为此处得到的事ZHANG张SAN三这个形式，所以下面对这个字符串做处理，将它变成 zhang san  
         for(int i=0;i<pinyin.length();i++){  
         String temp=pinyin.substring(i,i+1);  
             if(temp.matches("[a-zA-Z]")){  
                 result=result+temp; 
             }  
         }  
         return result.toLowerCase();
     }  
     
     public void delete(long rawContactId)  
     {  
        mContext.getContentResolver().delete(ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId), null, null);
     }  
     
     //根据城市得到相应的拼音
     public ArrayList<String> getPinyin(ArrayList<String> cityList){
    	 ArrayList<String> pinyinList = new ArrayList<String>();
         for (int i = 0; i < cityList.size(); i++) {
			String cityname = cityList.get(i);
			ContentValues values = new ContentValues();  
			Uri rawContactUri =  mContext.getContentResolver().insert(RawContacts.CONTENT_URI, values);  
			long rawContactId = ContentUris.parseId(rawContactUri);    
			 if (cityname.length()!=0)  
             {  
                 values.clear();  
                 values.put(Data.RAW_CONTACT_ID, rawContactId);  
                 values.put(Data.MIMETYPE,StructuredName.CONTENT_ITEM_TYPE);  
                 values.put(StructuredName.GIVEN_NAME, cityname);  
                 mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);  
                 String pinyin = hanziToPinyin(rawContactId); 
                 pinyinList.add(pinyin);
                 delete(rawContactId);  
             } 
		}
         return pinyinList;
     }
      //把数据插入到数据库的对应的表中
     public void insertData(ArrayList<String> cityList,ArrayList<String> pinyinList,String tableName){
    	 ContentValues values=new ContentValues();
    	 SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath("weather_rushfunsion.db") ,null);
    	 for (int i = 0; i < cityList.size(); i++) {
    		 String pinyin = pinyinList.get(i);
    		 values.put("pinyinfor"+tableName+"", pinyin);
    		 System.out.println("insertData=====我已经执行过一次");
    		 //相当于set name=Beauty where id=3
    		 db.update(tableName, values,""+tableName+"Name=?" ,new String[]{cityList.get(i)});
		}
    	 db.close();
     }
}
