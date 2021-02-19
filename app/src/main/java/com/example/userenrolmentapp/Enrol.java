package com.example.userenrolmentapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Enrol#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Enrol extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean pass=true;
    EditText firstName,lastName,DOB,gender,country,state,homeTown,phone;
    ImageView picture;
    Bitmap curBitmap;
    public static final int GET_FROM_GALLERY = 3;
    Button addUser;
    public Enrol() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Enrol.
     */
    // TODO: Rename and change types and number of parameters
    public static Enrol newInstance(String param1, String param2) {
        Enrol fragment = new Enrol();
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
    private void validate() {
        if( firstName.getText().toString().length() == 0 ) {
            firstName.setError("First name is required!");
            pass = false;
        }
        else
            firstName.setText(firstName.getText().toString().substring(0,1).toUpperCase().concat(firstName.getText().toString().substring(1).toLowerCase()));
        if( lastName.getText().toString().length() == 0 ){
            lastName.setError( "Last name is required!" );
            pass = false;
        }
        else
            lastName.setText(lastName.getText().toString().substring(0,1).toUpperCase().concat(lastName.getText().toString().substring(1).toLowerCase()));
        if( DOB.getText().toString().length() !=10){
            if(DOB.getText().toString().length() ==0)
                DOB.setError( "Date of birth is required!" );
            else
                DOB.setError( "Date of birth should be in the format dd-mm-yyyy!" );
            pass = false;
        }
        else {
            boolean passDate=true;
            for (int i = 0; i < 10; i++) {
                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        if (DOB.getText().toString().charAt(i) < '0' || DOB.getText().toString().charAt(i) > '9') {
                            DOB.setError("Date of birth should be in the format dd-mm-yyyy!");
                            pass = false;
                            passDate=false;
                        }
                        break;
                    case 2:
                    case 5:
                        if (DOB.getText().toString().charAt(i) != '-') {
                            DOB.setError("Date of birth should be in the format dd-mm-yyyy!");
                            pass = false;
                            passDate=false;
                        }
                }
            }
            if(passDate){
                Set<String> dates = new HashSet<String>();
                for (int year = 1800; year < 2050; year++) {
                    for (int month = 1; month <= 12; month++) {
                        for (int day = 1; day <= YearMonth.of(year, month).lengthOfMonth(); day++) {
                            StringBuilder date = new StringBuilder();
                            date.append(String.format("%02d", day));
                            date.append("-");
                            date.append(String.format("%02d", month));
                            date.append("-");
                            date.append(String.format("%04d", year));
                            dates.add(date.toString());
                        }
                    }
                }
                if(!dates.contains(DOB.getText().toString())) {
                    DOB.setError("Invalid Date!");
                    pass = false;
                }
                else{
                    Date enteredDate=null;
                    try
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        enteredDate = sdf.parse(DOB.getText().toString());
                    }catch (Exception ex)
                    {
                        // enteredDate will be null if date="287686";
                    }
                    Date currentDate = new Date();
                    if(enteredDate.after(currentDate)){
                        DOB.setError("Invalid Date!");
                        pass = false;
                    }
                }
            }
        }
        String gen=gender.getText().toString().toLowerCase();
        if(!((gen.equals(("male")))||(gen.equals("female"))||(gen.equals("other")))){
            if(gen.length()==0)
                gender.setError("Country is required!");
            else
                    gender.setError("Gender must be either Male, Female or Other.");
            pass = false;
        }
        else
            gender.setText(gen.substring(0,1).toUpperCase().concat(gen.substring(1).toLowerCase()));
        if( country.getText().toString().length() == 0 ) {
            country.setError("Country is required!");
            pass = false;
        }
        else
            country.setText(country.getText().toString().substring(0,1).toUpperCase().concat(country.getText().toString().substring(1).toLowerCase()));
        if( state.getText().toString().length() == 0 ){
            state.setError( "State is required!" );
            pass = false;
        }
        else
            state.setText(state.getText().toString().substring(0,1).toUpperCase().concat(state.getText().toString().substring(1).toLowerCase()));
        if( homeTown.getText().toString().length() ==0){
            homeTown.setError( "Home Town is required!" );
            pass = false;
        }
        else
            homeTown.setText(homeTown.getText().toString().substring(0,1).toUpperCase().concat(homeTown.getText().toString().substring(1).toLowerCase()));
        if( phone.getText().toString().length()==0){
            phone.setError( "Phone number is required!" );
            pass = false;
        }
        else {
            boolean phPass=true;
            if (phone.getText().toString().charAt(0) != '+') {
                phone.setError("Country code is required.");
                pass = false;
                phPass=false;
            }
            else if(phone.getText().toString().length()<8||phone.getText().toString().length()>15){
                phone.setError("Invalid Phone number");
                pass = false;
                phPass=false;
            }
            else {
                if (!isNumeric(phone.getText().toString().substring(1))) {
                    phone.setError("Invalid Phone number");
                    pass = false;
                    phPass = false;
                }
            }
            if(phPass){
                DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                if(childSnapshot.getKey().equals(phone.getText().toString())){
                                    pass=false;
                                    phone.setError("Phone number already enrolled!");
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enrol, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstName=getView().findViewById(R.id.fname);
        lastName=getView().findViewById(R.id.lname);
        DOB=getView().findViewById(R.id.dob);
        gender=getView().findViewById(R.id.gender);
        country=getView().findViewById(R.id.country);
        state=getView().findViewById(R.id.state);
        homeTown=getView().findViewById(R.id.hometown);
        phone=getView().findViewById(R.id.phone);
        picture=getView().findViewById(R.id.upload);
        addUser=getView().findViewById(R.id.adduser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                if(pass){
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");

                    db.child(phone.getText().toString()).child("fname").setValue(firstName.getText().toString());
                    db.child(phone.getText().toString()).child("lname").setValue(lastName.getText().toString());
                    db.child(phone.getText().toString()).child("dob").setValue(DOB.getText().toString());
                    db.child(phone.getText().toString()).child("gender").setValue(gender.getText().toString());
                    db.child(phone.getText().toString()).child("country").setValue(country.getText().toString());
                    db.child(phone.getText().toString()).child("state").setValue(state.getText().toString());
                    db.child(phone.getText().toString()).child("hometown").setValue(homeTown.getText().toString());
                    DateTimeFormatter formatter =
                            DateTimeFormatter
                                    .ofPattern("yyyyMMdd HH:ss.SSS")
                                    .withLocale(Locale.getDefault())
                                    .withZone(ZoneId.systemDefault());
                    Instant now = Instant.now();

                    String formatted = formatter.format(now);
                    db.child(phone.getText().toString()).child("timestamp").setValue(formatted);
                    if(curBitmap!=null) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("admin").child(phone.getText().toString() + ".jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        curBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                            }
                        });
                    }
                    Toast.makeText(getContext(),"Enrolment successful",Toast.LENGTH_SHORT).show();
                    firstName.setText("");
                    lastName.setText("");
                    DOB.setText("");
                    gender.setText("");
                    country.setText("");
                    state.setText("");
                    homeTown.setText("");
                    phone.setText("");
                }
                else
                    pass=true;
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                curBitmap=bitmap;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}