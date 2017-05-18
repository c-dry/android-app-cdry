package com.amobi_team.c_dry_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amobi_team.c_dry_application.model.OrderLaundry;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OfficerActivity extends AppCompatActivity {
    private ListView listView;
    private List<OrderLaundry> resultResponseActive;
    private Button btnRefresh;
    private ArrayAdapter<String> adapterActive;
    static ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer);
        listView = (ListView) findViewById(R.id.ListActiveOrder);
        btnRefresh = (Button) findViewById(R.id.btnRefreshActiveOfficer);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActiveOrderByEmail();
                adapterActive = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, parseOrderLaundryToShowDateOrderOnlyForActive());
                listView.setAdapter(adapterActive);
            }
        });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
            SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setCanceledOnTouchOutside(false);
            sweetAlertDialog.setTitleText("Action")
                    .setContentText("Please chooce your action : ")
                    .setConfirmText("Update Order")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            UpdateOrderLaundry.data=resultResponseActive.get(i);
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(getApplicationContext(),UpdateOrderLaundry.class);
                            startActivity(intent);
                        }
                    })
                    .setCancelText("Delete Orer")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deleteActiveOrderById(resultResponseActive.get(i).getId_order().toString());
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
            }
        });
    }

    public void getActiveOrderByEmail(){
        RequestQueue requestQueueActive = Volley.newRequestQueue(this);
        StringRequest endpointActive = new StringRequest(Request.Method.GET, "http://c-laundry.hol.es/api2/getAllOrder.php",
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
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
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
        progressDialog = new ProgressDialog(getApplicationContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data");
        progressDialog.show();

        requestQueueActive.add(endpointActive);

    }
    public void deleteActiveOrderById(final String id_order){
        RequestQueue requestQueueActive = Volley.newRequestQueue(this);
        StringRequest endpointActive = new StringRequest(Request.Method.POST, "http://c-laundry.hol.es/api2/deleteOrder.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_order",id_order);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        progressDialog = new ProgressDialog(getApplicationContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Deleting Order");
        progressDialog.show();

        requestQueueActive.add(endpointActive);
    }
    public ArrayList<String> parseOrderLaundryToShowDateOrderOnlyForActive(){
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < resultResponseActive.size(); i++) {
            temp.add(resultResponseActive.get(i).getDate_order());
            Log.e("Parsing data",resultResponseActive.get(i).getDate_order().toString());
        }
        return temp;
    }
}
