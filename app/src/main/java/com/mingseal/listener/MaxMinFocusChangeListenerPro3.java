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
 * @Description 最大最小值的输入框判断
 * @author wj
 * @date 2016年1月27日 上午10:50:28
 *
 */
public class MaxMinFocusChangeListenerPro3 implements OnFocusChangeListener {

	/**
	 * @Fields maxValue: 最大值
	 */
	private float maxValue;
	/**
	 * @Fields minValue: 最小值
	 */
	private float minValue;
	/**
	 * @Fields etNumber: 当前Edittext控件
	 */
	private EditText etNumber;
	/**
	 * @Fields value: 获取当前输入框的值
	 */
	private float value;

	/**
	 * @Title MaxMinFocusChangeListener
	 * @Description if(>=maxValue)取maxValue;else(<=minValue)取minValue
	 * @param maxValue
	 *            允许输入的最大值
	 * @param minValue
	 *            允许输入的最小值
	 * @param etNumber
	 *            当前Edittext控件
	 */
	public MaxMinFocusChangeListenerPro3(float maxValue, float minValue, EditText etNumber) {
		super();
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.etNumber = etNumber;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			if (etNumber.getText().toString().equals("")) {
				etNumber.setText(minValue + "");
			}
			try {
				value = Float.parseFloat(etNumber.getText().toString());
			}catch (NumberFormatException e){
				value=minValue;
			}
			//向下取整
			if (value>1){
				value= (int) Math.floor(value);
				etNumber.setText(String.valueOf(value));
			}

			if (value <= minValue) {
				etNumber.setText(minValue + "");
			} else if (value >= maxValue) {
				etNumber.setText(maxValue + "");
			} else {
				etNumber.setText(value + "");
			}
		}
	}

}
