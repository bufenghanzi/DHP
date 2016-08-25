package com.mingseal.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

import com.mingseal.activity.GlueAloneActivity;
import com.mingseal.activity.GlueFaceEndActivity;
import com.mingseal.activity.GlueFaceStartActivity;
import com.mingseal.activity.GlueInputActivity;
import com.mingseal.activity.GlueLineEndActivity;
import com.mingseal.activity.GlueLineMidActivity;
import com.mingseal.activity.GlueLineStartActivity;
import com.mingseal.activity.GlueOutputActivity;
import com.mingseal.activity.TaskActivity;
import com.mingseal.adapter.TaskMainBaseAdapter;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueLineArcParam;
import com.mingseal.dhp.R;
import com.mingseal.utils.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * @author 商炎炳
 *
 */
public class MyPopWindowClickListener implements OnClickListener {
	public static String POPWINDOW_KEY = "com.mingseal.listener.popwindow.key";
	/**
	 * 0代表要增加数据，1代表要更新数据
	 */
	public final static String FLAG_KEY = "com.mingseal.listener.flag.key";
	/**
	 * 获取传过来的type种类(0代表是从MyPopWindowClickListner传过去的,1代表是从TaskActivity中的方案传过去的)
	 */
	public final static String TYPE_KEY = "com.mingseal.listener.type.key";

	/**
	 * 代表发送过去的key
	 */
	public final static String TYPE_UPDATE = "com.mingseal.listener.update.key";
	private PopupWindow popupWindow;
	private TaskActivity mParent;
	private final static String TAG = "MyPopWindowClickListener";
	private List<Point> points;
	private Point point;
	private Point pointBase;
	private Point pointClear;
	private Point pointArc;
	private Intent intent;
	private Bundle extras;
	/**
	 * @Fields mFlag: 0代表从TaskActivity传值，1代表从TaskMainBaseAdapter中传值
	 */
	private int mFlag = 0;// 0代表从TaskActivity传值，1代表从TaskMainBaseAdapter中传值
	private int selectRadio = 0;
	private TaskMainBaseAdapter mAdapter;
	private String taskname;

	public MyPopWindowClickListener(TaskActivity mParent) {
		this.mParent = mParent;
		View menu = initMenuView(mParent);
		popupWindow = new PopupWindow(menu);
		popupWindow.setWidth(AutoUtils.getPercentWidthSize(200));// 弹出框的宽高
		popupWindow.setHeight(AutoUtils.getPercentHeightSize(370));
	}

	/**
	 * 用于activity向自定义的onClickListener传值
	 *  @param _points
	 * @param _selectRadioID
	 * @param _flag
 *            0代表从TaskActivity中传值
	 * @param mAdapter
	 * @param taskName 任务名
	 */
	public void setPointLists(List<Point> _points, int _selectRadioID, int _flag, TaskMainBaseAdapter mAdapter, String taskName) {
		this.points = _points;
		this.point = getPointLast(_points, _selectRadioID);
		this.mFlag = _flag;
		this.mAdapter = mAdapter;
		this.taskname=taskName;
	}

	/**
	 * TaskMainBaseAdapter中传值，更换点类型
	 *  @param pointLists
	 *
	 * @param _point
	 * @param _flag
 *            1代表从TaskMainBaseAdapter中传值
	 * @param taskMainBaseAdapter
	 * @param taskname
	 */
	public void setPoint(List<Point> pointLists, Point _point, int _flag, TaskMainBaseAdapter taskMainBaseAdapter, String taskname) {
		this.points = pointLists;
		this.point = _point;
		this.mFlag = _flag;
		this.mAdapter = taskMainBaseAdapter;
		this.taskname=taskname;
	}

	/**
	 * 取得List列表的当前选中的数据，如果没有数据，则添加一个（0,0,0,0,null）的数据
	 * 
	 * @param points
	 * @return
	 */
	private Point getPointLast(List<Point> points, int _selectRadioID) {
		if (points.size() != 0 && points != null) {
			// int size = points.size() - 1;
			point = points.get(_selectRadioID);
		} else {
			point = new Point(PointType.POINT_GLUE_BASE);
		}
		return point;
	}

	/**
	 * 设置具体的监听
	 * 
	 * @param mParent
	 * @return
	 */
	private View initMenuView(Activity mParent) {
		View menuView = mParent.getLayoutInflater().inflate(R.layout.activity_task_main_button_popwindow, null);
		Button but_jieshu= (Button) menuView.findViewById(R.id.but_jieshu);
		Button but_qishi= (Button) menuView.findViewById(R.id.but_qishi);
		Button but_yuanhu= (Button) menuView.findViewById(R.id.but_yuanhu);
		Button but_duli= (Button) menuView.findViewById(R.id.but_duli);
		Button but_zhongjian= (Button) menuView.findViewById(R.id.but_zhongjian);
		Button but_jizhun= (Button) menuView.findViewById(R.id.but_jizhun);
		Button but_mianqi= (Button) menuView.findViewById(R.id.but_mianqi);
		Button but_mianzhong= (Button) menuView.findViewById(R.id.but_mianzhong);
//		Button but_qingjiaodian= (Button) menuView.findViewById(R.id.but_qingjiaodian);
		Button but_inputio= (Button) menuView.findViewById(R.id.but_inputio);
		Button but_outputio= (Button) menuView.findViewById(R.id.but_outputio);
		but_jieshu.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_qishi.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_yuanhu.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_duli.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_zhongjian.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_jizhun.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_mianqi.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_mianzhong.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
//		but_qingjiaodian.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_inputio.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));
		but_outputio.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(30));

