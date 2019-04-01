# myexam

#### 介绍
基于web的在线考试系统

#### 软件架构
采用springboot+spring data jpa+mybatis框架

#### 软件需求

1. redis 3.2+
2. mysql 5.7+
3. jdk 1.8

#### 开启方式
导入sql文件（/src/main/resources/file/myexam.sql）

修改相关密码
```
redis密码配置在（/src/main/resources/redis/redis-config.properties）中，属性名为redis.password
mysql密码配置在（/src/main/resources/application.properties）中，属性名为spring.datasource.password
```
运行方式
```
运行com.learning.exam.ExamApplication或者使用maven打包jar运行
访问：http://127.0.0.1:8080/login
```


角色账号密码
```
超级管理员:admin 123456
学生：201511010125 123456
管理员：admin1 123456
教师：admin2 123456
```