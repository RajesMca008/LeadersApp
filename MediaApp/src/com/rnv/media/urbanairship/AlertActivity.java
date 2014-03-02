package com.rnv.media.urbanairship;

 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rnv.media.util.Constants;
import com.rnv.mediaapp.R;
import com.urbanairship.push.PushManager;


public class AlertActivity extends Activity{
	private TextView mAlertMessageTv;
	private Button mView;
	private Button mClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGUI();
		updateGUI();
	}
	private void initGUI() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//hides the default title bar of activity 
		setContentView(R.layout.alert);
		mAlertMessageTv = (TextView)findViewById(R.id.alert_content);
		mView = (Button)findViewById(R.id.alert_view);
		mClose = (Button)findViewById(R.id.alert_close);


		if(Constants.C2DM_ACTIVITY_FOCUS_CHECK){
			mView.setVisibility(View.GONE);
			mView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(AlertActivity.this, SplashActivity.class);
					startActivity(intent);*/
					//finish();
					System.out.println("MODULE 1");
					Toast.makeText(getApplicationContext(), "MODULE 1", Toast.LENGTH_SHORT).show();
				}
			});
		}

		//it works only when the user is not in our Application.


		mClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//finish();
				System.out.println("MODULE 2");
				Toast.makeText(getApplicationContext(), "MODULE 2", Toast.LENGTH_SHORT).show();
			}
		});



	}

	private void updateGUI() {
		//displaying alert message
		if(getIntent()!=null && getIntent().hasExtra(Constants.EXTRA_ALERT_DATA)){
			Intent data = getIntent().getParcelableExtra(Constants.EXTRA_ALERT_DATA);
			if(data!=null){
				if(data.hasExtra(PushManager.EXTRA_ALERT)){
					mAlertMessageTv.setText(data.getStringExtra(PushManager.EXTRA_ALERT));
				}
			}
		}

	}



	/*@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alert_view:
			Intent intent = new Intent(this, Vue.class);
			startActivity(intent);
			finish();
			break;
		case R.id.alert_close:
			finish();
			break;
		default:
			break;
		}

	}*/

}
