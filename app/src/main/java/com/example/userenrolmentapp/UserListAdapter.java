package com.example.userenrolmentapp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
    List<UserObject> userList=new ArrayList<>();
    private RecyclerView.Adapter mChatListAdapter;
    public UserListAdapter(List<UserObject>userList){
        this.userList=userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        UserListViewHolder rcv=new UserListViewHolder(layoutView);
        return rcv;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {
        String name=userList.get(position).getFname()+" "+userList.get(position).getLname();
        holder.mName.setText(name);
        String dateob="01",monthob="01",yearob="2000";
        if(userList.get(position).getDob().length()>0) {
            dateob = userList.get(position).getDob().substring(0, 2);
            monthob = userList.get(position).getDob().substring(3, 5);
            yearob = userList.get(position).getDob().substring(6, 10);
        }
        LocalDate birthdate = LocalDate.of(Integer.parseInt(yearob), Integer.parseInt(monthob), Integer.parseInt(dateob));
        LocalDate now = LocalDate.now();
        int age = Period.between(birthdate, now).getYears();
        String details=userList.get(position).getGender()+" | "+age+" | "+userList.get(position).getHometown();
        holder.mDetails.setText(details);
        StorageReference picture= FirebaseStorage.getInstance().getReference().child("admin").child(userList.get(position).getPhone()+".jpg");
        if(picture!=null) {
            final long ONE_MEGABYTE = 1024 * 1024;
            picture.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.mPic.setImageBitmap(bm);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("users").child(userList.get(position).getPhone()).setValue(false);
                FirebaseDatabase.getInstance().getReference().child("users").child(userList.get(position).getPhone()).removeValue();
                FirebaseStorage.getInstance().getReference().child("admin").child(userList.get(position).getPhone()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });
                userList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{
        public TextView mName,mDetails;
        public ImageView mPic,mDelete;
        public RelativeLayout mLayout;
        public UserListViewHolder(View view){
            super(view);
            mName=view.findViewById(R.id.name);
            mDetails=view.findViewById(R.id.details);
            mPic=view.findViewById(R.id.pic);
            mDelete=view.findViewById(R.id.delete);
            mLayout=view.findViewById(R.id.layout);
        }
    }
}

