package com.rushfusion.weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Node;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.rushfusion.weather.util.DateUtil;
import com.rushfusion.weather.util.WeatherService;
import com.rushfusion.weather.util.Weatherinfo;

public class MyWeatherActivity implements Runnable {

	private WeatherService weatherService;
	private ProgressDialog progressDialog;
	private Weatherinfo weatherInfo;
	private WeatherService service;

	private TextView cityname;
	private TextView curntime;
	private TextView fengli;
	private ImageView image;
	private TextView qiwen;
	private TextView shushidu;
	private TextView tianqi;
	private TextView week;
	private TextView releasetime;
	private Button shezhi_button;
	private Button query_button;
	private Button confirm;
	private Button cancle;
	private View view;
	private View textEntryView;
	private ViewGroup mContainer = null;
	private ListView listview;
	private Spinner spiner_city ;
	private Spinner spiner_dist ;
	private Spinner spiner_pro ;
	private View selectcity;
	private View tvforcastshow;
	private TextView spinner_textview;

	private static final String LOG_TAG = "MyWeather";
	private String DefaultCtiy = "�Ϻ�";
	private String private_city;
	private String citynameforselct;
	private String cityCode;
	private int viewNumber = 1;

	private ArrayAdapter<String> spiner_distAdapter;
	private ArrayAdapter<String> spiner_proAdapter;
	private ArrayAdapter<String> spiner_cityAdapter;
	private List<String> datelist;
	private List<String> imagelist;
	private List<String> templist;
	private List<String> weatherlist;
	private List<String> weeklist;
	private List<String> allProv;
	private List<String> distname;
	private List<String> citynamelist;

	private Context mContext;
	private Node mParams = null;

	public void init(Context context, ViewGroup container, Node params) {
		mContext = context;
		mContainer = container;
		mParams = params;
	}

	public void run() {
		showData();

	}

