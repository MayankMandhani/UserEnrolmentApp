package com.example.userenrolmentapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<UserObject> userList=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    public UserList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserList.
     */
    // TODO: Rename and change types and number of parameters
    public static UserList newInstance(String param1, String param2) {
        UserList fragment = new UserList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_user_list, container, false);
        recyclerView=(RecyclerView) v.findViewById(R.id.userlist);
        recyclerView.setNestedScrollingEnabled (false);
        recyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(v.getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new UserListAdapter(userList);
        recyclerView.setAdapter(adapter);
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("users");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String phone="",fname="",lname="",dob="",gender="",country="",state="",hometown="",timestamp="";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            phone = childSnapshot.getKey();
                        if (childSnapshot.child("fname").getValue() != null)
                            fname = childSnapshot.child("fname").getValue().toString();
                        if (childSnapshot.child("lname").getValue() != null)
                            lname = childSnapshot.child("lname").getValue().toString();
                        if (childSnapshot.child("dob").getValue() != null)
                            dob = childSnapshot.child("dob").getValue().toString();
                        if (childSnapshot.child("gender").getValue() != null)
                            gender = childSnapshot.child("gender").getValue().toString();
                        if (childSnapshot.child("country").getValue() != null)
                            country = childSnapshot.child("country").getValue().toString();
                        if (childSnapshot.child("state").getValue() != null)
                            state = childSnapshot.child("state").getValue().toString();
                        if (childSnapshot.child("hometown").getValue() != null)
                            hometown = childSnapshot.child("hometown").getValue().toString();
                        if (childSnapshot.child("timestamp").getValue() != null)
                            timestamp = childSnapshot.child("timestamp").getValue().toString();
                        UserObject mUser = new UserObject(phone,fname,lname,dob,gender,country,state,hometown,timestamp);
                        boolean exists=false;
                        for(UserObject userObject:userList){
                            if(userObject.getPhone().equals(mUser.getPhone())){
                                exists=true;
                                break;
                            }
                        }
                        if (!exists) {
                            Map newMessageMap=new HashMap<>();
                            newMessageMap.put("phone",phone);
                            newMessageMap.put("fname",fname);
                            newMessageMap.put("fname",fname);
                            newMessageMap.put("fname",fname);
                            newMessageMap.put("fname",fname);
                            newMessageMap.put("fname",fname);
                            newMessageMap.put("fname",fname);
                            newMessageMap.put("fname",fname);
                            userList.add(mUser);
                            adapter.notifyDataSetChanged();
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        adapter.notifyDataSetChanged();

        return v;
    }

}

