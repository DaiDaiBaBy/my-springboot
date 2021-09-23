## Springboot + Shiro
1.  shiro框架
shiro 是Apache下的一个开源项目。shiro属于轻量级框架，相对于SpringSecurity简单的多，也没有SpringSecurity那么复杂

2.  主要功能
shiro 主要有三大功能模块：
1). Subject：主体，一般指用户。
2). SecurityManager：安全管理器，管理所有Subject，可以配合内部安全组件。(类似于SpringMVC中的DispatcherServlet)
3). Realm：用于进行权限信息的验证，一般需要自己实现  (连接数据)

3.  细分功能
1). Authentication：身份认证/登录(账号密码验证)。                                                                                                                                                                                                                                                                                                                             
2). Authorization：授权，即角色或者权限验证。
3). Session Manager：会话管理，用户登录后的session相关管理。
4). Cryptography：加密，密码加密等。
5). Web Support：Web支持，集成Web环境。
6). Caching：缓存，用户信息、角色、权限等缓存到如redis等缓存中。
7). Concurrency：多线程并发验证，在一个线程中开启另一个线程，可以把权限自动传播过去。
8). Testing：测试支持；
9). Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问。
10). Remember Me：记住我，登录后，下次再来的话不用登录了
