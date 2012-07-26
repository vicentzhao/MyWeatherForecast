package com.rushfusion.weather.util;
import java.util.Calendar;
public class DateUtil {
	public String[] getCurrDay(){
		String week = null;
		 Calendar  c =Calendar.getInstance();
		 int cy =c.get(Calendar.YEAR);
		  int cm =c.get(Calendar.MONTH);
		  int cd = c.get(Calendar.DAY_OF_MONTH);
		  int cw =c.get(Calendar.DAY_OF_WEEK);
		  switch (cw) {
			case 2:
				week ="星期一";
				break;
			case 3:
				week ="星期二";
				break;
			case 4:
				week ="星期三";
				break;
			case 5:
				week ="星期四";
				break;
			case 6:
				week ="星期五";
				break;
			case 7:
				week="星期六";
				break;
			case 1:
				week="星期天";
				break;		
		}
		  String eday =cy+"年"+(cm+1)+"月"+cd+"日";
		  String eweek =week;
		  String[] date = new String[]{eday,eweek};
		  return date;
	}
     
	  public String[] getDate(int y ,int m ,int d ){
		  String week = null;
		  Calendar  c =Calendar.getInstance();
		  c.set(y, m, d);
		  int cy = c.get(Calendar.YEAR);
		  int cm =c.get(Calendar.MONTH);
		  int cd = c.get(Calendar.DAY_OF_MONTH);
		  int cw =c.get(Calendar.DAY_OF_WEEK);
		  switch (cw) {

			case 2:
				week ="星期一";
				break;
			case 3:
				week ="星期二";
				break;
			case 4:
				week ="星期三";
				break;
			case 5:
				week ="星期四";
				break;
			case 6:
				week ="星期五";
				break;
			case 7:
				week="星期六";
				break;
			case 1:
				week="星期天";
				break;
			
		}
		  String eyear=null;
		   eyear = cy+"年"+(cm+1)+"月"+cd+"日";
		   String eweek = week;
		   String[] s = new String[]{eyear,eweek};
		   return s;
	  }
}
