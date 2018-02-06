package com.newinspections.pso.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.newinspections.pso.R;
import com.squareup.picasso.Picasso;

public class FullViewActivity extends AppCompatActivity {
	Bundle bundle;
	String fileName="";
	private Toolbar toolBar;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		setContentView(R.layout.fullview_activity);
		toolBar = (Toolbar) findViewById(R.id.toolBar_tankRding);
		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		bundle =getIntent().getExtras();
		if (bundle != null) {
			fileName= bundle.getString("fileName");
		}
		TouchImageView iv = (TouchImageView) findViewById(R.id.tiv_image);
		try {

			Log.i("fileName ","fileName "+fileName);
			Context context=iv.getContext();
			Picasso.with(context)
					.load(fileName).placeholder(R.drawable.loader)
					.into(iv);
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();

		}catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//finish();
		super.onBackPressed();
		return true;
	}
}
