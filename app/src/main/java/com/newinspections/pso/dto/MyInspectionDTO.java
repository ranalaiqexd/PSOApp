package com.newinspections.pso.dto;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobiweb on 17/10/16.
 */

    public class MyInspectionDTO {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("response")
        @Expose
        private List<Response> response = new ArrayList<>();

        /**
         *
         * @return
         * The message
         */
        public String getMessage() {
            return message;
        }

        /**
         *
         * @param message
         * The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The response
         */
        public List<Response> getResponse() {
            return response;
        }

        /**
         *
         * @param response
         * The response
         */
        public void setResponse(List<Response> response) {
            this.response = response;
        }


    public class Response {

        @SerializedName("stationsDetalis")
        @Expose
        private StationsDetalis stationsDetalis;
        @SerializedName("tanks")
        @Expose
        private List<Tank> tanks = new ArrayList<Tank>();
        @SerializedName("common_object")
        @Expose
        private CommonObject commonObject;
        @SerializedName("hse_survey")
        @Expose
        private List<HseSurvey> hseSurvey = new ArrayList<HseSurvey>();
        @SerializedName("forcast_survey")
        @Expose
        private List<ForcastSurvey> forcastSurvey = new ArrayList<ForcastSurvey>();
        @SerializedName("calculation")
        @Expose
        private List<Calculation> calculation = new ArrayList<Calculation>();

        /**
         *
         * @return
         * The stationsDetalis
         */
        public StationsDetalis getStationsDetalis() {
            return stationsDetalis;
        }

        /**
         *
         * @param stationsDetalis
         * The stationsDetalis
         */
        public void setStationsDetalis(StationsDetalis stationsDetalis) {
            this.stationsDetalis = stationsDetalis;
        }

        /**
         *
         * @return
         * The tanks
         */
        public List<Tank> getTanks() {
            return tanks;
        }

        /**
         *
         * @param tanks
         * The tanks
         */
        public void setTanks(List<Tank> tanks) {
            this.tanks = tanks;
        }

        /**
         *
         * @return
         * The commonObject
         */
        public CommonObject getCommonObject() {
            return commonObject;
        }

        /**
         *
         * @param commonObject
         * The common_object
         */
        public void setCommonObject(CommonObject commonObject) {
            this.commonObject = commonObject;
        }

        /**
         *
         * @return
         * The hseSurvey
         */
        public List<HseSurvey> getHseSurvey() {
            return hseSurvey;
        }

        /**
         *
         * @param hseSurvey
         * The hse_survey
         */
        public void setHseSurvey(List<HseSurvey> hseSurvey) {
            this.hseSurvey = hseSurvey;
        }

        /**
         *
         * @return
         * The forcastSurvey
         */
        public List<ForcastSurvey> getForcastSurvey() {
            return forcastSurvey;
        }

        /**
         *
         * @param forcastSurvey
         * The forcast_survey
         */
        public void setForcastSurvey(List<ForcastSurvey> forcastSurvey) {
            this.forcastSurvey = forcastSurvey;
        }

        /**
         *
         * @return
         * The calculation
         */
        public List<Calculation> getCalculation() {
            return calculation;
        }

        /**
         *
         * @param calculation
         * The calculation
         */
        public void setCalculation(List<Calculation> calculation) {
            this.calculation = calculation;
        }

        public class StationsDetalis {

            @SerializedName("station_id")
            @Expose
            private String stationId;
            @SerializedName("station_name")
            @Expose
            private String stationName;
            @SerializedName("sap_code")
            @Expose
            private String sapCode;
            @SerializedName("station_latitude")
            @Expose
            private String stationLatitude;
            @SerializedName("station_longitude")
            @Expose
            private String stationLongitude;
            @SerializedName("inspection_timestamp")
            @Expose
            private String inspectionTimestamp;
//            edited by exd
            @SerializedName("previous_timestamp")
            @Expose
            private String previousTimestamp;
            @SerializedName("division_name")
            @Expose
            private String divisionName;
            @SerializedName("sales_area")
            @Expose
            private String salesArea;
            /**
             *
             * @return
             * The stationId
             */
            public String getStationId() {
                return stationId;
            }

            /**
             *
             * @param stationId
             * The station_id
             */
            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            /**
             *
             * @return
             * The stationName
             */
            public String getStationName() {
                return stationName;
            }

            /**
             *
             * @param stationName
             * The station_name
             */
            public void setStationName(String stationName) {
                this.stationName = stationName;
            }

            /**
             *
             * @return
             * The sapCode
             */
            public String getSapCode() {
                return sapCode;
            }

            /**
             *
             * @param sapCode
             * The sap_code
             */
            public void setSapCode(String sapCode) {
                this.sapCode = sapCode;
            }

            /**
             *
             * @return
             * The stationLatitude
             */
            public String getStationLatitude() {
                return stationLatitude;
            }

            /**
             *
             * @param stationLatitude
             * The station_latitude
             */
            public void setStationLatitude(String stationLatitude) {
                this.stationLatitude = stationLatitude;
            }

            /**
             *
             * @return
             * The stationLongitude
             */
            public String getStationLongitude() {
                return stationLongitude;
            }

            /**
             *
             * @param stationLongitude
             * The station_longitude
             */
            public void setStationLongitude(String stationLongitude) {
                this.stationLongitude = stationLongitude;
            }

            /**
             *
             * @return
             * The inspectionTimestamp
             */
            public String getInspectionTimestamp() {
                if(inspectionTimestamp==null)
                    return "empty";
                else
                    return inspectionTimestamp;
            }

            /**
             *
             * @param inspectionTimestamp
             * The inspection_timestamp
             */
            public void setInspectionTimestamp(String inspectionTimestamp) {
                this.inspectionTimestamp = inspectionTimestamp;
            }

//            edited by exd
            public String getPreviousTimestamp()
            {
                if(previousTimestamp==null)
                    return "empty";
                else
                    return previousTimestamp;
            }

            public void setPreviousTimestamp(String previousTimestamp)
            {
                this.previousTimestamp = previousTimestamp;
            }

            public String getDivisionName()
            {
                return divisionName;
            }

            public void setDivisionName(String divisionName)
            {
                this.divisionName = divisionName;
            }

            public String getSalesArea()
            {
                return salesArea;
            }

            public void setSalesArea(String salesArea)
            {
                this.salesArea = salesArea;
            }

        }

        public class Calculation {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("a_hsd")
            @Expose
            private String aHsd;
            @SerializedName("a_pmg")
            @Expose
            private String aPmg;
            @SerializedName("a_hobc")
            @Expose
            private String aHobc;
            @SerializedName("b_hsd")
            @Expose
            private String bHsd;
            @SerializedName("b_pmg")
            @Expose
            private String bPmg;
            @SerializedName("b_hobc")
            @Expose
            private String bHobc;
            @SerializedName("c_hsd")
            @Expose
            private String cHsd;
            @SerializedName("c_pmg")
            @Expose
            private String cPmg;
            @SerializedName("c_hobc")
            @Expose
            private String cHobc;
            @SerializedName("d_hsd")
            @Expose
            private String dHsd;
            @SerializedName("d_pmg")
            @Expose
            private String dPmg;
            @SerializedName("d_hobc")
            @Expose
            private String dHobc;
            @SerializedName("e_hsd")
            @Expose
            private String eHsd;
            @SerializedName("e_pmg")
            @Expose
            private String ePmg;
            @SerializedName("e_hobc")
            @Expose
            private String eHobc;
            @SerializedName("f_hsd")
            @Expose
            private String fHsd;
            @SerializedName("f_pmg")
            @Expose
            private String fPmg;
            @SerializedName("f_hobc")
            @Expose
            private String fHobc;
            @SerializedName("result_hsd")
            @Expose
            private String resultHsd;
            @SerializedName("result_pmg")
            @Expose
            private String resultPmg;
            @SerializedName("result_hobc")
            @Expose
            private String resultHobc;
            @SerializedName("dealer_code")
            @Expose
            private String dealerCode;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("station_id")
            @Expose
            private String stationId;
            @SerializedName("inspection_id")
            @Expose
            private String inspectionId;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("avg_hsd")
            @Expose
            private String avgHsd;
            @SerializedName("avg_pmg")
            @Expose
            private String avgPmg;
            @SerializedName("avg_hobc")
            @Expose
            private String avgHobc;

            /**
             *
             * @return
             * The id
             */
            public String getId() {
                return id;
            }

            /**
             *
             * @param id
             * The id
             */
            public void setId(String id) {
                this.id = id;
            }

            /**
             *
             * @return
             * The aHsd
             */
            public String getAHsd() {
                return aHsd;
            }

            /**
             *
             * @param aHsd
             * The a_hsd
             */
            public void setAHsd(String aHsd) {
                this.aHsd = aHsd;
            }

            /**
             *
             * @return
             * The aPmg
             */
            public String getAPmg() {
                return aPmg;
            }

            /**
             *
             * @param aPmg
             * The a_pmg
             */
            public void setAPmg(String aPmg) {
                this.aPmg = aPmg;
            }

            /**
             *
             * @return
             * The aHobc
             */
            public String getAHobc() {
                return aHobc;
            }

            /**
             *
             * @param aHobc
             * The a_hobc
             */
            public void setAHobc(String aHobc) {
                this.aHobc = aHobc;
            }

            /**
             *
             * @return
             * The bHsd
             */
            public String getBHsd() {
                return bHsd;
            }

            /**
             *
             * @param bHsd
             * The b_hsd
             */
            public void setBHsd(String bHsd) {
                this.bHsd = bHsd;
            }

            /**
             *
             * @return
             * The bPmg
             */
            public String getBPmg() {
                return bPmg;
            }

            /**
             *
             * @param bPmg
             * The b_pmg
             */
            public void setBPmg(String bPmg) {
                this.bPmg = bPmg;
            }

            /**
             *
             * @return
             * The bHobc
             */
            public String getBHobc() {
                return bHobc;
            }

            /**
             *
             * @param bHobc
             * The b_hobc
             */
            public void setBHobc(String bHobc) {
                this.bHobc = bHobc;
            }

            /**
             *
             * @return
             * The cHsd
             */
            public String getCHsd() {
                return cHsd;
            }

            /**
             *
             * @param cHsd
             * The c_hsd
             */
            public void setCHsd(String cHsd) {
                this.cHsd = cHsd;
            }

            /**
             *
             * @return
             * The cPmg
             */
            public String getCPmg() {
                return cPmg;
            }

            /**
             *
             * @param cPmg
             * The c_pmg
             */
            public void setCPmg(String cPmg) {
                this.cPmg = cPmg;
            }

            /**
             *
             * @return
             * The cHobc
             */
            public String getCHobc() {
                return cHobc;
            }

            /**
             *
             * @param cHobc
             * The c_hobc
             */
            public void setCHobc(String cHobc) {
                this.cHobc = cHobc;
            }

            /**
             *
             * @return
             * The dHsd
             */
            public String getDHsd() {
                return dHsd;
            }

            /**
             *
             * @param dHsd
             * The d_hsd
             */
            public void setDHsd(String dHsd) {
                this.dHsd = dHsd;
            }

            /**
             *
             * @return
             * The dPmg
             */
            public String getDPmg() {
                return dPmg;
            }

            /**
             *
             * @param dPmg
             * The d_pmg
             */
            public void setDPmg(String dPmg) {
                this.dPmg = dPmg;
            }

            /**
             *
             * @return
             * The dHobc
             */
            public String getDHobc() {
                return dHobc;
            }

            /**
             *
             * @param dHobc
             * The d_hobc
             */
            public void setDHobc(String dHobc) {
                this.dHobc = dHobc;
            }

            /**
             *
             * @return
             * The eHsd
             */
            public String getEHsd() {
                return eHsd;
            }

            /**
             *
             * @param eHsd
             * The e_hsd
             */
            public void setEHsd(String eHsd) {
                this.eHsd = eHsd;
            }

            /**
             *
             * @return
             * The ePmg
             */
            public String getEPmg() {
                return ePmg;
            }

            /**
             *
             * @param ePmg
             * The e_pmg
             */
            public void setEPmg(String ePmg) {
                this.ePmg = ePmg;
            }

            /**
             *
             * @return
             * The eHobc
             */
            public String getEHobc() {
                return eHobc;
            }

            /**
             *
             * @param eHobc
             * The e_hobc
             */
            public void setEHobc(String eHobc) {
                this.eHobc = eHobc;
            }

            /**
             *
             * @return
             * The fHsd
             */
            public String getFHsd() {
                return fHsd;
            }

            /**
             *
             * @param fHsd
             * The f_hsd
             */
            public void setFHsd(String fHsd) {
                this.fHsd = fHsd;
            }

            /**
             *
             * @return
             * The fPmg
             */
            public String getFPmg() {
                return fPmg;
            }

            /**
             *
             * @param fPmg
             * The f_pmg
             */
            public void setFPmg(String fPmg) {
                this.fPmg = fPmg;
            }

            /**
             *
             * @return
             * The fHobc
             */
            public String getFHobc() {
                return fHobc;
            }

            /**
             *
             * @param fHobc
             * The f_hobc
             */
            public void setFHobc(String fHobc) {
                this.fHobc = fHobc;
            }

            /**
             *
             * @return
             * The resultHsd
             */
            public String getResultHsd() {
                return resultHsd;
            }

            /**
             *
             * @param resultHsd
             * The result_hsd
             */
            public void setResultHsd(String resultHsd) {
                this.resultHsd = resultHsd;
            }

            /**
             *
             * @return
             * The resultPmg
             */
            public String getResultPmg() {
                return resultPmg;
            }

            /**
             *
             * @param resultPmg
             * The result_pmg
             */
            public void setResultPmg(String resultPmg) {
                this.resultPmg = resultPmg;
            }

            /**
             *
             * @return
             * The resultHobc
             */
            public String getResultHobc() {
                return resultHobc;
            }

            /**
             *
             * @param resultHobc
             * The result_hobc
             */
            public void setResultHobc(String resultHobc) {
                this.resultHobc = resultHobc;
            }

            /**
             *
             * @return
             * The dealerCode
             */
            public String getDealerCode() {
                return dealerCode;
            }

            /**
             *
             * @param dealerCode
             * The dealer_code
             */
            public void setDealerCode(String dealerCode) {
                this.dealerCode = dealerCode;
            }

            /**
             *
             * @return
             * The userId
             */
            public String getUserId() {
                return userId;
            }

            /**
             *
             * @param userId
             * The user_id
             */
            public void setUserId(String userId) {
                this.userId = userId;
            }

            /**
             *
             * @return
             * The stationId
             */
            public String getStationId() {
                return stationId;
            }

            /**
             *
             * @param stationId
             * The station_id
             */
            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            /**
             *
             * @return
             * The inspectionId
             */
            public String getInspectionId() {
                return inspectionId;
            }

            /**
             *
             * @param inspectionId
             * The inspection_id
             */
            public void setInspectionId(String inspectionId) {
                this.inspectionId = inspectionId;
            }

            /**
             *
             * @return
             * The date
             */
            public String getDate() {
                return date;
            }

            /**
             *
             * @param date
             * The date
             */
            public void setDate(String date) {
                this.date = date;
            }

            /**
             *
             * @return
             * The avgHsd
             */
            public String getAvgHsd() {
                return avgHsd;
            }

            /**
             *
             * @param avgHsd
             * The avg_hsd
             */
            public void setAvgHsd(String avgHsd) {
                this.avgHsd = avgHsd;
            }

            /**
             *
             * @return
             * The avgPmg
             */
            public String getAvgPmg() {
                return avgPmg;
            }

            /**
             *
             * @param avgPmg
             * The avg_pmg
             */
            public void setAvgPmg(String avgPmg) {
                this.avgPmg = avgPmg;
            }

            /**
             *
             * @return
             * The avgHobc
             */
            public String getAvgHobc() {
                return avgHobc;
            }

            /**
             *
             * @param avgHobc
             * The avg_hobc
             */
            public void setAvgHobc(String avgHobc) {
                this.avgHobc = avgHobc;
            }

        }
        public class CommonObject {

            @SerializedName("hse_survey")
            @Expose
            private String hseSurvey;
            @SerializedName("forcast_survey")
            @Expose
            private String forcastSurvey;
            @SerializedName("purpose")
            @Expose
            private String purpose;
            @SerializedName("comment")
            @Expose
            private String comment;
            @SerializedName("attachement")
            @Expose
            private String attachement;
            @SerializedName("path")
            @Expose
            private String path;

            /**
             *
             * @return
             * The hseSurvey
             */
            public String getHseSurvey() {
                return hseSurvey;
            }

            /**
             *
             * @param hseSurvey
             * The hse_survey
             */
            public void setHseSurvey(String hseSurvey) {
                this.hseSurvey = hseSurvey;
            }

            /**
             *
             * @return
             * The forcastSurvey
             */
            public String getForcastSurvey() {
                return forcastSurvey;
            }

            /**
             *
             * @param forcastSurvey
             * The forcast_survey
             */
            public void setForcastSurvey(String forcastSurvey) {
                this.forcastSurvey = forcastSurvey;
            }

            /**
             *
             * @return
             * The purpose
             */
            public String getPurpose() {
                return purpose;
            }

            /**
             *
             * @param purpose
             * The purpose
             */
            public void setPurpose(String purpose) {
                this.purpose = purpose;
            }

            /**
             *
             * @return
             * The comment
             */
            public String getComment() {
                return comment;
            }

            /**
             *
             * @param comment
             * The comment
             */
            public void setComment(String comment) {
                this.comment = comment;
            }

            /**
             *
             * @return
             * The attachement
             */
            public String getAttachement() {
                return attachement;
            }

            /**
             *
             * @param attachement
             * The attachement
             */
            public void setAttachement(String attachement) {
                this.attachement = attachement;
            }

            /**
             *
             * @return
             * The path
             */
            public String getPath() {
                return path;
            }

            /**
             *
             * @param path
             * The path
             */
            public void setPath(String path) {
                this.path = path;
            }

        }

        public class ForcastSurvey {

            @SerializedName("custmer_survey_id")
            @Expose
            private String custmerSurveyId;
            @SerializedName("forecourtarea_clearity")
            @Expose
            private String forecourtareaClearity;
            @SerializedName("dispensers_functionality_properly")
            @Expose
            private String dispensersFunctionalityProperly;
            @SerializedName("signage_lighting_proper_order")
            @Expose
            private String signageLightingProperOrder;
            @SerializedName("lubricants_proper_available")
            @Expose
            private String lubricantsProperAvailable;
            @SerializedName("meter_reading_custmer")
            @Expose
            private String meterReadingCustmer;
            @SerializedName("wind_screen_cleaning")
            @Expose
            private String windScreenCleaning;
            @SerializedName("quick_oil_change")
            @Expose
            private String quickOilChange;
            @SerializedName("only_pso_lubricant_sold")
            @Expose
            private String onlyPsoLubricantSold;
            @SerializedName("all_the_fuel_prices_update_visible")
            @Expose
            private String allTheFuelPricesUpdateVisible;
            @SerializedName("dustbins_available")
            @Expose
            private String dustbinsAvailable;
            @SerializedName("staff_members_clean_proper")
            @Expose
            private String staffMembersCleanProper;
            @SerializedName("solgan_wall_presentable")
            @Expose
            private String solganWallPresentable;
            @SerializedName("clean_drinking_water_avaialble")
            @Expose
            private String cleanDrinkingWaterAvaialble;
            @SerializedName("explosive_license_valid_paid_challan")
            @Expose
            private String explosiveLicenseValidPaidChallan;
            @SerializedName("custmer_comment")
            @Expose
            private String custmerComment;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("station_id")
            @Expose
            private String stationId;
            @SerializedName("custmer_survey_forcort_rating")
            @Expose
            private String custmerSurveyForcortRating;
            @SerializedName("inspection_id")
            @Expose
            private String inspectionId;
            @SerializedName("delear_code")
            @Expose
            private String delearCode;

            /**
             *
             * @return
             * The custmerSurveyId
             */
            public String getCustmerSurveyId() {
                return custmerSurveyId;
            }

            /**
             *
             * @param custmerSurveyId
             * The custmer_survey_id
             */
            public void setCustmerSurveyId(String custmerSurveyId) {
                this.custmerSurveyId = custmerSurveyId;
            }

            /**
             *
             * @return
             * The forecourtareaClearity
             */
            public String getForecourtareaClearity() {
                return forecourtareaClearity;
            }

            /**
             *
             * @param forecourtareaClearity
             * The forecourtarea_clearity
             */
            public void setForecourtareaClearity(String forecourtareaClearity) {
                this.forecourtareaClearity = forecourtareaClearity;
            }

            /**
             *
             * @return
             * The dispensersFunctionalityProperly
             */
            public String getDispensersFunctionalityProperly() {
                return dispensersFunctionalityProperly;
            }

            /**
             *
             * @param dispensersFunctionalityProperly
             * The dispensers_functionality_properly
             */
            public void setDispensersFunctionalityProperly(String dispensersFunctionalityProperly) {
                this.dispensersFunctionalityProperly = dispensersFunctionalityProperly;
            }

            /**
             *
             * @return
             * The signageLightingProperOrder
             */
            public String getSignageLightingProperOrder() {
                return signageLightingProperOrder;
            }

            /**
             *
             * @param signageLightingProperOrder
             * The signage_lighting_proper_order
             */
            public void setSignageLightingProperOrder(String signageLightingProperOrder) {
                this.signageLightingProperOrder = signageLightingProperOrder;
            }

            /**
             *
             * @return
             * The lubricantsProperAvailable
             */
            public String getLubricantsProperAvailable() {
                return lubricantsProperAvailable;
            }

            /**
             *
             * @param lubricantsProperAvailable
             * The lubricants_proper_available
             */
            public void setLubricantsProperAvailable(String lubricantsProperAvailable) {
                this.lubricantsProperAvailable = lubricantsProperAvailable;
            }

            /**
             *
             * @return
             * The meterReadingCustmer
             */
            public String getMeterReadingCustmer() {
                return meterReadingCustmer;
            }

            /**
             *
             * @param meterReadingCustmer
             * The meter_reading_custmer
             */
            public void setMeterReadingCustmer(String meterReadingCustmer) {
                this.meterReadingCustmer = meterReadingCustmer;
            }

            /**
             *
             * @return
             * The windScreenCleaning
             */
            public String getWindScreenCleaning() {
                return windScreenCleaning;
            }

            /**
             *
             * @param windScreenCleaning
             * The wind_screen_cleaning
             */
            public void setWindScreenCleaning(String windScreenCleaning) {
                this.windScreenCleaning = windScreenCleaning;
            }

            /**
             *
             * @return
             * The quickOilChange
             */
            public String getQuickOilChange() {
                return quickOilChange;
            }

            /**
             *
             * @param quickOilChange
             * The quick_oil_change
             */
            public void setQuickOilChange(String quickOilChange) {
                this.quickOilChange = quickOilChange;
            }

            /**
             *
             * @return
             * The onlyPsoLubricantSold
             */
            public String getOnlyPsoLubricantSold() {
                return onlyPsoLubricantSold;
            }

            /**
             *
             * @param onlyPsoLubricantSold
             * The only_pso_lubricant_sold
             */
            public void setOnlyPsoLubricantSold(String onlyPsoLubricantSold) {
                this.onlyPsoLubricantSold = onlyPsoLubricantSold;
            }

            /**
             *
             * @return
             * The allTheFuelPricesUpdateVisible
             */
            public String getAllTheFuelPricesUpdateVisible() {
                return allTheFuelPricesUpdateVisible;
            }

            /**
             *
             * @param allTheFuelPricesUpdateVisible
             * The all_the_fuel_prices_update_visible
             */
            public void setAllTheFuelPricesUpdateVisible(String allTheFuelPricesUpdateVisible) {
                this.allTheFuelPricesUpdateVisible = allTheFuelPricesUpdateVisible;
            }

            /**
             *
             * @return
             * The dustbinsAvailable
             */
            public String getDustbinsAvailable() {
                return dustbinsAvailable;
            }

            /**
             *
             * @param dustbinsAvailable
             * The dustbins_available
             */
            public void setDustbinsAvailable(String dustbinsAvailable) {
                this.dustbinsAvailable = dustbinsAvailable;
            }

            /**
             *
             * @return
             * The staffMembersCleanProper
             */
            public String getStaffMembersCleanProper() {
                return staffMembersCleanProper;
            }

            /**
             *
             * @param staffMembersCleanProper
             * The staff_members_clean_proper
             */
            public void setStaffMembersCleanProper(String staffMembersCleanProper) {
                this.staffMembersCleanProper = staffMembersCleanProper;
            }

            /**
             *
             * @return
             * The solganWallPresentable
             */
            public String getSolganWallPresentable() {
                return solganWallPresentable;
            }

            /**
             *
             * @param solganWallPresentable
             * The solgan_wall_presentable
             */
            public void setSolganWallPresentable(String solganWallPresentable) {
                this.solganWallPresentable = solganWallPresentable;
            }

            /**
             *
             * @return
             * The cleanDrinkingWaterAvaialble
             */
            public String getCleanDrinkingWaterAvaialble() {
                return cleanDrinkingWaterAvaialble;
            }

            /**
             *
             * @param cleanDrinkingWaterAvaialble
             * The clean_drinking_water_avaialble
             */
            public void setCleanDrinkingWaterAvaialble(String cleanDrinkingWaterAvaialble) {
                this.cleanDrinkingWaterAvaialble = cleanDrinkingWaterAvaialble;
            }

            /**
             *
             * @return
             * The explosiveLicenseValidPaidChallan
             */
            public String getExplosiveLicenseValidPaidChallan() {
                return explosiveLicenseValidPaidChallan;
            }

            /**
             *
             * @param explosiveLicenseValidPaidChallan
             * The explosive_license_valid_paid_challan
             */
            public void setExplosiveLicenseValidPaidChallan(String explosiveLicenseValidPaidChallan) {
                this.explosiveLicenseValidPaidChallan = explosiveLicenseValidPaidChallan;
            }

            /**
             *
             * @return
             * The custmerComment
             */
            public String getCustmerComment() {
                return custmerComment;
            }

            /**
             *
             * @param custmerComment
             * The custmer_comment
             */
            public void setCustmerComment(String custmerComment) {
                this.custmerComment = custmerComment;
            }

            /**
             *
             * @return
             * The userId
             */
            public String getUserId() {
                return userId;
            }

            /**
             *
             * @param userId
             * The user_id
             */
            public void setUserId(String userId) {
                this.userId = userId;
            }

            /**
             *
             * @return
             * The stationId
             */
            public String getStationId() {
                return stationId;
            }

            /**
             *
             * @param stationId
             * The station_id
             */
            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            /**
             *
             * @return
             * The custmerSurveyForcortRating
             */
            public String getCustmerSurveyForcortRating() {
                return custmerSurveyForcortRating;
            }

            /**
             *
             * @param custmerSurveyForcortRating
             * The custmer_survey_forcort_rating
             */
            public void setCustmerSurveyForcortRating(String custmerSurveyForcortRating) {
                this.custmerSurveyForcortRating = custmerSurveyForcortRating;
            }

            /**
             *
             * @return
             * The inspectionId
             */
            public String getInspectionId() {
                return inspectionId;
            }

            /**
             *
             * @param inspectionId
             * The inspection_id
             */
            public void setInspectionId(String inspectionId) {
                this.inspectionId = inspectionId;
            }

            /**
             *
             * @return
             * The delearCode
             */
            public String getDelearCode() {
                return delearCode;
            }

            /**
             *
             * @param delearCode
             * The delear_code
             */
            public void setDelearCode(String delearCode) {
                this.delearCode = delearCode;
            }

        }

        public class HseSurvey {

            @SerializedName("hse_survey_id")
            @Expose
            private String hseSurveyId;
            @SerializedName("hse_3kg_co_extinguisher")
            @Expose
            private String hse3kgCoExtinguisher;
            @SerializedName("hse_50kg_dcp_trolley")
            @Expose
            private String hse50kgDcpTrolley;
            @SerializedName("hse_6kg_dcp_extinguisher")
            @Expose
            private String hse6kgDcpExtinguisher;
            @SerializedName("hse_earthing")
            @Expose
            private String hseEarthing;
            @SerializedName("hse_all_wiring")
            @Expose
            private String hseAllWiring;
            @SerializedName("hse_gas_leakage")
            @Expose
            private String hseGasLeakage;
            @SerializedName("hse_visible_sign")
            @Expose
            private String hseVisibleSign;
            @SerializedName("hse_compressors_covered_guards")
            @Expose
            private String hseCompressorsCoveredGuards;
            @SerializedName("hse_bonding_cable_working")
            @Expose
            private String hseBondingCableWorking;
            @SerializedName("hse_toilets_cleans")
            @Expose
            private String hseToiletsCleans;
            @SerializedName("hse_shop_stop")
            @Expose
            private String hseShopStop;
            @SerializedName("hse_cng_compressor_free")
            @Expose
            private String hseCngCompressorFree;
            @SerializedName("station_id")
            @Expose
            private String stationId;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("hse_comment")
            @Expose
            private String hseComment;
            @SerializedName("sufficent_buckets_available")
            @Expose
            private String sufficentBucketsAvailable;
            @SerializedName("hse_survey_rating")
            @Expose
            private String hseSurveyRating;
            @SerializedName("inspection_id")
            @Expose
            private String inspectionId;
            @SerializedName("delear_code")
            @Expose
            private String delearCode;

            /**
             *
             * @return
             * The hseSurveyId
             */
            public String getHseSurveyId() {
                return hseSurveyId;
            }

            /**
             *
             * @param hseSurveyId
             * The hse_survey_id
             */
            public void setHseSurveyId(String hseSurveyId) {
                this.hseSurveyId = hseSurveyId;
            }

            /**
             *
             * @return
             * The hse3kgCoExtinguisher
             */
            public String getHse3kgCoExtinguisher() {
                return hse3kgCoExtinguisher;
            }

            /**
             *
             * @param hse3kgCoExtinguisher
             * The hse_3kg_co_extinguisher
             */
            public void setHse3kgCoExtinguisher(String hse3kgCoExtinguisher) {
                this.hse3kgCoExtinguisher = hse3kgCoExtinguisher;
            }

            /**
             *
             * @return
             * The hse50kgDcpTrolley
             */
            public String getHse50kgDcpTrolley() {
                return hse50kgDcpTrolley;
            }

            /**
             *
             * @param hse50kgDcpTrolley
             * The hse_50kg_dcp_trolley
             */
            public void setHse50kgDcpTrolley(String hse50kgDcpTrolley) {
                this.hse50kgDcpTrolley = hse50kgDcpTrolley;
            }

            /**
             *
             * @return
             * The hse6kgDcpExtinguisher
             */
            public String getHse6kgDcpExtinguisher() {
                return hse6kgDcpExtinguisher;
            }

            /**
             *
             * @param hse6kgDcpExtinguisher
             * The hse_6kg_dcp_extinguisher
             */
            public void setHse6kgDcpExtinguisher(String hse6kgDcpExtinguisher) {
                this.hse6kgDcpExtinguisher = hse6kgDcpExtinguisher;
            }

            /**
             *
             * @return
             * The hseEarthing
             */
            public String getHseEarthing() {
                return hseEarthing;
            }

            /**
             *
             * @param hseEarthing
             * The hse_earthing
             */
            public void setHseEarthing(String hseEarthing) {
                this.hseEarthing = hseEarthing;
            }

            /**
             *
             * @return
             * The hseAllWiring
             */
            public String getHseAllWiring() {
                return hseAllWiring;
            }

            /**
             *
             * @param hseAllWiring
             * The hse_all_wiring
             */
            public void setHseAllWiring(String hseAllWiring) {
                this.hseAllWiring = hseAllWiring;
            }

            /**
             *
             * @return
             * The hseGasLeakage
             */
            public String getHseGasLeakage() {
                return hseGasLeakage;
            }

            /**
             *
             * @param hseGasLeakage
             * The hse_gas_leakage
             */
            public void setHseGasLeakage(String hseGasLeakage) {
                this.hseGasLeakage = hseGasLeakage;
            }

            /**
             *
             * @return
             * The hseVisibleSign
             */
            public String getHseVisibleSign() {
                return hseVisibleSign;
            }

            /**
             *
             * @param hseVisibleSign
             * The hse_visible_sign
             */
            public void setHseVisibleSign(String hseVisibleSign) {
                this.hseVisibleSign = hseVisibleSign;
            }

            /**
             *
             * @return
             * The hseCompressorsCoveredGuards
             */
            public String getHseCompressorsCoveredGuards() {
                return hseCompressorsCoveredGuards;
            }

            /**
             *
             * @param hseCompressorsCoveredGuards
             * The hse_compressors_covered_guards
             */
            public void setHseCompressorsCoveredGuards(String hseCompressorsCoveredGuards) {
                this.hseCompressorsCoveredGuards = hseCompressorsCoveredGuards;
            }

            /**
             *
             * @return
             * The hseBondingCableWorking
             */
            public String getHseBondingCableWorking() {
                return hseBondingCableWorking;
            }

            /**
             *
             * @param hseBondingCableWorking
             * The hse_bonding_cable_working
             */
            public void setHseBondingCableWorking(String hseBondingCableWorking) {
                this.hseBondingCableWorking = hseBondingCableWorking;
            }

            /**
             *
             * @return
             * The hseToiletsCleans
             */
            public String getHseToiletsCleans() {
                return hseToiletsCleans;
            }

            /**
             *
             * @param hseToiletsCleans
             * The hse_toilets_cleans
             */
            public void setHseToiletsCleans(String hseToiletsCleans) {
                this.hseToiletsCleans = hseToiletsCleans;
            }

            /**
             *
             * @return
             * The hseShopStop
             */
            public String getHseShopStop() {
                return hseShopStop;
            }

            /**
             *
             * @param hseShopStop
             * The hse_shop_stop
             */
            public void setHseShopStop(String hseShopStop) {
                this.hseShopStop = hseShopStop;
            }

            /**
             *
             * @return
             * The hseCngCompressorFree
             */
            public String getHseCngCompressorFree() {
                return hseCngCompressorFree;
            }

            /**
             *
             * @param hseCngCompressorFree
             * The hse_cng_compressor_free
             */
            public void setHseCngCompressorFree(String hseCngCompressorFree) {
                this.hseCngCompressorFree = hseCngCompressorFree;
            }

            /**
             *
             * @return
             * The stationId
             */
            public String getStationId() {
                return stationId;
            }

            /**
             *
             * @param stationId
             * The station_id
             */
            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            /**
             *
             * @return
             * The userId
             */
            public String getUserId() {
                return userId;
            }

            /**
             *
             * @param userId
             * The user_id
             */
            public void setUserId(String userId) {
                this.userId = userId;
            }

            /**
             *
             * @return
             * The hseComment
             */
            public String getHseComment() {
                return hseComment;
            }

            /**
             *
             * @param hseComment
             * The hse_comment
             */
            public void setHseComment(String hseComment) {
                this.hseComment = hseComment;
            }

            /**
             *
             * @return
             * The sufficentBucketsAvailable
             */
            public String getSufficentBucketsAvailable() {
                return sufficentBucketsAvailable;
            }

            /**
             *
             * @param sufficentBucketsAvailable
             * The sufficent_buckets_available
             */
            public void setSufficentBucketsAvailable(String sufficentBucketsAvailable) {
                this.sufficentBucketsAvailable = sufficentBucketsAvailable;
            }

            /**
             *
             * @return
             * The hseSurveyRating
             */
            public String getHseSurveyRating() {
                return hseSurveyRating;
            }

            /**
             *
             * @param hseSurveyRating
             * The hse_survey_rating
             */
            public void setHseSurveyRating(String hseSurveyRating) {
                this.hseSurveyRating = hseSurveyRating;
            }

            /**
             *
             * @return
             * The inspectionId
             */
            public String getInspectionId() {
                return inspectionId;
            }

            /**
             *
             * @param inspectionId
             * The inspection_id
             */
            public void setInspectionId(String inspectionId) {
                this.inspectionId = inspectionId;
            }

            /**
             *
             * @return
             * The delearCode
             */
            public String getDelearCode() {
                return delearCode;
            }

            /**
             *
             * @param delearCode
             * The delear_code
             */
            public void setDelearCode(String delearCode) {
                this.delearCode = delearCode;
            }

        }
        public class Tank {

            @SerializedName("tank_id")
            @Expose
            private String tankId;
            @SerializedName("tank_name")
            @Expose
            private String tankName;
            @SerializedName("opening_balance")
            @Expose
            private String openingBalance;
            @SerializedName("station_id")
            @Expose
            private String stationId;
            @SerializedName("tank_allquantity")
            @Expose
            private String tankAllquantity;
            @SerializedName("dispensary")
            @Expose
            private String dispensary;
            @SerializedName("tank_type")
            @Expose
            private String tankType;
            @SerializedName("tank_timezone")
            @Expose
            private String tankTimezone;
            @SerializedName("delear_code")
            @Expose
            private String delearCode;

            @SerializedName("remark")
            @Expose
            private String remark;

            @SerializedName("remark_date")
            @Expose
            private String remarkDate;

            @SerializedName("tank_flush")
            @Expose
            private String tankFlush;

            @SerializedName("tank_max")
            @Expose
            private String tankMax;


            @SerializedName("previous_balance")
            @Expose
            private String previousBalance;
            @SerializedName("tank_remain_bal")
            @Expose
            private Integer tankRemainBal;
            @SerializedName("Nozzle")
            @Expose
            private List<Nozzle> nozzle = new ArrayList<Nozzle>();

            /**
             *
             * @return
             * The remarkDate
             */
            public String getRemarkDate() {
                return remarkDate;
            }

            /**
             *
             * @param remarkDate
             * The remark_date
             */
            public void setRemarkDate(String remarkDate) {
                this.remarkDate = remarkDate;
            }

            /**
             *
             * @return
             * The tankId
             */
            public String getTankId() {
                return tankId;
            }

            /**
             *
             * @param tankId
             * The tank_id
             */
            public void setTankId(String tankId) {
                this.tankId = tankId;
            }

            /**
             *
             * @return
             * The tankFlush
             */
            public String getTankFlush() {
                return tankFlush;
            }

            /**
             *
             * @param tankFlush
             * The tank_flush
             */
            public void setTankFlush(String tankFlush) {
                this.tankFlush = tankFlush;
            }

            /**
             *
             * @return
             * The remark
             */
            public String getRemark() {
                return remark;
            }

            /**
             *
             * @param remark
             * The remark
             */
            public void setRemark(String remark) {
                this.remark = remark;
            }



            /**
             *
             * @return
             * The tankName
             */
            public String getTankName() {
                return tankName;
            }

            /**
             *
             * @param tankName
             * The tank_name
             */
            public void setTankName(String tankName) {
                this.tankName = tankName;
            }

            /**
             *
             * @return
             * The openingBalance
             */
            public String getOpeningBalance() {
                return openingBalance;
            }

            /**
             *
             * @param openingBalance
             * The opening_balance
             */
            public void setOpeningBalance(String openingBalance) {
                this.openingBalance = openingBalance;
            }

            /**
             *
             * @return
             * The stationId
             */
            public String getStationId() {
                return stationId;
            }

            /**
             *
             * @param stationId
             * The station_id
             */
            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            /**
             *
             * @return
             * The tankAllquantity
             */
            public String getTankAllquantity() {
                return tankAllquantity;
            }

            /**
             *
             * @param tankAllquantity
             * The tank_allquantity
             */
            public void setTankAllquantity(String tankAllquantity) {
                this.tankAllquantity = tankAllquantity;
            }

            /**
             *
             * @return
             * The dispensary
             */
            public String getDispensary() {
                return dispensary;
            }

            /**
             *
             * @param dispensary
             * The dispensary
             */
            public void setDispensary(String dispensary) {
                this.dispensary = dispensary;
            }

            /**
             *
             * @return
             * The tankType
             */
            public String getTankType() {
                return tankType;
            }

            /**
             *
             * @param tankType
             * The tank_type
             */
            public void setTankType(String tankType) {
                this.tankType = tankType;
            }

            /**
             *
             * @return
             * The tankTimezone
             */
            public String getTankTimezone() {
                return tankTimezone;
            }

            /**
             *
             * @param tankTimezone
             * The tank_timezone
             */
            public void setTankTimezone(String tankTimezone) {
                this.tankTimezone = tankTimezone;
            }

            /**
             *
             * @return
             * The delearCode
             */
            public String getDelearCode() {
                return delearCode;
            }

            /**
             *
             * @param delearCode
             * The delear_code
             */
            public void setDelearCode(String delearCode) {
                this.delearCode = delearCode;
            }

            /**
             *
             * @return
             * The tankMax
             */
            public String getTankMax() {
                return tankMax;
            }

            /**
             *
             * @param tankMax
             * The tank_max
             */
            public void setTankMax(String tankMax) {
                this.tankMax = tankMax;
            }

            /**
             *
             * @return
             * The previousBalance
             */
            public String getPreviousBalance() {
                return previousBalance;
            }

            /**
             *
             * @param previousBalance
             * The previous_balance
             */
            public void setPreviousBalance(String previousBalance) {
                this.previousBalance = previousBalance;
            }

            /**
             *
             * @return
             * The tankRemainBal
             */
            public Integer getTankRemainBal() {
                return tankRemainBal;
            }

            /**
             *
             * @param tankRemainBal
             * The tank_remain_bal
             */
            public void setTankRemainBal(Integer tankRemainBal) {
                this.tankRemainBal = tankRemainBal;
            }

            /**
             *
             * @return
             * The nozzle
             */
            public List<Nozzle> getNozzle() {
                return nozzle;
            }

            /**
             *
             * @param nozzle
             * The Nozzle
             */
            public void setNozzle(List<Nozzle> nozzle) {
                this.nozzle = nozzle;
            }

            public class Nozzle {

                @SerializedName("dumeter_id")
                @Expose
                private String dumeterId;
                @SerializedName("tank_id")
                @Expose
                private String tankId;
                @SerializedName("opening_balance_d")
                @Expose
                private String openingBalanceD;
                @SerializedName("dumeter_name")
                @Expose
                private String dumeterName;
                @SerializedName("dumeter_type")
                @Expose
                private String dumeterType;
                @SerializedName("station_id")
                @Expose
                private String stationId;
                @SerializedName("dumeter_allquantity")
                @Expose
                private String dumeterAllquantity;
                @SerializedName("dispensary_d")
                @Expose
                private String dispensaryD;
                @SerializedName("sap_code")
                @Expose
                private String sapCode;
                @SerializedName("dumeter_timezone")
                @Expose
                private String dumeterTimezone;
                @SerializedName("dumeter_product")
                @Expose
                private String dumeterProduct;
                @SerializedName("dumeter_accuracy")
                @Expose
                private String dumeterAccuracy;
                @SerializedName("dumeter_defect")
                @Expose
                private String dumeterDefect;
                @SerializedName("dumeter_max_value")
                @Expose
                private String dumeterMaxValue;
                @SerializedName("opening_bal")
                @Expose
                private String openingBal;
                @SerializedName("dumeter_previous_volume")
                @Expose
                private String dumeterPreviousVolume;
                @SerializedName("dumeter_remaining_bal")
                @Expose
//                edited by exd
                private Long dumeterRemainingBal;

                @SerializedName("comments_reset")
                @Expose
                private String commentsReset="";
                @SerializedName("reset_status")
                @Expose
                private String resetStatus="";
                @SerializedName("special_reading_reset")
                @Expose
                private String specialReadingReset="";


                /**
                 * @return The resetStatus
                 */
                public String getResetStatus() {
                    if (TextUtils.isEmpty(resetStatus))
                        return "0";
                    return resetStatus;
                }

                /**
                 * @param resetStatus The reset_status
                 */
                public void setResetStatus(String resetStatus) {
                    this.resetStatus = resetStatus;
                }

                /**
                 * @return The commentsReset
                 */
                public String getCommentsReset() {
                    if (TextUtils.isEmpty(commentsReset))
                        return "";
                    return commentsReset;
                }

                /**
                 * @param commentsReset The comments_reset
                 */
                public void setCommentsReset(String commentsReset) {
                    this.commentsReset = commentsReset;
                }

                /**
                 * @return The specialReadingReset
                 */
                public String getSpecialReadingReset() {
                    if (TextUtils.isEmpty(specialReadingReset))
                        return "0";
                    return specialReadingReset;
                }

                /**
                 * @param specialReadingReset The special_reading_reset
                 */
                public void setSpecialReadingReset(String specialReadingReset) {
                    this.specialReadingReset = specialReadingReset;
                }
                /**
                 *
                 * @return
                 * The dumeterId
                 */
                public String getDumeterId() {
                    return dumeterId;
                }

                /**
                 *
                 * @param dumeterId
                 * The dumeter_id
                 */
                public void setDumeterId(String dumeterId) {
                    this.dumeterId = dumeterId;
                }

                /**
                 *
                 * @return
                 * The tankId
                 */
                public String getTankId() {
                    return tankId;
                }

                /**
                 *
                 * @param tankId
                 * The tank_id
                 */
                public void setTankId(String tankId) {
                    this.tankId = tankId;
                }

                /**
                 *
                 * @return
                 * The openingBalanceD
                 */
                public String getOpeningBalanceD() {
                    return openingBalanceD;
                }

                /**
                 *
                 * @param openingBalanceD
                 * The opening_balance_d
                 */
                public void setOpeningBalanceD(String openingBalanceD) {
                    this.openingBalanceD = openingBalanceD;
                }

                /**
                 *
                 * @return
                 * The dumeterName
                 */
                public String getDumeterName() {
                    return dumeterName;
                }

                /**
                 *
                 * @param dumeterName
                 * The dumeter_name
                 */
                public void setDumeterName(String dumeterName) {
                    this.dumeterName = dumeterName;
                }

                /**
                 *
                 * @return
                 * The dumeterType
                 */
                public String getDumeterType() {
                    return dumeterType;
                }

                /**
                 *
                 * @param dumeterType
                 * The dumeter_type
                 */
                public void setDumeterType(String dumeterType) {
                    this.dumeterType = dumeterType;
                }

                /**
                 *
                 * @return
                 * The stationId
                 */
                public String getStationId() {
                    return stationId;
                }

                /**
                 *
                 * @param stationId
                 * The station_id
                 */
                public void setStationId(String stationId) {
                    this.stationId = stationId;
                }

                /**
                 *
                 * @return
                 * The dumeterAllquantity
                 */
                public String getDumeterAllquantity() {
                    return dumeterAllquantity;
                }

                /**
                 *
                 * @param dumeterAllquantity
                 * The dumeter_allquantity
                 */
                public void setDumeterAllquantity(String dumeterAllquantity) {
                    this.dumeterAllquantity = dumeterAllquantity;
                }

                /**
                 *
                 * @return
                 * The dispensaryD
                 */
                public String getDispensaryD() {
                    return dispensaryD;
                }

                /**
                 *
                 * @param dispensaryD
                 * The dispensary_d
                 */
                public void setDispensaryD(String dispensaryD) {
                    this.dispensaryD = dispensaryD;
                }

                /**
                 *
                 * @return
                 * The sapCode
                 */
                public String getSapCode() {
                    return sapCode;
                }

                /**
                 *
                 * @param sapCode
                 * The sap_code
                 */
                public void setSapCode(String sapCode) {
                    this.sapCode = sapCode;
                }

                /**
                 *
                 * @return
                 * The dumeterTimezone
                 */
                public String getDumeterTimezone() {
                    return dumeterTimezone;
                }

                /**
                 *
                 * @param dumeterTimezone
                 * The dumeter_timezone
                 */
                public void setDumeterTimezone(String dumeterTimezone) {
                    this.dumeterTimezone = dumeterTimezone;
                }

                /**
                 *
                 * @return
                 * The dumeterProduct
                 */
                public String getDumeterProduct() {
                    return dumeterProduct;
                }

                /**
                 *
                 * @param dumeterProduct
                 * The dumeter_product
                 */
                public void setDumeterProduct(String dumeterProduct) {
                    this.dumeterProduct = dumeterProduct;
                }

                /**
                 *
                 * @return
                 * The dumeterAccuracy
                 */
                public String getDumeterAccuracy() {
                    return dumeterAccuracy;
                }

                /**
                 *
                 * @param dumeterAccuracy
                 * The dumeter_accuracy
                 */
                public void setDumeterAccuracy(String dumeterAccuracy) {
                    this.dumeterAccuracy = dumeterAccuracy;
                }

                /**
                 *
                 * @return
                 * The dumeterDefect
                 */
                public String getDumeterDefect() {
                    return dumeterDefect;
                }

                /**
                 *
                 * @param dumeterDefect
                 * The dumeter_defect
                 */
                public void setDumeterDefect(String dumeterDefect) {
                    this.dumeterDefect = dumeterDefect;
                }

                /**
                 *
                 * @return
                 * The dumeterMaxValue
                 */
                public String getDumeterMaxValue() {
                    return dumeterMaxValue;
                }

                /**
                 *
                 * @param dumeterMaxValue
                 * The dumeter_max_value
                 */
                public void setDumeterMaxValue(String dumeterMaxValue) {
                    this.dumeterMaxValue = dumeterMaxValue;
                }

                /**
                 *
                 * @return
                 * The openingBal
                 */
                public String getOpeningBal() {
                    return openingBal;
                }

                /**
                 *
                 * @param openingBal
                 * The opening_bal
                 */
                public void setOpeningBal(String openingBal) {
                    this.openingBal = openingBal;
                }

                /**
                 *
                 * @return
                 * The dumeterPreviousVolume
                 */
                public String getDumeterPreviousVolume() {
                    return dumeterPreviousVolume;
                }

                /**
                 *
                 * @param dumeterPreviousVolume
                 * The dumeter_previous_volume
                 */
                public void setDumeterPreviousVolume(String dumeterPreviousVolume) {
                    this.dumeterPreviousVolume = dumeterPreviousVolume;
                }

                /**
                 *
                 * @return
                 * The dumeterRemainingBal
                 */
//                edited by exd
                public Long getDumeterRemainingBal() {
                    return dumeterRemainingBal;
                }

                /**
                 *
                 * @param dumeterRemainingBal
                 * The dumeter_remaining_bal
                 */
//                edited by exd
                public void setDumeterRemainingBal(Long dumeterRemainingBal) {
                    this.dumeterRemainingBal = dumeterRemainingBal;
                }

            }
        }
    }
}
