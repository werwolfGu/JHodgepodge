# shadowsocks搭建


                                            首先要租用一个服务器:阿里云或http://www.vultr.com/  linux 版本 ： oscent7  
- 安装shadowsocks
curl "https://bootstrap.pypa.io/get-pip.py" -o "get-pip.py"  或 wget https://bootstrap.pypa.io/get-pip.py

python get-pip.py

pip install --upgrade pip

pip install shadowsocks

- 创建文件

vi  /etc/shadowsocks.json

```json
{
    "server":"0.0.0.0",
    "server_port":2020,
    "password":"12345678",
    "method":"aes-256-cfb"
}
```

- 关闭防火墙三种方式

systemctl stop firewalld.service

iptables -I INPUT -p tcp --dport your_server_port -j ACCEPT

iptables -I OUTPUT -p tcp --dport your_server_port -j ACCEPT

- 启动ShadowSocks服务

ssserver -c /etc/shadowsocks.json -d start

- 停止ss 服务   

ssserver -c /etc/shadowsocks.json -d stop

>mac/window 下载ShadowSocks 客户端 ; https://github.com/shadowsocks/ShadowsocksX-NG/releases