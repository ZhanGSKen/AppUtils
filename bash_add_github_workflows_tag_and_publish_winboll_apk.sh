#!/usr/bin/bash

## 定义相关函数
## 检查 Git 源码是否完全提交了，完全提交就返回0
function checkGitSources {
    #local input="$1"
    #echo "The string is: $input"
	git config --global --add safe.directory `pwd`
	if [[ -n $(git diff --stat)  ]]
    then
		local result="Source is no commit completely."
	    echo $result
	    # 脚本调试时使用
	    #return 0
	    # 正式检查源码时使用
	    return 1
    fi
    local result="Git Source Check OK."
	echo $result
    return 0
}

function askAddWorkflowsTag {
	read answer
	if [[ $answer =~ ^[Yy]$ ]]; then
	    #echo "You chose yes."
	    # 如果文件中有 libDefaultVersion 这一项，
		# 就读取脚本 bash_setgittag.sh 生成的 libDefaultVersion。
		# 使用grep找到包含"libDefaultVersion="的那一行，然后用awk提取其后的值
		LIB_DEFAULT_VERSION=$(grep -o "libDefaultVersion=.*" ./build_flag.properties | awk -F '=' '{print $2}')
		echo "The bash_setgittag.sh libDefaultVersion is: (${LIB_DEFAULT_VERSION})"
		echo "WinBoll Stage Tag: (v${LIB_DEFAULT_VERSION})";
		## 设新的 workflows 标签
		# 脚本调试时使用
		#tag="v7.6.4-test1-github"
		# 正式设置标签时使用
		tag="v"${LIB_DEFAULT_VERSION}-github
		echo "Adding Github Workflows Tag : ($tag)";
		# 检查是否已经添加了工作流 Tag
		if [ "$(git tag -l ${tag})" == "${tag}" ]; then
	        echo -e "Github Workflows Tag (${tag}) is exist."
	        return 1 # 工作流标签重复
	    fi
	    # 添加工作流标签
    	git tag -a ${tag} -m "Github workflows tag."
	    return 0
	else
	    #echo "You chose no."
	    return 0
	fi
}

## 开始执行脚本
echo -e "Current dir : \n"`pwd`
result=$(checkGitSources)
if [[ $? -eq 0 ]]; then
    echo $result
    # 如果Git已经提交了所有代码就执行标签和应用发布操作
    echo "Add Github Workflows Tag? (yes/no)"
	result=$(askAddWorkflowsTag)
	if [[ $? -eq 0 ]]; then
	    echo $result
        # 发布应用
        bash ~/addbashkey.sh
		echo "Publish WinBoll APK :"
		# 脚本调试时使用
		#bash gradlew assembleBetaDebug
		# 正式发布
	    #bash gradlew assembleStageRelease
	    exit 0
	else
		echo -e "${0}:askAddWorkflowsTag\n${result}\nShell cancel."
		exit 1 # askAddWorkflowsTag 异常
	fi
else
	echo -e "${0}:checkGitSources\n${result}\nShell cancel."
	exit 1 # checkGitSources 异常
fi