		menuView.findViewById(R.id.but_jieshu).setOnClickListener(this);
		menuView.findViewById(R.id.but_qishi).setOnClickListener(this);
		menuView.findViewById(R.id.but_yuanhu).setOnClickListener(this);
		menuView.findViewById(R.id.but_duli).setOnClickListener(this);
		menuView.findViewById(R.id.but_zhongjian).setOnClickListener(this);
		menuView.findViewById(R.id.but_jizhun).setOnClickListener(this);
		menuView.findViewById(R.id.but_mianqi).setOnClickListener(this);
		menuView.findViewById(R.id.but_mianzhong).setOnClickListener(this);
//		menuView.findViewById(R.id.but_qingjiaodian).setOnClickListener(this);
		menuView.findViewById(R.id.but_inputio).setOnClickListener(this);
		menuView.findViewById(R.id.but_outputio).setOnClickListener(this);
		return menuView;
	}

	/**
	 * 跳转页面,并将Point类和mFlag标志一起传到跳转的Activity中去
	 * 
	 * @param _intent
	 */
	private void saveToActivity(Intent _intent) {
		extras = new Bundle();
		extras.putParcelable(POPWINDOW_KEY, point);
		extras.putInt(FLAG_KEY, mFlag);
		extras.putInt(TYPE_KEY, 0);
		extras.putString("taskname",taskname);
		_intent.putExtras(extras);

		mParent.startActivityForResult(_intent, TaskActivity.requestCode);
		mParent.overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);

		disPopWindow(popupWindow);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_jieshu:// 结束点
			intent = new Intent(mParent, GlueLineEndActivity.class);
			saveToActivity(intent);

			break;
		case R.id.but_qishi:// 起始点

			intent = new Intent(mParent, GlueLineStartActivity.class);
			saveToActivity(intent);

			break;
		case R.id.but_yuanhu:// 圆弧点
			pointArc = new Point(point.getX(), point.getY(), point.getZ(), point.getU(), PointType.POINT_GLUE_LINE_ARC);
			
			PointGlueLineArcParam param = new PointGlueLineArcParam();
			pointArc.setPointParam(param);
			if (mFlag == 0) {
				if (points.size() != 0) {
					selectRadio = mParent.getSelectRadioID();
					selectRadio = selectRadio + 1;
				}
				points.add(selectRadio, pointArc);

				mParent.setSelectRadioID(selectRadio);// Activity需要设置选中id
				mAdapter.setSelectID(selectRadio);// 选中位置

			} else if (mFlag == 1) {
				selectRadio = mParent.getSelectRadioID();

				points.remove(selectRadio);
				points.add(selectRadio, pointArc);
			}

			mAdapter.setData(points);
			mAdapter.notifyDataSetChanged();

			disPopWindow(popupWindow);

			break;
		case R.id.but_duli:// 独立点

			intent = new Intent(mParent, GlueAloneActivity.class);
			saveToActivity(intent);

			break;
		case R.id.but_zhongjian:// 中间点

			intent = new Intent(mParent, GlueLineMidActivity.class);
			saveToActivity(intent);

			break;
		case R.id.but_jizhun:// 基准点
			if (points.size() != 0) {
				if (!points.get(0).getPointParam().getPointType().equals(PointType.POINT_GLUE_BASE)) {
					pointBase = new Point(PointType.POINT_GLUE_BASE);
					if (mFlag == 0) {
						selectRadio = 0;
						points.add(selectRadio, pointBase);

						mParent.setSelectRadioID(selectRadio);// Activity需要设置选中id
						mAdapter.setSelectID(selectRadio);// 选中位置

					} else if (mFlag == 1) {
						selectRadio = mParent.getSelectRadioID();
						if (selectRadio == 0) {
							points.remove(selectRadio);
							points.add(selectRadio, pointBase);
						} else {
							ToastUtil.displayPromptInfo(mParent, "基准点只允许插在开始位置");
						}
					}

					mAdapter.setData(points);
					mAdapter.notifyDataSetChanged();
				} else {
					ToastUtil.displayPromptInfo(mParent, "只允许有一个基准点");
				}
			} else {
				pointBase = new Point(PointType.POINT_GLUE_BASE);
				selectRadio = 0;
				points.add(selectRadio, pointBase);

				mParent.setSelectRadioID(selectRadio);// Activity需要设置选中id
				mAdapter.setSelectID(selectRadio);// 选中位置
				mAdapter.setData(points);
				mAdapter.notifyDataSetChanged();
			}

			disPopWindow(popupWindow);
			break;
		case R.id.but_mianqi:// 面起点

			intent = new Intent(mParent, GlueFaceStartActivity.class);
			saveToActivity(intent);

			break;
		case R.id.but_mianzhong:// 面终点

			intent = new Intent(mParent, GlueFaceEndActivity.class);
			saveToActivity(intent);

			break;
//		case R.id.but_qingjiaodian:// 清胶点
//
//			intent = new Intent(mParent, GlueClearActivity.class);
//			saveToActivity(intent);
//
//			break;
		case R.id.but_inputio:// 输入IO
			intent = new Intent(mParent, GlueInputActivity.class);
			saveToActivity(intent);

			break;
		case R.id.but_outputio:// 输出IO
			intent = new Intent(mParent, GlueOutputActivity.class);
			saveToActivity(intent);
			break;

		}

	}

	/**
	 * 隐藏弹出框
	 * 
	 * @param popupWindow
	 */
	private void disPopWindow(PopupWindow popupWindow) {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	/**
	 * @return View
	 */
	public PopupWindow getMenu() {
		return popupWindow;
	}
}
