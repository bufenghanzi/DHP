/**
 * @Title MaxMinFocusChangeListener.java
 * @Package com.mingseal.listener
 * @Description TODO
 * @author 商炎炳
 * @date 2016年1月27日 上午10:50:28
 * @version V1.0
 */
package com.mingseal.listener;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * @ClassName MaxMinFocusChangeListener
 * @Description 最大最小值的输入框判断,以及互斥事件
 *
 */
public class MaxMinFocusChangeListenerPro implements OnFocusChangeListener {

	/**
	 * @Fields maxValue: 最大值
	 */
	private int maxValue;
	/**
	 * @Fields minValue: 最小值
	 */
	private int minValue;
	/**
	 * @Fields etNumber: 当前Edittext控件
	 */
	private EditText etNumber;
	/**
	 * @Fields etNumber: 后置位Edittext控件
	 */
	private EditText etNumber2;
	/**
	 * @Fields value: 获取当前输入框的值
	 */
	private int value;
	private int value2;

	/**
	 * @Title MaxMinFocusChangeListener
	 * @Description if(>=maxValue)取maxValue;else(<=minValue)取minValue
	 * @param maxValue
	 *            允许输入的最大值
	 * @param minValue
 *            允许输入的最小值
	 * @param etNumber
	 */
	public MaxMinFocusChangeListenerPro(int maxValue, int minValue, EditText etNumber, EditText etNumber2) {
		super();
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.etNumber = etNumber;
		this.etNumber2 = etNumber2;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			if (etNumber.getText().toString().equals("")) {
				etNumber.setText(minValue + "");
			}
			try {
				value = Integer.parseInt(etNumber.getText().toString());
			}catch (NumberFormatException e){
				value=minValue;
			}
			try {
				value2 = Integer.parseInt(etNumber2.getText().toString());
			}catch (NumberFormatException e){
				value2=minValue;
			}
			if (value <= minValue) {
				etNumber.setText(minValue + "");
			} else if (value >= maxValue) {
				etNumber.setText(maxValue + "");
			} else {
				etNumber.setText(value + "");
			}

			if (value>0){
				etNumber2.setText(0+"");
			}else if (value2>0){
				etNumber.setText(0+"");
			}
		}
	}

}
