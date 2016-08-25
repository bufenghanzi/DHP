/**
 *
 */
package com.mingseal.utils;

import android.content.Context;

import com.mingseal.data.dao.GlueAloneDao;
import com.mingseal.data.dao.GlueClearDao;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.dao.GlueFaceStartDao;
import com.mingseal.data.dao.GlueInputDao;
import com.mingseal.data.dao.GlueLineEndDao;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.dao.GlueLineStartDao;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 商炎炳
 * @description 上传解析数据成功之后需要把数据变成自己想要的List格式
 */
public class UploadTaskAnalyse {

    private static final String TAG = "UploadTaskAnalyse";
    /**
     * 任务名
     */
    private String taskname;
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
    /**
     * 清胶点的数据库操作
     */
    private GlueClearDao glueClearDao;// 清胶点的数据库操作
    /**
     * 输入IO的数据库操作
     */
    private GlueInputDao glueInputDao;// 输入IO的数据库操作
    /**
     * 输出IO的数据库操作
     */
    private GlueOutputDao glueOutputDao;// 输出IO的数据库操作
    private int glueAlone_key=0;
    private int glueLineStart_key=0;
    private int glueLineMid_key=0;
    private int glueLineEnd_key=0;
    private int glueFaceStart_key=0;
    private int glueFaceEnd_key=0;
    private int glueClear_key=0;
    private int glueInput_key=0;
    private int glueOutput_key=0;

    /**
     * 初始化各个和数据库操作的Dao
     *
     * @param context
     */
    public UploadTaskAnalyse(Context context, String taskname) {
        glueAloneDao = new GlueAloneDao(context);
        glueLineStartDao = new GlueLineStartDao(context);
        glueLineMidDao = new GlueLineMidDao(context);
        glueLineEndDao = new GlueLineEndDao(context);
        glueFaceStartDao = new GlueFaceStartDao(context);
        glueFaceEndDao = new GlueFaceEndDao(context);
        glueClearDao = new GlueClearDao(context);
        glueInputDao = new GlueInputDao(context);
        glueOutputDao = new GlueOutputDao(context);
        this.taskname = taskname;
    }

