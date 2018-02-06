package com.newinspections.pso.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobiweb on 25/10/16.
 */
public class SapDetailDTO {

    @SerializedName("first")
    @Expose
    private List<First> first = new ArrayList<>();

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
