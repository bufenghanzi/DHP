package com.mingseal.data.point.glueparam;

import android.os.Parcel;
import android.os.Parcelable;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import java.util.Arrays;

/**
 * 点胶线中间点参数类
 * 
 * @author lyq
 */
public class PointGlueLineMidParam extends PointParam {

	private float moveSpeed; // 轨迹速度
	private float radius; // 圆角半径
	private float stopGlueDisPrev;// 断胶前距离
//	private float outGlueDisNext;//滞后出胶距离 (原)
	private float stopGLueDisNext; // 断胶后距离
	private boolean[] gluePort; // 点胶口

	/**
	 * 点胶线中间点参数构造方法,默认值为
	 * 
	 * @moveSpeed 轨迹速度 1
	 * @radius 圆角半径 0
	 * @stopGlueDisPrev 断胶前距离 0
	 * @stopGlueDisNext 断胶后距离 0
	 * @isOutGlue 是否出胶 是
	 */
	public PointGlueLineMidParam() {
		pointGlueLineMidInit(1, 0, 0, 0);
		super.setPointType(PointType.POINT_GLUE_LINE_MID);
		gluePort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		gluePort[0] = true;
	}

	/**
	 * 点胶线中间点初始化构造方法
	 * 
	 * @param moveSpeed
	 *            轨迹速度
	 * @param radius
	 *            圆角半径
	 * @param stopGlueDisPrev
	 *            断胶前距离
	 * @param stopGlueDisNext
	 *            断胶后距离
	 * @param gluePort
	 *            点胶口数据
	 */
	public PointGlueLineMidParam(int moveSpeed, float radius, int stopGlueDisPrev, int stopGlueDisNext,
			 boolean[] gluePort) {
		pointGlueLineMidInit(moveSpeed, radius, stopGlueDisPrev, stopGlueDisNext);
		super.setPointType(PointType.POINT_GLUE_LINE_MID);
		this.gluePort = gluePort;
	}

	/**
	 * 点胶线中间点参数私有初始化方法
	 * 
	 * @param moveSpeed
	 *            轨迹速度
	 * @param radius
	 *            圆角半径
	 * @param stopGlueDisPrev
	 *            断胶前距离
	 * @param stopGLueDisNext
	 *            断胶后距离
	 * @gluePort 10000000000000000000
	 */
	private void pointGlueLineMidInit(int moveSpeed, float radius, int stopGlueDisPrev, int stopGLueDisNext) {
		this.moveSpeed = moveSpeed;
		this.radius = radius;
		this.stopGlueDisPrev = stopGlueDisPrev;
		this.stopGLueDisNext = stopGLueDisNext;
	}

	/**
	 * @return 获取轨迹速度
	 */
	public float getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * 设置轨迹速度
	 * 
	 * @param moveSpeed
	 *            轨迹速度
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * @return 获取圆角半径
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * 设置圆角半径
	 * 
	 * @param radius
	 *            圆角半径
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * @return 获取提前停胶距离
	 */
	public float getStopGlueDisPrev() {
		return stopGlueDisPrev;
	}

	/**
	 * 设置提前停胶距离
	 * 
	 * @param stopGlueDisPrev
	 *            提前停胶距离
	 */
	public void setStopGlueDisPrev(float stopGlueDisPrev) {
		this.stopGlueDisPrev = stopGlueDisPrev;
	}

	/**
	 * @return 获取断胶后距离
	 */
	public float getStopGLueDisNext() {
		return stopGLueDisNext;
	}

	/**
	 * 设置断胶后距离
	 * 
	 * @param stopGLueDisNext
	 *            断胶后距离
	 */
	public void setStopGLueDisNext(float stopGLueDisNext) {
		this.stopGLueDisNext = stopGLueDisNext;
	}

//	/**
//	 * @return 获取是否出胶
//	 */
//	public boolean isOutGlue() {
//		return isOutGlue;
//	}
//
//	/**
//	 * 设置是否出胶
//	 *
//	 * @param isOutGlue
//	 *            是否出胶
//	 */
//	public void setOutGlue(boolean isOutGlue) {
//		this.isOutGlue = isOutGlue;
//	}

