/**
 * 
 */
package com.mingseal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mingseal.data.dao.GlueFaceStartDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.ui.PopupListView;
import com.mingseal.ui.PopupListView.OnClickPositionChanged;
import com.mingseal.ui.PopupListView.OnZoomInChanged;
import com.mingseal.ui.PopupView;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mingseal.data.param.PointConfigParam.GlueFaceStart;

/**
 * @author 商炎炳
 * 
 */
public class GlueFaceStartActivity extends AutoLayoutActivity implements OnClickListener {

	private final static String TAG = "GlueFaceStartActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * 点胶口
	 */
	private ToggleButton[] isGluePort;

	/**
	 * 返回上级菜单
	 */
	private RelativeLayout rl_back;
	/**
	 * 完成按钮
	 */
	private RelativeLayout rl_complete;
	private Intent intent;
	private Point point;// 从taskActivity中传值传过来的point
	private GlueFaceStartDao glueFaceStartDao;
	private List<PointGlueFaceStartParam> glueStartLists;
	private PointGlueFaceStartParam glueStart;
	private boolean[] glueBoolean;
	private int param_id = 1;// / 选取的是几号方案
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据

	/**
	 * @Fields outGlueTimePrevInt: 出胶前延时的int值
	 */
	private int outGlueTimePrevInt = 0;
	/**
	 * @Fields outGlueTimeInt: 出胶后延时的int值
	 */
	private int outGlueTimeInt = 0;
	/**
	 * @Fields moveSpeedInt: 移动速度的int值
	 */
	private int moveSpeedInt = 0;
	/**
	 * @Fields stopGlueTimePrevInt: 停胶延时的int值
	 */
	private int stopGlueTimePrevInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	/* =================== begin =================== */
	private HashMap<Integer, PointGlueFaceStartParam> update_id;// 修改的方案号集合
	private int defaultNum = 1;// 默认号
	ArrayList<PopupView> popupViews;
	private TextView mMorenTextView;
	PopupListView popupListView;
	int p = 0;
	View extendView;

	private boolean isOk;
	private boolean isExist = false;// 是否存在
	private boolean firstExist = false;// 是否存在
	/**
	 * 当前任务号
	 */
	private int currentTaskNum;
	private int currentClickNum;// 当前点击的序号
	private int mIndex;// 对应方案号
	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private ImageView iv_moren;
	/**
	 * @Fields et_start_outGlueTimePrev: 出胶前延时
	 */
	private EditText et_facestart_outGlueTimePrev;
	/**
	 * @Fields et_start_moveSpeed: 轨迹速度
	 */
	private EditText et_facestart_movespeed;
	/**
	 * @Fields et_start_outGlueTime: 出胶后延时
	 */
	private EditText et_facestart_outGlueTime;
	/**
	 * @Fields et_start_stopGlueTimePrev: 停胶延时
	 */
	private EditText et_facestart_stopGlueTime;
	/**
	 * 是否出胶
	 */
	private ToggleButton switch_isOutGlue;
	/**
	 * 起始方向 true:x方向 false:y方向
	 */
	private ToggleButton switch_startDir;
	private RelativeLayout rl_save;
	String[] GluePort;
	 private TextView title_outGlueTimePrev;
	    private TextView title_et_facestart_outGlueTimePrev;
	    private TextView activity_ms;
	    private TextView activity_fenghao;
	    private TextView title_moveSpeed;
	    private TextView title_et_facestart_movespeed;
	    private TextView activity_mm_s;
	    private TextView activity_second_fenghao;
	    private TextView title_outGlueTime;
	    private TextView title_et_facestart_outGlueTime;
	    private TextView activity_second_ms;
	    private TextView activity_third_fenghao;
	    private TextView title_stopGlueTime;
	    private TextView title_et_facestart_stopGlueTime;
	    private TextView activity_third_ms;
	    private TextView activity_four_fenghao;
	    private TextView title_activity_glue_alone_isOutGlue;
	    private TextView title_et_activity_glue_alone_isOutGlue;
	    private TextView activity_five_fenghao;
	    private TextView title_activity_glue_startDir;
	    private TextView title_et_activity_glue_startDir;
	    private TextView activity_six_fenghao;
	    private TextView activity_glue_port;
	    private TextView title_et_glue_port;
	private TextView tv_outGlueTimePrev;
	private TextView extend_ms;
	private TextView tv_moveSpeed;
	private TextView tv_facestart_mm_s;
	private TextView tv_outGlueTime;
	private TextView tv_outGlueTime_ms;
	private TextView tv_stopGlueTime;
	private TextView tv_stopGlueTime_ms;
	private TextView extend_isOutGlue;
	private TextView extend_startDir;
	private TextView extend_dianjiao1;
	private TextView extend_dianjiao2;
	private TextView extend_dianjiao3;
	private TextView extend_dianjiao4;
	private TextView extend_dianjiao5;
	private TextView extend_default;
	private TextView extend_save;
	private TextView mFanganliebiao;

