package util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

public class Test1 {
	public static void main(String args[]) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("book_id", 1);
		jsonObject.put("user_id", 2);
//		try{
//		String str = jsonObject.getString("str");
//		}catch(Exception e){
//			System.out.println("adsfadsf"+e.toString());
//			
//		}
		System.out.println(jsonObject.size());
	}

}
