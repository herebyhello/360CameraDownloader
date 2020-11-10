JNI=$(dirname $0)
cd $JNI
# export PATH=/system/bin:/system/xbin:/data/local/bin:/data/data/com.termux/files/usr/bin/


if [ "$1" = "run" ];
then
	unset LD_PRELOAD
	export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/system/lib/:/system/lib64:$PWD/libs
	/system/bin/dalvikvm32 -cp 360.dex com.qihoo.jia.play.jnibase.DownloadSession $2
	exit
fi

# compile & run
/data/data/com.termux/files/usr/bin/ecj -verbose -d output/  DownloadSession.java JPlayer.java &&
LD_LIBRARY_PATH=/system/lib64 /data/data/com.termux/files/usr/bin/dx --dex --verbose --output 360.dex ./output/  

# run
unset LD_PRELOAD
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/system/lib/:/system/lib64:$PWD/libs
/system/bin/dalvikvm32 -cp 360.dex com.qihoo.jia.play.jnibase.DownloadSession $@

