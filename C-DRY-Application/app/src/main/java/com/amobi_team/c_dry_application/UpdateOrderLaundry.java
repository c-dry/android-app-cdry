package com.amobi_team.c_dry_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amobi_team.c_dry_application.model.OrderLaundry;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdateOrderLaundry extends AppCompatActivity {
    public static OrderLaundry data;
    private EditText edEmail;
    private EditText edAddress;
    private EditText edWeight;
    private EditText edPrice;
    private EditText edDate;
    private Button btnUpdate;
    private ProgressDialog progressDialog = null;
    private OrderLaundry dataUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_laundry);
        edAddress = (EditText) findViewById(R.id.edAddress);
        edPrice = (EditText) findViewById(R.id.edPrice);
        edDate = (EditText) findViewById(R.id.edDateEnd);
        edEmail = (EditText) findViewById(R.id.edEmail);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        edWeight = (EditText) findViewById(R.id.edWeight);

        edAddress.setText(data.getAddress());
        edEmail.setText(data.getEmail());
        edDate.setText(data.getDate_order());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUpdate = new OrderLaundry();
                dataUpdate.setId_order(data.getId_order());
                dataUpdate.setWeight(edWeight.getText().toString());
                dataUpdate.setPrice(edPrice.getText().toString());
                dataUpdate.setDate_end(edDate.getText().toString());

                SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(UpdateOrderLaundry.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setTitleText("Action")
                        .setContentText("Please chooce your action : ")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                updateOrderByIdOrder();
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(getApplicationContext(),OfficerActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setCancelText("Cancel Update")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });
    }

    public void updateOrderByIdOrder(){
        RequestQueue requestQueueActive = Volley.newRequestQueue(this);
        StringRequest endpointActive = new StringRequest(Request.Method.POST, "c-laundry.hol.es/api2/updateOrder.php",
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
                params.put("id_order",dataUpdate.getId_order());
                params.put("weight",dataUpdate.getWeight());
                params.put("price",dataUpdate.getPrice());
                params.put("date_end",dataUpdate.getDate_end());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        progressDialog = new ProgressDialog(UpdateOrderLaundry.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Deleting Order");
        progressDialog.show();

        requestQueueActive.add(endpointActive);
    }
}
