#!/usr/bin/bash
## 提交新的 APK 编译配置标志信息，并推送到Git仓库。

# 使用 `-z` 命令检查变量是否为空
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "Script parameter error: $0"
    exit 2
fi

# 进入项目根目录
cd ${1}
echo -e "Work dir : \n"`pwd`

git add .
git commit -m "APK Release ${2}"
git push origin && git push origin --tags && git push archives && git push archives --tags
