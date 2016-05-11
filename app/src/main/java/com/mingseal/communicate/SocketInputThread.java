package com.mingseal.communicate;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.CmdParam;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 客户端读消息线程
 *
 * @author way
 */
public class SocketInputThread extends Thread {

    public static int SocketInputWhat = 0x123;
    public static int SocketError = 100;
    public static int SocketInputUPLOADWhat = 0x1024;
    private boolean isStart = true;
    private Handler handler;

    // private BufferedInputStream bis;
    private int dataLength = 0;
    private final static String TAG = "SocketInputThread";

    public SocketInputThread() {
    }

    /**
     * <p/>
     * Title: setSocketInputThreadHandler
     * <p/>
     * Description: 设置当前Activity所属的Handler
     *
     * @param handler
     */
    public void setSocketInputThreadHandler(Handler handler) {
        this.handler = handler;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    @Override
    public void run() {
        while (isStart) {
            // 手机能联网，读socket数据
            if (NetManager.instance().isNetworkConnected()) {

                if (!TCPClient.instance().isConnect()) {
//					Log.e(TAG, "TCPClient connet server is fail read thread sleep second" + Const.SOCKET_SLEEP_SECOND);
//
//					try {
//						sleep(Const.SOCKET_SLEEP_SECOND * 1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
                    //发送消息给activity，连接中断，提示用户wifi中断，停止线程，释放单列
//					Message msg = new Message();
//					msg.what = SocketError;
//					handler.sendMessage(msg);
                }

                readSocket();

                // 如果连接服务器失败,服务器连接失败，sleep固定的时间，能联网，就不需要sleep

                System.out.println("TCPClient.instance().isConnect() " + TCPClient.instance().isConnect());

            }
        }
    }

    public void readSocket() {
        Selector selector = TCPClient.instance().getSelector();
        if (selector == null) {
            return;
        }
        ByteBuffer buffer = null;
        try {
            // 如果没有数据过来，一直阻塞
            while (selector.select() > 0) {
                for (SelectionKey sk : selector.selectedKeys()) {
                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isReadable()) {
                        // 使用NIO读取Channel中的数据
                        SocketChannel sc = (SocketChannel) sk.channel();
                        dataLength = sc.socket().getInputStream().available();
                        System.out.println("可读数据长度：" + dataLength);

                        Log.d(TAG, "MessageMgr.INSTANCE.cmdDelayFlag:" + MessageMgr.INSTANCE.cmdDelayFlag);
                        if (dataLength == 0) {
                            sk.cancel();
                            //发送消息给activity，连接中断，提示用户wifi中断，停止线程，释放单列
					        Message msg = new Message();
					        msg.what = SocketError;
					        handler.sendMessage(msg);
                            System.out.println("cancel key for < 0");
                            break;
                        }
                        if (MessageMgr.INSTANCE.cmdDelayFlag == CmdParam.Cmd_Device) {
                            if (dataLength != 79) {
                                buffer = ByteBuffer.allocate(79);
                            } else {
                                buffer = ByteBuffer.allocate(79);
                                sc.read(buffer);
                                // Log.d("SocketInputThread", "" + buffer);
                                Message msg = new Message();
                                msg.what = SocketInputWhat;
                                msg.obj = buffer;
                                msg.arg1 = dataLength;
                                handler.sendMessage(msg);
                            }
                        } else if (MessageMgr.INSTANCE.cmdDelayFlag == CmdParam.Cmd_UpLoad) {

                            if (dataLength == 8) {
                                buffer = ByteBuffer.allocate(dataLength);
                                sc.read(buffer);
                                Log.d(TAG, "bb:" + buffer);
                                Message msg = new Message();
                                msg.what = SocketInputWhat;
                                msg.obj = buffer;
                                msg.arg1 = dataLength;
                                handler.sendMessage(msg);
                            } else if (dataLength != MessageMgr.INSTANCE.upLoadLen + 10) {
                                buffer = ByteBuffer.allocate(MessageMgr.INSTANCE.upLoadLen + 10);
//								 Log.d(TAG, "ccc");
                            } else {
                                buffer = ByteBuffer.allocate(MessageMgr.INSTANCE.upLoadLen + 10);
                                sc.read(buffer);
                                Message msg = new Message();
                                msg.what = SocketInputUPLOADWhat;
                                msg.obj = buffer;
                                msg.arg1 = dataLength;
                                handler.sendMessage(msg);
                            }
                        } else {
                            buffer = ByteBuffer.allocate(dataLength);
                            sc.read(buffer);
                            Log.d(TAG, "dd:" + buffer);
                            Message msg = new Message();
                            msg.what = SocketInputWhat;
                            msg.obj = buffer;
                            msg.arg1 = dataLength;
                            // 上传这里必须要有
                            if (MessageMgr.INSTANCE.cmdDelayFlag == CmdParam.Cmd_PreUpLoad) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendMessage(msg);
                        }
                        buffer.flip();
                        buffer.clear();
                        buffer = null;

                        try {
                            //为下次读取做准备
                            sk.interestOps(SelectionKey.OP_READ);
                            //删除正在处理的selectedKeys
                            selector.selectedKeys().remove(sk);

                        } catch (CancelledKeyException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException e1) {
            e1.printStackTrace();

        } catch (ClosedSelectorException e2) {
        }
    }

}
