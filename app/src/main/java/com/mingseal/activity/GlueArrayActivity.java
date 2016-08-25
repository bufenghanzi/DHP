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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mingseal.adapter.TestArrayAdapter;
import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.ArrayParam;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.SMatrix1_4;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditFloatWatcher;
import com.mingseal.listener.MaxMinFocusChangeFloatListener;
import com.mingseal.utils.ArrayArithmetic;
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
 * 阵列
 * 
 * @author 商炎炳
 *
 */
public class 	GlueArrayActivity extends AutoLayoutActivity implements OnClickListener {

//	private MyCircleView myCircleUp;
//	private MyCircleView myCircleDown;

	private TextView tv_title;
	private RelativeLayout rl_back;
	private RelativeLayout rl_title_speed;// 标题栏的速度布局
	private RelativeLayout rl_title_moshi;// 标题栏的模式布局
	private RelativeLayout rl_title_wifi_connecting;// wifi连接情况
	private ImageView image_speed;// 标题栏的速度
	private ImageView image_moshi;// 标题栏的模式

	/**
	 * 速度按钮
	 */
	private RelativeLayout rl_speed;
	/**
	 * 模式
	 */
	private RelativeLayout rl_moshi;
	/**
	 * 完成
	 */
	private RelativeLayout rl_complete;
	/**
	 * @Fields tv_speed: 速度的文本
	 */
	private TextView tv_speed;
	/**
	 * @Fields tv_moshi: 模式的文本
	 */
	private TextView tv_moshi;
	/**
	 * 行数
	 */
	private EditText rowEdit;
	/**
	 * 列数
	 */
	private EditText columnEdit;
	/**
	 * 阵列方式
	 */
	private Spinner arraySpinner;

	/**
	 * X轴方向偏移x
	 */
	private EditText et_x_offset_x;
	/**
	 * X轴方向偏移y
	 */
	private EditText et_x_offset_y;
	/**
	 * X轴方向偏移z
	 */
	private EditText et_x_offset_z;
	/**
	 * Y轴方向偏移x
	 */
	private EditText et_y_offset_x;
	/**
	 * Y轴方向偏移y
	 */
	private EditText et_y_offset_y;
	/**
	 * Y轴方向偏移z
	 */
	private EditText et_y_offset_z;
	/**
	 * 终点x坐标
	 */
	private EditText et_end_x;
	/**
	 * 终点y坐标
	 */
	private EditText et_end_y;
	/**
	 * 终点z坐标
	 */
	private EditText et_end_z;

	private LinearLayout linear_x;// X轴方向偏移的LineayLayout
	private LinearLayout linear_y;// Y轴方向偏移的LineayLayout
	private LinearLayout linear_e;// 终点坐标偏移的LineayLayout
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

	private final static int KEY_X_X = 0;
	private final static int KEY_X_Y = 1;
	private final static int KEY_X_Z = 2;
	private final static int KEY_Y_X = 3;
	private final static int KEY_Y_Y = 4;
	private final static int KEY_Y_Z = 5;
	private final static int KEY_E_X = 6;
	private final static int KEY_E_Y = 7;
	private final static int KEY_E_Z = 8;
	private final static int ROW = 9;
	private final static int COLUMN = 10;

	private ArrayAdapter adapterSpinner;
	private List<Point> points;// 接收从TaskActivity传递过来的数据
	/**
	 * 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	 */
	private String numberType;// 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	private Point mPoint;//基准点
	private Point xPoint;//x轴临时点
	private Point yPoint;//y轴临时点
	private Point ePoint;//终点临时点

	private ArrayParam arrayParam;
	private SMatrix1_4 sMatrix_x;
	private SMatrix1_4 sMatrix_y;
	private SMatrix1_4 sMatrix_e;

	/**
	 * 从TaskActivity传过来的Intent
	 */
	private Intent intent;
	private static String TAG = "GlueArrayActivity";

	private String prompt = "";// 提示信息
	/**
	 * 判断当前的焦点在哪个输入框(0是x轴偏移,1是y轴偏移,2是终点坐标)
	 */
	private int selectFocus = 0;// 判断当前的焦点在哪个输入框(0是x轴偏移,1是y轴偏移,2是终点坐标)
	private int selectLastFocus = -1;// 用于判断焦点框是否变化,如果变化了的话,就需要先定位

	private int speedFlag = 0;// 判断点击了几次，三次一循环，分别为高速33,中速13,低速3
	private int speed;
	private int[] speedXYZ;// 单步的话保存x,y,z的坐标
	private SettingParam settingParam;// 任务设置参数
	/**
	 * @Fields m_nAxisNum : 机器轴数
	 */
	private int m_nAxisNum;
	/**
	 * 连续0(默认),单步是1
	 */
	private int modeFlag = 0;// 连续单步判断标志
	private RevHandler handler;
	private UserApplication userApplication;
	private ImageView iv_wifi_connecting;//wifi连接情况
// Content View Elements

