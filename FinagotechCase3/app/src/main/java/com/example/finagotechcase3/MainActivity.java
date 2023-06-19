package com.example.finagotechcase3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputEditText;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.android.material.textfield.TextInputLayout;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


public class MainActivity extends AppCompatActivity {
    TextInputEditText outputBox;
    public static final MediaType mediaType
            = MediaType.get("application/json; charset=utf-8");
    private ProgressDialog progressDialog;
    TextInputEditText inputWordText;

    TextInputLayout inputWordTextLayout;
    TextInputLayout languageInputDropdownLayout;
    TextInputLayout languageOutputDropdownLayout;

    Button translateBtn;
    String inputLanguageCode;
    String outputLanguageCode;
    String serviceAccountKeyPath = "finagotech-translate-38b7de063840.json";

    private AssetManager assetManager;
    private InputStream inputStream;
    private GoogleCredentials credentials;
    AutoCompleteTextView languageDropdownInput;
    AutoCompleteTextView languageDropdownOutput;
    HashMap<String, String> languageCodes = new HashMap<>();
    private static final String TAG = "TranslationApp";
    ArrayList<String> languages = new ArrayList<>(Arrays.asList(
            "Afrikaans", "Amharic", "Arabic", "Assamese", "Azerbaijani", "Bulgarian", "Bengali", "Bosnian", "Catalan", "Czech",
            "Welsh", "Danish", "German", "Greek", "English", "Spanish", "Estonian", "Basque", "Persian", "Finnish",
            "Filipino", "Fijian", "French", "Irish", "Gujarati", "Hebrew", "Hindi", "Croatian", "Haitian Creole", "Hungarian",
            "Armenian", "Indonesian", "Icelandic", "Italian", "Inuktitut", "Japanese", "Georgian", "Kazakh", "Khmer", "Kannada",
            "Korean", "Kurdish", "Kyrgyz", "Lao", "Lithuanian", "Latvian", "Malagasy", "Maori", "Malayalam", "Marathi",
            "Malay", "Maltese", "Hmong Daw", "Burmese", "Norwegian", "Nepali", "Dutch", "Norwegian", "Chichewa", "Odia",
            "Zulu", "Panjabi", "Polish", "Pashto", "Portuguese", "Romanian", "Russian", "Kinyarwanda", "Sindhi",
            "Sinhala", "Slovak", "Slovenian", "Samoan", "Shona", "Somali", "Albanian", "Serbian", "Serbian (Cyrillic)",
            "Serbian (Latin)", "Swedish", "Swahili", "Tamil", "Telugu", "Tajik", "Thai", "Turkmen", "Tagalog",
            "Klingon", "Tongan", "Turkish", "Tahitian", "Uighur", "Ukrainian", "Urdu", "Uzbek", "Vietnamese", "Xhosa",
            "Yiddish", "Yoruba", "Yucatec Maya", "Cantonese", "Chinese (Simplified)", "Chinese (Traditional)", "Zulu"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = MainActivity.this.getAssets();
        inputStream = null;
        try {
            inputStream = assetManager.open(serviceAccountKeyPath);
            credentials = GoogleCredentials.fromStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Create GoogleCredentials from the JSON file

        initializeHashMap();
        initializeDropDownMenus();
        outputBox = findViewById(R.id.outputBox);
        inputWordText = findViewById(R.id.inputWord);
        inputWordTextLayout = findViewById(R.id.inputWordLayout);
        translateBtn = findViewById(R.id.translateButton);
        languageDropdownInput = findViewById(R.id.languageDropdownInput);
        languageDropdownOutput = findViewById(R.id.languageDropdownOutput);
        languageInputDropdownLayout = findViewById(R.id.languageInputLayout);
        languageOutputDropdownLayout = findViewById(R.id.languageOutputLayout);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Translating...");
        progressDialog.setCancelable(false);
        // Perform translation






        translateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(languageDropdownInput.getText().toString().length()==0){
                    languageInputDropdownLayout.setErrorEnabled(true);
                    languageInputDropdownLayout.setError("Choose a language");
                }
                else if (languageDropdownOutput.getText().toString().length()==0){
                    //disable the error of input
                    languageInputDropdownLayout.setErrorEnabled(false);
                    languageOutputDropdownLayout.setErrorEnabled(true);
                    languageOutputDropdownLayout.setError("Choose a language");
                }
                else{
                    languageInputDropdownLayout.setErrorEnabled(false);
                    languageOutputDropdownLayout.setErrorEnabled(false);
                    inputLanguageCode=languageCodes.get(languageDropdownInput.getText().toString());
                    outputLanguageCode=languageCodes.get(languageDropdownOutput.getText().toString());
                    String inputWord = inputWordText.getText().toString();
                    if(inputWord.length()!=0){
                        translate(inputWord,inputLanguageCode,outputLanguageCode);
                    }
                    else {
                        inputWordTextLayout.setError("This field can't be empty");
                    }
                }
                //initialize here inputLanguage and outputlanguage from dropdownmenu



            }

        });
    }
    public void translate(String inputWord, String sourceLanguageCode, String targetLanguageCode){

        TranslateTask translateTask = new TranslateTask(inputWord, sourceLanguageCode, targetLanguageCode);
        translateTask.execute();



    }
    public void initializeHashMap(){
        languageCodes.put("Afrikaans", "af");
        languageCodes.put("Amharic", "am");
        languageCodes.put("Arabic", "ar");
        languageCodes.put("Assamese", "as");
        languageCodes.put("Azerbaijani", "az");
        languageCodes.put("Bulgarian", "bg");
        languageCodes.put("Bengali", "bn");
        languageCodes.put("Bosnian", "bs");
        languageCodes.put("Catalan", "ca");
        languageCodes.put("Czech", "cs");
        languageCodes.put("Welsh", "cy");
        languageCodes.put("Danish", "da");
        languageCodes.put("German", "de");
        languageCodes.put("Greek", "el");
        languageCodes.put("English", "en");
        languageCodes.put("Spanish", "es");
        languageCodes.put("Estonian", "et");
        languageCodes.put("Basque", "eu");
        languageCodes.put("Persian", "fa");
        languageCodes.put("Finnish", "fi");
        languageCodes.put("Filipino", "fil");
        languageCodes.put("Fijian", "fj");
        languageCodes.put("French", "fr");
        languageCodes.put("Irish", "ga");
        languageCodes.put("Gujarati", "gu");
        languageCodes.put("Hebrew", "he");
        languageCodes.put("Hindi", "hi");
        languageCodes.put("Croatian", "hr");
        languageCodes.put("Haitian Creole", "ht");
        languageCodes.put("Hungarian", "hu");
        languageCodes.put("Armenian", "hy");
        languageCodes.put("Indonesian", "id");
        languageCodes.put("Icelandic", "is");
        languageCodes.put("Italian", "it");
        languageCodes.put("Inuktitut", "iu");
        languageCodes.put("Japanese", "ja");
        languageCodes.put("Georgian", "ka");
        languageCodes.put("Kazakh", "kk");
        languageCodes.put("Khmer", "km");
        languageCodes.put("Kannada", "kn");
        languageCodes.put("Korean", "ko");
        languageCodes.put("Kurdish", "ku");
        languageCodes.put("Kyrgyz", "ky");
        languageCodes.put("Lao", "lo");
        languageCodes.put("Lithuanian", "lt");
        languageCodes.put("Latvian", "lv");
        languageCodes.put("Malagasy", "mg");
        languageCodes.put("Maori", "mi");
        languageCodes.put("Malayalam", "ml");
        languageCodes.put("Marathi", "mr");
        languageCodes.put("Malay", "ms");
        languageCodes.put("Maltese", "mt");
        languageCodes.put("Hmong Daw", "mww");
        languageCodes.put("Burmese", "my");
        languageCodes.put("Norwegian", "no");
        languageCodes.put("Nepali", "ne");
        languageCodes.put("Dutch", "nl");
        languageCodes.put("Chichewa", "ny");
        languageCodes.put("Odia", "or");
        languageCodes.put("Zulu", "zu");
        languageCodes.put("Panjabi", "pa");
        languageCodes.put("Polish", "pl");
        languageCodes.put("Pashto", "ps");
        languageCodes.put("Portuguese", "pt");
        languageCodes.put("Romanian", "ro");
        languageCodes.put("Russian", "ru");
        languageCodes.put("Kinyarwanda", "rw");
        languageCodes.put("Sindhi", "sd");
        languageCodes.put("Sinhala", "si");
        languageCodes.put("Slovak", "sk");
        languageCodes.put("Slovenian", "sl");
        languageCodes.put("Samoan", "sm");
        languageCodes.put("Shona", "sn");
        languageCodes.put("Somali", "so");
        languageCodes.put("Albanian", "sq");
        languageCodes.put("Serbian", "sr");
        languageCodes.put("Serbian (Cyrillic)", "sr-Cyrl");
        languageCodes.put("Serbian (Latin)", "sr-Latn");
        languageCodes.put("Swedish", "sv");
        languageCodes.put("Swahili", "sw");
        languageCodes.put("Tamil", "ta");
        languageCodes.put("Telugu", "te");
        languageCodes.put("Tajik", "tg");
        languageCodes.put("Thai", "th");
        languageCodes.put("Turkmen", "tk");
        languageCodes.put("Tagalog", "tl");
        languageCodes.put("Klingon", "tlh");
        languageCodes.put("Tongan", "to");
        languageCodes.put("Turkish", "tr");
        languageCodes.put("Tahitian", "ty");
        languageCodes.put("Uighur", "ug");
        languageCodes.put("Ukrainian", "uk");
        languageCodes.put("Urdu", "ur");
        languageCodes.put("Uzbek", "uz");
        languageCodes.put("Vietnamese", "vi");
        languageCodes.put("Xhosa", "xh");
        languageCodes.put("Yiddish", "yi");
        languageCodes.put("Yoruba", "yo");
        languageCodes.put("Yucatec Maya", "yua");
        languageCodes.put("Cantonese", "yue");
        languageCodes.put("Chinese (Simplified)", "zh-CN");
        languageCodes.put("Chinese (Traditional)", "zh-TW");
        languageCodes.put("Zulu", "zu");
    }
    public void initializeDropDownMenus(){
        Collections.sort(languages);
        AutoCompleteTextView languageDropdownInput = findViewById(R.id.languageDropdownInput);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, languages);

        // Set the adapter to the AutoCompleteTextView
        languageDropdownInput.setAdapter(adapter);
        AutoCompleteTextView languageDropdownOutput = findViewById(R.id.languageDropdownOutput);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, languages);

        // Set the adapter to the AutoCompleteTextView
        languageDropdownOutput.setAdapter(adapter2);
    }
    private class TranslateTask extends AsyncTask<Void, Void, String> {
        private String inputWord;
        private String sourceLanguageCode;
        private String targetLanguageCode;

        public TranslateTask(String inputWord, String sourceLanguageCode, String targetLanguageCode) {
            this.inputWord = inputWord;
            this.sourceLanguageCode = sourceLanguageCode;
            this.targetLanguageCode = targetLanguageCode;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.show(); // Show the progress dialog before translation starts
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Get the AssetManager



                // Path to the JSON file in the assets folder


                // Open the JSON file from the assets folder


                // Build Translate service with the credentials
                Translate translate = TranslateOptions.newBuilder()
                        .setCredentials(credentials)
                        .build()
                        .getService();

                // Perform translation
                Log.i(TAG,"translation starting");
                Translation translation = translate.translate(inputWord, Translate.TranslateOption.sourceLanguage(sourceLanguageCode), Translate.TranslateOption.targetLanguage(targetLanguageCode));
                String translatedText = translation.getTranslatedText();
                Log.i(TAG,"translation completed "+translatedText);
                // Close the input stream
                inputStream.close();

                return translatedText;
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String translatedText) {
            progressDialog.dismiss();
            if (translatedText != null) {
                outputBox.setText(translatedText);
            }
        }
    }
}



