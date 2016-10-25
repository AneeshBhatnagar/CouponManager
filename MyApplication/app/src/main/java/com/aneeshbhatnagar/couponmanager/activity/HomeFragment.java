package com.aneeshbhatnagar.couponmanager.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aneeshbhatnagar.couponmanager.R;
import com.aneeshbhatnagar.couponmanager.adapter.CouponAdapter;
import com.aneeshbhatnagar.couponmanager.adapter.DatabaseHandler;
import com.aneeshbhatnagar.couponmanager.model.Coupon;

import java.util.List;


public class HomeFragment extends Fragment {

    private Context context;
    private View view;

    public HomeFragment() {
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
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleViewHome);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        rv.setLayoutManager(lm);
        DatabaseHandler db = new DatabaseHandler(context);
        List<Coupon> couponList = db.getAllCoupons();
        CouponAdapter adapter = new CouponAdapter(couponList,getActivity());
        rv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);


        // Inflate the layout for this fragment
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
        Fragment fragment = new AddNewCouponFragment();
        if (item.getItemId() == R.id.action_search) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top,
                    android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add New Item");
        }

        return super.onOptionsItemSelected(item);
    }
}