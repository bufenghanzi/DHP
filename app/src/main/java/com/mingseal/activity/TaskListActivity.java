/**
 *
 */
package com.mingseal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mingseal.adapter.TaskListBaseAdapter;
import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.communicate.TCPClient;
import com.mingseal.data.dao.GlueAloneDao;
import com.mingseal.data.dao.GlueClearDao;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.dao.GlueFaceStartDao;
import com.mingseal.data.dao.GlueInputDao;
import com.mingseal.data.dao.GlueLineEndDao;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.dao.GlueLineStartDao;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.dao.PointDao;
import com.mingseal.data.dao.PointTaskDao;
import com.mingseal.data.dao.WiFiDao;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.CmdParam;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointTask;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
import com.mingseal.data.protocol.Protocol_400_1;
import com.mingseal.dhp.R;
import com.mingseal.utils.CustomUploadDialog;
import com.mingseal.utils.DateUtil;
import com.mingseal.utils.FileDatabase;
import com.mingseal.utils.L;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.UploadTaskAnalyse;
import com.mingseal.utils.WifiConnectTools;
import com.mingseal.view.SuperTrackView;
import com.mingseal.view.TrackView;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王健
 *
 */
public class TaskListActivity extends AutoLayoutActivity implements OnClickListener {

	private final static String TAG = "TaskListActivity";
	/**
	 * 从TaskListActivity开始的requestCode
	 */
	public final static int TASK_RequestCode = 0x00000001;
	/**
	 * 从TaskListActivity结束的resultCode
	 */
	public final static int TASK_ResultCode = 0x00000002;
	/**
	 * 从MainAdminSettingActivity返回的resultCode
	 */
	public final static int ADMIN_SETTING_RESULT_CODE = 0x00000003;
	/**
	 * 保存任务的key
	 */
	public final static String TASK_KEY = "com.mingseal.tasklistactivity.task.key";
	public final static String TASK_POINT_LIST = "com.mingseal.tasklistactivity.task.point.list";

	/**
	 * 任务点的个数最大值
	 */
	public final static int TASK_MAX_SIZE = 5000;
	/**
	 * 任务点的个数，0代表超过最大值，取内存中的数，1代表没有超过，取Bundle中的数
	 */
	public final static String TASK_NUMBER_TYPE = "com.mingseal.tasklistactivity.number.key";
	/**
	 * 任务列表
	 */
	private ListView lv_task;
	/**
	 * 搜索框
	 */
	private EditText et_search;
	/**
	 * 自定义的绘图区间
	 */
	private TrackView view_track;
	/**
	 * 新建
	 */
	private RelativeLayout rl_add;
	/**
	 * 打开
	 */
	private RelativeLayout rl_open;
	/**
	 * 导出
	 */
	private RelativeLayout rl_export;
	/**
	 * 删除
	 */
	private RelativeLayout rl_delete;
	/**
	 * 上传
	 */
	private RelativeLayout rl_uploading;
	/**
	 * 设置
	 */
	private RelativeLayout rl_setting;
	/**
	 * @Fields rl_paste: 粘贴
	 */
	private RelativeLayout rl_paste;
	/**
	 * 任务时长
	 */
	private TextView tv_totalTime;
	/**
	 * 对话框中的自定义View
	 */
	private View customView;
	/**
	 * 对话框中的自定义View中的Edittext
	 */
	private EditText et_title;
	/**
	 * 对话框中的自定义上传的View中的上传任务号
	 */
	private EditText et_upload_number;
	/**
	 * 对话框中的自定义上传的View中的保存任务名
	 */
	private EditText et_upload_name;
	/**
	 * 判断dialog需不需要关闭
	 */
	private Field field;

	private TaskListBaseAdapter mTaskAdapter;
	private List<PointTask> taskLists;
	private PointTask task;
	/**
	 * 从task中获取的points
	 */
	private List<Point> points;// 从task中获取的points
	/**
	 * 用于upload上传保存的point列表
	 */
	private List<Point> pointUploads;

	/**
	 * 和ListView中的position相对应
	 */
	private int pselect = 0;
	private PointTaskDao taskDao;
	private PointDao pointDao;
	/**
	 * 独立点的数据库操作
	 */
	private GlueAloneDao glueAloneDao;// 独立点的数据库操作
	/**
	 * 起始点的数据库操作
	 */
	private GlueLineStartDao glueLineStartDao;// 起始点的数据库操作
	/**
	 * 中间点的数据库操作
	 */
	private GlueLineMidDao glueLineMidDao;// 中间点的数据库操作
	/**
	 * 结束点的数据库操作
	 */
	private GlueLineEndDao glueLineEndDao;// 结束点的数据库操作
	/**
	 * 面起始点的数据库操作
	 */
	private GlueFaceStartDao glueFaceStartDao;// 面起始点的数据库操作
	/**
	 * 面结束点的数据库操作
	 */
	private GlueFaceEndDao glueFaceEndDao;// 面结束点的数据库操作

	private Intent intent;

	// public static AsyncConnection myConnection = null;
	private RevHandler handler;
	/**
	 * 保存全局变量
	 */
	private UserApplication userApplication;// 保存全局变量
	private CustomUploadDialog progressDialog = null;

	/**
	 * @Fields iv_connect_tip: 连接信息
	 */
	private ImageView iv_connect_tip;
	/**
	 * @Fields taskNames: 当前所有任务的任务名
	 */
	private List<String> taskNames;
	/**
	 * @Fields pSelectLast: 上一次选中的行数
	 */
	protected int pSelectLast = 0;
	/**
	 * @Fields intTimeCur: 当前时间
	 */
	protected long intTimeCur = 0;
	/**
	 * @Fields intTimeLast: 上次保存的时间
	 */
	protected long intTimeLast = 0;
	private boolean prepareReset;
	/************************ add begin ************************/
	private int orderLength = 0;
	private byte[] buffer;
	private final int ORDER_BUFFER_LENTH = 100;
	private Protocol_400_1 protocol = null;
	private TextView yulan;
	private WiFiDao wifiDao;// wifi的ssid Dao
//	private NetworkStateService msgService;//service对象
//	private ServiceConnection mConnection=null;
    InvalidateCustomViewTask mTask;
	private SuperTrackView Super_view_track;
	private com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar circleProgressBar;
	private RelativeLayout rl_menu;
	private EditText et_num;
	private boolean checkSuccess=true;
	private SQLiteDatabase mSqLiteDatabase;
	private String mTaskname;//上传的任务名
	private String copyname;//黏贴的任务名
	private GlueClearDao glueClearDao;
	private GlueOutputDao glueOutputDao;
	private GlueInputDao glueInputDao;

