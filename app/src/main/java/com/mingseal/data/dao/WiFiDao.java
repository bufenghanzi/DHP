/**
 * 
 */
package com.mingseal.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;

/**
 * @author wj
 *
 */
public class WiFiDao {

	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { DBInfo.WifiSSID._ID, DBInfo.WifiSSID.SSID };

	/**
	 * 初始化
	 *
	 * @param context
	 */
	public WiFiDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	//通过wifissid寻找到当前的主键
	public int findNumbySSID(String wifiSSID){
		int id=-1;
		db=dbHelper.getWritableDatabase();
		Cursor cursor= null;
		try {
			cursor = db.query(DBInfo.WifiSSID.WIFI_TABLE,columns, DBInfo.WifiSSID.SSID+"=?",new String[]{wifiSSID},null,null,null);
			if (cursor!=null&&cursor.getCount()>0){
                while (cursor.moveToNext()){
                    id=cursor.getInt(cursor.getColumnIndex(DBInfo.WifiSSID._ID));
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor!=null){
				cursor.close();
			}
		}
		db.close();
		return id;
	}

//	/**
//	 * 通过参数寻找到当前方案的主键
//	 *
//	 * @param pointGlueOutPutIOParam
//	 * @return 当前方案的主键
//	 */
//	public int getOutputParamIDByParam(PointGlueOutputIOParam pointGlueOutPutIOParam) {
//		int id = -1;
//		db = dbHelper.getReadableDatabase();
//		Cursor cursor = db.query(DBInfo.TableOutputIO.OUTPUT_IO_TABLE, columns,
//				DBInfo.TableOutputIO.GO_TIME_PREV + "=? and " + DBInfo.TableOutputIO.GO_TIME_NEXT + "=? and "
//						+ DBInfo.TableOutputIO.INPUT_PORT + "=?",
//				new String[] { String.valueOf(pointGlueOutPutIOParam.getGoTimePrev()),
//						String.valueOf(pointGlueOutPutIOParam.getGoTimeNext()),
//						Arrays.toString(pointGlueOutPutIOParam.getInputPort()) },
//				null, null, null);
//		if (cursor != null && cursor.getCount() > 0) {
//			while (cursor.moveToNext()) {
//				id = cursor.getInt(cursor.getColumnIndex(DBInfo.TableOutputIO._ID));
//			}
//		}
//		db.close();
//		return id;
//	}

}
