package com.example.drinkserver;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drinkserver.databinding.ActivityMainBinding;
import com.example.drinkserver.databinding.FragmentOrdersBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FR_Orders extends Fragment implements View.OnClickListener {


    private FragmentOrdersBinding binding;
    private ActivityMainBinding binding_main;

    //Define variables for the RecyclerView
    private RecyclerView recyclerView_Order;
    private RV_Adapter_Orders adapter_Order;
    private RecyclerView.LayoutManager layoutManager_Order;
    private ArrayList<RV_Item_Order> orderList;
    private static int numberOfReturnedItems = 0;
    private static int numberOfInsertedItemsRV = 0;
    private String orderStatusToBeShownInRV = "ordered";
    ViewModel_FR_Orders viewModel;
    LiveData_FirebaseOrder liveData;
    DataSnapshot currentDataSnapShotFromFirebase;


    private int currentNumberOfActiveItems;
    private int currentNumberOfOrdersToBeDelivered ;

    DatabaseReference rootRef_Firebase;

    public FR_Orders() {
        // Required empty public constructor
    }


    public static FR_Orders newInstance(String param1, String param2) {
        FR_Orders fragment = new FR_Orders();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderList = new ArrayList<RV_Item_Order>();
        adapter_Order = new RV_Adapter_Orders(orderList);


        rootRef_Firebase = FirebaseDatabase.getInstance("https://drink-server-db-default-rtdb.europe-west1.firebasedatabase.app/").getReference();


        binding = FragmentOrdersBinding.inflate(getLayoutInflater());

        //Create View Model and LiveData for the Firebase queries
        viewModel = new ViewModelProvider(this).get(ViewModel_FR_Orders.class);

        liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                liveData.setCurrentDataSnapShotFromFirebase(dataSnapshot);
                if(liveData.getCurrentDataSnapShotFromFirebase()!=null) {
                    updateRecyclerView(liveData.getCurrentDataSnapShotFromFirebase());
                }

            }// end onChange
        }); //end observe

    }// end onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        binding.buttonAction.setOnClickListener(this);

        buildRecyclerView();
        return binding.getRoot();
    }//end onCreateView

    /*
    Update the RecyclerView with the DataSnaphot from the Firebase Query
     */
    public void updateRecyclerView(DataSnapshot dataSnapshot) {
        orderList.clear();
        currentNumberOfActiveItems = 0;

        for(DataSnapshot ds: dataSnapshot.getChildren()) {

            if (ds != null) {
                numberOfReturnedItems++;


                String comment_Text = "";
                String name  = "";

                String orderDate = "";
                String orderStatus = "";
                String orderOtherInfo = "";
                long orderDateInMilliseconds = 0;
                int orderID = 0;
                int quantity = 0;
                int tableNumber = 0;

                // update the UI here with values in the snapshot

                if(ds.child("comment_Text").getValue(String.class)!=null) {
                    comment_Text =  ds.child("comment_Text").getValue(String.class);
                }
                if( ds.child("name").getValue(String.class)!=null) {
                    name = ds.child("name").getValue(String.class);
                }


                if(ds.child("orderDate").getValue(String.class)!=null) {
                    orderDate = ds.child("orderDate").getValue(String.class);
                }
                if(ds.child("orderOtherInfo").getValue(String.class) !=null) {
                    orderOtherInfo = ds.child("orderOtherInfo").getValue(String.class);
                }
                if(ds.child("orderStatus").getValue(String.class)!=null) {
                    orderStatus = ds.child("orderStatus").getValue(String.class);
                }
                if(ds.child("orderDateInMilliseconds").getValue(Long.class)!=null) {
                    orderDateInMilliseconds = ds.child("orderDateInMilliseconds").getValue(Long.class);
                }
                if(ds.child("quantity").getValue(Integer.class)!=null) {
                    quantity = ds.child("quantity").getValue(Integer.class);
                }
                if(ds.child("orderID").getValue(Integer.class)!=null) {
                    orderID = ds.child("orderID").getValue(Integer.class);
                }
                if(ds.child("tableNumber").getValue(Integer.class)!=null) {
                    tableNumber = ds.child("tableNumber").getValue(Integer.class);
                }

                if(ds.child("orderStatus").getValue(String.class)!=null && ds.child("orderStatus").getValue(String.class).equals("ordered")) {
                    currentNumberOfActiveItems++;

                }


                if(ds.child("orderStatus").getValue(String.class)!=null && ds.child("orderStatus").getValue(String.class).equals(orderStatusToBeShownInRV)) {
                    orderList.add(new RV_Item_Order(name, comment_Text, orderDateInMilliseconds, orderDate, orderOtherInfo, tableNumber, orderStatus, quantity, orderID));
                    numberOfInsertedItemsRV++;
                }
                ;
            }
        }

        //Collections.reverse(orderList);
        adapter_Order.notifyDataSetChanged();
        binding.textViewNumberToBePreparedValue.setText("" + currentNumberOfActiveItems);

        numberOfInsertedItemsRV = 0;
        numberOfReturnedItems = 0;
    }//end updateRecyclerView


    public void buildRecyclerView() {
        recyclerView_Order = binding.rvDrinksToBeDisplayed;
        recyclerView_Order.setHasFixedSize(true);
        layoutManager_Order = new LinearLayoutManager(this.getContext());
        recyclerView_Order.setLayoutManager(layoutManager_Order);
        recyclerView_Order.setAdapter(adapter_Order);

        adapter_Order.setOnItemClickListener(new RV_Adapter_Orders.OnItemClickListener() {
            /*
        Define what happens when clicking on an item (order) in the RecyclerView
         */
            @Override
            public void onItemClick(int position) {

                //Unmark selected items if the toggle button "deliver" is not activated
                if (orderStatusToBeShownInRV.equals("prepared")==false) {
                     for (int i = 0; i < orderList.size(); i++) {
                            if (i!= position) {
                        orderList.get(i).setCurrentlySelected(false);
                    }
                }
                }

                //Mark the selected item
                if (orderList.get(position).isCurrentlySelected()==false) {
                    orderList.get(position).setCurrentlySelected(true);
                }

                //Unmark the selected item if it was marked before
                else if (orderList.get(position).isCurrentlySelected()==true) {
                    orderList.get(position).setCurrentlySelected(false);
                }

                adapter_Order.notifyDataSetChanged();

            }
        });


    }//end build recyclerView

    @Override
    public void onClick(View view) {

        /*
        Implement the action button
         */

        //PROBLEMATIC QUERY NR 1: GET QUERY.
        // All items in Firebase DB that have the same attribute "orderID" as the item selected in the RecyclerView should be returned
        if (view.getId() == R.id.buttonAction) {
            for (int i =0; i<orderList.size();i++) {
                if (orderList.get(i).isCurrentlySelected()==true) {

                    int tableNumberSelected = orderList.get(i).getTableNumber();
                    long orderDateInMillisecondsSelected = orderList.get(i).getOrderDateMilliSeconds();


                    Log.e("LogTag_Orders", "########Get query######" );
                    DatabaseReference ordersRef = rootRef_Firebase.child("Orders");
                    Query queryOrdesById = ordersRef.orderByChild("orderID").equalTo(orderList.get(i).getOrderID()); //The command "orderList.get(i).getOrderID()" gets the orderID of the item in the Recyclerview
                    queryOrdesById.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            int numberOfReturnedItemsGetQuery = 0;
                            if (task.isSuccessful()) {
                                for (DataSnapshot orderSnapshot : task.getResult().getChildren()) {
                                    String nameResult = "";
                                    numberOfReturnedItemsGetQuery++;
                                    if (orderSnapshot.child("name").getValue(String.class) !=null) {
                                        nameResult = orderSnapshot.child("name").getValue(String.class);
                                    }
                                    String orderDateResult ="";
                                    if (orderSnapshot.child("orderDate").getValue(String.class) !=null) {
                                        orderDateResult = orderSnapshot.child("orderDate").getValue(String.class);
                                    }

                                    int orderID =-1;
                                    if (orderSnapshot.child("orderID").getValue(Integer.class) !=null) {
                                        orderID = orderSnapshot.child("orderID").getValue(Integer.class);
                                    }
                                }
                            } else {
                                Log.e("LogTag_Orders", "Task not successfull: " +  task.getException().getMessage());
                            }
                            Log.e("LogTag_Orders", "numberOfReturnedItemsGetQuery (Get Query): " + numberOfReturnedItemsGetQuery);
                            binding.textViewQueryTextGetNumber.setText("" + numberOfReturnedItemsGetQuery);

                        }
                    });

                    //PROBLEMATIC QUERY NR 2: CONVENTIONAL QUERY
                    // All items in Firebase DB that have the same attribute "orderID" as the item selected in the RecyclerView should be returned
                    Log.e("LogTag_Orders", "########Conventional query######" );
                    rootRef_Firebase
                            .child("Orders")
                            .orderByChild("orderID")
                            .equalTo(orderList.get(i).getOrderID())  //The command "orderList.get(i).getOrderID()" gets the orderID of the item in the Recyclerview
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int numberOfReturnedItems = 0;
                                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                        numberOfReturnedItems++;
                                        int tableNumberResult = -1;
                                        if (ds.child("tableNumber").getValue(Integer.class)!=null) {
                                            tableNumberResult= ds.child("tableNumber").getValue(Integer.class);
                                        }
                                        long orderDateInMillisecondsResult = 0;
                                        if (ds.child("orderDateInMilliseconds").getValue(Long.class) !=null) {
                                             orderDateInMillisecondsResult = ds.child("orderDateInMilliseconds").getValue(Long.class);
                                        }

                                        String nameResult = "";
                                        if (ds.child("name").getValue(String.class) !=null) {
                                            nameResult = ds.child("name").getValue(String.class);
                                        }

                                        String orderDateResult ="";
                                        if (ds.child("orderDate").getValue(String.class) !=null) {
                                            orderDateResult = ds.child("orderDate").getValue(String.class);
                                        }

                                        int orderID = -1 ;
                                        if (ds.child("orderID").getValue(Integer.class) !=null) {
                                            orderID = ds.child("orderID").getValue(Integer.class);
                                        }


                                        //When the returned attributes of the item in firebase DB match the ones of the item in the recyclerview, the attribute "orderStatus" is set to "prepared" and thus it is not displayed anymore in the RecyclerView
                                        if (tableNumberResult == tableNumberSelected && orderDateInMillisecondsResult == orderDateInMillisecondsSelected) {
                                            ds.getRef().child("orderStatus").setValue("prepared");
                                        }
                                    }
                                    Log.e("LogTag_Orders", ":numberOfReturnedItems (First Query) " + numberOfReturnedItems);
                                    binding.textViewQueryTextConventionalNumber.setText("" + numberOfReturnedItems);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("LogTag_Orders", "onCancelled");
                                    Log.e("LogTag_Orders", "databaseError.toException(): " + databaseError.toException());
                                    throw databaseError.toException();
                                }

                            });

                } // end for loop i<orderList.size()

            } // end if button == Prepared

        }//End botton action

    }//End onClick

}//End class