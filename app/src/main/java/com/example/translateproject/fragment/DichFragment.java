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
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translateproject.R;
import com.example.translateproject.TranslateFM;
import com.example.translateproject.fragment.Dich.DongNghiaFragment;
import com.example.translateproject.fragment.Dich.GoiYFragment;
import com.example.translateproject.fragment.Dich.LoaiTuFragment;
import com.example.translateproject.fragment.Dich.TraiNghiaFragment;
import com.example.translateproject.model.Word;
import com.example.translateproject.model.WordAntonyms;
import com.example.translateproject.model.WordExamples;
import com.example.translateproject.model.WordSynonyms;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.cloud.translate.Translate;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
    FrameLayout loaiTuF, dongNghiaF, traiNghiaF, goiYF;
    TextToSpeech textToSpeech, sp2;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    int f = 0;
    Translate translate;
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
        super.onViewCreated(view, savedInstanceState);

        loaiTuF = getActivity().findViewById(R.id.fragment_Loaitu);
        dongNghiaF = getActivity().findViewById(R.id.fragment_DongNghia);
        traiNghiaF = getActivity().findViewById(R.id.fragment_TraiNghia);
        goiYF = getActivity().findViewById(R.id.fragment_GoiY);
        defaultState();
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
                if(!tvN.getText().toString().equals(""))
                {
                    textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            String text = tvN.getText().toString();
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
        // nút dịch nè....
        btnDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()) {
                    TranslateFM t = new TranslateFM(getContext());
                    tvN.setText(t.translate(editText.getText().toString()));
                    loadAdvFragment();
                } else {

                    //If not, display "no connection" warning:
                    tvN.setText(getResources().getString(R.string.no_connection));
                }
            }
        });


    }

    private void defaultState(){
        loaiTuF.setVisibility(View.INVISIBLE);
        traiNghiaF.setVisibility(View.INVISIBLE);
        dongNghiaF.setVisibility(View.INVISIBLE);
        goiYF.setVisibility(View.INVISIBLE);
    }

    private WordExamples getExamples(String word)
    {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/"+word+"/examples")
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e04ae66d6emshc4e396f30acf6a3p1c6175jsn5905fe697fe9")
                .build();

        try {
            Response response = client.newCall(request).execute();
            WordExamples rs;
            Gson gson = new Gson();
            try {
                String json = response.body().string();
                rs = gson.fromJson(json,WordExamples.class);
                return rs;
            }
            catch (Exception ex)
            {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private WordSynonyms getSynonyms(String word)
    {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/"+word+"/synonyms")
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e04ae66d6emshc4e396f30acf6a3p1c6175jsn5905fe697fe9")
                .build();



        try {
            Response response = client.newCall(request).execute();
            WordSynonyms rs;
            Gson gson = new Gson();
            try {
                String json = response.body().string();
                rs = gson.fromJson(json,WordSynonyms.class);
                return rs;
            }
            catch (Exception ex)
            {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private WordAntonyms getAntonyms(String word)
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/"+word+"/antonyms")
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e04ae66d6emshc4e396f30acf6a3p1c6175jsn5905fe697fe9")
                .build();
        try {
            Response response = client.newCall(request).execute();
            WordAntonyms rs;
            Gson gson = new Gson();
            try {
                String json = response.body().string();
                rs = gson.fromJson(json,WordAntonyms.class);
                return rs;
            }
            catch (Exception ex)
            {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Word getWord(String word)
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/"+word)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e04ae66d6emshc4e396f30acf6a3p1c6175jsn5905fe697fe9")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Word rs;
            Gson gson = new Gson();
            try {
                rs = gson.fromJson(response.body().string(),Word.class);
                return rs;
            }catch (Exception ex)
            {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    private void loadAdvFragment()
    {
        String text = editText.getText().toString();
        Bundle bundle = new Bundle();
        WordSynonyms synonyms = new WordSynonyms();
        synonyms = getSynonyms(text);
        if(synonyms != null)
        {

            bundle.putStringArray("synonyms",synonyms.getSynonyms());
            DongNghiaFragment dnf = new DongNghiaFragment();
            dnf.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_DongNghia,dnf);
            fragmentTransaction.commit();
            dongNghiaF.setVisibility(View.VISIBLE);
        }
        WordAntonyms antonyms = new WordAntonyms();
        antonyms = getAntonyms(text);
        if(antonyms != null)
        {
            bundle.putStringArray("antonyms",antonyms.getAntonyms());
            TraiNghiaFragment dnf = new TraiNghiaFragment();
            dnf.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_TraiNghia,dnf);
            fragmentTransaction.commit();
            traiNghiaF.setVisibility(View.VISIBLE);
        }
        WordExamples wordExamples = new WordExamples();
        wordExamples = getExamples(text);
        if(wordExamples != null)
        {
            bundle.putStringArray("examples",wordExamples.getExamples());
            GoiYFragment dnf = new GoiYFragment();
            dnf.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_GoiY,dnf);
            fragmentTransaction.commit();
            goiYF.setVisibility(View.VISIBLE);
        }
    }
    private void loadFragment(Fragment fragment, int fragment_dongNghia) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragment_dongNghia,fragment);
        fragmentTransaction.commit();
    }
}
