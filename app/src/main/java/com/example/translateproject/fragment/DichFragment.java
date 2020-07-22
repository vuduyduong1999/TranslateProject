package com.example.translateproject.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translateproject.R;
//import com.example.translateproject.TranslateText;
import com.example.translateproject.fragment.Dich.DongNghiaFragment;
import com.example.translateproject.fragment.Dich.GoiYFragment;
import com.example.translateproject.fragment.Dich.LoaiTuFragment;
import com.example.translateproject.fragment.Dich.TraiNghiaFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.v3.Translation;
//import com.google.cloud.translate.TranslateOptions;
//import com.google.cloud.translate.Translation;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
//import com.google.api.services.translate.Translate;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.TranslateOptions;
//import com.google.cloud.translate.Translation;

//import java.io.IOException;
//import java.io.InputStream;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

/**
 * A simple {@link Fragment} subclass.
 */
public class DichFragment extends Fragment {
    Button btnDich;
    TextView tvN;
    EditText editText;
    ImageButton IBinputVoice,IBSpeaker, ibCamera,ibSpeakerDifi;
    private String originalText;
    private String translatedText;
    private boolean connected;
    final int REQ_CODE = 100;
    TextToSpeech textToSpeech, sp2;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    int f = 0;
//    Translate translate;
    public DichFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dich, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //

        //
        super.onViewCreated(view, savedInstanceState);
        Fragment fragment = new LoaiTuFragment();
        loadFragment(fragment,R.id.fragment_Loaitu);
        fragment = new DongNghiaFragment();
        loadFragment(fragment,R.id.fragment_DongNghia);
        fragment = new TraiNghiaFragment();
        loadFragment(fragment,R.id.fragment_TraiNghia);
        fragment = new GoiYFragment();
        loadFragment(fragment,R.id.fragment_GoiY);
        IBinputVoice = getActivity().findViewById(R.id.ib_SpeechToText);
        btnDich = getActivity().findViewById(R.id.btnDich);
        tvN = getActivity().findViewById(R.id.tvDifin);
        editText = getActivity().findViewById(R.id.etInputText);
        IBSpeaker = getActivity().findViewById(R.id.ib_speaker);
        ibCamera = getActivity().findViewById(R.id.ib_camera);
        ibSpeakerDifi = getActivity().findViewById(R.id.ib_speakerDifi);
        //cài chức năng vào các view
        IBSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals(""))
                {
                    sp2 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            String text = editText.getText().toString();
                            if(status == TextToSpeech.SUCCESS)
                                sp2.setLanguage(Locale.ENGLISH);
                                sp2.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                                sp2.setPitch(1);
                        }
                    });
                }
            }
        });
        IBinputVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVoice();
            }
        });
        ibSpeakerDifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals(""))
                {
                    textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            String text = editText.getText().toString();
                            if(status == TextToSpeech.SUCCESS)
                                textToSpeech.setLanguage(Locale.forLanguageTag("vi-VN"));
                            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                        }
                    });
                }
            }
        });
        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });
        //
        btnDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkInternetConnection()) {
////                    getTranslateService();
////                    translate();
//                } else {
//
//                    //If not, display "no connection" warning:
//                    tvN.setText(getResources().getString(R.string.no_connection));
//                }
            }
        });


    }

    private void detectText() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result = detector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTextRecognitionResult(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Không thể đọc được chữ bên trong hình...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if(blocks.size()==0)
        {
            Toast.makeText(getContext(),"Không tìm thấy chữ trong hình ...", Toast.LENGTH_SHORT).show();
            return;
        }
        editText.setText("");
        for(int i = 0; i < blocks.size(); i++)
        {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for(int j = 0; j < lines.size();j++)
            {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for(int k = 0; k < elements.size(); k++)
                {
                    editText.append(elements.get(k).getText().toString());
                    editText.append(" ");
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void getVoice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speech...");
        try {
            startActivityForResult(intent, 100);
        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(getContext(),"Sorry your device not supported..",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 100:
                if(resultCode == -1 && data != null)
                {
                    ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0).toString());
                    break;
                }
            case REQUEST_IMAGE_CAPTURE:
            {
                if(resultCode == -1)
                {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    detectText();
                    break;
                }
            }
        }
    }

    private void getTranslateService() {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        try (InputStream is = getResources().openRawResource(R.raw.hihidichduocroine_211199)){
//            final GoogleCredentials mycredentials = GoogleCredentials.fromStream(is);
//
//            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(mycredentials).build();
//            translate = translateOptions.getService();
//
//        }
//        catch (IOException ioe)
//        {
//            ioe.printStackTrace();
//        }
    }

    private void translate() {
//        originalText = editText.getText().toString();
//        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("vn"),Translate.TranslateOption.model("base"));
//        translatedText = translation.getTranslatedText();
//
//        tvN.setText(translatedText);
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    private void loadFragment(Fragment fragment, int fragment_dongNghia) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragment_dongNghia,fragment);
        fragmentTransaction.commit();
    }
}
