package com.mingseal.utils;

import com.mingseal.application.UserApplication;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 * <p>
 * Description: 拐点自适应及前瞻
 */

public class LookAhead {
    public int NBR = 200;//对200个微线段进行前瞻
    public float len[];//保存微线段长度的数组
    public float angle[];//保存角度的数组
    public float xlen[];//投影到x轴的距离
    public float ylen[];//投影到y轴的距离
    public float zlen[];//投影到z轴的距离
    public float fV_Vec[];//微线段链拐点自适应降速的速度参数
    public float fV_Lmt[];//经自适应前瞻后的速度参数
    public int __Vg;//拐点降速参考速度（也是拐点固定降速速度）
    public float __Vs; //起始运行速度
    public float __Ve; //结束运行速度
    public float __Vm;//非拐点的运行速度(中间点的轨迹速度)
    public int __V[];//所有点的轨迹速度
    public float __Am;//加速度
    public List<PointGlueLineMidParam> lineMidParams=new ArrayList<>();
    public List<PointGlueLineStartParam> lineStartParams=new ArrayList<>();
    public LookAhead(int vg, int am) {
        this.__Vg = vg;
        this.__Am = am;
    }

    /**
     * 预前瞻准备工作 筛选出起始点中间点 结束点
     * @param points
     * @param userApplication
     */
    public void PreLookAhead(List<Point> points,UserApplication userApplication){
        List<Point> pointList_LookAhead=new ArrayList<>();
        for (Point point:points){
            if (point.getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_START)){//检测到起始点
                    pointList_LookAhead.add(point);
            }
            if (point.getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_MID)){//找出中间点
                pointList_LookAhead.add(point);
            }
            if (point.getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_END)){
                pointList_LookAhead.add(point);
                //计算微线段长度
                CalcLineSegmentLength(pointList_LookAhead,userApplication);
                pointList_LookAhead.clear();
                pointList_LookAhead=new ArrayList<>();
            }
        }
        System.out.println("前瞻后时间："+ DateUtil.getCurrentTime());
    }

    /**
     * 计算微线段长度,检测中间点是否重合，如果重合去掉后一个点
     *
     * @param pointlist
     */
    public void CalcLineSegmentLength(List<Point> pointlist, UserApplication userApplication) {
        xlen = new float[pointlist.size()];
        ylen = new float[pointlist.size()];
        zlen = new float[pointlist.size()];
        len = new float[pointlist.size() - 1];
        angle = new float[pointlist.size() - 2];
        fV_Vec = new float[pointlist.size() - 2];
        fV_Lmt = new float[pointlist.size() - 1];
        __V = new int[pointlist.size()];
        for (int j = 0; j < pointlist.size() - 1; j++) {//遍历算出投影到x轴 y轴的线段长度
            xlen[j] = pointlist.get(j + 1).getX() - pointlist.get(j).getX();
            ylen[j] = pointlist.get(j + 1).getY() - pointlist.get(j).getY();
            zlen[j] = pointlist.get(j + 1).getZ() - pointlist.get(j).getZ();

        }
        for (int i = 0; i < pointlist.size() - 1; i++) {
            len[i] = (float) Math.sqrt(Math.pow(xlen[i], 2)
                    + Math.pow(ylen[i], 2)+Math.pow(zlen[i],2));
            System.out.println("保存的微线段长度：" + len[i]);
        }
        System.out.println("加速度为："+__Am);
        System.out.println("拐点固定速度："+__Vg);
        CalcLength_Angle(xlen, ylen,zlen, len, pointlist, userApplication);
    }

    /**
     * 计算角度
     *
     * @param xlen
     * @param ylen
     * @param zlen
     * @param len
     * @param pointlist
     * @param userApplication
     */
    private void CalcLength_Angle(float[] zlen, float[] xlen, float[] ylen, float[] len, List<Point> pointlist, final UserApplication userApplication) {
        for (int i = 0; i < pointlist.size() - 2; i++) {

            if ((xlen[i] * xlen[i + 1] + ylen[i] * ylen[i + 1]+zlen[i]*zlen[i+1]) / (len[i] * len[i + 1])>1){
                angle[i]= (float) Math.acos(1);
            }else if ((xlen[i] * xlen[i + 1] + ylen[i] * ylen[i + 1]+zlen[i]*zlen[i+1]) / (len[i] * len[i + 1])<-1){
                angle[i]= (float) Math.acos(-1);
            }else {
                angle[i] = (float) Math.acos((xlen[i] * xlen[i + 1] + ylen[i] * ylen[i + 1]+zlen[i]*zlen[i+1]) / (len[i] * len[i + 1]));
            }
//            System.out.println("保存的角度：" + angle[i]);
        }

        SpeedProfile(pointlist, userApplication);
    }

    /**
     * 进行拐点自适应以及速度前瞻
     *
     * @param pointlist
     * @param userApplication
     */
    private void SpeedProfile(List<Point> pointlist, UserApplication userApplication) {
        //先考虑第一段起始点-中间点 的速度前瞻
        //取出点的轨迹速度
        for (int i = 0; i < pointlist.size(); i++) {
            if (pointlist.get(i).getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_START)) {//如果是起始点
                PointGlueLineStartParam pParam = userApplication.getLineStartParamMaps().get(pointlist.get(i).getPointParam().get_id());
                __V[i] = pParam.getMoveSpeed();//得出起始点轨迹速度
//                System.out.println("起始点初始轨迹速度：" + __V[i]);

            }
            if (pointlist.get(i).getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_MID)) {
                PointGlueLineMidParam pParam = userApplication.getLineMidParamMaps().get(pointlist.get(i).getPointParam().get_id());
                __V[i] = pParam.getMoveSpeed();
//                System.out.println("中间点轨迹速度：" + __V[i]);
            }//如果是中间点
            if (pointlist.get(i).getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_END)) {
                __V[i] = 0;
            }//如果是结束点
        }
        if (pointlist.size() > 2) {//3个点及以上

            for (int i = 0; i < pointlist.size() - 2; i++) {//拐点自适应
                fV_Vec[i] = (float) Math.pow(2 * Math.sin(angle[i] / 2), -1) * __Vg;
                if (fV_Vec[i] > __V[i + 1]) {//与中间点轨迹速度比较
                    fV_Vec[i] = __V[i + 1];
                }
//                System.out.println("拐点自适应速度：" + fV_Vec[i]);
            }
            for (int i = 0; i < pointlist.size() - 2; i++) {
                fV_Lmt[i] = fV_Vec[i];
            }
            fV_Lmt[pointlist.size()-2]=0;//结束点速度
        }
        //对起始点进行速度规划
        if (fV_Vec[0]<__V[0]){//中间点速度小于起始点速度 速度变化为 加速-起始点-减速到中间点
            int temp_v = (int) Math.sqrt(Math.pow(fV_Vec[0], 2) / 2 + __Am * len[0]);
            if (fV_Vec[0]<temp_v&&temp_v < __V[0]) {//速度达不到起始点速度就要减速且大于中间点速度，那么起始点的速度需要尽可能大
                __V[0] = temp_v;
//                System.out.println("起始点速度大于中间点速度但达不到起始点初始速度修改后轨迹速度：" + __V[0]);
            }else if (temp_v>=__V[0]&&fV_Vec[0]<temp_v){//速度有能力超过设置的起始点速度且大于中间点速度
                //使用原速度
            }else if (fV_Vec[0]>temp_v){//中间点速度大于起始速度
                __V[0]= (int) Math.sqrt(2*__Am*len[0]);
//                System.out.println("起始点速度无法达但是大于中间点速度：" + __V[0]);
            }
        }else {//中间点速度大于起始点速度 匀加速看是否能达到
            if (fV_Lmt[0]>Math.sqrt(2*__Am*len[0])){//达不到
                fV_Lmt[0]= (float) Math.sqrt(2*__Am*len[0]);
                if (fV_Lmt[0]<__V[0]){//计算出来的中间点速度小于起始点速度
                    __V[0] = (int) fV_Lmt[0];
//                    System.out.println("起始点速度无法达到且小于中间点速度："+__V[0]);
                }
            }
        }
        PointGlueLineStartParam pointGlueLineStartParam = userApplication.getLineStartParamMaps().get(pointlist.get(0).getPointParam().get_id());

        // 存放线起始点的参数方案
        PointGlueLineStartParam lineStartParam=new PointGlueLineStartParam();
        lineStartParam.setGluePort(pointGlueLineStartParam.getGluePort());
        lineStartParam.setBreakGlueLen(pointGlueLineStartParam.getBreakGlueLen());
        lineStartParam.setDrawDistance(pointGlueLineStartParam.getDrawDistance());
        lineStartParam.setDrawSpeed(pointGlueLineStartParam.getDrawSpeed());
        lineStartParam.setOutGlueTime(pointGlueLineStartParam.getOutGlueTime());
        lineStartParam.setOutGlueTimePrev(pointGlueLineStartParam.getOutGlueTimePrev());
        lineStartParam.setMoveSpeed(__V[0]);
        lineStartParams.add(lineStartParam);
        userApplication.setLineStartParams(lineStartParams);
