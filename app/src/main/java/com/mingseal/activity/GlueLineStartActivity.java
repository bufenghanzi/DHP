/**
 *
 */
package com.mingseal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.dao.GlueLineStartDao;
import com.mingseal.data.param.PointConfigParam;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.ui.PopupListView;
import com.mingseal.ui.PopupView;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 商炎炳
 */
public class GlueLineStartActivity extends AutoLayoutActivity implements OnClickListener {

    private final static String TAG = "GlueLineStartActivity";
    /**
     * 标题栏的标题
     */
    private TextView tv_title;

    /**
     * 线起始点Spinner
     */
    private Spinner lineStartSpinner;

    /**
     * 返回上级菜单
     */
    private RelativeLayout rl_back;

    /**
     * 保存方案按钮
     */
    private RelativeLayout rl_save;
    /**
     * 完成按钮
     */
    private RelativeLayout rl_complete;
    private Intent intent;
    private Point point;// 从taskActivity中传值传过来的point
    private int mFlag;// 0代表增加数据，1代表更新数据
    private int mType;// 1表示要更新数据

    private GlueLineStartDao glueStartDao;
    private List<PointGlueLineStartParam> glueStartLists;// 保存的方案,用来维护从数据库中读出来的方案列表的编号
    private PointGlueLineStartParam glueStart;
    private boolean[] glueBoolean;
    private int param_id = 1;// / 选取的是几号方案
    /**
     * @Fields outGlueTimePrevInt: 出胶前延时的int值
     */
    private int outGlueTimePrevInt = 0;
    /**
     * @Fields outGlueTimeInt: 出胶后延时的int值
     */
    private int outGlueTimeInt = 0;
    /**
     * @Fields moveSpeedInt: 轨迹速度的int值
     */
    private int moveSpeedInt = 0;
    /**
     * @Fields stopGlueTimePrevInt: 停胶前延时的int值
     */
    private int stopGlueTimePrevInt = 0;
    /**
     * @Fields stopGlueTimeInt: 停胶后延时的int值
     */
    private int stopGlueTimeInt = 0;
    private TextView tv_num;
    private TextView tv_outGlue;
    private boolean flag = false;// 可以与用户交互，初始化完成标志
    /* =================== begin =================== */
    private HashMap<Integer, PointGlueLineStartParam> update_id;// 修改的方案号集合
    private int defaultNum = 1;// 默认号
    ArrayList<PopupView> popupViews;
    private TextView mMorenTextView;
    PopupListView popupListView;
    int p = 0;
    View extendView;
    /**
     * 提前出胶时间
     */
    private EditText et_linestart_outGlueTimePrev;
    /**
     * 滞后出胶时间
     */
    private EditText et_linestart_outGlueTime;
    /**
     * 轨迹速度
     */
    private EditText et_linestart_moveSpeed;
    /**
     * 延时模式 true:联动(ETimeNode.TIME_MODE_GANGED_TIME) 延时模式
     * false:定时(ETimeMode.TIME_MODE_FIXED_TIME)
     */
    private ToggleButton switch_timeMode;

