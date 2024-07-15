#!/usr/bin/bash

# 使用 `-z` 命令检查变量是否为空
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "Script parameter error: $0"
    exit 2
fi

echo "Bug Fix." > app_update_description.txt
git add .
git commit -m "${2} Release"
git push origin && git push origin --tags
