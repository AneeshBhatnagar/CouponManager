package com.aneeshbhatnagar.couponmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aneeshbhatnagar.couponmanager.R;
import com.aneeshbhatnagar.couponmanager.activity.EditStore;
import com.aneeshbhatnagar.couponmanager.activity.MainActivity;
import com.aneeshbhatnagar.couponmanager.activity.StoreCoupons;
import com.aneeshbhatnagar.couponmanager.model.Store;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    List<Store> storeList;
    static Context context;
    static DatabaseHandler handler;

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name, coupons, type, url, urlHolder;
        ImageView web, edit, delete,showAll;

        public StoreViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv2);
            name = (TextView) itemView.findViewById(R.id.storeHolderName2);
            coupons = (TextView) itemView.findViewById(R.id.storeHolderCount2);
            type = (TextView) itemView.findViewById(R.id.storeHolderType2);
            url = (TextView) itemView.findViewById(R.id.storeHolderUrl);
            urlHolder = (TextView) itemView.findViewById(R.id.storeHolderUrl2);
            web = (ImageView) itemView.findViewById(R.id.image_web);
            delete = (ImageView) itemView.findViewById(R.id.image_delete2);
            edit = (ImageView) itemView.findViewById(R.id.image_edit2);
            showAll = (ImageView) itemView.findViewById(R.id.image_show_all);
        }
    }

    public StoreAdapter(List<Store> store, Context context) {
        this.storeList = store;
        this.context = context;
        handler = new DatabaseHandler(context);
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_layout, parent, false);
        StoreViewHolder cvh = new StoreViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final StoreViewHolder holder, final int position) {
        holder.name.setText(storeList.get(position).getName());
        holder.coupons.setText(Integer.toString(handler.getStoreCouponCount(storeList.get(position).getId())));
        if (storeList.get(position).getType() == 0) {
            holder.type.setText("Offline Store");
            holder.url.setVisibility(View.GONE);
            holder.urlHolder.setVisibility(View.GONE);
            holder.web.setVisibility(View.GONE);
        } else {
            holder.type.setText("Online Store");
            holder.urlHolder.setText(storeList.get(position).getUrl());
        }


        holder.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = storeList.get(position).getUrl();
                if (url != null) {
                    url = url.toLowerCase();
                    if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                        url = "http://" + url;
                    }
                    Uri uri = Uri.parse(url);
                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    context.startActivity(intent);
                }
            }
        });

        holder.showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    MainActivity feeds = (MainActivity) context;
                    feeds.switchFragment(new StoreCoupons(storeList.get(position)),storeList.get(position).getName());
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Deleting a store will delete all coupons of that store as well. Do you want to delete it?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.deleteStore(storeList.get(position));
                        storeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeRemoved(position, getItemCount());
                        Toast.makeText(context, "Store Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Operation Cancelled", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();

            }
        });

       holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    MainActivity feeds = (MainActivity) context;
                    feeds.switchFragment(new EditStore(storeList.get(position)),"Edit Store");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