	public void showData() {
		view = View.inflate(mContext, R.layout.main, null);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
		mContainer.removeAllViews();
		mContainer.addView(view, lp);
		initData();
		if (!weatherService.isDBExist()) {
			String title = mContext.getResources().getString(
					R.string.dialogtitle);
			String content = mContext.getResources().getString(
					R.string.dialogcontent);
			progressDialog = ProgressDialog
					.show(mContext, title, content, true);
			try {
				weatherService.copyDB();
				progressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			 new  AsyncTask<Void, Void, Void>(){
				@Override
				protected Void doInBackground(Void... params) {
					if (viewNumber == 1 || viewNumber == 6) {
						shezhi_button.post(new  Runnable() {
							public void run() {
							 	shezhi_button.requestFocus();
							}
						});
						SharedPreferences citysetting = mContext.getSharedPreferences(
								"setting", Context.MODE_PRIVATE);
						final String ecityname = citysetting.getString("private_city",
								DefaultCtiy);
						cityname.post(new  Runnable() {
							public void run() {
								
								cityname.setText(ecityname);
							}
						});
						cityCode = weatherService.getCityCode(ecityname);
						try {
							weatherInfo = weatherService.getWeatherInfo(cityCode);
							System.out.println("WEATHERINFO"+weatherInfo.getCity());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					getList();
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					if (viewNumber == 1 || viewNumber == 6) {
						DateUtil dateUtil = new DateUtil();
						String[] currDay = dateUtil.getCurrDay();
						curntime.setText(currDay[0]);
						week.setText(currDay[1]);
						int ereleasetime = weatherInfo.getFchh();
						String sreleasetime = ereleasetime + " ʱ����";
						releasetime.setText(sreleasetime);
						String efengli = weatherInfo.getWind1();
						fengli.setText(efengli);
						String eqiwen = weatherInfo.getTemp1();
						qiwen.setText(eqiwen);
						String eshushidu = weatherInfo.getIndex_co();
						shushidu.setText(eshushidu);
						String etianqi = weatherInfo.getWeather1();
						tianqi.setText(etianqi);
						String eimage = weatherInfo.getImg_title1();
						getImage(eimage);
					} else {
						shezhi_button.setFocusable(true);
						SharedPreferences citysetting = mContext.getSharedPreferences(
								"setting", Context.MODE_PRIVATE);
						String ecityname = citysetting.getString("private_city",
								DefaultCtiy);
						cityname.setText(ecityname);
						int numberofdata = viewNumber - 2;
						curntime.setText(datelist.get(numberofdata));
						week.setText(weeklist.get(numberofdata));
						qiwen.setText(weatherlist.get(numberofdata));
						tianqi.setText(templist.get(numberofdata));
						String eimage = imagelist.get(numberofdata);
						getImage(eimage);
						shezhi_button.setVisibility(View.GONE);

					}
					super.onPostExecute(result);
				}
				
				
				 
			 }.execute();
			
		

		
		shezhi_button.setFocusable(true);
		shezhi_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shezhi_button.setOnClickListener(new sCarButtonListener());
			}
		});
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		shezhi_button = (Button) view.findViewById(R.id.shezhi_button);
		weatherService = new WeatherService(mContext);
		cityname = (TextView) view.findViewById(R.id.cityname);
		curntime = (TextView) view.findViewById(R.id.curntime);
		fengli = (TextView) view.findViewById(R.id.fengli);
		image = (ImageView) view.findViewById(R.id.iamge);
		qiwen = (TextView) view.findViewById(R.id.qiwen);
		shushidu = (TextView) view.findViewById(R.id.shushidu);
		tianqi = (TextView) view.findViewById(R.id.tianqi);
		week = (TextView) view.findViewById(R.id.week);
		releasetime = (TextView) view.findViewById(R.id.releasetime);
		spiner_city = (Spinner) view.findViewById(R.id.spiner_city);
		spiner_dist = (Spinner) view.findViewById(R.id.spiner_dist);
		spiner_pro = (Spinner) view.findViewById(R.id.spiner_pro);
		selectcity =view.findViewById(R.id.selectcity);
		tvforcastshow =view.findViewById(R.id.tvforcastshow);
		confirm =(Button) view.findViewById(R.id.confirm);
		cancle  =(Button) view.findViewById(R.id.cancle);
		spinner_textview =(TextView) view.findViewById(R.id.spinner_textview);
	}

	/**
	 * ͨ����ͬ������ѡ����Ӧ��ͼƬ
	 * 
	 * @param eimage
	 */
	private void getImage(String eimage) {
		if ("����".equals(eimage)) {
			image.setImageResource(R.drawable.weather_mostlycloudy);
		} else if ("��".equals(eimage)) {
			image.setImageResource(R.drawable.weather_fog);
		} else if ("��".equals(eimage)) {
			image.setImageResource(R.drawable.weather_dust);
			// }else
			// if("С��".equals(eimage)||"����".equals(eimage)||"����".equals(eimage)||"����".equals(eimage)||"����".equals(eimage)){
		} else if (eimage.contains("��")) {
			image.setImageResource(R.drawable.weather_rain);
		} else if ("��ѩ".equals(eimage) || "Сѩ".equals(eimage)
				|| "��ѩ".equals(eimage) || "��ѩ".equals(eimage)
				|| "��ѩ".equals(eimage)) {
			image.setImageResource(R.drawable.weather_snow);
		}
	}

	/**
	 * �ѵõ������ݷֱ������Ӧ��list��
	 */
	public void getList() {
		DateUtil dateutil = new DateUtil();
		templist = new ArrayList<String>();
		weatherlist = new ArrayList<String>();
		imagelist = new ArrayList<String>();
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
		Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);
		String[] day2week = dateutil.getDate(y, m, d + 1);
		String[] day3week = dateutil.getDate(y, m, d + 2);
		String[] day4week = dateutil.getDate(y, m, d + 3);
		String[] day5week = dateutil.getDate(y, m, d + 4);
		String day2 = day2week[0];
		String day3 = day3week[0];
		String day4 = day4week[0];
		String day5 = day5week[0];
		String week2 = day2week[1];
		String week3 = day3week[1];
		String week4 = day4week[1];
		String week5 = day5week[1];
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
	 * ����ʡ������������������spinner
	 */
	private void showDialog_SCar() {
		selectcity.setVisibility(View.VISIBLE);
		tvforcastshow.setVisibility(View.GONE);
		service = new WeatherService(mContext);
		allProv = service.getAllProv();
		
		spiner_proAdapter = new ArrayAdapter<String>(mContext,
				R.drawable.drop_list_hover, allProv);
		spiner_proAdapter.setDropDownViewResource(R.drawable.drop_list_ys);
//		spiner_proAdapter = new MyAdapter(mContext,allProv);
		spiner_pro.setAdapter(spiner_proAdapter);
		spiner_pro.setPrompt("��ѡ����Ӧ��ʡ��");
		// ѡ��ʡ�����¼�
		spiner_pro
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int postion, long arg3) {
						// ����ʡ������
						System.out.println("���Ѿ���ѡ���ˣ���������");
						String provname = allProv.get(postion);
						int proid = service.getproid(provname);
						distname = service.getDist(proid);
//						spiner_distAdapter = new MyAdapter(mContext,distname);
//						spiner_dist.setAdapter(spiner_distAdapter);
						spiner_distAdapter = new ArrayAdapter<String>(mContext,
								R.drawable.drop_list_hover, distname);
						spiner_distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spiner_dist.setPrompt("��ѡ��");
						spiner_dist.setAdapter(spiner_distAdapter);
						/* ��mySpinner ��ʾ */
						arg0.setVisibility(View.VISIBLE);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// ѡ�������¼�
		spiner_dist
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String sqldistname = distname.get(arg2);
						citynamelist = service.getCityname(sqldistname);
						// ��������������
//						spiner_cityAdapter = new MyAdapter(mContext,citynamelist);
//
//						spiner_city.setAdapter(spiner_cityAdapter);
						spiner_cityAdapter = new ArrayAdapter<String>(mContext,
								R.drawable.drop_list_hover, citynamelist);
						spiner_cityAdapter.setDropDownViewResource(R.drawable.drop_list_ys);
						spiner_city.setPrompt("��ѡ��");
						spiner_city.setAdapter(spiner_cityAdapter);
						// chexiSpinner.setSelection(0, true);
						/* ��mySpinner ��ʾ */
						arg0.setVisibility(View.VISIBLE);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		/**
		 * �h�ǵ��c���¼�
		 */
		spiner_city
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						citynameforselct = citynamelist.get(arg2);
						arg0.setVisibility(View.VISIBLE);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		/**
		 * ���õ�����
		 */

//		final AlertDialog.Builder builder = new AlertDialog.Builder(
//				new ContextThemeWrapper(mContext, R.style.dialog));
//		builder.setCancelable(false);
//		builder.setIcon(R.drawable.icon);
//		builder.setTitle("ѡ�����");
//		builder.setView(textEntryView);
//		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				SharedPreferences citysetting = mContext.getSharedPreferences(
//						"setting", Context.MODE_PRIVATE);
//				Editor edit = citysetting.edit();
//				edit.putString("private_city", citynameforselct);
//				edit.commit();
//				String string = citysetting.getString("private_city", "hello");
//				run();
//			}
//		});
//		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//			}
//		});
//		AlertDialog dialog = builder.create();
//		builder.show();
//		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//		params.width = 10;
//		params.height = 10;
//		dialog.getWindow().setAttributes(params);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectcity.setVisibility(View.GONE);
				tvforcastshow.setVisibility(View.VISIBLE);
				SharedPreferences citysetting = mContext.getSharedPreferences(
						"setting", Context.MODE_PRIVATE);
				Editor edit = citysetting.edit();
				edit.putString("private_city", citynameforselct);
				edit.commit();
				String string = citysetting.getString("private_city", "hello");
				run();
				
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectcity.setVisibility(View.GONE);
				tvforcastshow.setVisibility(View.VISIBLE);
				
			}
		});
	}
	// �����¼�
	private class sCarButtonListener implements
			android.view.View.OnClickListener {
		public void onClick(View v) {
			showDialog_SCar();
		}
	}

}
