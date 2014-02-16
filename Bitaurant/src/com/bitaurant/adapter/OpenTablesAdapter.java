package com.bitaurant.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import com.bitaurant.R;
import com.bitaurant.models.OpenTable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OpenTablesAdapter extends ArrayAdapter<OpenTable> {

	private final Context context;
	private final ArrayList<OpenTable> values;

	SharedPreferences settings;
	SharedPreferences.Editor editor;

	public OpenTablesAdapter(Context context, ArrayList<OpenTable> values) {
		super(context, R.layout.list_item_open_table, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_item_open_table, parent, false);

		TextView title = (TextView) rowView.findViewById(R.id.table_number);
		title.setText(values.get(position).title);

		TextView object = (TextView) rowView.findViewById(R.id.price);
		if(values.get(position).price.equals(""))
			object.setText("");
		else
			object.setText("$"+values.get(position).price);


		return rowView;
	}
}
