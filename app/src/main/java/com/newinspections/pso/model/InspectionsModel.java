package com.newinspections.pso.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Exd on 10/18/2017.
 */

public class InspectionsModel {

    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("Stations")
    private List<Stations> stations = new ArrayList<Stations>();

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStations(List<Stations> stations) {
        this.stations = stations;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<Stations> getStations() {
        if (stations==null){
            stations=new ArrayList<>();
        }
        return stations;
    }

    public static class Stations {

        @SerializedName("station_id")
        private String stationId;
        @SerializedName("station_name")
        private String stationName;
        @SerializedName("station_code")
        private String stationCode;
        @SerializedName("station_longitude")
        private String stationLongitude;
        @SerializedName("station_latitude")
        private String stationLatitude;
        @SerializedName("hse_data")
        private String hseData;
        @SerializedName("hse_rating")
        private String hseRating;
        @SerializedName("hse_questions")
        private String hseQuestions;
        @SerializedName("forecourt_data")
        private String forecourtData;
        @SerializedName("forecourt_rating")
        private String forecourtRating;
        @SerializedName("forecourt_questions")
        private String forecourtQuestions;
        @SerializedName("purpose")
        private String purpose;
        @SerializedName("comments")
        private String comments;
        @SerializedName("wow_training")
        private String wowTraining;
        @SerializedName("inspection_date")
        private String inspectionDate;
        @SerializedName("inspection_time")
        private String inspectionTime;
        @SerializedName("Tanks")
        private List<Tanks> tanks = new ArrayList<Tanks>();
        @SerializedName("Images")
        private List<Images> images = new ArrayList<Images>();
        @SerializedName("calculation")
        private List<Calculation> calculation = new ArrayList<Calculation>();
        @SerializedName("ProductCalculations")
        private List<ProductCalculation> productCalculations = new ArrayList<ProductCalculation>();
        @SerializedName("Purchase")
        private List<Purchase> purchases = new ArrayList<Purchase>();

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public void setStationLongitude(String stationLongitude) {
            this.stationLongitude = stationLongitude;
        }

        public void setStationLatitude(String stationLatitude) {
            this.stationLatitude = stationLatitude;
        }

        public void setHseData(String hseData) {
            this.hseData = hseData;
        }

        public void setHseRating(String hseRating) {
            this.hseRating = hseRating;
        }

        public void setHseQuestions(String hseQuestions) {
            this.hseQuestions = hseQuestions;
        }

        public void setForecourtData(String forecourtData) {
            this.forecourtData = forecourtData;
        }

        public void setForecourtRating(String forecourtRating) {
            this.forecourtRating = forecourtRating;
        }

        public void setForecourtQuestions(String forecourtQuestions) {
            this.forecourtQuestions = forecourtQuestions;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public void setWowTraining(String wowTraining) {
            this.wowTraining = wowTraining;
        }

        public void setInspectionDate(String inspectionDate) {
            this.inspectionDate = inspectionDate;
        }

        public void setInspectionTime(String inspectionTime) {
            this.inspectionTime = inspectionTime;
        }

        public void setTanks(List<Tanks> tanks) {
            this.tanks = tanks;
        }

        public void setImages(List<Images> images) {
            this.images = images;
        }

        public void setCalculation(List<Calculation> calculation) {
            this.calculation = calculation;
        }

        public void setProductCalculations(List<ProductCalculation> productCalculations) {
            this.productCalculations = productCalculations;
        }

        public void setPurchases(List<Purchase> purchases) {
            this.purchases = purchases;
        }

        public String getStationId() {
            return stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public String getStationCode() {
            return stationCode;
        }

        public String getStationLongitude() {
            return stationLongitude;
        }

        public String getStationLatitude() {
            return stationLatitude;
        }

        public String getHseData() {
            return hseData;
        }

        public String getHseRating() {
            return hseRating;
        }

        public String getHseQuestions() {
            return hseQuestions;
        }

        public String getForecourtData() {
            return forecourtData;
        }

        public String getForecourtRating() {
            return forecourtRating;
        }

        public String getForecourtQuestions() {
            return forecourtQuestions;
        }

        public String getPurpose() {
            return purpose;
        }

        public String getComments() {
            return comments;
        }

        public String getWowTraining() {
            return wowTraining;
        }

        public String getInspectionDate() {
            return inspectionDate;
        }

        public String getInspectionTime() {
            return inspectionTime;
        }

        public List<Calculation> getCalculation() {
            return calculation;
        }

        public List<Tanks> getTanks() {
            if (tanks==null){
                tanks=new ArrayList<>();
            }
            return tanks;
        }

        public List<Images> getImages() {
            if (images==null){
                images=new ArrayList<>();
            }
            return images;
        }

        public List<ProductCalculation> getProductCalculations() {
            return productCalculations;
        }

        public List<Purchase> getPurchases() {
            return purchases;
        }

        public class Images
        {
            @SerializedName("image_name")
            private String imageName;
            @SerializedName("image_path")
            private String imagePath;
            @SerializedName("image_text")
            private String imageText;
            @SerializedName("image_string")
            private String imageString;

            public void setImageName(String imageName) {
                this.imageName = imageName;
            }

            public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
            }

            public void setImageText(String imageText) {
                this.imageText = imageText;
            }

            public void setImageString(String imageString) {
                this.imageString = imageString;
            }

            public String getImageName() {
                return imageName;
            }

            public String getImagePath() {
                return imagePath;
            }

            public String getImageText() {
                return imageText;
            }

            public String getImageString() {
                return imageString;
            }
        }

        public class Tanks {

            public int tankIdTemporary = 0;
            @SerializedName("tank_id")
            private String tankId;
            @SerializedName("tank_name")
            private String tankName;
            @SerializedName("tank_code")
            private String tankCode;
            @SerializedName("tank_openingBalance")
            private String tankOpeningBalance;
            @SerializedName("tank_maximum")
            private String tankMaximum;
            @SerializedName("tank_previousReading")
            private String tankPreviousReading;
            @SerializedName("tank_currentReading")
            private String tankCurrentReading;
            @SerializedName("tank_producttype")
            private String tankProductType;
            @SerializedName("tank_oldproducttype")
            private String tankOldProductType = "";
            @SerializedName("tank_flush")
            private String tankFlush;
            @SerializedName("tank_remarks")
            private String tankRemarks;
            @SerializedName("tank_remarks_date")
            private String tankRemarksDate;
            @SerializedName("tank_newlyCreated")
            private String tankNewlyCreated;
            @SerializedName("tank_creationDate")
            private String tankCreationDate;
            @SerializedName("tank_createdBy")
            private String tankCreatedBy;
            @SerializedName("tank_updationDate")
            private String tankUpdationDate;
            @SerializedName("tank_updatedBy")
            private String tankUpdatedBy;
            @SerializedName("tank_allquantity")
            private String tankAllQuantity = "";
            @SerializedName("station_id")
            private String stationId;
            @SerializedName("Nozzles")
            private List<Nozzles> nozzles = new ArrayList<Nozzles>();

            public void setTankId(String tankId) {
                this.tankId = tankId;
            }

            public void setTankName(String tankName) {
                this.tankName = tankName;
            }

            public void setTankCode(String tankCode) {
                this.tankCode = tankCode;
            }

            public void setTankOpeningBalance(String tankOpeningBalance) {
                this.tankOpeningBalance = tankOpeningBalance;
            }

            public void setTankMaximum(String tankMaximum) {
                this.tankMaximum = tankMaximum;
            }

            public void setTankPreviousReading(String tankPreviousReading) {
                this.tankPreviousReading = tankPreviousReading;
            }

            public void setTankCurrentReading(String tankCurrentReading) {
                this.tankCurrentReading = tankCurrentReading;
            }

            public void setTankFlush(String tankFlush) {
                this.tankFlush = tankFlush;
            }

            public void setTankRemarks(String tankRemarks) {
                this.tankRemarks = tankRemarks;
            }

            public void setTankRemarksDate(String tankRemarksDate) {
                this.tankRemarksDate = tankRemarksDate;
            }

            public void setTankNewlyCreated(String tankNewlyCreated) {
                this.tankNewlyCreated = tankNewlyCreated;
            }

            public void setTankCreationDate(String tankCreationDate) {
                this.tankCreationDate = tankCreationDate;
            }

            public void setTankCreatedBy(String tankCreatedBy) {
                this.tankCreatedBy = tankCreatedBy;
            }

            public void setTankUpdationDate(String tankUpdationDate) {
                this.tankUpdationDate = tankUpdationDate;
            }

            public void setTankUpdatedBy(String tankUpdatedBy) {
                this.tankUpdatedBy = tankUpdatedBy;
            }

            public void setTankAllQuantity(String tankAllQuantity) {
                this.tankAllQuantity = tankAllQuantity;
            }

            public void setTankStationId(String stationId) {
                this.stationId = stationId;
            }

            public void setTankProductType(String tankProductType) {
                this.tankProductType = tankProductType;
            }

            public void setTankOldProductType(String tankOldProductType) {
                this.tankOldProductType = tankOldProductType;
            }

            public void setNozzles(List<Nozzles> nozzles) {
                this.nozzles = nozzles;
            }

            public String getTankId() {
                return tankId;
            }

            public String getTankName() {
                return tankName;
            }

            public String getTankCode() {
                return tankCode;
            }

            public String getTankOpeningBalance() {
                return tankOpeningBalance;
            }

            public String getTankMaximum() {
                return tankMaximum;
            }

            public String getTankPreviousReading() {
                return tankPreviousReading;
            }

            public String getTankCurrentReading() {
                return tankCurrentReading;
            }

            public String getTankFlush() {
                return tankFlush;
            }

            public String getTankRemarks() {
                return tankRemarks;
            }

            public String getTankRemarksDate() {
                return tankRemarksDate;
            }

            public String getTankNewlyCreated() {
                return tankNewlyCreated;
            }

            public String getTankCreationDate() {
                return tankCreationDate;
            }

            public String getTankCreatedBy() {
                return tankCreatedBy;
            }

            public String getTankUpdationDate() {
                return tankUpdationDate;
            }

            public String getTankUpdatedBy() {
                return tankUpdatedBy;
            }

            public String getStationId() {
                return stationId;
            }

            public String getTankProductType() {
                return tankProductType;
            }

            public String getTankOldProductType() {
                return tankOldProductType;
            }

            public String getTankAllQuantity() {
                return tankAllQuantity;
            }

            public List<Nozzles> getNozzles() {
                return nozzles;
            }

            public class Nozzles
            {
                @SerializedName("nozzle_id")
                private String nozzleId;
                @SerializedName("nozzle_name")
                private String nozzleName;
                @SerializedName("nozzle_code")
                private String nozzleCode;
                @SerializedName("nozzle_number")
                private String nozzleNumber;
                @SerializedName("nozzle_openingBalance")
                private String nozzleOpeningBalance;
                @SerializedName("nozzle_maximum")
                private String nozzleMaximum;
                @SerializedName("nozzle_previousReading")
                private String nozzlePreviousReading;
                @SerializedName("nozzle_currentReading")
                private String nozzleCurrentReading;
                @SerializedName("nozzle_producttype")
                private String nozzleProductType;
                @SerializedName("nozzle_defected")
                private String nozzleDefected = "";
                @SerializedName("nozzle_reset")
                private String nozzleReset;
                @SerializedName("nozzle_specialReading")
                private String nozzleSpecialReading;
                @SerializedName("nozzle_specialRemarks")
                private String nozzleSpecialRemarks;
                @SerializedName("nozzle_newlyCreated")
                private String nozzleNewlyCreated;
                @SerializedName("nozzle_creationDate")
                private String nozzleCreationDate;
                @SerializedName("nozzle_createdBy")
                private String nozzleCreatedBy;
                @SerializedName("nozzle_updationDate")
                private String nozzleUpdationDate;
                @SerializedName("nozzle_updatedBy")
                private String nozzleUpdatedBy;
                @SerializedName("tank_id")
                private String tankId;

                public void setNozzleId(String nozzleId) {
                    this.nozzleId = nozzleId;
                }

                public void setNozzleName(String nozzleName) {
                    this.nozzleName = nozzleName;
                }

                public void setNozzleCode(String nozzleCode) {
                    this.nozzleCode = nozzleCode;
                }

                public void setNozzleNumber(String nozzleNumber) {
                    this.nozzleNumber = nozzleNumber;
                }

                public void setNozzleOpeningBalance(String nozzleOpeningBalance) {
                    this.nozzleOpeningBalance = nozzleOpeningBalance;
                }

                public void setNozzleMaximum(String nozzleMaximum) {
                    this.nozzleMaximum = nozzleMaximum;
                }

                public void setNozzlePreviousReading(String nozzlePreviousReading) {
                    this.nozzlePreviousReading = nozzlePreviousReading;
                }

                public void setNozzleCurrentReading(String nozzleCurrentReading) {
                    this.nozzleCurrentReading = nozzleCurrentReading;
                }

                public void setNozzleProductType(String nozzleProductType) {
                    this.nozzleProductType = nozzleProductType;
                }

                public void setNozzleNewlyCreated(String nozzleNewlyCreated) {
                    this.nozzleNewlyCreated = nozzleNewlyCreated;
                }

                public void setNozzleDefected(String nozzleDefected) {
                    this.nozzleDefected = nozzleDefected;
                }

                public void setNozzleReset(String nozzleReset) {
                    this.nozzleReset = nozzleReset;
                }

                public void setNozzleSpecialReading(String nozzleSpecialReading) {
                    this.nozzleSpecialReading = nozzleSpecialReading;
                }

                public void setNozzleSpecialRemarks(String nozzleSpecialRemarks) {
                    this.nozzleSpecialRemarks = nozzleSpecialRemarks;
                }

                public void setNozzleCreationDate(String nozzleCreationDate) {
                    this.nozzleCreationDate = nozzleCreationDate;
                }

                public void setNozzleCreatedBy(String nozzleCreatedBy) {
                    this.nozzleCreatedBy = nozzleCreatedBy;
                }

                public void setNozzleUpdationDate(String nozzleUpdationDate) {
                    this.nozzleUpdationDate = nozzleUpdationDate;
                }

                public void setNozzleUpdatedBy(String nozzleUpdatedBy) {
                    this.nozzleUpdatedBy = nozzleUpdatedBy;
                }

                public void setTankId(String tankId) {
                    this.tankId = tankId;
                }

                public String getNozzleId() {
                    return nozzleId;
                }

                public String getNozzleName() {
                    return nozzleName;
                }

                public String getNozzleCode() {
                    return nozzleCode;
                }

                public String getNozzleNumber() {
                    return nozzleNumber;
                }

                public String getNozzleOpeningBalance() {
                    return nozzleOpeningBalance;
                }

                public String getNozzleMaximum() {
                    return nozzleMaximum;
                }

                public String getNozzlePreviousReading() {
                    return nozzlePreviousReading;
                }

                public String getNozzleCurrentReading() {
                    return nozzleCurrentReading;
                }

                public String getNozzleProductType() {
                    return nozzleProductType;
                }

                public String getNozzleNewlyCreated() {
                    return nozzleNewlyCreated;
                }

                public String getNozzleDefected() {
                    return nozzleDefected;
                }

                public String getNozzleReset() {
                    return nozzleReset;
                }

                public String getNozzleSpecialReading() {
                    return nozzleSpecialReading;
                }

                public String getNozzleSpecialRemarks() {
                    return nozzleSpecialRemarks;
                }

                public String getNozzleCreationDate() {
                    return nozzleCreationDate;
                }

                public String getNozzleCreatedBy() {
                    return nozzleCreatedBy;
                }

                public String getNozzleUpdationDate() {
                    return nozzleUpdationDate;
                }

                public String getNozzleUpdatedBy() {
                    return nozzleUpdatedBy;
                }

                public String getTankId() {
                    return tankId;
                }
            }

        }

        public class Calculation {

            @SerializedName("title")
            @Expose
            private String title = "";
            @SerializedName("description")
            @Expose
            private String description = "";
            @SerializedName("product")
            @Expose
            private String product = "";
            @SerializedName("values")
            @Expose
            private String values = "";


            public void setTitle(String title) {
                this.title = title;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setProduct(String product) {
                this.product = product;
            }

            public void setValues(String values) {
                this.values = values;
            }

            public String getTitle() {
                return title;
            }

            public String getDescription() {
                return description;
            }

            public String getProduct() {
                return product;
            }

            public String getValues() {
                return values;
            }
        }

        public class ProductCalculation {

            @SerializedName("product_name")
            private String productName = "";
            @SerializedName("A")
            private Double A = 0.0;
            @SerializedName("B")
            private Double B = 0.0;
            @SerializedName("C")
            private Double C = 0.0;
            @SerializedName("D")
            private Double D = 0.0;
            @SerializedName("E")
            private Double E = 0.0;
            @SerializedName("F")
            private Double F = 0.0;
            @SerializedName("Result")
            private Double Result = 0.0;
            @SerializedName("station_id")
            private int stationId = 0;

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public void setA(Double a) {
                A = a;
            }

            public void setB(Double b) {
                B = b;
            }

            public void setC(Double c) {
                C = c;
            }

            public void setD(Double d) {
                D = d;
            }

            public void setE(Double e) {
                E = e;
            }

            public void setF(Double f) {
                F = f;
            }

            public void setResult(Double result) {
                Result = result;
            }

            public void setStationId(int stationId) {
                this.stationId = stationId;
            }

            public String getProductName() {
                return productName;
            }

            public Double getA() {
                return A;
            }

            public Double getB() {
                return B;
            }

            public Double getC() {
                return C;
            }

            public Double getD() {
                return D;
            }

            public Double getE() {
                return E;
            }

            public Double getF() {
                return F;
            }

            public Double getResult() {
                return Result;
            }

            public int getStationId() {
                return stationId;
            }
        }

        public class Purchase {

            @SerializedName("Product")
            private String productName = "";
            @SerializedName("SAPPreviousData")
            private Double sapPrevious = 0.0;
            @SerializedName("Amount")
            private Double amount = 0.0;
            @SerializedName("station_id")
            private Double stationId = 0.0;

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public void setSapPrevious(Double sapPrevious) {
                this.sapPrevious = sapPrevious;
            }

            public void setAmount(Double amount) {
                this.amount = amount;
            }

            public String getProductName() {
                return productName;
            }

            public Double getSapPrevious() {
                return sapPrevious;
            }

            public Double getAmount() {
                return amount;
            }

            public Double getStationId() {
                return stationId;
            }
        }

    }
}
