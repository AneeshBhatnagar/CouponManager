package com.aneeshbhatnagar.couponmanager.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aneeshbhatnagar.couponmanager.R;
import com.aneeshbhatnagar.couponmanager.adapter.DatabaseHandler;
import com.aneeshbhatnagar.couponmanager.model.Store;

public class AddNewStoreFragment extends Fragment {

    Button save, cancel;
    RadioGroup rg;
    RadioButton online, offline;
    EditText name, url;
    View view;
    Context context;

    public AddNewStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_store, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getActivity().getApplicationContext();
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        save = (Button) view.findViewById(R.id.btnSaveStore);
        cancel = (Button) view.findViewById(R.id.btnCancelStore);
        rg = (RadioGroup) view.findViewById(R.id.rgStoreType);
        online = (RadioButton) view.findViewById(R.id.rbOnline);
        offline = (RadioButton) view.findViewById(R.id.rbOffline);
        name = (EditText) view.findViewById(R.id.etStore);
        url = (EditText) view.findViewById(R.id.etUrl);
    }

    private void setupListeners() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Toast.makeText(context, "Operation Cancelled!", Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_bottom,
                        android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
                fragmentTransaction.replace(R.id.container_body, new StoresFragment());
                fragmentTransaction.commit();

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Saved Stores");
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbOffline:
                        url.setVisibility(View.GONE);
                        break;
                    case R.id.rbOnline:
                        url.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()){
                    Toast.makeText(context,"Please enter the store name",Toast.LENGTH_LONG).show();
                }else{
                    if(url.getText().toString().isEmpty() && online.isChecked()){
                        Toast.makeText(context,"Please enter URL for the Store",Toast.LENGTH_LONG).show();
                    }else{
                        String nameVal = name.getText().toString();
                        int storeType = -1;
                        String urlVal = null;
                        if(online.isChecked()){
                            storeType = 1;
                            urlVal = url.getText().toString();
                        }else if(offline.isChecked()){
                            storeType=0;
                            urlVal = null;
                        }
                        Store store = new Store(nameVal,storeType,urlVal);
                        DatabaseHandler db = new DatabaseHandler(context);
                        db.addStore(store);
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Toast.makeText(context,"Store Saved!",Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_bottom,
                                android.support.v7.appcompat.R.anim.abc_fade_out);
                        fragmentTransaction.replace(R.id.container_body, new StoresFragment());
                        fragmentTransaction.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Saved Stores");
                    }
                }
            }
        });
    }
}
