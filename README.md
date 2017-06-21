### 用笔记本做服务器

租用国内服务器需要备案，上传身份证，购买阿里云幕布，拍照，审核，特别麻烦，租用国外的虽然不用备案但价格贵，速度慢，若可以用自己的笔记本做服务器的话既不需要备案还划算，但由于IP不是固定的，每次手动修改IP太麻烦，杭州电信大概是4天自动更换一次IP，该项目可以每隔一段时间自动获取公网IP，若发现IP改变则调用阿里云接口修改域名IP，若你的笔记本连接的是路由器，则只需要去路由器设置界面开启DMZ，ip填写你的笔记本的内网ip，或者开启端口转发，ip填写你的笔记本的内网ip，内网端口填写你笔记本中服务器监听的端口，若使用https的话外网端口填写443，当然也可以填写除80，8080以外的端口。


### 使用说明
##### 申请域名
去阿里云申请域名并实名认证域名
##### 获取阿里云 Access Key ID 和 Access Key Secret

![img](https://github.com/android-notes/personalServer/blob/master/ali-key.png?raw=true)

##### 修改BandDomain中的ID和SECRET为你的

##### AutoBandDomain中设置你的域名
##### 运行AutoBandDomain

> 若你的笔记本或台式机链接的是路由器的话，需要在路由器设置界面开启DMZ或设置端口转发

> 不要使用openjdk，否则会导致调用阿里云API失败！
> 由于国内运营商封锁了80和8080端口，所以无法通过这两个端口访问私人服务器，可以使用其他端口访问，也可以使用https
https需要申请证书，可以去腾讯云免费申请，https://console.qcloud.com/ssl ，服务器配置https很简单，支持nginx，tomcat ，apache ，iis   https://www.qcloud.com/document/product/400/4143




### 下面是搭建的私人服务器
[https://auto.烫烫.xyz](https://auto.烫烫.xyz)
