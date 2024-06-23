#!/usr/bin/bash

tag="v${2}"
echo "Now make project tag : ${tag}"
cd ${1}
git config --global --add safe.directory "${1}"
echo "Current dir : "`pwd`

# 如果Git已经提交了所有代码就执行标签操作
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
  git tag ${tag}
  echo "Git source is tag by ${tag}"
fi
