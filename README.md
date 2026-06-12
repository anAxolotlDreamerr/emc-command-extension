1./favorites
    主命令:/favorites空格后接<type>,可显示对应类型的所有收藏夹
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

    <type>:-t -n -p分别对应收藏夹类型为town城镇,nation国家,player玩家的类型参数(必填)
    <query>:-i -N分别对应根据id代号查询和根据name名字查询的查询参数(默认为-N)
    <search>:-t -n -p分别对应操作对象为town城镇,nation国家,player玩家的搜索参数(默认为<type>对应的随所参数-t->-t,-n->-n,-p->-p)
    [name]对应name名字,[id]对应id代号,[object]对应操作对象
    查看的收藏夹或对应类型的所有收藏夹均可用鼠标互动

