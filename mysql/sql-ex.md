##分数排名
1.编写一个 SQL 查询来实现分数排名。如果两个分数相同，则两个分数排名（Rank）相同。请注意，平分后的下一个名次应该是下一个连续的整数值。换句话说，名次之间不应该有“间隔”。
  
select s1.Score,count(distinct(s2.score)) Rank
from
Scores s1,Scores s2
where
s1.score<=s2.score
group by s1.Id
order by Rank

##有趣的电影
需要编写一个 SQL查询，找出所有影片描述为非 boring (不无聊) 的并且 id 为奇数 的影片，结果请按等级 rating 排列  
select * from cinema  where description!='boring' and mod(id,2)=1 order by rating desc
##交换工资
给定一个 salary 表，如下所示，有 m = 男性 和 f = 女性 的值。交换所有的 f 和 m 值（例如，将所有 f 值更改为 m，反之亦然）。要求只使用一个更新（Update）语句，并且没有中间的临时表。

注意，您必只能写一个 Update 语句，请不要编写任何 Select 语句  
 update salary
 set
 sex = case sex
 when 'm' then 'f'
 else 'm'
 end;
 
##员工工资比他的经理多

select a.Name as Employee  from Employee as a,Employee as b where a.ManagerId = b.id and a.Salary>b.Salary
##连续出现的数字
编写一个 SQL 查询，查找所有至少连续出现三次的数字。

select distinct l1.Num AS ConsecutiveNums  
 from Logs l1,Logs l2,Logs l3

where l1 = l2-1
and l2 = l3-1
and l1.Num = l2.Num
and l2.Num = l3.Num