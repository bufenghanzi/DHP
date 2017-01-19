/**
 *
 */
package com.mingseal.data.db;

/**
 * @author wangjian
 */
public class DBInfo {
    /**
     * 数据库
     */
    public static class DB {

        /**
         * 数据库名称
         */
        public static final String DB_NAME = "mingseal_dhp";

        /**
         * 数据库版本
         */
        public static final int VERSION = 1;

        /**
         * 数据库保存的路径
         */
        public static final String DB_LOCATION = "/data/data/com.mingseal.dhp/databases/" + DB_NAME;

        /**
         * 保存之后的数据库的目录
         */
        public static final String DB_SAVED_LOCATION_DIRECTORY = "/mnt/sdcard/com.mingseal.dhp";
        /**
         * 保存之后的数据库的文件
         */
        public static final String DB_SAVED_LOCATION = DB_SAVED_LOCATION_DIRECTORY + "/" + DB_NAME;
    }

    /**
     * PointTask表,任务列表
     */
    public static class TablePointTask {
        /**
         * PointTask表名称
         */
        public static final String TASK_TABLE = "task_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 任务名称
         */
        public static final String TASK_NAME = "taskName";

        /**
         * Point表的主键 List集合
         */
        public static final String POINT_IDS = "pointIDs";

        /**
         * @Fields CREATE_TIME: 创建日期
         */
        public static final String CREATE_TIME = "createTime";
        /**
         * @Fields MODIFY_TIME: 修改日期
         */
        public static final String MODIFY_TIME = "modifyTime";

        /**
         * 创建任务列表
         */
        public static final String CREATE_TASK_TABLE = "create table if not exists " + TASK_TABLE + "(" + _ID
                + " integer primary key, " + TASK_NAME + " text, " + POINT_IDS + " blob, " + CREATE_TIME + " text, "
                + MODIFY_TIME + " text" + ");";

        /**
         * 删除任务列表
         */
        public static final String DROP_POINT_TABLE = "drop table " + TASK_TABLE;
    }

    /**
     * Point表
     */
    public static class TablePoint {
        /**
         * Point表名称
         */
        public static final String POINT_TABLE = "point_table";

        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * x点坐标
         */
        public static final String POINT_X = "x";
        /**
         * y点坐标
         */
        public static final String POINT_Y = "y";
        /**
         * z点坐标
         */
        public static final String POINT_Z = "z";
        /**
         * u点坐标
         */
        public static final String POINT_U = "u";

        /**
         * 要找任务方案的主键，和POINT_TYPE一起找
         */
        public static final String POINT_PARAM_ID = "paramid";

        /**
         * 点类型
         */
        public static final String POINT_TYPE = "pointtype";

