package com.suspecious.chatmate.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonitoringModel {
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("status_code")
        @Expose
        private String statusCode;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

    }
