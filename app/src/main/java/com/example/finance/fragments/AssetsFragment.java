package com.example.finance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finance.R;
import com.example.finance.adapters.AssetAdapter;
import com.example.finance.database.DatabaseHelper;
import com.example.finance.models.Asset;
import com.example.finance.utils.BitcoinPriceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AssetsFragment extends Fragment implements BitcoinPriceManager.BitcoinPriceListener  {
    private RecyclerView recyclerViewAssets;
    public AutoCompleteTextView spinnerAssetType;
    public EditText editTextAmount;
    private Button btnAddAsset;
    public List<Asset> assetList;
    public DatabaseHelper databaseHelper;
    private AssetAdapter adapter;
    private TextView textViewTotalWealth;
    private BitcoinPriceManager bitcoinPriceManager;
    private double currentBitcoinPrice = 66666.67; // Default value

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assets, container, false);

        bitcoinPriceManager = new BitcoinPriceManager();
        databaseHelper = new DatabaseHelper(requireContext());
        initializeViews(view);
        setupAssetTypeDropdown();
        loadAssets();
        updateTotalWealth();
        bitcoinPriceManager.startUpdates(this);

        return view;
    }

    private void initializeViews(View view) {
        textViewTotalWealth = view.findViewById(R.id.textViewTotalWealth);
        // Initialize RecyclerView
        recyclerViewAssets = view.findViewById(R.id.recyclerViewAssets);
        recyclerViewAssets.setLayoutManager(new LinearLayoutManager(requireContext()));
        assetList = new ArrayList<>();
        adapter = new AssetAdapter(assetList);
        recyclerViewAssets.setAdapter(adapter);


        // Initialize other views
        spinnerAssetType = view.findViewById(R.id.spinnerAssetType);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        btnAddAsset = view.findViewById(R.id.btnAddAsset);
        btnAddAsset.setOnClickListener(v -> addNewAsset());
    }

    private void setupAssetTypeDropdown() {
        String[] assetTypes = {"Cash", "Bitcoin", "Property", "Silver", "Palladium", "Gold"};
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_dropdown,
                assetTypes
        );
        spinnerAssetType.setAdapter(dropdownAdapter);
    }

    private void loadAssets() {
        assetList.clear();
        assetList.addAll(databaseHelper.getAllAssets());
        adapter.notifyDataSetChanged();
    }

    private void updateTotalWealth() {

        double totalWealth = calculateTotalWealth();

        String formattedTotal = String.format(Locale.getDefault(),

                "Total Wealth\n$%.2f", totalWealth);

        textViewTotalWealth.setText(formattedTotal);

    }


    private double calculateTotalWealth() {

        double total = 0;

        for (Asset asset : assetList) {

            total += asset.getWorth();

        }

        return total;

    }

    private void addNewAsset() {
        String type = spinnerAssetType.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();

        if (type.isEmpty()) {
            Toast.makeText(requireContext(), "Please select an asset type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            double worth = calculateWorth(type, amount);

            Asset existingAsset = findAssetByType(type);

            if (existingAsset != null) {
                // Update existing asset
                double newAmount = existingAsset.getAmount() + amount;
                existingAsset.setAmount(newAmount);
                existingAsset.setWorth(calculateWorth(type, newAmount));
                databaseHelper.addOrUpdateAsset(existingAsset);
            } else {
                // Create new asset
                Asset newAsset = new Asset(type, amount, worth);
                databaseHelper.addOrUpdateAsset(newAsset);
            }

            loadAssets();
            updateTotalWealth();
            clearInputs();
            Toast.makeText(requireContext(), "Asset added successfully", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private Asset findAssetByType(String type) {
        for (Asset asset : assetList) {
            if (asset.getType().equals(type)) {
                return asset;
            }
        }
        return null;
    }

    private double calculateWorth(String type, double amount) {
        switch (type) {
            case "Cash":
                return amount;
            case "Bitcoin":
                return amount * currentBitcoinPrice;
            case "Gold":
                return amount * 2631.58;
            case "Silver":
                return amount * 25.0;
            case "Palladium":
                return amount * 1000.0;
            case "Property":
                return amount * 100000.0;
            default:
                return amount;
        }
    }

    @Override
    public void onPriceUpdated(double price) {

        currentBitcoinPrice = price;

        // Update any existing Bitcoin assets

        boolean hasUpdates = false;

        for (Asset asset : assetList) {

            if ("Bitcoin".equals(asset.getType())) {

                double newWorth = asset.getAmount() * currentBitcoinPrice;

                if (Math.abs(newWorth - asset.getWorth()) > 0.01) {

                    asset.setWorth(newWorth);

                    databaseHelper.addOrUpdateAsset(asset);

                    hasUpdates = true;

                }

            }

        }

        if (hasUpdates) {

            requireActivity().runOnUiThread(() -> {

                adapter.notifyDataSetChanged();

                updateTotalWealth();

            });

        }

    }

    @Override
    public void onError(String error) {

        requireActivity().runOnUiThread(() ->

                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

        );

    }

    private void clearInputs() {
        spinnerAssetType.setText("");
        editTextAmount.setText("");
        spinnerAssetType.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        bitcoinPriceManager.startUpdates((BitcoinPriceManager.BitcoinPriceListener) this);
        loadAssets();
        updateTotalWealth();
    }

}