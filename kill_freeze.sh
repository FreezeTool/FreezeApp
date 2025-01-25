#!/bin/bash
serial=""
if [ -n "$1" ]; then
  serial="-s $1";
fi
# 设备
echo $serial

pid=$(adb $serial shell ps | grep 'FreezeApp' | awk '{print $2}')

adb $serial shell kill -9 $pid