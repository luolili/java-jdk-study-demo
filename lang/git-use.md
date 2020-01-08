1.查看 远程的 主机和网址：git remote；
git remote -v

2.设置/修改主机的网址：git remote rename old new（不加origin）

3.permission denied(publickey):在git bash 里用邮箱生成key:

然后输入：
ssh -T git@github.com

得到：
Hi xx(github的用户名)! You've successfully authenticated, but GitHub does not provide shell access.

4.git push 与 git push -u 区别？

>git push -u origin master 上面命令将本地的master分支推送到origin主机，同时指定origin为默认主机，后面就可以不加任何参数使用git push了

No tracked branch configured for branch

git branch --set-upstream luoli origin/luoli

git branch --set-upstream-to origin/luoli luoli

5.在提交到远程分支前先pull:
git pull origin luoli:luoli

出现：
warning:no common commits,
remote:enumerating objects:27 done--

6.删除远程分支：git push origin --delete branch-name