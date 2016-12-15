package com.inspections.pso.dao;

import android.util.Log;


import com.inspections.pso.utils.GeoAddress;
import com.inspections.pso.utils.RemoteRequestResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Pintu Kumar Patil 9977368049
 * @author 10-May-2015
 * 
 */
public class UtilDAO extends RemoteRequestResponse{

	final String ERROR="0",SUCCESS="1";
	String DEFAULT_LOCATION_URL = "http://www.ip-api.com/json";
	String GOOGLE_AUTOCOMPLETE_API = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
	//String KEY_FOR_AUTO_SEARCH = "AIzaSyAJWgv6m7oSA7Hd7nvx20JDM1HCfiFPOGQ";


	String KEY_FOR_AUTO_SEARCH = "AIzaSyDbkwWcHwii-_MMYDMQuKwh3ZUE7bTSNA8";

	//https://maps.googleapis.com/maps/api/geocode/json
	String GOOGLE_GEOCODE_API = "http://maps.googleapis.com/maps/api/geocode/json";
	String GOOGLE_DISTANCE_MATRIX_API = "https://maps.googleapis.com/maps/api/distancematrix/json";

	private static final String TAG = UtilDAO.class.getSimpleName();
	private String serviceUrl = null;
	public GeoAddress getAddress(double lat, double lon) {

		serviceUrl = "http://maps.google.com/maps/api/geocode/json?latlng="
				+ lat + "," + lon + "&output=json&sensor=false";
		Log.v(TAG, "serviceUrl-->" + serviceUrl);
		
		
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			String response = httpGet(serviceUrl, nameValuePairs, null);
			Log.i(TAG, "login= " + response);
			if (response != null) {
				return parseUrl(response);
			}else {
				return null;
			}
		} catch (Error e) {
			e.printStackTrace();
		}
		return null;

	}

	private GeoAddress parseUrl(String json) {
		GeoAddress address=new GeoAddress();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String status = jsonObject.getString("status");
			if (status.equalsIgnoreCase("OK")) {
				JSONArray jsonArray = jsonObject.getJSONArray("results");
				JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
				address.setStatus(SUCCESS);
				address.setFullAddress(jsonObject1.getString("formatted_address"));
				return address;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			address.setStatus(ERROR);
			address.setFullAddress(e.getMessage());
			return address;
		}
		address.setStatus(ERROR);
		address.setFullAddress("");
		return address;

	}

	public GeoAddress getDefaultLocation() {

		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			String response = httpGet(DEFAULT_LOCATION_URL, nameValuePairs, null);
			Log.i(TAG, "login= " + response);
			if (response != null) {
				return parseDefaultLocation(response);
			}else {
				return null;
			}
		} catch (Error e) {
			e.printStackTrace();
		}

		return null;
	}

	private GeoAddress parseDefaultLocation(String json) {

		GeoAddress address=new GeoAddress();

		try {
			Object object = new JSONTokener(json).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.has("city")) {
					address.setCity(jsonObject.getString("city"));
				}
				if (jsonObject.has("country")) {
					address.setCountry(jsonObject.getString("country"));
				}
				if (jsonObject.has("lat")) {
					address.setLatitude(Double.parseDouble(jsonObject.getString("lat")));
				}
				if (jsonObject.has("lon")) {
					address.setLongitude(Double.parseDouble(jsonObject.getString("lon")));
				}
				if (jsonObject.has("region")) {
					address.setRegion(jsonObject.getString("region"));
				}
				if (jsonObject.has("regionName")) {
					address.setRegionName(jsonObject.getString("regionName"));
				}
				if (jsonObject.has("timezone")) {
					address.setTimezone(jsonObject.getString("timezone"));
				}
				if (jsonObject.has("zip")) {
					address.setPostalCode(jsonObject.getString("zip"));
				}
				address.setStatus(SUCCESS);
				return address;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public GeoAddress getFullAddress(String address) {

		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("address", address));
			nameValuePairs.add(new BasicNameValuePair("key", KEY_FOR_AUTO_SEARCH));
			String response = httpGet(GOOGLE_GEOCODE_API, nameValuePairs, null);
			Log.i(TAG, "login= " + response);
			if (response != null) {
				return parseGeoAddress(response);
			}else {
				return null;
			}
		} catch (Error e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public GeoAddress getFullAddress(String latitude,String longitude) {
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("latlng", latitude+","+longitude));
			nameValuePairs.add(new BasicNameValuePair("key",KEY_FOR_AUTO_SEARCH));
			String response = httpGet(GOOGLE_GEOCODE_API, nameValuePairs, null);
			Log.i(TAG, "login= " + response);
			if (response != null) {
				return parseGeoAddress(response);
			}else {
				return null;
			}
		} catch (Error e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private GeoAddress parseGeoAddress(String json) {

		GeoAddress address=new GeoAddress();
		address.setStatus(ERROR);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String status = jsonObject.getString("status");
			if (status.equalsIgnoreCase("OK")) {
				address.setStatus("1");
				JSONArray jsonArray = jsonObject.getJSONArray("results");
				if (jsonArray!=null && jsonArray.length()>0) {
					JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
					if (jsonObject1.has("formatted_address")) {
						address.setFullAddress(jsonObject1.getString("formatted_address"));
					}
					if (jsonObject1.has("address_components")) {
						JSONArray jsonArray2 = jsonObject1.getJSONArray("address_components");
						if (jsonArray2!=null && jsonArray2.length()>0) {
							for (int i = 0; i < jsonArray2.length(); i++) {
								JSONObject jsonObject2 = (JSONObject) jsonArray2.get(i);
								if (jsonObject2.has("types")) {
									if (jsonObject2.get("types").toString().contains("premise")||jsonObject2.get("types").toString().contains("street_number")) {
										address.setStreetNumber(jsonObject2.getString("long_name"));
									}
									if (jsonObject2.get("types").toString().contains("route")) {
										address.setStreetAddress(jsonObject2.getString("long_name"));
									}
									if (jsonObject2.get("types").toString().contains("locality")) {
										address.setCity(jsonObject2.getString("long_name"));
									}
									if (jsonObject2.get("types").toString().contains("country")) {
										address.setCountry(jsonObject2.getString("long_name"));
									}
									if (jsonObject2.get("types").toString().contains("postal_code")) {
										address.setPostalCode(jsonObject2.getString("long_name"));
									}
									if (jsonObject2.get("types").toString().contains("administrative_area_level_1")) {
										address.setState(jsonObject2.getString("long_name"));
									}
									if (jsonObject2.get("types").toString().contains("administrative_area_level_2")) {
										address.setDistrict(jsonObject2.getString("long_name"));
									}
								}
							}
						}
					}
					if (jsonObject1.has("geometry")) {
						JSONObject jsonObject2=jsonObject1.getJSONObject("geometry");
						if (jsonObject2.has("location")) {
							JSONObject jsonObject3=jsonObject2.getJSONObject("location");
							if (jsonObject3.has("lat")) {
								address.setLatitude(jsonObject3.getDouble("lat"));
							}
							if (jsonObject3.has("lng")) {
								address.setLongitude(jsonObject3.getDouble("lng"));
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		address.setStatus(SUCCESS);
		return address;
	}

	private ArrayList<HashMap<String,String>> parseAutocomplete(String json) {
		ArrayList<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
		try {
			Object object = new JSONTokener(json).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.has("predictions")) {
					JSONArray jPlaces=jsonObject.getJSONArray("predictions");
			        /** Taking each place, parses and adds to list object */
			        for(int i=0; i<jPlaces.length();i++){
			        	 HashMap<String, String> place = new HashMap<String, String>();
			        	 JSONObject jPlace=(JSONObject)jPlaces.get(i);
			            try {
			                 place.put("description", jPlace.getString("description"));
			                 place.put("_id",jPlace.getString("id"));
			                 place.put("reference",jPlace.getString("reference"));
			                 placesList.add(place);
			 
			            } catch (JSONException e) {
			                e.printStackTrace();
			            }
			        }
				}
				return placesList;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	private GeoAddress parseDistance(String json2) throws JSONException {
		GeoAddress geoAddress =new GeoAddress();
		geoAddress.setStatus(ERROR);


		try {
			JSONObject jsonObject=new JSONObject(json2);

			if (jsonObject.has("status")){
				geoAddress.setStatus(jsonObject.getString("status"));

			}
			if (jsonObject.has("destination_addresses")){
				JSONArray jsonArrayDestination=jsonObject.getJSONArray("destination_addresses");
				if (jsonArrayDestination.length()>0){
					geoAddress.setDestinationAddress(jsonArrayDestination.get(0).toString());
				}
			}
			if (jsonObject.has("origin_addresses")){
				JSONArray jsonArrayOrigin=jsonObject.getJSONArray("origin_addresses");
				if (jsonArrayOrigin.length()>0){
					geoAddress.setSourceAddress(jsonArrayOrigin.get(0).toString());
				}

			}
			if (jsonObject.has("rows")){
				JSONArray jsonArray=jsonObject.getJSONArray("rows");
				if (jsonArray.length()>0){
					JSONObject jsonObject2=jsonArray.getJSONObject(0);
					if (jsonObject2.has("elements")){
						JSONArray jsonElements=jsonObject2.getJSONArray("elements");
						if (jsonElements.length()>0) {
							JSONObject jsonObjectD = jsonElements.getJSONObject(0);
							if (jsonObjectD.has("status")) {
								geoAddress.setStatus(jsonObjectD.getString("status"));
							}
							if (jsonObjectD.has("distance")) {
								JSONObject jsonObjectValue = jsonObjectD.getJSONObject("distance");
								geoAddress.setDistanceValue(jsonObjectValue.getString("value"));
								geoAddress.setDistanceText(jsonObjectValue.getString("text"));
								geoAddress.setStatus(SUCCESS);
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();

		}

		return geoAddress;
	}
}
