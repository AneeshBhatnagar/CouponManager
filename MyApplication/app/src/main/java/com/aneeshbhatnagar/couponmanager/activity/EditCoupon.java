package com.aneeshbhatnagar.couponmanager.activity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aneeshbhatnagar.couponmanager.R;
import com.aneeshbhatnagar.couponmanager.adapter.DatabaseHandler;
import com.aneeshbhatnagar.couponmanager.helper.DatePickerFragment;
import com.aneeshbhatnagar.couponmanager.model.Coupon;

import java.util.List;

public class EditCoupon extends Fragment {

    Spinner spin;
    Button saveCoupon, cancelCoupon;
    ImageView datePick;
    Context context;
    View view;
    RadioButton discount,giftCard;
    RadioGroup rgCode;
    EditText couponCode, couponDesc, value;
    Coupon coupon;
    DatabaseHandler db;
    static EditText expiryDate;

    public EditCoupon(){

    }

    public EditCoupon(Coupon c){
        this.coupon = c;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_coupon, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getActivity().getApplicationContext();
        db = new DatabaseHandler(context);
        initializeViews();
        loadSpinnerData();
        setValues();
        setListeners();
    }

    private void initializeViews() {
        spin = (Spinner) view.findViewById(R.id.spinnerStore);
        saveCoupon = (Button) view.findViewById(R.id.btnSaveCode);
        cancelCoupon = (Button) view.findViewById(R.id.btnCancelCode);
        datePick = (ImageView) view.findViewById(R.id.ivDatePick);
        couponCode = (EditText) view.findViewById(R.id.etCouponCode);
        couponDesc = (EditText) view.findViewById(R.id.etCouponDesc);
        expiryDate = (EditText) view.findViewById(R.id.etExpiry);
        value = (EditText) view.findViewById(R.id.etGiftValue);
        discount = (RadioButton) view.findViewById(R.id.rbDiscount);
        giftCard = (RadioButton) view.findViewById(R.id.rbGiftCard);
        rgCode = (RadioGroup) view.findViewById(R.id.rgCodeType);
        spin.setPrompt("Select Store");
    }

    private void loadSpinnerData() {
        List<String> labels = db.getAllStoreName();
        if(labels.isEmpty()){
            Toast.makeText(context, "Please create a store first", Toast.LENGTH_LONG);
        }else {

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, labels);

            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            spin.setAdapter(dataAdapter);
            spin.setSelection(dataAdapter.getPosition(db.getStoreName(coupon.getStoreId())));
        }
    }

    private void setValues() {
        couponCode.setText(coupon.getCode());
        couponDesc.setText(coupon.getDesc());
        expiryDate.setText(coupon.getExpiry());
        if(coupon.getType() == 0){
            discount.setChecked(true);
        }else{
            giftCard.setChecked(true);
            value.setText(Float.toString(coupon.getValue()));
        }
    }

    private void setListeners() {
        saveCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(couponCode.getText().toString().isEmpty() ||
                        couponDesc.getText().toString().isEmpty() ||
                        expiryDate.getText().toString().isEmpty()){
                    Toast.makeText(context,"Enter all the fields!",Toast.LENGTH_LONG).show();
                }else{
                    if(giftCard.isChecked() && value.getText().toString().isEmpty()){
                        Toast.makeText(context,"Please enter Gift Card Value",Toast.LENGTH_LONG).show();
                    }else{
                        int store_id,type=-1;
                        float giftValue =-1;
                        store_id = db.getStoreId(spin.getSelectedItem().toString());

                        if(giftCard.isChecked()){
                            type=1;
                            giftValue = Float.parseFloat(value.getText().toString());
                        }else if(discount.isChecked()){
                            type=0;
                            giftValue = -1;
                        }

                        Coupon c = new Coupon(coupon.getId(),store_id, type, couponCode.getText().toString(),
                                couponDesc.getText().toString(), expiryDate.getText().toString(),giftValue);
                        db.updateCoupon(c);
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Toast.makeText(context,"Coupon Updated!",Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_bottom,
                                android.support.v7.appcompat.R.anim.abc_fade_out);
                        fragmentTransaction.replace(R.id.container_body, new HomeFragment());
                        fragmentTransaction.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Coupons");
                    }
                }
            }
        });
        rgCode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbDiscount:
                        value.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rbGiftCard:
                        value.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        });
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        cancelCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Toast.makeText(context, "Operation Cancelled!", Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_bottom,
                        android.support.v7.appcompat.R.anim.abc_fade_out);
                fragmentTransaction.replace(R.id.container_body, new HomeFragment());
                fragmentTransaction.commit();

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Coupons");
            }
        });

    }

    public static void setDateField(String date) {
        expiryDate.setText(date);
    }
}