    /**
     * 解析下载成功的任务（*重写HashCode方法*）
     *
     * @param pointUploads
     * @return 解析之后的List数据
     */
    public List<Point> analyseTaskSuccess(List<Point> pointUploads) {
        // 上传成功里面的Point的List数组
        List<Point> points = new ArrayList<>();
        // 用于上传成功之后数据的PointParam的解析
        PointParam pointParam = null;
        // 独立点参数
        PointGlueAloneParam aloneParam = null;
        // 起始点参数
        PointGlueLineStartParam lineStartParam = null;
        // 中间点参数
        PointGlueLineMidParam lineMidParam = null;
        // 结束点参数
        PointGlueLineEndParam lineEndParam = null;
        // 面起始点参数
        PointGlueFaceStartParam faceStartParam = null;
        // 面结束点参数
        PointGlueFaceEndParam faceEndParam = null;
        // 输入IO参数
        PointGlueInputIOParam inputParam = null;
        // 输出IO参数
        PointGlueOutputIOParam outputParam = null;
        // 各个点胶口的初始化，因为下载是有24个的，实际上保存的时候是不需要这么多的
        boolean[] ports = null;
        // 独立点HashMap集合
        HashMap<String, Integer> aloneParamMaps = new HashMap<>();
        // 起始点HashMap集合
        HashMap<String, Integer> lineStartParamMaps = new HashMap<>();
        // 中间点HashMap集合
        HashMap<String, Integer> lineMidParamMaps = new HashMap<>();
        // 结束点HashMap集合
        HashMap<String, Integer> lineEndParamMaps = new HashMap<>();
        // 面起始点HashMap集合
        HashMap<String, Integer> faceStartParamMaps = new HashMap<>();
        // 面结束点HashMap集合
        HashMap<String, Integer> faceEndParamMaps = new HashMap<>();
        // 输入IO点HashMap集合
        HashMap<String, Integer> inputParamMaps = new HashMap<>();
        // 输出IO点HashMap集合
        HashMap<String, Integer> outputParamMaps = new HashMap<>();

        for (Point point : pointUploads) {
            if (point.getPointParam().getPointType() == PointType.POINT_GLUE_BASE) {
                // 基准点解析
                points.add(point);
            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_ALONE) {
                // 独立点解析
                aloneParam = (PointGlueAloneParam) point.getPointParam();
                ports = new boolean[20];
                for (int i = 0; i < 20; i++) {
                    ports[i] = aloneParam.getGluePort()[i];
                }
                aloneParam.setGluePort(ports);
                System.out.println("aloneParam.toString():"+aloneParam.toString());
                System.out.println("aloneParam.toString():"+aloneParam.hashCode());
                // 先判断Map里面有没有，有的话，直接添加，无需插入数据库
                pointParam = new PointParam();
                System.out.println("aloneParamMaps.contains(aloneParam.hashCode())::"+aloneParamMaps.containsKey(aloneParam.toString()));
                if (aloneParamMaps.containsKey(aloneParam.getString())){
                    pointParam.set_id(aloneParamMaps.get(aloneParam.getString()));
                } else {
                    //自增主键从1开始,如果大于10，则使用第一种方案
                    glueAlone_key=glueAlone_key+1;
                    if (glueAlone_key>10){
                        pointParam.set_id(1);
                    }else {

                        aloneParamMaps.put(aloneParam.getString(),glueAlone_key);
                        aloneParam.set_id(glueAlone_key);
                        int rowid=(int) glueAloneDao.insertGlueAlone(aloneParam, taskname);
                        System.out.println("插入数据库数据："+glueAloneDao.getPointGlueAloneParamById(glueAlone_key,taskname).toString());

                        System.out.println("aloneParamMaps.contains(aloneParam.hashCode())::"+aloneParamMaps.containsKey(aloneParam.toString()));
                        pointParam.set_id(glueAlone_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_ALONE);
                point.setPointParam(pointParam);
                points.add(point);

            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_START) {
                // 起始点解析
                lineStartParam = (PointGlueLineStartParam) point.getPointParam();
                ports = new boolean[20];
                for (int i = 0; i < 20; i++) {
                    ports[i] = lineStartParam.getGluePort()[i];
                }
                lineStartParam.setGluePort(ports);
                // 先判断Map里面有没有，有的话，直接添加，无需查询数据库
                pointParam = new PointParam();
                if (lineStartParamMaps.containsKey(lineStartParam.getString())) {
                    pointParam.set_id(lineStartParamMaps.get(lineStartParam.getString()));
                } else {
                    //自增主键从1开始
                    glueLineStart_key=glueLineStart_key+1;
                    if (glueLineStart_key>10){
                        pointParam.set_id(1);

                    }else {

                        lineStartParamMaps.put(lineStartParam.getString(), glueLineStart_key);
                        lineStartParam.set_id(glueLineStart_key);
                        int rowid=(int) glueLineStartDao.insertGlueLineStart(lineStartParam, taskname);
                        pointParam.set_id(glueLineStart_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_LINE_START);
                point.setPointParam(pointParam);
                points.add(point);

            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_MID) {
                // 中间点解析
                lineMidParam = (PointGlueLineMidParam) point.getPointParam();
                ports = new boolean[20];
                for (int i = 0; i < 20; i++) {
                    ports[i] = lineMidParam.getGluePort()[i];
                }
                lineMidParam.setGluePort(ports);
                pointParam = new PointParam();
                if (lineMidParamMaps.containsKey(lineMidParam.toString())) {
                    pointParam.set_id(lineMidParamMaps.get(lineMidParam.toString()));
                } else {
                    //自增主键从1开始
                    glueLineMid_key=glueLineMid_key+1;
                    if (glueLineMid_key>10){
                        pointParam.set_id(1);

                    }else {

                        lineMidParamMaps.put(lineMidParam.toString(), glueLineMid_key);
                        lineMidParam.set_id(glueLineMid_key);
                        int rowid=(int) glueLineMidDao.insertGlueLineMid(lineMidParam, taskname);
                        pointParam.set_id(glueLineMid_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_LINE_MID);
                point.setPointParam(pointParam);

                points.add(point);

            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_ARC) {
                // 圆弧点解析
                points.add(point);
            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_END) {
                // 结束点解析
                lineEndParam = (PointGlueLineEndParam) point.getPointParam();
                pointParam = new PointParam();
                if (lineEndParamMaps.containsKey(lineEndParam.getString())) {
                    pointParam.set_id(lineEndParamMaps.get(lineEndParam.getString()));
                } else {
                    //自增主键从1开始
                    glueLineEnd_key=glueLineEnd_key+1;
                    if (glueLineEnd_key>10){
                        pointParam.set_id(1);

                    }else {

                        lineEndParamMaps.put(lineEndParam.getString(), glueLineEnd_key);
                        lineEndParam.set_id(glueLineEnd_key);
                        int rowid=(int) glueLineEndDao.insertGlueLineEnd(lineEndParam, taskname);
                        pointParam.set_id(glueLineEnd_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_LINE_END);
                point.setPointParam(pointParam);

                points.add(point);

            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_FACE_START) {
                // 面起始点解析
                faceStartParam = (PointGlueFaceStartParam) point.getPointParam();
                ports = new boolean[20];
                for (int i = 0; i < 20; i++) {
                    ports[i] = faceStartParam.getGluePort()[i];
                }
                faceStartParam.setGluePort(ports);
                pointParam = new PointParam();
                if (faceStartParamMaps.containsKey(faceStartParam.getString())) {
                    pointParam.set_id(faceStartParamMaps.get(faceStartParam.getString()));
                } else {
                    //自增主键从1开始
                    glueFaceStart_key=glueFaceStart_key+1;
                    if (glueFaceStart_key>10){
                        pointParam.set_id(1);

                    }else {

                        faceStartParamMaps.put(faceStartParam.getString(), glueFaceStart_key);
                        faceStartParam.set_id(glueFaceStart_key);
                        int rowid=(int) glueFaceStartDao.insertGlueFaceStart(faceStartParam, taskname);
                        pointParam.set_id(glueFaceStart_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_FACE_START);
                point.setPointParam(pointParam);

                points.add(point);

            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_FACE_END) {
                // 面结束点
                faceEndParam = (PointGlueFaceEndParam) point.getPointParam();
                pointParam = new PointParam();
                if (faceEndParamMaps.containsKey(faceEndParam.getString())) {
                    pointParam.set_id(faceEndParamMaps.get(faceEndParam.getString()));
                } else {
                    //自增主键从1开始
                    glueFaceEnd_key=glueFaceEnd_key+1;
                    if (glueFaceEnd_key>10){
                        pointParam.set_id(1);

                    }else {

                        faceEndParamMaps.put(faceEndParam.getString(), glueFaceEnd_key);
                        faceEndParam.set_id(glueFaceEnd_key);
                        int rowid=(int) glueFaceEndDao.insertGlueFaceEnd(faceEndParam, taskname);
                        pointParam.set_id(glueFaceEnd_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_FACE_END);
                point.setPointParam(pointParam);

                points.add(point);
            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_INPUT) {
                // 输入IO
                inputParam = (PointGlueInputIOParam) point.getPointParam();
                pointParam = new PointParam();

                if (inputParamMaps.containsKey(inputParam.getString())) {
                    pointParam.set_id(inputParamMaps.get(inputParam.getString()));
                } else {
                    //自增主键从1开始
                    glueInput_key=glueInput_key+1;
                    if (glueInput_key>10){
                        pointParam.set_id(1);

                    }else {

                        inputParamMaps.put(inputParam.getString(), glueInput_key);
                        inputParam.set_id(glueInput_key);
                        int rowid=(int) glueInputDao.insertGlueInput(inputParam, taskname);
                        pointParam.set_id(glueInput_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_INPUT);
                point.setPointParam(pointParam);

                points.add(point);

            } else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_OUTPUT) {
                // 输出IO
                outputParam = (PointGlueOutputIOParam) point.getPointParam();
                pointParam = new PointParam();

                if (outputParamMaps.containsKey(outputParam.getString())) {
                    pointParam.set_id(outputParamMaps.get(outputParam.getString()));
                } else {
                    //自增主键从1开始
                    glueOutput_key=glueOutput_key+1;
                    if (glueOutput_key>10){
                        pointParam.set_id(1);

                    }else {

                        outputParamMaps.put(outputParam.getString(), glueOutput_key);
                        outputParam.set_id(glueOutput_key);
                        int rowid=(int) glueOutputDao.insertGlueOutput(outputParam, taskname);
                        pointParam.set_id(glueOutput_key);
                    }
                }
                pointParam.setPointType(PointType.POINT_GLUE_OUTPUT);
                point.setPointParam(pointParam);

                points.add(point);

            }
        }
        return points;
    }


}
