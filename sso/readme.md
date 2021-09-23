##  单点登录  各种实现方式   springboot集成
什么是单点登录？
    单点登录全称 Single Sign On（以下简称SSO），是指在多系统应用群中登录一个系统，
    便可在其他所有系统中得到授权而无需再次登录，包括单点登录与单点注销两部分
用户登录成功之后，会与sso认证中心及各个子系统建立会话，
    用户与sso认证中心建立的会话称为全局会话，用户与各个子系统建立的会话称为局部会话
### 实现方式一： 共享父域Cookie (不支持跨域)  基于redis--token
Cookie的作用域；是由domain属性和path属性共同决定的：
domain属性：   当前域或其父域的域名/IP地址，在 Tomcat 中，domain 属性默认为当前域的域名/IP地址
path属性： 以“/”开头的路径，在 Tomcat 中，path 属性默认为当前 Web 应用的上下文路径
如果将 Cookie 的 domain 属性设置为当前域的父域，那么就认为它是父域 Cookie
Cookie有一个特点，即父域中的Cookie被子域所共享，即子域能够继承父域cookie，利用这个特点，只要将SessionId(或Token)保存在父域中就OK了；

so？
我们只需要将 Cookie 的 domain 属性设置为父域的域名（主域名），同时将 Cookie 的 path 属性设置为根路径，这样所有的子域应用就都可以访问到这个 Cookie 了
不过这要求应用系统的域名需建立在一个共同的主域名之下，如 http://tieba.baidu.com 和 http://map.baidu.com，
它们都建立在 http://baidu.com 这个主域名之下，那么它们就可以通过这种方式来实现单点登录

注： 此种实现方式比较简单，但不支持跨主域名

### 实现方式二：认证中心 
可以专门布置一个认证中心：
认证中心就是一个专门负责处理登录请求的独立的 Web 服务：
用户统一在认证中心进行登录，登录成功后，认证中心记录用户的登录状态，并将 Token 写入 Cookie。（注意这个 Cookie 是认证中心的，应用系统是访问不到的）

1. 系统会检查当前请求有没有token，如果没有，这说明还没登录，那么就会将页面跳转至认证中心，由于这个操作会将认证中心的Cookie自动带过去，
因此，认证中心就能够根据Cookie知道用户是否登录过了，

2. 如果认证中心发现用户尚未登录，则返回登录页面进行登录； 如果发现用户已经登录过了，，就不会再让用户登录了，而是会跳转回目标URL，并在跳转前生成一个Token
拼接在目标URL后面，回传给系统；

3. 系统拿到Token以后，还需要向认证中心确认下Token的合法性，防止用户伪造

4. 系统确认无误后，会记录用户的登录状态，并将Token写入Cookie，并给本次访问放行，(注意这个Cookie是当前系统的，其他系统是访问不到的)
当用户再次访问当前系统的时候，就会自动带上这个Token，系统验证Token就会发现用户已经登录，于是就不会再有认证中心什么事儿了，

认证中心：
1. Apereo CAS 是一个企业级单点登录系统，其中 CAS 的意思是”Central Authentication Service“
2. XXL-SSO 是一个简易的单点登录系统，由大众点评工程师许雪里个人开发，代码比较简单，没有做安全控制，因而不推荐直接应用在项目中，

注： 此种实现方式相对复杂，支持跨域，扩展性好，是单点登录的标准做法

### 实现方式三： LocalStorage跨域
通过前面两种实现方式，我们知道了实现单点登录的关键在于：怎么让SessionId(或Token)在多个域中进行共享。
父域Cookie是一种不错的解决方案，但是不支持跨域，

但是，现在很多开发都使用的是 前后端分离的模式，完全可以不再使用Cookie，可以选择SessionIf(或Token)保存到浏览器的LocalStorage中，
让前端在每次向后端发送请求时，主动将LocalStorage中的数据传递给服务端
而且这些都是由前端来控制的，后端需要做的仅仅是在用户登录成功后，将SessionId(或Token)放在响应体中传递给前端，
在这样的场景下，单点登录完全可以在前端实现：
前端拿到SessionId(或Token)后， 除了将它写入到自己的LocalStorage中之外，还可以通过其他手段把它写入其他域下的LocalStorage中：
```html
// 获取 token
var token = result.data.token;

// 动态创建一个不可见的iframe，在iframe中加载一个跨域HTML
var iframe = document.createElement("iframe");
iframe.src = "http://app1.com/localstorage.html";
document.body.append(iframe);
// 使用postMessage()方法将token传递给iframe
setTimeout(function () {
    iframe.contentWindow.postMessage(token, "http://app1.com");
}, 4000);
setTimeout(function () {
    iframe.remove();
}, 6000);

// 在这个iframe所加载的HTML中绑定一个事件监听器，当事件被触发时，把接收到的token数据写入localStorage
window.addEventListener('message', function (event) {
    localStorage.setItem('token', event.data)
}, false);
```
```text
前端通过 iframe+postMessage() 方式，将同一份 Token 写入到了多个域下的 LocalStorage 中，
前端每次在向后端发送请求之前，都会主动从 LocalStorage 中读取 Token 并在请求中携带，
这样就实现了同一份 Token 被多个域所共享
```

注： 此种实现方式完全由前端控制，几乎不需要后端参与，同样支持跨域

### 域名分级
根据《计算机网络》中的定义：
.com、 .cn为一级域名(也称顶级域名)
.com.cn、 baidu.com为二级域名
sina.com.cn、 tieba.baidu.com为三级域名

而在实际使用中，可以根据使用者的具体使用进行分级：
一般把可支持独立备案的主域名称为一级域名比如baidu.com、 sina.com.cn
在主域名下建立的子域名称为二级域名tieba.baidu.com









