package com.amobi_team.c_dry_application;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amobi_team.c_dry_application.model.OrderLaundry;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderHistory extends AppCompatActivity {
    static int PAGE_ADD = 0;
    static int PAGE_VIEW = 1;
    static int PAGE_HISTORY=2;
    public static String emailUser;
    public static String address;
    private static List<OrderLaundry> resultResponseActive = new ArrayList<>();
    private static List<OrderLaundry> resultResponseHistory = new ArrayList<>();
    static ProgressDialog progressDialog = null;
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
        tabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        View rootView;
        ListView listViewActive = null;
        ListView listView = null;
        ArrayAdapter<String> adapterActive;
        ArrayAdapter<String> adapterHistory;

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
                rootView = inflater.inflate(R.layout.fragment_add_order, container, false);
                rootView.setBackgroundColor(getResources().getColor(R.color.accent));

                TextView notif = (TextView) rootView.findViewById(R.id.tvNotif);
                notif.setText("Hello Member... \n" +
                        "In this app, you can order for laundry and see your order \n \n" +
                        "Step to order : \n" +
                        "1. Press button ORDER LAUNDRY below \n" +
                        "2. Wait for our courier to pick up your clothes in your house \n" +
                        "3. Our courier will set weight, price and date end \n" +
                        "4. You can take your clothes after date end be passed \n");

                Button btnTambah = (Button) rootView.findViewById(R.id.btnTambah);

                btnTambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                   SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText("Are you sure?")
                            .setContentText("You can not cancel your order from this application.")
                            .setConfirmText("Order Now")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    GoOrderLaundry();
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelText("Cancel Order")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                    }
                });

                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==PAGE_VIEW) {
                rootView = inflater.inflate(R.layout.fragment_view_order, container, false);
                rootView.setBackgroundColor(getResources().getColor(R.color.accent));
                Button btnRefreshActive = (Button) rootView.findViewById(R.id.btnRefreshActive);
                listViewActive = (ListView) rootView.findViewById(R.id.listViewResult);
                final TextView text = (TextView) rootView.findViewById(R.id.txtNotFound);
                text.setVisibility(View.INVISIBLE);

                getActiveOrderByEmail();

                btnRefreshActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActiveOrderByEmail();
                    }
                });

                listViewActive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        new LovelyStandardDialog(adapterView.getContext())
                                .setTopColorRes(R.color.primary_dark)
                                .setTitle("Detail Order")
                                .setMessage(resultResponseActive.get(i).toString())
                                .setNegativeButton(android.R.string.ok, null)
                                .show();
                    }
                });

                return rootView;
            }

            else if(getArguments().getInt(ARG_SECTION_NUMBER)==PAGE_HISTORY) {
                rootView = inflater.inflate(R.layout.fragment_view_order_history, container, false);
                rootView.setBackgroundColor(getResources().getColor(R.color.accent));
                listView = (ListView) rootView.findViewById(R.id.listViewHistory);
                Button btnRefresh = (Button) rootView.findViewById(R.id.btnRefresh);
                final TextView text = (TextView) rootView.findViewById(R.id.txtNotFound2);
                text.setVisibility(View.INVISIBLE);

                getHistoryOrderByEmail();

                btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getHistoryOrderByEmail();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        new LovelyStandardDialog(adapterView.getContext())
                                .setTopColorRes(R.color.primary_dark)
                                .setTitle("Detail History")
                                .setMessage(resultResponseHistory.get(i).toString())
                                .setNegativeButton(android.R.string.ok, null)
                                .show();
                    }
                });

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
                            Toast.makeText(getContext(),"Your order has been added," +
                                            " please see active order and wait our employee come",
                                    Toast.LENGTH_SHORT).show();
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
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    params.put("date_order", currentDate);
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

        public void getActiveOrderByEmail(){
            RequestQueue requestQueueActive = Volley.newRequestQueue(this.getContext());
            StringRequest endpointActive = new StringRequest(Request.Method.GET, "http://c-laundry.hol.es/api2/getOrders.php?email="+OrderHistory.emailUser,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                resultResponseActive.clear();
                                JSONObject result = new JSONObject(response);
                                JSONArray results = result.getJSONArray("result");
                                for (int i = 0; i < results.length(); i++) {
                                    OrderLaundry orderLaundry = new OrderLaundry();
                                    orderLaundry.setId_order(results.getJSONObject(i).getString("id_order"));
                                    orderLaundry.setEmail(results.getJSONObject(i).getString("email"));
                                    orderLaundry.setAddress(results.getJSONObject(i).getString("address"));
                                    orderLaundry.setWeight(results.getJSONObject(i).getString("weight"));
                                    orderLaundry.setPrice(results.getJSONObject(i).getString("price"));
                                    orderLaundry.setDate_order(results.getJSONObject(i).getString("date_order"));
                                    orderLaundry.setDate_end(results.getJSONObject(i).getString("date_end"));
                                    orderLaundry.setStatus(results.getJSONObject(i).getString("status"));
                                    resultResponseActive.add(orderLaundry);
                                    for (OrderLaundry orderLaundry1: resultResponseActive) {
                                        Log.e("Data : ",orderLaundry1.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();

                            adapterActive = new ArrayAdapter<String>
                                    (rootView.getContext(), android.R.layout.simple_list_item_1, parseOrderLaundryToShowDateOrderOnlyForActive()){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    View view = super.getView(position, convertView, parent);

                                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                                    tv.setTextColor(getResources().getColor(R.color.primary));
                                    return view;
                                }
                            };
                            listViewActive.setAdapter(adapterActive);
                            adapterActive.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return super.getHeaders();
                }
            };
            progressDialog = new ProgressDialog(this.getContext(),R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Getting Data");
            progressDialog.show();

            requestQueueActive.add(endpointActive);

        }

        public void  getHistoryOrderByEmail(){
            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            StringRequest endpoint = new StringRequest(Request.Method.GET, "http://c-laundry.hol.es/api2/getHistory.php?email="+OrderHistory.emailUser,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                resultResponseHistory.clear();
                                JSONObject result = new JSONObject(response);
                                JSONArray results = result.getJSONArray("result");
                                for (int i = 0; i < results.length(); i++) {
                                    OrderLaundry orderLaundry = new OrderLaundry();
                                    orderLaundry.setId_order(results.getJSONObject(i).getString("id_order"));
                                    orderLaundry.setEmail(results.getJSONObject(i).getString("email"));
                                    orderLaundry.setAddress(results.getJSONObject(i).getString("address"));
                                    orderLaundry.setWeight(results.getJSONObject(i).getString("weight"));
                                    orderLaundry.setPrice(results.getJSONObject(i).getString("price"));
                                    orderLaundry.setDate_order(results.getJSONObject(i).getString("date_order"));
                                    orderLaundry.setDate_end(results.getJSONObject(i).getString("date_end"));
                                    orderLaundry.setStatus(results.getJSONObject(i).getString("status"));
                                    resultResponseHistory.add(orderLaundry);
                                    for (OrderLaundry orderLaundry1: resultResponseHistory) {
                                        Log.e("Data : ",orderLaundry1.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();

                            adapterHistory = new ArrayAdapter<String>
                                    (rootView.getContext(), android.R.layout.simple_list_item_1, parseOrderLaundryToShowDateOrderOnlyForHistory()){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    View view = super.getView(position, convertView, parent);

                                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                                    tv.setTextColor(getResources().getColor(R.color.primary));
                                    return view;
                                }
                            };
                            listView.setAdapter(adapterHistory);
                            adapterHistory.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return super.getHeaders();
                }
            };
            progressDialog = new ProgressDialog(this.getContext(),R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Getting Data...");
            progressDialog.show();
            requestQueue.add(endpoint);
        }

        public ArrayList<String> parseOrderLaundryToShowDateOrderOnlyForActive(){
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < resultResponseActive.size(); i++) {
                String tempView = resultResponseActive.get(i).getDate_order();
                temp.add(tempView);
                Log.e("Parsing data",resultResponseActive.get(i).getDate_order().toString());
            }
            return temp;
        }

        public ArrayList<String> parseOrderLaundryToShowDateOrderOnlyForHistory(){
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < resultResponseHistory.size(); i++) {
                String tempView = resultResponseHistory.get(i).getDate_order();
                temp.add(tempView);
                Log.e("Parsing data",resultResponseHistory.get(i).getDate_order().toString());
            }
            return temp;
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
            return PlaceholderFragment.newInstance(position);
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

    @Override
    public void onBackPressed() {
        SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(OrderHistory.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Log out")
                .setContentText("Do you want to logout ?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(OrderHistory.this,LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