	private TextView tv_canshu;
	private TextView tv_row;
	private EditText et_row;
	private EditText et_column;
	private TextView tv_column;
	private TextView tv_array;
	private Spinner sp_array;

	private TextView tv_x_offset;
	private TextView tv_x_offset_x;
	private TextView tv_x_offset_y;
	private TextView tv_x_offset_z;

	private TextView tv_offset_y;
	private TextView tv_y_offset_x;
	private TextView tv_y_offset_y;
	private TextView tv_y_offset_z;

	private TextView tv_zhongdian;
	private TextView tv_end_x;
	private TextView tv_end_y;
	private TextView tv_end_z;

	private ImageView iv_sudu;
	private TextView tv_array_speed;

	private ImageView iv_moshi;
	private TextView tv_array_moshi;

	private ImageView iv_complete;
	private TextView tv_wanchen;
	private int locateFirst=0;//判断是否需要先定位,0:初始值，1：需要先定位（编辑了edittext），2：不需要定位（获取的新坐标）
	boolean StopFlag=false;//是否在重发状态
	boolean StopSuccessFlag=false;//停止成功标记
	private int StopRetryTimes=5;//重传次数
	Timer mTimer;
	TimerTask mTimerTask;
	private Double mLast_x_x;
	private Double mLast_x_y;
	private Double mLast_x_z;
	private Double mLast_y_x;
	private Double mLast_y_y;
	private Double mLast_y_z;
	private Double mLast_e_x;
	private Double mLast_e_y;
	private Double mLast_e_z;

	// End Of Content View Elements
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_array);

		userApplication = (UserApplication) getApplication();
		intent = getIntent();
		numberType = intent.getStringExtra(TaskActivity.KEY_NUMBER);
		if ("0".equals(numberType)) {
			points = userApplication.getPoints();
		} else if ("1".equals(numberType)) {
			points = intent.getParcelableArrayListExtra(TaskActivity.ARRAY_KEY);
		}
		// Log.d(TAG, "numberType:"+numberType+",长度:"+points.size());
		settingParam = SharePreferenceUtils.readFromSharedPreference(this);
		initView();
		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
		m_nAxisNum = RobotParam.INSTANCE.getM_nAxisNum();
		// 进入偏移界面，先定位到第一个点
		mPoint=points.get(0);
		System.out.println("基准点x轴坐标："+mPoint.getX());
		xPoint=new Point(PointType.POINT_NULL);
		xPoint.setX(mPoint.getX());
		xPoint.setY(mPoint.getY());
		xPoint.setZ(mPoint.getZ());
		yPoint=new Point(PointType.POINT_NULL);
		yPoint.setX(mPoint.getX());
		yPoint.setY(mPoint.getY());
		yPoint.setZ(mPoint.getZ());
		ePoint=new Point(PointType.POINT_NULL);
		ePoint.setX(mPoint.getX());
		ePoint.setY(mPoint.getY());
		ePoint.setZ(mPoint.getZ());
		MoveUtils.locationCoord(mPoint);
		if (m_nAxisNum == 3) {
//			myCircleDown.setRow("");
			but_u_minus.setVisibility(View.INVISIBLE);
			but_u_plus.setVisibility(View.INVISIBLE);
			et_end_x.setEnabled(false);
			et_end_y.setEnabled(false);
			et_end_z.setEnabled(false);
			et_end_x.setFocusable(false);
			et_end_y.setFocusable(false);
			et_end_z.setFocusable(false);
		}else if(m_nAxisNum ==4){
			but_u_minus.setVisibility(View.VISIBLE);
			but_u_plus.setVisibility(View.VISIBLE);
			et_end_x.setEnabled(true);
			et_end_y.setEnabled(true);
			et_end_z.setEnabled(true);
			et_end_x.setFocusable(true);
			et_end_y.setFocusable(true);
			et_end_z.setFocusable(true);
		}

		// 初始化
		arrayParam = new ArrayParam();
		 //使用自定义的ArrayAdapter  
		adapterSpinner = new TestArrayAdapter(GlueArrayActivity.this,getResources().getStringArray(R.array.arrayMethods));  