	/**
	 * @return 获取点胶口数据
	 */
	public boolean[] getGluePort() {
		return gluePort;
	}

	/**
	 * 设置点胶口数据
	 * 
	 * @param gluePort
	 *            点胶口数据
	 */
	public void setGluePort(boolean[] gluePort) {
		this.gluePort = gluePort;
	}
	

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + Arrays.hashCode(gluePort);
//		result = prime * result + (isOutGlue ? 1231 : 1237);
//		result = prime * result + moveSpeed;
//		result = prime * result + Float.floatToIntBits(radius);
//		result = prime * result + Float.floatToIntBits(stopGLueDisNext);
//		result = prime * result + Float.floatToIntBits(stopGlueDisPrev);
//		return result;
//	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (moveSpeed != +0.0f ? Float.floatToIntBits(moveSpeed) : 0);
		result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
		result = 31 * result + (stopGlueDisPrev != +0.0f ? Float.floatToIntBits(stopGlueDisPrev) : 0);
		result = 31 * result + (stopGLueDisNext != +0.0f ? Float.floatToIntBits(stopGLueDisNext) : 0);
		result = 31 * result + Arrays.hashCode(gluePort);
		return result;
	}


//	@Override
//	public int hashCode() {
//		int result = super.hashCode();
//		result = 31 * result + moveSpeed;
//		result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
//		result = 31 * result + (stopGlueDisPrev != +0.0f ? Float.floatToIntBits(stopGlueDisPrev) : 0);
//		result = 31 * result + (stopGLueDisNext != +0.0f ? Float.floatToIntBits(stopGLueDisNext) : 0);
//		result = 31 * result + Arrays.hashCode(gluePort);
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointGlueLineMidParam other = (PointGlueLineMidParam) obj;
		if (!Arrays.equals(gluePort, other.gluePort))
			return false;
		if (moveSpeed != other.moveSpeed)
			return false;
		if (Float.floatToIntBits(radius) != Float.floatToIntBits(other.radius))
			return false;
		if (Float.floatToIntBits(stopGLueDisNext) != Float.floatToIntBits(other.stopGLueDisNext))
			return false;
		if (Float.floatToIntBits(stopGlueDisPrev) != Float.floatToIntBits(other.stopGlueDisPrev))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PointGlueLineMidParam [moveSpeed=" + moveSpeed + ", radius=" + radius + ", stopGlueDisPrev="
				+ stopGlueDisPrev + ", stopGLueDisNext=" + stopGLueDisNext + ", gluePort="
				+ Arrays.toString(gluePort) + ", get_id()=" + get_id() + "]";
	}
	public String getString(){
		return "PointGlueLineMidParam [moveSpeed=" + moveSpeed + ", radius=" + radius + ", stopGlueDisPrev="
				+ stopGlueDisPrev + ", stopGLueDisNext=" + stopGLueDisNext + ", gluePort="
				+ Arrays.toString(gluePort) +  "]";
	}
	public static final Parcelable.Creator<PointGlueLineMidParam> CREATOR = new Creator<PointGlueLineMidParam>() {

		@Override
		public PointGlueLineMidParam createFromParcel(Parcel source) {
			PointGlueLineMidParam point = new PointGlueLineMidParam();
			point.moveSpeed = source.readFloat();
			point.radius = source.readFloat();
			point.stopGlueDisPrev = source.readFloat();
			point.stopGLueDisNext = source.readFloat();
			boolean[] val = null;
			val = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
			source.readBooleanArray(val);
			point.gluePort = val;
			point.set_id(source.readInt());
			
			return point;
		}

		@Override
		public PointGlueLineMidParam[] newArray(int size) {
			
			return new PointGlueLineMidParam[size];
		}
	}; 

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(moveSpeed);
		dest.writeFloat(radius);
		dest.writeFloat(stopGlueDisPrev);
		dest.writeFloat(stopGLueDisNext);
		dest.writeBooleanArray(gluePort);
		dest.writeInt(get_id());
	}
	
	
	
	

}
