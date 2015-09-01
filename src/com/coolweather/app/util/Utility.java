package com.coolweather.app.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {

	//�����ʹ���ʡ������
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB) {
		String provinces = "01|����,02|�Ϻ�,03|���,04|����,05|������,06|����,07|����,08|���ɹ�,09|�ӱ�,"
				+ "10|ɽ��,11|����,12|ɽ��,13|�½�,14|����,15|�ຣ,16|����,17|����,18|����,19|����,"
				+ "20|����,21|�㽭,22|����,23|����,24|����,25|����,26|����,27|�Ĵ�,28|�㶫,29|����,"
				+ "30|����,31|����,32|���,33|����,34|̨��";
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
	
	//�����ʹ����м�����
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
	
	//�����ʹ����ؼ�����
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
