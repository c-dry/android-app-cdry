package com.amobi_team.c_dry_application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderHistory extends AppCompatActivity {
    static int PAGE_ADD = 1;
    static int PAGE_VIEW = 2;
    static int PAGE_HISTORY=3;
    public static String emailUser;
    public static String address;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue_btn_bg_pressed_color));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(getArguments().getInt(ARG_SECTION_NUMBER)==PAGE_ADD) {
                View rootView = inflater.inflate(R.layout.fragment_add_order, container, false);
                rootView.setBackgroundColor(getResources().getColor(R.color.forgetMeNots));
                TextView notif = (TextView) rootView.findViewById(R.id.tvNotif);
                notif.setText("Berikut adalah ketentuan yang berlaku : \n" +
                        "1. Aturan Pertama"+"\n" +
                        "2. Aturan Kedua, dst...");

                Button btnTambah = (Button) rootView.findViewById(R.id.btnTambah);

                btnTambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("You can not cancel your order from this application.")
                                .setConfirmText("Order Now")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        GoOrderLaundry();
//                                        Toast.makeText(view.getContext(),"Your order has been added," +
//                                        " please see active order and wait our employee come",
//                                        Toast.LENGTH_SHORT).show();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                });

                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==PAGE_VIEW) {
                View rootView = inflater.inflate(R.layout.fragment_view_order, container, false);
                rootView.setBackgroundColor(getResources().getColor(R.color.lightSteelBlue1));

                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==PAGE_HISTORY) {
                View rootView = inflater.inflate(R.layout.fragment_view_order_history, container, false);
                rootView.setBackgroundColor(getResources().getColor(R.color.blueRidgeMtns));

                return rootView;
            }
            return null;
        }

        public void GoOrderLaundry(){
            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            StringRequest endpointPost = new StringRequest(Request.Method.POST, "http://c-laundry.hol.es/api2/postOrder.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    params.put("date_order", date);
                    params.put("email",OrderHistory.emailUser);
                    params.put("address",OrderHistory.address);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            requestQueue.add(endpointPost);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
//            switch (position){
//                case 0:
//                        //Add
//                        AddOrder addOrder = new AddOrder();
//                        return addOrder;
//                case 1:
//                        //ActiveOrder
//                        ViewOrder viewOrder = new ViewOrder();
//                        return viewOrder;
//                case 2:
//                        //ViewHistory
//                        ViewOrderHistory viewOrderHistory = new ViewOrderHistory();
//                        return viewOrderHistory;
//            }
//            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Add Order";
                case 1:
                    return "Active Order";
                case 2:
                    return "History";
            }
            return null;
        }
    }
}
