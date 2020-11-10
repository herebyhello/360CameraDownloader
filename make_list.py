# encoding: utf-8

import json
import urllib2
import sqlite3
import time
import os
import sys
import ssl

reload(sys)
sys.setdefaultencoding("utf-8")
ssl._create_default_https_context = ssl._create_unverified_context

#####################################################################
# this is global data
# all you need to do is paste your sn number and browser coookie here
#####################################################################
url = 'https://q2.jia.360.cn/cloud/getEventList?lang=zh_CN'
body = {"from": "mpc_ipcam_and",
        "parad": "{\"sn\":\"36000000000\",\"sday\":\"20180514\",\"eday\":\"20180514\",\"page\":0,\"count\":50,\"sort\":0,\"channel_id\":\"ch_txyyb\",\"taskid\":\"fe61f8b0-3525-4b5e-b8d4-5faa7ddb9b95\"}"}

interobj = json.loads(body['parad'])
interobj['sn'] = "3601607000000" # [please input your sn number]

headers = {'content-type': 'application/json',
           'User-Agent': 'okhttp/3.3.0',
           'Cookie': 'Q=u%3xxxxxxxxx0; xxxxxxxxxxxxxxxxxxxxxxxxxx;jia_web_sid=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx; monitor_count=2'
            # [please paste your cookie, you can find it from https://my.jia.360.cn/web/myList]
           }

dataset = []
num = 1
total = 1
page = 0
stime = "20180514"

#####################################################################
# check param
#####################################################################
if len(sys.argv) < 2:
    print "usage:%s 20180514" % (sys.argv[0])
    sys.exit(1)

if len(sys.argv[1]) != len(stime):
    sys.exit(1)
else:
    stime = sys.argv[1]
    interobj['sday'] = stime
    interobj['eday'] = stime
    body['parad'] = json.dumps(interobj)

    skip = 0
    #skip = int(sys.argv[2])


###############################################################################33
# start fetch cloud video info
###############################################################################33
def got50page(url, headers, data):
    request = urllib2.Request(url=url, headers=headers, data=json.dumps(body))
    res = urllib2.urlopen(request, timeout=5)
    result = res.read()
    # print result
    global total
    global num
    global dataset
    obj = json.loads(result)
    array = obj["data"]
    dataset.append(array)
    totalnow = int(obj["total"])

    if total == 1 and totalnow > total:
        total = totalnow

    for info in array:
        print '%d %s %s %s %s %s %s %s %s %s %d %d %d %s %s %d' % (num, info["sn"],
                                                                   info["eventId"], info["eventCover"],
                                                                   info["eventTime"], info["eventDuration"],
                                                                   info["eventHls"], info["size"], info["playKey"],
                                                                   "path", 0, 0, 0, "1526358829668", "1526358829668", 0)
        num = num + 1


###############################################################################33
# start fetch cloud video info
###############################################################################33
while num <= total and page <= 15:
    print 'processing %d page' % (page)
    interobj = json.loads(body['parad'])
    interobj['page'] = page
    body['parad'] = json.dumps(interobj)
    got50page(url, headers, data=body)
    page = page + 1

print 'total file: %d, actual: %d,  page: %d' % (total, num - 1, page)
go = raw_input("are you sure to continue?")
if go != "y" and go != "Y":
    sys.exit(0)


###############################################################################33
# print hls address and decrypt key to a txt
###############################################################################33
log = '%s.txt' % (sys.argv[1])#(time.strftime("%Y-%m-%d", time.localtime()))
fd = open(log, 'w') 

index = 1
for data in dataset:
    for info in data:
        create_time = int(time.time()*1000)
        event_time = int(info["eventTime"])
        event_time = event_time/1000
        #name = info["eventId"][-10:]
        name = time.localtime(event_time)
        name = time.strftime("%Y-%m-%d_%H%M%S_cloud_video.mp4", name)

        line = '%s %s %s' % (info["eventHls"], name, info["playKey"])
        if (index > skip):
            #print line
            print >> fd,line
            index = index + 1

