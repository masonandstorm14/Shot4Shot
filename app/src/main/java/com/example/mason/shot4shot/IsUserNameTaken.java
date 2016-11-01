package com.example.mason.shot4shot;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by mason on 10/25/2016.
 */


public class IsUserNameTaken {

    private Boolean returner;

    public IsUserNameTaken(){}

    public boolean IsUserNameTaken(DatabaseReference data, final String username){
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                Iterator it = map.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry entry = (Map.Entry)it.next();
                    User user = dataSnapshot.child(entry.getKey().toString()).getValue(User.class);
                    if(user.username.equals(username)){
                        returner = true;
                        break;
                    }else{
                        returner = false;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return returner;
    }
}
