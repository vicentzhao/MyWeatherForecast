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
				week ="����һ";
				break;
			case 3:
				week ="���ڶ�";
				break;
			case 4:
				week ="������";
				break;
			case 5:
				week ="������";
				break;
			case 6:
				week ="������";
				break;
			case 7:
				week="������";
				break;
			case 1:
				week="������";
				break;		
		}
		  String eday =cy+"��"+(cm+1)+"��"+cd+"��";
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
				week ="����һ";
				break;
			case 3:
				week ="���ڶ�";
				break;
			case 4:
				week ="������";
				break;
			case 5:
				week ="������";
				break;
			case 6:
				week ="������";
				break;
			case 7:
				week="������";
				break;
			case 1:
				week="������";
				break;
			
		}
		  String eyear=null;
		   eyear = cy+"��"+(cm+1)+"��"+cd+"��";
		   String eweek = week;
		   String[] s = new String[]{eyear,eweek};
		   return s;
	  }
}
