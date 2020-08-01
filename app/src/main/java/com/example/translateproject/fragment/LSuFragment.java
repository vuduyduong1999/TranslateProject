package com.example.translateproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translateproject.Adapter.LichSuAdapter;
import com.example.translateproject.R;
import com.example.translateproject.model.WordHistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LSuFragment extends Fragment {
    ArrayList<WordHistory> listHistory = new ArrayList<WordHistory>();
    DatabaseReference reff;
    long countHistory = 0;
    ListView lstViewls;

    public LSuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_l_su,container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstViewls = getActivity().findViewById(R.id.lstView_Lsu);


        reff = FirebaseDatabase.getInstance().getReference().child("WordHistory");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countHistory = snapshot.getChildrenCount();
                loadHistory(listHistory);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadHistory(final ArrayList<WordHistory> listHistory) {

        for(int i = 1; i <= countHistory; i++)
        {
            DatabaseReference rmn = reff.child(String.valueOf(i));
            final int finalI = i;
            rmn.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String w = snapshot.child("word").getValue().toString();
                        String c = snapshot.child("content").getValue().toString();
                        String t = snapshot.child("time").getValue().toString();
                        long id = Long.valueOf(snapshot.child("numberID").getValue().toString());
                        WordHistory n = new WordHistory(w,c,t,id);
                        listHistory.add(n);
                    }
                    if(finalI == countHistory)
                    {
                        final LichSuAdapter lichSuAdapter = new LichSuAdapter(listHistory,getContext());
                        lstViewls.setAdapter(lichSuAdapter);
                        lstViewls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView textView = view.findViewById(R.id.tvWordH);
                                Bundle bundle = new Bundle();
                                bundle.putString("word",textView.getText().toString());
                                DichFragment dichFragment = new DichFragment();
                                dichFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment,dichFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Toast.makeText(getContext(), String.valueOf(countHistory), Toast.LENGTH_SHORT).show();
    }
}
