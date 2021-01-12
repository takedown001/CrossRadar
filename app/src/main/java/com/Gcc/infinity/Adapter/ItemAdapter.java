package com.Gcc.infinity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Gcc.infinity.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<NewsAdapter> itemList1;
    private Context ctx;

    public ItemAdapter(Context context, List<NewsAdapter> itemList1) {
        ctx = context;
        this.itemList1 = itemList1;
    }


    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, final int position) {

    holder.textView.setText(itemList1.get(position).getTitle());
    holder.itemtxt.setText(itemList1.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return itemList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView itemtxt;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.titlenews);
            itemtxt=itemView.findViewById(R.id.descriptionnews);

        }
    }
}