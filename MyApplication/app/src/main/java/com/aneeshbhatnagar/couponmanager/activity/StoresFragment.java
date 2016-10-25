package com.aneeshbhatnagar.couponmanager.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aneeshbhatnagar.couponmanager.R;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aneeshbhatnagar.couponmanager.adapter.DatabaseHandler;
import com.aneeshbhatnagar.couponmanager.adapter.StoreAdapter;
import com.aneeshbhatnagar.couponmanager.model.Store;

import java.util.List;

public class StoresFragment extends Fragment {

    private Context context;
    private View view;

    public StoresFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getActivity().getApplicationContext();
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleViewStore);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        rv.setLayoutManager(lm);
        DatabaseHandler db = new DatabaseHandler(context);
        List<Store> storeList = db.getAllStores();
        StoreAdapter adapter = new StoreAdapter(storeList,getActivity());
        rv.setAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_stores, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = new AddNewStoreFragment();
        if (item.getItemId() == R.id.action_search) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                    android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add New Store");
        }

        return super.onOptionsItemSelected(item);
    }
}
