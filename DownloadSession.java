//package com.test;
//import com.qihoo.jia.play.jnibase.DownloadSession;
package com.qihoo.jia.play.jnibase;

import java.io.File;
import com.qihoo.jia.play.jnibase.JPlayer;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class DownloadSession {

    public native void bar(String s, Object o);
	public native long nativeOpen(long j, String str, int s, int end, String str2, String str3);
    public native int nativeClose(long j);
    public native int nativePause(long j, boolean z);

        int i;
        public static void func() {
            System.out.println("func\n");
        }

        public static void main(String[] args) {
            //**************load library**************
            try {
                System.loadLibrary("jplayer");
            	System.out.println("usage: xxx 20180514.txt");

            } catch (UnsatisfiedLinkError e) {

                e.printStackTrace();
                System.exit(1);
            }
        
            //**************new player**************
            JPlayer player = new JPlayer();
            long mJPHandle = player.init();
            player.setDeviceInfo("360camer", "360camer");
            player.setLogPrint(true, false, "log.txt");

            long mDlHandle = -1;
            long i;
            

            String key = "";

            String line;
            String url;
            String file;
            int num = 0;
            String path = "/sdcard/yun/";
            try {

               //**********read file **********
                FileInputStream fs = new FileInputStream(args[0]);
                InputStreamReader sr = new InputStreamReader(fs, "UTF-8");
                BufferedReader br = new BufferedReader(sr);

                //***********start download***********
                while ((line = br.readLine()) != null) {

                    String info[] = line.split("\\s+");

                    url = info[0];
                    file = path.concat(info[1]);
                    key = info[2];
                    player.url = line;

                    player.over = 0;

                    while (true)
                    {
                        boolean good = true;
	                    DownloadSession dl = new DownloadSession();
                        do {
                            mDlHandle = dl.nativeOpen(mJPHandle, url, 0, 0, key, file);
                            Thread.sleep(5000);
                        } while (new File(file).exists() == false);

                        long size1 = new File(file).length();
                        long start = System.currentTimeMillis()/1000;
                        for( i = 0; player.over == 0; ) {
                        
                               if (player.percent != i){
                                    i = player.percent;
                                    System.out.print(i);
                               }    
                               Thread.sleep(50);
                               //checkpoint
                               if (System.currentTimeMillis()/1000 - start > 30){
                                    if (new File(file).length() == size1){
                                        good = false;
                                        System.out.print(file + " sz not change in 30s");
                                        break;
                                    }
                                    start = System.currentTimeMillis()/1000;
                                    size1 = new File(file).length();
                               }
                        }//for
                        System.out.println(":)");

                        if (good) {
                            player.finished = num + 2;
                            System.out.println(num++);
                            dl.nativeClose(mDlHandle);
                            break;
                        } else {
                            dl.nativeClose(mDlHandle);
                            continue;
                        }


                     }//while good

                }//while

                br.close();
                sr.close();
                fs.close();

                //dl.nativePause(mDlHandle, true);
                //dl.nativeClose(mDlHandle);
             } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
             }

    

           try {
               Thread.sleep(2000);
           } catch (InterruptedException e){
               System.exit(1);
           }


            System.out.println(mDlHandle);
            System.out.println("java done");
        }

    } 
