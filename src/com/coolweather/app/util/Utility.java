package com.coolweather.app.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {

	//解析和处理省级数据
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB) {
		String provinces = "01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,"
				+ "10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,"
				+ "20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,"
				+ "30|广西,31|海南,32|香港,33|澳门,34|台湾";
		String[] allProvinces = provinces.split(",");
		for (String p : allProvinces) {
			String[] array = p.split("\\|");
			Province province = new Province();
			province.setProvinceCode(array[0]);
			province.setProvinceName(array[1]);
			coolWeatherDB.saveProvince(province);
		}
		return true;
	}
	
	//解析和处理市级数据
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject dataJSON = new JSONObject(response);
				JSONArray jsonArray = dataJSON.getJSONArray("retData");
				for (int i=0; i<jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					City city = new City();
					city.setCityCode(jsonObject.getString("area_id"));
					city.setCityName(jsonObject.getString("district_cn"));
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//解析和处理县级数据
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject dataJSON = new JSONObject(response);
				JSONArray jsonArray = dataJSON.getJSONArray("retData");
				for (int i=0; i<jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					County county = new County();
					county.setCountyCode(jsonObject.getString("area_id"));
					county.setCountyName(jsonObject.getString("name_cn"));
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
