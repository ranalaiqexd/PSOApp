package com.newinspections.pso.utils;

/*Constant interface for entire project.All the constant are declare here.*/

public interface ConstantLib {
	String LOG = "GAMEPLAN";


//	String BASE_URL = "http://inspection.schooleasy.pk/inspection/Webservices/";//server url
//	String BASE_URL = "http://125.209.115.228:8001/inspection/Webservices/";//local url
//	String BASE_URL = "http://125.209.115.234:8001/inspection/Webservices/";///local url new
//	String BASE_URL = "http://125.209.115.246:8001/inspection/Webservices/";

	// Innovation pg
//	String BASE_URL = "http://203.215.173.200/pso_files/";

	// Innovation oracle
//	String BASE_URL = "http://203.215.173.203/pso_files/";

	// Client pg
	String BASE_URL = "http://125.209.115.246:8001/pso_files/";

	public static final String USER_LOGIN="userlogin.php?";
	public static final String USER_LOCATION="userlocation.php?";
	public static final String USER_BLOCK="userblock.php?";
	public static final String GET_USER_INSPECTIONS="getuserinspections.php?";
    public static final String GET_PRODUCTS="getproducts.php";
	public static final String SUBMIT_USER_INSPECTIONS="setuserinspections.php";

	/*
	 * Define all method here
	 */

	public static final String URL_LOGIN="doLogin?";
	public static final String URL_LOGOUT="logout?";
	public static final String URL_NEWINSPCTN="inspection_data?";
	public static final String URL_SRCH_INSPCTN_SRCHBY_LOC="inspection_by_location?";
	public static final String URL_SRCH_BY_STATN="inspection_by_station?";
    public static final String URL_SRCH_BY_DTE="inspection_by_date?";
	public static final String URL_NW_INSPCTN="getInspection?";
	public static final String URL_MAC_ADDRSS="scan_mac?";

	  public static final String method="method";
	  public static final String description = "description";
	  public static final String id = "id";
	  public static final String layout = "layout";
	  public static final String logo = "logo";
	  public static final String name = "name";
	  public static final String quantity = "quantity";
	  public static final String required = "required";//required
	  public static final String result = "result";
	  public static final String title = "title";
	  public static final String type = "type";
	  public static final String value = "value";
	  public static final String user_id ="user_id";
	  public static final String code ="code";
	  public static final String product="product";
	  public static final String status = "status";
	  public static final String background ="background";

//	edited by exd
	public static final String purchase_is_used ="1";
	public static final String purchase_not_used ="0";
}