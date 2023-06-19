package com.example.finagotechcase1.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finagotechcase1.R;
import com.example.finagotechcase1.adapters.AssetAdapter;
import com.example.finagotechcase1.models.Asset;
import com.example.finagotechcase1.models.Market;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetListFragment extends Fragment {
    private List<Asset> assetList = new ArrayList<>();
    private AssetAdapter adapter;
    private static final String TAG = "MyAppAssetFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssetListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssetListFragment newInstance(String param1, String param2) {
        AssetListFragment fragment = new AssetListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_asset_list, container, false);
        getAssets(); //this will also initialize assetList
        RecyclerView recyclerView = view.findViewById(R.id.assetRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AssetAdapter(getContext(), assetList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(asset -> {
            showPopupPage(asset);
        });

        return view;
    }
    private void getAssets(){
        OkHttpClient client = new OkHttpClient();
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        Request request = new Request.Builder()
                .url("https://bravenewcoin.p.rapidapi.com/asset?status=ACTIVE")
                .get()
                .addHeader("X-RapidAPI-Key", "42d53e9ea6msh0f4a94084a37feap147d55jsn9e5da01827a7")
                .addHeader("X-RapidAPI-Host", "bravenewcoin.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success
                if (response.isSuccessful()) {
                    try{
                        String responseBody = response.body().string();
                        Log.i(TAG,"response is successful");
                        //here initialize the asset list from the responsebody
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        JsonArray contentArray = jsonObject.getAsJsonArray("content");
                        Type assetListType = new TypeToken<List<Asset>>() {}.getType();
                        List<Asset> updatedAssetList = gson.fromJson(contentArray, assetListType);
                        requireActivity().runOnUiThread(() -> {
                            assetList.clear();
                            assetList.addAll(updatedAssetList);
                            adapter.notifyDataSetChanged();
                        });
                        Log.i(TAG,"asset list notified");
                    } catch (Exception e){
                        Log.i(TAG,"Couldnt convert it to market object");
                    }


                    // Process the token from the response body
                    // For example, you can parse the JSON response and extract the token
                } else {
                    // Handle non-successful response
                    Log.i(TAG,"Response failed");
                }
                progressDialog.dismiss();
                response.close();
            }
        });
    }
    private void showPopupPage(Asset asset) {
        // Inflate the popup_page layout
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_page, null);

        // Customize the views in the popup page based on the clicked asset

        TextView assetIDTextView = popupView.findViewById(R.id.assetIDTextView);
        TextView assetStatusTextView = popupView.findViewById(R.id.assetStatusTextView);
        TextView assetSymbolTextView = popupView.findViewById(R.id.assetSymbolTextView);
        TextView assetTypeTextView = popupView.findViewById(R.id.assetTypeTextView);
        TextView assetNameTextView = popupView.findViewById(R.id.assetNameTextView);
        TextView assetURLTextView = popupView.findViewById(R.id.assetURLTextView);
        Button closeButton = popupView.findViewById(R.id.closeButton);

        assetIDTextView.setText("Asset id: "+asset.getId());
        assetURLTextView.setText("Asset link: "+asset.getUrl());
        assetNameTextView.setText("Asset name: "+asset.getName());
        assetStatusTextView.setText("Asset status: "+asset.getStatus());
        assetTypeTextView.setText("Asset type: "+asset.getType());
        assetSymbolTextView.setText("Asset symbol: "+asset.getSymbol());
        // Get the screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Create a PopupWindow and set its content view
        PopupWindow popupWindow = new PopupWindow(popupView, screenWidth, screenHeight, true);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the popup window
                popupWindow.dismiss();
            }
        });
        // Configure the PopupWindow properties (e.g., animation, focusability, outside touch, etc.)
        // For example:
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        // Show the PopupWindow at a desired location on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
}