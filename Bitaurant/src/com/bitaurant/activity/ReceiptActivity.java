package com.bitaurant.activity;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitaurant.R;
import com.bitaurant.adapter.OpenTablesAdapter;
import com.bitaurant.adapter.ReceiptAdapter;
import com.bitaurant.models.OpenTable;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ReceiptActivity extends BaseActivity{

	ListView receiptListView;
	ReceiptAdapter mAdapter;

	Button confirmButton;
	Button cancelButton;

	TextView title;
	TextView total;

	ProgressDialog pd;

	RelativeLayout totalLayout;
	OpenTable[] oList;
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);

		receiptListView = (ListView) findViewById(R.id.receipt);

		Bundle extras = getIntent().getExtras();
		id = extras.getString("id");

		final RequestQueue queue = Volley.newRequestQueue(this);
		final String url = "http://hardindd.com/stage/bitaurant/j/get-order?order_id="+id;

		title = (TextView) findViewById(R.id.title);
		total = (TextView) findViewById(R.id.total_price);

		totalLayout = (RelativeLayout) findViewById(R.id.total_layout);

		// prepare the Request
		final StringRequest getRequest = new StringRequest(url, 
				new Listener<String>() {

			@Override
			public void onResponse(String response) {
				pd.dismiss();

				response = Html.fromHtml(response).toString();
				Gson gson = new Gson();

				oList = gson.fromJson(response, OpenTable[].class);

				mAdapter = new ReceiptAdapter(ReceiptActivity.this, oList[0].items);
				receiptListView.setAdapter(mAdapter);

				title.setText("Receipt - "+oList[0].title);
				total.setText("$"+oList[0].price+" / DOGE "+(Double.parseDouble(oList[0].price)*875.0));
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				pd.dismiss();
				Toast.makeText(ReceiptActivity.this, "Error Loading", Toast.LENGTH_SHORT).show();
			}
		});

		// add it to the RequestQueue 
		pd = ProgressDialog.show(this, "", "Loading Receipt...");
		queue.add(getRequest);


		confirmButton = (Button) findViewById(R.id.confirm_button);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// add it to the RequestQueue 
				//				pd = ProgressDialog.show(ReceiptActivity.this, "", "Paying...");
				totalLayout.setBackgroundColor(getResources().getColor(R.color.red));
				confirmButton.setEnabled(false);
				confirmRequest1();
			}
		});

		cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void confirmRequest1(){

		final RequestQueue queue = Volley.newRequestQueue(ReceiptActivity.this);
		final String url = "http://hardindd.com/stage/bitaurant/api/balance-for-address/"+oList[0].address;

		// prepare the Request
		final StringRequest getRequest = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = Html.fromHtml(response).toString();

				try {
					JSONObject jobj = new JSONObject(response);
					JSONObject data = jobj.getJSONObject("data");
					String balance = data.getString("balance_usd");

					if(Double.parseDouble(balance) >= Double.parseDouble(oList[0].price)){
						pd.dismiss();
						totalLayout.setBackgroundColor(getResources().getColor(R.color.green));
						cancelButton.setText("Done");
						title.setText("Receipt - "+oList[0].title+" (PAID)");
						
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReceiptActivity.this);

			            // Setting Dialog Title
			            alertDialog.setTitle("Payment Received!");

			            // Setting Dialog Message
			            alertDialog.setMessage("Send receipt to email?");
			            final EditText input = new EditText(ReceiptActivity.this);
			            input.setHint("Enter Email");
			            alertDialog.setView(input);

			            // Setting Positive "Yes" Button
			            alertDialog.setPositiveButton("Yes, Please",
			                    new DialogInterface.OnClickListener() {
			                        public void onClick(DialogInterface dialog,int which) {
			                            // Write your code here to execute after dialog
			                            Toast.makeText(getApplicationContext(),"Receipt sent to email.", Toast.LENGTH_SHORT).show();
			                            dialog.dismiss();
			                        }
			                    });
			            // Setting Negative "NO" Button
			            alertDialog.setNegativeButton("No Thanks",
			                    new DialogInterface.OnClickListener() {
			                        public void onClick(DialogInterface dialog, int which) {
			                            // Write your code here to execute after dialog
			                            dialog.cancel();
			                        }
			                    });

			            // closed

			            // Showing Alert Message
			            alertDialog.show();
			            sendPaymentComplete();
						
//						Toast.makeText(ReceiptActivity.this, "Payment Received!", Toast.LENGTH_LONG).show();
					}else{
						final Handler handler = new Handler();
						Thread thread = new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									Thread.sleep(5000);
									handler.post(new Runnable() {

										@Override
										public void run() {
											confirmRequest1();
										}
									});
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
						thread.start();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				//						pd.dismiss();
				confirmButton.setEnabled(false);
				Toast.makeText(ReceiptActivity.this, "Error Loading. Try again.", Toast.LENGTH_SHORT).show();
			}
		});
		
		getRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    
		queue.add(getRequest);
	}
	
	private void sendPaymentComplete(){

		final RequestQueue queue = Volley.newRequestQueue(this);
		final String url = "http://hardindd.com/stage/bitaurant/j/paid?order_id="+id;
		
		final StringRequest getRequest = new StringRequest(url, 
				new Listener<String>() {

			@Override
			public void onResponse(String response) {
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
			}
		});

		// add it to the RequestQueue 
		queue.add(getRequest);
	}
	
}
