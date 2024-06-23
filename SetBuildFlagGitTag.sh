#!/usr/bin/bash

# 使用 `-z` 命令检查变量是否为空
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "Script parameter error: $0"
    exit 2
fi

cd ${1}
git config --global --add safe.directory "${1}"
echo "Current dir : "`pwd`

## 读取properties文件，更新属性
properties_path="SetBuildFlagGitTag.properties"
## 使用grep查找"^<key>="，然后使用awk提取值
versionName=${2}
stageCount=$(grep "^stageCount=" ${properties_path} | awk -F '=' '{print $2}')
buildCount=$(grep "^buildCount=" ${properties_path} | awk -F '=' '{print $2}')

## 设新的Stage标签
tag="v"${versionName}"."${stageCount:0}"."${buildCount:0}

## 如果Git已经提交了所有代码就执行标签操作
if [[ -n $(git diff --stat)  ]]
then
  echo 'Git status is dirty, tag action cancel.'
  exit 1
else
  echo "Git status is clean."
  if [ "$(git tag -l ${tag})" == "${tag}" ]; then
      echo "Tag ${tag} exist."
      exit 2
  fi
  echo "Stage Tag is : ${tag}"
  git tag ${tag}
  echo "Git Is Tag by ${tag}"
fi
