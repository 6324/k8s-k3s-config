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

### docker镜像加速
``` 
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://on03swcr.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 重启docker
```
sudo systemctl start docker
```

## 安装Master节点
### 安装server时指定容器为docker、指定token (国内)
```
curl -sfL https://rancher-mirror.oss-cn-beijing.aliyuncs.com/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn  K3S_TOKEN=ABCDEFG sh -s - --docker	
```
### 查看K3S token
```
cat /var/lib/rancher/k3s/server/token
```
## 安装Agent节点
```
curl -sfL https://rancher-mirror.oss-cn-beijing.aliyuncs.com/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn K3S_TOKEN=K102884562888a23fc7064420d1685fa3cd6ec6f253dd6ef003729c940fe6c64309::server:ABCDEFG K3S_URL=https://192.168.114.201:6443 sh -s - --docker

或

curl -sfL https://rancher-mirror.oss-cn-beijing.aliyuncs.com/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn K3S_TOKEN=ABCDEFG K3S_URL=https://192.168.114.200:6443 sh -s - --docker

```
## Docker和Container关系
## 有状态和无状态
## CoreDNS
## ingress 路由
## 一个pod多个容器
## nginx示例
### 集群 
## 金丝雀发布示例
### nginx为例 replicas

## 生产环境最佳实践 spring cloud示例
### 网关 限流 rpc 服务发现 grafana 普罗米修斯


### 安装helm

官网: https://helm.sh/zh/docs
```
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh

或

curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
```
### 添加helm仓库

```
helm repo add bitnami https://charts.bitnami.com/bitnami
```
### 使用helm安装应用
```
helm install bitnami/mysql --generate-name
```

## 其他
### 容器内查看IP
```
cat /etc/hosts
```
### 一个pod多个容器
```
kubectl exec -it test-7d7c9775c5-pp9mm -c nginx  -- bash
```