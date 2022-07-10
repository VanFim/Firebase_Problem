package com.example.drinkserver;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewModel_FR_Orders extends ViewModel {

    //Define Firebase Query that returns all the orders of the last 'pastDaysForDisplayingOrders' days
    private static int pastDaysForDisplayingOrders = 1;
    private static long pastTimeMillis = System.currentTimeMillis() - (long) (pastDaysForDisplayingOrders * 24L * 60 * 60 * 1000);

    private static final Query QUERY_DATE =
            FirebaseDatabase.getInstance("https://drink-server-db-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Orders").orderByChild("orderDateInMilliseconds").startAt(pastTimeMillis);


    private final LiveData_FirebaseOrder liveData = new LiveData_FirebaseOrder(QUERY_DATE);

    @NonNull
    public LiveData_FirebaseOrder getDataSnapshotLiveData() {
        Log.e("LogTag", "In Method: ViewModel_FR getDataSnapshotLiveData() is called at time : " + System.currentTimeMillis());
        return liveData;
    }
}