// BitcoinPriceManager.java
package com.example.finance.utils;

import android.os.Handler;
import android.os.Looper;
import com.example.finance.api.ApiClient;
import com.example.finance.api.BitcoinApi;
import com.example.finance.api.BitcoinPriceResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BitcoinPriceManager {
    private static final long UPDATE_INTERVAL = 60000; // 1 minute
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final BitcoinApi bitcoinApi;
    private BitcoinPriceListener listener;
    private boolean isUpdating = false;

    public interface BitcoinPriceListener {
        void onPriceUpdated(double price);
        void onError(String error);
    }

    public BitcoinPriceManager() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        bitcoinApi = ApiClient.getClient().create(BitcoinApi.class);
    }

    public void startUpdates(BitcoinPriceListener listener) {
        this.listener = listener;
        isUpdating = true;
        scheduleUpdate();
    }

    public void stopUpdates() {
        isUpdating = false;
        mainHandler.removeCallbacksAndMessages(null);
    }

    private void scheduleUpdate() {
        if (!isUpdating) return;

        executorService.execute(() -> {
            fetchBitcoinPrice();
            mainHandler.postDelayed(this::scheduleUpdate, UPDATE_INTERVAL);
        });
    }

    private void fetchBitcoinPrice() {
        bitcoinApi.getBitcoinPrice().enqueue(new Callback<BitcoinPriceResponse>() {
            @Override
            public void onResponse(Call<BitcoinPriceResponse> call, Response<BitcoinPriceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double price = response.body().getBitcoinPrice();
                    mainHandler.post(() -> {
                        if (listener != null) {
                            listener.onPriceUpdated(price);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BitcoinPriceResponse> call, Throwable t) {
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onError("Failed to fetch Bitcoin price: " + t.getMessage());
                    }
                });
            }
        });
    }
}