package com.bitaurant.activity;
import com.bitaurant.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class CustomerHomeActivity extends BaseActivity{

	Button payButton;
	Button historyButton;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_home);

		payButton = (Button) findViewById(R.id.pay_button);
		historyButton = (Button) findViewById(R.id.receipt_history_button);

		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
				startActivityForResult(intent, 0);

			}
		});

		historyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(CustomerHomeActivity.this, "Receipt History", Toast.LENGTH_LONG).show();

			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
						Intent intent2 = new Intent(CustomerHomeActivity.this, ReceiptActivity.class);
						intent2.putExtra("id", intent.getStringExtra("SCAN_RESULT"));
						startActivity(intent2);
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}
}
