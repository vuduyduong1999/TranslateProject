package com.example.translateproject.fragment;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translateproject.Adapter.DongNghiaAdapter;
import com.example.translateproject.Adapter.ExpandableListViewAdapter;
import com.example.translateproject.Adapter.LichSuAdapter;
import com.example.translateproject.Adapter.LoaiTuAdapter;
import com.example.translateproject.R;
import com.example.translateproject.TranslateFM;
import com.example.translateproject.model.Definition;
import com.example.translateproject.model.Word;
import com.example.translateproject.model.WordAntonyms;
import com.example.translateproject.model.WordDefinitions;
import com.example.translateproject.model.WordExamples;
import com.example.translateproject.model.WordHistory;
import com.example.translateproject.model.WordSynonyms;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.cloud.translate.Translate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.gson.Gson;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
    ExpandableListView expandableListView;

    TextToSpeech textToSpeech, sp2;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    int f = 0;
    Translate translate;
    Bundle bundle;

    DatabaseReference reff,nwfb;
    WordHistory wordHistory;
    long countHistory = 0;
    boolean flag = true;
    int indx = 1;
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

        //Firebase data
        nwfb = FirebaseDatabase.getInstance().getReference().child("WordAdded");
        reff = FirebaseDatabase.getInstance().getReference().child("WordHistory");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    countHistory = (snapshot.getChildrenCount());
                    if (countHistory >= 10) {
                        flag = false;
                        final ArrayList<WordHistory> list = new ArrayList<WordHistory>();
                        for (int i = 1; i <= countHistory; i++) {
                            DatabaseReference rmn = reff.child(String.valueOf(i));
                            final int finalI = i;
                            rmn.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String w = snapshot.child("word").getValue().toString();
                                        String c = snapshot.child("content").getValue().toString();
                                        String t = snapshot.child("time").getValue().toString();
                                        long id = Long.valueOf(snapshot.child("numberID").getValue().toString());
                                        WordHistory n = new WordHistory(w, c, t, id);
                                        list.add(n);
                                    }
                                    if (finalI == countHistory) {
                                        WordHistory h1 = list.get(0);
                                        for (WordHistory it : list) {
                                            if (h1.getNumberID() > it.getNumberID()) {
                                                h1 = it;
                                                indx = list.indexOf(it) + 1;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } else
                        flag = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //------------

        expandableListView = getActivity().findViewById(R.id.expandableListView);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(expandableListView, groupPosition);
                return false;
            }
        });

        IBinputVoice = getActivity().findViewById(R.id.ib_SpeechToText);
        btnDich = getActivity().findViewById(R.id.btnDich);
        tvN = getActivity().findViewById(R.id.tvDifin);
        editText = getActivity().findViewById(R.id.etInputText);
        IBSpeaker = getActivity().findViewById(R.id.ib_speaker);
        ibCamera = getActivity().findViewById(R.id.ib_camera);
        ibSpeakerDifi = getActivity().findViewById(R.id.ib_speakerDifi);
        //-------------
        bundle = getArguments();
        if(bundle != null)
        {
            editText.setText(bundle.getString("word"));
            TranslateMethod();
        }

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
        // Xuli su kien btnDich-------------------
        btnDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateMethod();

            }
        });


    }
    private  void defaultSate()
    {

    }

    private void TranslateMethod() {
        expandableListView.setAdapter(new ExpandableListViewAdapter(getContext(),new ArrayList<String>(),new ArrayList<ListView>()));
        hideKeyboardFrom(getContext(), getView());
        if (checkInternetConnection()) {
            String path = editText.getText().toString();
            DatabaseReference searchW = nwfb.child(path.replaceAll("\\s+",""));
            searchW.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        loadWord(snapshot);
                    }
                    else
                    {

                        TranslateFM t = new TranslateFM(getContext());
                        String tu = editText.getText().toString().trim();
                        String dich = t.translate(tu);
                        tvN.setText(dich);
                        if(checkNotSpaceInText(tu))
                        {
                            Word or = new Word();
                            try {
                                or = makeWordFromAPI(tu,dich);
                                loadExpandanbleListView(or);
                            }
                            catch (Exception ex){}
                        }

                        addHistorySearch(editText.getText().toString(),flag);
                        Toast.makeText(getContext(), "Dịch thành công", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            //If not, display "no connection" warning:
            tvN.setText(getResources().getString(R.string.no_connection));
        }
    }

    private boolean checkNotSpaceInText(String tu) {
        for (int i = 0; i < tu.length(); i++) {
            if(Character.isSpaceChar(tu.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    private Word makeWordFromAPI(String tu,String nghia) {
        WordDefinitions dfs = getDefinition(tu);
        WordSynonyms dns = getSynonyms(tu);
        if(dns == null) {
            dns = new WordSynonyms();
            dns.setSynonyms(new String[0]);
        }
        WordAntonyms tns = getAntonyms(tu);
        if(tns == null) {
            tns = new WordAntonyms();
            tns.setAntonyms(new String[0]);
        }
        WordExamples vds = getExamples(tu);
        if(vds == null) {
            vds = new WordExamples();
            vds.setExamples(new String[0]);
        }
        Word rs = new Word(tu,nghia,dfs.getDefinitions(),dns.getSynonyms(),tns.getAntonyms(),vds.getExamples());
        return rs;
    }

    //-----expandablelistview---
    private void setListViewHeight(final ExpandableListView listView, final int groupPosition) {
        final ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        // be careful with unspecified width measure spec, it will ignore all layout params and even
        // screen dimensions, so to get correct height, first get maximum width View can use and
        // call measure() this way
        final int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            final View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();
            // count only expanded other groups or the clicked collapsed one
            if (((listView.isGroupExpanded(i)) && (i != groupPosition))
                    || ((!listView.isGroupExpanded(i)) && (i == groupPosition))) {
                for (int j = 0, childrenNumber = listAdapter.getChildrenCount(i); j < childrenNumber; j++) {

                    final View listItem = listAdapter.getChildView(i, j, j == childrenNumber - 1, null, listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }
        final ViewGroup.LayoutParams params = listView.getLayoutParams();
        final int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        params.height = height + 50;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    //-------------
    private void loadWord(DataSnapshot snapshot) {
        Word rs = snapshot.getValue(Word.class);
        tvN.setText(rs.getDefinition());
        try {
            loadExpandanbleListView(rs);
        }
        catch (Exception ex){}

        addHistorySearch(rs.getWordn(),flag);
    }

    private void loadExpandanbleListView(Word w) {
        ArrayList<String> title = new ArrayList<String>();
        ArrayList<ListView> detail = new ArrayList<ListView>();

        String tt = "Nghĩa của từ";
        ListView nct = new ListView(getContext());
        ArrayList<Definition> arrlt = new ArrayList<Definition>();
        for (Definition it:w.getDefinitions()) {
            arrlt.add(it);
        }
        LoaiTuAdapter loaiTuAdapter = new LoaiTuAdapter(getContext(),arrlt);
        nct.setAdapter(loaiTuAdapter);

        title.add(tt);
        detail.add(nct);

        if(w.getSynonyms().size() != 0)
        {
            if(w.getSynonyms().size()==1 && w.getSynonyms().get(0).equals(""))
            {

            }
            else
            {
                String dn = "Đồng nghĩa";
                ListView dnl = new ListView(getContext());
                ArrayList<String> arrdn = new ArrayList<String>();
                for (String it:w.getSynonyms()) {
                    arrdn.add(it);
                }
                DongNghiaAdapter dongNghiaAdapter = new DongNghiaAdapter(arrdn,getContext());
                dnl.setAdapter(dongNghiaAdapter);

                title.add(dn);
                detail.add(dnl);
            }

        }
        if(w.getAntonyms().size() != 0)
        {
            if(w.getAntonyms().size()==1 && w.getAntonyms().get(0).equals(""))
            {

            }
            else {
                String tn = "Trái nghĩa";
                ListView tnl = new ListView(getContext());
                ArrayList<String> arrtn = new ArrayList<String>();
                for (String it : w.getAntonyms()) {
                    arrtn.add(it);
                }
                DongNghiaAdapter dongNghiaAdapter = new DongNghiaAdapter(arrtn, getContext());
                tnl.setAdapter(dongNghiaAdapter);

                title.add(tn);
                detail.add(tnl);
            }
        }
        if(w.getExamples().size() != 0)
        {
            if(w.getExamples().size()==1 && w.getExamples().get(0).equals(""))
            {

            }
            else {
                String ex = "Ví dụ";
                ListView exl = new ListView(getContext());
                ArrayList<String> arrex = new ArrayList<String>();
                for (String it : w.getExamples()) {
                    arrex.add(it);
                }
                DongNghiaAdapter dongNghiaAdapter = new DongNghiaAdapter(arrex, getContext());
                exl.setAdapter(dongNghiaAdapter);

                title.add(ex);
                detail.add(exl);
            }
        }
        expandableListView.setAdapter(new ExpandableListViewAdapter(getContext(),title,detail));
        setListViewHeight(expandableListView,1);
    }

    private void addHistorySearch(String toString,boolean flag) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        if(flag) {
            wordHistory = new WordHistory(toString, "Tra từ", simpleDateFormat.format(Calendar.getInstance().getTime()), System.currentTimeMillis());
            reff.child(String.valueOf(countHistory + 1)).setValue(wordHistory);
        }
        else
        {
            wordHistory = new WordHistory(toString, "Tra từ", simpleDateFormat.format(Calendar.getInstance().getTime()), System.currentTimeMillis());
            reff.child(String.valueOf(indx)).setValue(wordHistory);
        }

    }
    //-----------GetAssetinAPI
    private WordDefinitions getDefinition(String word)
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/"+word+"/definitions")
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e04ae66d6emshc4e396f30acf6a3p1c6175jsn5905fe697fe9")
                .build();


        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            WordDefinitions wordDefinitions = new WordDefinitions();
            wordDefinitions = gson.fromJson(json,WordDefinitions.class);
            return wordDefinitions;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
                if(rs.getExamples().length == 0)
                    return null;
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
                if(rs.getSynonyms().length == 0)
                    return null;
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
                if(rs.getAntonyms().length == 0)
                    return null;
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
    //----Hidekeyboard------
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //----Camera----
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
    //-----------GiongNoi
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
    //-----CheckInternet
    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }
    //-------LoadFragment
    private void loadFragment(Fragment fragment, int fragment_dongNghia) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragment_dongNghia,fragment);
        fragmentTransaction.commit();
    }
}
