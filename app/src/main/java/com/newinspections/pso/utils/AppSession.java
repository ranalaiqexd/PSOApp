package com.newinspections.pso.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newinspections.pso.dto.InspectionDTO;
import com.newinspections.pso.dto.MyInspectionDTO;
import com.newinspections.pso.model.InspectionsModel;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Pintu Kumar Patil 9977368049
 * @author 11-May-2016
 * 
 */
public class AppSession
{
	public static SharedPreferences sharedPref;
	private Editor editor;
	private static String SHARED = "inspections_preferences";

	public AppSession(Context context) {
		try {
			sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
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

	public void setIemiNo(String value){

		editor.putString("IemiNo",value);
		editor.commit();
	}

	public String getIemiNo() {
		return sharedPref.getString("IemiNo","");
	}

	public void setDevice(String value){

		editor.putString("Device",value);
		editor.commit();
	}

	public String getDevice(){
		return sharedPref.getString("Device","Not found");
	}

//	public void setTankTableId(int tankTableId)
//	{
//		editor.putInt("TankTableId", tankTableId);
//		editor.commit();
//	}
//
//	public int getTankTableId()
//	{
//		return sharedPref.getInt("TankTableId", 0);
//	}
//
//	public void setNozzleTableId(int nozzleTableId)
//	{
//		editor.putInt("NozzleTableId", nozzleTableId);
//		editor.commit();
//	}
//
//	public int getNozzleTableId()
//	{
//		return sharedPref.getInt("NozzleTableId", 0);
//	}

	public void setInspectionModel(InspectionsModel inspectionModel) {
		editor.putString("InspectionModel", new Gson().toJson(inspectionModel));
		editor.commit();
	}

	public InspectionsModel getInspectionModel()
	{
		try {
			String userJSONString = sharedPref.getString("InspectionModel", null);
			if (userJSONString == null)
				return null;
			return new Gson().fromJson(userJSONString, new TypeToken<InspectionsModel>() {}.getType());
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}

	public void setInspectionModelDraft(InspectionsModel inspectionModel) {
		editor.putString("InspectionModelDraft", new Gson().toJson(inspectionModel));
		editor.commit();
	}

	public InspectionsModel getInspectionModelDraft()
	{
		try {
			String userJSONString = sharedPref.getString("InspectionModelDraft", null);
			if (userJSONString == null)
				return null;
			return new Gson().fromJson(userJSONString, new TypeToken<InspectionsModel>() {}.getType());
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}
}