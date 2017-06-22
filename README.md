
### 搭建免费私人服务器---用你的笔记本做服务器

> 首先去阿里云申请域名，并实名认证域名，否则无法解析域名到IP


租用国内服务器需要备案，上传身份证，购买阿里云幕布，拍照，审核，特别麻烦，租用国外的虽然不用备案但价格贵，速度慢，若可以用自己的笔记本做服务器的话既不需要备案还划算，但由于IP不是固定的，每次手动修改IP太麻烦，杭州电信大概是4天自动更换一次IP，该工程可以每隔一段时间自动获取公网IP，若发现IP改变则调用阿里云接口修改域名IP，若你的笔记本连接的是路由器，则只需要去路由器设置界面开启DMZ，ip填写你的笔记本的内网ip，或者开启端口转发，ip填写你的笔记本的内网ip，内网端口填写你笔记本中服务器监听的端口，若使用https的话外网端口填写443，当然也可以填写除80，8080以外的端口。

### 使用方式
#### 修改配置
* 修改`AutoBandDomain.DOMAIN`和`AutoBandDomain.SUB_DOMAIN`为你的域名
* 修改` BandDomain.ID和BandDomain.SECRET`为你的阿里云`Access Key ID`和`Access Key Secret`(如何获取Access Key ID和Access Key Secret见下文)

#### 运行代码
* mac和linux用户命令窗口切换目录到工程跟目录，运行` sh run.sh`，windows用户双击 `run_wind.bat`即可
* 当然也可以把代码复制到eclipse等java编辑器中，同时加入`commons-codec-1.10.jar`和`gson-2.8.1.jar`依赖后运行`AutoBandDomain`


### 注意

* 建议代码编码采用utf8
* 支持mac和linux，windows暂未测试
* 不支持openJDK，若一定要使用openJDK可以把`HttpRequest`中`HttpURLConnection`替换成其他http库，比如`okhttp`



按照上述操作后你的域名就解析到你的笔记本的公网IP了，若你的笔记本没有直接连接公网，而是连接了路由器的话，可以在路由器管理页面开启DMZ或者端口转发，这样即使你在内网，外网中的用户通过域名也能访问到你了，附 极路由设置方式，其他路由器略有不同。

![img](https://github.com/android-notes/personalServer/blob/master/forward-port.png?raw=true)
我的笔记本的内网IP是`192.168.199.249`，我的笔记本中的tomcat服务器监听的端口是`8443`，只要外网用户 访问 `http://你的域名:443` 就可以请求到你的tomcat服务器了，另外我还设置了1024端口映射到路由器的80端口，这样我就可以远程登录我的路由器了。

由于运营商封锁了80和8080端口，所以外网用户没法通过http默认的80端口进行访问，所以只能通过非80，8080端口进行访问。若一定要通过默认端口的可可以参考下文

### 配置Tomcat，开启https

https默认采用443端口，这个端口没有被运营商封锁，只要我们的服务器开启https的话外网用户就可以通过 `https://你的域名`  进行访问了，开启https需要SSL证书，你可以自己生成SSL证书，但浏览器会提示SSL证书有问题，所有我们可以去第三方申请免费的SSL证书，比如 腾讯云 或者 阿里云等等。

### 申请证书
以腾讯云为例，首先进入 [https://console.qcloud.com/ssl]() ，然后点击申请证书
![img](https://github.com/android-notes/personalServer/blob/master/Tencent-ssl.png?raw=true)
按照提示填写 域名和私钥密码，提交后会立即生成证书，可以下载下来放到tomcat服务器目录下，如果没有填写私钥密码，则不提供Tomcat证书文件的下载，需要用户手动转换格式生成。

> 可以通过 Nginx 文件夹内证书文件和私钥文件生成jks格式证书
转换工具：[https://www.trustasia.com/tools/cert-converter.htm]()
使用工具时注意填写 密钥库密码 ，安装证书时配置文件中需要填写。


###  证书安装
以 tomcat7为例
配置SSL连接器，将下载的 `你的域名.jks` 文件存放到tomcat下的conf目录下，然后配置同目录下的server.xml文件：

```xml
<Connector port="443" protocol="HTTP/1.1" SSLEnabled="true"
    maxThreads="150" scheme="https" secure="true"
    keystoreFile="conf\你的域名.jks"
    keystorePass="申请证书时的私钥"
    clientAuth="false" sslProtocol="TLS" />
```

#### 可选配置

http自动跳转https的安全配置

到conf目录下的web.xml。在`</welcome-file-list>`后面，`</web-app>`，也就是倒数第二段里，加上这样一段
```xml
<web-resource-collection >
    <web-resource-name >SSL</web-resource-name>
    <url-pattern>/*</url-pattern>
</web-resource-collection>
<user-data-constraint>
    <transport-guarantee>CONFIDENTIAL</transport-guarantee>
</user-data-constraint>

```
这步目的是让非ssl的connector跳转到ssl的connector去。所以还需要前往server.xml进行配置：

```
<Connector port="8080" protocol="HTTP/1.1"
    connectionTimeout="20000"
    redirectPort="443" />
```
redirectPort改成ssl的connector的端口443，重启后便会生效。


配置完后重启tomcat即可生效，若无法通过https访问可查看 `tomcat/log/catalina.yyyy-mm-dd.log` 日志文件
> 若使用Apache、IIS、Nginx 服务器可以参考 [https://www.qcloud.com/document/product/400/4143]()

### 如何获取Access Key ID和Access Key Secret ？

登录阿里云，点击控制台，鼠标移动到用户名上会弹出如下窗口，点击 accesskeys 即可看到Access Key ID和Access Key Secret


![img](https://github.com/android-notes/personalServer/blob/master/ali-key.png?raw=true)


### 下面是搭建的私人服务器
[https://auto.烫烫.xyz](https://auto.烫烫.xyz)
