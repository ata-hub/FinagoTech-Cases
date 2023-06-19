package com.example.finagotechcase1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finagotechcase1.R;
import com.example.finagotechcase1.models.Token;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Button getTokenBtn;
    TextView displayToken;
    private static final String TAG = "MyAppHome";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getTokenBtn = view.findViewById(R.id.getTokenButton);
        displayToken = view.findViewById(R.id.displayToken);
        getTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send post api
                try{
                    getToken();
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }
    private void getToken() {
        Log.i(TAG, "function called");
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                    "{\r\"audience\": \"https://api.bravenewcoin.com\",\r\"client_id\": \"oCdQoZoI96ERE9HY3sQ7JmbACfBf55RY\",\r\"grant_type\": \"client_credentials\"\r}");
            Request request = new Request.Builder()
                    .url("https://bravenewcoin.p.rapidapi.com/oauth/token")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("X-RapidAPI-Key", "3488e03c2fmsh18fdb8b66bfc00fp16b1c9jsnbde48da3d259")
                    .addHeader("X-RapidAPI-Host", "bravenewcoin.p.rapidapi.com")
                    .build();
            Log.i(TAG, "request created");
            Log.i(TAG, String.valueOf(request.headers()));
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle request failure
                    e.printStackTrace();
                    Log.e(TAG,"response on failure");
                    Log.i(TAG,"response on failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Handle request success
                    if (response.isSuccessful()) {
                        Log.i(TAG, "response is successful");
                        try {
                            String responseBody = response.body().string();
                            Token token = Token.fromJson(responseBody);
                            displayToken.setText(token.getAccess_token());
                        } catch (Exception e) {
                            Log.e(TAG,e.toString());
                        }

                        // Process the token from the response body
                        // For example, you can parse the JSON response and extract the token
                    } else {
                        // Handle non-successful response
                        int responseCode = response.code();
                        String responseBody = response.body().string();
                        Log.i(TAG, "response failed with code: " + responseCode);
                        Log.i(TAG, "response body: " + responseBody);
                    }
                    response.close();
                }
            });


        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }
}