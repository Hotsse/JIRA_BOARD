package com.hotsse.jira.common.json.vo;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class JsonVO {
	
	public Map<String, Object> body;
	
	public JsonVO() {
		body = new HashMap<String, Object>();
	}
	public JsonVO(Map<String, Object> body) {
		this.body = body;
	}
	
	public JSONObject toJSONObject() {
		
		JSONObject result = null;
		try {
			result = new JSONObject(body);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String toJSONString() {
		
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject(body);
			result = jsonObj.toJSONString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;		
	}
	
}
