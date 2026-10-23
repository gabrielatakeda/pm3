# EasyNote no Kubernetes (Minikube)

Migração do projeto **EasyNote** (Mensal 1), um sistema de empréstimo de equipamentos para alunos, para um cluster Kubernetes local com Minikube.

| Componente | Tecnologia | Imagem |
|---|---|---|
| Frontend | Angular 18 servido por nginx | `easynote-frontend:2.0` |
| Backend | Spring Boot 3.3 (Java 17), API REST com JWT | `easynote-backend:1.0` |
| Banco de dados | MySQL 8.0 | `mysql:8.0` |

## Arquitetura

### Como os componentes se comunicam

1. **Navegador → frontend:** o Service `easynote-frontend` é do tipo **NodePort**, e por isso fica acessível de fora do cluster na porta `30080` do nó do Minikube.
2. **Frontend → backend:** o Angular chama URLs relativas (`/api/...`), que vão para o mesmo nginx que entregou a página. O nginx ([frontend/nginx.conf](frontend/nginx.conf)) repassa essas requisições para `http://easynote-backend:8082`. `easynote-backend` é o nome do Service, resolvido pelo **DNS interno do Kubernetes** (CoreDNS). Como a página e a API têm a mesma origem, não há problema de CORS.
3. **Backend → banco:** o Spring lê `DB1_HOST`, `DB1_PORT` e `DB1_NAME` do **ConfigMap** e `DB1_USER`/`DB1_PASS` do **Secret**. `DB1_HOST=easynote-db` é o nome do Service do MySQL.
4. **Balanceamento:** os Services do tipo ClusterIP distribuem as conexões entre todos os Pods prontos que têm o label do `selector`. O nginx abre uma conexão nova com o backend a cada requisição, então as chamadas se alternam entre as 2 réplicas.

### Recursos do Kubernetes utilizados

| Recurso | Arquivo | Para quê |
|---|---|---|
| Namespace | [k8s/00-namespace.yaml](k8s/00-namespace.yaml) | Isola os recursos do projeto (`easynote`) |
| ConfigMap | [k8s/01-configmap.yaml](k8s/01-configmap.yaml) | Host, porta e nome do banco |
| Secret | [k8s/02-secret.yaml](k8s/02-secret.yaml) | Usuário e senhas do MySQL |
| PersistentVolumeClaim | [k8s/10-mysql.yaml](k8s/10-mysql.yaml) | Mantém os dados do MySQL quando o Pod é recriado |
| Deployment + Service (ClusterIP) | [k8s/10-mysql.yaml](k8s/10-mysql.yaml) | Banco, 1 réplica |
| Deployment + Service (ClusterIP) | [k8s/20-backend.yaml](k8s/20-backend.yaml) | API, **2 réplicas**, com probes de saúde |
| Deployment + Service (NodePort) | [k8s/30-frontend.yaml](k8s/30-frontend.yaml) | Interface web, 2 réplicas, exposta na porta 30080 |

O backend também usa a **Downward API** para receber o nome e o IP do próprio Pod em variáveis de ambiente (`POD_NAME`, `POD_IP`). O endpoint `/api/info` devolve esses dados e é usado na demonstração do balanceamento.

## Estrutura do repositório

```
backend/     código do Spring Boot + Dockerfile + docker-entrypoint.sh
frontend/    código do Angular + Dockerfile + nginx.conf
k8s/         manifestos do Kubernetes (aplicados em ordem numérica)
docs/        evidências (capturas de tela)
```

## Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) em execução
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/), ou use `minikube kubectl --` no lugar de `kubectl`

No Windows, com winget:

```bash
winget install Kubernetes.minikube
winget install Kubernetes.kubectl
```

## Passo a passo

### 1. Iniciar o Minikube

```bash
minikube start --driver=docker --memory=4096 --cpus=2
kubectl get nodes
```

### 2. Construir as imagens e disponibilizá-las no Minikube

As imagens são construídas com o Docker local e depois carregadas no Minikube. Os manifestos usam `imagePullPolicy: Never`, então o cluster usa essas imagens locais em vez de buscá-las num registry.

```bash
docker build -t easynote-backend:1.0 backend
docker build -t easynote-frontend:2.0 frontend

minikube image load easynote-backend:1.0
minikube image load easynote-frontend:2.0

minikube image ls | grep easynote
```

> Alternativa: `minikube image build -t easynote-backend:1.0 backend` constrói a imagem direto dentro do Minikube.

> A imagem `easynote-frontend:1.0` (visual antigo) só é usada na demonstração de atualização de versão. Ela foi construída com o código original do Mensal 1 ([DevsEmCrise/easynote-front](https://github.com/DevsEmCrise/easynote-front)) mais a página `lb.html`, antes da mudança de visual.

### 3. Aplicar os manifestos

```bash
kubectl apply -f k8s/
```

Acompanhe até todos os Pods ficarem `Running` e `READY 1/1`. O backend leva cerca de 1 minuto para subir, porque espera o MySQL ficar disponível:

```bash
kubectl get pods -n easynote -w
```

### 4. Acessar a aplicação

```bash
minikube service easynote-frontend -n easynote
```

Esse comando abre o navegador na URL do frontend. Com o driver Docker no Windows/macOS, o terminal precisa ficar aberto enquanto você usa a aplicação.

- Aplicação: `/`. Login padrão criado automaticamente: **admin / admin123**
- Demonstração do balanceamento: `/lb.html`
- Teste direto da API: `/api/info`

Outra opção é usar port-forward, que funciona em qualquer sistema:

```bash
kubectl port-forward -n easynote service/easynote-frontend 8090:80
# acessar http://localhost:8090
```

> **Atenção:** o `port-forward` se conecta a **um único Pod**. Se esse Pod for excluído ou substituído (numa atualização de versão, por exemplo), a conexão cai e é preciso rodar o comando de novo. Nas demonstrações de recriação e de atualização, use o `minikube service`, que passa pelo Service NodePort e continua funcionando durante a troca de Pods.

## Demonstração

Para facilitar, defina o namespace padrão:

```bash
kubectl config set-context --current --namespace=easynote
```

### Comandos pedidos

```bash
kubectl get pods -o wide
kubectl get services
kubectl get deployments
```

### Balanceamento de carga

Abra `/lb.html`. A página chama `/api/info` duas vezes por segundo e mostra um contador por Pod do backend. Os dois contadores sobem, o que mostra que o Service está distribuindo as requisições.

Pelo terminal (com o port-forward da seção anterior ativo):

```bash
for i in $(seq 1 10); do curl -s http://localhost:8090/api/info; echo; done
```

Ou acompanhando os logs das duas réplicas ao mesmo tempo:

```bash
kubectl logs -l app=easynote-backend -f --prefix
```

### Recriação automática de um Pod

```bash
kubectl get pods -l app=easynote-backend
kubectl delete pod <nome-de-um-pod-do-backend>
kubectl get pods -l app=easynote-backend -w
```

O Deployment mantém o número de réplicas declarado (`replicas: 2`). Quando um Pod é excluído, o ReplicaSet cria outro com um nome novo. Na página `/lb.html`, o cartão do Pod excluído fica cinza e aparece um cartão novo assim que o Pod substituto fica pronto.