	/* =================== end =================== */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_face_start);
		update_id = new HashMap<>();
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueFaceStartActivity.this,
				SettingParam.DefaultNum.ParamGlueFaceStartNumber);
		// Log.d(TAG, point.toString());
		glueFaceStartDao = new GlueFaceStartDao(GlueFaceStartActivity.this);
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();
		if (glueStartLists == null || glueStartLists.isEmpty()) {
			glueStart = new PointGlueFaceStartParam();
			glueStart.set_id(param_id);
			glueFaceStartDao.insertGlueFaceStart(glueStart);
			// 插入主键id
		}
		// 重新获取一下数据
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();
		// 初始化数组
		glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		popupViews = new ArrayList<>();
		GluePort = new String[5];
		initPicker();
		// // 初始化Handler,用来处理消息
		// handler = new Handler(GlueFaceStartActivity.this);

	}

	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 打开extendview的时候设置界面内容，显示最新的方案数据而不是没有保存的数据,没有得到保存的方案
	 * @author wj
	 */
	private void SetDateAndRefreshUI() {
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
			list.add(pointGlueFaceStartParam.get_id());
		}
		System.out.println("存放主键id的集合---->" + list);
		System.out.println("当前选择的方案号---->" + currentTaskNum);
		System.out.println("list是否存在------------》"
				+ list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			// 已经保存在数据库中的数据
			for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
				if (currentTaskNum == pointGlueFaceStartParam.get_id()) {
					View extendView = popupListView.getItemViews()
							.get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueFaceStartParam);
				}
			}
		} else {
			// 对所有数据进行置空
			View allextendView = popupListView.getItemViews()
					.get(currentClickNum).getExtendView();
			initView(allextendView);
			UpdateInfos(null);
		}
	}

	/**
	 * @Title UpdateInfos
	 * @Description 更新上半部分界面
	 * @author wj
	 * @param glueFaceStartParam
	 */
	private void UpdateInfos(PointGlueFaceStartParam glueFaceStartParam) {
		if (glueFaceStartParam == null) {
			et_facestart_outGlueTimePrev.setText("");
			et_facestart_movespeed.setText("");
			et_facestart_outGlueTime.setText("");
			et_facestart_stopGlueTime.setText("");

		} else {
			et_facestart_outGlueTimePrev.setText(glueFaceStartParam
					.getOutGlueTimePrev() + "");
			et_facestart_movespeed.setText(glueFaceStartParam.getMoveSpeed()
					+ "");
			et_facestart_outGlueTime.setText(glueFaceStartParam
					.getOutGlueTime() + "");
			et_facestart_stopGlueTime.setText(glueFaceStartParam
					.getStopGlueTime() + "");

			switch_isOutGlue.setChecked(glueFaceStartParam.isOutGlue());

			isGluePort[0].setChecked(glueFaceStartParam.getGluePort()[0]);
			isGluePort[1].setChecked(glueFaceStartParam.getGluePort()[1]);
			isGluePort[2].setChecked(glueFaceStartParam.getGluePort()[2]);
			isGluePort[3].setChecked(glueFaceStartParam.getGluePort()[3]);
			isGluePort[4].setChecked(glueFaceStartParam.getGluePort()[4]);
		}
	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(
				R.string.activity_glue_face_start));
		mMorenTextView = (TextView) findViewById(R.id.morenfangan);
		mFanganliebiao = (TextView) findViewById(R.id.fanganliebiao);
		mFanganliebiao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(50));
		mMorenTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mMorenTextView.setText("当前默认方案号(" + defaultNum + ")");
		// 初始化popuplistview区域
		popupListView = (PopupListView) findViewById(R.id.popupListView);
		popupListView.init(null);

		// 初始化创建10个popupView
		for (int i = 0; i < 10; i++) {
			p = i + 1;
			PopupView popupView = new PopupView(this, R.layout.popup_view_item_face_start) {

				@Override
				public void setViewsElements(View view) {
					glueStartLists = glueFaceStartDao
							.findAllGlueFaceStartParams();
					ImageView title_num = (ImageView) view
							.findViewById(R.id.title_num);
					if (p == 1) {// 方案列表第一位对应一号方案
						title_num.setImageResource(R.drawable.green1);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 2) {
						title_num.setImageResource(R.drawable.green2);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 3) {
						title_num.setImageResource(R.drawable.green3);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 4) {
						title_num.setImageResource(R.drawable.green4);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 5) {
						title_num.setImageResource(R.drawable.green5);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 6) {
						title_num.setImageResource(R.drawable.green6);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 7) {
						title_num.setImageResource(R.drawable.green7);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 8) {
						title_num.setImageResource(R.drawable.green8);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 9) {
						title_num.setImageResource(R.drawable.green9);
						setTitleInfos(glueStartLists, view, p);
					} else if (p == 10) {
						title_num.setImageResource(R.drawable.green10);
						setTitleInfos(glueStartLists, view, p);
					}
				}

				@Override
				public View setExtendView(View view) {
					if (view == null) {
						extendView = LayoutInflater.from(
								getApplicationContext()).inflate(
								R.layout.glue_face_start_extend_view, null);
						int size = glueStartLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							}
						}
						extendView.setBackgroundColor(Color.WHITE);
					} else {
						extendView = view;
					}
					return extendView;
				}

				@Override
				public void initViewAndListener(View extendView) {
					et_facestart_outGlueTimePrev = (EditText) extendView
							.findViewById(R.id.et_facestart_outGlueTimePrev);
					et_facestart_movespeed = (EditText) extendView
							.findViewById(R.id.et_facestart_movespeed);
					et_facestart_outGlueTime = (EditText) extendView
							.findViewById(R.id.et_facestart_outGlueTime);
					et_facestart_stopGlueTime = (EditText) extendView
							.findViewById(R.id.et_facestart_stopGlueTime);
					switch_isOutGlue = (ToggleButton) extendView
							.findViewById(R.id.switch_isOutGlue);
					switch_startDir = (ToggleButton) extendView
							.findViewById(R.id.switch_startDir);

					isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL
							.ordinal()];
					isGluePort[0] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport1);
					isGluePort[1] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport2);
					isGluePort[2] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport3);
					isGluePort[3] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport4);
					isGluePort[4] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport5);

					// 设置出胶前延时的默认值和最大最小值(要重新设置)
					et_facestart_outGlueTimePrev
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceStart.OutGlueTimePrevMax,
									GlueFaceStart.GlueFaceStartMin,
									et_facestart_outGlueTimePrev));
					et_facestart_outGlueTimePrev
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceStart.OutGlueTimePrevMax,
									GlueFaceStart.GlueFaceStartMin,
									et_facestart_outGlueTimePrev));
					et_facestart_outGlueTimePrev.setSelectAllOnFocus(true);

					// 设置出胶后延时的默认值和最大最小值(要重新设置)
					et_facestart_outGlueTime
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceStart.OutGlueTimeMax,
									GlueFaceStart.GlueFaceStartMin,
									et_facestart_outGlueTime));
					et_facestart_outGlueTime
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceStart.OutGlueTimeMax,
									GlueFaceStart.GlueFaceStartMin,
									et_facestart_outGlueTime));
					et_facestart_outGlueTime.setSelectAllOnFocus(true);

					// 设置轨迹速度的默认值和最大最小值(要重新设置)
					et_facestart_movespeed
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceStart.MoveSpeedMax,
									GlueFaceStart.MoveSpeedMin,
									et_facestart_movespeed));
					et_facestart_movespeed
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceStart.MoveSpeedMax,
									GlueFaceStart.MoveSpeedMin,
									et_facestart_movespeed));
					et_facestart_movespeed.setSelectAllOnFocus(true);

					// 设置停胶延时的默认值和最大最小值(要重新设置)
					et_facestart_stopGlueTime
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceStart.StopGlueTimeMax,
									GlueFaceStart.GlueFaceStartMin,
									et_facestart_stopGlueTime));
					et_facestart_stopGlueTime
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceStart.StopGlueTimeMax,
									GlueFaceStart.GlueFaceStartMin,
									et_facestart_stopGlueTime));
					et_facestart_stopGlueTime.setSelectAllOnFocus(true);
					rl_moren = (RelativeLayout) extendView
							.findViewById(R.id.rl_moren);
					iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
					rl_save = (RelativeLayout) extendView
							.findViewById(R.id.rl_save);// 保存按钮
					iv_moren = (ImageView) extendView
							.findViewById(R.id.iv_moren);// 默认按钮
					rl_moren.setOnClickListener(this);
					rl_save.setOnClickListener(this);
				}

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.rl_moren:// 设为默认
						// 判断界面
						save();
						if ((isOk && isExist) || firstExist) {// 不为空且已经存在或者不存在且插入新的
							// 刷新ui
							mMorenTextView.setText("当前默认方案号(" + currentTaskNum
									+ ")");
							// 默认号存到sp
							SharePreferenceUtils
									.saveParamNumberToPref(
											GlueFaceStartActivity.this,
											SettingParam.DefaultNum.ParamGlueFaceStartNumber,
											currentTaskNum);
						}
						isExist = false;
						firstExist = false;
						// 更新数据
						break;
					case R.id.rl_save:// 保存
						save();
						// 数据库保存数据
						break;

					default:
						break;
					}
				}
			};
			popupViews.add(popupView);
		}
		popupListView.setItemViews(popupViews);
		if (mType != 1) {
			popupListView.setPosition(defaultNum - 1);// 第一次默认选中第一个item，后面根据方案号(新建点)
		} else {
			// 显示point的参数方案
			// PointGlueAloneParam glueAloneParam= (PointGlueAloneParam)
			// point.getPointParam();
			// System.out.println("传进来的方案号为----------》"+glueAloneParam.get_id());
			popupListView.setPosition(point.getPointParam().get_id() - 1);
		}
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
			list.add(pointGlueFaceStartParam.get_id());
		}
		popupListView.setSelectedEnable(list);
		popupListView.setOnClickPositionChanged(new OnClickPositionChanged() {
			@Override
			public void getCurrentPositon(int position) {
				currentTaskNum = position + 1;
				currentClickNum = position;
			}
		});
		popupListView.setOnZoomInListener(new OnZoomInChanged() {

			@Override
			public void getZoomState(Boolean isZoomIn) {
				if (isZoomIn) {
					// 设置界面
					SetDateAndRefreshUI();
				}
			}
		});
		rl_back.setOnClickListener(this);

	}

	protected void setTitleInfos(List<PointGlueFaceStartParam> glueStartLists,
			View view, int p) {
		    title_outGlueTimePrev = (TextView) view.findViewById(R.id.title_outGlueTimePrev);
	        title_et_facestart_outGlueTimePrev = (TextView) view.findViewById(R.id.title_et_facestart_outGlueTimePrev);
	        activity_ms = (TextView) view.findViewById(R.id.activity_ms);
	        activity_fenghao = (TextView) view.findViewById(R.id.activity_fenghao);
	        title_moveSpeed = (TextView) view.findViewById(R.id.title_moveSpeed);
	        title_et_facestart_movespeed = (TextView) view.findViewById(R.id.title_et_facestart_movespeed);
	        activity_mm_s = (TextView) view.findViewById(R.id.activity_mm_s);
	        activity_second_fenghao = (TextView) view.findViewById(R.id.activity_second_fenghao);
	        title_outGlueTime = (TextView) view.findViewById(R.id.title_outGlueTime);
	        title_et_facestart_outGlueTime = (TextView) view.findViewById(R.id.title_et_facestart_outGlueTime);
	        activity_second_ms = (TextView) view.findViewById(R.id.activity_second_ms);
	        activity_third_fenghao = (TextView) view.findViewById(R.id.activity_third_fenghao);
	        title_stopGlueTime = (TextView) view.findViewById(R.id.title_stopGlueTime);
	        title_et_facestart_stopGlueTime = (TextView) view.findViewById(R.id.title_et_facestart_stopGlueTime);
	        activity_third_ms = (TextView) view.findViewById(R.id.activity_third_ms);
	        activity_four_fenghao = (TextView) view.findViewById(R.id.activity_four_fenghao);
	        title_activity_glue_alone_isOutGlue = (TextView) view.findViewById(R.id.title_activity_glue_alone_isOutGlue);
	        title_et_activity_glue_alone_isOutGlue = (TextView) view.findViewById(R.id.title_et_activity_glue_alone_isOutGlue);
	        activity_five_fenghao = (TextView) view.findViewById(R.id.activity_five_fenghao);
	        title_activity_glue_startDir = (TextView) view.findViewById(R.id.title_activity_glue_startDir);
	        title_et_activity_glue_startDir = (TextView) view.findViewById(R.id.title_et_activity_glue_startDir);
	        activity_six_fenghao = (TextView) view.findViewById(R.id.activity_six_fenghao);
	        activity_glue_port = (TextView) view.findViewById(R.id.activity_glue_port);
	        title_et_glue_port = (TextView) view.findViewById(R.id.title_et_glue_port);
	        
	        for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
				/*===================== begin =====================*/
				title_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_facestart_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_moveSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_facestart_movespeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_mm_s.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_second_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_facestart_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_second_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_third_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_stopGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_facestart_stopGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_third_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_four_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_activity_glue_alone_isOutGlue.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_activity_glue_alone_isOutGlue.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_five_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_activity_glue_startDir.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_activity_glue_startDir.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_six_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				activity_glue_port.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				title_et_glue_port.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(32));
				/*=====================  end =====================*/
				if (p == pointGlueFaceStartParam.get_id()) {
					activity_ms.setText(getResources().getString(
							R.string.activity_ms));
					activity_third_ms.setText(getResources().getString(
							R.string.activity_ms));
					activity_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					activity_second_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					activity_third_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					activity_four_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					activity_five_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					activity_six_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					activity_second_ms.setText(getResources().getString(
							R.string.activity_ms));
					activity_mm_s.setText(getResources().getString(
							R.string.activity_mm_s));
					
					title_outGlueTimePrev.setText(getResources().getString(
							R.string.activity_glue_outGlueTimePrev)
							+ " ");
					title_outGlueTime.setText(getResources().getString(
							R.string.activity_glue_outGlueTime)
							+ " ");
					title_stopGlueTime.setText(getResources().getString(R.string.activity_glue_alone_stopGlueTime));
					title_activity_glue_startDir.setText(getResources().getText(R.string.activity_glue_startDir));
					title_moveSpeed.setText(getResources().getString(
							R.string.activity_glue_moveSpeed)
							+ " ");
					title_activity_glue_alone_isOutGlue.setText(getResources()
							.getString(R.string.activity_glue_alone_isOutGlue)
							+ " ");
					activity_glue_port.setText(getResources().getString(
							R.string.activity_glue_port)
							+ " ");
					for (int j = 0; j < 5; j++) {
						if (pointGlueFaceStartParam.getGluePort()[j]) {
							GluePort[j] = "开";
						} else {
							GluePort[j] = "关";
						}
					}

					title_et_facestart_outGlueTimePrev.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_facestart_outGlueTimePrev.getPaint()
							.setAntiAlias(true); // 抗锯齿
					title_et_facestart_movespeed.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_facestart_movespeed.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_facestart_outGlueTime.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_facestart_outGlueTime.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_facestart_stopGlueTime.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_facestart_stopGlueTime.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_activity_glue_startDir.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_activity_glue_startDir.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_glue_port.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_glue_port.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_activity_glue_alone_isOutGlue.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_activity_glue_alone_isOutGlue.getPaint().setAntiAlias(true); // 抗锯齿

					title_et_facestart_outGlueTimePrev.setText(pointGlueFaceStartParam
							.getOutGlueTimePrev() + "");
					title_et_facestart_movespeed.setText(pointGlueFaceStartParam
							.getOutGlueTime() + "");
					title_et_facestart_outGlueTime.setText(pointGlueFaceStartParam.getMoveSpeed()
							+ "");
					title_et_facestart_stopGlueTime.setText(pointGlueFaceStartParam.getStopGlueTime()+"");
					if (pointGlueFaceStartParam.isOutGlue()) {
						title_et_activity_glue_alone_isOutGlue.setText("是");
					} else {
						title_et_activity_glue_alone_isOutGlue.setText("否");
					}
					if (pointGlueFaceStartParam.isStartDir()) {
						title_et_activity_glue_startDir.setText("是");
					} else {
						title_et_activity_glue_startDir.setText("否");
					}
					title_et_glue_port.setText(GluePort[0] + GluePort[1]
							+ GluePort[2] + GluePort[3] + GluePort[4]);
				}
			}
	}

	protected void save() {
		View extendView = popupListView.getItemViews().get(currentClickNum)
				.getExtendView();
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
			list.add(pointGlueFaceStartParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueFaceStartParam upfaceStartParam = getFaceStart(extendView);
			if (glueStartLists.contains(upfaceStartParam)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号
				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != glueStartLists.get(
						glueStartLists.indexOf(upfaceStartParam)).get_id()) {
					ToastUtil.displayPromptInfo(GlueFaceStartActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {
				for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
					if (currentTaskNum == pointGlueFaceStartParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = glueFaceStartDao
							.upDateGlueFaceStart(upfaceStartParam);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upfaceStartParam.get_id(), upfaceStartParam);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upfaceStartParam.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = glueFaceStartDao
							.insertGlueFaceStart(upfaceStartParam);
					firstExist = true;
					glueStartLists = glueFaceStartDao
							.findAllGlueFaceStartParams();
					Log.i(TAG, "保存之后新方案-->" + glueStartLists.toString());
					ToastUtil.displayPromptInfo(GlueFaceStartActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {
						list.add(pointGlueFaceStartParam.get_id());
					}
					popupListView.setSelectedEnable(list);
				}
			}
			if (popupListView.isItemZoomIn()) {
				popupListView.zoomOut();
			}
			// 更新title
			refreshTitle();
			flag = false;
		} else {
			ToastUtil.displayPromptInfo(this,
					getResources().getString(R.string.data_is_null));
		}
	}

	/**
	 * @Title refreshTitle
	 * @Description 按下保存之后刷新title
	 * @author wj
	 */
	private void refreshTitle() {
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();
		// popupListView->pupupview->title
		for (PointGlueFaceStartParam pointGlueFaceStartParam : glueStartLists) {

			if (currentTaskNum == pointGlueFaceStartParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				setTitleInfos(glueStartLists, titleViewItem, currentTaskNum);
				setTitleInfos(glueStartLists, titleViewExtend, currentTaskNum);

//				TextView textViewItem = (TextView) titleViewItem
//						.findViewById(R.id.title);
//				TextView textViewExtend = (TextView) titleViewExtend
//						.findViewById(R.id.title);
//				textViewItem.setText(pointGlueFaceStartParam.toString());
//				textViewExtend.setText(pointGlueFaceStartParam.toString());
//				for (int j = 0; j < 5; j++) {
//					if (pointGlueFaceStartParam.getGluePort()[j]) {
//						GluePort[j] = "开";
//					} else {
//						GluePort[j] = "关";
//					}
//				}
//				textViewItem.setText("提前出胶时间："
//						+ pointGlueFaceStartParam.getOutGlueTimePrev() + "ms,"
//						+ "轨迹速度：" + pointGlueFaceStartParam.getMoveSpeed()
//						+ "mm/s," + "滞后出胶时间："
//						+ pointGlueFaceStartParam.getOutGlueTime() + "ms,"
//						+ "停胶延时：" + pointGlueFaceStartParam.getStopGlueTime()
//						+ "ms," + "是否出胶：" + pointGlueFaceStartParam.isOutGlue()
//						+ "," + "起始方向：" + pointGlueFaceStartParam.isStartDir()
//						+ "," + "点胶口：" + GluePort[0] + GluePort[1]
//						+ GluePort[2] + GluePort[3] + GluePort[4]);
//				textViewExtend.setText("提前出胶时间："
//						+ pointGlueFaceStartParam.getOutGlueTimePrev() + "ms,"
//						+ "轨迹速度：" + pointGlueFaceStartParam.getMoveSpeed()
//						+ "mm/s," + "滞后出胶时间："
//						+ pointGlueFaceStartParam.getOutGlueTime() + "ms,"
//						+ "停胶延时：" + pointGlueFaceStartParam.getStopGlueTime()
//						+ "ms," + "是否出胶：" + pointGlueFaceStartParam.isOutGlue()
//						+ "," + "起始方向：" + pointGlueFaceStartParam.isStartDir()
//						+ "," + "点胶口：" + GluePort[0] + GluePort[1]
//						+ GluePort[2] + GluePort[3] + GluePort[4]);
			}
		}
	}

	/**
	 * @Title isEditClean
	 * @Description 判断输入框是否为空
	 * @author wj
	 * @param extendView
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditClean(View extendView) {
		et_facestart_outGlueTimePrev = (EditText) extendView
				.findViewById(R.id.et_facestart_outGlueTimePrev);
		et_facestart_movespeed = (EditText) extendView
				.findViewById(R.id.et_facestart_movespeed);
		et_facestart_outGlueTime = (EditText) extendView
				.findViewById(R.id.et_facestart_outGlueTime);
		et_facestart_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_facestart_stopGlueTime);
		if ("".equals(et_facestart_movespeed.getText().toString())) {
			return false;
		} else if ("".equals(et_facestart_outGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_facestart_outGlueTimePrev.getText().toString())) {
			return false;
		} else if ("".equals(et_facestart_outGlueTimePrev.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * @Title initView
	 * @Description 初始化当前extendView视图
	 * @author wj
	 * @param extendView
	 */
	protected void initView(View extendView) {
		et_facestart_outGlueTimePrev = (EditText) extendView
				.findViewById(R.id.et_facestart_outGlueTimePrev);
		et_facestart_movespeed = (EditText) extendView
				.findViewById(R.id.et_facestart_movespeed);
		et_facestart_outGlueTime = (EditText) extendView
				.findViewById(R.id.et_facestart_outGlueTime);
		et_facestart_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_facestart_stopGlueTime);
		switch_isOutGlue = (ToggleButton) extendView
				.findViewById(R.id.switch_isOutGlue);
		switch_startDir = (ToggleButton) extendView
				.findViewById(R.id.switch_startDir);

		isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport1);
		isGluePort[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport2);
		isGluePort[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport3);
		isGluePort[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport4);
		isGluePort[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport5);
		// rl_moren = (RelativeLayout) findViewById(R.id.rl_moren);
		// iv_add = (ImageView) findViewById(R.id.iv_add);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// iv_moren = (ImageView) findViewById(R.id.iv_moren);
		tv_outGlueTimePrev = (TextView) extendView.findViewById(R.id.tv_outGlueTimePrev);
		extend_ms = (TextView) extendView.findViewById(R.id.extend_ms);
		tv_moveSpeed = (TextView) extendView.findViewById(R.id.tv_moveSpeed);
		tv_facestart_mm_s = (TextView) extendView.findViewById(R.id.tv_facestart_mm_s);
		tv_outGlueTime = (TextView) extendView.findViewById(R.id.tv_outGlueTime);
		tv_outGlueTime_ms = (TextView) extendView.findViewById(R.id.tv_outGlueTime_ms);
		tv_stopGlueTime = (TextView) extendView.findViewById(R.id.tv_stopGlueTime);
		tv_stopGlueTime_ms = (TextView) extendView.findViewById(R.id.tv_stopGlueTime_ms);
		extend_isOutGlue = (TextView) extendView.findViewById(R.id.extend_isOutGlue);
		extend_startDir = (TextView) extendView.findViewById(R.id.extend_startDir);
		extend_dianjiao1 = (TextView) extendView.findViewById(R.id.extend_dianjiao1);
		extend_dianjiao2 = (TextView) extendView.findViewById(R.id.extend_dianjiao2);
		extend_dianjiao3 = (TextView) extendView.findViewById(R.id.extend_dianjiao3);
		extend_dianjiao4 = (TextView) extendView.findViewById(R.id.extend_dianjiao4);
		extend_dianjiao5 = (TextView) extendView.findViewById(R.id.extend_dianjiao5);
		extend_default = (TextView) extendView.findViewById(R.id.extend_default);
		extend_save = (TextView) extendView.findViewById(R.id.extend_save);
		/*===================== begin =====================*/
		et_facestart_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		et_facestart_movespeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		et_facestart_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		et_facestart_stopGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_moveSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_facestart_mm_s.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_outGlueTime_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_stopGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		tv_stopGlueTime_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_isOutGlue.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_startDir.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_dianjiao1.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_dianjiao2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_dianjiao3.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_dianjiao4.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_dianjiao5.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_default.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		extend_save.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		/*=====================  end =====================*/
	}

	/**
	 * 将页面上的数据保存到PointGlueFaceStartParam对象中
	 * 
	 * @param extendView
	 * 
	 * @return PointGlueFaceStartParam
	 */
	private PointGlueFaceStartParam getFaceStart(View extendView) {
		glueStart = new PointGlueFaceStartParam();
		et_facestart_outGlueTimePrev = (EditText) extendView
				.findViewById(R.id.et_facestart_outGlueTimePrev);
		et_facestart_movespeed = (EditText) extendView
				.findViewById(R.id.et_facestart_movespeed);
		et_facestart_outGlueTime = (EditText) extendView
				.findViewById(R.id.et_facestart_outGlueTime);
		et_facestart_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_facestart_stopGlueTime);
		switch_isOutGlue = (ToggleButton) extendView
				.findViewById(R.id.switch_isOutGlue);
		switch_startDir = (ToggleButton) extendView
				.findViewById(R.id.switch_startDir);

		isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport1);
		isGluePort[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport2);
		isGluePort[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport3);
		isGluePort[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport4);
		isGluePort[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport5);
		try {
			outGlueTimePrevInt = Integer.parseInt(et_facestart_outGlueTimePrev
					.getText().toString());
		} catch (NumberFormatException e) {
			outGlueTimePrevInt = 0;
		}
		try {
			outGlueTimeInt = Integer.parseInt(et_facestart_outGlueTime
					.getText().toString());
		} catch (NumberFormatException e) {
			outGlueTimeInt = 0;
		}
		try {
			moveSpeedInt = Integer.parseInt(et_facestart_movespeed.getText()
					.toString());
			if (moveSpeedInt == 0) {
				moveSpeedInt = 1;
			}
		} catch (NumberFormatException e) {
			moveSpeedInt = 1;
		}
		try {
			stopGlueTimePrevInt = Integer.parseInt(et_facestart_stopGlueTime
					.getText().toString());
		} catch (NumberFormatException e) {
			stopGlueTimePrevInt = 0;
		}

		glueStart.setOutGlueTimePrev(outGlueTimePrevInt);
		glueStart.setOutGlueTime(outGlueTimeInt);
		glueStart.setMoveSpeed(moveSpeedInt);
		glueStart.setStopGlueTime(stopGlueTimePrevInt);
		glueStart.setOutGlue(switch_isOutGlue.isChecked());
		glueStart.setStartDir(switch_startDir.isChecked());

		glueBoolean[0] = isGluePort[0].isChecked();
		glueBoolean[1] = isGluePort[1].isChecked();
		glueBoolean[2] = isGluePort[2].isChecked();
		glueBoolean[3] = isGluePort[3].isChecked();
		glueBoolean[4] = isGluePort[4].isChecked();

		glueStart.setGluePort(glueBoolean);
		glueStart.set_id(currentTaskNum);

		return glueStart;
	}

	@Override
	public void onBackPressed() {
		// 不想保存只想回退，不保存数据
		if (popupListView.isItemZoomIn()) {
			popupListView.zoomOut();
		} else {
			complete();
			super.onBackPressed();
			overridePendingTransition(R.anim.in_from_left,
					R.anim.out_from_right);
		}
	}

	private void complete() {
		ArrayList<? extends PopupView> itemPopuViews = popupListView
				.getItemViews();
		for (PopupView popupView : itemPopuViews) {
			ImageView iv_selected = (ImageView) popupView.getPopupView()
					.findViewById(R.id.iv_selected);
			if (iv_selected.getVisibility() == View.VISIBLE) {
				mIndex = itemPopuViews.indexOf(popupView) + 1;
			}
		}
		System.out.println("返回的方案号为================》" + mIndex);
		point.setPointParam(glueFaceStartDao.getPointFaceStartParamByID(mIndex));
		System.out.println("返回的Point为================》" + point);

		List<Map<Integer, PointGlueFaceStartParam>> list = new ArrayList<Map<Integer, PointGlueFaceStartParam>>();
		list.add(update_id);
		Log.i(TAG, point.toString());
		Bundle extras = new Bundle();
		extras.putParcelable(MyPopWindowClickListener.POPWINDOW_KEY, point);
		extras.putInt(MyPopWindowClickListener.FLAG_KEY, mFlag);
		// 须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的
		ArrayList bundlelist = new ArrayList();
		bundlelist.add(list);
		extras.putParcelableArrayList(MyPopWindowClickListener.TYPE_UPDATE,
				bundlelist);
		intent.putExtras(extras);

		setResult(TaskActivity.resultCode, intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:// 返回按钮的响应事件
			if (popupListView.isItemZoomIn()) {
				popupListView.zoomOut();
			} else {
				complete();
				super.onBackPressed();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_from_right);
			}
			break;
		default:
			break;
		}
	}

}
