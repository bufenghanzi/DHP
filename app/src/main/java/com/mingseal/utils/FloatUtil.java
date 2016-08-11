package com.mingseal.utils;

import java.text.DecimalFormat;

public class FloatUtil {
	/**
	 * float显示三位小数，转换成String类型显示
	 * 
	 * @param num
	 * @return
	 */
	public static String getFloatToString(Float num) {
		DecimalFormat fnum = new DecimalFormat("0.000");
		String parseNum = fnum.format(num);

		return parseNum;

	}
	
	/**
	 * float显示三位小数，转换成String类型显示
	 * @param num
	 * @return
	 */
	public static String getFloatToString(Double num) {
		DecimalFormat fnum = new DecimalFormat();
		fnum.applyPattern("0.000");
		String parseNum = fnum.format(num);
		
		return parseNum;
		
	}
	/**
	 * float显式6位小数，转换成String类型显示
	 * @param num
	 * @return
	 */
	public static String getFloat2String(Double num) {
		DecimalFormat fnum = new DecimalFormat();
		fnum.applyPattern("0.000000");
		String parseNum = fnum.format(num);

		return parseNum;

	}
}
