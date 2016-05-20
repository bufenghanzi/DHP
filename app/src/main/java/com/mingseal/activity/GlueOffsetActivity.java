/**
 * 
 */
package com.mingseal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.dhp.R;
import com.mingseal.utils.FloatUtil;
import com.mingseal.utils.MoveUtils;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.WifiConnectTools;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 商炎炳
 *
 */
public class GlueOffsetActivity extends AutoLayoutActivity implements OnClickListener {
	private static final String TAG = "GlueOffsetActivity";

	private TextView tv_title;
	private RelativeLayout rl_back;
	private RelativeLayout rl_title_speed;//标题栏的速度布局
	private RelativeLayout rl_title_moshi;//标题栏的模式布局
	private RelativeLayout rl_title_wifi_connecting;//wifi连接情况
	private ImageView image_speed;//标题栏的速度
	private ImageView image_moshi;//标题栏的模式
	/**
	 * 相对还是绝对
	 */
	private TextView tv_absolute;
	private TextView tv_exchange;
	/**
	 * x轴偏移量
	 */
	private EditText et_offset_x;
	/**
	 * y轴偏移量
	 */
	private EditText et_offset_y;
	/**
	 * z轴偏移量
	 */
	private EditText et_offset_z;
	/**
	 * u轴偏移量
	 */
	private EditText et_offset_u;
	/**
	 * 速度按钮
	 */
	private RelativeLayout rl_speed;
	/**
	 * 模式
	 */
	private RelativeLayout rl_moshi;
	/**
	 * 相对还是绝对
	 */
	private RelativeLayout rl_exchange;
	/**
	 * 完成
	 */
	private RelativeLayout rl_complete;
	/**
	 * @Fields tv_speed: 速度文本
	 */
	private TextView tv_speed;
	/**
	 * @Fields tv_moshi: 模式文本
	 */
	private TextView tv_moshi;

//	/**
//	 * 控制x,y变化的导航杆
//	 */
//	private MyCircleView myCircleUp;
//	/**
//	 * 移动点的导航杆
//	 */
//	private MyCircleView myCircleDown;

	private double position_x;
	private double position_y;
	private double position_z;
	private double position_u;
	//临时保存页面初始化坐标
	private double tem_position_x;
	private double tem_position_y;
	private double tem_position_z;
	private double tem_position_u;

	private int number = 1;// 按下相对/绝对的次数

	private List<Point> points;// 接收从TaskActivity传过来的值
	/**
	 * 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	 */
	private String numberType;//接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	private Point point;

	private int speedFlag = 0;// 判断点击了几次，三次一循环，分别为高速33,中速13,低速3
	private int speed;
	private int[] speedXYZ;// 单步的话保存x,y,z的坐标
	private SettingParam settingParam;// 任务设置参数
	/**
	 * 连续0(默认),单步是1
	 */
	private int modeFlag = 0;// 连续单步判断标志
	private RevHandler handler;

	private Intent intent;
	// private List<Point> pointResults;//偏移之后保存的

	private final static int KEY_X = 0;
	private final static int KEY_Y = 1;
	private final static int KEY_Z = 2;
	private final static int KEY_U = 3;
	
	private UserApplication userApplication;
	/**
	 * @Fields m_nAxisNum : 机器轴数
	 */
	private int m_nAxisNum;
	/**
	 * @Fields but_x_plus: X+
	 */
	private Button but_x_plus;
	/**
	 * @Fields but_x_minus: X-
	 */
	private Button but_x_minus;
	/**
	 * @Fields but_y_plus: Y+
	 */
	private Button but_y_plus;
	/**
	 * @Fields but_y_minus: Y-
	 */
	private Button but_y_minus;
	/**
	 * @Fields but_z_plus: Z+
	 */
	private Button but_z_plus;
	/**
	 * @Fields but_z_minus: Z-
	 */
	private Button but_z_minus;
	/**
	 * @Fields but_u_plus: U+
	 */
	private Button but_u_plus;
	/**
	 * @Fields but_u_minus: U-
	 */
	private Button but_u_minus;

	private ImageView iv_wifi_connecting;//wifi连接情况
	private TextView tv_dianzuobiao;

	private TextView tv_pianyi;
	private TextView tv_offset_x;
	private TextView tv_offset_y;
	private TextView tv_offset_z;
	private TextView tv_offset_u;

	private ImageView iv_sudu;
	private TextView tv_offset_speed;

