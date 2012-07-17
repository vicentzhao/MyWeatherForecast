package cn.rushfunsion.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
	    final static int BUFFER_SIZE = 4096; 
	    public JSONArray getSource(String path)throws Exception{
	    	SourceDownLoader sc = new SourceDownLoader();
			JSONArray array = sc.getall(path);
	    	return array;
	    }
	    public String getStringSource(String path)throws Exception{
	    	SourceDownLoader sc = new SourceDownLoader();
			String array = sc.getallString(path);
	    	return array;
	    }
		public static String InputStreamTOString(InputStream in) throws Exception{  
			          ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
			          byte[] data = new byte[BUFFER_SIZE];  
			          int count = -1;  
			          while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
			            outStream.write(data, 0, count);  
                        data = null; 
                        String result=new String(outStream.toByteArray(),"utf-8");
                        return result;
		}
		
		}  
		    