	/************************ end ******************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_list);
		/************************ add begin ************************/
		protocol = new Protocol_400_1();
		/************************ end ******************************/
		L.d("TaskListActivity--->onCreate()");
		userApplication = (UserApplication) getApplication();
		SharePreferenceUtils.setSharedPreference(this);
		initView();
		initDao();
		/* =================== begin =================== */
		if (!TCPClient.instance().isConnect()) {// 如果没连接上，等待被链接，释放单例对象
			SocketThreadManager.releaseInstance();
			L.d("单例被释放了-----------------------------");
		}
		/* =================== add =================== */
		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);

		prepareReset=true;
//		MessageMgr.INSTANCE.resetCoord();
		taskLists = taskDao.findALLTaskLists();
		//开启线程后台去加载视图
		mTaskAdapter = new TaskListBaseAdapter(this);
		mTaskAdapter.setTaskList(taskLists);
		lv_task.setAdapter(mTaskAdapter);
		if (taskLists.size() == 0) {
			showAndHideLayout(false);
		} else {
			invalidateCustomView(mTaskAdapter.getItem(pselect), pointDao);
		}
		// ListView的点击事件
		lv_task.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				pSelectLast = pselect;
				pselect = position;
				intTimeLast = intTimeCur;
				intTimeCur = System.currentTimeMillis();
				if (pSelectLast == pselect) {
					if (intTimeCur - intTimeLast < 800) {
						gotoActivity(mTaskAdapter.getItem(position));
					}
				} else {
					mTaskAdapter.setSelectItem(position);
					mTaskAdapter.notifyDataSetInvalidated();
					invalidateCustomView(mTaskAdapter.getItem(position), pointDao);
				}
			}
		});
		// 长按事件监听
		lv_task.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				pselect = position;
				mTaskAdapter.setSelectItem(position);
				mTaskAdapter.notifyDataSetInvalidated();

				Log.d(TAG, mTaskAdapter.getItem(position).toString());

				invalidateCustomView(mTaskAdapter.getItem(position), pointDao);

				showModifyDialog();

				return false;
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				mTaskAdapter.performFiltering(s);
				// 搜索的时候实时更新绘图界面
				invalidateCustomView(
						mTaskAdapter.getItem(mTaskAdapter.getSelectItem()),
						pointDao);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		Log.d(TAG, "是否连接?" + userApplication.isWifiConnecting());


	}

	@Override
	protected void onResume(){
		super.onResume();
		Super_view_track.SurfaceView_OnResume();
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		/************************ add begin ************************/
			WifiConnectTools
					.processWifiConnect(userApplication, iv_connect_tip);

		/************************ end ******************************/
		if(this.taskLists !=null&& this.taskLists.size()!=0){
			PointTask subTask = this.taskLists.get(pselect);
			if(subTask.getPointids()==null||subTask.getPointids().size()==0){//判断集合为空则删除任务
				taskDao.deleteTask(subTask);
				// 删除任务时,将任务点也跟着删除
				pointDao.deletePointsByIds(subTask.getPointids(),subTask.getTaskName());
				this.taskLists = taskDao.findALLTaskLists();

				mTaskAdapter.setTaskList(this.taskLists);
				Log.d(TAG, "taskLists.size():" + this.taskLists.size()
						+ ",pselect:" + pselect);
				if (this.taskLists.size() == 0) {
					showAndHideLayout(false);
				} else {
					showAndHideLayout(true);
				}
				// 超过范围，要减少pselect的值
				if (pselect >= this.taskLists.size()) {
					pselect = pselect - 1;
				}
				mTaskAdapter.setSelectItem(pselect);
				if (pselect < 0) {
					invalidateCustomView(new PointTask(), pointDao);
				} else {
					invalidateCustomView(mTaskAdapter.getItem(pselect),
							pointDao);
				}
				mTaskAdapter.notifyDataSetChanged();
			}
		}
		L.d("TaskListActivity-->onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Super_view_track.SurfaceView_OnPause();
		L.d("TaskListActivity-->onPause");

	}

	@Override
	protected void onStop() {
		super.onStop();
		L.d("TaskListActivity-->onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.d("TaskListActivity-->onDestroy");
		// Activity结束需要关闭进度条对话框
		stopProgressDialog();
		SocketThreadManager.releaseInstance();
//		/*===================== 关闭服务，解绑activity =====================*/
//		Intent stopIntent = new Intent(this, NetworkStateService.class);
//		stopService(stopIntent);
//		if (mConnection!=null){
//			unbindService(mConnection);
//		}
//		/*=====================  end =====================*/

	}

	/**
	 * 绘制左边的自定义视图
	 * @param pointTask
	 * @param pointDao
	 */
	private void invalidateCustomView(PointTask pointTask, PointDao pointDao) {
//		circleProgressBar.setVisibility(View.VISIBLE);
		Super_view_track.setPointTask(pointTask,pointDao);
//		view_track.setPointTask(pointTask, pointDao);
//		if (mTask==null){
//			mTask=new InvalidateCustomViewTask();
//			ThreadPoolFactory.getNormalPool().execute(mTask);
//		}else {
//			ThreadPoolFactory.getNormalPool().removeTask(mTask);
//			mTask=new InvalidateCustomViewTask();
//			ThreadPoolFactory.getNormalPool().execute(mTask);
//		}
	}
	//画图操作线程池管理
	class InvalidateCustomViewTask implements Runnable{
		@Override
		public void run() {
			view_track.postInvalidate();
		}
	}
//	/**
//	 * 画图操作放到异步线程中去
//	 *
//	 */
//	private class InvalidateViewAsynctask extends
//			AsyncTask<Object, Void, Boolean> {
//
//		@Override
//		protected Boolean doInBackground(Object... params) {
//			// 设置任务点
//			view_track
//					.setPointTask((PointTask)params[0], (PointDao) params[1]);
//			return true;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			if (result) {
//				// 重绘
//				view_track.invalidate();
//			}
//		}
//
//	}

//	/**
//	 * 初始化任务列表的内容
//	 */
//	private void initTaskList() {
//		taskLists = new ArrayList<PointTask>();
//		List<Integer> pointids = new ArrayList<Integer>();
//
//		for (int i = 1; i < 20; i++) {
//			task = new PointTask();
//			task.setId(i);
//			task.setTaskName("任务" + i);
//			pointids.add(i);
//			task.setPointids(pointids);
//			taskLists.add(task);
//		}
//		pointids = new ArrayList<Integer>();
//		for (int i = 1; i < 30; i++) {
//			task = new PointTask();
//			task.setId(i);
//			task.setTaskName("事件" + i);
//			pointids.add(i);
//			task.setPointids(pointids);
//			taskLists.add(task);
//		}
//	}

	/**
	 * 加载自定义组件
	 */
	private void initView() {
		lv_task = (ListView) findViewById(R.id.lv_task);
		et_search = (EditText) findViewById(R.id.et_Search);
//		view_track = (TrackView) findViewById(R.id.view_track);
		Super_view_track = (SuperTrackView) findViewById(R.id.super_view_track);
		circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_progress);
		tv_totalTime = (TextView) findViewById(R.id.tv_totaltime);

//		/*===================== begin =====================*/
//		Fl_1 = (TextView) findViewById(R.id.fl_1);
//		Fl_2 = (TextView) findViewById(R.id.fl_2);
//		Fl_3 = (TextView) findViewById(R.id.fl_3);
//		Fl_4 = (TextView) findViewById(R.id.fl_4);
//		Fl_5 = (TextView) findViewById(R.id.fl_5);
//		cacheViews=new SparseArray<>();
//		cacheViews.put(0,Fl_1);
//		cacheViews.put(1,Fl_2);
//		cacheViews.put(2,Fl_3);
//		cacheViews.put(3,Fl_4);
//		cacheViews.put(4,Fl_5);
//		/*=====================  end =====================*/
		rl_add = (RelativeLayout) findViewById(R.id.rl_add);
		rl_open = (RelativeLayout) findViewById(R.id.rl_open);
		rl_export = (RelativeLayout) findViewById(R.id.rl_daochu);
		rl_delete = (RelativeLayout) findViewById(R.id.rl_shanchu);
		rl_uploading = (RelativeLayout) findViewById(R.id.rl_shangchuan);
		rl_setting = (RelativeLayout) findViewById(R.id.rl_shezhi);
		rl_paste = (RelativeLayout) findViewById(R.id.rl_task_paste);
		rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
		yulan = (TextView) findViewById(R.id.tv_yulan);
		yulan.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(50));
		et_search.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(50));

		rl_add.setOnClickListener(this);
		rl_open.setOnClickListener(this);
		rl_export.setOnClickListener(this);
		rl_delete.setOnClickListener(this);
		rl_uploading.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
		rl_paste.setOnClickListener(this);
		rl_menu.setOnClickListener(this);

