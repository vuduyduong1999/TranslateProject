package com.example.translateproject;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateException;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;

public class TranslateFM {
    Context context;
    Translate translate;

    public TranslateFM(Context context) {
        this.context = context;
    }

    private void getTranslateService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try (InputStream is = context.getResources().openRawResource(R.raw.app_android_translate_0477bd9fee94)){
            final GoogleCredentials mycredentials = GoogleCredentials.fromStream(is);

            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(mycredentials).build();
            translate = translateOptions.getService();

        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public String translate(String originalText) {
        try {
            getTranslateService();
            Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("vi"),Translate.TranslateOption.model("base"));
            return translation.getTranslatedText();
        }
        catch (TranslateException ex)
        {
            Log.e("errTranslate",ex.getMessage());
            return "";
        }

    }
}
