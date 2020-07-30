package com.example.translateproject.fragment.Dich;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.translateproject.Adapter.DongNghiaAdapter;
import com.example.translateproject.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class TraiNghiaFragment extends Fragment {
    ListView lvTN;
    public TraiNghiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trai_nghia, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String[] synonyms = bundle.getStringArray("antonyms");
        ArrayList<String> a = new ArrayList<String>(Arrays.asList(synonyms));
        DongNghiaAdapter dna = new DongNghiaAdapter(a,getContext());
        lvTN = getActivity().findViewById(R.id.lstView_trainghia);
        lvTN.setAdapter(dna);

    }
}

