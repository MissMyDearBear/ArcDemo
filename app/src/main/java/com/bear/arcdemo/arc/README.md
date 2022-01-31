## 基础架构设计

基于jetpack clean架构设计分层，app，data，domain
1. app:存放Activity等等页面相关的逻辑
2. data:存放数据仓库，和业务逻辑无相关的帮助类等
3. domain：大型复杂的页面需要多层数据的组合，可能需要这一层，充当中间件组合多个数据仓库。

### app
App对应UI层，这里采用MVVM模式来设计

- model：数据model （页面数据model 区别于服务端给的数据结构）
- view: 界面绘制，xml动态代码
- viewModel: 处理业务逻辑，通常配合liveData以及dataBinding实现数据双向绑定