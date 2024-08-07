#!/usr/bin/bash

echo "Current dir : "`pwd`
echo "Do you want to make a github workflows tag? (yes/no)"
read answer

if [[ $answer =~ ^[Yy]$ ]]; then
    #echo "You chose yes."
    echo "Start to make a github workflows tag."
else
    #echo "You chose no."
    exit 0
fi

git config --global --add safe.directory `pwd`

# 如果文件中有 libDefaultVersion 这一项，
# 就读取脚本 bash_setgittag.sh 生成的 libDefaultVersion。
# 使用grep找到包含"libDefaultVersion="的那一行，然后用awk提取其后的值
LIB_DEFAULT_VERSION=$(grep -o "libDefaultVersion=.*" ./build_flag.properties | awk -F '=' '{print $2}')
echo "The bash_setgittag.sh libDefaultVersion is: (${LIB_DEFAULT_VERSION})"
echo "WinBoll Stage Tag: (v${LIB_DEFAULT_VERSION})";

## 设新的 workflows 标签
tag="v"${LIB_DEFAULT_VERSION}-github
echo "Adding workflows tag : ($tag)";

## 如果Git已经提交了所有代码就执行标签操作
if [[ -n $(git diff --stat)  ]]
then
  echo 'Source is no commit git completely, tag action cancel.'
  exit 1
else
  echo "Git status is clean."
  if [ "$(git tag -l ${tag})" == "${tag}" ]; then
      echo "Github workflows tag (${tag}) exist."
      exit 2
  fi
  
  git tag -a ${tag} -m "Github workflows tag."
  echo -e "${0}: Github workflows tag is saved: (${tag})"
  echo "Update git repositories :"
  git push origin && git push origin --tags && git push archives && git push archives --tags && git push github && git push github --tags
fi
