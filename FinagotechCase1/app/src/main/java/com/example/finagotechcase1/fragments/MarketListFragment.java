package com.example.finagotechcase1.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.example.finagotechcase1.R;
import com.example.finagotechcase1.adapters.MarketAdapter;
import com.example.finagotechcase1.models.Market;
import com.example.finagotechcase1.models.Token;
import com.google.gson.Gson;

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
 * Use the {@link MarketListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketListFragment extends Fragment {
    private List<Market> marketList = new ArrayList<>();
    private MarketAdapter adapter;
    private static final String TAG = "MyAppMarketListFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MarketListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketListFragment newInstance(String param1, String param2) {
        MarketListFragment fragment = new MarketListFragment();
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
        View view = inflater.inflate(R.layout.fragment_market_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.marketRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MarketAdapter(getContext(), marketList);
        recyclerView.setAdapter(adapter);
        getMarkets(); //this will also initialize marketList


        return view;
    }
    private void getMarkets(){
        OkHttpClient client = new OkHttpClient();
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
        Request request = new Request.Builder()
                .url("https://bravenewcoin.p.rapidapi.com/market")
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
                        Log.i(TAG,"response successful");
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        JsonArray contentArray = jsonObject.getAsJsonArray("content");
                        Type marketListType = new TypeToken<List<Market>>() {}.getType();
                        List<Market> updatedMarketList = gson.fromJson(contentArray, marketListType);
                        requireActivity().runOnUiThread(() -> {
                            marketList.clear();
                            marketList.addAll(updatedMarketList);
                            adapter.notifyDataSetChanged();
                        });
                        Log.i(TAG,"market list notified");



                    } catch (Exception e){
                        Log.i(TAG,e.toString());
                    }

                } else {
                    // Handle non-successful response
                    Log.i(TAG,"Response failed");
                }
                // Dismiss the loading popup
                progressDialog.dismiss();
                response.close();
            }
        });
    }
}