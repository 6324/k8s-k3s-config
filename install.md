## 环境准备

### 关闭防火墙

```
systemctl stop firewalld.service
systemctl disable firewalld.service
systemctl status firewalld.service
```

### 设置静态ip
```
vi /etc/sysconfig/network-scripts/ifcfg-ens33
```

```
BOOTPROTO="static"
ONBOOT="yes"
#ip
IPADDR=192.168.114.200
NETMASK=255.255.255.0
#gateway
GATEWAY=192.168.114.2
#dns
DNS1=192.168.114.2
```


### 重启网络
```
service network restart
```
### 设置hostname
```
hostnamectl set-hostname k3s-master
--hostnamectl set-hostname k3s-node1
--hostnamectl set-hostname k3s-node2
```


### 安装docker(国内)
```
curl -sSL https://get.daocloud.io/docker | sh
或
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
```

### 重启docker
```
sudo systemctl start docker
```

## 安装Master节点
### 安装server时指定容器为docker、指定token (国内)
```
curl -sfL https://rancher-mirror.oss-cn-beijing.aliyuncs.com/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn  K3S_TOKEN=fuckfuck sh -s - --docker	
```
## 安装Agent节点
```
curl -sfL https://rancher-mirror.oss-cn-beijing.aliyuncs.com/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn K3S_TOKEN=K102884562888a23fc7064420d1685fa3cd6ec6f253dd6ef003729c940fe6c64309::server:fuckfuckfuck K3S_URL=https://192.168.100.201:6443 sh -s - --docker
```