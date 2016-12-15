package com.inspections.pso.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.inspections.pso.dto.InspectionDTO;
import com.inspections.pso.dto.MyInspectionDTO;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Pintu Kumar Patil 9977368049
 * @author 11-May-2016
 * 
 */
public class AppSession {
	public static SharedPreferences sharedPref;
	private Editor editor;
	private static String SHARED = "inspections_preferences";
	public static String COUNT_NOZZEL="nozzel_final_count1";
	public static String TANK_1="tank_11";
	public static String TANK_2="tank_21";
	public static String TANK_3="tank_31";
	public static String TANK_4="tank_41";
	public static String TANK_5="tank_51";
	public static String TANK_6="tank_61";

	public static String OLD="old";
	public static String CURRENT="Current";




	public AppSession(Context context) {
		try {
			sharedPref = context.getSharedPreferences(SHARED,
					Context.MODE_PRIVATE);


			editor = sharedPref.edit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////START SESSION FOR LOGIN DETAILS//////////////////
	public void setStationIds(String value) {
		editor.putString("StationIds", value);
		editor.commit();
	}

	public String getStationIds() {
		return sharedPref.getString("StationIds", "");
	}

	public void setStation(String stationId,InspectionDTO.Station station) {
		editor.putString("InspectionDTO.Station"+stationId, new Gson().toJson(station));
		editor.commit();
	}

	public InspectionDTO.Station getStation(String stationId) {
		try {
			String userJSONString = sharedPref.getString("InspectionDTO.Station"+stationId, null);
			if (userJSONString == null)
				return null;
			return new Gson().fromJson(userJSONString, new TypeToken<InspectionDTO.Station>() {}.getType());
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}

	public void setInspectionDTO(InspectionDTO dto) {
		editor.putString("InspectionDTO", new Gson().toJson(dto));
		editor.commit();
	}

	public InspectionDTO getInspectionDTO() {
		try {
			String userJSONString = sharedPref.getString("InspectionDTO", null);
			if (userJSONString == null)
				return null;
			return new Gson().fromJson(userJSONString, new TypeToken<InspectionDTO>() {}.getType());
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}
	public void setMyInspectionDTO(MyInspectionDTO dto) {
		editor.putString("MyInspectionDTO", new Gson().toJson(dto));
		editor.commit();
	}

	public MyInspectionDTO getMyInspectionDTO() {
		try {
			String userJSONString = sharedPref.getString("MyInspectionDTO", null);
			if (userJSONString == null)
				return null;
			return new Gson().fromJson(userJSONString, new TypeToken<MyInspectionDTO>() {}.getType());
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}
	private static final String PREF_KEY_LAST_ACTIVE = "last_active";
	public  void saveTimeStamp() {
		editor.putLong(PREF_KEY_LAST_ACTIVE, Calendar.getInstance().getTimeInMillis()).commit();
		editor.commit();
	}

	public  long getElapsedTime() {
		try {
			return TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis()- sharedPref.getLong(PREF_KEY_LAST_ACTIVE, 0));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 1;
	}

	public void setLogin(Boolean value) {
		editor.putBoolean("Login", value);
		editor.commit();
	}

	public boolean isLogin() {
		return sharedPref.getBoolean("Login", false);
	}

	public void setImei(Boolean value) {
		editor.putBoolean("Imei", value);
		editor.commit();
	}

	public boolean isImei() {
		return sharedPref.getBoolean("Imei", false);
	}



	public void setUserId(String value) {
		editor.putString("UserId", value);
		editor.commit();
	}

	public String getUserId() {
		return sharedPref.getString("UserId", "");
	}

	public void setUserName(String value){

		editor.putString("UserName",value);
		editor.commit();
	}

	public String getUserName(){

		return sharedPref.getString("UserName","");
	}

	public void setPassword(String value){
		editor.putString("Password",value);
		editor.commit();

	}

	public String getPassword(){
		return sharedPref.getString("Password","");

	}
	public void setDeviceId(String deviceId){
		editor.putString("diD",deviceId);
		editor.commit();
	}
	public String getDeviceId(){
		return sharedPref.getString("diD","");
	}

	public void setNozzelCount(String cnt){

		editor.putString(COUNT_NOZZEL,cnt);
		editor.commit();
	}

	public String getCountNozzel(){

		if (sharedPref.contains(COUNT_NOZZEL)){
			return sharedPref.getString(COUNT_NOZZEL," ");
		}
		return "0";
	}

	public void setTankDu_count(String key,int cntVal){
		editor.putInt(key, cntVal);
		editor.commit();
	}
	public int getTankNzlCnt(String key){
		//return sharedPref.getInt(key, 0);


		if (sharedPref.contains(key)){
			try {

				Log.i("key ","key1 "+key+" value1:"+sharedPref.getInt(key, 0));
				return sharedPref.getInt(key, 0);
			}catch (ClassCastException e){
				e.printStackTrace();
				try {
					Log.i("key ","key2 "+key+" value2:"+sharedPref.getString(key, ""));
				}catch (ClassCastException e1){
					e1.printStackTrace();
					Log.i("llllllllllllll","llllllllll");
				}
			}
		}
		return 0;
	}

	public void saveEdtCrrnt(List<EditText> favorites,String key) {

		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<EditText> getEdtCrrnt(String key) {
		//SharedPreferences settings;
		List<EditText> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			EditText[] favoriteItems = gson.fromJson(jsonFavorites,
					EditText[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void saveDuMax(List<EditText> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<EditText> getDuMax(String key) {
		//SharedPreferences settings;
		List<EditText> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			EditText[] favoriteItems = gson.fromJson(jsonFavorites,
					EditText[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void saveEdt_DuqntyAccry(List<EditText> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<EditText> getEdt_DuqntyAccry(String key) {
		//SharedPreferences settings;
		List<EditText> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			EditText[] favoriteItems = gson.fromJson(jsonFavorites,
					EditText[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void saveOpeningBal(List<EditText> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<EditText> getOpeningBal(String key) {
		//SharedPreferences settings;
		List<EditText> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			EditText[] favoriteItems = gson.fromJson(jsonFavorites,
					EditText[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void saveEdtPrvss(List<TextView> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<TextView> getEdtPrvss(String key) {
		//SharedPreferences settings;
		List<TextView> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			TextView[] favoriteItems = gson.fromJson(jsonFavorites,
					TextView[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void saveDuSpnnr(List<Spinner> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<Spinner> getDuSpnnr(String key) {
		//SharedPreferences settings;
		List<Spinner> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			Spinner[] favoriteItems = gson.fromJson(jsonFavorites,
					Spinner[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}
	public void saveDefectdChkbx(List<CheckBox> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<CheckBox> getDefectdChkbx(String key) {
		//SharedPreferences settings;
		List<CheckBox> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			CheckBox[] favoriteItems = gson.fromJson(jsonFavorites,
					CheckBox[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void saveDuMtr_prdct(List<String> favorites,String key) {




		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(key, jsonFavorites);
		editor.commit();
	}
	public List<String> getDuMtr_prdct(String key) {
		//SharedPreferences settings;
		List<String> favorites;



		if (sharedPref.contains(key)) {
			String jsonFavorites = sharedPref.getString(key, " ");
			Gson gson = new Gson();
			String[] favoriteItems = gson.fromJson(jsonFavorites,
					String[].class);

			favorites = Arrays.asList(favoriteItems);
			//favorites = new ArrayList<Product_detailList_entity>(favorites);
		} else
			return null;

		return  favorites;
	}

	public void setChkBool(String key,boolean ck){

		editor.putBoolean(key,ck);
		editor.commit();
	}

	public Boolean getChkBool(String key){
		if (sharedPref.contains(key)){
			return sharedPref.getBoolean(key,false);
		}
		return false;
	}

	public void removeSharedPrfnc(String key){
		sharedPref.edit().remove(key).apply();
	}

	public void setProdct(String key,String val){

		editor.putString(key,val);
		editor.commit();
	}

	public String getProduct(String key){

		if (sharedPref.contains(key)){
			return sharedPref.getString(key, " ");
		}
		return "0";
	}

	public void setNozlSize(String key, int val){

		editor.putInt(key,val);
		editor.commit();
	}

	public int getNozlSize(String key){

		if (sharedPref.contains(key)){

			return sharedPref.getInt(key,0);
		}
		return 0;
	}

	public void setData(String key,String data){
		editor.putString(key,data);
		editor.commit();
	}

	public String getData(String key){
		return sharedPref.getString(key,null);
	}

}