//        System.out.println("保存的起始点list："+lineStartParams.toString());


        if (pointlist.size() > NBR + 1) {//如果微线段个数大于指定值200

            for (int i = 0; i < pointlist.size() - NBR; i++) {//计算缓存区前瞻次数为点的个数-1-制定微线段值+1
                for (int idx = i; idx < i + NBR - 1; idx++) {//前瞻，
                    if (Math.abs(Math.pow(fV_Lmt[idx + 1], 2) - Math.pow(fV_Lmt[idx], 2)) > 2 * __Am * len[idx+1]) {//判断还需要加速或减速
                        if (fV_Lmt[idx + 1] < fV_Lmt[idx]) {//需要减速
                            fV_Lmt[idx] = (float) Math.sqrt(Math.pow(fV_Lmt[idx + 1], 2) + 2 * __Am * len[idx+1]);//计算出最大的起始速度
                            for (int subidx = idx; subidx > 1 + i; subidx--) {//回溯看速度是否符合条件
                                if (Math.abs(Math.pow(fV_Lmt[subidx], 2) - Math.pow(fV_Lmt[subidx - 1], 2)) > 2 * __Am * len[subidx ]) {//判断还需要加速或减速
                                    if (fV_Lmt[subidx] > fV_Lmt[subidx - 1]) {//当前点速度大于前置位点的速度，需要修改前置点的速度，加速(尽自己能力加速算出前置点最大速度)
                                        fV_Lmt[subidx - 1] = (float) Math.sqrt(Math.pow(fV_Lmt[subidx], 2) - 2 * __Am * len[subidx ]);
                                    } else {
                                        fV_Lmt[subidx - 1] = (float) Math.sqrt(Math.pow(fV_Lmt[subidx], 2) + 2 * __Am * len[subidx ]);//尽自己能力减速，算出前置点的最大速度
                                    }

                                } else {
                                    break;
                                }
                            }
                        } else {//加速
                            fV_Lmt[idx + 1] = (float) Math.sqrt(Math.pow(fV_Lmt[idx], 2) + 2 * __Am * len[idx+1]);
                        }
                    }
                }
            }
        } else if (pointlist.size() > 2) {
            for (int idx = 0; idx < pointlist.size() - 2; idx++) {//
                if (Math.abs(Math.pow(fV_Lmt[idx + 1], 2) - Math.pow(fV_Lmt[idx], 2)) > 2 * __Am * len[idx+1]) {
//                    System.out.println("进入前瞻");
                    if (fV_Lmt[idx + 1] < fV_Lmt[idx]) {
                        fV_Lmt[idx] = (float) Math.sqrt(Math.pow(fV_Lmt[idx + 1], 2) + 2 * __Am * len[idx+1]);

                        for (int subidx = idx; subidx > 1; subidx--) {
                            if (Math.abs(Math.pow(fV_Lmt[subidx], 2) - Math.pow(fV_Lmt[subidx - 1], 2)) > 2 * __Am * len[subidx ]) {
                                if (fV_Lmt[subidx] > fV_Lmt[subidx - 1]) {
                                    fV_Lmt[subidx - 1] = (float) Math.sqrt(Math.pow(fV_Lmt[subidx], 2) - 2 * __Am * len[subidx ]);
                                } else {
                                    fV_Lmt[subidx - 1] = (float) Math.sqrt(Math.pow(fV_Lmt[subidx], 2) + 2 * __Am * len[subidx ]);
                                }
                            } else {
                                break;
                            }
                        }

                    } else {
                        fV_Lmt[idx + 1] = (float) Math.sqrt(Math.pow(fV_Lmt[idx], 2) + 2 * __Am * len[idx+1]);
                    }
                }
            }
        }
        //速度规划完成 将规划完的速度重新分配给相应中间点
        for (int i = 0, j = 0; i < pointlist.size(); i++) {
            if (pointlist.get(i).getPointParam().getPointType().equals(PointType.POINT_GLUE_LINE_MID)) {
                PointGlueLineMidParam pParam = userApplication.getLineMidParamMaps().get(pointlist.get(i).getPointParam().get_id());//用方案参数取出原本的参数进行拷贝赋值
//                System.out.println("前瞻速度：" + fV_Lmt[j]);
                PointGlueLineMidParam pointGlueLineMidParam=new PointGlueLineMidParam();//生成新的中间点
                pointGlueLineMidParam.setGluePort(pParam.getGluePort());
                pointGlueLineMidParam.setRadius(pParam.getRadius());
                pointGlueLineMidParam.setStopGLueDisNext(pParam.getStopGLueDisNext());
                pointGlueLineMidParam.setStopGlueDisPrev(pParam.getStopGlueDisPrev());
                pointGlueLineMidParam.setMoveSpeed((int) fV_Lmt[j]);
                j++;
                // 存放线中间点的参数方案
                lineMidParams.add(pointGlueLineMidParam);
//                System.out.println("保存的中间点list："+lineMidParams.toString());
            }
        }
        userApplication.setLineMidParams(lineMidParams);

    }

}
