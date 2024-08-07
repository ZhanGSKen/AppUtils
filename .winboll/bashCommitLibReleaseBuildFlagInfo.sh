#!/usr/bin/bash
## 提交新的 Library 编译配置标志信息，并推送到Git仓库。

# 使用 `-z` 命令检查变量是否为空
#if [ -z "$1" ] || [ -z "$2" ]; then
#    echo "Script parameter error: $0"
#    exit 2
#fi

## 开始执行脚本
echo -e "Current dir : \n"`pwd`
# 检查当前目录是否是项目根目录
if [[ -e .winboll/winbollBuildProps.properties ]]; then
    echo "The .winboll/winbollBuildProps.properties file exists."
    echo -e "Work dir correctly."
else
    echo "The .winboll/winbollBuildProps.properties file does not exist."
    echo "尝试进入根目录"
    # 进入项目根目录
    cd ..
fi
## 本脚本需要在项目根目录下执行
echo -e "Current dir : \n"`pwd`
# 检查当前目录是否是项目根目录
if [[ -e .winboll/winbollBuildProps.properties ]]; then
    echo "The .winboll/winbollBuildProps.properties file exists."
    echo -e "Work dir correctly."
else
    echo "The .winboll/winbollBuildProps.properties file does not exist."
    echo -e "Work dir error."
    exit 1
fi

# 就读取脚本 .winboll/winboll_app_build.gradle 生成的 publishVersion。
# 如果文件中有 publishVersion 这一项，
# 使用grep找到包含"publishVersion="的那一行，然后用awk提取其后的值
PUBLISH_VERSION=$(grep -o "publishVersion=.*" .winboll/winbollBuildProps.properties | awk -F '=' '{print $2}')
echo "< .winboll/winbollBuildProps.properties publishVersion : ${PUBLISH_VERSION} >"
## 设新的 WinBoll 标签
# 脚本调试时使用
#tag="v7.6.4-test1"
# 正式设置标签时使用
#tag="v"${PUBLISH_VERSION}

git add .
git commit -m "Library Release ${PUBLISH_VERSION}"
git push origin && git push origin --tags && git push archives && git push archives --tags
