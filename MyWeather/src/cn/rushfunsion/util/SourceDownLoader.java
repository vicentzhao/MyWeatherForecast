package cn.rushfunsion.util;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
public class SourceDownLoader {
    private static JsonUtil ju ;
	public static JSONArray getall(String path) throws Exception {
        URL url =new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        String  json = ju.InputStreamTOString(is);
        JSONArray js = new JSONArray(json);
		return js;  
	}
	public static String getallString(String path) throws Exception {
        URL url =new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        String  json = ju.InputStreamTOString(is);
		return json;  
	}
}
