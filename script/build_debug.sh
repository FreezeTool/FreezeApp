#!/bin/bash
serial=""
if [ -n "$1" ]; then
  serial="-s $1";
fi
# 设备
echo $serial
# 编译
./gradlew :app:assembleDebug
# 安装
adb $serial install -r app/build/outputs/apk/debug/app-debug.apk
# 阻塞0.3s
sleep 0.3
#运行
adb $serial shell am start -n com.john.freezeapp/.main.MainActivity