/**
 * 
 */
package com.mingseal.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 商炎炳
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库名称
	 * @param factory
	 *            游标工厂
	 * @param version
	 *            数据库版本
	 */
	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		this(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(DBInfo.TableUser.CREATE_USER_TABLE);
		db.execSQL(DBInfo.TablePointTask.CREATE_TASK_TABLE);
		//在创建表的时候把wifi的ssid添加到数据库
		db.execSQL(DBInfo.WifiSSID.CREATE_WIFI_TABLE);
		if (db.isOpen()){
			new Thread() {
				public void run() {
					int i=10000;
					int j=1;
					StringBuffer sb=null;
					db.beginTransaction();
					while(i>0){
                        if (j<=9){
                            sb= new StringBuffer("MS-DJ0000");
                            sb.append(j);
//                            System.out.println("插入到数据库的ssid："+sb.toString());
                        }else if (j<=99){
                            sb = new StringBuffer("MS-DJ000");
                            sb.append(j);
//                            System.out.println("插入到数据库的ssid："+sb.toString());
                        }else if (j<=999){
                            sb = new StringBuffer("MS-DJ00");
                            sb.append(j);
//                            System.out.println("插入到数据库的ssid："+sb.toString());
                        }else if (j<=9999){
                            sb = new StringBuffer("MS-DJ0");
                            sb.append(j);
//                            System.out.println("插入到数据库的ssid："+sb.toString());
                        }else if (j<=99999){
                            sb =  new StringBuffer("MS-DJ");
                            sb.append(j);
//                            System.out.println("插入到数据库的ssid："+sb.toString());
                        }
                        db.execSQL("insert into wifi_table(SSID) values(?)",new Object[]{sb.toString()});
                        i--;
                        j++;
                    }
					db.setTransactionSuccessful();
					db.endTransaction();
				}
			}.start();
		}
	}

	/*
	 * VERSION改变会更新所有的表
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.
	 * sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBInfo.TableUser.DROP_USER_TABLE);
//		db.execSQL(DBInfo.TableAlone.DROP_ALONE_TABLE);
//		db.execSQL(DBInfo.TableFaceStart.DROP_FACE_START_TABLE);
//		db.execSQL(DBInfo.TablePoint.DROP_POINT_TABLE);
//		db.execSQL(DBInfo.TableFaceEnd.DROP_FACE_END_TABLE);
//		db.execSQL(DBInfo.TableClear.DROP_CLEAR_TABLE);
//		db.execSQL(DBInfo.TableLineStart.DROP_LINE_START_TABLE);
//		db.execSQL(DBInfo.TableLineMid.DROP_LINE_MID_TABLE);
//		db.execSQL(DBInfo.TableLineEnd.DROP_LINE_END_TABLE);
//		db.execSQL(DBInfo.TableOutputIO.DROP_OUTPUT_IO_TABLE);
//		db.execSQL(DBInfo.TableInputIO.DROP_INPUT_IO_TABLE);
		db.execSQL(DBInfo.TablePointTask.DROP_POINT_TABLE);
		db.execSQL(DBInfo.WifiSSID.DROP_WIFI_TABLE);
		onCreate(db);
	}
	

}