//		// 将数组内容与ArrayAdapter连接起来
//		adapterSpinner = ArrayAdapter.createFromResource(this, R.array.arrayMethods,
//				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到Spinner中
		arraySpinner.setAdapter(adapterSpinner);
		// 添加事件监听
		arraySpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		arraySpinner.setVisibility(View.VISIBLE);

		et_x_offset_x.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_x_offset_x, KEY_X_X));
		et_x_offset_y.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_x_offset_y, KEY_X_Y));
		et_x_offset_z.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_x_offset_z, KEY_X_Z));
		et_y_offset_x.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_y_offset_x, KEY_Y_X));
		et_y_offset_y.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_y_offset_y, KEY_Y_Y));
		et_y_offset_z.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_y_offset_z, KEY_Y_Z));
		et_end_x.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_end_x, KEY_E_X));
		et_end_y.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_end_y, KEY_E_Y));
		et_end_z.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_end_z, KEY_E_Z));

		et_x_offset_x
				.addTextChangedListener(new MaxMinEditFloatWatcher(
						RobotParam.INSTANCE.GetXJourney()-mPoint.getX(),
						-mPoint.getX(), et_x_offset_x));
		et_x_offset_x
				.setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(
						RobotParam.INSTANCE.GetXJourney()-mPoint.getX(),
						-mPoint.getX(), et_x_offset_x));

		et_x_offset_y
				.addTextChangedListener(new MaxMinEditFloatWatcher(
						RobotParam.INSTANCE.GetYJourney()-mPoint.getY(),
						-mPoint.getY(), et_x_offset_y));
		et_x_offset_y
				.setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(
						RobotParam.INSTANCE.GetYJourney()-mPoint.getY(),
						-mPoint.getY(), et_x_offset_y));

		et_x_offset_z
				.addTextChangedListener(new MaxMinEditFloatWatcher(
						RobotParam.INSTANCE.GetZJourney()-mPoint.getZ(),
						-mPoint.getZ(), et_x_offset_z));
		et_x_offset_z
				.setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(
						RobotParam.INSTANCE.GetZJourney()-mPoint.getZ(),
						-mPoint.getZ(), et_x_offset_z));

		et_y_offset_x
				.addTextChangedListener(new MaxMinEditFloatWatcher(
						RobotParam.INSTANCE.GetXJourney()-mPoint.getX(),
						-mPoint.getX(), et_y_offset_x));
		et_y_offset_x
				.setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(
						RobotParam.INSTANCE.GetXJourney()-mPoint.getX(),
						-mPoint.getX(), et_y_offset_x));

		et_y_offset_y
				.addTextChangedListener(new MaxMinEditFloatWatcher(
						RobotParam.INSTANCE.GetYJourney()-mPoint.getY(),
						-mPoint.getY(), et_y_offset_y));
		et_y_offset_y
				.setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(
						RobotParam.INSTANCE.GetYJourney()-mPoint.getY(),
						-mPoint.getY(), et_y_offset_y));

		et_y_offset_z
				.addTextChangedListener(new MaxMinEditFloatWatcher(
						RobotParam.INSTANCE.GetZJourney()-mPoint.getZ(),
						-mPoint.getZ(), et_y_offset_z));
		et_y_offset_z
				.setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(
						RobotParam.INSTANCE.GetZJourney()-mPoint.getZ(),
						-mPoint.getZ(), et_y_offset_z));

//		et_x_offset_x.addTextChangedListener(mTextWatcher);
//		et_x_offset_y.addTextChangedListener(mTextWatcher);
//		et_x_offset_z.addTextChangedListener(mTextWatcher);
//		et_y_offset_x.addTextChangedListener(mTextWatcher);
//		et_y_offset_y.addTextChangedListener(mTextWatcher);
//		et_y_offset_z.addTextChangedListener(mTextWatcher);


		et_x_offset_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_x_offset_x, KEY_X_X));
		et_x_offset_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_x_offset_y, KEY_X_Y));
		et_x_offset_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_x_offset_z, KEY_X_Z));
		et_y_offset_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_y_offset_x, KEY_Y_X));
		et_y_offset_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_y_offset_y, KEY_Y_Y));
		et_y_offset_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_y_offset_z, KEY_Y_Z));
		et_end_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_end_x, KEY_E_X));
		et_end_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_end_y, KEY_E_Y));
		et_end_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_end_z, KEY_E_Z));
		rowEdit.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, rowEdit, ROW));
		columnEdit.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, columnEdit, COLUMN));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