	private ImageView iv_moshi;
	private TextView tv_offset_moshi;

	private ImageView iv_exchange;

	private ImageView iv_complete;
	private TextView tv_wanchen;

	boolean StopFlag=false;//是否在重发状态
	boolean StopSuccessFlag=false;//停止成功标记
	private int StopRetryTimes=5;//重传次数
	Timer mTimer;
	TimerTask mTimerTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offset);
		userApplication = (UserApplication) getApplication();
		intent = getIntent();
		numberType = intent.getStringExtra(TaskActivity.KEY_NUMBER);
		if("0".equals(numberType)){
			points = userApplication.getPoints();
		}else{
			points = intent.getParcelableArrayListExtra(TaskActivity.ARRAY_KEY);
		}
		settingParam = SharePreferenceUtils.readFromSharedPreference(this);
		initView();

		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
		Log.d(TAG, "收到的Points：" + points.toString());
//		point = new Point(PointType.POINT_NULL);
		// 进入偏移界面，先定位到坐标圆点?
		//应该改为基准点或者第一个点
		MoveUtils.locationCoord(points.get(0));
		m_nAxisNum = RobotParam.INSTANCE.getM_nAxisNum();
		if(m_nAxisNum == 3){
			et_offset_u.setEnabled(false);
			but_u_plus.setVisibility(View.INVISIBLE);
			but_u_minus.setVisibility(View.INVISIBLE);
		}else{
			et_offset_u.setEnabled(true);
			but_u_plus.setVisibility(View.VISIBLE);
			but_u_minus.setVisibility(View.VISIBLE);
		}


	}

	/**
	 * 加载自定义的组件
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_pianyi));
		rl_title_speed = (RelativeLayout) findViewById(R.id.rl_title_speed);
		rl_title_moshi = (RelativeLayout) findViewById(R.id.rl_title_moshi);
		iv_wifi_connecting  =(ImageView) findViewById(R.id.iv_title_wifi_connecting);
		WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
		//让RelativeLayout显示
		rl_title_speed.setVisibility(View.VISIBLE);
		rl_title_moshi.setVisibility(View.VISIBLE);
		image_speed = (ImageView) findViewById(R.id.iv_title_speed);
		image_moshi = (ImageView) findViewById(R.id.iv_title_moshi);
		//设置初值
		image_speed.setBackgroundResource(R.drawable.icon_speed_high);
		image_moshi.setBackgroundResource(R.drawable.icon_step_serious);
		

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_speed = (RelativeLayout) findViewById(R.id.rl_sudu);
		tv_speed = (TextView) rl_speed.findViewById(R.id.tv_offset_speed);
		rl_moshi = (RelativeLayout) findViewById(R.id.rl_moshi);
		tv_moshi = (TextView) rl_moshi.findViewById(R.id.tv_offset_moshi);
		rl_exchange = (RelativeLayout) findViewById(R.id.rl_exchange);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		but_x_plus = (Button) findViewById(R.id.nav_x_plus);
		but_x_minus = (Button) findViewById(R.id.nav_x_minus);
		but_y_plus = (Button) findViewById(R.id.nav_y_plus);
		but_y_minus = (Button) findViewById(R.id.nav_y_minus);
		but_z_plus = (Button) findViewById(R.id.nav_z_plus);
		but_z_minus = (Button) findViewById(R.id.nav_z_minus);
		but_u_plus = (Button) findViewById(R.id.nav_u_plus);
		but_u_minus = (Button) findViewById(R.id.nav_u_minus);
		/*===================== begin =====================*/
		tv_dianzuobiao = (TextView) findViewById(R.id.tv_dianzuobiao);
		tv_absolute = (TextView) findViewById(R.id.tv_absolute);
		tv_pianyi = (TextView) findViewById(R.id.tv_pianyi);
		tv_offset_x = (TextView) findViewById(R.id.tv_offset_x);
		et_offset_x = (EditText) findViewById(R.id.et_offset_x);
		tv_offset_y = (TextView) findViewById(R.id.tv_offset_y);
		et_offset_y = (EditText) findViewById(R.id.et_offset_y);
		tv_offset_z = (TextView) findViewById(R.id.tv_offset_z);
		et_offset_z = (EditText) findViewById(R.id.et_offset_z);
		tv_offset_u = (TextView) findViewById(R.id.tv_offset_u);
		et_offset_u = (EditText) findViewById(R.id.et_offset_u);
		tv_offset_speed = (TextView) findViewById(R.id.tv_offset_speed);
		tv_offset_moshi = (TextView) findViewById(R.id.tv_offset_moshi);
		tv_exchange = (TextView) findViewById(R.id.tv_exchange);
		tv_wanchen = (TextView) findViewById(R.id.tv_wanchen);

		tv_dianzuobiao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_absolute.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_pianyi.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_offset_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_offset_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_u.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_offset_u.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_speed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_moshi.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_exchange.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_wanchen.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));

		but_x_plus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_x_minus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_y_plus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_y_minus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_z_plus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_z_minus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_u_plus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		but_u_minus.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		/*=====================  end =====================*/

		MoveListener moveListener = new MoveListener();
		but_x_plus.setOnTouchListener(moveListener);
		but_x_minus.setOnTouchListener(moveListener);
		but_y_plus.setOnTouchListener(moveListener);
		but_y_minus.setOnTouchListener(moveListener);
		but_z_plus.setOnTouchListener(moveListener);
		but_z_minus.setOnTouchListener(moveListener);
		but_u_plus.setOnTouchListener(moveListener);
		but_u_minus.setOnTouchListener(moveListener);
		
		et_offset_x.setSelectAllOnFocus(true);
		et_offset_y.setSelectAllOnFocus(true);
		et_offset_z.setSelectAllOnFocus(true);
		et_offset_u.setSelectAllOnFocus(true);

		rl_back.setOnClickListener(this);
		rl_speed.setOnClickListener(this);
		rl_moshi.setOnClickListener(this);
		rl_exchange.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

		et_offset_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_offset_x, KEY_X));
		et_offset_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_offset_y, KEY_Y));
		et_offset_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_offset_z, KEY_Z));
		et_offset_u.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_offset_u, KEY_U));

		//初始化x/y/z/u
		et_offset_x.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(points.get(0).getX())));
		tem_position_x = Double.parseDouble(et_offset_x.getText().toString());
		et_offset_y.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(points.get(0).getY())));
		tem_position_y = Double.parseDouble(et_offset_y.getText().toString());
		et_offset_z.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(points.get(0).getZ())));
		tem_position_z = Double.parseDouble(et_offset_z.getText().toString());
		et_offset_u.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.UPulse2Journey(points.get(0).getU())));
		tem_position_u = Double.parseDouble(et_offset_u.getText().toString());
		speed = settingParam.getHighSpeed();
		speedXYZ = new int[3];
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();
	}
	
	/**
	 * @ClassName MoveListener
	 * @Description x,y,z,u控制移动
	 * @author 商炎炳
	 * @date 2016年1月28日 下午3:07:15
	 *
	 */
	private class MoveListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (modeFlag == 0) {
				// 连续
				switch (v.getId()) {
				case R.id.nav_x_plus:// x+
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(0, 0, 0, speed);
						stopTimer();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(0);
						prepareStopRetry(0);
					}
					break;
				case R.id.nav_x_minus:// x-
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(1, 0, 0, speed);
						stopTimer();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(0);
						prepareStopRetry(0);
					}
					break;
				case R.id.nav_y_plus:// y+
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(0, 0, 1, speed);
						stopTimer();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(1);
						prepareStopRetry(1);
					}
					break;
				case R.id.nav_y_minus:// y-
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(1, 0, 1, speed);
						stopTimer();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(1);
						prepareStopRetry(1);
					}
					break;
				case R.id.nav_z_plus:// z+
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(0, 0, 2, speed);
						stopTimer();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(2);
						prepareStopRetry(2);
					}
					break;
				case R.id.nav_z_minus:// z-
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(1, 0, 2, speed);
						stopTimer();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(2);
						prepareStopRetry(2);
					}
					break;
				case R.id.nav_u_plus:// u+
					if (m_nAxisNum == 4) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 0, 3, speed);
							stopTimer();
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(3);
							prepareStopRetry(3);
						}
					}
					break;
				case R.id.nav_u_minus:// u-
					if (m_nAxisNum == 4) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 0, 3, speed);
							stopTimer();
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(3);
							prepareStopRetry(3);
						}
					}
					break;
				}
			} else if (modeFlag == 1) {
				// 单步
				switch (v.getId()) {
				case R.id.nav_x_plus:// x+
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(0, 1, 0, speedXYZ[0]);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(0);
					}
					break;
				case R.id.nav_x_minus:// x-
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(1, 1, 0, speedXYZ[0]);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(0);
					}
					break;
				case R.id.nav_y_plus:// y+
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(0, 1, 1, speedXYZ[1]);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(1);
					}
					break;
				case R.id.nav_y_minus:// y-
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(1, 1, 1, speedXYZ[1]);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(1);
					}
					break;
				case R.id.nav_z_plus:// z+
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(0, 1, 2, speedXYZ[2]);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(2);
					}
					break;
				case R.id.nav_z_minus:// z-
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.move(1, 1, 2, speedXYZ[2]);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						MoveUtils.stop(2);
					}
				case R.id.nav_u_plus:// u+
					if(m_nAxisNum == 4){
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 1, 3, speedXYZ[0]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(3);
						}
					}
					break;
				case R.id.nav_u_minus:// u-
					if(m_nAxisNum == 4){
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 1, 3, speedXYZ[0]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(3);
						}
					}
					break;
				}
			}
			return false;
		}
		
	}
	/**
	 * 将之前的定时任务移除队列
	 */
	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
	/**
	 * 重发停止指令
	 * @param i
	 */
	private void prepareStopRetry(final int i) {
		StopRetryTimes=5;//重新设置重传次数
		StopSuccessFlag=false;//重置标记为
		StopFlag=false;//非重发停止指令状态
		if (mTimer==null){

			mTimer=new Timer();
		}
		if (mTimerTask == null){
			mTimerTask=new TimerTask() {
				@Override
				public void run() {
					if (StopSuccessFlag==false) {
						if (StopRetryTimes > 0) {
							if (StopSuccessFlag == false) {
								StopRetryTimes--;
								MoveUtils.stop(i);
//								Log.d(TAG, "重发了停止指令");
								StopFlag=true;
							}
						}else{
							//重发失败
//							Log.d(TAG,"重发失败！");
							//关闭timer，重置参数
							StopRetryTimes=5;//重新设置重传次数
							StopSuccessFlag=false;//重置标记为
							StopFlag=false;//非重发停止指令状态
							mTimer.cancel();
							mTimer = null;
							mTimerTask.cancel();
							mTimerTask = null;
						}
					}else {
						if (StopFlag){//重发状态
							//成功
//								Log.d(TAG, "重发了停止指令成功！");
							StopSuccessFlag = false;
							StopRetryTimes=5;//重新设置重传次数
							StopSuccessFlag=false;//重置标记为
							StopFlag=false;//非重发停止指令状态
							mTimer.cancel();
							mTimer = null;
							mTimerTask.cancel();
							mTimerTask = null;
						}
					}
				}
			};
		}
		if(mTimer != null && mTimerTask != null ){
			mTimer.schedule(mTimerTask,220,60);
		}
	}
	
	/**
	 * 失去焦点事件
	 *
	 */
	private class OnKeyFocusChangeListener implements OnFocusChangeListener {

		private EditText et;
		private double value;
		private int key;

		/**
		 * 当失去焦点时,如果为空的话，要设置为0
		 * 
		 * @param et
		 *            Edittext
		 */
		public OnKeyFocusChangeListener(EditText et, int key) {
			this.et = et;
			this.key = key;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				((EditText) v).setCursorVisible(false);
				value = Double.parseDouble(et.getText().toString());
				if (key == KEY_X) {
					if (value > RobotParam.INSTANCE.GetXJourney()) {
						value = RobotParam.INSTANCE.GetXJourney();
					} else if (value < 0) {
						value = 0;
					}
				} else if (key == KEY_Y) {
					if (value > RobotParam.INSTANCE.GetYJourney()) {
						value = RobotParam.INSTANCE.GetYJourney();
					} else if (value < 0) {
						value = 0;
					}
				} else if (key == KEY_Z) {
					if (value > RobotParam.INSTANCE.GetZJourney()) {
						value = RobotParam.INSTANCE.GetZJourney();
					} else if (value < 0) {
						value = 0;
					}
				} else if (key == KEY_U) {
					if (value > RobotParam.INSTANCE.GetUJourney()) {
						value = RobotParam.INSTANCE.GetUJourney();
					} else if (value < 0) {
						value = 0;
					}
				}
				et.setText(value + "");
			}
		}

	}

	/**
	 * 保存并返回TaskActivity
	 */
	private void saveBackActivity() {
		if (checkAllComponents()) {
			
			position_x = Double.parseDouble(et_offset_x.getText().toString())-tem_position_x;
			position_y = Double.parseDouble(et_offset_y.getText().toString())-tem_position_y;
			position_z = Double.parseDouble(et_offset_z.getText().toString())-tem_position_z;
			position_u = Double.parseDouble(et_offset_u.getText().toString())-tem_position_u;
			if (tv_exchange.getText().toString().equals(getResources().getString(R.string.relative))) {
				// 相对偏移,点坐标=基准点偏移后位置-基准点原位置+点的原位置
				for (Point point : points) {
					Log.d(TAG, "相对偏移（没偏移之前）：" + point.toString());
					if (point.getPointParam().getPointType().equals(PointType.POINT_GLUE_BASE)){
						Log.d(TAG, "相对偏移（没偏移之前）基准点不操作");
					}else {
						point.setX(RobotParam.INSTANCE
								.XJourney2Pulse(RobotParam.INSTANCE.XPulse2Journey(point.getX()) +position_x));
						point.setY(RobotParam.INSTANCE
								.YJourney2Pulse(RobotParam.INSTANCE.YPulse2Journey(point.getY()) + position_y));
						point.setZ(RobotParam.INSTANCE
								.ZJourney2Pulse(RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) + position_z));
						point.setU(RobotParam.INSTANCE
								.UJourney2Pulse(RobotParam.INSTANCE.UPulse2Journey(point.getU()) + position_u));
					}
				}
				Log.d(TAG, "相对偏移：" + points.toString());
			} else if (tv_exchange.getText().toString().equals(getResources().getString(R.string.absolute))) {
				// 绝对偏移
				for (Point point : points) {
					if (point.getPointParam().getPointType().equals(PointType.POINT_GLUE_BASE)){
						Log.d(TAG, "绝对偏移（没偏移之前）基准点不操作");
					}else {
						point.setX(RobotParam.INSTANCE.XJourney2Pulse(position_x));
						point.setY(RobotParam.INSTANCE.YJourney2Pulse(position_y));
						point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(position_z));
						point.setU(RobotParam.INSTANCE.UJourney2Pulse(position_u));
					}
				}
				Log.d(TAG, "绝对偏移：" + points.toString());
			}

			Bundle extras = new Bundle();
			if("0".equals(numberType)){
				extras.putString(TaskActivity.KEY_NUMBER, "0");
				userApplication.setPoints(points);
			}else{
				extras.putString(TaskActivity.KEY_NUMBER, "1");
				extras.putParcelableArrayList(TaskActivity.OFFSET_KEY, (ArrayList<? extends Parcelable>) points);
			}

			intent.putExtras(extras);
			setResult(TaskActivity.resultOffsetCode, intent);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
		}
	}

	/**
	 * 检查所有输入框是否为空
	 * 
	 * @return true代表正常,false代表有异常
	 */
	private boolean checkAllComponents() {
		String str = "";
		if (et_offset_x.getText().toString().equals("")) {
			str += "x" + getResources().getString(R.string.not_null);
		} else if (et_offset_y.getText().toString().equals("")) {
			str += "y" + getResources().getString(R.string.not_null);
		} else if (et_offset_z.getText().toString().equals("")) {
			str += "z" + getResources().getString(R.string.not_null);
		} else if (et_offset_u.getText().toString().equals("")) {
			str += "u" + getResources().getString(R.string.not_null);
		}

		if (str.equals("")) {
			return true;
		} else {
			ToastUtil.displayPromptInfo(this, str);
			return false;
		}
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueOffsetActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_offset));
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
				GlueOffsetActivity.this.finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
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

	private void DisPlayInfoAfterGetMsg(byte[] revBuffer) {
		int retValue = MessageMgr.INSTANCE.managingMessage(revBuffer);
//		Log.d(TAG, "retValue:"+retValue);
		switch (retValue) {
		case 0:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "校验失败");
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
//			Log.d(TAG, "cmdFlag:"+cmdFlag);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);
				Log.d(TAG, "返回的Point:"+coordPoint.toString()+","+RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX()));
				StopSuccessFlag=true;//说明下位机成功返回消息
				StopRetryTimes=5;//重新设置重传次数
				et_offset_x.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX())));
				et_offset_y.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(coordPoint.getY())));
				et_offset_z.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(coordPoint.getZ())));
				et_offset_u.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.UPulse2Journey(coordPoint.getU())));
				
			}
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "任务上传失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "任务下载失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(GlueOffsetActivity.this, "未知错误");
			break;
		}
	}

	private class RevHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// 如果消息来自子线程
			if (msg.what == SocketInputThread.SocketInputWhat) {
				// 获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer) msg.obj;
				byte[] buffer;
				buffer = temp.array();
				// byte[] revBuffer = (byte[]) msg.obj;
				DisPlayInfoAfterGetMsg(buffer);
			}else if (msg.what==SocketInputThread.SocketError){
				//wifi中断
				System.out.println("wifi连接断开。。");
				SocketThreadManager.releaseInstance();
				System.out.println("单例被释放了-----------------------------");
				//设置全局变量，跟新ui
				userApplication.setWifiConnecting(false);
				WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
				ToastUtil.displayPromptInfo(GlueOffsetActivity.this,"wifi连接断开。。");
			}
		}
	}
	
	/**
	 * @Title saveMediumSpeed
	 * @Description 单步和连续的速度都改成中速的，且将文本提示设置成中速
	 * @param settingParam
	 */
	private void saveMediumSpeed(SettingParam settingParam) {
		speed = settingParam.getMediumSpeed();
		speedXYZ[0] = 2 * settingParam.getxStepDistance();
		speedXYZ[1] = 2 * settingParam.getyStepDistance();
		speedXYZ[2] = 2 * settingParam.getzStepDistance();
		ToastUtil.displayPromptInfo(this, getResources().getString(R.string.activity_medium));
		image_speed.setBackgroundResource(R.drawable.icon_speed_medium);
		tv_speed.setText(getResources().getString(R.string.activity_medium));
	}

	/**
	 * @Title saveLowSpeed
	 * @Description 单步和连续的速度都改成低速的，且将文本提示设置成低速
	 * @param settingParam
	 */
	private void saveLowSpeed(SettingParam settingParam) {
		speed = settingParam.getLowSpeed();
		speedXYZ[0] = 1 * settingParam.getxStepDistance();
		speedXYZ[1] = 1 * settingParam.getyStepDistance();
		speedXYZ[2] = 1 * settingParam.getzStepDistance();
		ToastUtil.displayPromptInfo(this, getResources().getString(R.string.activity_low));
		image_speed.setBackgroundResource(R.drawable.icon_speed_low);
		tv_speed.setText(getResources().getString(R.string.activity_low));
	}

	/**
	 * @Title saveHighSpeed
	 * @Description 单步和连续的速度都改成高速的，且将文本提示设置成高速
	 * @param settingParam
	 */
	private void saveHighSpeed(SettingParam settingParam) {
		speed = settingParam.getHighSpeed();
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();
		ToastUtil.displayPromptInfo(this, getResources().getString(R.string.activity_high));
		image_speed.setBackgroundResource(R.drawable.icon_speed_high);
		tv_speed.setText(getResources().getString(R.string.activity_high));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:// 返回
			showBackDialog();

			break;
		case R.id.rl_sudu:// 速度
			speedFlag++;
			if (speedFlag % 3 == 1) {
				saveMediumSpeed(settingParam);
			} else if (speedFlag % 3 == 2) {
				saveLowSpeed(settingParam);
			} else if (speedFlag % 3 == 0) {
				saveHighSpeed(settingParam);
			}
			break;
		case R.id.rl_moshi:// 模式
			if (modeFlag == 0) {
				modeFlag = 1;// 点击一次变为单步
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.step_single));
				image_moshi.setBackgroundResource(R.drawable.icon_step_single);
				tv_moshi.setText(getResources().getString(R.string.step_single));
			} else {
				modeFlag = 0;// 默认为0
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.step_serious));
				image_moshi.setBackgroundResource(R.drawable.icon_step_serious);
				tv_moshi.setText(getResources().getString(R.string.step_serious));
			}
			break;
		case R.id.rl_exchange:// 交换相对还是绝对
			number++;
			if (number % 2 == 1) {
				tv_exchange.setText(getResources().getString(R.string.relative));
				tv_absolute.setText(getResources().getString(R.string.relative));
			} else {
				tv_exchange.setText(getResources().getString(R.string.absolute));
				tv_absolute.setText(getResources().getString(R.string.absolute));
			}
			break;
		case R.id.rl_complete:// 完成
			showBackDialog();
			break;
		}
	}
}
