# 360CameraDownloader
scripts and tools to download encrypted cloud video of 360 camera 

说明：
这是一些用于下载360云录像的脚本，目前没有GUI界面，只能在安卓的termux环境使用，支持h265编码的云录。（哪位朋友如果有时间的话，不妨集成一下，搞个界面）

准备工作：
1. 准备一台安卓手机
2. 安装termux环境
3. pkg install python2 ecj dx vim

运行：
1. 输入自己的设备信息
    修改make_list.py，填入自己摄像头的sn号、以及360网站的Cookie以便获取云录列表。 (可以在chrome浏览器的debug模式下，打开https://my.jia.360.cn/web/myList登录，然后查看Network标签。比如这个请求：viewImage?imgKey=xxxx，Cookie在 Request Headers, sn号在Query String, 填入到python脚本的interobj['sn'] = "360xxx", 和'Cookie': 'Qu%3Duryyb_axxxxxxxxxxxxxxx')

2. 获取云录像列表 
    python2 make_list.py 20201010

3. 下载云录（视频会保存在/sdcard/yun/）
    bash do.sh run 20201010.txt
    (也可以执行bash do.sh 20201010.txt重新编译程序)

