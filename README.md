### 列出本项目所有的task
`gradlew tasks`

### 编译整个项目
`gradlew clean build` 确保每次提交代码前,上述之类都能跑过.

### 启动application
`gradlew clean bootRun`

### 手动测试application
可使用postman发送请求:

* Header: 
```
Content-Type :text/html
```
* Body:
```
['ITEM000000-3','ITEM000000-2','ITEM000001-2', 'ITEM000004']
```
* 可以得到下面的ResponseBody:
```
***<没钱赚商店>购物清单***
名称：可口可乐，数量：5瓶，单价：3.00(元)，小计：15.00(元)
名称：雪碧，数量：2瓶，单价：3.00(元)，小计：6.00(元)
名称：电池，数量：1个，单价：2.00(元)，小计：2.00(元)```
`-----------------------------
总计：23.00(元)
`******************************
```

### 生成intellij需要的ipr文件
`gradlew idea`
`gradlew cleanIdea idea`

### 提交代码规范
`[name & name] comment`


