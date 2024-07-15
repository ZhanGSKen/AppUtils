#!/usr/bin/bash

# 使用 `-z` 命令检查变量是否为空
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "Script parameter error: $0"
    exit 2
fi

cd ${1}
git config --global --add safe.directory "${1}"
echo "Current dir : "`pwd`
versionName=${2}

## 设新的Stage标签
tag="v"${versionName}

## 如果Git已经提交了所有代码就执行标签操作
if [[ -n $(git diff --stat)  ]]
then
  echo 'Source is no commit git completely, tag action cancel.'
  exit 1
else
  echo "Git status is clean."
  if [ "$(git tag -l ${tag})" == "${tag}" ]; then
      echo "Tag ${tag} exist."
      exit 2
  fi
  
  git tag -a ${tag} -F update_description.txt
  echo -e "${0}: Git tag is saved: (${tag} : ${message})"
fi
