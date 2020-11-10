package com.qihoo.jia.play.jnibase;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class JPlayer {
    private EventHandler mEventHandler = null;
    //this.mEventHandler.sendMessage(msg);
    //private HandlerThread mHandlerThread = new HandlerThread("JPlayer");
    private long mJPHandle = 0;
    //private IJPlayerEventListener mListener = null;
    public long finished = 0;
    public long percent = 0;
    public long over = 0;
    public long err = 0;
    public String url = "";

    private class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
        }
    }    

     //JPlayer.this.mJPHandle = JPlayer.this.nativeInit();


    public  class JPlayerEvent {
        public int event;
        public int handle;
        public long param1;
        public long param2;
    }

    public void onPlayerEvent(int event, long param1, long param2, int handle) {

              switch (event) {
             case 1300:
                System.out.println("Event.EVENT_DL_OPEN_RESULT "); /*1300*/
                System.out.println(String.valueOf(this.finished) + " " + this.url); /*1300*/
                over = 0;
                err = 0;
                break;
            case 1301:
                System.out.println("Event.EVENT_DL_CLOSE_RESULT"); /*1301*/
                break;
            case 1302:
                //System.out.println("EVENT_DL_PROGRESS");
                //System.out.print(".");
                percent = param2;
                break;
            case 1303://
                System.out.println("Event.EVENT_DL_OVER!");
                //percent = 100;
                over = 1;
                break;
            case 1304:
                System.out.println("Event.EVENT_DL_ERROR");
                err = 1;
                break;

            default:
                return ;
        }

                
    }

    public native long nativeInit();

    public native void nativeRelease(long j);

    public static native void nativeEnableFeature(int i, boolean z);

    public static native void nativeSetDeviceInfo(String str, String str2);

    public static native void nativeSetLogPrint(boolean z, boolean z2, String str);

    public static native void nativeSetVqeParam(int i, int i2);

    public  long init() {
        return nativeInit();
    }

    public static void setLogPrint(boolean enableConsoleLog, boolean enableFileLog, String logFileName) {
        nativeSetLogPrint(enableConsoleLog, enableFileLog, logFileName);
    }

    public static void enableFeature(int feature, boolean enable) {
        nativeEnableFeature(feature, enable);
    }

    public static void setDeviceInfo(String brand, String model) {
        nativeSetDeviceInfo(brand, model);
    }

    public static void setVqeParam(int paramKey, int paramValue) {
        nativeSetVqeParam(paramKey, paramValue);
    }

    public JPlayer() {
    //public JPlayer(IJPlayerEventListener listener) {
        //this.mListener = listener;
        //this.mHandlerThread.start();
        //this.mEventHandler = new EventHandler(this.mHandlerThread.getLooper());
        //this.mEventHandler.sendEmptyMessage(1);

        try {
                System.loadLibrary("jplayer");
            System.out.println("JPlayer loadLibrary");

            } catch (UnsatisfiedLinkError e) {

                e.printStackTrace();
                System.exit(1);
            }


    }

    /*public DownloadSession createDownloadSession() {
        if (this.mJPHandle == 0) {
            return null;
        }
        //DownloadSession ds = new DownloadSession(this.mHandlerThread.getLooper(), this.mJPHandle);
        //return ds;
    }*/

//onPlayerEvent->mEventHandler.sendMessage  /  handleMessage -> handlePlayerEvent
//                if (ds != null && ds.handleNativeEvent(event, arg1, arg2, handle)) {
//                    return;
//                }

 //   static {
//        System.loadLibrary("upixels_pano150_core");
 //       System.loadLibrary("mwVqeDemo");
  //      System.loadLibrary("SnxAEC");
//        System.loadLibrary("jplayer");
 //  }
}
