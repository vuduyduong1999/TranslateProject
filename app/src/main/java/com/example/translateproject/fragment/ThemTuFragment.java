package com.example.translateproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.example.translateproject.R;
import com.example.translateproject.model.Definition;
import com.example.translateproject.model.Word;
import com.example.translateproject.model.WordHistory;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThemTuFragment extends Fragment {
    EditText edWord,edDefi,edNoun,edVerd,edADJ,edADV, edSyno,edAnto,edExam;
    Button btnSave;
    DatabaseReference rff,reff;
    long countHistory = 0;
    boolean flag = true;
    int indx = 1;
    public ThemTuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_them_tu, container, false);
    }
    private void addHistoryAdd(String toString,boolean flag) {
        WordHistory wordHistory;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        if(flag) {
            wordHistory = new WordHistory(toString, "Thêm từ mới", simpleDateFormat.format(Calendar.getInstance().getTime()), System.currentTimeMillis());
            reff.child(String.valueOf(countHistory + 1)).setValue(wordHistory);
        }
        else
        {
            wordHistory = new WordHistory(toString, "Tra từ", simpleDateFormat.format(Calendar.getInstance().getTime()), System.currentTimeMillis());
            reff.child(String.valueOf(indx)).setValue(wordHistory);
        }

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edWord = getActivity().findViewById(R.id.etNewWord);
        edDefi = getActivity().findViewById(R.id.edDifiN);
        edNoun = getActivity().findViewById(R.id.eddanhtu);
        edVerd = getActivity().findViewById(R.id.eddongtu);
        edADJ = getActivity().findViewById(R.id.edtrangtu);
        edADV = getActivity().findViewById(R.id.edtrangtu);
        edSyno = getActivity().findViewById(R.id.edDongNghia);
        edAnto = getActivity().findViewById(R.id.edtraiNghia);
        edExam= getActivity().findViewById(R.id.edExample);
        btnSave = getActivity().findViewById(R.id.btnSave);

        //------Firebase----
        rff = FirebaseDatabase.getInstance().getReference("WordAdded");
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConditonAdd()) {
                    Word nw = makeNewWord();
                    try {
                        String path = nw.getWordn();
                        rff.child(path.replaceAll("\\s+","")).setValue(nw);
                        addHistoryAdd(nw.getWordn(),flag);
                        Toast.makeText(getContext(),"Thêm từ thành công",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception d)
                    {
                        Log.e("Firebase",d.getMessage());
                    }
                }
                else
                {
                    edWord.setFocusable(true);
                }
            }
        });


    }

    private boolean checkConditonAdd() {
        if(edWord.getText().toString().equals("")||edDefi.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private Word makeNewWord() {

        List<Definition> nw= new ArrayList<Definition>();
        String w = edWord.getText().toString();
        String defi = edDefi.getText().toString();
        if(!edNoun.getText().toString().equals(""))
        {
            nw.add(new Definition(edNoun.getText().toString(),"noun"));
        }
        if(!edVerd.getText().toString().equals(""))
        {
            nw.add(new Definition(edVerd.getText().toString(),"verd"));
        }
        if(!edADJ.getText().toString().equals(""))
        {
            nw.add(new Definition(edADJ.getText().toString(),"adjective"));
        }
        if(!edADV.getText().toString().equals(""))
        {
            nw.add(new Definition(edADV.getText().toString(),"adverb"));
        }
        String dn = edSyno.getText().toString();
        String[] synonyms = dn.split(",");
        String tn = edAnto.getText().toString();
        String[] antonyms = tn.split(",");
        String vd = edExam.getText().toString();
        String[] examples = vd.split(",");
        Word n = new Word(w,defi,nw,synonyms,antonyms,examples);
        return n;

    }

}
