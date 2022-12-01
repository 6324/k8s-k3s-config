









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


### 安装docker (containerd跳过)
```
curl -sSL https://get.daocloud.io/docker | sh
或
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
```

# 问题1. IDC厂商如何动态扩缩容服务器并规划hostname以及ip？
- pxe+kickstart 
- ansible

### docker镜像加速 (containerd跳过)
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

### containerd镜像加速
每个节点新增配置
```
vi /etc/rancher/k3s/registries.yaml

mirrors:
  docker.io:
    endpoint:
      - "https://on03swcr.mirror.aliyuncs.com/"
```



### 简单创建pod
```
kubectl run mynginx --image=nginx
```
### 命名空间

### 常用命令
```
kubectl get pod ...
kubectl get pod ... -owide
kubectl get pod ... -owide --watch
kubectl get service ...
kubectl get deploy ...
kubectl get rs ...
kubectl get all
kubectl describe pod xxx
kubectl logs xxx -n=xxx
kubectl exex -it xxx -- bash
kubectl delete xxx 
kubectl delete xxx --force
...

```

### 部署副本集(Deployment Replicaset) 
```
kubectl create deployment nginx-d1m --image=nginx:1.15 --replicas=3
```
### 手动缩放 （场景）
```
kubectl scale deployment/nginx-d1m --replicas=5
```
### 自动缩放 （场景）
```
kubectl autoscale deployment/nginx-d1m --min=3 --max=10 --cpu-percent=90

```
### 滚动更新
```
kubectl set image deployment/nginx-d1m nginx=nginx:1.17

kubectl rollout status deployment/nginx-d1m

```
### 历史版本
```
kubectl rollout history deployment/nginx-d1m
```
### 查看具体版本信息
```
kubectl rollout history deployment/nginx-d1m --revision=2
```
### 回滚到某个版本
```
kubectl rollout history deployment/nginx-d1m --revision=2
```

## Docker和Container关系
```
Kubelet->CRI->[docker-shim->Docker->]Containerd->runc->container

crictl k8s中CRI的交互客户端
ctr containerd 自带命令行工具
[详细用法](https://blog.csdn.net/u010157986/article/details/126118897)
```

### Service



## 一个pod可以多个容器 共享IP
## 金丝雀发布示例
## 生产环境最佳实践 spring cloud示例
### 网关 限流 rpc 服务发现 grafana 普罗米修斯

## 术语
- control-plane 控制平面（包含apiserver、scheduler、controller-manager...）
- kubelet 节点控制器 用来操作容器

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

### k3s kubeconfig文件
```
/etc/rancher/k3s/k3s.yaml
```
### 查看docker已登录信息
```
cat /root/.docker/config.json
```

### k8s创建secret

```
 kubectl create secret docker-registry xxx-harbor     --docker-server=https://docker-new.xxx.cn/     --docker-username='xxxxxxxxx'     --docker-password='xxxxxxxxxxxxx'
```

#### 私服创建pod
```
apiVersion: v1
kind: Pod
metadata:
  name: "k8sserver"
  namespace: default
  labels:
    app: "k8sserver"
spec:
  containers:
  - name: k8sserver
    image: "docker-xxxx.cn/pim/k8s-server:1.0"
    resources:
      limits:
        cpu: 200m
        memory: 500Mi
      requests:
        cpu: 100m
        memory: 200Mi
    ports:
    - containerPort: 8888
      name: http
  imagePullSecrets:
    - name: xxx-harbor
  restartPolicy: Always
```

## 生态

|  项目   | 类型  | 简介
|  ----  | ----  | ----
| [minikube](https://minikube.sigs.k8s.io/docs/)  | 官方 | k8s官方项目，用于单机学习模拟k8s |
| [Kubernetes DashBoard](https://github.com/kubernetes/dashboard)  | 官方 | kubernetes官方面板 |
| [Rancher](https://docs.ranchermanager.rancher.io/zh/)  | 官方 | k3s的爸爸 以前做k8s周边相关服务 可以从rancher安装并管理k8s |
| [k3s](https://docs.rancher.cn/docs/k3s/_index/)  | 官方 | k3s |
| [AutoK3s](https://docs.rancher.cn/docs/k3s/autok3s/_index)  | 官方 | 安装k3s集群脚手架 |
| [Kuboard](https://kuboard.cn/)  | 开源 | kubernetes开源面板 功能丰富 |
| [kuboard-spray](https://github.com/eip-work/kuboard-spray)  | 开源 | 基于 kubespray 提供图形化的 K8S 集群离线安装、维护工具。跟Kuboard一个作者 |
| [KubeSphere](https://kubesphere.io/zh/)  | 开源 | kubernetes开源面板 专注流程化 |
| [kubespray](https://github.com/kubernetes-sigs/kubespray)  | 开源 | 安装k8s的脚手架 |
| [kubeasz](https://github.com/easzlab/kubeasz)  | 开源 |安装k8s的脚手架 |
| [rainbond](https://www.rainbond.com/docs/)  | 开源 |云原生应用管理平台 |
| [Helm](https://helm.sh/zh/)  | 开源 |k8s下的包管理器 |


## 服务生态

|  项目   | 类型  | 简介
|  ----  | ----  | ----
| [istio]( )  | 服务网格 |  稳定的服务网格 路由 灰度 流量分发 |
| [linkerd](https://github.com/linkerd/linkerd2)  | 服务网格 | 轻量级的服务器网格 比较新  |
| [etcd](https://github.com/etcd-io/etcd)  | 存储 | 类Redis的轻量级分布式键值对数据库 |
| [CoreDNS](https://github.com/etcd-io/etcd)  | 服务发现 | 配合service解决pod pi频繁变动的问题 |
| [traefik](https://github.com/traefik/traefik)  | 反向代理 | 配合service解决pod pi频繁变动的问题 |
| [metrics-server](https://github.com/traefik/traefik)  | 监控 |  |
| [ingress]()  |   |  |
 