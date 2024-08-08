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
	    return 1
	else
	    #echo "You chose no."
	    return 0
	fi
}

function addWinBollTag {
	# 就读取脚本 .winboll/winboll_app_build.gradle 生成的 publishVersion。
    # 如果文件中有 publishVersion 这一项，
	# 使用grep找到包含"publishVersion="的那一行，然后用awk提取其后的值
	PUBLISH_VERSION=$(grep -o "publishVersion=.*" .winboll/build_flag.properties | awk -F '=' '{print $2}')
	echo "< .winboll/build_flag.properties publishVersion : ${PUBLISH_VERSION} >"
	## 设新的 WinBoll 标签
	# 脚本调试时使用
	#tag="v7.6.4-test1"
	# 正式设置标签时使用
	tag="v"${PUBLISH_VERSION}
	echo "< WinBoll Tag To: $tag >";
	# 检查是否已经添加了 WinBoll Tag
	if [ "$(git tag -l ${tag})" == "${tag}" ]; then
        echo -e "< WinBoll Tag ${tag} exist! >"
        return 1 # WinBoll标签重复
    fi
    # 添加WinBoll标签
	git tag -a ${tag} -F .winboll/app_update_description.txt
    return 0
}

function addWorkflowsTag {
	# 就读取脚本 .winboll/winboll_app_build.gradle 生成的 betaVersion。
    # 如果文件中有 betaVersion 这一项，
	# 使用grep找到包含"betaVersion="的那一行，然后用awk提取其后的值
	BETA_VERSION=$(grep -o "betaVersion=.*" .winboll/build_flag.properties | awk -F '=' '{print $2}')
	echo "< .winboll/build_flag.properties publishVersion : ${BETA_VERSION} >"
	## 设新的 workflows 标签
	# 脚本调试时使用
	#tag="v7.6.4-beta"
	# 正式设置标签时使用
	tag="v"${BETA_VERSION}-beta
	echo "< Workflows Tag To: $tag >";
	# 检查是否已经添加了工作流 Tag
	if [ "$(git tag -l ${tag})" == "${tag}" ]; then
        echo -e "< Github Workflows Tag ${tag} exist! >"
        return 1 # 工作流标签重复
    fi
    # 添加工作流标签
	git tag -a ${tag} -F .winboll/app_update_description.txt
    return 0
}

## 开始执行脚本
echo -e "Current dir : \n"`pwd`
# 检查当前目录是否是项目根目录
if [[ -e .winboll/build_flag.properties ]]; then
    echo "The .winboll/build_flag.properties file exists."
    echo -e "Work dir correctly."
else
    echo "The .winboll/build_flag.properties file does not exist."
    echo "尝试进入根目录"
    # 进入项目根目录
    cd ..
fi
## 本脚本需要在项目根目录下执行
echo -e "Current dir : \n"`pwd`
# 检查当前目录是否是项目根目录
if [[ -e .winboll/build_flag.properties ]]; then
    echo "The .winboll/build_flag.properties file exists."
    echo -e "Work dir correctly."
else
    echo "The .winboll/build_flag.properties file does not exist."
    echo -e "Work dir error."
    exit 1
fi

# 检查源码状态
result=$(checkGitSources)
if [[ $? -eq 0 ]]; then
    echo $result
    # 如果Git已经提交了所有代码就执行标签和应用发布操作

    # 预先询问是否添加工作流标签
    echo "Add Github Workflows Tag? (yes/no)"
	result=$(askAddWorkflowsTag)
	nAskAddWorkflowsTag=$?
	echo $result

    # 发布应用
	echo "Publishing WinBoll APK ..."
	# 脚本调试时使用
	#bash gradlew assembleBetaDebug
	# 正式发布
    bash gradlew assembleStageRelease
    echo "Publishing WinBoll APK OK."
    
    # 添加 WinBoll 标签
    result=$(addWinBollTag)
    echo $result
	if [[ $? -eq 0 ]]; then
	    echo $result
    	# WinBoll 标签添加成功
	else
    	echo -e "${0}: addWinBollTag\n${result}\nAdd WinBoll tag cancel."
        exit 1 # addWinBollTag 异常
	fi
    
    # 添加 GitHub 工作流标签
	if [[ $nAskAddWorkflowsTag -eq 1 ]]; then
	    # 如果用户选择添加工作流标签
    	result=$(addWorkflowsTag)
		if [[ $? -eq 0 ]]; then
		    echo $result
		    # 工作流标签添加成功
		else
			echo -e "${0}: addWorkflowsTag\n${result}\nAdd workflows tag cancel."
			exit 1 # addWorkflowsTag 异常
		fi
	fi
	
	## 清理更新描述文件内容
	echo "" > .winboll/app_update_description.txt
	
	# 设置新版本开发参数配置
	# 提交配置
	git add .
	git commit -m "Start New Stage Version."
	echo "Push sources to git repositories ..."
    # 推送源码到所有仓库
    git push origin && git push origin --tags && git push archives && git push archives --tags && git push github && git push github --tags
else
	echo -e "${0}: checkGitSources\n${result}\nShell cancel."
	exit 1 # checkGitSources 异常
fi