//		view_track.setCircle(50);
//		view_track.setRadius(5);
		Super_view_track.setCircle(50);
		Super_view_track.setRadius(5);

		iv_connect_tip = (ImageView) findViewById(R.id.iv_connect_tip);
		iv_connect_tip.setOnClickListener(this);
//		Super_view_track.setOnINotifyListener(new SuperTrackView.INotify() {
//			@Override
//			public void notifyEvent(boolean msg) {
//				if (msg){
//					CircleProgressBar.setVisibility(View.INVISIBLE);
//				}
//			}
//		});
//		view_track.setOnINotifyUIListener(new TrackView.INotifyUI() {
//			@Override
//			public void notifyEvent(int msg, TrackView trackView) {
//				cacheViews.put(msg,trackView);
//				System.out.println("缓存了视图---->"+msg);
//			}
//		});

	}

	/**
	 * 加载自定义的Dao
	 */
	private void initDao() {
		// 加载自定义的Dao
		glueAloneDao = new GlueAloneDao(this);
		glueLineStartDao = new GlueLineStartDao(this);
		glueLineMidDao = new GlueLineMidDao(this);
		glueLineEndDao = new GlueLineEndDao(this);
		glueFaceStartDao = new GlueFaceStartDao(this);
		glueFaceEndDao = new GlueFaceEndDao(this);
		glueClearDao=new GlueClearDao(this);
		glueOutputDao=new GlueOutputDao(this);
		glueInputDao=new GlueInputDao(this);
		taskDao = new PointTaskDao(this);
		pointDao = new PointDao(this);
		wifiDao=new WiFiDao(this);
	}

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode,
									Intent _data) {
		if (_requestCode == TASK_RequestCode && _resultCode == TASK_ResultCode) {
			task = _data.getParcelableExtra(TASK_KEY);
			Log.d(TAG, "onActivityResult中的：" + task.toString());
			int number = taskDao.updateTask(task);
			Log.d(TAG, "number:" + number);
			if (number > 0) {
				ToastUtil.displayPromptInfo(this, "修改成功！");
				// 更新成功，Task列表也需要更新
				taskLists = taskDao.findALLTaskLists();
				mTaskAdapter.setTaskList(taskLists);
				mTaskAdapter.notifyDataSetChanged();
				// 要更新绘图界面
				// view_track.setPointTask(taskLists.get(pselect), pointDao);
				// view_track.invalidate();
				// 改
				invalidateCustomView(taskLists.get(pselect), pointDao);

			} else {
				ToastUtil.displayPromptInfo(this, "没有修改任何东西！");
			}
		} else if (_requestCode == TASK_RequestCode
				&& _resultCode == TaskActivity.resultDownLoadCode) {
			Log.d(TAG, "从DownloadActivity返回结果");
		}
	}

	/**
	 * true显示所有,false隐藏不该显示的
	 *
	 * @param showFlag
	 */
	private void showAndHideLayout(boolean showFlag) {
		rl_delete.setEnabled(showFlag);
		rl_open.setEnabled(showFlag);
		rl_paste.setEnabled(showFlag);
	}

	/**
	 * Activity的跳转
	 *
	 * @param task
	 */
	private void gotoActivity(PointTask task) {
		// Log.d(TAG + "-->gotoActivity", "gotoActivity中的：" + task.toString());
		intent = new Intent(TaskListActivity.this, TaskActivity.class);
		Bundle extras = new Bundle();
		if (task.getPointids().size() > TASK_MAX_SIZE) {
			extras.putString(TASK_NUMBER_TYPE, "0");
			userApplication.setPointTask(task);
		} else {
			extras.putString(TASK_NUMBER_TYPE, "1");
			extras.putParcelable(TASK_KEY, task);
		}
		intent.putExtras(extras);
		startActivityForResult(intent, TASK_RequestCode);
		// 绘制区域,跳转时不需要再等绘制完毕？
//		invalidateCustomView(task, pointDao);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
	}

	/**
	 * 新建对话框,输入任务名称之后跳转到新建点界面
	 */
	private void showAddDialog() {
		AlertDialog.Builder buildAdd = new AlertDialog.Builder(
				TaskListActivity.this);
		buildAdd.setTitle("新建任务");
		customView = View.inflate(TaskListActivity.this,
				R.layout.custom_dialog_edittext, null);
		buildAdd.setView(customView);
		et_title = (EditText) customView.findViewById(R.id.et_title);
		et_title.setSelectAllOnFocus(true);
		taskNames = taskDao.getALLTaskNames();
		// Log.d(TAG, "任务名:"+taskNames.toString());
		// Log.d(TAG,
		// "任务名是否重复:"+taskNames.contains(et_title.getText().toString()));
		buildAdd.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// true表示要关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		buildAdd.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				}
				if ("".equals(et_title.getText().toString())) {
					ToastUtil.displayPromptInfo(TaskListActivity.this,
							"任务名不能为空！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				} else if (taskNames.contains(et_title.getText().toString())) {
					ToastUtil.displayPromptInfo(TaskListActivity.this,
							"任务名不能重复！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				} else {
					try {
						field.set(dialog, true);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					List<PointTask> pointTaskList=mTaskAdapter.geTaskList();
					if (pointTaskList.size()>=120){//不允许创建了
						ToastUtil.displayPromptInfo(TaskListActivity.this,"仅允许创建120个任务！");
					}else {
						task = new PointTask();
						task.setTaskName(et_title.getText().toString());
						List<Integer> pointids = new ArrayList<Integer>();
						task.setPointids(pointids);
						long rowID = taskDao.insertTask(task);
						taskLists = taskDao.findALLTaskLists();

						mTaskAdapter.setTaskList(taskLists);
						// 设置刚添加的被选中
						pselect = taskLists.size() - 1;
						mTaskAdapter.setSelectItem(pselect);
						mTaskAdapter.notifyDataSetChanged();
						// 滚动到最底部
						lv_task.smoothScrollToPosition(pselect);
						showAndHideLayout(true);
						task.setId((int) rowID);
						gotoActivity(task);
					}
				}

			}
		});
		buildAdd.show();
	}

	/**
	 * 新建对话框,修改任务名之后跳转到新建点界面
	 */
	private void showModifyDialog() {
		AlertDialog.Builder builderModify = new AlertDialog.Builder(
				TaskListActivity.this);
		builderModify.setTitle("修改任务名");
		customView = View.inflate(TaskListActivity.this,
				R.layout.custom_dialog_edittext, null);
		builderModify.setView(customView);
		et_title = (EditText) customView.findViewById(R.id.et_title);
		Log.d(TAG, "showModifyDialog中的：" + taskLists.get(pselect).toString());
		et_title.setText(taskLists.get(pselect).getTaskName());
		et_title.setSelectAllOnFocus(true);
		taskNames = taskDao.getALLTaskNames();
		builderModify.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);// true表示要关闭
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		builderModify.setPositiveButton("修改",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
						} catch (NoSuchFieldException e1) {
							e1.printStackTrace();
						}
						if ("".equals(et_title.getText().toString())) {
							ToastUtil.displayPromptInfo(TaskListActivity.this,
									"任务名不能为空！");

							try {
								field.set(dialog, false);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}

						} else if (taskNames.contains(et_title.getText()
								.toString())) {
							ToastUtil.displayPromptInfo(TaskListActivity.this,
									"任务名不能重复！");

							try {
								field.set(dialog, false);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
						} else {
							try {
								field.set(dialog, true);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
							task = taskLists.get(pselect);
							//更改相应表名
							alterTableName(task.getTaskName(),et_title.getText().toString());
							task.setTaskName(et_title.getText().toString());
							taskDao.updateTask(task);
							// gotoActivity(task);
							taskLists = taskDao.findALLTaskLists();
							mTaskAdapter.setTaskList(taskLists);
							mTaskAdapter.notifyDataSetChanged();
						}

					}
				});
		builderModify.show();
	}

	/**
	 * 更改相应任务下的表名字
	 * @param taskname
	 * @param newTaskname
	 */
	private void alterTableName(String taskname, String newTaskname) {
		mSqLiteDatabase = this.openOrCreateDatabase(DBInfo.DB.DB_NAME,MODE_PRIVATE,null);
		mSqLiteDatabase.execSQL(DBInfo.TableAlone.alter_alone_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableFaceStart.alter_face_start_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableFaceEnd.alter_face_end_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableClear.alter_clear_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableLineStart.alter_line_start_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableLineMid.alter_line_mid_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableLineEnd.alter_line_end_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableOutputIO.alter_output_io_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableInputIO.alter_input_io_table(taskname,newTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TablePoint.alter_point_table(taskname,newTaskname));
	}

	/**
	 * 新建对话框，确认是否删除
	 */
	private void showDeleteDialog() {
		AlertDialog.Builder buildDelete = new AlertDialog.Builder(
				TaskListActivity.this);
		buildDelete.setTitle("删除任务");
		buildDelete.setMessage("是否删除任务：" + taskLists.get(pselect).getTaskName()
				+ " ?");
		buildDelete.setNegativeButton("取消", null);
		buildDelete.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// taskLists.remove(pselect);
						PointTask subTask = taskLists.get(pselect);
						taskDao.deleteTask(subTask);
						// 删除任务时,将任务点也跟着删除
						pointDao.deletePointsByIds(subTask.getPointids(),subTask.getTaskName());
						//删除相应任务下的点表和参数表
						deleteTable(subTask.getTaskName());
						taskLists = taskDao.findALLTaskLists();

						mTaskAdapter.setTaskList(taskLists);
						Log.d(TAG, "taskLists.size():" + taskLists.size()
								+ ",pselect:" + pselect);
						if (taskLists.size() == 0) {
							showAndHideLayout(false);
						} else {
							showAndHideLayout(true);
						}
						// 超过范围，要减少pselect的值
						if (pselect >= taskLists.size()) {
							pselect = pselect - 1;
						}
						mTaskAdapter.setSelectItem(pselect);
						if (pselect < 0) {
							invalidateCustomView(new PointTask(), pointDao);
						} else {
							invalidateCustomView(mTaskAdapter.getItem(pselect),
									pointDao);

						}
						mTaskAdapter.notifyDataSetChanged();

					}
				});
		buildDelete.show();
	}

	/**
	 * 删除任务下的表
	 * @param taskName
     */
	private void deleteTable(String taskName) {
		mSqLiteDatabase = this.openOrCreateDatabase(DBInfo.DB.DB_NAME,MODE_PRIVATE,null);
		mSqLiteDatabase.execSQL(DBInfo.TablePoint.drop_point_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableAlone.drop_alone_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableFaceStart.drop_face_start_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableFaceEnd.drop_face_end_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableClear.drop_clear_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableLineStart.drop_line_start_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableLineMid.drop_line_mid_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableLineEnd.drop_line_end_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableOutputIO.drop_output_io_table(taskName));
		mSqLiteDatabase.execSQL(DBInfo.TableInputIO.drop_input_io_table(taskName));
	}

	/**
	 * 查询任务号
	 */
	private void showFunclistDialog(){
		AlertDialog.Builder buildAdd = new AlertDialog.Builder(
				TaskListActivity.this);
		buildAdd.setTitle("查看功能列表");
		customView = View.inflate(TaskListActivity.this,
				R.layout.custom_dialog_funclist, null);
		buildAdd.setView(customView);
		et_num = (EditText) customView.findViewById(R.id.et_num);
		et_num.setSelectAllOnFocus(true);
		taskNames = taskDao.getALLTaskNames();
		// Log.d(TAG, "任务名:"+taskNames.toString());
		// Log.d(TAG,
		// "任务名是否重复:"+taskNames.contains(et_title.getText().toString()));
		buildAdd.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// true表示要关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		buildAdd.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				}
				if ("".equals(et_num.getText().toString())) {
					ToastUtil.displayPromptInfo(TaskListActivity.this,
							"任务号不能为空！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				}  else {
					try {
						field.set(dialog, true);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					//请求读取功能列表
					int a;
					try{
						if (isEditLow()){
							a=Integer.parseInt(et_num.getText().toString().trim());
							OrderParam.INSTANCE.setnTaskNum(a);
							MessageMgr.INSTANCE.isTaskExist();

						}else {
							ToastUtil.displayPromptInfo(TaskListActivity.this,"1~120之间整数！");
						}
					}catch (NumberFormatException e){
						ToastUtil.displayPromptInfo(TaskListActivity.this,"请输入合法数字！");
					}

				}

			}
		});
		buildAdd.show();
	}
	/**
	 * @Title isEditLow
	 * @Description 判断输入框的内容是不是小于最小值
	 * @return false表示小于最小值，true表示正常
	 */
	private boolean isEditLow() {
		if (Integer.parseInt(et_num.getText().toString()) < 1) {
			return false;
		} else if (Integer.parseInt(et_num.getText().toString()) > 120) {
			return false;
		}
		return true;

	}
	/**
	 * 新建对话框，选中上传任务号
	 */
	private void showUploadDialog() {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		View view = LayoutInflater.from(TaskListActivity.this).inflate(
				R.layout.custom_dialog_upload, null);
		TextView tv_upload= (TextView) view.findViewById(R.id.tv_upload);
		TextView tv_save= (TextView) view.findViewById(R.id.tv_save);
		tv_upload.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
		tv_save.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(35));
		builder = new AlertDialog.Builder(TaskListActivity.this);
		builder.setView(view);
		builder.setTitle("上传任务");
		et_upload_number = (EditText) view
				.findViewById(R.id.upload_task_number);
		et_upload_name = (EditText) view.findViewById(R.id.upload_task_name);
		et_upload_number.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		et_upload_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(40));
		taskNames = taskDao.getALLTaskNames();
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// true表示要关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				}
				if ("".equals(et_upload_name.getText().toString())
						|| "".equals(et_upload_number.getText().toString())) {
					ToastUtil.displayPromptInfo(TaskListActivity.this,
							"任务号和任务名都不能为空！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else if (taskNames.contains(et_upload_name.getText()
						.toString())){
					ToastUtil.displayPromptInfo(TaskListActivity.this,
							"任务名不能重复！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						field.set(dialog, true);// true表示要关闭
					} catch (Exception e) {
						e.printStackTrace();
					}
					int taskNum = Integer.parseInt(et_upload_number.getText()
							.toString());
					OrderParam.INSTANCE.setAllParamToZero();
					OrderParam.INSTANCE.setnTaskNum(taskNum);
					pointUploads = new ArrayList<>();
					Log.d(TAG, "上传之前:point" + pointUploads.size());
					Log.d(TAG, "上传之前:" + DateUtil.getCurrentTime());
					startProgressDialog();
					MessageMgr.INSTANCE.taskUpload(pointUploads);

				}
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 打开进度条对话框
	 */
	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomUploadDialog.createDialog(this);
			progressDialog.setMessage("正在上传中..");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**
	 * 打开进度条对话框
	 */
	private void startProgressUIDialog() {
		if (progressDialog == null) {
			progressDialog = CustomUploadDialog.createDialog(this);
			progressDialog.setMessage("正在初始化数据..");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度条对话框
	 */
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * 解析下载成功的任务（*重写HashCode方法*）
	 *
	 * @param pointUploads
	 */
	private void analyseTaskSuccess(List<Point> pointUploads) {
		// 上传成功里面的Point的List数组
		List<Point> points = new ArrayList<>();
		UploadTaskAnalyse uploadAnalyse = new UploadTaskAnalyse(TaskListActivity.this,mTaskname);
//		if (checkPointParamID(pointUploads)){
			points = uploadAnalyse.analyseTaskSuccess(pointUploads);
			Log.d(TAG, "解析之后：" + DateUtil.getCurrentTime());
			// 往界面上添加（暂时先删了）
			// 先往数据库里面添加，然后再将Point的id保存出来
			List<Integer> ids = pointDao.insertPoints(points,mTaskname);
			task = new PointTask();
			task.setPointids(ids);
			task.setTaskName(mTaskname);
			int id = (int) taskDao.insertTask(task);
			task.setId(id);
			taskLists.add(task);
			mTaskAdapter.setTaskList(taskLists);
			// 设置刚添加的被选中
			pselect = taskLists.size() - 1;
			mTaskAdapter.setSelectItem(pselect);
			// 滚动到最底部
			lv_task.smoothScrollToPosition(pselect);
			showAndHideLayout(true);
			mTaskAdapter.notifyDataSetChanged();
			invalidateCustomView(taskLists.get(pselect), pointDao);
//			checkSuccess=true;
//		}else{
//			checkSuccess=false;
//			ToastUtil.displayPromptInfo(TaskListActivity.this, "此任务不兼容！");
//		}
		// /////////////////////
		// 全部更新完之后关闭进度框
		stopProgressDialog();
	}
	/**
	 * 检查任务号
	 * @param pointList
	 * @return true:合法 flase:不合法
	 */
	private boolean checkPointParamID(List<Point> pointList) {
		for (Point point:pointList) {
			L.d("上传的任务号："+point.getPointParam().get_id());
			if (point.getPointParam().getPointType()!= PointType.POINT_GLUE_BASE&&point.getPointParam().getPointType()!=PointType.POINT_GLUE_LINE_ARC
					&&point.getPointParam().getPointType()!=PointType.POINT_GLUE_CLEARIO){
				if (point.getPointParam().get_id()==0){
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_add:// 新建

				showAddDialog();

				break;
			case R.id.rl_open:// 打开

				// showModifyDialog();
				task = taskLists.get(pselect);
				gotoActivity(task);

				break;
			case R.id.rl_daochu:// 导出
				FileDatabase.exportToFile(this);
				break;
			case R.id.rl_shanchu:// 删除

				showDeleteDialog();

				break;
			case R.id.rl_shangchuan:// 上传
				if (TCPClient.instance().isConnect()) {

					showUploadDialog();
				} else {
					ToastUtil.displayPromptInfo(TaskListActivity.this, "通信未连接！");
				}

				break;
			case R.id.rl_shezhi:// 设置
				intent = new Intent(this, MainAdminSettingActivity.class);
				startActivityForResult(intent, TASK_RequestCode);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_from_left);
				break;
			case R.id.rl_task_paste:// 粘贴任务
				displayPasteDialog();
				break;
			case R.id.iv_connect_tip:// wifi是否连接成功
				WifiConnectTools
						.processWifiConnect(userApplication, iv_connect_tip);
				break;
			case R.id.rl_menu:
				showFunclistDialog();
				break;
		}
	}

	/**
	 * 显示粘贴任务的对话框，输入粘贴任务的任务名
	 *
	 * @Title displayPasteDialog
	 * @Description 显示粘贴任务的对话框，输入粘贴任务的任务名
	 */
	private void displayPasteDialog() {
		AlertDialog.Builder buildPaste = new AlertDialog.Builder(
				TaskListActivity.this);
		buildPaste.setTitle("粘贴任务");
		customView = View.inflate(TaskListActivity.this,
				R.layout.custom_dialog_edittext, null);
		buildPaste.setView(customView);
		et_title = (EditText) customView.findViewById(R.id.et_title);
		et_title.setText(taskLists.get(pselect).getTaskName() + "_1");
		et_title.setSelectAllOnFocus(true);
		taskNames = taskDao.getALLTaskNames();
		buildPaste.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);// true表示要关闭
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		buildPaste.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
						} catch (NoSuchFieldException e1) {
							e1.printStackTrace();
						}
						if ("".equals(et_title.getText().toString())) {
							ToastUtil.displayPromptInfo(TaskListActivity.this,
									"任务名不能为空！");

							try {
								field.set(dialog, false);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}

						} else if (taskNames.contains(et_title.getText()
								.toString())) {
							ToastUtil.displayPromptInfo(TaskListActivity.this,
									"任务名不能重复！");

							try {
								field.set(dialog, false);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
						} else {
							try {
								field.set(dialog, true);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
							List<PointTask> pointTaskList=mTaskAdapter.geTaskList();
							if (pointTaskList.size()>=120){//不允许创建了
								ToastUtil.displayPromptInfo(TaskListActivity.this,"仅允许创建120个任务！");
							}else {

								// 设置新粘贴的任务
								task = new PointTask();
								//从一个任务中取出所有点
								List<Point> pointsCur = pointDao.findALLPointsByIdLists(taskLists.get(pselect).getPointids(),taskLists.get(pselect).getTaskName());
								//存入另一个任务中，首先得创建表
								createTable(et_title);
								copyTable(et_title);
								List<Integer> pointIDsCur = pointDao.insertPoints(pointsCur,et_title.getText().toString());
								task.setPointids(pointIDsCur);
								task.setTaskName(et_title.getText().toString());
								long rowID = taskDao.insertTask(task);
								task.setId((int) rowID);
								taskLists = taskDao.findALLTaskLists();

								mTaskAdapter.setTaskList(taskLists);
								// 设置刚添加的被选中
								pselect = taskLists.size() - 1;
								mTaskAdapter.setSelectItem(pselect);
								mTaskAdapter.notifyDataSetChanged();
								// 滚动到最底部
								lv_task.smoothScrollToPosition(pselect);
								showAndHideLayout(true);
								invalidateCustomView(task, pointDao);
							}
						}

					}
				});
		buildPaste.show();
	}

	/**
	 * 复制表内容至新表
	 * @param et_title
     */
	private void copyTable(EditText et_title) {
		try {
			copyname = et_title.getText().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String orginalname=taskLists.get(pselect).getTaskName();
		List<PointGlueAloneParam> pointGlueAloneParamList=glueAloneDao.findAllGlueAloneParams(orginalname);
		if (pointGlueAloneParamList!=null){

			for (PointGlueAloneParam pointglueAloneParam:pointGlueAloneParamList) {
				glueAloneDao.insertGlueAlone(pointglueAloneParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueFaceStartParam> pointglueFaceStartParamList=glueFaceStartDao.findAllGlueFaceStartParams(orginalname);
		if (pointglueFaceStartParamList!=null){

			for (PointGlueFaceStartParam pointgluefaceStartParam:pointglueFaceStartParamList) {
				glueFaceStartDao.insertGlueFaceStart(pointgluefaceStartParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueFaceEndParam> pointglueFaceEndParamList=glueFaceEndDao.findAllGlueFaceEndParams(orginalname);
		if (pointglueFaceEndParamList!=null){

			for (PointGlueFaceEndParam pointglueFaceEndParam:pointglueFaceEndParamList) {
				glueFaceEndDao.insertGlueFaceEnd(pointglueFaceEndParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueClearParam> pointGlueClearParamList= glueClearDao.findAllGlueClearParams(orginalname);
		if (pointGlueClearParamList!=null){

			for (PointGlueClearParam pointGlueClearParam:pointGlueClearParamList) {
				glueClearDao.insertGlueClear(pointGlueClearParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueLineStartParam> pointGlueLineStartParamList= glueLineStartDao.findAllGlueLineStartParams(orginalname);
		if (pointGlueLineStartParamList!=null){

			for (PointGlueLineStartParam pointGlueLineStartParam:pointGlueLineStartParamList) {
				glueLineStartDao.insertGlueLineStart(pointGlueLineStartParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueLineMidParam> pointGlueLineMidParamList= glueLineMidDao.findAllGlueLineMidParams(orginalname);
		if (pointGlueLineMidParamList!=null){

			for (PointGlueLineMidParam pointGlueLineMidParam:pointGlueLineMidParamList) {
				glueLineMidDao.insertGlueLineMid(pointGlueLineMidParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueLineEndParam> pointGlueLineEndParamList= glueLineEndDao.findAllGlueLineEndParams(orginalname);
		if (pointGlueLineEndParamList!=null){

			for (PointGlueLineEndParam pointGlueLineEndParam:pointGlueLineEndParamList) {
				glueLineEndDao.insertGlueLineEnd(pointGlueLineEndParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueOutputIOParam> pointGlueOutputIOParamList= glueOutputDao.findAllGlueOutputParams(orginalname);
		if (pointGlueOutputIOParamList!=null){

			for (PointGlueOutputIOParam pointGlueOutputIOParam:pointGlueOutputIOParamList) {
				glueOutputDao.insertGlueOutput(pointGlueOutputIOParam,copyname);//将数据复制到新表
			}
		}
		List<PointGlueInputIOParam> pointGlueInputIOParamList= glueInputDao.findAllGlueInputParams(orginalname);
		if (pointGlueInputIOParamList!=null){

			for (PointGlueInputIOParam pointGlueInputIOParam:pointGlueInputIOParamList) {
				glueInputDao.insertGlueInput(pointGlueInputIOParam,copyname);//将数据复制到新表
			}
		}


	}

	/**
	 * <p>
	 * Title: DisPlayInfoAfterGetMsg
	 * <p>
	 * Description: 显示收到的数据信息
	 *
	 * @param revBuffer
	 */
	private void disPlayInfoAfterGetMsg(byte[] revBuffer) {
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
			case 0:
				if (MessageMgr.INSTANCE.cmdDelayFlag==CmdParam.Cmd_IsTaskExist){
					if (((revBuffer[3] & 0x00ff) == 0x31) && ((revBuffer[2] & 0x00ff) == 0x79)) {
						if ((revBuffer[5] & 0x00ff) == 0) {

							ToastUtil.displayPromptInfo(TaskListActivity.this,"空任务！");
						}
					}
					MessageMgr.INSTANCE.cmdDelayFlag=CmdParam.Cmd_Null;
				}else {

					ToastUtil.displayPromptInfo(TaskListActivity.this, "校验失败");
				}
				break;
			case 1: {
				int cmdFlag = ((revBuffer[2] & 0x00ff) << 8)
						| (revBuffer[3] & 0x00ff);
				if (revBuffer[2] == 0x4A) {// 获取下位机参数成功
					ToastUtil.displayPromptInfo(TaskListActivity.this, "连接成功!");
					Log.d(TAG, RobotParam.INSTANCE.GetXJourney() + ",分辨率：x"
							+ RobotParam.INSTANCE.GetXDifferentiate() + ",y:"
							+ RobotParam.INSTANCE.GetYDifferentiate() + ",z:"
							+ RobotParam.INSTANCE.GetZDifferentiate());
				}else if (revBuffer[2]==0x2E){//获取功能列表成功
					ToastUtil.displayPromptInfo(TaskListActivity.this,"获取功能列表成功！");
				}
				if ((revBuffer[3] & 0x00ff) == 0x31) {
					if ((revBuffer[5] & 0x00ff) == 1) {
						L.d(TAG, "任务存在");
						// 任务存在的话，需要给个提示框
						MessageMgr.INSTANCE.cmdDelayFlag=CmdParam.Cmd_Null;
						Intent intent=new Intent(TaskListActivity.this,FuncListActivity.class);
						startActivity(intent);
					}
				}
				sendResetCommand();

			}
			break;
			case 8421:
				// 任务上传分包数据,不作处理
				break;
			case 1248:
				// 上传成功
				if ((revBuffer[3] & 0x00ff) == 0xe6) {
					if (!pointUploads.isEmpty() && pointUploads.size() > 0) {
						Log.d(TAG, "上传的列表长度:" + pointUploads.size() + "--"
								+ et_upload_name.getText().toString());
						// for(Point point:pointUploads){
						// Log.d(TAG, point.toString());
						// }
						//创建新表
						createTable(et_upload_name);
						analyseTaskSuccess(pointUploads);
					}
//					if (checkSuccess){
						ToastUtil.displayPromptInfo(TaskListActivity.this, "上传完成");
//					}
				}
				break;
			case 1249:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "上传失败");
				sendResetCommand();
				break;
			case 40101:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "非法功能");
				sendResetCommand();
				break;
			case 40102:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "非法数据地址");
				sendResetCommand();
				break;
			case 40103:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "非法数据");
				sendResetCommand();
				break;
			case 40105:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "设备忙");
				sendResetCommand();
				break;
			case 40109:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "急停中");
				break;
			case 40110:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "X轴光电报警");
				sendResetCommand();
				break;
			case 40111:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "Y轴光电报警");
				sendResetCommand();
				break;
			case 40112:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "Z轴光电报警");
				sendResetCommand();
				break;
			case 40113:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "U轴光电报警");
				sendResetCommand();
				break;
			case 40114:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "行程超限报警");
				sendResetCommand();
				break;
			case 40115:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "任务下载失败");
				break;
			case 40116:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "任务上传失败");
				break;
			case 40117:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "任务模拟失败");
				break;
			case 40118:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "示教指令错误");
				break;
			case 40119:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "循迹定位失败");
				break;
			case 40120:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "任务号不可用");
				break;
			case 40121:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "初始化失败");
				break;
			case 40122:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "API版本错误");
				break;
			case 40123:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "程序升级失败");
				break;
			case 40124:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "系统损坏");
				break;
			case 40125:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "任务未加载");
				break;
			case 40126:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "(Z轴)基点抬起高度过高");
				sendResetCommand();
				break;
			case 40106:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "空任务");
				stopProgressDialog();
				MessageMgr.INSTANCE.cmdDelayFlag=CmdParam.Cmd_Null;
				MessageMgr.INSTANCE.setisUploading(false);
				break;
			case 40127:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "等待输入超时");
				break;
			default:
				ToastUtil.displayPromptInfo(TaskListActivity.this, "未知错误");
				sendResetCommand();
				break;
		}
	}

	/**
	 * 打开数据库创建新表(参数方案)
	 */
	private void createTable(EditText et) {
		try {
			mTaskname = et.getText().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mSqLiteDatabase = this.openOrCreateDatabase(DBInfo.DB.DB_NAME,MODE_PRIVATE,null);
		mSqLiteDatabase.execSQL(DBInfo.TableAlone.create_alone_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableFaceStart.create_face_start_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableFaceEnd.create_face_end_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableClear.create_clear_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableLineStart.create_line_start_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableLineMid.create_line_mid_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableLineEnd.create_line_end_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableOutputIO.create_output_io_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TableInputIO.create_input_io_table(mTaskname));
		mSqLiteDatabase.execSQL(DBInfo.TablePoint.create_point_table(mTaskname));

	}


	private void sendResetCommand() {
		// TODO Auto-generated method stub
		if(prepareReset){
			/************************ add begin ************************/
			buffer = new byte[ORDER_BUFFER_LENTH];
			orderLength = protocol.CreaterOrder(buffer, CmdParam.Cmd_Reset);
			MessageMgr.INSTANCE.writeData(buffer, orderLength);
			/************************ end ******************************/
			prepareReset=false;
//			mPointsCur.get(selectRadioIDCur).setX(0);
//			mPointsCur.get(selectRadioIDCur).setY(0);
//			mPointsCur.get(selectRadioIDCur).setZ(0);
//			mPointsCur.get(selectRadioIDCur).setU(0);
//			mAdapter.setData(mPointsCur);
//			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * <p>
	 * Title: RevHandler
	 * <p>
	 * Description: 数据接收Handler
	 * <p>
	 * Company: MingSeal .Ltd
	 *
	 * @author lyq
	 * @date 2015年11月6日
	 */
	private class RevHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// 如果消息来自子线程
			if (msg.what == SocketInputThread.SocketInputWhat) {
				userApplication.setWifiConnecting(true);
				WifiConnectTools.processWifiConnect(userApplication,
						iv_connect_tip);
				// 获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer) msg.obj;
				byte[] buffer;
				buffer = temp.array();
				// byte[] revBuffer = (byte[]) msg.obj;
				if (buffer.length != 0) {
					disPlayInfoAfterGetMsg(buffer);
				}
			} else if (msg.what == SocketInputThread.SocketInputUPLOADWhat) {
				userApplication.setWifiConnecting(true);
				WifiConnectTools.processWifiConnect(userApplication,
						iv_connect_tip);
				// 获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer) msg.obj;
				byte[] buffer;
				buffer = temp.array();
				disPlayInfoAfterGetMsg(buffer);
			}else if (msg.what==SocketInputThread.SocketError){
				//wifi中断
				L.d("wifi连接断开。。");
				SocketThreadManager.releaseInstance();
				L.d("单例被释放了-----------------------------");
				//设置全局变量，跟新ui
				userApplication.setWifiConnecting(false);
				WifiConnectTools.processWifiConnect(userApplication, iv_connect_tip);
				ToastUtil.displayPromptInfo(TaskListActivity.this,"wifi连接断开。。");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}
}
