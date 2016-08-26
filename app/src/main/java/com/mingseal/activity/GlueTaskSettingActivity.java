/**
 * 
 */
package com.mingseal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.param.SettingParam;
import com.mingseal.dhp.R;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @title 任务设置属性
 * @author 商炎炳
 *
 */
public class GlueTaskSettingActivity extends AutoLayoutActivity implements OnClickListener{

	private String TAG = "GlueTaskSettingActivity";
	/**
	 * x轴步距
	 */
	private NumberPicker num_xDistance;
	/**
	 * y轴步距
	 */
	private NumberPicker num_yDistance;
	/**
	 * z轴步距
	 */
	private NumberPicker num_zDistance;
	/**
	 * 高速度
	 */
	private NumberPicker num_highSpeed;
	/**
	 * 中速度
	 */
	private NumberPicker num_mediumSpeed;
	/**
	 * 低速度
	 */
	private NumberPicker num_lowSpeed;
	/**
	 * 循迹速度
	 */
	private NumberPicker num_trackSpeed;
	/**
	 * 循迹定位
	 */
	private Switch sw_location;
	/**
	 * 返回
	 */
	private RelativeLayout rl_back;
	/**
	 * 完成
	 */
	private RelativeLayout rl_complete;
	/**
	 * 标题
	 */
	private TextView tv_title;

	private SettingParam setting;// 任务设置参数

	private TextView tv_canshushezhi;
	private TextView tv_xDistance;

	private TextView tv_yDistance;

	private TextView tv_zDistance;

	private TextView tv_highSpeed;

	private TextView tv_mms;
	private TextView tv_mediumSpeed;

	private TextView tv_mms2;
	private TextView tv_lowSpeed;

	private TextView tv_mms3;
	private TextView tv_trackLocation;

	private TextView tv_trackSpeed;

	private TextView tv_mms4;

	private RevHandler handler;
	private TextView tv_wanchen;
	private UserApplication userApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		userApplication = (UserApplication) getApplication();
		setting = SharePreferenceUtils.readFromSharedPreference(this);
//		System.out.println("任务设置-------->"+setting);
		initView();
		handler = new RevHandler();
	}

	/**
	 * 加载组件
	 */
	private void initView() {
		num_xDistance = (NumberPicker) findViewById(R.id.num_xDistance);
		num_yDistance = (NumberPicker) findViewById(R.id.num_yDistance);
		num_zDistance = (NumberPicker) findViewById(R.id.num_zDistance);
		num_highSpeed = (NumberPicker) findViewById(R.id.num_highSpeed);
		num_mediumSpeed = (NumberPicker) findViewById(R.id.num_mediumSpeed);
		num_lowSpeed = (NumberPicker) findViewById(R.id.num_lowSpeed);
		num_trackSpeed = (NumberPicker) findViewById(R.id.num_trackSpeed);
		sw_location = (Switch) findViewById(R.id.switch_trackLocation);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_task_set));
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		/*===================== begin =====================*/
		tv_canshushezhi = (TextView) findViewById(R.id.tv_canshushezhi);
		tv_xDistance = (TextView) findViewById(R.id.tv_xDistance);
		tv_yDistance = (TextView) findViewById(R.id.tv_yDistance);
		tv_zDistance = (TextView) findViewById(R.id.tv_zDistance);
		tv_highSpeed = (TextView) findViewById(R.id.tv_highSpeed);
		tv_mms = (TextView) findViewById(R.id.tv_mms);
		tv_mediumSpeed = (TextView) findViewById(R.id.tv_mediumSpeed);
		tv_mms2 = (TextView) findViewById(R.id.tv_mms2);
		tv_lowSpeed = (TextView) findViewById(R.id.tv_lowSpeed);
		tv_mms3 = (TextView) findViewById(R.id.tv_mms3);
		tv_trackLocation = (TextView) findViewById(R.id.tv_trackLocation);
		tv_trackSpeed = (TextView) findViewById(R.id.tv_trackSpeed);
		tv_mms4 = (TextView) findViewById(R.id.tv_mms4);
		tv_wanchen = (TextView) findViewById(R.id.tv_wanchen);
		tv_canshushezhi.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_xDistance.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_yDistance.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_zDistance.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_highSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_mms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_mediumSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_mms2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_lowSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_mms3.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_trackLocation.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_trackSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_mms4.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_wanchen.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));

		/*=====================  end =====================*/

		//设置NumberPicker的最大最小值
		//1-10(1)
		num_xDistance.setMaxValue(10);
		num_xDistance.setMinValue(1);
		num_xDistance.setValue(setting.getxStepDistance());
		//1-10(1)
		num_yDistance.setMaxValue(10);
		num_yDistance.setMinValue(1);
		num_yDistance.setValue(setting.getyStepDistance());
		//1-10(1)
		num_zDistance.setMaxValue(10);
		num_zDistance.setMinValue(1);
		num_zDistance.setValue(setting.getzStepDistance());
		//31-50(33)
		num_highSpeed.setMaxValue(50);
		num_highSpeed.setMinValue(31);
		num_highSpeed.setValue(setting.getHighSpeed());
		//11-30(13)
		num_mediumSpeed.setMaxValue(30);
		num_mediumSpeed.setMinValue(11);
		num_mediumSpeed.setValue(setting.getMediumSpeed());
		//2-10(3)
		num_lowSpeed.setMaxValue(10);
		num_lowSpeed.setMinValue(2);
		num_lowSpeed.setValue(setting.getLowSpeed());
		//1-100(50)
		num_trackSpeed.setMaxValue(100);
		num_trackSpeed.setMinValue(1);
		num_trackSpeed.setValue(setting.getTrackSpeed());
		sw_location.setChecked(setting.isTrackLocation());
		
		rl_back.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

	}
	
	/** 将页面上的数据保存到SettingParam对象中
	 * @return SettingParam
	 */
	private SettingParam getParam(){
		setting = new SettingParam();
		
		setting.setxStepDistance(num_xDistance.getValue());
		setting.setyStepDistance(num_yDistance.getValue());
		setting.setzStepDistance(num_zDistance.getValue());
		setting.setHighSpeed(num_highSpeed.getValue());
		setting.setMediumSpeed(num_mediumSpeed.getValue());
		setting.setLowSpeed(num_lowSpeed.getValue());
		setting.setTrackSpeed(num_trackSpeed.getValue());
		setting.setTrackLocation(sw_location.isChecked());
		
		return setting;
	}
	
	/**
	 * 保存页面中的信息并返回到之前的TaskActivity
	 */
	private void saveBackActivity() {
		
		setting = getParam();
		SharePreferenceUtils.saveToSharedPreferences(this, setting);
		setResult(TaskActivity.resultSettingCode);
		finish();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
	}
	
	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueTaskSettingActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_save));
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setPositiveButton(getResources().getString(R.string.need_yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				saveBackActivity();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.is_need_no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				GlueTaskSettingActivity.this.finish();
			}
		});
		builder.setNeutralButton(getResources().getString(R.string.is_need_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			showBackDialog();
			break;
		case R.id.rl_complete:
			saveBackActivity();
			break;
		}
	}
	private class RevHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			 if (msg.what==SocketInputThread.SocketError){
				//wifi中断
				SocketThreadManager.releaseInstance();
				//设置全局变量，跟新ui
				userApplication.setWifiConnecting(false);
//				WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
				ToastUtil.displayPromptInfo(GlueTaskSettingActivity.this,"wifi连接断开。。");
			}
		}
	}

}
