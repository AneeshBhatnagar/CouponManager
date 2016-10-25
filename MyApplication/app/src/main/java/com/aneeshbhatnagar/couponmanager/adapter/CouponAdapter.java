package com.aneeshbhatnagar.couponmanager.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aneeshbhatnagar.couponmanager.R;
import com.aneeshbhatnagar.couponmanager.activity.EditCoupon;
import com.aneeshbhatnagar.couponmanager.activity.MainActivity;
import com.aneeshbhatnagar.couponmanager.model.Coupon;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {

    List<Coupon> couponList;
    static Context context;
    static DatabaseHandler handler;

    public static class CouponViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView code, store, expiry, desc, type, value, valueholder;
        ImageView copy, share, edit, delete, markUsed;

        public CouponViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            code = (TextView) itemView.findViewById(R.id.holderCode2);
            store = (TextView) itemView.findViewById(R.id.holderStore2);
            expiry = (TextView) itemView.findViewById(R.id.holderExpiry2);
            desc = (TextView) itemView.findViewById(R.id.holderDesc2);
            type = (TextView) itemView.findViewById(R.id.holderType2);
            value = (TextView) itemView.findViewById(R.id.holderValue2);
            valueholder = (TextView) itemView.findViewById(R.id.holderValue);
            copy = (ImageView) itemView.findViewById(R.id.image_copy);
            delete = (ImageView) itemView.findViewById(R.id.image_delete);
            share = (ImageView) itemView.findViewById(R.id.image_share);
            edit = (ImageView) itemView.findViewById(R.id.image_edit);
            markUsed = (ImageView) itemView.findViewById(R.id.image_done);
        }
    }

    public CouponAdapter(List<Coupon> coupons, Context context) {
        this.couponList = coupons;
        this.context = context;
        handler = new DatabaseHandler(context);
    }

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card, parent, false);
        CouponViewHolder cvh = new CouponViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final CouponViewHolder holder, final int position) {
        holder.code.setText(couponList.get(position).getCode());
        holder.store.setText(handler.getStoreName(couponList.get(position).getStoreId()));
        holder.expiry.setText(couponList.get(position).getExpiry());
        holder.desc.setText(couponList.get(position).getDesc());
        if (couponList.get(position).getType() == 0) {
            holder.type.setText("Discount Coupon");
            holder.value.setVisibility(View.INVISIBLE);
            holder.valueholder.setVisibility(View.INVISIBLE);
        } else {
            holder.type.setText("Gift Card");
            holder.value.setText(Float.toString(couponList.get(position).getValue()));
        }


        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Coupon Code", couponList.get(position).getCode());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Code Copied to Clipboard", Toast.LENGTH_LONG).show();
            }
        });

        holder.markUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponList.get(position).setUsed(1);
                handler.updateCoupon(couponList.get(position));
                Toast.makeText(context, "Marked as Used", Toast.LENGTH_LONG).show();
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "Hey! " + holder.desc.getText().toString() +
                        ". Use the coupon code: " + holder.code.getText().toString() +
                        " on " + holder.store.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.putExtra(Intent.EXTRA_TEXT, data);
                sendIntent.setType("text/plain");
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure you want to delete this?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.deleteCoupon(couponList.get(position));
                        couponList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeRemoved(position, getItemCount());
                        Toast.makeText(context, "Code Deleted", Toast.LENGTH_LONG).show();
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
                    feeds.switchFragment(new EditCoupon(couponList.get(position)),"Edit Coupon");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