    private ToggleButton switch_glueport1;
    private ToggleButton switch_glueport2;
    private ToggleButton switch_glueport3;
    private ToggleButton switch_glueport4;
    private ToggleButton switch_glueport5;
    private RelativeLayout rl_moren;
    private ImageView iv_add;
    private ImageView iv_moren;
    /**
     * 点胶口
     */
    private ToggleButton[] isGluePort;
    private boolean isOk;
    private boolean isExist = false;// 是否存在
    private boolean firstExist = false;// 是否存在
    /**
     * 当前任务号
     */
    private int currentTaskNum;
    private int currentClickNum;// 当前点击的序号
    private int mIndex;// 对应方案号
    String[] GluePort;
    private TextView title_outGlueTimePrev;
    private TextView title_et_linestart_outGlueTimePrev;
    private TextView activity_ms;
    private TextView activity_fenghao;
    private TextView title_outGlueTime;
    private TextView title_et_outGlueTime;
    private TextView activity_second_ms;
    private TextView activity_second_fenghao;
    private TextView title_moveSpeed;
    private TextView title_et_linestart_moveSpeed;
    private TextView activity_mm_s;
    private TextView activity_four_fenghao;
    private TextView title_activity_glue_timeMode;
    private TextView title_et_activity_glue_timeMode;
    private TextView activity_five_fenghao;
    private TextView activity_glue_port;
    private TextView title_et_glue_port;
    private TextView tv_outGlueTimePrev;
    private TextView extend_line_start_ms;
    private TextView tv_outGlueTime;
    private TextView tv_outGlueTime_ms;
    private TextView tv_moveSpeed;
    private TextView extend_line_start_mms;
    private TextView extend_line_start_timeMode;
    private TextView extend_line_start_dianjiao1;
    private TextView extend_line_start_dianjiao2;
    private TextView extend_line_start_dianjiao3;
    private TextView extend_line_start_dianjiao4;
    private TextView extend_line_start_dianjiao5;
    private TextView extend_default;
    private TextView extend_save;
    private TextView mFanganliebiao;
    private RevHandler handler;
    private UserApplication userApplication;
    /* =================== end =================== */
    private ViewStub stub_glue;
    private int Activity_Init_View = 2;
    private ImageView iv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_glue_line_start);
        userApplication = (UserApplication) getApplication();
        handler = new RevHandler();
        update_id = new HashMap<>();
        intent = getIntent();
        point = intent
                .getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
        mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
        mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
        defaultNum = SharePreferenceUtils.getParamNumberFromPref(
                GlueLineStartActivity.this,
                SettingParam.DefaultNum.ParamGlueLineStartNumber);
        Log.d(TAG, point.toString());

        glueStartDao = new GlueLineStartDao(GlueLineStartActivity.this);
        glueStartLists = glueStartDao.findAllGlueLineStartParams();
        if (glueStartLists == null || glueStartLists.isEmpty()) {
            glueStart = new PointGlueLineStartParam();
            glueStart.set_id(param_id);
            glueStartDao.insertGlueLineStart(glueStart);
        }
        glueStartLists = glueStartDao.findAllGlueLineStartParams();
        // 初始化数组
        glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
        GluePort = new String[5];
        popupViews = new ArrayList<>();
        initPicker();

    }

    /**
     * @param glueLineStartParam
     * @Title UpdateInfos
     * @Description 更新extendView数据（保存的数据）
     * @author wj
     */
    private void UpdateInfos(PointGlueLineStartParam glueLineStartParam) {
        if (glueLineStartParam == null) {
            et_linestart_outGlueTimePrev.setText("");
            et_linestart_outGlueTime.setText("");
            et_linestart_moveSpeed.setText("");

        } else {
            et_linestart_outGlueTimePrev.setText(glueLineStartParam.getOutGlueTimePrev() + "");
            et_linestart_outGlueTime.setText(glueLineStartParam.getOutGlueTime() + "");
            et_linestart_moveSpeed.setText(glueLineStartParam.getMoveSpeed() + "");

            switch_timeMode.setChecked(glueLineStartParam.isTimeMode());

            isGluePort[0].setChecked(glueLineStartParam.getGluePort()[0]);
            isGluePort[1].setChecked(glueLineStartParam.getGluePort()[1]);
            isGluePort[2].setChecked(glueLineStartParam.getGluePort()[2]);
            isGluePort[3].setChecked(glueLineStartParam.getGluePort()[3]);
            isGluePort[4].setChecked(glueLineStartParam.getGluePort()[4]);
        }
    }

    /**
     * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
     */
    private void initPicker() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(
                R.string.activity_glue_line_start));
        mMorenTextView = (TextView) findViewById(R.id.morenfangan);
        mFanganliebiao = (TextView) findViewById(R.id.fanganliebiao);
        mFanganliebiao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(50));
        mMorenTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        mMorenTextView.setText("当前默认方案号(" + defaultNum + ")");
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        iv_loading.setVisibility(View.VISIBLE);
        stub_glue = (ViewStub) findViewById(R.id.stub_glue_line_start);
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = Activity_Init_View;
                handler.sendMessage(msg);
            }
        }, 50);// 延迟1毫秒,然后加载


    }

    protected void setTitleInfos(List<PointGlueLineStartParam> glueStartLists,
                                 View view, int p) {
        title_outGlueTimePrev = (TextView) view.findViewById(R.id.title_outGlueTimePrev);
        title_et_linestart_outGlueTimePrev = (TextView) view.findViewById(R.id.title_et_linestart_outGlueTimePrev);
        activity_ms = (TextView) view.findViewById(R.id.activity_ms);
        activity_fenghao = (TextView) view.findViewById(R.id.activity_fenghao);
        title_outGlueTime = (TextView) view.findViewById(R.id.title_outGlueTime);
        title_et_outGlueTime = (TextView) view.findViewById(R.id.title_et_outGlueTime);
        activity_second_ms = (TextView) view.findViewById(R.id.activity_second_ms);
        activity_second_fenghao = (TextView) view.findViewById(R.id.activity_second_fenghao);
        title_moveSpeed = (TextView) view.findViewById(R.id.title_moveSpeed);
        title_et_linestart_moveSpeed = (TextView) view.findViewById(R.id.title_et_linestart_moveSpeed);
        activity_mm_s = (TextView) view.findViewById(R.id.activity_mm_s);
        activity_four_fenghao = (TextView) view.findViewById(R.id.activity_four_fenghao);
        title_activity_glue_timeMode = (TextView) view.findViewById(R.id.title_activity_glue_timeMode);
        title_et_activity_glue_timeMode = (TextView) view.findViewById(R.id.title_et_activity_glue_timeMode);
        activity_five_fenghao = (TextView) view.findViewById(R.id.activity_five_fenghao);
        activity_glue_port = (TextView) view.findViewById(R.id.activity_glue_port);
        title_et_glue_port = (TextView) view.findViewById(R.id.title_et_glue_port);
        for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
            if (p == pointGlueLineStartParam.get_id()) {
                /*===================== begin =====================*/
                title_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_et_linestart_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_et_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_second_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_second_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_moveSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_et_linestart_moveSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_mm_s.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_four_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_activity_glue_timeMode.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_et_activity_glue_timeMode.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_five_fenghao.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                activity_glue_port.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
                title_et_glue_port.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));

				/*=====================  end =====================*/
                title_outGlueTimePrev.setText(getResources().getString(
                        R.string.activity_glue_outGlueTimePrev)
                        + " ");
                activity_ms.setText(getResources().getString(
                        R.string.activity_ms));
                activity_fenghao.setText(getResources().getString(
                        R.string.activity_fenghao)
                        + " ");
                title_outGlueTime.setText(getResources().getString(
                        R.string.activity_glue_outGlueTime)
                        + " ");
                activity_second_ms.setText(getResources().getString(
                        R.string.activity_ms));
                activity_second_fenghao.setText(getResources().getString(
                        R.string.activity_fenghao)
                        + " ");
                title_moveSpeed.setText(getResources().getString(
                        R.string.activity_glue_moveSpeed)
                        + " ");
                activity_mm_s.setText(getResources().getString(
                        R.string.activity_mm_s));
                activity_four_fenghao.setText(getResources().getString(
                        R.string.activity_fenghao)
                        + " ");
                title_activity_glue_timeMode.setText(getResources().getString(
                        R.string.activity_glue_timeMode)
                        + " ");
                activity_five_fenghao.setText(getResources().getString(
                        R.string.activity_fenghao)
                        + " ");
                activity_glue_port.setText(getResources().getString(
                        R.string.activity_glue_port)
                        + " ");
                for (int j = 0; j < 5; j++) {
                    if (pointGlueLineStartParam.getGluePort()[j]) {
                        GluePort[j] = "开";
                    } else {
                        GluePort[j] = "关";
                    }
                }

                title_et_linestart_outGlueTimePrev.getPaint().setFlags(
                        Paint.UNDERLINE_TEXT_FLAG); // 下划线
                title_et_linestart_outGlueTimePrev.getPaint()
                        .setAntiAlias(true); // 抗锯齿
                title_et_outGlueTime.getPaint().setFlags(
                        Paint.UNDERLINE_TEXT_FLAG); // 下划线
                title_et_outGlueTime.getPaint().setAntiAlias(true); // 抗锯齿
                title_et_linestart_moveSpeed.getPaint().setFlags(
                        Paint.UNDERLINE_TEXT_FLAG); // 下划线
                title_et_linestart_moveSpeed.getPaint().setAntiAlias(true); // 抗锯齿
                title_et_activity_glue_timeMode.getPaint().setFlags(
                        Paint.UNDERLINE_TEXT_FLAG); // 下划线
                title_et_activity_glue_timeMode.getPaint().setAntiAlias(true); // 抗锯齿
                title_et_glue_port.getPaint().setFlags(
                        Paint.UNDERLINE_TEXT_FLAG); // 下划线
                title_et_glue_port.getPaint().setAntiAlias(true); // 抗锯齿

                title_et_linestart_outGlueTimePrev.setText(pointGlueLineStartParam
                        .getOutGlueTimePrev() + "");
                title_et_outGlueTime.setText(pointGlueLineStartParam
                        .getOutGlueTime() + "");
                title_et_linestart_moveSpeed.setText(pointGlueLineStartParam.getMoveSpeed()
                        + "");

                if (pointGlueLineStartParam.isTimeMode()) {
                    title_et_activity_glue_timeMode.setText("是");
                } else {
                    title_et_activity_glue_timeMode.setText("否");
                }
                title_et_glue_port.setText(GluePort[0] + GluePort[1]
                        + GluePort[2] + GluePort[3] + GluePort[4]);
            }
        }
    }

    /**
     * @Title SetDateAndRefreshUI
     * @Description 打开extendview的时候设置界面内容，显示最新的方案数据而不是没有保存的数据,没有得到保存的方案
     * @author wj
     */
    protected void SetDateAndRefreshUI() {
        glueStartLists = glueStartDao.findAllGlueLineStartParams();
        ArrayList<Integer> list = new ArrayList<>();
        for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
            list.add(pointGlueLineStartParam.get_id());
        }
        System.out.println("存放主键id的集合---->" + list);
        System.out.println("当前选择的方案号---->" + currentTaskNum);
        System.out.println("list是否存在------------》"
                + list.contains(currentTaskNum));
        if (list.contains(currentTaskNum)) {
            // 已经保存在数据库中的数据
            for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                if (currentTaskNum == pointGlueLineStartParam.get_id()) {
                    View extendView = popupListView.getItemViews()
                            .get(currentClickNum).getExtendView();
                    initView(extendView);
                    UpdateInfos(pointGlueLineStartParam);
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
     * @Title save
     * @Description 保存信息到Param的一个对象中，并更新数据库数据
     * @author wj
     */
    protected void save() {
        View extendView = popupListView.getItemViews().get(currentClickNum)
                .getExtendView();
        glueStartLists = glueStartDao.findAllGlueLineStartParams();
        ArrayList<Integer> list = new ArrayList<>();
        for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
            list.add(pointGlueLineStartParam.get_id());
        }
        // 判空
        isOk = isEditClean(extendView);
        if (isOk) {

            PointGlueLineStartParam upglueStartParam = getLineStart(extendView);
            if (glueStartLists.contains(upglueStartParam)) {
                // 默认已经存在的方案但是不能创建方案只能改变默认方案号
                if (list.contains(currentTaskNum)) {
                    isExist = true;
                }
                // 保存的方案已经存在但不是当前编辑的方案
                if (currentTaskNum != glueStartLists.get(
                        glueStartLists.indexOf(upglueStartParam)).get_id()) {
                    ToastUtil.displayPromptInfo(GlueLineStartActivity.this,
                            getResources()
                                    .getString(R.string.task_is_exist_yes));
                }
            } else {

                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                    if (currentTaskNum == pointGlueLineStartParam.get_id()) {// 说明之前插入过
                        flag = true;
                    }
                }
                if (flag) {
                    // 更新数据
                    int rowid = glueStartDao
                            .upDateGlueLineStart(upglueStartParam);
                    update_id.put(upglueStartParam.get_id(), upglueStartParam);
                    // System.out.println("修改的方案号为："+upglueAlone.get_id());
                } else {
                    // 插入一条数据
                    long rowid = glueStartDao
                            .insertGlueLineStart(upglueStartParam);
                    firstExist = true;
                    glueStartLists = glueStartDao.findAllGlueLineStartParams();
                    Log.i(TAG, "保存之后新方案-->" + glueStartLists.toString());
                    ToastUtil.displayPromptInfo(GlueLineStartActivity.this,
                            getResources().getString(R.string.save_success));
                    list.clear();
                    for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                        list.add(pointGlueLineStartParam.get_id());
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
        glueStartLists = glueStartDao.findAllGlueLineStartParams();
        // popupListView->pupupview->title
        for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {

            if (currentTaskNum == pointGlueLineStartParam.get_id()) {
                // 需要设置两个view，因为view内容相同但是parent不同
                View titleViewItem = popupListView.getItemViews()
                        .get(currentClickNum).getPopupView();
                View titleViewExtend = popupListView.getItemViews()
                        .get(currentClickNum).getExtendPopupView();
                setTitleInfos(glueStartLists, titleViewItem, currentTaskNum);
                setTitleInfos(glueStartLists, titleViewExtend, currentTaskNum);
            }
        }
    }

    /**
     * @param extendView
     * @Title initView
     * @Description 初始化当前extendView视图
     * @author wj
     */
    protected void initView(View extendView) {
        et_linestart_outGlueTimePrev = (EditText) extendView
                .findViewById(R.id.et_linestart_outGlueTimePrev);
        et_linestart_outGlueTime = (EditText) extendView
                .findViewById(R.id.et_linestart_outGlueTime);
        et_linestart_moveSpeed = (EditText) extendView
                .findViewById(R.id.et_linestart_moveSpeed);

        switch_timeMode = (ToggleButton) extendView
                .findViewById(R.id.switch_timeMode);

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

        tv_outGlueTimePrev = (TextView) extendView.findViewById(R.id.tv_outGlueTimePrev);
        extend_line_start_ms = (TextView) extendView.findViewById(R.id.extend_line_start_ms);
        tv_outGlueTime = (TextView) extendView.findViewById(R.id.tv_outGlueTime);
        tv_outGlueTime_ms = (TextView) extendView.findViewById(R.id.tv_outGlueTime_ms);
        tv_moveSpeed = (TextView) extendView.findViewById(R.id.tv_moveSpeed);
        extend_line_start_mms = (TextView) extendView.findViewById(R.id.extend_line_start_mms);
        extend_line_start_timeMode = (TextView) extendView.findViewById(R.id.extend_line_start_timeMode);
        extend_line_start_dianjiao1 = (TextView) extendView.findViewById(R.id.extend_line_start_dianjiao1);
        extend_line_start_dianjiao2 = (TextView) extendView.findViewById(R.id.extend_line_start_dianjiao2);
        extend_line_start_dianjiao3 = (TextView) extendView.findViewById(R.id.extend_line_start_dianjiao3);
        extend_line_start_dianjiao4 = (TextView) extendView.findViewById(R.id.extend_line_start_dianjiao4);
        extend_line_start_dianjiao5 = (TextView) extendView.findViewById(R.id.extend_line_start_dianjiao5);
        extend_default = (TextView) extendView.findViewById(R.id.extend_default);
        extend_save = (TextView) extendView.findViewById(R.id.extend_save);
        /*===================== begin =====================*/

        et_linestart_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_linestart_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_linestart_moveSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_outGlueTimePrev.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_outGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_outGlueTime_ms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_moveSpeed.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_mms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_timeMode.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_dianjiao1.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_dianjiao2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_dianjiao3.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_dianjiao4.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_line_start_dianjiao5.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_default.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_save.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		/*=====================  end =====================*/
    }

    /**
     * 将页面上的数据保存到一个PointGlueLineStartParam对象中
     *
     * @return PointGlueLineStartParam
     */
    private PointGlueLineStartParam getLineStart(View extendView) {
        glueStart = new PointGlueLineStartParam();
        et_linestart_outGlueTimePrev = (EditText) extendView
                .findViewById(R.id.et_linestart_outGlueTimePrev);
        et_linestart_outGlueTime = (EditText) extendView
                .findViewById(R.id.et_linestart_outGlueTime);
        et_linestart_moveSpeed = (EditText) extendView
                .findViewById(R.id.et_linestart_moveSpeed);

        switch_timeMode = (ToggleButton) extendView
                .findViewById(R.id.switch_timeMode);

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
            outGlueTimePrevInt = Integer.parseInt(et_linestart_outGlueTimePrev
                    .getText().toString());
        } catch (NumberFormatException e) {
            outGlueTimePrevInt = 0;
        }
        try {
            outGlueTimeInt = Integer.parseInt(et_linestart_outGlueTime
                    .getText().toString());
        } catch (NumberFormatException e) {
            outGlueTimeInt = 0;
        }
        try {
            moveSpeedInt = Integer.parseInt(et_linestart_moveSpeed.getText()
                    .toString());
            if (moveSpeedInt == 0) {
                moveSpeedInt = 1;
            }
        } catch (NumberFormatException e) {
            moveSpeedInt = 1;
        }
        glueStart.setOutGlueTimePrev(outGlueTimePrevInt);
        glueStart.setOutGlueTime(outGlueTimeInt);
        glueStart.setMoveSpeed(moveSpeedInt);
        glueStart.setTimeMode(switch_timeMode.isChecked());

        glueBoolean[0] = isGluePort[0].isChecked();
        glueBoolean[1] = isGluePort[1].isChecked();
        glueBoolean[2] = isGluePort[2].isChecked();
        glueBoolean[3] = isGluePort[3].isChecked();
        glueBoolean[4] = isGluePort[4].isChecked();
        glueStart.setGluePort(glueBoolean);
        glueStart.set_id(currentTaskNum);

        return glueStart;
    }

    /**
     * @param extendView
     * @return false表示为空, true表示都有数据
     * @Title isEditClean
     * @Description 判断输入框是否为空
     * @author wj
     */
    private boolean isEditClean(View extendView) {
        et_linestart_outGlueTimePrev = (EditText) extendView
                .findViewById(R.id.et_linestart_outGlueTimePrev);
        et_linestart_outGlueTime = (EditText) extendView
                .findViewById(R.id.et_linestart_outGlueTime);
        et_linestart_moveSpeed = (EditText) extendView
                .findViewById(R.id.et_linestart_moveSpeed);

        if ("".equals(et_linestart_outGlueTimePrev.getText().toString())) {
            return false;
        } else if ("".equals(et_linestart_outGlueTime.getText().toString())) {
            return false;
        } else if ("".equals(et_linestart_moveSpeed.getText().toString())) {
            return false;
        }
        return true;
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

    /**
     * @Title complete
     * @Description 最终完成返回
     * @author wj
     */
    private void complete() {
        // TODO Auto-generated method stub
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
        point.setPointParam(glueStartDao.getPointGlueLineStartParamByID(mIndex));
        System.out.println("返回的Point为================》" + point);

        List<Map<Integer, PointGlueLineStartParam>> list = new ArrayList<Map<Integer, PointGlueLineStartParam>>();
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

    private class RevHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SocketInputThread.SocketError) {
                //wifi中断
                System.out.println("wifi连接断开。。");
                SocketThreadManager.releaseInstance();
                System.out.println("单例被释放了-----------------------------");
                //设置全局变量，跟新ui
                userApplication.setWifiConnecting(false);
//				WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
                ToastUtil.displayPromptInfo(GlueLineStartActivity.this, "wifi连接断开。。");
            } else if (msg.what == Activity_Init_View) {
                View activity_glue_popuplistview = stub_glue.inflate();
                popupListView = (PopupListView) activity_glue_popuplistview.findViewById(R.id.popupListView);
                popupListView.init(null);
                CreatePopupViews();
            }
        }
    }

    private void CreatePopupViews() {
        // 初始化创建10个popupView
        for (int i = 0; i < 10; i++) {
            p = i + 1;
            PopupView popupView = new PopupView(this,
                    R.layout.popup_view_item_glue_start) {

                @Override
                public void setViewsElements(View view) {
                    // TextView textView = (TextView) view
                    // .findViewById(R.id.title);
                    glueStartLists = glueStartDao.findAllGlueLineStartParams();
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
                    } else if (p == 8) {
                        title_num.setImageResource(R.drawable.green8);
                    } else if (p == 9) {
                        title_num.setImageResource(R.drawable.green9);
                    } else if (p == 10) {
                        title_num.setImageResource(R.drawable.green10);
                    }
                }

                @Override
                public View setExtendView(View view) {
                    if (view == null) {
                        extendView = LayoutInflater.from(
                                getApplicationContext()).inflate(
                                R.layout.glue_start_extend_view, null);
                        int size = glueStartLists.size();
                        while (size > 0) {
                            size--;
                            if (p == 1) {// 方案列表第一位对应一号方案
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 2) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 3) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 4) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 5) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 6) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 7) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 8) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 9) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
                                    }
                                }
                            } else if (p == 10) {
                                initView(extendView);
                                for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
                                    if (p == pointGlueLineStartParam.get_id()) {
                                        UpdateInfos(pointGlueLineStartParam);
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
                    et_linestart_outGlueTimePrev = (EditText) extendView
                            .findViewById(R.id.et_linestart_outGlueTimePrev);
                    et_linestart_outGlueTime = (EditText) extendView
                            .findViewById(R.id.et_linestart_outGlueTime);
                    et_linestart_moveSpeed = (EditText) extendView
                            .findViewById(R.id.et_linestart_moveSpeed);
                    switch_timeMode = (ToggleButton) extendView
                            .findViewById(R.id.switch_timeMode);

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
                    // 设置出胶前延时的默认值和最大最小值
                    et_linestart_outGlueTimePrev
                            .addTextChangedListener(new MaxMinEditWatcher(
                                    PointConfigParam.GlueLineStart.OutGlueTimePrevMax,
                                    PointConfigParam.GlueLineStart.GlueLineStartMin,
                                    et_linestart_outGlueTimePrev));
                    et_linestart_outGlueTimePrev
                            .setOnFocusChangeListener(new MaxMinFocusChangeListener(
                                    PointConfigParam.GlueLineStart.OutGlueTimePrevMax,
                                    PointConfigParam.GlueLineStart.GlueLineStartMin,
                                    et_linestart_outGlueTimePrev));
                    et_linestart_outGlueTimePrev.setSelectAllOnFocus(true);

                    // 设置出胶后延时的默认值和最大最小值
                    et_linestart_outGlueTime
                            .addTextChangedListener(new MaxMinEditWatcher(
                                    PointConfigParam.GlueLineStart.OutGlueTimeMax,
                                    PointConfigParam.GlueLineStart.GlueLineStartMin,
                                    et_linestart_outGlueTime));
                    et_linestart_outGlueTime
                            .setOnFocusChangeListener(new MaxMinFocusChangeListener(
                                    PointConfigParam.GlueLineStart.OutGlueTimeMax,
                                    PointConfigParam.GlueLineStart.GlueLineStartMin,
                                    et_linestart_outGlueTime));
                    et_linestart_outGlueTime.setSelectAllOnFocus(true);

                    // 设置轨迹速度的默认值和最大最小值
                    et_linestart_moveSpeed
                            .addTextChangedListener(new MaxMinEditWatcher(
                                    RobotParam.INSTANCE.GetXSpeed(),
                                    1,
                                    et_linestart_moveSpeed));
                    et_linestart_moveSpeed
                            .setOnFocusChangeListener(new MaxMinFocusChangeListener(
                                    RobotParam.INSTANCE.GetXSpeed(),
                                    1,
                                    et_linestart_moveSpeed));
                    et_linestart_moveSpeed.setSelectAllOnFocus(true);

                    rl_moren = (RelativeLayout) extendView
                            .findViewById(R.id.rl_moren);
                    iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
                    rl_save = (RelativeLayout) extendView
                            .findViewById(R.id.rl_save);// 保存按钮
                    iv_moren = (ImageView) extendView
                            .findViewById(R.id.iv_moren);// 默认按钮
                    rl_moren.setOnClickListener(this);
                    rl_save.setOnClickListener(this);
                    et_linestart_outGlueTimePrev.setSelectAllOnFocus(true);
                    et_linestart_outGlueTime.setSelectAllOnFocus(true);
                    et_linestart_moveSpeed.setSelectAllOnFocus(true);
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
                                                GlueLineStartActivity.this,
                                                SettingParam.DefaultNum.ParamGlueLineStartNumber,
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
        for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
            list.add(pointGlueLineStartParam.get_id());
        }
        popupListView.setSelectedEnable(list);
        popupListView.setOnClickPositionChanged(new PopupListView.OnClickPositionChanged() {
            @Override
            public void getCurrentPositon(int position) {
                currentTaskNum = position + 1;
                currentClickNum = position;
            }
        });
        popupListView.setOnZoomInListener(new PopupListView.OnZoomInChanged() {

            @Override
            public void getZoomState(Boolean isZoomIn) {
                if (isZoomIn) {
                    // 设置界面
                    SetDateAndRefreshUI();

                }
            }
        });
        rl_back.setOnClickListener(this);
        iv_loading.setVisibility(View.INVISIBLE);
    }
}
