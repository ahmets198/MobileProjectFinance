// BitcoinApi.java
package com.example.finance.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BitcoinApi {
    @GET("simple/price?ids=bitcoin&vs_currencies=usd")
    Call<BitcoinPriceResponse> getBitcoinPrice();
}