package com.example.drinkserver;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RV_Adapter_Orders extends RecyclerView.Adapter<RV_Adapter_Orders.OrdersViewHolder> {
    private ArrayList<RV_Item_Order> orderList;
    private RV_Adapter_Orders.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (RV_Adapter_Orders.OnItemClickListener mListener) {

        listener = mListener;
    }


    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderName;
        public TextView tv_orderInfo;
        public TextView textView_Time;
        public TextView textView_Table;

        public ConstraintLayout constraintLayout;
        public CardView cardView_itemOrder;



        public OrdersViewHolder(View itemView, final RV_Adapter_Orders.OnItemClickListener listener) {
            super(itemView);
            tv_orderName = itemView.findViewById(R.id.tv_orderName);
            tv_orderInfo = itemView.findViewById(R.id.tv_orderInfo);
            textView_Time = itemView.findViewById(R.id.textView_Time);
            textView_Table = itemView.findViewById(R.id.textView_Table);

            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            cardView_itemOrder = itemView.findViewById(R.id.cardView_itemOrder);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener !=null)  {
                        int position = getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }//End inner class

    public RV_Adapter_Orders(ArrayList<RV_Item_Order> list){
        orderList = list;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_order, parent, false);
        OrdersViewHolder evh = new OrdersViewHolder(v, listener);
        return evh;
    }

    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        RV_Item_Order currentItem = orderList.get(position);
        holder.tv_orderName.setText(currentItem.getName());



        String orderDateFormatted = currentItem.getOrderedDateFormatted();
        String[] orderDate_Parts = orderDateFormatted.split(" ");
        if (orderDate_Parts[0]!=null) {
            holder.textView_Time.setText(orderDate_Parts[0]);
        }


        holder.tv_orderInfo.setText("");

        holder.textView_Table.setText("" + currentItem.getTableNumber());
        holder.cardView_itemOrder.setBackgroundResource(R.drawable.cardview_foreground_rim);

        if(currentItem.isCurrentlySelected() == true) {
            holder.constraintLayout.setBackground(new ColorDrawable(Color.parseColor("#c1fbed")));

        }

        if(currentItem.isCurrentlySelected() == false) {
            holder.constraintLayout.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));

        }

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }


}//end class
