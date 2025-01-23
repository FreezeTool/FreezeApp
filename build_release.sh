!/bin/bash
# 编译
./gradlew :app:assembleRelease
# 安装
adb install -r app/build/outputs/apk/release/app-release.apk
# 阻塞0.3s
sleep 0.3
#运行
adb shell am start -n com.john.freezeapp/.MainActivity