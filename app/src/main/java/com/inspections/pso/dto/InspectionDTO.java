package com.inspections.pso.dto;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobiweb on 30/9/16.
 */
public class InspectionDTO {


        @SerializedName("status_mode")
        @Expose
        private String statusMode;
        @SerializedName("user_status")
        @Expose
        private Integer userStatus;
        @SerializedName("stations")
        @Expose
        private List<Station> stations = new ArrayList<Station>();
        @SerializedName("message")
        @Expose
        private String message="";
        @SerializedName("status")
        @Expose
        private Integer status;



        /**
         *
         * @return
         * The statusMode
         */
        public String getStatusMode() {
                return statusMode;
        }

        /**
         *
         * @param statusMode
         * The status_mode
         */
        public void setStatusMode(String statusMode) {
                this.statusMode = statusMode;
        }




        /**
         *
         * @return
         * The userStatus
         */
        public Integer getUserStatus() {
                return userStatus;
        }

        /**
         *
         * @param userStatus
         * The user_status
         */
        public void setUserStatus(Integer userStatus) {
                this.userStatus = userStatus;
        }

        /**
         *
         * @return
         * The stations
         */
        public List<Station> getStations() {
                if (stations==null){
                        stations=new ArrayList<>();
                }
                return stations;
        }

        /**
         *
         * @param stations
         * The stations
         */
        public void setStations(List<Station> stations) {
                this.stations = stations;
        }

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
        public Integer getStatus() {
                return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(Integer status) {
                this.status = status;
        }

        public class Station {

                @SerializedName("stationsDetalis")
                @Expose
                private StationsDetalis stationsDetalis;
                @SerializedName("total_tank")
                @Expose
                private Integer totalTank;
                @SerializedName("total_nozzzle")
                @Expose
                private Integer totalNozzzle;
                @SerializedName("tanks")
                @Expose
                private List<Tank> tanks = new ArrayList<Tank>();
                @SerializedName("calculation")
                @Expose
                private List<Calculation> calculation = new ArrayList<Calculation>();
                @SerializedName("A")
                @Expose
                private A a;
                @SerializedName("B")
                @Expose
                private B b;


                @SerializedName("purchase")
                @Expose
                private Purchase purchase;

                /**
                 *
                 * @return
                 * The purchase
                 */
                public Purchase getPurchase() {
                        return purchase;
                }

                /**
                 *
                 * @param purchase
                 * The purchase
                 */
                public void setPurchase(Purchase purchase) {
                        this.purchase = purchase;
                }
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
                 * The totalTank
                 */
                public Integer getTotalTank() {
                        return totalTank;
                }

                /**
                 *
                 * @param totalTank
                 * The total_tank
                 */
                public void setTotalTank(Integer totalTank) {
                        this.totalTank = totalTank;
                }

                /**
                 *
                 * @return
                 * The totalNozzzle
                 */
                public Integer getTotalNozzzle() {
                        return totalNozzzle;
                }

                /**
                 *
                 * @param totalNozzzle
                 * The total_nozzzle
                 */
                public void setTotalNozzzle(Integer totalNozzzle) {
                        this.totalNozzzle = totalNozzzle;
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
                 * The calculation
                 */
                public List<Calculation> getCalculation() {
                        if (calculation==null){
                                calculation=new ArrayList<>();
                        }
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

                /**
                 *
                 * @return
                 * The a
                 */
                public A getA() {
                        return a;
                }

                /**
                 *
                 * @param a
                 * The A
                 */
                public void setA(A a) {
                        this.a = a;
                }

                /**
                 *
                 * @return
                 * The b
                 */
                public B getB() {
                        return b;
                }

                /**
                 *
                 * @param b
                 * The B
                 */
                public void setB(B b) {
                        this.b = b;
                }



                public class Calculation {

                        @SerializedName("title")
                        @Expose
                        private String title;
                        @SerializedName("description")
                        @Expose
                        private String description;
                        @SerializedName("hsd")
                        @Expose
                        private String hsd;
                        @SerializedName("pmg")
                        @Expose
                        private String pmg;
                        @SerializedName("hobc")
                        @Expose
                        private String hobc;

                        /**
                         *
                         * @return
                         * The title
                         */
                        public String getTitle() {
                                return title;
                        }

                        /**
                         *
                         * @param title
                         * The title
                         */
                        public void setTitle(String title) {
                                this.title = title;
                        }

                        /**
                         *
                         * @return
                         * The description
                         */
                        public String getDescription() {
                                return description;
                        }

                        /**
                         *
                         * @param description
                         * The description
                         */
                        public void setDescription(String description) {
                                this.description = description;
                        }

                        /**
                         *
                         * @return
                         * The hsd
                         */
                        public String getHsd() {
                                return hsd;
                        }

                        /**
                         *
                         * @param hsd
                         * The hsd
                         */
                        public void setHsd(String hsd) {
                                this.hsd = hsd;
                        }

                        /**
                         *
                         * @return
                         * The pmg
                         */
                        public String getPmg() {
                                return pmg;
                        }

                        /**
                         *
                         * @param pmg
                         * The pmg
                         */
                        public void setPmg(String pmg) {
                                this.pmg = pmg;
                        }

                        /**
                         *
                         * @return
                         * The hobc
                         */
                        public String getHobc() {
                                return hobc;
                        }

                        /**
                         *
                         * @param hobc
                         * The hobc
                         */
                        public void setHobc(String hobc) {
                                this.hobc = hobc;
                        }

                }

                public class Purchase {

                        @SerializedName("station_name")
                        @Expose
                        private String stationName;
                        @SerializedName("first")
                        @Expose
                        private List<First> first = new ArrayList<First>();
                        @SerializedName("second")
                        @Expose
                        private List<Second> second = new ArrayList<Second>();
                        @SerializedName("third")
                        @Expose
                        private List<Third> third = new ArrayList<Third>();
                        @SerializedName("fourth")
                        @Expose
                        private List<Fourth> fourth = new ArrayList<Fourth>();
                        @SerializedName("fifth")
                        @Expose
                        private List<Fifth> fifth = new ArrayList<Fifth>();
                        @SerializedName("total")
                        @Expose
                        private List<List<Total>> total = new ArrayList<List<Total>>();

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
                         * The first
                         */
                        public List<First> getFirst() {
                                return first;
                        }

                        /**
                         *
                         * @param first
                         * The first
                         */
                        public void setFirst(List<First> first) {
                                this.first = first;
                        }

                        /**
                         *
                         * @return
                         * The second
                         */
                        public List<Second> getSecond() {
                                return second;
                        }

                        /**
                         *
                         * @param second
                         * The second
                         */
                        public void setSecond(List<Second> second) {
                                this.second = second;
                        }

                        /**
                         *
                         * @return
                         * The third
                         */
                        public List<Third> getThird() {
                                return third;
                        }

                        /**
                         *
                         * @param third
                         * The third
                         */
                        public void setThird(List<Third> third) {
                                this.third = third;
                        }

                        /**
                         *
                         * @return
                         * The fourth
                         */
                        public List<Fourth> getFourth() {
                                return fourth;
                        }

                        /**
                         *
                         * @param fourth
                         * The fourth
                         */
                        public void setFourth(List<Fourth> fourth) {
                                this.fourth = fourth;
                        }

                        /**
                         *
                         * @return
                         * The fifth
                         */
                        public List<Fifth> getFifth() {
                                return fifth;
                        }

                        /**
                         *
                         * @param fifth
                         * The fifth
                         */
                        public void setFifth(List<Fifth> fifth) {
                                this.fifth = fifth;
                        }

                        /**
                         *
                         * @return
                         * The total
                         */
                        public List<List<Total>> getTotal() {
                                return total;
                        }

                        /**
                         *
                         * @param total
                         * The total
                         */
                        public void setTotal(List<List<Total>> total) {
                                this.total = total;
                        }



                        public class First {

                                @SerializedName("month")
                                @Expose
                                private String month;
                                @SerializedName("invoice_no")
                                @Expose
                                private String invoiceNo;
                                @SerializedName("product")
                                @Expose
                                private String product;
                                @SerializedName("quantity")
                                @Expose
                                private String quantity;
                                @SerializedName("date")
                                @Expose
                                private String date;
                                @SerializedName("amount")
                                @Expose
                                private String amount;
                                @SerializedName("timestamp")
                                @Expose
                                private String timestamp;

                                /**
                                 *
                                 * @return
                                 * The month
                                 */
                                public String getMonth() {
                                        return month;
                                }

                                /**
                                 *
                                 * @param month
                                 * The month
                                 */
                                public void setMonth(String month) {
                                        this.month = month;
                                }

                                /**
                                 *
                                 * @return
                                 * The invoiceNo
                                 */
                                public String getInvoiceNo() {
                                        return invoiceNo;
                                }

                                /**
                                 *
                                 * @param invoiceNo
                                 * The invoice_no
                                 */
                                public void setInvoiceNo(String invoiceNo) {
                                        this.invoiceNo = invoiceNo;
                                }

                                /**
                                 *
                                 * @return
                                 * The product
                                 */
                                public String getProduct() {
                                        return product;
                                }

                                /**
                                 *
                                 * @param product
                                 * The product
                                 */
                                public void setProduct(String product) {
                                        this.product = product;
                                }

                                /**
                                 *
                                 * @return
                                 * The quantity
                                 */
                                public String getQuantity() {
                                        return quantity;
                                }

                                /**
                                 *
                                 * @param quantity
                                 * The quantity
                                 */
                                public void setQuantity(String quantity) {
                                        this.quantity = quantity;
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
                                 * The amount
                                 */
                                public String getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(String amount) {
                                        this.amount = amount;
                                }

                                /**
                                 *
                                 * @return
                                 * The timestamp
                                 */
                                public String getTimestamp() {
                                        return timestamp;
                                }

                                /**
                                 *
                                 * @param timestamp
                                 * The timestamp
                                 */
                                public void setTimestamp(String timestamp) {
                                        this.timestamp = timestamp;
                                }

                        }

                        public class Second {

                                @SerializedName("month")
                                @Expose
                                private String month;
                                @SerializedName("invoice_no")
                                @Expose
                                private String invoiceNo;
                                @SerializedName("product")
                                @Expose
                                private String product;
                                @SerializedName("quantity")
                                @Expose
                                private String quantity;
                                @SerializedName("date")
                                @Expose
                                private String date;
                                @SerializedName("amount")
                                @Expose
                                private String amount;
                                @SerializedName("timestamp")
                                @Expose
                                private String timestamp;

                                /**
                                 *
                                 * @return
                                 * The month
                                 */
                                public String getMonth() {
                                        return month;
                                }

                                /**
                                 *
                                 * @param month
                                 * The month
                                 */
                                public void setMonth(String month) {
                                        this.month = month;
                                }

                                /**
                                 *
                                 * @return
                                 * The invoiceNo
                                 */
                                public String getInvoiceNo() {
                                        return invoiceNo;
                                }

                                /**
                                 *
                                 * @param invoiceNo
                                 * The invoice_no
                                 */
                                public void setInvoiceNo(String invoiceNo) {
                                        this.invoiceNo = invoiceNo;
                                }

                                /**
                                 *
                                 * @return
                                 * The product
                                 */
                                public String getProduct() {
                                        return product;
                                }

                                /**
                                 *
                                 * @param product
                                 * The product
                                 */
                                public void setProduct(String product) {
                                        this.product = product;
                                }

                                /**
                                 *
                                 * @return
                                 * The quantity
                                 */
                                public String getQuantity() {
                                        return quantity;
                                }

                                /**
                                 *
                                 * @param quantity
                                 * The quantity
                                 */
                                public void setQuantity(String quantity) {
                                        this.quantity = quantity;
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
                                 * The amount
                                 */
                                public String getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(String amount) {
                                        this.amount = amount;
                                }

                                /**
                                 *
                                 * @return
                                 * The timestamp
                                 */
                                public String getTimestamp() {
                                        return timestamp;
                                }

                                /**
                                 *
                                 * @param timestamp
                                 * The timestamp
                                 */
                                public void setTimestamp(String timestamp) {
                                        this.timestamp = timestamp;
                                }

                        }
                        public class Third {

                                @SerializedName("month")
                                @Expose
                                private String month;
                                @SerializedName("invoice_no")
                                @Expose
                                private String invoiceNo;
                                @SerializedName("product")
                                @Expose
                                private String product;
                                @SerializedName("quantity")
                                @Expose
                                private String quantity;
                                @SerializedName("date")
                                @Expose
                                private String date;
                                @SerializedName("amount")
                                @Expose
                                private String amount;
                                @SerializedName("timestamp")
                                @Expose
                                private String timestamp;

                                /**
                                 *
                                 * @return
                                 * The month
                                 */
                                public String getMonth() {
                                        return month;
                                }

                                /**
                                 *
                                 * @param month
                                 * The month
                                 */
                                public void setMonth(String month) {
                                        this.month = month;
                                }

                                /**
                                 *
                                 * @return
                                 * The invoiceNo
                                 */
                                public String getInvoiceNo() {
                                        return invoiceNo;
                                }

                                /**
                                 *
                                 * @param invoiceNo
                                 * The invoice_no
                                 */
                                public void setInvoiceNo(String invoiceNo) {
                                        this.invoiceNo = invoiceNo;
                                }

                                /**
                                 *
                                 * @return
                                 * The product
                                 */
                                public String getProduct() {
                                        return product;
                                }

                                /**
                                 *
                                 * @param product
                                 * The product
                                 */
                                public void setProduct(String product) {
                                        this.product = product;
                                }

                                /**
                                 *
                                 * @return
                                 * The quantity
                                 */
                                public String getQuantity() {
                                        return quantity;
                                }

                                /**
                                 *
                                 * @param quantity
                                 * The quantity
                                 */
                                public void setQuantity(String quantity) {
                                        this.quantity = quantity;
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
                                 * The amount
                                 */
                                public String getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(String amount) {
                                        this.amount = amount;
                                }

                                /**
                                 *
                                 * @return
                                 * The timestamp
                                 */
                                public String getTimestamp() {
                                        return timestamp;
                                }

                                /**
                                 *
                                 * @param timestamp
                                 * The timestamp
                                 */
                                public void setTimestamp(String timestamp) {
                                        this.timestamp = timestamp;
                                }

                        }

                        public class Fifth {

                                @SerializedName("month")
                                @Expose
                                private String month;
                                @SerializedName("invoice_no")
                                @Expose
                                private String invoiceNo;
                                @SerializedName("product")
                                @Expose
                                private String product;
                                @SerializedName("quantity")
                                @Expose
                                private String quantity;
                                @SerializedName("date")
                                @Expose
                                private String date;
                                @SerializedName("amount")
                                @Expose
                                private String amount;
                                @SerializedName("timestamp")
                                @Expose
                                private String timestamp;

                                /**
                                 *
                                 * @return
                                 * The month
                                 */
                                public String getMonth() {
                                        return month;
                                }

                                /**
                                 *
                                 * @param month
                                 * The month
                                 */
                                public void setMonth(String month) {
                                        this.month = month;
                                }

                                /**
                                 *
                                 * @return
                                 * The invoiceNo
                                 */
                                public String getInvoiceNo() {
                                        return invoiceNo;
                                }

                                /**
                                 *
                                 * @param invoiceNo
                                 * The invoice_no
                                 */
                                public void setInvoiceNo(String invoiceNo) {
                                        this.invoiceNo = invoiceNo;
                                }

                                /**
                                 *
                                 * @return
                                 * The product
                                 */
                                public String getProduct() {
                                        return product;
                                }

                                /**
                                 *
                                 * @param product
                                 * The product
                                 */
                                public void setProduct(String product) {
                                        this.product = product;
                                }

                                /**
                                 *
                                 * @return
                                 * The quantity
                                 */
                                public String getQuantity() {
                                        return quantity;
                                }

                                /**
                                 *
                                 * @param quantity
                                 * The quantity
                                 */
                                public void setQuantity(String quantity) {
                                        this.quantity = quantity;
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
                                 * The amount
                                 */
                                public String getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(String amount) {
                                        this.amount = amount;
                                }

                                /**
                                 *
                                 * @return
                                 * The timestamp
                                 */
                                public String getTimestamp() {
                                        return timestamp;
                                }

                                /**
                                 *
                                 * @param timestamp
                                 * The timestamp
                                 */
                                public void setTimestamp(String timestamp) {
                                        this.timestamp = timestamp;
                                }

                        }
                        public class Total {

                                @SerializedName("month")
                                @Expose
                                private String month;
                                @SerializedName("product")
                                @Expose
                                private String product;
                                @SerializedName("amount")
                                @Expose
                                private String amount="";
                                @SerializedName("quantity")
                                @Expose
                                private String quantity;

                                /**
                                 * @return The month
                                 */
                                public String getMonth() {
                                        return month;
                                }

                                /**
                                 * @param month The month
                                 */
                                public void setMonth(String month) {
                                        this.month = month;
                                }

                                /**
                                 * @return The product
                                 */
                                public String getProduct() {
                                        return product;
                                }

                                /**
                                 * @param product The product
                                 */
                                public void setProduct(String product) {
                                        this.product = product;
                                }

                                /**
                                 * @return The amount
                                 */
                                public String getAmount() {
                                        return amount;
                                }

                                /**
                                 * @param amount The amount
                                 */
                                public void setAmount(String amount) {
                                        this.amount = amount;
                                }

                                /**
                                 * @return The quantity
                                 */
                                public String getQuantity() {
                                        return quantity;
                                }

                                /**
                                 * @param quantity The quantity
                                 */
                                public void setQuantity(String quantity) {
                                        this.quantity = quantity;
                                }

                        }
                                public class Fourth {

                                @SerializedName("month")
                                @Expose
                                private String month;
                                @SerializedName("invoice_no")
                                @Expose
                                private String invoiceNo;
                                @SerializedName("product")
                                @Expose
                                private String product;
                                @SerializedName("quantity")
                                @Expose
                                private String quantity;
                                @SerializedName("date")
                                @Expose
                                private String date;
                                @SerializedName("amount")
                                @Expose
                                private String amount;
                                @SerializedName("timestamp")
                                @Expose
                                private String timestamp;

                                /**
                                 *
                                 * @return
                                 * The month
                                 */
                                public String getMonth() {
                                        return month;
                                }

                                /**
                                 *
                                 * @param month
                                 * The month
                                 */
                                public void setMonth(String month) {
                                        this.month = month;
                                }

                                /**
                                 *
                                 * @return
                                 * The invoiceNo
                                 */
                                public String getInvoiceNo() {
                                        return invoiceNo;
                                }

                                /**
                                 *
                                 * @param invoiceNo
                                 * The invoice_no
                                 */
                                public void setInvoiceNo(String invoiceNo) {
                                        this.invoiceNo = invoiceNo;
                                }

                                /**
                                 *
                                 * @return
                                 * The product
                                 */
                                public String getProduct() {
                                        return product;
                                }

                                /**
                                 *
                                 * @param product
                                 * The product
                                 */
                                public void setProduct(String product) {
                                        this.product = product;
                                }

                                /**
                                 *
                                 * @return
                                 * The quantity
                                 */
                                public String getQuantity() {
                                        return quantity;
                                }

                                /**
                                 *
                                 * @param quantity
                                 * The quantity
                                 */
                                public void setQuantity(String quantity) {
                                        this.quantity = quantity;
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
                                 * The amount
                                 */
                                public String getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(String amount) {
                                        this.amount = amount;
                                }

                                /**
                                 *
                                 * @return
                                 * The timestamp
                                 */
                                public String getTimestamp() {
                                        return timestamp;
                                }

                                /**
                                 *
                                 * @param timestamp
                                 * The timestamp
                                 */
                                public void setTimestamp(String timestamp) {
                                        this.timestamp = timestamp;
                                }

                        }



                }
                public class StationsDetalis {

                        @SerializedName("user_st_id")
                        @Expose
                        private String userStId="";
                        @SerializedName("user_st_userid")
                        @Expose
                        private String userStUserid="";
                        @SerializedName("user_st_station_name")
                        @Expose
                        private String userStStationName="";
                        @SerializedName("user_st_timezone")
                        @Expose
                        private String userStTimezone="";
                        @SerializedName("status")
                        @Expose
                        private String status="";
                        @SerializedName("station_id")
                        @Expose
                        private String stationId="";
                        @SerializedName("station_name")
                        @Expose
                        private String stationName="";
                        @SerializedName("sap_code")
                        @Expose
                        private String sapCode="";
                        @SerializedName("timezone")
                        @Expose
                        private String timezone="";
                        @SerializedName("location")
                        @Expose
                        private String location="";
                        @SerializedName("station_latitude")
                        @Expose
                        private String stationLatitude="";
                        @SerializedName("station_longitude")
                        @Expose
                        private String stationLongitude="";
                        @SerializedName("division")
                        @Expose
                        private String division="";
                        @SerializedName("city")
                        @Expose
                        private String city="";

                        /**
                         *
                         * @return
                         * The userStId
                         */
                        public String getUserStId() {
                                return userStId;
                        }

                        /**
                         *
                         * @param userStId
                         * The user_st_id
                         */
                        public void setUserStId(String userStId) {
                                this.userStId = userStId;
                        }

                        /**
                         *
                         * @return
                         * The userStUserid
                         */
                        public String getUserStUserid() {
                                if (TextUtils.isEmpty(userStUserid))
                                        return "";
                                return userStUserid;
                        }

                        /**
                         *
                         * @param userStUserid
                         * The user_st_userid
                         */
                        public void setUserStUserid(String userStUserid) {
                                this.userStUserid = userStUserid;
                        }

                        /**
                         *
                         * @return
                         * The userStStationName
                         */
                        public String getUserStStationName() {
                                return userStStationName;
                        }

                        /**
                         *
                         * @param userStStationName
                         * The user_st_station_name
                         */
                        public void setUserStStationName(String userStStationName) {
                                this.userStStationName = userStStationName;
                        }

                        /**
                         *
                         * @return
                         * The userStTimezone
                         */
                        public String getUserStTimezone() {
                                return userStTimezone;
                        }

                        /**
                         *
                         * @param userStTimezone
                         * The user_st_timezone
                         */
                        public void setUserStTimezone(String userStTimezone) {
                                this.userStTimezone = userStTimezone;
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
                         * The stationId
                         */
                        public String getStationId() {
                                if (TextUtils.isEmpty(stationId))
                                        return "";
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
                                if (TextUtils.isEmpty(sapCode))
                                        return "";
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
                         * The timezone
                         */
                        public String getTimezone() {
                                return timezone;
                        }

                        /**
                         *
                         * @param timezone
                         * The timezone
                         */
                        public void setTimezone(String timezone) {
                                this.timezone = timezone;
                        }

                        /**
                         *
                         * @return
                         * The location
                         */
                        public String getLocation() {
                                return location;
                        }

                        /**
                         *
                         * @param location
                         * The location
                         */
                        public void setLocation(String location) {
                                this.location = location;
                        }

                        /**
                         *
                         * @return
                         * The stationLatitude
                         */
                        public String getStationLatitude() {
                                if (TextUtils.isEmpty(stationLatitude))
                                        return "";
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
                                if (TextUtils.isEmpty(stationLongitude))
                                        return "";
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
                         * The division
                         */
                        public String getDivision() {
                                return division;
                        }

                        /**
                         *
                         * @param division
                         * The division
                         */
                        public void setDivision(String division) {
                                this.division = division;
                        }

                        /**
                         *
                         * @return
                         * The city
                         */
                        public String getCity() {
                                return city;
                        }

                        /**
                         *
                         * @param city
                         * The city
                         */
                        public void setCity(String city) {
                                this.city = city;
                        }






                        @SerializedName("HSESurvayDATA")
                        @Expose
                        private String HSESurvayDATA="";

                        @SerializedName("FORCOURTSurvayDATA")
                        @Expose
                        private String FORCOURTSurvayDATA="";

                        @SerializedName("staff_members_clean_proper")
                        @Expose
                        private String staffMembersCleanProper="";
                        @SerializedName("tanks_reading")
                        @Expose
                        private String tanksReading="";
                        @SerializedName("dispensers_functionality_properly")
                        @Expose
                        private String dispensersFunctionalityProperly="";
                        @SerializedName("all_the_fuel_prices_update_visible")
                        @Expose
                        private String allTheFuelPricesUpdateVisible="";
                        @SerializedName("inspection_resion")
                        @Expose
                        private String inspectionResion="";
                        @SerializedName("dumeter_reading")
                        @Expose
                        private String dumeterReading="";
                        @SerializedName("custmer_comment")
                        @Expose
                        private String custmerComment="";
                        @SerializedName("forecourtarea_clearity")
                        @Expose
                        private String forecourtareaClearity="";
                        @SerializedName("clean_drinking_water_avaialble")
                        @Expose
                        private String cleanDrinkingWaterAvaialble="";
                        @SerializedName("purpose")
                        @Expose
                        private String purpose="";
                        @SerializedName("time")
                        @Expose
                        private String time="";
                        @SerializedName("dustbins_available")
                        @Expose
                        private String dustbinsAvailable="";
                        @SerializedName("user_id")
                        @Expose
                        private String userId="";
                        @SerializedName("comment")
                        @Expose
                        private String comment="";
                        @SerializedName("a_pmg")
                        @Expose
                        private String aPmg="";
                        @SerializedName("b_pmg")
                        @Expose
                        private String bPmg="";
                        @SerializedName("c_pmg")
                        @Expose
                        private String cPmg="";
                        @SerializedName("d_pmg")
                        @Expose
                        private String dPmg="";
                        @SerializedName("e_pmg")
                        @Expose
                        private String ePmg="";
                        @SerializedName("f_pmg")
                        @Expose
                        private String fPmg="";
                        @SerializedName("result_pmg")
                        @Expose
                        private String resultPmg="";
                        @SerializedName("a_hobc")
                        @Expose
                        private String aHobc="";
                        @SerializedName("b_hobc")
                        @Expose
                        private String bHobc="";
                        @SerializedName("c_hobc")
                        @Expose
                        private String cHobc="";
                        @SerializedName("d_hobc")
                        @Expose
                        private String dHobc;
                        @SerializedName("e_hobc")
                        @Expose
                        private String eHobc="";
                        @SerializedName("f_hobc")
                        @Expose
                        private String fHobc="";
                        @SerializedName("result_hobc")
                        @Expose
                        private String resultHobc="";
                        @SerializedName("a_hsd")
                        @Expose
                        private String aHsd="";
                        @SerializedName("b_hsd")
                        @Expose
                        private String bHsd="";
                        @SerializedName("c_hsd")
                        @Expose
                        private String cHsd="";
                        @SerializedName("d_hsd")
                        @Expose
                        private String dHsd="";
                        @SerializedName("e_hsd")
                        @Expose
                        private String eHsd="";
                        @SerializedName("f_hsd")
                        @Expose
                        private String fHsd="";
                        @SerializedName("result_hsd")
                        @Expose
                        private String resultHsd="";
                        @SerializedName("hse_reading")
                        @Expose
                        private String hseReading="";
                        @SerializedName("fore_cord_reading")
                        @Expose
                        private String foreCordReading="";
                        @SerializedName("hse_survey_rating")
                        @Expose
                        private String hseSurveyRating="";
                        @SerializedName("fore_cord_survey_rating")
                        @Expose
                        private String foreCordSurveyRating="";

                        @SerializedName("images")
                        @Expose
                        private List<Image> images = new ArrayList<Image>();


                        /**
                         *
                         * @return
                         * The HSESurvayDATA
                         */
                        public String getHSESurvayDATA() {
                                if (TextUtils.isEmpty(HSESurvayDATA))
                                        return "";
                                return HSESurvayDATA;
                        }

                        /**
                         *
                         * @param HSESurvayDATA
                         * The HSESurvayDATA
                         */
                        public void setHSESurvayDATA(String HSESurvayDATA) {
                                this.HSESurvayDATA = HSESurvayDATA;
                        }

                        /**
                         *
                         * @return
                         * The FORCOURTSurvayDATA
                         */
                        public String getFORCOURTSurvayDATA() {
                                if (TextUtils.isEmpty(FORCOURTSurvayDATA))
                                        return "";
                                return FORCOURTSurvayDATA;
                        }

                        /**
                         *
                         * @param FORCOURTSurvayDATA
                         * The FORCOURTSurvayDATA
                         */
                        public void setFORCOURTSurvayDATA(String FORCOURTSurvayDATA) {
                                this.FORCOURTSurvayDATA = FORCOURTSurvayDATA;
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
                         * The tanksReading
                         */
                        public String getTanksReading() {
                                return tanksReading;
                        }

                        /**
                         *
                         * @param tanksReading
                         * The tanks_reading
                         */
                        public void setTanksReading(String tanksReading) {
                                this.tanksReading = tanksReading;
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
                         * The inspectionResion
                         */
                        public String getInspectionResion() {
                                return inspectionResion;
                        }

                        /**
                         *
                         * @param inspectionResion
                         * The inspection_resion
                         */
                        public void setInspectionResion(String inspectionResion) {
                                this.inspectionResion = inspectionResion;
                        }

                        /**
                         *
                         * @return
                         * The dumeterReading
                         */
                        public String getDumeterReading() {
                                return dumeterReading;
                        }

                        /**
                         *
                         * @param dumeterReading
                         * The dumeter_reading
                         */
                        public void setDumeterReading(String dumeterReading) {
                                this.dumeterReading = dumeterReading;
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
                         * The purpose
                         */
                        public String getPurpose() {
                                if (TextUtils.isEmpty(purpose))
                                        return "";
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
                         * The time
                         */
                        public String getTime() {
                                if (TextUtils.isEmpty(time))
                                        return "";
                                return time;
                        }

                        /**
                         *
                         * @param time
                         * The time
                         */
                        public void setTime(String time) {
                                this.time = time;
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
                         * The comment
                         */
                        public String getComment() {
                                if (TextUtils.isEmpty(comment))
                                        return "";
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
                         * The hseReading
                         */
                        public String getHseReading() {
                                return hseReading;
                        }

                        /**
                         *
                         * @param hseReading
                         * The hse_reading
                         */
                        public void setHseReading(String hseReading) {
                                this.hseReading = hseReading;
                        }

                        /**
                         *
                         * @return
                         * The foreCordReading
                         */
                        public String getForeCordReading() {
                                return foreCordReading;
                        }

                        /**
                         *
                         * @param foreCordReading
                         * The fore_cord_reading
                         */
                        public void setForeCordReading(String foreCordReading) {
                                this.foreCordReading = foreCordReading;
                        }

                        /**
                         *
                         * @return
                         * The hseSurveyRating
                         */
                        public String getHseSurveyRating() {
                                if (TextUtils.isEmpty(hseSurveyRating))
                                        return "";
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
                         * The foreCordSurveyRating
                         */
                        public String getForeCordSurveyRating() {
                                if (TextUtils.isEmpty(foreCordSurveyRating))
                                        return "";
                                return foreCordSurveyRating;
                        }

                        /**
                         *
                         * @param foreCordSurveyRating
                         * The fore_cord_survey_rating
                         */
                        public void setForeCordSurveyRating(String foreCordSurveyRating) {
                                this.foreCordSurveyRating = foreCordSurveyRating;
                        }

                        /**
                         *
                         * @return
                         * The images
                         */
                        public List<Image> getImages() {
                                if (images==null){
                                        images = new ArrayList<Image>();
                                }
                                return images;
                        }

                        /**
                         *
                         * @param images
                         * The images
                         */
                        public void setImages(List<Image> images) {
                                this.images = images;
                        }



                        public class Image {

                                @SerializedName("name")
                                @Expose
                                private String name="";
                                @SerializedName("text")
                                @Expose
                                private String text="";
                                @SerializedName("status")
                                @Expose
                                private String status="";

                                /**
                                 *
                                 * @return
                                 * The name
                                 */
                                public String getName() {
                                        return name;
                                }

                                /**
                                 *
                                 * @param name
                                 * The name
                                 */
                                public void setName(String name) {
                                        this.name = name;
                                }

                                /**
                                 *
                                 * @return
                                 * The text
                                 */
                                public String getText() {
                                        return text;
                                }

                                /**
                                 *
                                 * @param text
                                 * The text
                                 */
                                public void setText(String text) {
                                        this.text = text;
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

                        }



                }



                public class Tank {

                        @SerializedName("tank_id")
                        @Expose
                        private String tankId="";
                        @SerializedName("tank_name")
                        @Expose
                        private String tankName="";
                        @SerializedName("opening_balance")
                        @Expose
                        private String openingBalance="";
                        @SerializedName("previous_balance")
                        @Expose
                        private String previousBalance="";


                        @SerializedName("station_id")
                        @Expose
                        private String stationId="";
                        @SerializedName("tank_allquantity")
                        @Expose
                        private String tankAllquantity="";
                        @SerializedName("dispensary")
                        @Expose
                        private String dispensary="";
                        @SerializedName("tank_flush")
                        @Expose
                        private String flush="0";
                        @SerializedName("tank_type")
                        @Expose
                        private String tankType="";
                        @SerializedName("old_tank_type")
                        @Expose
                        private String oldTankType="";
                        @SerializedName("tank_timezone")
                        @Expose
                        private String tankTimezone="";
                        @SerializedName("delear_code")
                        @Expose
                        private String delearCode="";
                        @SerializedName("tank_remain_bal")
                        @Expose
                        private Integer tankRemainBal;

                        @SerializedName("remark")
                        @Expose
                        private String remark="";
                        @SerializedName("remark_date")
                        @Expose
                        private String remarkDate="";
                        @SerializedName("tank_current_data")
                        @Expose
                        private String tankCurrentData="";
                        @SerializedName("tank_max")
                        @Expose
                        private String tankMaximumValue="";
                        @SerializedName("Nozzle")
                        @Expose
                        private List<Nozzle> nozzle = new ArrayList<Nozzle>();
                        /**
                         * @return The flush
                         */
                        public String getFlush() {
                                if (TextUtils.isEmpty(flush))
                                        return "0";
                                return flush;
                        }

                        /**
                         * @param flush The tank_flush
                         */
                        public void setFlush(String flush) {

                                this.flush = flush;
                        }

                        /**
                         * @return The tankName
                         */


                        /**
                         * @return The tankId
                         */
                        public String getTankId() {
                                if (TextUtils.isEmpty(tankId))
                                        return "";
                                return tankId;
                        }

                        /**
                         * @param tankId The tank_id
                         */
                        public void setTankId(String tankId) {
                                this.tankId = tankId;
                        }

                        /**
                         * @return The tankName
                         */
                        public String getTankName() {
                                return tankName;
                        }

                        /**
                         * @param tankName The tank_name
                         */
                        public void setTankName(String tankName) {
                                this.tankName = tankName;
                        }

                        /**
                         * @return The openingBalance
                         */
                        public String getOpeningBalance() {
                                if (TextUtils.isEmpty(openingBalance))
                                        return "";
                                return openingBalance;
                        }

                        /**
                         * @param openingBalance The opening_balance
                         */
                        public void setOpeningBalance(String openingBalance) {
                                this.openingBalance = openingBalance;
                        }


                        /**
                         * @return The previousBalance
                         */
                        public String getPreviousBalance() {
                                if (TextUtils.isEmpty(previousBalance))
                                        return "";
                                return previousBalance;
                        }

                        /**
                         * @param previousBalance The previous_balance
                         */
                        public void setPreviousBalance(String previousBalance) {
                                this.previousBalance = previousBalance;
                        }


                        /**
                         * @return The stationId
                         */
                        public String getStationId() {
                                return stationId;
                        }

                        /**
                         * @param stationId The station_id
                         */
                        public void setStationId(String stationId) {
                                this.stationId = stationId;
                        }

                        /**
                         * @return The tankAllquantity
                         */
                        public String getTankAllquantity() {
                                return tankAllquantity;
                        }

                        /**
                         * @param tankAllquantity The tank_allquantity
                         */
                        public void setTankAllquantity(String tankAllquantity) {
                                this.tankAllquantity = tankAllquantity;
                        }

                        /**
                         * @return The dispensary
                         */
                        public String getDispensary() {
                                if (TextUtils.isEmpty(dispensary))
                                        return "";
                                return dispensary;
                        }

                        /**
                         * @param dispensary The dispensary
                         */
                        public void setDispensary(String dispensary) {
                                this.dispensary = dispensary;
                        }

                        /**
                         * @return The tankType
                         */
                        public String getTankType() {
                                if (TextUtils.isEmpty(tankType))
                                        return "";
                                return tankType;
                        }

                        /**
                         * @param tankType The tank_type
                         */
                        public void setTankType(String tankType) {
                                this.tankType = tankType;
                        }


                        /**
                         * @return The oldTankType
                         */
                        public String getOldTankType() {
                                if (TextUtils.isEmpty(oldTankType))
                                        return "";
                                return oldTankType;
                        }

                        /**
                         * @param oldTankType The old_tank_type
                         */
                        public void setOldTankType(String oldTankType) {
                                this.oldTankType = oldTankType;
                        }

                        /**
                         * @return The tankTimezone
                         */
                        public String getTankTimezone() {
                                if (TextUtils.isEmpty(tankTimezone))
                                        return "";
                                return tankTimezone;
                        }

                        /**
                         * @param tankTimezone The tank_timezone
                         */
                        public void setTankTimezone(String tankTimezone) {
                                this.tankTimezone = tankTimezone;
                        }

                        /**
                         * @return The delearCode
                         */
                        public String getDelearCode() {
                                if (TextUtils.isEmpty(delearCode))
                                        return "";
                                return delearCode;
                        }

                        /**
                         * @param delearCode The delear_code
                         */
                        public void setDelearCode(String delearCode) {
                                this.delearCode = delearCode;
                        }

                        /**
                         * @return The tankRemainBal
                         */
                        public Integer getTankRemainBal() {
                                return tankRemainBal;
                        }

                        /**
                         * @param tankRemainBal The tank_remain_bal
                         */
                        public void setTankRemainBal(Integer tankRemainBal) {
                                this.tankRemainBal = tankRemainBal;
                        }
                        /**
                         *
                         * @return
                         * The remark
                         */
                        public String getRemark() {
                                if (TextUtils.isEmpty(remark))
                                        return "";
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
                         * The remarkDate
                         */
                        public String getRemarkDate() {
                                if (TextUtils.isEmpty(remarkDate))
                                        return "";
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
                         * The tankCurrentData
                         */
                        public String getTankCurrentData() {
                                if (TextUtils.isEmpty(tankCurrentData))
                                        return "";
                                return tankCurrentData;
                        }

                        /**
                         *
                         * @param tankCurrentData
                         * The tank_current_data
                         */
                        public void setTankCurrentData(String tankCurrentData) {
                                this.tankCurrentData = tankCurrentData;
                        }

                        /**
                         *
                         * @return
                         * The tankMaximumValue
                         */
                        public String getTankMaximumValue() {
                                if (TextUtils.isEmpty(tankMaximumValue))
                                        return "";
                                return tankMaximumValue;
                        }

                        /**
                         *
                         * @param tankMaximumValue
                         * The tank_max
                         */
                        public void setTankMaximumValue(String tankMaximumValue) {
                                this.tankMaximumValue = tankMaximumValue;
                        }

                        /**
                         * @return The nozzle
                         */
                        public List<Nozzle> getNozzle() {
                                return nozzle;
                        }

                        /**
                         * @param nozzle The Nozzle
                         */
                        public void setNozzle(List<Nozzle> nozzle) {
                                this.nozzle = nozzle;
                        }

                        public class Nozzle {

                                @SerializedName("dumeter_id")
                                @Expose
                                private String dumeterId="";
                                @SerializedName("tank_id")
                                @Expose
                                private String tankId="";
                                @SerializedName("opening_balance_d")
                                @Expose
                                private String openingBalanceD="";
                                @SerializedName("dumeter_name")
                                @Expose
                                private String dumeterName="";
                                @SerializedName("dumeter_type")
                                @Expose
                                private String dumeterType="";
                                @SerializedName("station_id")
                                @Expose
                                private String stationId="";
                                @SerializedName("dumeter_allquantity")
                                @Expose
                                private String dumeterAllquantity="";
                                @SerializedName("dispensary_d")
                                @Expose
                                private String dispensaryD="";
                                @SerializedName("sap_code")
                                @Expose
                                private String sapCode="";
                                @SerializedName("dumeter_timezone")
                                @Expose
                                private String dumeterTimezone="";
                                @SerializedName("dumeter_product")
                                @Expose
                                private String dumeterProduct="";
                                @SerializedName("dumeter_accuracy")
                                @Expose
                                private String dumeterAccuracy="";
                                @SerializedName("dumeter_defect")
                                @Expose
                                private String dumeterDefect="";
                                @SerializedName("comments_reset")
                                @Expose
                                private String commentsReset="";
                                @SerializedName("reset_status")
                                @Expose
                                private String resetStatus="";
                                @SerializedName("special_reading")
                                @Expose
                                private String specialReading="";
                                @SerializedName("dumeter_max_value")
                                @Expose
                                private String dumeterMaxValue="";
                                @SerializedName("opening_bal")
                                @Expose
                                private String openingBal="";
                                @SerializedName("dumeter_previous_volume")
                                @Expose
                                private String dumeterPreviousVolume="";
                                @SerializedName("dumeter_current_volume")
                                @Expose
                                private String dumeterCurrentVolume="0";

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
                                 * @return The dumeterId
                                 */
                                public String getDumeterId() {
                                        if (TextUtils.isEmpty(dumeterId))
                                                return "";
                                        return dumeterId;
                                }

                                /**
                                 * @param dumeterId The dumeter_id
                                 */
                                public void setDumeterId(String dumeterId) {
                                        this.dumeterId = dumeterId;
                                }

                                /**
                                 * @return The tankId
                                 */
                                public String getTankId() {
                                        return tankId;
                                }

                                /**
                                 * @param tankId The tank_id
                                 */
                                public void setTankId(String tankId) {
                                        this.tankId = tankId;
                                }

                                /**
                                 * @return The openingBalanceD
                                 */
                                public String getOpeningBalanceD() {
                                        if (TextUtils.isEmpty(openingBalanceD))
                                                return "";
                                        return openingBalanceD;
                                }

                                /**
                                 * @param openingBalanceD The opening_balance_d
                                 */
                                public void setOpeningBalanceD(String openingBalanceD) {
                                        this.openingBalanceD = openingBalanceD;
                                }

                                /**
                                 * @return The dumeterName
                                 */
                                public String getDumeterName() {
                                        return dumeterName;
                                }

                                /**
                                 * @param dumeterName The dumeter_name
                                 */
                                public void setDumeterName(String dumeterName) {
                                        this.dumeterName = dumeterName;
                                }

                                /**
                                 * @return The dumeterType
                                 */
                                public String getDumeterType() {
                                        if (TextUtils.isEmpty(dumeterType))
                                                return "";
                                        return dumeterType;
                                }

                                /**
                                 * @param dumeterType The dumeter_type
                                 */
                                public void setDumeterType(String dumeterType) {
                                        this.dumeterType = dumeterType;
                                }

                                /**
                                 * @return The stationId
                                 */
                                public String getStationId() {
                                        return stationId;
                                }

                                /**
                                 * @param stationId The station_id
                                 */
                                public void setStationId(String stationId) {
                                        this.stationId = stationId;
                                }

                                /**
                                 * @return The dumeterAllquantity
                                 */
                                public String getDumeterAllquantity() {
                                        if (TextUtils.isEmpty(dumeterAllquantity))
                                                return "";
                                        return dumeterAllquantity;
                                }

                                /**
                                 * @param dumeterAllquantity The dumeter_allquantity
                                 */
                                public void setDumeterAllquantity(String dumeterAllquantity) {
                                        this.dumeterAllquantity = dumeterAllquantity;
                                }

                                /**
                                 * @return The dispensaryD
                                 */
                                public String getDispensaryD() {
                                        if (TextUtils.isEmpty(dispensaryD))
                                                return "";
                                        return dispensaryD;
                                }

                                /**
                                 * @param dispensaryD The dispensary_d
                                 */
                                public void setDispensaryD(String dispensaryD) {
                                        this.dispensaryD = dispensaryD;
                                }

                                /**
                                 * @return The sapCode
                                 */
                                public String getSapCode() {
                                        if (TextUtils.isEmpty(sapCode))
                                                return "";
                                        return sapCode;
                                }

                                /**
                                 * @param sapCode The sap_code
                                 */
                                public void setSapCode(String sapCode) {
                                        this.sapCode = sapCode;
                                }

                                /**
                                 * @return The dumeterTimezone
                                 */
                                public String getDumeterTimezone() {
                                        if (TextUtils.isEmpty(dumeterTimezone))
                                                return "";
                                        return dumeterTimezone;
                                }

                                /**
                                 * @param dumeterTimezone The dumeter_timezone
                                 */
                                public void setDumeterTimezone(String dumeterTimezone) {
                                        this.dumeterTimezone = dumeterTimezone;
                                }

                                /**
                                 * @return The dumeterProduct
                                 */
                                public String getDumeterProduct() {
                                        return dumeterProduct;
                                }

                                /**
                                 * @param dumeterProduct The dumeter_product
                                 */
                                public void setDumeterProduct(String dumeterProduct) {
                                        this.dumeterProduct = dumeterProduct;
                                }

                                /**
                                 * @return The dumeterAccuracy
                                 */
                                public String getDumeterAccuracy() {
                                        if (TextUtils.isEmpty(dumeterAccuracy))
                                                return "";
                                        return dumeterAccuracy;
                                }

                                /**
                                 * @param dumeterAccuracy The dumeter_accuracy
                                 */
                                public void setDumeterAccuracy(String dumeterAccuracy) {
                                        this.dumeterAccuracy = dumeterAccuracy;
                                }

                                /**
                                 * @return The dumeterDefect
                                 */
                                public String getDumeterDefect() {
                                        if (TextUtils.isEmpty(dumeterDefect))
                                                return "";
                                        return dumeterDefect;
                                }

                                /**
                                 * @param dumeterDefect The dumeter_defect
                                 */
                                public void setDumeterDefect(String dumeterDefect) {
                                        this.dumeterDefect = dumeterDefect;
                                }

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
                                 * @return The specialReading
                                 */
                                public String getSpecialReading() {
                                        if (TextUtils.isEmpty(specialReading))
                                                return "0";
                                        return specialReading;
                                }

                                /**
                                 * @param specialReading The special_reading
                                 */
                                public void setSpecialReading(String specialReading) {
                                        this.specialReading = specialReading;
                                }

                                /**
                                 * @return The dumeterMaxValue
                                 */
                                public String getDumeterMaxValue() {
                                        if (TextUtils.isEmpty(dumeterMaxValue))
                                                return "0";
                                        return dumeterMaxValue;
                                }

                                /**
                                 * @param dumeterMaxValue The dumeter_max_value
                                 */
                                public void setDumeterMaxValue(String dumeterMaxValue) {
                                        this.dumeterMaxValue = dumeterMaxValue;
                                }

                                /**
                                 * @return The openingBal
                                 */
                                public String getOpeningBal() {
                                        return openingBal;
                                }

                                /**
                                 * @param openingBal The opening_bal
                                 */
                                public void setOpeningBal(String openingBal) {
                                        this.openingBal = openingBal;
                                }

                                /**
                                 * @return The dumeterPreviousVolume
                                 */
                                public String getDumeterPreviousVolume() {
                                        if (TextUtils.isEmpty(dumeterPreviousVolume))
                                                return "";
                                        return dumeterPreviousVolume;
                                }

                                /**
                                 * @param dumeterPreviousVolume The dumeter_previous_volume
                                 */
                                public void setDumeterPreviousVolume(String dumeterPreviousVolume) {
                                        this.dumeterPreviousVolume = dumeterPreviousVolume;
                                }
                                /**
                                 *
                                 * @return
                                 * The dumeterCurrentVolume
                                 */
                                public String getDumeterCurrentVolume() {
                                        if (TextUtils.isEmpty(dumeterCurrentVolume))
                                                return "";
                                        return dumeterCurrentVolume;
                                }

                                /**
                                 *
                                 * @param dumeterCurrentVolume
                                 * The dumeter_current_volume
                                 */
                                public void setDumeterCurrentVolume(String dumeterCurrentVolume) {
                                        this.dumeterCurrentVolume = dumeterCurrentVolume;
                                }
                        }



                }


                public class A {

                        @SerializedName("hsd")
                        @Expose
                        private Hsd hsd;
                        @SerializedName("pmg")
                        @Expose
                        private Pmg pmg;
                        @SerializedName("hobc")
                        @Expose
                        private Hobc hobc;

                        /**
                         *
                         * @return
                         * The hsd
                         */
                        public Hsd getHsd() {
                                return hsd;
                        }

                        /**
                         *
                         * @param hsd
                         * The hsd
                         */
                        public void setHsd(Hsd hsd) {
                                this.hsd = hsd;
                        }

                        /**
                         *
                         * @return
                         * The pmg
                         */
                        public Pmg getPmg() {
                                return pmg;
                        }

                        /**
                         *
                         * @param pmg
                         * The pmg
                         */
                        public void setPmg(Pmg pmg) {
                                this.pmg = pmg;
                        }

                        /**
                         *
                         * @return
                         * The hobc
                         */
                        public Hobc getHobc() {
                                return hobc;
                        }

                        /**
                         *
                         * @param hobc
                         * The hobc
                         */
                        public void setHobc(Hobc hobc) {
                                this.hobc = hobc;
                        }


                        public class Hsd {

                                @SerializedName("hsdb")
                                @Expose
                                private Double hsdb;
                                @SerializedName("hsd_du")
                                @Expose
                                private Integer hsdDu;

                                /**
                                 *
                                 * @return
                                 * The hsdb
                                 */
                                public Double getHsdb() {
                                        return hsdb;
                                }

                                /**
                                 *
                                 * @param hsdb
                                 * The hsdb
                                 */
                                public void setHsdb(Double hsdb) {
                                        this.hsdb = hsdb;
                                }

                                /**
                                 *
                                 * @return
                                 * The hsdDu
                                 */
                                public Integer getHsdDu() {
                                        return hsdDu;
                                }

                                /**
                                 *
                                 * @param hsdDu
                                 * The hsd_du
                                 */
                                public void setHsdDu(Integer hsdDu) {
                                        this.hsdDu = hsdDu;
                                }

                        }

                        public class Pmg {

                                @SerializedName("pmgb")
                                @Expose
                                private Double pmgb;
                                @SerializedName("pmg_du")
                                @Expose
                                private Integer pmgDu;

                                /**
                                 *
                                 * @return
                                 * The pmgb
                                 */
                                public Double getPmgb() {
                                        return pmgb;
                                }

                                /**
                                 *
                                 * @param pmgb
                                 * The pmgb
                                 */
                                public void setPmgb(Double pmgb) {
                                        this.pmgb = pmgb;
                                }

                                /**
                                 *
                                 * @return
                                 * The pmgDu
                                 */
                                public Integer getPmgDu() {
                                        return pmgDu;
                                }

                                /**
                                 *
                                 * @param pmgDu
                                 * The pmg_du
                                 */
                                public void setPmgDu(Integer pmgDu) {
                                        this.pmgDu = pmgDu;
                                }

                        }

                        public class Hobc {

                                @SerializedName("hobcb")
                                @Expose
                                private Double hobcb;
                                @SerializedName("hobc_du")
                                @Expose
                                private Integer hobcDu;

                                /**
                                 *
                                 * @return
                                 * The hobcb
                                 */
                                public Double getHobcb() {
                                        return hobcb;
                                }

                                /**
                                 *
                                 * @param hobcb
                                 * The hobcb
                                 */
                                public void setHobcb(Double hobcb) {
                                        this.hobcb = hobcb;
                                }

                                /**
                                 *
                                 * @return
                                 * The hobcDu
                                 */
                                public Integer getHobcDu() {
                                        return hobcDu;
                                }

                                /**
                                 *
                                 * @param hobcDu
                                 * The hobc_du
                                 */
                                public void setHobcDu(Integer hobcDu) {
                                        this.hobcDu = hobcDu;
                                }

                        }


                }


                public class B {

                        @SerializedName("hsd")
                        @Expose
                        private Hsd_ hsd;
                        @SerializedName("pmg")
                        @Expose
                        private Pmg_ pmg;
                        @SerializedName("hobc")
                        @Expose
                        private Hobc_ hobc;

                        /**
                         *
                         * @return
                         * The hsd
                         */
                        public Hsd_ getHsd() {
                                return hsd;
                        }

                        /**
                         *
                         * @param hsd
                         * The hsd
                         */
                        public void setHsd(Hsd_ hsd) {
                                this.hsd = hsd;
                        }

                        /**
                         *
                         * @return
                         * The pmg
                         */
                        public Pmg_ getPmg() {
                                return pmg;
                        }

                        /**
                         *
                         * @param pmg
                         * The pmg
                         */
                        public void setPmg(Pmg_ pmg) {
                                this.pmg = pmg;
                        }

                        /**
                         *
                         * @return
                         * The hobc
                         */
                        public Hobc_ getHobc() {
                                return hobc;
                        }

                        /**
                         *
                         * @param hobc
                         * The hobc
                         */
                        public void setHobc(Hobc_ hobc) {
                                this.hobc = hobc;
                        }


                        public class Hsd_ {

                                @SerializedName("sap_previous_data")
                                @Expose
                                private Integer sapPreviousData;
                                @SerializedName("amount")
                                @Expose
                                private Integer amount;

                                /**
                                 *
                                 * @return
                                 * The sapPreviousData
                                 */
                                public Integer getSapPreviousData() {
                                        return sapPreviousData;
                                }

                                /**
                                 *
                                 * @param sapPreviousData
                                 * The sap_previous_data
                                 */
                                public void setSapPreviousData(Integer sapPreviousData) {
                                        this.sapPreviousData = sapPreviousData;
                                }

                                /**
                                 *
                                 * @return
                                 * The amount
                                 */
                                public Integer getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(Integer amount) {
                                        this.amount = amount;
                                }

                        }

                        public class Pmg_ {

                                @SerializedName("sap_previous_data")
                                @Expose
                                private Integer sapPreviousData;
                                @SerializedName("amount")
                                @Expose
                                private Integer amount;

                                /**
                                 *
                                 * @return
                                 * The sapPreviousData
                                 */
                                public Integer getSapPreviousData() {
                                        return sapPreviousData;
                                }

                                /**
                                 *
                                 * @param sapPreviousData
                                 * The sap_previous_data
                                 */
                                public void setSapPreviousData(Integer sapPreviousData) {
                                        this.sapPreviousData = sapPreviousData;
                                }

                                /**
                                 *
                                 * @return
                                 * The amount
                                 */
                                public Integer getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(Integer amount) {
                                        this.amount = amount;
                                }

                        }

                        public class Hobc_ {

                                @SerializedName("sap_previous_data")
                                @Expose
                                private Integer sapPreviousData;
                                @SerializedName("amount")
                                @Expose
                                private Integer amount;

                                /**
                                 *
                                 * @return
                                 * The sapPreviousData
                                 */
                                public Integer getSapPreviousData() {
                                        return sapPreviousData;
                                }

                                /**
                                 *
                                 * @param sapPreviousData
                                 * The sap_previous_data
                                 */
                                public void setSapPreviousData(Integer sapPreviousData) {
                                        this.sapPreviousData = sapPreviousData;
                                }

                                /**
                                 *
                                 * @return
                                 * The amount
                                 */
                                public Integer getAmount() {
                                        return amount;
                                }

                                /**
                                 *
                                 * @param amount
                                 * The amount
                                 */
                                public void setAmount(Integer amount) {
                                        this.amount = amount;
                                }

                        }
                }

        }
}
