package com.quirodev.sac.Ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quirodev.sac.MainActivity;
import com.quirodev.sac.R;
import com.quirodev.sac.UsageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hj on 2018. 5. 24..
 */

public class refreshRank extends Fragment {
    Button send;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    ListView listview;
    private ArrayAdapter<String> adapter;

    List<Object> Array = new ArrayList<Object>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.inputrank, container, false);
        send = (Button) view.findViewById(R.id.sendData);
        listview = (ListView) view.findViewById(R.id.listview);


        String username = ((MainActivity)getActivity()).getName();
        String daytime = ((MainActivity)getActivity()).getTime();
        String tokenID = FirebaseInstanceId.getInstance().getToken();

        initDatabase();

        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line
                ,new ArrayList<String>());
        mReference = mDatabase.getReference("Ranking");
        listview.setAdapter(adapter);
        if(daytime.equals("SAC")) {
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.content_main,new UsageFragment()).commit();
            Toast.makeText(getContext(), "사용량을 업데이트 해주세요", Toast.LENGTH_LONG).show();
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child(username);
                    mRef.child("name").setValue(username);
                    mRef.child("time").setValue(daytime);
                    mRef.child("tokenID").setValue(tokenID);
                    DatabaseReference rankRef = FirebaseDatabase.getInstance().getReference().child("Ranking").child(username);
                    rankRef.setValue(daytime);
            }
        });

        mReference = mDatabase.getReference("Ranking"); // 변경값을 확인할 child 이름
        mReference.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 1;
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {

                    String msg2= i+"위  "+messageData.getKey() +": "+ messageData.getValue().toString();
                    Array.add(msg2);
                    adapter.add(msg2);
                    i++;
                }
                for(int j = 0 ; i < j ; i++){
            }
                adapter.notifyDataSetChanged();
                listview.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

}

