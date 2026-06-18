\<type\>:-t -n -p分别对应收藏夹类型为town城镇,nation国家,player玩家的类型参数(必填)

\<query\>:-i -N分别对应根据id代号查询和根据name名字查询的查询参数(默认为-N)

\<search\>:-t -n -p分别对应操作对象为town城镇,nation国家,player玩家的搜索参数(默认为\<type\>对应的搜索参数-t-\>-t,-n-\>-n,-p->-p)

\[name\]对应name名字,\[id\]对应id代号,\[object\]对应操作对象

位于-p收藏夹类型中的hate收藏夹的玩家,名称会被标记为红色且在周围100m时会发出警告

位于-t收藏夹类型中的show_border的城镇会显示边界

1./favorites \<type\>

查看的收藏夹或对应类型的所有收藏夹均可用鼠标互动
    
    直接按enter即可显示对应类型的所有收藏夹
    <type>空格后接

    create [name] 自定义[name]名字来创建对应类型的收藏夹
    create [name] [id] 自定义[name]名字和[id]代号来创建对应类型的收藏夹

    delete <query> [favorite] 自定义<query>来删除对应类型的[favorite]收藏夹
    delete [favorite] 使用默认<query>来删除对应类型的[favorite]收藏夹

    add <query> [favorite] <search> [object] 自定义<search>自定义<query>,来为[favorite]收藏夹添加[object]相关对象
    add <query> [favorite] [object] 默认<search>自定义<query>,来为[favorite]收藏夹添加[object]相关对象
    add [favorite] <search> [object] 自定义<search>默认<query>,来为[favorite]收藏夹添加[object]相关对象
    add [favorite] [object] 默认<search>默认<query>,来为[favorite]收藏夹添加[object]相关对象

    remove <query> [favorite] <search> [object] 自定义<search>自定义<query>,来为[favorite]收藏夹移除[object]相关对象
    remove <query> [favorite] [object] 默认<search>自定义<query>,来为[favorite]收藏夹移除[object]相关对象
    remove [favorite] <search> [object] 自定义<search>默认<query>,来为[favorite]收藏夹移除[object]相关对象
    remove [favorite] [object] 默认<search>默认<query>,来为[favorite]收藏夹移除[object]相关对象

    show <query> [favorite] 自定义<query>来查看对应类型的[favorite]收藏夹
    show [favorite] 使用默认<query>来查看对应类型的[favorite]收藏夹

    

2./page \[pages\] 对于本mod任何可翻页显示均适用,\[pages\]即为对应的页数

3./rex 后接

    hate <search> [object] 根据<search>搜索参数对[object]对象查找相关的player玩家并将该玩家加入hate收藏夹
    hate [object] 将[object]玩家对象添加进hate收藏夹

    unhate <search> [object] 根据<search>搜索参数对[object]对象查找相关的player玩家并将该玩家移出hate收藏夹
    unhate [object] 将[object]玩家对象移出hate收藏夹

    favorites同/favorties命令,收藏夹类型为-p玩家

4./tx 后接

    border show <search> [object] 根据<search>搜索参数对[object]对象查找相关的town城镇并将该城镇加入show_border收藏夹
    border show [object] 将[object]城镇对象添加进show_border收藏夹
    border hide <search> [object] 根据<search>搜索参数对[object]对象查找相关的town城镇并将该城镇移出show_border收藏夹
    border hide [object] 将[object]城镇对象移出show_border收藏夹

    favorites同/favorties命令,收藏夹类型为-t城镇
5./nx 后接

    favorites同/favorties命令,收藏夹类型为-n国家
