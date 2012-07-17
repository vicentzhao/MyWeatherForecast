package cn.rushfunsion;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cn.rushfunsion.domain.Weatherinfo;
import cn.rushfunsion.util.DateUtil;
import cn.rushfunsion.util.WeatherService;
public class MyWeatherActivity  implements Runnable{
		WeatherService weatherService ;
		ProgressDialog      progressDialog ;
		private TextView cityname;
		private TextView curntime;
		private TextView fengli;
		private ImageView image;
		private TextView qiwen;
		private TextView shushidu;
		private TextView tianqi;
		private TextView week;
		private TextView releasetime;
		private String DefaultCtiy = "上海"; 
		private String private_city;
		private Weatherinfo weatherInfo;
		private ListView listview;
		private List<String> datelist;
		private List<String> imagelist;
		private List<String> templist;
		private List<String> weatherlist;
		private Button shezhi_button;
		private Button query_button;
		private int  viewNumber = 1 ;
		private List<String> weeklist;
		private ArrayAdapter spiner_distAdapter;
		private ArrayAdapter spiner_proAdapter;
		private ArrayAdapter spiner_cityAdapter;
		private List<String> allProv;
		private WeatherService  service ;
		private List<String> distname;
		private List<String> citynamelist;
		private String citynameforselct;
		private String cityCode;
		private View textEntryView;
		private static final String LOG_TAG = "MyWeather";
		private	Context mContext;
		private View  view ; 
	    ViewGroup mContainer = null;
			Node  mParams = null;
		public void init(Context context, ViewGroup container, Node  params) {
				mContext = context;
				mContainer = container;
				mParams = params;
		}
			public void run() {				
				  showData();
				
	}
			public void showData(){
				 view =View.inflate(mContext, R.layout.main, null);
				  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1,-1);
					mContainer.removeAllViews();
					mContainer.addView(view, lp);
           initData();
	        if(!weatherService.isDBExist()){
	            	String title = mContext.getResources().getString(R.string.dialogtitle);
	            	String content = mContext.getResources().getString(R.string.dialogcontent);
	            	progressDialog = ProgressDialog.show(mContext, title, content, true);
	            	 try {
						weatherService.copyDB();
						progressDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
	        }
	        try {
	        	if(viewNumber==1||viewNumber==6){
	        		shezhi_button.requestFocus();
	        		SharedPreferences citysetting =mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
	        		String ecityname = citysetting.getString("private_city", DefaultCtiy);
	        		cityname.setText(ecityname);
	        		cityCode= weatherService.getCityCode(ecityname);
				weatherInfo = weatherService.getWeatherInfo(cityCode);
				DateUtil dateUtil = new DateUtil();
				String[] currDay = dateUtil.getCurrDay();
		        curntime.setText(currDay[0]);
		        week.setText(currDay[1]);
		        int ereleasetime = weatherInfo.getFchh();
		        String sreleasetime =ereleasetime+" 时发布";
		        releasetime.setText(sreleasetime);
		        String efengli =weatherInfo.getWind1();
		        fengli.setText(efengli);
		        String eqiwen =weatherInfo.getTemp1();
		        qiwen.setText(eqiwen);
		        String eshushidu =weatherInfo.getIndex_co();
		        shushidu.setText(eshushidu);
		        String etianqi =weatherInfo.getWeather1();
		        tianqi.setText(etianqi);
		        String eimage =weatherInfo.getImg_title1();
		        getImage(eimage);}
	        	else{
	        		shezhi_button.setFocusable(true);
	        		SharedPreferences citysetting = mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
	        		String ecityname = citysetting.getString("private_city", DefaultCtiy);
	        		cityname.setText(ecityname);
	        		int  numberofdata = viewNumber-2;
					curntime.setText(datelist.get(numberofdata));
					week.setText(weeklist.get(numberofdata));
			        qiwen.setText(weatherlist.get(numberofdata));
			        tianqi.setText(templist.get(numberofdata));
			        String eimage =imagelist.get(numberofdata);
			        getImage(eimage);
			        shezhi_button.setVisibility(View.GONE);
			        
	        	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			  getList();
			  shezhi_button.setFocusable(true);
		        shezhi_button.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						shezhi_button.setOnClickListener(new sCarButtonListener());
					}
				}); 
			}
			
	     /**
	      * 初始化数据
	      */
	    private void initData() {
	    	shezhi_button=(Button)view.findViewById(R.id.shezhi_button);
	    	 weatherService = new WeatherService(mContext);
	         cityname = (TextView) view.findViewById(R.id.cityname);
	        curntime = (TextView) view.findViewById(R.id.curntime);
	         fengli = (TextView) view.findViewById(R.id.fengli);
	         image = (ImageView) view.findViewById(R.id.iamge);
	         qiwen = (TextView) view.findViewById(R.id.qiwen);
	         shushidu = (TextView) view.findViewById(R.id.shushidu);
	         tianqi = (TextView) view.findViewById(R.id.tianqi);
	         week = (TextView) view.findViewById(R.id.week); 
	         releasetime=(TextView)view.findViewById(R.id.releasetime);
	        
		}

		/**
	     * 通过不同的描述选用相应的图片
	     * @param eimage
	     */       
	    private void getImage(String eimage) {
		if("多云".equals(eimage)){
			image.setImageResource(R.drawable.weather_mostlycloudy);
		}else if("晴".equals(eimage)){
			image.setImageResource(R.drawable.weather_fog);
		}else if("阴".equals(eimage)){
			image.setImageResource(R.drawable.weather_dust);
	//	}else if("小雨".equals(eimage)||"大雨".equals(eimage)||"中雨".equals(eimage)||"暴雨".equals(eimage)||"阵雨".equals(eimage)){
		}else if(eimage.contains("雨")){
			image.setImageResource(R.drawable.weather_rain);
		}else if("阵雪".equals(eimage)||"小雪".equals(eimage)||"大雪".equals(eimage)||"中雪".equals(eimage)||"暴雪".equals(eimage)){
			image.setImageResource(R.drawable.weather_snow);
		}
	}
	    /**
	     * 把得到的数据分别放在相应的list里
	     */
	    public void getList(){
	    	DateUtil dateutil =new DateUtil();
	    	templist = new ArrayList<String>();
	    	weatherlist = new ArrayList<String>();
	    	imagelist= new ArrayList<String>();
	    	datelist = new ArrayList<String>();
	    	weeklist = new ArrayList<String>();
	    	String temp2 = weatherInfo.getTemp2();
	    	String temp3 = weatherInfo.getTemp3();
	        String temp4 = weatherInfo.getTemp4();
	        String temp5 = weatherInfo.getTemp5();
	        templist.add(temp2);
	        templist.add(temp3);
	        templist.add(temp4);
	        templist.add(temp5);
	         String weather2 = weatherInfo.getWeather2();
	         String weather3 = weatherInfo.getWeather3();
	         String weather4 = weatherInfo.getWeather4();
	         String weather5 = weatherInfo.getWeather5();
	         weatherlist.add(weather2);
	         weatherlist.add(weather3);
	         weatherlist.add(weather4);
	         weatherlist.add(weather5);
	         String img_title3 = weatherInfo.getImg_title3();
	         String img_title5 = weatherInfo.getImg_title5();
	         String img_title7 = weatherInfo.getImg_title7();
	         String img_title9 = weatherInfo.getImg_title9();
	         imagelist.add(img_title3);
	         imagelist.add(img_title5);
	         imagelist.add(img_title7);
	         imagelist.add(img_title9);
	         Calendar  c =Calendar.getInstance();
	         int y = c.get(Calendar.YEAR);
			  int m =c.get(Calendar.MONTH);
			  int d = c.get(Calendar.DAY_OF_MONTH);
			  String[] day2week = dateutil.getDate(y, m, d+1);
			  String[] day3week =dateutil.getDate(y, m, d+2);
			  String[] day4week =dateutil.getDate(y, m, d+3);
			  String[] day5week =dateutil.getDate(y, m, d+4);
			  String day2 =day2week[0];
			  String day3 =day3week[0];
			  String day4 =day4week[0];
			  String day5 =day5week[0];
			  String week2 =day2week[1];
			  String week3 =day3week[1];
			  String week4 =day4week[1];
			  String week5 =day5week[1];
			  datelist.add(day2);
			  datelist.add(day3);
			  datelist.add(day4);
			  datelist.add(day5);
			  weeklist.add(week2);
			  weeklist.add(week3);
			  weeklist.add(week4);
			  weeklist.add(week5);
	    }
	    
	  
	    
	    /**
	     * 设置省市县三级联动，利用spinner
	     */
	    private void showDialog_SCar() { 
	    	service = new WeatherService(mContext);
	    	LayoutInflater inflater = LayoutInflater.from(mContext); 
	    	final Spinner spiner_city;
	    	final Spinner spiner_dist;
	    	Spinner spiner_pro;
	    	textEntryView= inflater.inflate(R.layout.city, null); 
	    	spiner_city=(Spinner)textEntryView.findViewById(R.id.spiner_city);
	    	spiner_dist=(Spinner)textEntryView.findViewById(R.id.spiner_dist);
	    	spiner_pro=(Spinner)textEntryView.findViewById(R.id.spiner_pro);
	    	 allProv = service.getAllProv();
	    	spiner_proAdapter =
	    		 new ArrayAdapter<String>(mContext,
	    				   android.R.layout.simple_spinner_item, allProv);
	    	spiner_pro.setAdapter(spiner_proAdapter);
	    	spiner_pro.setPrompt("请选择");

	    	//选择省监听事件
	    	spiner_pro.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
	    	public void onItemSelected(AdapterView<?> arg0, View arg1, int postion, long arg3) { 
	    	//根据省绑定市区
	    		String provname =allProv.get(postion);
	    		int proid = service.getproid(provname);
	    		distname= service.getDist(proid);
	    		spiner_distAdapter= new ArrayAdapter<String>(mContext,
	 				   android.R.layout.simple_spinner_item, distname);
	    		spiner_dist.setAdapter(spiner_distAdapter);
	    		spiner_dist.setPrompt("请选择");
	    	/* 将mySpinner 显示*/ 
	    	arg0.setVisibility(View.VISIBLE); 
	    	} 

	    	public void onNothingSelected(AdapterView<?> arg0) { 
	    	} 
	    	}); 

	    	//选择市区事件
	    	spiner_dist.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
	    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
	    		String sqldistname = distname.get(arg2);
	    		citynamelist= service.getCityname(sqldistname);
	    	//根据市区绑定县市
	    		spiner_cityAdapter=
	    			 new ArrayAdapter<String>(mContext,
	      				   android.R.layout.simple_spinner_item, citynamelist);

	    	spiner_city.setAdapter(spiner_cityAdapter);
	    	spiner_city.setPrompt("请选择");
	    	//chexiSpinner.setSelection(0, true);
	    	/* 将mySpinner 显示*/ 
	    	arg0.setVisibility(View.VISIBLE); 
	    	} 
	    	public void onNothingSelected(AdapterView<?> arg0) { 
	    	} 
	    	}); 
	    	
	    	/**
	    	 * 縣城的點擊事件
	    	 */
	    	spiner_city.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
		    	
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
		    		citynameforselct = citynamelist.get(arg2);
		    	arg0.setVisibility(View.VISIBLE); 
		    	} 
		    	public void onNothingSelected(AdapterView<?> arg0) { 
		    	} 
		    	}); 
	    	/**
	    	 *设置弹出框
	    	 */

	    	final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.dialog));  
	    	builder.setCancelable(false); 
	    	builder.setIcon(R.drawable.icon); 
	    	builder.setTitle("选择地区"); 
	    	builder.setView(textEntryView); 
	    	builder.setPositiveButton("确认", 
	    	new DialogInterface.OnClickListener() { 
	    	public void onClick(DialogInterface dialog, int whichButton) { 
	    		SharedPreferences citysetting = mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
	    		Editor edit =citysetting.edit();
	    		edit.putString("private_city", citynameforselct);
	    		edit.commit();
	    		String string = citysetting.getString("private_city", "hello");
	    		run();
	    	} 
	    	}); 
	    	builder.setNegativeButton("取消", 
	    	new DialogInterface.OnClickListener() { 
	    	public void onClick(DialogInterface dialog, int whichButton) { 
	    	} 
	    	});
	    	AlertDialog dialog = builder.create();
	    	builder.show();
	    	 WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
	    	 params.width = 10;
	    	 params.height =10 ;
	    	 dialog.getWindow().setAttributes(params);
	    } 
	  //单击事件
	    private class sCarButtonListener implements android.view.View.OnClickListener {
	    	public void onClick(View v) {
	    	showDialog_SCar();
	    	}
  }
	     
	     /**
	      * 自定义dialog
	      * @author Administrator
	      *
	      */
//	    public class SelectDialog extends AlertDialog {
//	    	public SelectDialog(Context context, int theme) {
//	    	    super(context, theme);
//	    	}
//	    	public SelectDialog(Context context) {
//	    	    super(context);
//	    	}
//	    	@Override
//	    	protected void onCreate(Bundle savedInstanceState) {
//	    	    super.onCreate(savedInstanceState);
//	    	    setContentView(R.layout.city);
//	    	  
//	    	}
//	    }
	    	
			
		}
 	


