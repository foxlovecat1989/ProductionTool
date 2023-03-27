branchName=$1
commitA=$2
commitB=$3
filename=$4
projectPath=$5

cd "${projectPath}" || exit
echo '切換分支至 <'"$branchName"'>'
git checkout "$branchName"
echo 'commitA: <'"$commitA"'>'
echo 'commitB: <'"$commitB"'>'

# 產出diff文件
git diff-tree -r --no-commit-id --name-status --text --diff-filter=ACDMRT "${commitA}" "${commitB}" > "${filename}"
echo 'Produce Git Change File<'"$filename"'> done!'
