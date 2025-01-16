// BitcoinPriceResponse.java
package com.example.finance.api;

import com.google.gson.annotations.SerializedName;

public class BitcoinPriceResponse {
    @SerializedName("bitcoin")
    private Bitcoin bitcoin;

    public static class Bitcoin {
        @SerializedName("usd")
        private double usd;

        public double getUsd() {
            return usd;
        }
    }

    public double getBitcoinPrice() {
        return bitcoin != null ? bitcoin.usd : 0;
    }
}