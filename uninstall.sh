#!/bin/bash
serial=""
if [ -n "$1" ]; then
  serial="-s $1";
fi
# 设备
echo $serial
adb $serial uninstall com.john.freezeapp