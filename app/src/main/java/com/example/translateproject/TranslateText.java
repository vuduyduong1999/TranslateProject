package com.example.translateproject;
//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.v3.LocationName;
//import com.google.cloud.translate.v3.TranslateTextRequest;
//import com.google.cloud.translate.v3.TranslateTextResponse;
//import com.google.cloud.translate.v3.Translation;
//import com.google.cloud.translate.v3.TranslationServiceClient;
//import java.io.IOException;
import java.io.IOException;
import java.util.Locale;

public class TranslateText {
//    public TranslateText(){
//
//    }
//    public static String translateText(String input)throws IOException
//    {
//        String project_id = "hihidichduocroine";
//        String targerLanguage = "vi-VN";
//        String text = input;
//        return translateText(project_id,targerLanguage,text);
//    }
//    public static String translateText(String projectId, String targetLanguage, String text)
//            throws IOException {
//
//        // Initialize client that will be used to send requests. This client only needs to be created
//        // once, and can be reused for multiple requests. After completing all of your requests, call
//        // the "close" method on the client to safely clean up any remaining background resources.
//        try (TranslationServiceClient client = TranslationServiceClient.create()) {
//            // Supported Locations: `global`, [glossary location], or [model location]
//            // Glossaries must be hosted in `us-central1`
//            // Custom Models must use the same location as your model. (us-central1)
//            LocationName parent = LocationName.of(projectId, "global");
//
//            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
//            TranslateTextRequest request =
//                    TranslateTextRequest.newBuilder()
//                            .setParent(parent.toString())
//                            .setMimeType("text/plain")
//                            .setTargetLanguageCode(targetLanguage)
//                            .addContents(text)
//                            .build();
//
//            TranslateTextResponse response = client.translateText(request);
//
//            // Display the translation for each input text provided
//            for (Translation translation : response.getTranslationsList()) {
//                 return translation.getTranslatedText();
//            }
//        }
//        return null;
//    }


}
