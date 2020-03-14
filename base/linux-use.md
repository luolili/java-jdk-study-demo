##在线linux环境
http://cb.vu/

1.查看当前进程：ps

2.退出：exit

3.看当前路径：pwd

4.清屏：clear

5.退出当前命令：ctr+c

6.执行睡眠：ctr+z 
7.恢复后台：fg

8.ls:列出所有目录和文件，ls -a:所有文件；ls -l :详细信息（字节数，可读可写的权限）

9.建立快捷方式：In -s slink source

10.创建目录：mkdir,touch,vi都可创建目录（只有向一个不存在的文件输出，都会创建文件）

11.复制文件 :cp

12.查看文件：vi 文件名，他可修改文件；cat 文件名，他可显示全部文件内容；less 文件名，可往前翻页；tail 文件名：看文件的末尾部分，可指定行数；head 文件名，与tail 相反。

13.echo halo:向屏幕输出 halo

14./dev/tty:终端

15.移动文件：mv

16.统计文件内容：wc,wc -c （字节数,char），wc -l（行数,line），wc -w（字数：word）

17.grep [^st] filename:ctr+f

18.查看后台任务：job -l

19.查看用过的命令：history

20.看网络是否连通：netstat

21.看ip：ipconfig

22.看环境：env ,看某个 env $HOME

23.显示目录/文件大小：du,df:看文件系统数据

24.打印系统支持的所有命令：compgen -c 

25.打印目录：dirs

26.一页一页的看文件内容：cat dd.txt | more

27.看命令的用法：whatisc:看c的用法

28.man -ls:显示命令的帮助信息

29.top: 任务管理器

30. ps -ef:系统运行状态
---
1.Tab : 补全命令，补全文件名，路径名

2.shift+pgup/pgdown: 向上/下翻页

3.ctrl+u:删除光标左边的命令的字符；ctrl+w:删除光标左边的那个单词（对应ctrl+k），  
ctrl+e:跳到 命令的最后;ctrl+y:复制 ctrl+w/k/u的字符  

3.pwd:print working directory 打印当前目录  
4.which pwd:pwd 这个可执行程序的文件位置  
5.cd: change directory  
6.cp file one/ : one 必须是和file 同级别的文件夹  
7.cp -r one one_copy:复制 文件夹  
8.mv file_test.txt one:把文件移动到 one 目录下；mv *.txt one  
9.mv file renamed_file:重命名文件  
10.rm -i file1,file2:删除的时候提示是否删除，i表示 inform，yes/no  
11. rm -f file:强制删除  
12.rm -r one: r:recursive 递归的删除one文件夹和他下面的文件夹/文件  
13.rm -rf /*:最危险的命令，删除系统所有文件（整个系统）  
14.ln:link 链接/快捷方式
>linux 在硬盘上存储的时候，可分为文件名+文件内容+权限；文件名和内容是分开存的，每个文件的内容都有一个编码，inode.硬链接：使链接的两个文件共享同样的文件内容，也就是同样的 inode。只能创建指向文件，不可指向目录

15.find -name "*.txt" -atime -2:2天之内的txt文件  
16.grep:搜索，grep -in i index.html:在index.html 里面搜索字幕 i,忽略大小写，显示行号 
17.grep -v i index.html :只显示没有i的行  
18.grep -r folder/:folder 必须是当前目录下面的目录，当你不知道要搜索的文本存在哪个文件的时候用  

19.sort name.txt:对里面的名字排序；sort -o sorted_name.txt name.txt把排序后的内容输出到sorted_name.txt  

## 上线流程
1.准备 linux 服务器，web 服务器（如nginx）

2.远程连接 linux :  
ssh root@linux的ip
 
 centos默认创建的文件夹：  
 bin（二进制文件）,boot（linux启动的文件）  
 dev（设备device）,etc（系统的配置文件）  
 home（用户的私人目录）,lib（被程序所调用的库文件）  
 lib64,,  
 lost+found,media（访问外设的文件）,mnt（mount,挂载）  
 opt（可选的应用软件）,proc,root,run,sbin（系统的可执行程序）,srv（服务）,sys,tmp,usr（软件资源，类似win program file）,var（可变的，程序的数据，如 log文件）
3.安装 nginx:  
先查看是否安装了 nginx: nginx -V  
bash:/usr/sbin/nginx:no such file or directory 表示没有安装 nginx  

yum install nginx: no package nginx available  表示 nginx 不在 centos 官方的软件源  
用 yum install epel-release  install 1 package,total download size:22 

然后 yum install nginx  

4.修改web服务器配置：  
配置文件默认在 /etc/nginx  
ls 查看是否 有 nginx.conf,没有就用 nginx -t 查看他的位置  

yum install vim : 安装 vim  
vim nginx.conf  

把 user nginx 改为 user root  
http-server-location  

shift+: 输入 set nu:显示行号  
进入编辑模式 按 i,wq : 保存退出  

5.上传网站到服务器  
先 yum install openssh-client  
logout 退出服务器（同时可以看 网站 访问的ip）
scp -r local_dir @ip:/remote_dir   
scp -r  ./demo/* @ip:/root/www (在项目文件哪里打开gitbash)  

ps -ef | grep nginx （看nginx是否启动）  
nginx -s stop （停止 nginx）  
nginx （启动nginx）  
nginx -s reload （重新加载）
cat /etc/nginx/nginx.conf （查看文件）  

通过公网 ip 可以访问 demo了。  
##软件安装
对于 ubuntu ,软件叫做软件包：package,后缀 .deb,对于redhat:。rpm.  
软件包 放在 软件仓库