package com.mingseal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditFloatWatcher;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeFloatListener;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.nio.ByteBuffer;


public class FuncListActivity extends AutoLayoutActivity implements View.OnClickListener {

    private static final String TAG = "FunclistActivity";
    private TextView mTv_title;
    private TextView tv_task_number;
    private EditText et_download_tasknumber;
    private TextView et_nRunNum;
    private TextView tv_nZeroCheck;
    private RadioButton rb_zero;
    private RadioButton rb_linestart;
    private RadioButton rb_lineend;
    private TextView tv_accelerate_time;
    private EditText et_download_accelerate_time;
    private TextView tv_mms;
    private TextView tv_xy;
    private EditText et_download_xy_move;
    private TextView tv_xy_kongzou;
    private TextView tv_decelerate_time;
    private EditText et_download_decelerate_time;
    private TextView tv_mms2;
    private TextView tv_z;
    private EditText et_download_z_move;
    private TextView tv_z_kongzou;
    private TextView tv_inflexion_time;
    private EditText et_download_inflexion_time;
    private TextView tv_mm;
    private TextView tv_max;
    private EditText et_download_max_accelerate_move;
    private TextView tv_guaidian;
    private TextView tv_nAutoRunTime;
    private EditText et_nAutoRunTime;
    private TextView tv_s;
    private EditText et_nBaseUnit;
    private TextView tv_nBaseHeight;
    private EditText et_nBaseHeight;
    private TextView tv_mm2;
    private TextView tv_s2;
    private EditText et_nNoHardOutGlueTime;
    private TextView tv_nNoHardOutGlueInterval;
    private EditText et_nNoHardOutGlueInterval;
    private TextView tv_s3;
    private ToggleButton switch_bBackDefault;
    private TextView tv_bBackDefault;
    private TextView tv_bRunNumZero;
    private ToggleButton switch_bRunNumZero;
    private ToggleButton switch_bTaskBack;
    private TextView tv_bTaskBack;
    private TextView tv_bTaskDelete;
    private ToggleButton switch_bTaskDelete;
    private ToggleButton switch_nPauseType;
    private TextView tv_nPauseType;
    private RelativeLayout rl_save;
    private TextView extend_save;
    private TextView tv_nRunNum;
    private TextView tv_nBaseUnit;
    private TextView tv_nNoHardOutGlueTime;
    private RelativeLayout rl_back;
    private Handler handler;
    private UserApplication userApplication;
    /**
     * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
     */
    private boolean isNull = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_list);
        userApplication = (UserApplication) getApplication();
        MessageMgr.INSTANCE.getFunclist();
        initView();
        initData();
        handler = new RevHandler();
        // 线程管理单例初始化
        SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
    }

    private void initData() {
        et_nRunNum.setText(OrderParam.INSTANCE.getnRunNum() + "");
        //零点校正
        if (OrderParam.INSTANCE.getnZeroCheck() == 0) {//零点
            rb_zero.setChecked(true);
        } else if (OrderParam.INSTANCE.getnZeroCheck() == 1) {//起始点
            rb_linestart.setChecked(true);
        } else {
            rb_lineend.setChecked(true);//结束点
        }
        //运行次数清零
        if (OrderParam.INSTANCE.isbRunNumZero()) {
            switch_bRunNumZero.setChecked(true);
        } else {
            switch_bRunNumZero.setChecked(false);
        }
        //任务还原
        if (OrderParam.INSTANCE.isbTaskBack()) {
            switch_bTaskBack.setChecked(true);
        } else {
            switch_bTaskBack.setChecked(false);
        }
        //任务删除
        if (OrderParam.INSTANCE.isbTaskDelete()) {
            switch_bTaskDelete.setChecked(true);
        } else {
            switch_bTaskDelete.setChecked(false);
        }
        //暂停模式
        if (OrderParam.INSTANCE.getnPauseType() == 1) {//实时暂停
            switch_nPauseType.setChecked(true);
        } else {
            switch_nPauseType.setChecked(false);//节点暂停
        }
        //恢复出厂设置
        if (OrderParam.INSTANCE.isbBackDefault()) {
            switch_bBackDefault.setChecked(true);
        } else {
            switch_bBackDefault.setChecked(false);
        }
    }

    private void initView() {
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_title.setText(getResources().getString(R.string.activity_setting_task));
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_task_number = (TextView) findViewById(R.id.tv_task_number);
        tv_nRunNum = (TextView) findViewById(R.id.tv_nRunNum);
        tv_nBaseUnit = (TextView) findViewById(R.id.tv_nBaseUnit);
        tv_nNoHardOutGlueTime = (TextView) findViewById(R.id.tv_nNoHardOutGlueTime);
        et_download_tasknumber = (EditText) findViewById(R.id.et_download_tasknumber);
        et_nRunNum = (TextView) findViewById(R.id.et_nRunNum);
        tv_nZeroCheck = (TextView) findViewById(R.id.tv_nZeroCheck);
        rb_zero = (RadioButton) findViewById(R.id.rb_zero);
        rb_linestart = (RadioButton) findViewById(R.id.rb_linestart);
        rb_lineend = (RadioButton) findViewById(R.id.rb_lineend);
        tv_accelerate_time = (TextView) findViewById(R.id.tv_accelerate_time);
        et_download_accelerate_time = (EditText) findViewById(R.id.et_download_accelerate_time);
        tv_mms = (TextView) findViewById(R.id.tv_mms);
        tv_xy = (TextView) findViewById(R.id.tv_xy);
        et_download_xy_move = (EditText) findViewById(R.id.et_download_xy_move);
        tv_xy_kongzou = (TextView) findViewById(R.id.tv_xy_kongzou);
        tv_decelerate_time = (TextView) findViewById(R.id.tv_decelerate_time);
        et_download_decelerate_time = (EditText) findViewById(R.id.et_download_decelerate_time);
        tv_mms2 = (TextView) findViewById(R.id.tv_mms2);
        tv_z = (TextView) findViewById(R.id.tv_z);
        et_download_z_move = (EditText) findViewById(R.id.et_download_z_move);
        tv_z_kongzou = (TextView) findViewById(R.id.tv_z_kongzou);
        tv_inflexion_time = (TextView) findViewById(R.id.tv_inflexion_time);
        et_download_inflexion_time = (EditText) findViewById(R.id.et_download_inflexion_time);
        tv_mm = (TextView) findViewById(R.id.tv_mm);
        tv_max = (TextView) findViewById(R.id.tv_max);
        et_download_max_accelerate_move = (EditText) findViewById(R.id.et_download_max_accelerate_move);
        tv_guaidian = (TextView) findViewById(R.id.tv_guaidian);
        tv_nAutoRunTime = (TextView) findViewById(R.id.tv_nAutoRunTime);
        et_nAutoRunTime = (EditText) findViewById(R.id.et_nAutoRunTime);
        tv_s = (TextView) findViewById(R.id.tv_s);
        et_nBaseUnit = (EditText) findViewById(R.id.et_nBaseUnit);
        tv_nBaseHeight = (TextView) findViewById(R.id.tv_nBaseHeight);
        et_nBaseHeight = (EditText) findViewById(R.id.et_nBaseHeight);
        tv_mm2 = (TextView) findViewById(R.id.tv_mm2);
        tv_s2 = (TextView) findViewById(R.id.tv_s2);
        et_nNoHardOutGlueTime = (EditText) findViewById(R.id.et_nNoHardOutGlueTime);
        tv_nNoHardOutGlueInterval = (TextView) findViewById(R.id.tv_nNoHardOutGlueInterval);
        et_nNoHardOutGlueInterval = (EditText) findViewById(R.id.et_nNoHardOutGlueInterval);
        tv_s3 = (TextView) findViewById(R.id.tv_s3);
        switch_bBackDefault = (ToggleButton) findViewById(R.id.switch_bBackDefault);
        tv_bBackDefault = (TextView) findViewById(R.id.tv_bBackDefault);
        tv_bRunNumZero = (TextView) findViewById(R.id.tv_bRunNumZero);
        switch_bRunNumZero = (ToggleButton) findViewById(R.id.switch_bRunNumZero);
        switch_bTaskBack = (ToggleButton) findViewById(R.id.switch_bTaskBack);
        tv_bTaskBack = (TextView) findViewById(R.id.tv_bTaskBack);
        tv_bTaskDelete = (TextView) findViewById(R.id.tv_bTaskDelete);
        switch_bTaskDelete = (ToggleButton) findViewById(R.id.switch_bTaskDelete);
        switch_nPauseType = (ToggleButton) findViewById(R.id.switch_nPauseType);
        tv_nPauseType = (TextView) findViewById(R.id.tv_nPauseType);
        rl_save = (RelativeLayout) findViewById(R.id.rl_save);
        extend_save = (TextView) findViewById(R.id.extend_save);
        /*===================== begin =====================*/
        tv_task_number.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_tasknumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_nRunNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nZeroCheck.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_accelerate_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_accelerate_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_mms.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_xy.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_xy_move.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_xy_kongzou.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_decelerate_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_decelerate_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_mms2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_z.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_z_move.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_z_kongzou.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_inflexion_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_inflexion_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_mm.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_max.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_download_max_accelerate_move.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_guaidian.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nAutoRunTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_nAutoRunTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_s.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_nBaseUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nBaseHeight.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_nBaseHeight.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_mm2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_s2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_nNoHardOutGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nNoHardOutGlueInterval.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        et_nNoHardOutGlueInterval.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_s3.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_bBackDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_bRunNumZero.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_bTaskBack.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_bTaskDelete.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nPauseType.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        extend_save.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nRunNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nBaseUnit.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        tv_nNoHardOutGlueTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        rb_zero.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        rb_linestart.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        rb_lineend.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
        /*=====================  end =====================*/
        //任务号
        et_download_tasknumber.addTextChangedListener(new MaxMinEditWatcher(120, 1, et_download_tasknumber));
        et_download_tasknumber.setOnFocusChangeListener(new MaxMinFocusChangeListener(120, 1, et_download_tasknumber));
        et_download_tasknumber.setSelectAllOnFocus(true);
        et_download_tasknumber.setText(OrderParam.INSTANCE.getnTaskNum() + "");
        //加速度
        et_download_accelerate_time.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_download_accelerate_time));
        et_download_accelerate_time.setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_download_accelerate_time));
        et_download_accelerate_time.setSelectAllOnFocus(true);
        et_download_accelerate_time.setText(OrderParam.INSTANCE.getnAccelerate() + "");
        //xy轴空走速度
        et_download_xy_move.addTextChangedListener(new MaxMinEditWatcher(800, 1, et_download_xy_move));
        et_download_xy_move.setOnFocusChangeListener(new MaxMinFocusChangeListener(800, 1, et_download_xy_move));
        et_download_xy_move.setSelectAllOnFocus(true);
        et_download_xy_move.setText(OrderParam.INSTANCE.getnXYNullSpeed() + "");
        //减速度
        et_download_decelerate_time.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_download_decelerate_time));
        et_download_decelerate_time.setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_download_decelerate_time));
        et_download_decelerate_time.setSelectAllOnFocus(true);
        et_download_decelerate_time.setText(OrderParam.INSTANCE.getnDecelerate() + "");
        //z轴空走速度
        et_download_z_move.addTextChangedListener(new MaxMinEditWatcher(400, 1, et_download_z_move));
        et_download_z_move.setOnFocusChangeListener(new MaxMinFocusChangeListener(400, 1, et_download_z_move));
        et_download_z_move.setSelectAllOnFocus(true);
        et_download_z_move.setText(OrderParam.INSTANCE.getnZNullSpeed() + "");
        //y轴校正距离
        et_download_inflexion_time.addTextChangedListener(new MaxMinEditWatcher(RobotParam.INSTANCE.GetYJourney(), 1, et_download_inflexion_time));
        et_download_inflexion_time.setOnFocusChangeListener(new MaxMinFocusChangeListener(RobotParam.INSTANCE.GetYJourney(), 1, et_download_inflexion_time));
        et_download_inflexion_time.setSelectAllOnFocus(true);
        et_download_inflexion_time.setText(OrderParam.INSTANCE.getnYCheckDis() + "");
        //拐点最大加速度
        et_download_max_accelerate_move.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_download_max_accelerate_move));
        et_download_max_accelerate_move
                .setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_download_max_accelerate_move));
        et_download_max_accelerate_move.setSelectAllOnFocus(true);
        et_download_max_accelerate_move.setText(OrderParam.INSTANCE.getnTurnAccelerateMax() + "");
        //延时运行时间
        et_nAutoRunTime.addTextChangedListener(new MaxMinEditFloatWatcher(200, 0, et_nAutoRunTime));
        et_nAutoRunTime
                .setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(200, 0, et_nAutoRunTime));
        et_nAutoRunTime.setSelectAllOnFocus(true);
        et_nAutoRunTime.setText((float)OrderParam.INSTANCE.getnAutoRunTime()/10 + "");
        //基点调整单位
        et_nBaseUnit.addTextChangedListener(new MaxMinEditWatcher(50, 1, et_nBaseUnit));
        et_nBaseUnit
                .setOnFocusChangeListener(new MaxMinFocusChangeListener(50, 1, et_nBaseUnit));
        et_nBaseUnit.setSelectAllOnFocus(true);
        et_nBaseUnit.setText(OrderParam.INSTANCE.getnBaseUnit() + "");
        //基点调整高度
        et_nBaseHeight.addTextChangedListener(new MaxMinEditWatcher(10, 0, et_nBaseHeight));
        et_nBaseHeight
                .setOnFocusChangeListener(new MaxMinFocusChangeListener(10, 0, et_nBaseHeight));
        et_nBaseHeight.setSelectAllOnFocus(true);
        et_nBaseHeight.setText(OrderParam.INSTANCE.getnBaseHeight() + "");
        //防止硬化出胶时间
        et_nNoHardOutGlueTime.addTextChangedListener(new MaxMinEditFloatWatcher(200, 0, et_nNoHardOutGlueTime));
        et_nNoHardOutGlueTime
                .setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(200, 0, et_nNoHardOutGlueTime));
        et_nNoHardOutGlueTime.setSelectAllOnFocus(true);
        et_nNoHardOutGlueTime.setText((float)OrderParam.INSTANCE.getnNoHardOutGlueTime()/10 + "");
        //防止硬化出胶间隔
        et_nNoHardOutGlueInterval.addTextChangedListener(new MaxMinEditFloatWatcher(200, 0, et_nNoHardOutGlueInterval));
        et_nNoHardOutGlueInterval
                .setOnFocusChangeListener(new MaxMinFocusChangeFloatListener(200, 0, et_nNoHardOutGlueInterval));
        et_nNoHardOutGlueInterval.setSelectAllOnFocus(true);
        et_nNoHardOutGlueInterval.setText((float)OrderParam.INSTANCE.getnNoHardOutGlueInterval()/10 + "");

        rl_save.setOnClickListener(this);
        rl_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back://返回不改变数据
                FuncListActivity.this.finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
                break;
            case R.id.rl_save:
                //写功能列表
                writeFunclist();
                break;
        }

    }

    private void writeFunclist() {
        isNull = isEditClean();
        if (isNull) {
                saveBackActivity();
        } else {
            ToastUtil.displayPromptInfo(FuncListActivity.this, getResources().getString(R.string.data_is_null));
        }
    }

    private void saveBackActivity() {
        OrderParam.INSTANCE.setnTaskNum(Integer.parseInt(et_download_tasknumber.getText().toString().trim()));//设置任务号
        if (rb_zero.isChecked()){//零点
            OrderParam.INSTANCE.setnZeroCheck(0);
        }else if(rb_linestart.isChecked()){//起始点
            OrderParam.INSTANCE.setnZeroCheck(1);
        }else if (rb_lineend.isChecked()){//结束点
            OrderParam.INSTANCE.setnZeroCheck(2);
        }
        OrderParam.INSTANCE.setnAccelerate(Integer.parseInt(et_download_accelerate_time.getText().toString().trim()));//设置加速度
        OrderParam.INSTANCE.setnDecelerate(Integer.parseInt(et_download_decelerate_time.getText().toString().trim()));//设置减速度
        OrderParam.INSTANCE.setnXYNullSpeed(Integer.parseInt(et_download_xy_move.getText().toString().trim()));//设置xy轴速度
        OrderParam.INSTANCE.setnZNullSpeed(Integer.parseInt(et_download_z_move.getText().toString().trim()));//设置z轴速度
        OrderParam.INSTANCE.setnYCheckDis(Integer.parseInt(et_download_inflexion_time.getText().toString().trim()));//设置Y轴校正距离
        OrderParam.INSTANCE.setnTurnAccelerateMax(Integer.parseInt(et_download_max_accelerate_move.getText().toString().trim()));//设置拐点最大加速度
        OrderParam.INSTANCE.setnAutoRunTime((int) Float.parseFloat(et_nAutoRunTime.getText().toString().trim())*10);//设置延时运行时间
        OrderParam.INSTANCE.setnBaseUnit(Integer.parseInt(et_nBaseUnit.getText().toString().trim()));//设置基点调整单位
        OrderParam.INSTANCE.setnBaseHeight(Integer.parseInt(et_nBaseHeight.getText().toString().trim()));//设置基点调整高度
        OrderParam.INSTANCE.setnNoHardOutGlueTime((int) Float.parseFloat(et_nNoHardOutGlueTime.getText().toString().trim())*10);//设置防止硬化出胶时间
        OrderParam.INSTANCE.setnNoHardOutGlueInterval((int) Float.parseFloat(et_nNoHardOutGlueInterval.getText().toString().trim())*10);//设置防止硬化出胶间隔
        if (switch_bBackDefault.isChecked()){
            OrderParam.INSTANCE.setbBackDefault(true);//恢复出厂设置
        }else {
            OrderParam.INSTANCE.setbBackDefault(false);//恢复出厂设置
        }
        if (switch_bRunNumZero.isChecked()){//运行次数清零
            OrderParam.INSTANCE.setbRunNumZero(true);
        }else {
            OrderParam.INSTANCE.setbRunNumZero(false);
        }
        if (switch_bTaskBack.isChecked()){//任务还原
            OrderParam.INSTANCE.setbTaskBack(true);
        }else {
            OrderParam.INSTANCE.setbTaskBack(false);
        }
        if (switch_bTaskDelete.isChecked()){//任务删除
            OrderParam.INSTANCE.setbTaskDelete(true);
        }else {
            OrderParam.INSTANCE.setbTaskDelete(false);
        }
        if (switch_nPauseType.isChecked()){//暂停模式
            OrderParam.INSTANCE.setnPauseType(1);
        }else {
            OrderParam.INSTANCE.setnPauseType(0);
        }
        MessageMgr.INSTANCE.writeFunclist();
    }

    /**
     * @Title isEditNull
     * @Description 判断输入框是否为空
     * @return false表示为空,true表示都有数据
     */
    private boolean isEditClean() {
        if ("".equals(et_download_tasknumber.getText().toString())) {
            return false;
        } else if ("".equals(et_download_accelerate_time.getText().toString())) {
            return false;
        } else if ("".equals(et_download_xy_move.getText().toString())) {
            return false;
        } else if ("".equals(et_download_decelerate_time.getText().toString())) {
            return false;
        } else if ("".equals(et_download_z_move.getText().toString())) {
            return false;
        } else if ("".equals(et_download_inflexion_time.getText().toString())) {
            return false;
        } else if ("".equals(et_download_max_accelerate_move.getText().toString())) {
            return false;
        } else if ("".equals(et_nAutoRunTime.getText().toString())) {
            return false;
        } else if ("".equals(et_nBaseUnit.getText().toString())) {
            return false;
        }else if ("".equals(et_nBaseHeight.getText().toString())) {
            return false;
        }else if ("".equals(et_nNoHardOutGlueTime.getText().toString())) {
            return false;
        }else if ("".equals(et_nNoHardOutGlueInterval.getText().toString())) {
            return false;
        }
        return true;
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
                disPlayInfoAfterGetMsg(buffer);
                // new ManagingMessage().execute(buffer);
            }else if (msg.what==SocketInputThread.SocketError){
                //wifi中断
                System.out.println("wifi连接断开。。");
                SocketThreadManager.releaseInstance();
                System.out.println("单例被释放了-----------------------------");
                //设置全局变量，跟新ui
                userApplication.setWifiConnecting(false);
//				WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
                ToastUtil.displayPromptInfo(FuncListActivity.this,"wifi连接断开。。");
            }
        }
    }

    private void disPlayInfoAfterGetMsg(byte[] revBuffer) {
        switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
            case 0:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "校验失败");
                break;
            case 1: {
                int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
                if (revBuffer[2] == 0x2E) {
                    ToastUtil.displayPromptInfo(FuncListActivity.this, "读取功能列表成功！");
                    refreshData();
                }else if (cmdFlag==0x7A47){
//                    ToastUtil.displayPromptInfo(FuncListActivity.this,"写入功能列表成功！");
                    this.finish();
                }
            }
            break;
            case 40101:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "非法功能");
                break;
            case 40102:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "非法数据地址");
                break;
            case 40103:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "非法数据");
                break;
            case 40105:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "设备忙");
                break;
            case 40109:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "急停中");
                break;
            case 40110:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "X轴光电报警");
                break;
            case 40111:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "Y轴光电报警");
                break;
            case 40112:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "Z轴光电报警");
                break;
            case 40113:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "U轴光电报警");
                break;
            case 40114:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "行程超限报警");
                break;
            case 40115:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "任务下载失败");
                break;
            case 40116:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "任务上传失败");
                break;
            case 40117:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "任务模拟失败");
                break;
            case 40118:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "示教指令错误");
                break;
            case 40119:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "循迹定位失败");
                break;
            case 40120:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "任务号不可用");
                break;
            case 40121:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "初始化失败");
                break;
            case 40122:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "API版本错误");
                break;
            case 40123:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "程序升级失败");
                break;
            case 40124:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "系统损坏");
                break;
            case 40125:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "任务未加载");
                break;
            case 40126:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "(Z轴)基点抬起高度过高");
                break;
            case 40127:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "等待输入超时");
                break;
            default:
                ToastUtil.displayPromptInfo(FuncListActivity.this, "未知错误");
                break;
        }
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        et_download_tasknumber.setText(OrderParam.INSTANCE.getnTaskNum() + "");
        et_download_accelerate_time.setText(OrderParam.INSTANCE.getnAccelerate() + "");
        et_download_xy_move.setText(OrderParam.INSTANCE.getnXYNullSpeed() + "");
        et_download_decelerate_time.setText(OrderParam.INSTANCE.getnDecelerate() + "");
        et_download_z_move.setText(OrderParam.INSTANCE.getnZNullSpeed() + "");
        et_download_inflexion_time.setText(OrderParam.INSTANCE.getnYCheckDis() + "");
        et_download_max_accelerate_move.setText(OrderParam.INSTANCE.getnTurnAccelerateMax() + "");
        et_nAutoRunTime.setText((float)OrderParam.INSTANCE.getnAutoRunTime()/10 + "");
        et_nBaseUnit.setText(OrderParam.INSTANCE.getnBaseUnit() + "");
        et_nBaseHeight.setText(OrderParam.INSTANCE.getnBaseHeight() + "");
        et_nNoHardOutGlueTime.setText((float)OrderParam.INSTANCE.getnNoHardOutGlueTime()/10 + "");
        et_nNoHardOutGlueInterval.setText((float)OrderParam.INSTANCE.getnNoHardOutGlueInterval()/10 + "");
        et_nRunNum.setText(OrderParam.INSTANCE.getnRunNum() + "");
        //零点校正
        if (OrderParam.INSTANCE.getnZeroCheck() == 0) {//零点
            rb_zero.setChecked(true);
        } else if (OrderParam.INSTANCE.getnZeroCheck() == 1) {//起始点
            rb_linestart.setChecked(true);
        } else {
            rb_lineend.setChecked(true);//结束点
        }
        //运行次数清零
        if (OrderParam.INSTANCE.isbRunNumZero()) {
            switch_bRunNumZero.setChecked(true);
        } else {
            switch_bRunNumZero.setChecked(false);
        }
        //任务还原
        if (OrderParam.INSTANCE.isbTaskBack()) {
            switch_bTaskBack.setChecked(true);
        } else {
            switch_bTaskBack.setChecked(false);
        }
        //任务删除
        if (OrderParam.INSTANCE.isbTaskDelete()) {
            switch_bTaskDelete.setChecked(true);
        } else {
            switch_bTaskDelete.setChecked(false);
        }
        //暂停模式
        if (OrderParam.INSTANCE.getnPauseType() == 1) {//实时暂停
            switch_nPauseType.setChecked(true);
        } else {
            switch_nPauseType.setChecked(false);//节点暂停
        }
        //恢复出厂设置
        if (OrderParam.INSTANCE.isbBackDefault()) {
            switch_bBackDefault.setChecked(true);
        } else {
            switch_bBackDefault.setChecked(false);
        }
    }
}
