package com.mingseal.data.point.glueparam;

import android.os.Parcel;
import android.os.Parcelable;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 点胶线结束点参数类
 * 
 * @author lyq
 */
public class PointGlueLineEndParam extends PointParam {

//	private int stopGlueTimePrev; // 停胶前延时
	private int stopGlueTime; // 停胶(后)延时
	private int upHeight; // 抬起高度
	private int breakGlueLen; // 提前停胶距离
	private int drawDistance; // 拉丝距离
	private int drawSpeed; // 拉丝速度
	private boolean isPause; // 是否暂停

	/**
	 * 点胶线结束点参数构造方法,默认值为
	 * 
	 * @stopGlueTimePrev 停胶前延时 0
	 * @stopGlueTime 停胶(后)延时 0
	 * @upHeight 抬起高度 0
	 * @breakGlueLen 断胶长度 0
	 * @drawDistance 拉丝距离 0
	 * @drawSpeed 拉丝速度 0
	 * @isPause 是否暂停 否
	 */
	public PointGlueLineEndParam() {
		pointGlueLineEndInit(0, 0, 0, 0, 0, false);
		super.setPointType(PointType.POINT_GLUE_LINE_END);
	}

	/**
	 * 点胶线结束点初始化构造方法
	 * @param stopGlueTime
	 *            停胶延时
	 * @param upHeight
	 *            抬起高度
	 * @param breakGlueLen
	 *            断胶长度
	 * @param drawDistance
	 *            拉丝距离
	 * @param drawSpeed
	 *            拉丝速度
	 * @param isPause
	 *            是否暂停
	 */
	public PointGlueLineEndParam( int stopGlueTime, int upHeight, int breakGlueLen,
			int drawDistance, int drawSpeed, boolean isPause) {
		pointGlueLineEndInit( stopGlueTime, upHeight, breakGlueLen, drawDistance, drawSpeed, isPause);
		super.setPointType(PointType.POINT_GLUE_LINE_END);

	}

	/**
	 * 点胶线结束点参数私有初始化方法
	 * 
	 * @param stopGlueTime
	 *            停胶延时
	 * @param upHeight
	 *            抬起高度
	 * @param breakGlueLen
	 *            断胶长度
	 * @param drawDistance
	 *            拉丝距离
	 * @param drawSpeed
	 *            拉丝速度
	 * @param isPause
	 *            是否暂停
	 */
	private void pointGlueLineEndInit(int stopGlueTime, int upHeight, int breakGlueLen,
			int drawDistance, int drawSpeed, boolean isPause) {
		this.stopGlueTime = stopGlueTime;
		this.upHeight = upHeight;
		this.breakGlueLen = breakGlueLen;
		this.drawDistance = drawDistance;
		this.drawSpeed = drawSpeed;
		this.isPause = isPause;
	}

//	/**
//	 * @return 提前关胶距离（mm）
//	 */
//	public int getStopGlueTimePrev() {
//		return stopGlueTimePrev;
//	}
//
//	/**
//	 * 设置提前关胶距离（mm）
//	 *
//	 * @param stopGlueTimePrev
//	 *            提前关胶距离（mm）
//	 */
//	public void setStopGlueTimePrev(int stopGlueTimePrev) {
//		this.stopGlueTimePrev = stopGlueTimePrev;
//	}

	/**
	 * @return 获取停胶延时（ms）
	 */
	public int getStopGlueTime() {
		return stopGlueTime;
	}

	/**
	 * 设置停胶延时（ms）
	 * 
	 * @param stopGlueTime
	 *           停胶延时（ms）
	 */
	public void setStopGlueTime(int stopGlueTime) {
		this.stopGlueTime = stopGlueTime;
	}

	/**
	 * @return 抬起高度
	 */
	public int getUpHeight() {
		return upHeight;
	}

	/**
	 * 设置抬起高度
	 * 
	 * @param upHeight
	 *            抬起高度
	 */
	public void setUpHeight(int upHeight) {
		this.upHeight = upHeight;
	}

	/**
	 * @return 获取断胶长度
	 */
	public int getBreakGlueLen() {
		return breakGlueLen;
	}

	/**
	 * 设置断胶长度
	 * 
	 * @param breakGlueLen
	 *            断胶长度
	 */
	public void setBreakGlueLen(int breakGlueLen) {
		this.breakGlueLen = breakGlueLen;
	}

	/**
	 * @return 拉丝距离
	 */
	public int getDrawDistance() {
		return drawDistance;
	}

	/**
	 * 设置拉丝距离
	 * 
	 * @param drawDistance
	 *            拉丝距离
	 */
	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}

	/**
	 * @return 获取拉丝速度
	 */
	public int getDrawSpeed() {
		return drawSpeed;
	}

	/**
	 * 设置拉丝速度
	 * 
	 * @param drawSpeed
	 *            拉丝速度
	 */
	public void setDrawSpeed(int drawSpeed) {
		this.drawSpeed = drawSpeed;
	}

	/**
	 * @return 是否暂停
	 */
	public boolean isPause() {
		return isPause;
	}

	/**
	 * 设置是否暂停
	 * 
	 * @param isPause
	 *            是否暂停
	 */
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	@Override
	public String toString() {
		return "PointGlueLineEndParam [ stopGlueTime=" + stopGlueTime
				+ ", upHeight=" + upHeight + ", breakGlueLen=" + breakGlueLen + ", drawDistance=" + drawDistance
				+ ", drawSpeed=" + drawSpeed + ", isPause=" + isPause + ", get_id()=" + get_id() + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		PointGlueLineEndParam that = (PointGlueLineEndParam) o;

		if (stopGlueTime != that.stopGlueTime) return false;
		if (upHeight != that.upHeight) return false;
		if (breakGlueLen != that.breakGlueLen) return false;
		if (drawDistance != that.drawDistance) return false;
		if (drawSpeed != that.drawSpeed) return false;
		return isPause == that.isPause;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + stopGlueTime;
		result = 31 * result + upHeight;
		result = 31 * result + breakGlueLen;
		result = 31 * result + drawDistance;
		result = 31 * result + drawSpeed;
		result = 31 * result + (isPause ? 1 : 0);
		return result;
	}

	public static final Parcelable.Creator<PointGlueLineEndParam> CREATOR = new Creator<PointGlueLineEndParam>() {

		@Override
		public PointGlueLineEndParam[] newArray(int size) {
			return new PointGlueLineEndParam[size];
		}

		@Override
		public PointGlueLineEndParam createFromParcel(Parcel source) {
			PointGlueLineEndParam point = new PointGlueLineEndParam();
			point.stopGlueTime = source.readInt();
			point.upHeight = source.readInt();
			point.breakGlueLen = source.readInt();
			point.drawDistance = source.readInt();
			point.drawSpeed = source.readInt();
			point.isPause = (source.readInt() == 0) ? false : true;
			point.set_id(source.readInt());

			return point;
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(stopGlueTime);
		dest.writeInt(upHeight);
		dest.writeInt(breakGlueLen);
		dest.writeInt(drawDistance);
		dest.writeInt(drawSpeed);
		dest.writeInt((boolean) isPause ? 1 : 0);
		dest.writeInt(get_id());
	}

}