        public static String create_point_table(String taskname){
            return "create table if not exists " + POINT_TABLE+taskname + "(" + _ID
                    + " integer primary key, " + POINT_X + " REAL, " + POINT_Y + " REAL, " + POINT_Z + " REAL, "
                    + POINT_U + " REAL, " + POINT_PARAM_ID + " integer, " + POINT_TYPE + " text" + ");";
        }
        public static String drop_point_table(String taskname){
            return "drop table " + POINT_TABLE+taskname;
        }
        public static String alter_point_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ POINT_TABLE+taskname +" RENAME TO "+POINT_TABLE+newTaskname;
        }
    }

    /**
     * 点胶独立点
     */
    public static class TableAlone {

        /**
         * 独立点表名称
         */
        public static final String ALONE_TABLE = "glue_alone_table";

        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 点胶延时
         */
        public static final String DOT_GLUE_TIME = "dotGlueTime";
        /**
         * 停胶延时
         */
        public static final String STOP_GLUE_TIME = "stopGlueTime";
        /**
         * 抬起高度
         */
        public static final String UP_HEIGHT = "upHeight";
        /**
         * 是否暂停
         */
        public static final String IS_PAUSE = "isPause";
        /**
         * 点胶口
         */
        public static final String GLUE_PORT = "glueport";
        /*
         * @Fields z轴倾斜距离
         */
        public static final String DIPDISTANCE_Z = "DipDistanceZ";
        /*
         * @Fields y轴倾斜距离
         */
        public static final String DIPDISTANCE_Y = "DipDistanceY";
        /*
         * @Fields 斜插速度
         */
        public static final String DIPSPEED = "DipSpeed";
        /**
         * 方案号
         */

        /**
         * 调用以生成独立表
         * @param taskname 任务名
         * @return 表名：独立点+任务名
         */
        public static String create_alone_table(String taskname){
            return  "create table if not exists " + ALONE_TABLE+taskname + "(" + _ID
                    + " integer primary key, " + DOT_GLUE_TIME + " integer, " + STOP_GLUE_TIME + " integer, "
                    + UP_HEIGHT + " integer, " + IS_PAUSE + " integer, " + GLUE_PORT + " BLOB, "+ DIPDISTANCE_Y + " integer, "
                    + DIPDISTANCE_Z + " integer, "+ DIPSPEED + " integer"
                    + ");";
        }

        /**
         * 删除表
         * @param taskname
         * @return
         */
        public static String drop_alone_table(String taskname){
            return "drop table " + ALONE_TABLE+taskname;
        }

        /**
         * 更改表名
         * @param taskname
         * @param newTaskname
         * @return
         */
        public static String alter_alone_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ ALONE_TABLE+taskname +" RENAME TO "+ALONE_TABLE+newTaskname;
        }

    }

    /**
     * 面起始点
     */
    public static class TableFaceStart {
        /**
         * 面起始点表名称
         */
        public static final String FACE_START_TABLE = "face_start_table";

        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 出胶前延时
         */
        public static final String OUT_GLUE_TIME_PREV = "outGlueTimePrev";
        /**
         * 出胶(后)延时
         */
        public static final String OUT_GLUE_TIME = "outGlueTime";
        /**
         * 轨迹速度
         */
        public static final String MOVE_SPEED = "moveSpeed";
        /**
         * 是否出胶
         */
        public static final String IS_OUT_GLUE = "isOutGlue";
        /**
         * 停胶延时
         */
        public static final String STOP_GLUE_TIME = "stopGlueTime";
        /**
         * 起始方向 true:x方向 false:y方向
         */
        public static final String START_DIR = "startDir";
        /**
         * 点胶口
         */
        public static final String GLUE_PORT = "gluePort";

        public static String create_face_start_table(String taskname){
            return  "create table if not exists " + FACE_START_TABLE+taskname + "("
                    + _ID + " integer primary key autoincrement, " + OUT_GLUE_TIME_PREV + " integer, " + OUT_GLUE_TIME
                    + " integer, " + MOVE_SPEED + " real, " + IS_OUT_GLUE + " integer, " + STOP_GLUE_TIME + " integer, "
                    + START_DIR + " integer, " + GLUE_PORT + " BLOB" + ");";
        }

        public static String drop_face_start_table(String taskname){
            return  "drop table " + FACE_START_TABLE+taskname;
        }
        public static String alter_face_start_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ FACE_START_TABLE+taskname +" RENAME TO "+FACE_START_TABLE+newTaskname;
        }
    }

    /**
     * 面结束点
     */
    public static class TableFaceEnd {

        /**
         * 面结束点名称
         */
        public static final String FACE_END_TABLE = "face_end_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 停胶延时
         */
        public static final String STOP_GLUE_TIME = "stopGlueTime";
        /**
         * 抬起高度
         */
        public static final String UP_HEIGHT = "upHeight";
        /**
         * 直线条数
         */
        public static final String LINE_NUM = "lineNum";
        /**
         * 是否暂停
         */
        public static final String IS_PAUSE = "isPause";

        public static String create_face_end_table(String taskname){
            return "create table if not exists " + FACE_END_TABLE+taskname + "(" + _ID
                    + " integer primary key autoincrement, " + STOP_GLUE_TIME + " integer, " + UP_HEIGHT + " integer, "
                    + LINE_NUM + " integer, " + IS_PAUSE + " integer" + ");";
        }

        public static String drop_face_end_table(String taskname){
            return "drop table " + FACE_END_TABLE+taskname;
        }

        public static String alter_face_end_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ FACE_END_TABLE+taskname +" RENAME TO "+FACE_END_TABLE+newTaskname;
        }
    }

    /**
     * @author wj
     */
    public static class TableClear {

        /**
         * 清胶点数据表名
         */
        public static final String CLEAR_TABLE = "clear_table";
        /**
         * 清胶点主键
         */
        public static final String _ID = "_id";
        /**
         * 清胶延时
         */
        public static final String CLEAR_GLUE_TIME = "clearGlueTime";

        public static String create_clear_table(String taskname){
            return "create table if not exists " + CLEAR_TABLE+taskname + "(" + _ID
                    + " integer primary key autoincrement, " + CLEAR_GLUE_TIME + " integer" + ");";
        }

        public static String drop_clear_table(String taskname){
            return "drop table " + CLEAR_TABLE+taskname;
        }

        public static String alter_clear_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ CLEAR_TABLE+taskname +" RENAME TO "+CLEAR_TABLE+newTaskname;
        }
    }

    /**
     * 线起始点
     */
    public static class TableLineStart {

        /**
         * 起始点表名称
         */
        public static final String LINE_START_TABLE = "line_start_table";

        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 出胶前延时
         */
        public static final String OUT_GLUE_TIME_PREV = "outGlueTimePrev";
        /**
         * 出胶后延时
         */
        public static final String OUT_GLUE_TIME = "outGlueTime";
//        /**
//         * 延时模式
//         */
//        public static final String TIME_MODE = "timeMode";
        /**
         * 轨迹速度
         */
        public static final String MOVE_SPEED = "moveSpeed";
        /**
         * 停胶前延时
         */
        public static final String STOP_GLUE_TIME_PREV = "stopGlueTimePrev";
        /**
         * 停胶后延时
         */
        public static final String STOP_GLUE_TIME = "stopGlueTime";
        /**
         * 抬起高度
         */
        public static final String UP_HEIGHT = "upHeight";
        /**
         * 点胶口
         */
        public static final String GLUE_PORT = "glueport";

        public static String create_line_start_table(String taskname){
            return "create table if not exists " + LINE_START_TABLE+taskname + "("
                    + _ID + " integer primary key autoincrement, " + OUT_GLUE_TIME_PREV + " integer, " + OUT_GLUE_TIME
                    + " integer, "  + MOVE_SPEED + " real, "
                    + STOP_GLUE_TIME_PREV + " integer, " + STOP_GLUE_TIME + " integer, " + UP_HEIGHT + " integer, "
                    + GLUE_PORT + " BLOB" + ");";
        }

        public static String drop_line_start_table(String taskname){
            return  "drop table " + LINE_START_TABLE+taskname;
        }
        public static String alter_line_start_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ LINE_START_TABLE+taskname +" RENAME TO "+LINE_START_TABLE+newTaskname;
        }
    }

    /**
     * 线中间点
     */
    public static class TableLineMid {
        /**
         * 线中间点表名称
         */
        public static final String LINE_MID_TABLE = "line_mid_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 轨迹速度
         */
        public static final String MOVE_SPEED = "moveSpeed";
        /**
         * 圆角半径
         */
        public static final String RADIUS = "radius";
        /**
         * 断胶前距离
         */
        public static final String STOP_GLUE_DIS_PREV = "stopGlueDisPrev";
        /**
         * 断胶后距离
         */
        public static final String STOP_GLUE_DIS_NEXT = "stopGlueDisNext";
        /**
         * 点胶口
         */
        public static final String GLUE_PORT = "gluePort";

        public static String create_line_mid_table(String taskname){
            return "create table if not exists " + LINE_MID_TABLE+taskname + "(" + _ID
                    + " integer primary key autoincrement, " + MOVE_SPEED + " real, " + RADIUS + " real, "
                    + STOP_GLUE_DIS_PREV + " real, " + STOP_GLUE_DIS_NEXT + " real, " + GLUE_PORT + " BLOB" + ");";
        }

        public static String drop_line_mid_table(String taskname){
            return "drop table " + LINE_MID_TABLE+taskname;
        }
        public static String alter_line_mid_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ LINE_MID_TABLE+taskname +" RENAME TO "+LINE_MID_TABLE+newTaskname;
        }
    }

    /**
     * 线结束点
     */
    public static class TableLineEnd {
        /**
         * 线结束点表名称
         */
        public static final String LINE_END_TABLE = "line_end_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 停胶(后)延时
         */
        public static final String STOP_GLUE_TIME = "stopGlueTime";
        /**
         * 抬起高度
         */
        public static final String UP_HEIGHT = "upHeight";
        /**
         * 提前停胶距离
         */
        public static final String BREAK_GLUE_LEN = "breakGlueLen";
        /**
         * 拉丝距离
         */
        public static final String DRAW_DISTANCE = "drawDistance";
        /**
         * 拉丝速度
         */
        public static final String DRAW_SPEED = "drawSpeed";
        /**
         * 是否暂停
         */
        public static final String IS_PAUSE = "isPause";

        public static String create_line_end_table(String taskname){
            return "create table if not exists " + LINE_END_TABLE+taskname + "(" + _ID
                    + " integer primary key autoincrement, "  + STOP_GLUE_TIME
                    + " integer, " + UP_HEIGHT + " integer, " + BREAK_GLUE_LEN + " integer, " + DRAW_DISTANCE + " integer, "
                    + DRAW_SPEED + " integer, " + IS_PAUSE + " integer" + ");";
        }

        public static String drop_line_end_table(String taskname){
            return "drop table " + LINE_END_TABLE+taskname;
        }
        public static String alter_line_end_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ LINE_END_TABLE+taskname +" RENAME TO "+LINE_END_TABLE+newTaskname;
        }

    }

    /**
     * 点胶输出IO
     */
    public static class TableOutputIO {

        /**
         * 输出IO表名称
         */
        public static final String OUTPUT_IO_TABLE = "output_io_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 动作前延时
         */
        public static final String GO_TIME_PREV = "goTimePrev";
        /**
         * 动作后延时
         */
        public static final String GO_TIME_NEXT = "goTimeNext";
        /**
         * 输入口
         */
        public static final String INPUT_PORT = "inputPort";

        public static String create_output_io_table(String taskname){
            return "create table if not exists " + OUTPUT_IO_TABLE+taskname + "(" + _ID
                    + " integer primary key autoincrement, " + GO_TIME_PREV + " integer, " + GO_TIME_NEXT + " integer, "
                    + INPUT_PORT + " BLOB" + ");";
        }

        public static String drop_output_io_table(String taskname){
            return "drop table " + OUTPUT_IO_TABLE+taskname;
        }
        public static String alter_output_io_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ OUTPUT_IO_TABLE+taskname +" RENAME TO "+OUTPUT_IO_TABLE+newTaskname;
        }

    }

    /**
     * 点胶输入IO
     */
    public static class TableInputIO {

        /**
         * 输入IO表名称
         */
        public static final String INPUT_IO_TABLE = "input_io_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 动作前延时
         */
        public static final String GO_TIME_PREV = "goTimePrev";
        /**
         * 动作后延时
         */
        public static final String GO_TIME_NEXT = "goTimeNext";
        /**
         * 输入口
         */
        public static final String INPUT_PORT = "inputPort";

        public static String create_input_io_table(String taskname){
            return "create table if not exists " + INPUT_IO_TABLE+taskname + "(" + _ID
                    + " integer primary key autoincrement, " + GO_TIME_PREV + " integer, " + GO_TIME_NEXT + " integer, "
                    + INPUT_PORT + " BLOB" + ");";
        }
//        /**
//         * 创建输入IO表语
//         */
//        public static final String CREATE_INPUT_IO_TABLE = "create table if not exists " + INPUT_IO_TABLE + "(" + _ID
//                + " integer primary key autoincrement, " + GO_TIME_PREV + " integer, " + GO_TIME_NEXT + " integer, "
//                + INPUT_PORT + " BLOB" + ");";

        public static String drop_input_io_table(String taskname){
            return "drop table " + INPUT_IO_TABLE+taskname;
        }
        public static String alter_input_io_table(String taskname,String newTaskname){
            return "ALTER TABLE "+ INPUT_IO_TABLE+taskname +" RENAME TO "+INPUT_IO_TABLE+newTaskname;
        }
//        /**
//         * 删除输入IO表语
//         */
//        public static final String DROP_INPUT_IO_TABLE = "drop table " + INPUT_IO_TABLE;

    }

    public static class TableUser {
        /**
         * 用户表名称
         */
        public static final String USER_TABLE = "user_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * 用户名
         */
        public static final String USERNAME = "username";
        /**
         * 密码
         */
        public static final String PASSWORD = "password";
        /**
         * 类型(管理员/技术支持/操作员)
         */
        public static final String TYPE = "type";
        /**
         * 创建用户表语句
         */
        public static final String CREATE_USER_TABLE = "create table if not exists " + USER_TABLE + "(" + _ID
                + " integer primary key autoincrement, " + USERNAME + " text, " + PASSWORD + " text, " + TYPE + " text"
                + ");";
        /**
         * 删除用户表语
         */
        public static final String DROP_USER_TABLE = "drop table " + USER_TABLE;

    }

    public static class WifiSSID {
        /**
         * wifi名称
         */
        public static final String WIFI_TABLE = "wifi_table";
        /**
         * 主键
         */
        public static final String _ID = "_id";
        /**
         * wifi-ssid
         */
        public static final String SSID = "ssid";
        /**
         * 创建用户表语句
         */
        public static final String CREATE_WIFI_TABLE = "create table if not exists " + WIFI_TABLE + "(" + _ID
                + " integer primary key autoincrement, " + SSID + " text " + ");";
        /**
         * 删除用户表语
         */
        public static final String DROP_WIFI_TABLE = "drop table " + WIFI_TABLE;
    }
}
