package com.bitaurant.activity;
import com.bitaurant.R;
import com.bitaurant.qrcode.Contents;
import com.bitaurant.qrcode.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class QRCodeActivity extends BaseActivity{

	ImageView qrCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		
		qrCode = (ImageView) findViewById(R.id.qr_code);
		
		Bundle extras = getIntent().getExtras();
		
		String qrData = extras.getString("id");
		int qrCodeDimention = 600;

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
		        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

		try {
		    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
		    qrCode.setImageBitmap(bitmap);
		} catch (WriterException e) {
		    e.printStackTrace();
		}
		
	}
}
