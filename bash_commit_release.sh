#!/usr/bin/bash

# 使用 `-z` 命令检查变量是否为空
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "Script parameter error: $0"
    exit 2
fi

## 进入项目根目录
cd ${1}

## 清理更新描述文件内容
echo "" > app_update_description.txt

## 提交新的标志设置，并推送到Git仓库。
git add .
git commit -m "${2} Release"
git push origin && git push origin --tags && git push archives && git push archives --tags
