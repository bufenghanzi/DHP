/**
 * 
 */
package com.mingseal.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mingseal.utils.CameraInterface;
import com.mingseal.utils.DisplayUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author 商炎炳
 *
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "CameraSurfaceView";
	private CameraInterface mCameraInterface;
	private SurfaceHolder mSurfaceHolder;
	private Context context;
	float previewRate = -1f;
	public CameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CameraSurfaceView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);// translucent半透明
															// transparent透明

		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		Log.d(TAG, "width:" + width + ",height:" + height);
		setMeasuredDimension(width, AutoUtils.getPercentHeightSize(1200));
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		previewRate = DisplayUtil.getScreenRate(context); // 默认全屏的比例预览
		CameraInterface.getInstance(context).doStartPreview(holder, previewRate);
		Log.i(TAG, "surfaceCreated...");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i(TAG, "surfaceChanged...");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed...");
		CameraInterface.getInstance(context).doStopCamera();
	}

	public SurfaceHolder getSurfaceHolder() {
		return mSurfaceHolder;
	}
}