//	//监听edittext变化，如果变化了先定位
//	private TextWatcher mTextWatcher=new TextWatcher() {
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			locateFirst=1;
//
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//				locateFirst=1;
//		}
//	};
	/**
	 * 判断Edittext输入框里面是否有数据,如果没有数据则置为0
	 * 
	 * @param edittext
	 * @return double数据
	 */
	private double getStringToDouble(EditText edittext) {
		String str = edittext.getText().toString();
		if (str.equals("")) {
			str = "0";
			edittext.setText(str);
		}
		double result = Double.parseDouble(str);
		return result;
	}

	/**
	 * 加载自定义控件
	 */
	private void initView() {

		but_x_plus = (Button) findViewById(R.id.nav_x_plus);
		but_x_minus = (Button) findViewById(R.id.nav_x_minus);
		but_y_plus = (Button) findViewById(R.id.nav_y_plus);
		but_y_minus = (Button) findViewById(R.id.nav_y_minus);
		but_z_plus = (Button) findViewById(R.id.nav_z_plus);
		but_z_minus = (Button) findViewById(R.id.nav_z_minus);
		but_u_plus = (Button) findViewById(R.id.nav_u_plus);
		but_u_minus = (Button) findViewById(R.id.nav_u_minus);

		/*===================== begin =====================*/
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

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_array));

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_title_speed = (RelativeLayout) findViewById(R.id.rl_title_speed);
		rl_title_moshi = (RelativeLayout) findViewById(R.id.rl_title_moshi);
		iv_wifi_connecting  =(ImageView) findViewById(R.id.iv_title_wifi_connecting);
		WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
		// 让RelativeLayout显示
		rl_title_speed.setVisibility(View.VISIBLE);
		rl_title_moshi.setVisibility(View.VISIBLE);
		image_speed = (ImageView) findViewById(R.id.iv_title_speed);
		image_moshi = (ImageView) findViewById(R.id.iv_title_moshi);
		// 设置初值
		image_speed.setBackgroundResource(R.drawable.icon_speed_high);
		image_moshi.setBackgroundResource(R.drawable.icon_step_serious);

		rowEdit = (EditText) findViewById(R.id.et_row);
		columnEdit = (EditText) findViewById(R.id.et_column);
		arraySpinner = (Spinner) findViewById(R.id.sp_array);
		rl_speed = (RelativeLayout) findViewById(R.id.rl_sudu);
		rl_moshi = (RelativeLayout) findViewById(R.id.rl_moshi);
		tv_speed = (TextView) rl_speed.findViewById(R.id.tv_array_speed);
		tv_moshi = (TextView) rl_moshi.findViewById(R.id.tv_array_moshi);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		et_x_offset_x = (EditText) findViewById(R.id.et_x_offset_x);
		et_x_offset_y = (EditText) findViewById(R.id.et_x_offset_y);
		et_x_offset_z = (EditText) findViewById(R.id.et_x_offset_z);
		et_y_offset_x = (EditText) findViewById(R.id.et_y_offset_x);
		et_y_offset_y = (EditText) findViewById(R.id.et_y_offset_y);
		et_y_offset_z = (EditText) findViewById(R.id.et_y_offset_z);
		et_end_x = (EditText) findViewById(R.id.et_end_x);
		et_end_y = (EditText) findViewById(R.id.et_end_y);
		et_end_z = (EditText) findViewById(R.id.et_end_z);
/*===================== begin =====================*/
		tv_canshu = (TextView) findViewById(R.id.tv_canshu);
		tv_row = (TextView) findViewById(R.id.tv_row);
		tv_column = (TextView) findViewById(R.id.tv_column);
		tv_array = (TextView) findViewById(R.id.tv_array);

		tv_x_offset = (TextView) findViewById(R.id.tv_x_offset);
		tv_x_offset_x = (TextView) findViewById(R.id.tv_x_offset_x);
		tv_x_offset_y = (TextView) findViewById(R.id.tv_x_offset_y);
		tv_x_offset_z = (TextView) findViewById(R.id.tv_x_offset_z);

		tv_offset_y = (TextView) findViewById(R.id.tv_offset_y);
		tv_y_offset_x = (TextView) findViewById(R.id.tv_y_offset_x);
		tv_y_offset_y = (TextView) findViewById(R.id.tv_y_offset_y);
		tv_y_offset_z = (TextView) findViewById(R.id.tv_y_offset_z);

		tv_zhongdian = (TextView) findViewById(R.id.tv_zhongdian);
		tv_end_x = (TextView) findViewById(R.id.tv_end_x);
		et_end_x = (EditText) findViewById(R.id.et_end_x);
		tv_end_y = (TextView) findViewById(R.id.tv_end_y);
		et_end_y = (EditText) findViewById(R.id.et_end_y);
		tv_end_z = (TextView) findViewById(R.id.tv_end_z);
		et_end_z = (EditText) findViewById(R.id.et_end_z);

		tv_array_speed = (TextView) findViewById(R.id.tv_array_speed);

		tv_array_moshi = (TextView) findViewById(R.id.tv_array_moshi);

		tv_wanchen = (TextView) findViewById(R.id.tv_wanchen);

		tv_canshu.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_row.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		rowEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		columnEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_column.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_array.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_x_offset.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_x_offset_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_x_offset_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_x_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_x_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_x_offset_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_x_offset_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_y_offset_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_y_offset_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_y_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_y_offset_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_y_offset_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_y_offset_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_zhongdian.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_end_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_end_x.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_end_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_end_y.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_end_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		et_end_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_array_speed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_array_moshi.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));
		tv_wanchen.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(45));

		/*=====================  end =====================*/

		/*===================== 初始化edittext =====================*/
		getUpdateInfo();
		/*=====================  end =====================*/

		// 点击全选
		rowEdit.setSelectAllOnFocus(true);
		columnEdit.setSelectAllOnFocus(true);
		et_x_offset_x.setSelectAllOnFocus(true);
		et_x_offset_y.setSelectAllOnFocus(true);
		et_x_offset_z.setSelectAllOnFocus(true);
		et_y_offset_x.setSelectAllOnFocus(true);
		et_y_offset_y.setSelectAllOnFocus(true);
		et_y_offset_z.setSelectAllOnFocus(true);
		et_end_x.setSelectAllOnFocus(true);
		et_end_y.setSelectAllOnFocus(true);
		et_end_z.setSelectAllOnFocus(true);

		linear_x = (LinearLayout) findViewById(R.id.array_lin_x);
		linear_y = (LinearLayout) findViewById(R.id.array_lin_y);
		linear_e = (LinearLayout) findViewById(R.id.array_lin_e);
