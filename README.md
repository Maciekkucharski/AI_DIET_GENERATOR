# AI_DIET_GENERATOR
A personalized diet generator using AI, which is an engineering thesis project

## Setup guide

### Pre-requisites

1. Running docker daemon
2. Mysql workbench installed

### Mysql server setup with docker

```bash
docker pull mysql
docker run --name diet-generator -e MYSQL_ROOT_PASSWORD=jajco123 -p 3306:3306 -d mysql:latest
```

### Create new database connection in Mysql workbench

![obraz](https://user-images.githubusercontent.com/66561544/197417128-c345961c-0e7a-486d-8e8e-95dcfcf71b59.png)

Use **jajco123** as root password

### Create new database schema 

![obraz](https://user-images.githubusercontent.com/66561544/197417223-6f9ed678-6218-4967-a822-e52ae5808048.png)