//		linear_x.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				linear_x.setFocusable(true);
//				linear_x.setFocusableInTouchMode(true);
//				linear_x.requestFocus();
//				return false;
//			}
//		});
//		linear_y.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				linear_y.setFocusable(true);
//				linear_y.setFocusableInTouchMode(true);
//				linear_y.requestFocus();
//				return false;
//			}
//		});
//		linear_e.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				linear_e.setFocusable(true);
//				linear_e.setFocusableInTouchMode(true);
//				linear_e.requestFocus();
//				return false;
//			}
//		});
		linear_x.setOnClickListener(this);
		linear_y.setOnClickListener(this);
		linear_e.setOnClickListener(this);

		linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
		linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
		linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));

		rl_back.setOnClickListener(this);
		rl_speed.setOnClickListener(this);
		rl_moshi.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

		speed = settingParam.getHighSpeed();
		speedXYZ = new int[3];
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();

	}

	/**
	 * 获取最新的输入框中的信息
	 */
	private void getUpdateInfo() {
		mLast_x_x = getStringToDouble(et_x_offset_x);
		mLast_x_y = getStringToDouble(et_x_offset_y);
		mLast_x_z = getStringToDouble(et_x_offset_z);

		mLast_y_x = getStringToDouble(et_y_offset_x);
		mLast_y_y = getStringToDouble(et_y_offset_y);
		mLast_y_z = getStringToDouble(et_y_offset_z);

		mLast_e_x = getStringToDouble(et_end_x);
		mLast_e_y = getStringToDouble(et_end_y);
		mLast_e_z = getStringToDouble(et_end_z);
	}
	/**
	 *	比较输入框中的内容是否改变了
	 * @return true:没有改变 false:改变了
	 */
	private boolean ifInfoChange() {
		if(mLast_x_x!=getStringToDouble(et_x_offset_x)){
//			System.out.println("比较输入框内容："+mLast_x_x+","+"输入框："+getStringToDouble(et_x_offset_x));
			return false;
		}else if (mLast_x_y != getStringToDouble(et_x_offset_y)){
			return false;
		}else if (mLast_x_z != getStringToDouble(et_x_offset_z)){
			return false;
		}else if (mLast_y_x != getStringToDouble(et_y_offset_x)){
			return false;
		}else if (mLast_y_y != getStringToDouble(et_y_offset_y)){
			return false;
		}else if (mLast_y_z != getStringToDouble(et_y_offset_z)){
			return false;
		}else if (mLast_e_x != getStringToDouble(et_end_x)){
			return false;
		}else if (mLast_e_y != getStringToDouble(et_end_y)){
			return false;
		}else if (mLast_e_z != getStringToDouble(et_end_z)){
			return false;
		}
		return true;
	}
	/**
	 * @ClassName MoveListener
	 * @Description x,y,z,u移动
	 * @author 商炎炳
	 * @date 2016年1月28日 下午2:36:18
	 *
	 */
	private class MoveListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(selectLastFocus == selectFocus&&ifInfoChange()){
				//直接移动
				if(modeFlag == 0){
					//连续
					switch (v.getId()) {
					case R.id.nav_x_plus:// x+

							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(0, 0, 0, speed);
//								System.out.println("x轴move");
								stopTimer();
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(0);
								stopTimer();
//								System.out.println("x轴stop");
								prepareStopRetry(0);
							}

						break;
					case R.id.nav_x_minus:// x-

							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(1, 0, 0, speed);
								stopTimer();
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(0);
								stopTimer();
								prepareStopRetry(0);
							}
						break;
					case R.id.nav_y_plus:// y+

							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(0, 0, 1, speed);
								stopTimer();
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(1);
								stopTimer();
								prepareStopRetry(1);
							}

						break;
					case R.id.nav_y_minus:// y-

							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(1, 0, 1, speed);
								stopTimer();
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(1);
								stopTimer();
								prepareStopRetry(1);
							}

						break;
					case R.id.nav_z_plus:// z+

							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(0, 0, 2, speed);
								stopTimer();
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(2);
								stopTimer();
								prepareStopRetry(2);
							}

						break;
					case R.id.nav_z_minus:// z-

							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(1, 0, 2, speed);
								stopTimer();
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(2);
								stopTimer();
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
									stopTimer();
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
									stopTimer();
									prepareStopRetry(3);
								}
							}

						break;
					}
				}else if(modeFlag ==1 ){
					//单步
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
			}
			else{
				locatCoord(event);
			}
			return false;
		}
		
	}



	//定位
	private void locatCoord(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){

			if (selectFocus == 0) {//在基准点的基础上偏移,偏移时基准点不变
				xPoint.setX((float) getStringToDouble(et_x_offset_x)+mPoint.getX());
				xPoint.setY((float) getStringToDouble(et_x_offset_y)+mPoint.getY());
				xPoint.setZ((float) getStringToDouble(et_y_offset_z)+mPoint.getZ());
//				System.out.println("定位点x轴坐标："+RobotParam.INSTANCE.XCenterPulse2Journey(xPoint.getX()));
//				System.out.println("定位点y轴坐标："+RobotParam.INSTANCE.YCenterPulse2Journey(xPoint.getY()));
//				System.out.println("定位点z轴坐标："+RobotParam.INSTANCE.ZCenterPulse2Journey(xPoint.getZ()));
				MoveUtils.locationCoord(xPoint);
				getUpdateInfo();
			} else if (selectFocus == 1) {
				yPoint.setX((float) getStringToDouble(et_y_offset_x)+mPoint.getX());
				yPoint.setY((float) getStringToDouble(et_y_offset_y)+mPoint.getY());
				yPoint.setZ((float) getStringToDouble(et_y_offset_z)+mPoint.getZ());
//				System.out.println("定位点y_x轴坐标："+RobotParam.INSTANCE.XCenterPulse2Journey(yPoint.getX()));
//				System.out.println("定位点y_y轴坐标："+RobotParam.INSTANCE.YCenterPulse2Journey(yPoint.getY()));
//				System.out.println("定位点y_z轴坐标："+RobotParam.INSTANCE.ZCenterPulse2Journey(yPoint.getZ()));
				MoveUtils.locationCoord(yPoint);
			} else if (selectFocus == 2) {
				ePoint.setX((float) getStringToDouble(et_end_x)+mPoint.getX());
				ePoint.setY((float) getStringToDouble(et_end_y)+mPoint.getY());
				ePoint.setZ((float) getStringToDouble(et_end_z)+mPoint.getZ());
				MoveUtils.locationCoord(ePoint);
			}
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			// 抬起的时候把设置上次选中的单选框
			selectLastFocus = selectFocus;
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
			System.out.println("新建一个mTimer");
			mTimer=new Timer();
		}
		if (mTimerTask == null){
			System.out.println("新建一个mTimerTask");
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
			System.out.println("执行了mTimer.schedule");
			mTimer.schedule(mTimerTask,220,60);
		}
	}
	/**
	 * 自定义OnFocusChangeListene,失去焦点时，保存当前的内容
	 *
	 */
	private class OnKeyFocusChangeListener implements OnFocusChangeListener {

		private ArrayParam arrayParam;
		private EditText et;
		private int key;// 判断是哪一个
		private double value;

		/**
		 * 自定义OnFocusChangeListene,失去焦点时，保存当前的内容
		 * 
		 * @param arrayParam
		 *            阵列参数
		 * @param et
		 *            Edittext
		 * @param key
		 *            判断是哪一个
		 */
		public OnKeyFocusChangeListener(ArrayParam arrayParam, EditText et, int key) {
			super();
			this.arrayParam = arrayParam;
			this.et = et;
			this.key = key;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				// ((EditText) v).setCursorVisible(false);
				value = Double.parseDouble(et.getText().toString());
				switch (key) {
				case KEY_X_X:
					arrayParam.getMx().setX(value);
					break;
				case KEY_X_Y:
					arrayParam.getMx().setY(value);
					break;
				case KEY_X_Z:
					arrayParam.getMx().setZ(value);
					break;
				case KEY_Y_X:
					arrayParam.getMy().setX(value);
					break;
				case KEY_Y_Y:
					arrayParam.getMy().setY(value);
					break;
				case KEY_Y_Z:
					arrayParam.getMy().setZ(value);
					break;
				case KEY_E_X:
					arrayParam.getMe().setX(value);
					break;
				case KEY_E_Y:
					arrayParam.getMe().setY(value);
					break;
				case KEY_E_Z:
					arrayParam.getMe().setZ(value);
					break;
				case ROW:
					arrayParam.setRow((int) value);
					break;
				case COLUMN:
					arrayParam.setCol((int) value);
					break;
				}
			}
		}

	}

	/**
	 * 自定义的OnEditorActionListener,软键盘输入回车，将数据保存到List集合中
	 *
	 */
	private class OnKeyEditorEnterListener implements OnEditorActionListener {

		private ArrayParam arrayParam;
		private EditText et;
		private int key;// 判断是哪一个
		private double value;

		/**
		 * 软键盘输入回车，将数据保存到ArrayParam中
		 * 
		 * @param arrayParam
		 *            阵列参数
		 * @param et
		 *            Edittext
		 * @param key
		 *            判断是哪一个Edittext
		 */
		public OnKeyEditorEnterListener(ArrayParam arrayParam, EditText et, int key) {
			this.arrayParam = arrayParam;
			this.et = et;
			this.key = key;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				et.clearFocus();
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				value = Double.parseDouble(et.getText().toString());
				switch (key) {
				case KEY_X_X:
					arrayParam.getMx().setX(value);
					break;
				case KEY_X_Y:
					arrayParam.getMx().setY(value);
					break;
				case KEY_X_Z:
					arrayParam.getMx().setZ(value);
					break;
				case KEY_Y_X:
					arrayParam.getMy().setX(value);
					break;
				case KEY_Y_Y:
					arrayParam.getMy().setY(value);
					break;
				case KEY_Y_Z:
					arrayParam.getMy().setZ(value);
					break;
				case KEY_E_X:
					arrayParam.getMe().setX(value);
					break;
				case KEY_E_Y:
					arrayParam.getMe().setY(value);
					break;
				case KEY_E_Z:
					arrayParam.getMe().setZ(value);
					break;
				}
				et.setText(value + "");
			}
			return false;
		}

	}

	private class SpinnerXMLSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String bStart = adapterSpinner.getItem(position).toString();
			if (bStart.equals("Z")) {
				arrayParam.setbStartDirY(false);
				arrayParam.setbSType(false);
			} else if (bStart.equals("S")) {
				arrayParam.setbStartDirY(false);
				arrayParam.setbSType(true);

			} else if (bStart.equals("W")) {
				arrayParam.setbStartDirY(true);
				arrayParam.setbSType(false);

			} else if (bStart.equals("U")) {
				arrayParam.setbStartDirY(true);
				arrayParam.setbSType(true);
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	/**
	 * 找到所有组件的属性值
	 * 
	 * @return false代表有错误信息,true代表一起正常
	 */
	private boolean checkAllComponents() {

		double value;
		if (rowEdit.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.row_not_null);
			return false;
		} else if (Integer.parseInt(rowEdit.getText().toString()) < 1) {
			prompt = getResources().getString(R.string.row_not_less_1);
			return false;
		} else {
			arrayParam.setRow(Integer.parseInt(rowEdit.getText().toString()));
		}
		if (columnEdit.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.column_not_null);
			return false;
		} else if (Integer.parseInt(columnEdit.getText().toString()) < 1) {
			prompt = getResources().getString(R.string.column_not_less_1);
			return false;
		} else {
			arrayParam.setCol(Integer.parseInt(columnEdit.getText().toString()));
		}
		if (et_x_offset_x.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.x_offset_x_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_x_offset_x.getText().toString());
			arrayParam.getMx().setX(value);
		}
		if (et_x_offset_y.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.x_offset_y_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_x_offset_y.getText().toString());
			arrayParam.getMx().setY(value);
		}
		if (et_x_offset_z.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.x_offset_z_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_x_offset_z.getText().toString());
			arrayParam.getMx().setZ(value);
			//默认是为1的
			arrayParam.getMx().setU(0);
		}
		if (et_y_offset_x.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.y_offset_x_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_y_offset_x.getText().toString());
			arrayParam.getMy().setX(value);
		}
		if (et_y_offset_y.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.y_offset_y_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_y_offset_y.getText().toString());
			arrayParam.getMy().setY(value);
		}
		if (et_y_offset_z.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.y_offset_z_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_y_offset_z.getText().toString());
			arrayParam.getMy().setZ(value);
			//默认是为1
			arrayParam.getMy().setU(0);
		}
		if (et_end_x.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.et_end_x_not_null);
			return false;
		} else {
			arrayParam.getMe().setX(Double.parseDouble(et_end_x.getText().toString()));
		}
		if (et_end_y.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.et_end_y_not_null);
			return false;
		} else {
			arrayParam.getMe().setY(Double.parseDouble(et_end_y.getText().toString()));
		}
		if (et_end_z.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.et_end_z_not_null);
			return false;
		} else {
			arrayParam.getMe().setZ(Double.parseDouble(et_end_z.getText().toString()));
			// U默认是设为1的
			arrayParam.getMe().setU(0);
		}
		return true;
	}

	/**
	 * 保存并返回TaskActivity
	 */
	private void saveBackActivity() {
		if (checkAllComponents()) {
			// arrayParam.setbSort(true);
			List<Point> pLists = ArrayArithmetic.arrayPoint(arrayParam, points);
			Log.d(TAG, pLists.toString());
			Bundle extras = new Bundle();
			if (pLists.size() > TaskActivity.MAX_SIZE) {
				extras.putString(TaskActivity.KEY_NUMBER, "0");
				userApplication.setPoints(pLists);
			} else {
				extras.putString(TaskActivity.KEY_NUMBER, "1");
				extras.putParcelableArrayList(TaskActivity.ARRAY_KEY, (ArrayList<? extends Parcelable>) pLists);
			}
			intent.putExtras(extras);
			setResult(TaskActivity.resultArrayCode, intent);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
		} else {
			ToastUtil.displayPromptInfo(this, prompt);
		}
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueArrayActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_array));
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setPositiveButton(getResources().getString(R.string.is_need_yes),
				new DialogInterface.OnClickListener() {
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
				GlueArrayActivity.this.finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			}
		});
		builder.setNeutralButton(getResources().getString(R.string.is_need_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	private void DisPlayInfoAfterGetMsg(byte[] revBuffer) {
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
		case 0:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "校验失败");
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值

				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);
				Log.d(TAG, "返回的Point:"+coordPoint.toString()+","+coordPoint.getX());
				StopSuccessFlag=true;//说明下位机成功返回消息
				StopRetryTimes=5;//重新设置重传次数
				if (selectFocus == 0) {
//					System.out.println("x轴偏移行程："+RobotParam.INSTANCE.XCenterPulse2Journey(coordPoint.getX()-mPoint.getX()));
					et_x_offset_x
							.setText(FloatUtil.getFloatToString(coordPoint.getX()-mPoint.getX()));
					et_x_offset_y
							.setText(FloatUtil.getFloatToString(coordPoint.getY()-mPoint.getY()));
					et_x_offset_z
							.setText(FloatUtil.getFloatToString(coordPoint.getZ()-mPoint.getZ()));
				} else if (selectFocus == 1) {
//					System.out.println("y轴偏移行程："+RobotParam.INSTANCE.XCenterPulse2Journey(coordPoint.getX()-mPoint.getX()));
					et_y_offset_x
							.setText(FloatUtil.getFloatToString(coordPoint.getX()-mPoint.getX()));
					et_y_offset_y
							.setText(FloatUtil.getFloatToString(coordPoint.getY()-mPoint.getY()));
					et_y_offset_z
							.setText(FloatUtil.getFloatToString(coordPoint.getZ()-mPoint.getZ()));
				} else if (selectFocus == 2) {
					et_end_x.setText(FloatUtil.getFloatToString(coordPoint.getX()-mPoint.getX()));
					et_end_y.setText(FloatUtil.getFloatToString(coordPoint.getY()-mPoint.getY()));
					et_end_z.setText(FloatUtil.getFloatToString(coordPoint.getZ()-mPoint.getZ()));
				}
				//获取最新的
				getUpdateInfo();
			}
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务上传失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务下载失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "未知错误");
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
				ToastUtil.displayPromptInfo(GlueArrayActivity.this,"wifi连接断开。。");
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
		case R.id.array_lin_x:
			selectFocus = 0;
			linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
			linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			if (m_nAxisNum==3){
				but_x_plus.setEnabled(true);
				but_x_minus.setEnabled(true);
				but_y_plus.setEnabled(true);
				but_y_minus.setEnabled(true);
				but_z_plus.setEnabled(true);
				but_z_minus.setEnabled(true);
			}else {
				but_x_plus.setEnabled(true);
				but_x_minus.setEnabled(true);
				but_y_plus.setEnabled(true);
				but_y_minus.setEnabled(true);
				but_z_plus.setEnabled(true);
				but_z_minus.setEnabled(true);
				but_u_plus.setEnabled(true);
				but_u_minus.setEnabled(true);
			}
			break;
		case R.id.array_lin_y:
			selectFocus = 1;
			linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
			linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			if (m_nAxisNum==3){
				but_x_plus.setEnabled(true);
				but_x_minus.setEnabled(true);
				but_y_plus.setEnabled(true);
				but_y_minus.setEnabled(true);
				but_z_plus.setEnabled(true);
				but_z_minus.setEnabled(true);
			}else {
				but_x_plus.setEnabled(true);
				but_x_minus.setEnabled(true);
				but_y_plus.setEnabled(true);
				but_y_minus.setEnabled(true);
				but_z_plus.setEnabled(true);
				but_z_minus.setEnabled(true);
				but_u_plus.setEnabled(true);
				but_u_minus.setEnabled(true);
			}
			break;
		case R.id.array_lin_e:
			selectFocus = 2;
			linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
			if (m_nAxisNum==3){
				but_x_plus.setEnabled(false);
				but_x_minus.setEnabled(false);
				but_y_plus.setEnabled(false);
				but_y_minus.setEnabled(false);
				but_z_plus.setEnabled(false);
				but_z_minus.setEnabled(false);
				but_u_plus.setEnabled(false);
				but_u_minus.setEnabled(false);
			}

			break;
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
		case R.id.rl_complete:// 完成
			showBackDialog();
			break;
		}
	}

}
