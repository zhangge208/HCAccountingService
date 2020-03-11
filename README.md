# HCAccountingService
HardCore Accounting Service



# 第一个Spring Boot应用：HelloSpringBoot

## Spring boot 开发四大步
- 添加相应依赖
- 添加相应注解
- 编写代码
- 添加相应配置

## 介绍

目标：构建一个RESTful Web Service

构建一个接受HTTP GET请求的服务：

```
http://localhost:8080/greeting
```

并返回JSON表示问候语：

```json
{"id":1,"content":"Hello, World!"}
```

可以使用查询字符串中的可选名称参数自定义问候语：

```
http://localhost:8080/greeting?name=User
```

name参数值将覆盖默认值“World”并显示在response中：

```json
{"id":2,"content":"Hello, User!"}
```

## 实验软件需求

1. JDK 1.8或更高的版本
2. Maven 3.2或者更高的版本
3. 一个顺手的IDE，推荐使用IntelliJ IDEA(当然Spring Tool Suite (STS)和Eclipse也是可以的）

## 实验步骤

### 1. 创建Maven项目

pom.xml中常用的几个标签

- `<modelVersion>`： POM model版本 (总是4.0.0).
- `<groupId>`： 项目所属的组或组织。通常表示为倒置的域名
- `<artifactId>` ：提供给项目的库项目的名称 (例如, 其 jar 或 war 文件的名称)
- `<version>`. 正在生成项目的版本
- `<packaging>` - 应如何打包项目。默认为 jar ，代表项目文件打包成jar包。使用 "war" 代表打包成war 包

### 2. 创建相应目录结构

``` text
└── src
    └── main
        └── java
            └── hello
            	└── controllers
            	└── model
  
```

### 3. 添加依赖

#### 添加全部依赖后完整的pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.xiedaimala.microservices</groupId>
    <artifactId>hello-spring-boot</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!-- Inherit defaults from Spring Boot -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
    </parent>
    <dependencies>
        <!-- Add typical dependencies for a web application -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <!-- Package as an executable jar -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 要点1: 添加Spring Boot依赖

开始一个Spring Boot项目，最简单的方式是继承`spring-boot-starter-parent`, 在pom.xml中添加如下代码

```
<!-- Inherit defaults from Spring Boot -->
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.2.5.RELEASE</version>
</parent>
```

不是每个人都喜欢从spring-boot-starter-parent继承开始Spring Boot项目。可能公司有自己标准的parent pom, 或者可能更愿意显式声明所有 maven 配置，这时候只需要在pom.xml添加如下代码，也可以使用Spring Boot：

```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<!-- Import dependency management from Spring Boot -->
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-dependencies</artifactId>
			<version>2.2.5.RELEASE</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

#### 要点2: Spring Boot Maven插件

```xml
<!-- Package as an executable jar -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
Spring Boot Maven(spring-boot-maven-plugin)插件提供了许多方便的功能：

- 收集类路径上的所有jar并构建一个可运行的jar，这使得执行和传输服务更加方便。
- 搜索public static void main()方法以标记为可运行的类。
- 提供了一个内置的依赖项解析器，它设置版本号以匹配Spring Boot依赖项。可以覆盖任何希望的版本，但它将默认为Boot的所选版本集

**最重要的事情:打包，变成一个可执行的jar包**

### 4. 编写资源表示类

从服务交互的角度出发，该服务将处理/greeting的GET请求，可选地在查询字符串中使用name参数。 GET请求应返回200 OK响应，其中JSON位于表示问候语的正文中。它应该看起来像这样：

```json
{
    "id": 1,
    "content": "Hello, World!"
}
```

在model文件夹下创建Greeting.java, 用于代表我们需要返回repsonse的内容

**src/main/java/hello/model/Greeting.java**

```java
package hello.model;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

```

### 5. 编写Controller

HTTP请求由控制器处理。这些组件可以通过@RestController注释轻松识别，下面的GreetingController通过返回Greeting类的新实例来处理/ greeting的GET请求：

**src/main/java/hello/controllers/GreetingController.java**

```java
package com.hardcore.accounting.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.hardcore.accounting.model.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
```

关于上面代码的一些解析：

- @RequestMapping注释用于确保对/ greeting的HTTP请求可以映射到greeting()方法上

- @RequestParam将查询字符串参数名称的值绑定到greeting()方法的name参数中。如果请求中不存在name参数，则使用“World”的defaultValue

- 传统MVC控制器和上面的RESTful Web服务控制器之间的关键区别在于创建HTTP响应主体的方式。RESTful Web服务控制器只是填充并返回一个Greeting对象，而不是依赖于视图技术来将Greeting对象数据呈现为HTML。Greeting对象数据将作为JSON直接写入HTTP响应

- @RestController注释会将类标记为控制器，其中每个方法都返回对象而不是视图。使用@RestController等同于使用@Controller和@ResponseBody

- Greeting对象必须转换为JSON。由于Spring的HTTP消息转换器支持，所以无需手动执行此转换。使用Jackson 2会自动选择Spring的MappingJackson2HttpMessageConverter将Greeting实例转换为JSON。

### 6. 编写Application

定义好model，完成controller编写后，我们需要让应用运行起来

虽然可以将此服务打包为传统的WAR文件以部署到外部应用程序服务器，但下面演示的更简单的方法创建了一个独立的应用程序。将所有内容打包在一个可执行的JAR中，由main()方法驱动。在此过程中使用Spring的支持将Tomcat servlet容器嵌入为HTTP运行时，而不是部署到外部实例。

**src/main/java/hello/Application.java**

```java
package com.hardcore.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

@SpringBootApplication（https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/SpringBootApplication.html）是一个便利注释，使用@SpringBootApplication注释等同于使用@Configuration，@ EnableAutoConfiguration和@ComponentScan

简单解释一下这些注解的作用：

- `@Configuration`: 允许在上下文中注册额外的 bean 或导入其他配置类
- `@EnableAutoConfiguration`: 开启Spring Boot自动配置机制（关于自动配置机制，见附录3）
- `@ComponentScan`告诉Spring在hello包中寻找其他组件、配置和服务，允许它找到控制器

在main()中利用SpringApplication.run()来启动应用，而且值得注意的一点是在Spring Boot应用中没有任何XML的配置，完全是纯粹的Java代码



### 7. 运行应用

两种方式：

1. 利用`./mvnw spring-boot:run`（当然也可以直接运行主函数）可直接运行

2. 运行`./mvnw clean package`打包（会产生一个target文件夹）, 然后执行jar包：`java -jar target/hello-spring-boot-1.0-SNAPSHOT.jar `

### 8. 测试RESTful Service
服务启动后，访问 http://localhost:8080/greeting 会看到：
```json
{"id":1,"content":"Hello, World!"}
```

 提供一个query参数：http://localhost:8080/greeting?name=User 显示：

```json
{"id":2,"content":"Hello, User!"}
```

## 总结

我们实现了一个最基本的Spring Boot应用

对于Spring Boot应用开发的流程，主要有下面的几个步骤：

1. 添加相应依赖
2. 添加对应注解
3. 编写模块代码
4. 添加相应配置

## 附录与参考资料

1. 如果你想用gradle构建Spring Boot，请看下面的链接：

   https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/reference/htmlsingle/#using-boot-gradle

2. https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-using-springbootapplication-annotation.html

3. Spring Boot自动配置机制：https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-auto-configuration.html

4. Spring Boot Reference：https://docs.spring.io/spring-boot/docs/2.0.8.RELEASE/reference/htmlsingle/

# 开始连接数据库

## mysql安装

### 下载镜像文件

``` sh
docker pull mysql:5.7 
```

### 创建实例并启动

```sh
docker run -p 3306:3306 --name mysql \
-v /tmp/mysql/log:/var/log/mysql \
-v /tmp/mysql/data:/var/lib/mysql \
-v /tmp/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=hardcore  \
-d mysql:5.7
```

参数说明

- -p 3306:3306：将容器的3306端口映射到主机的3306端口
- -v /tmp/mysql/conf:/etc/mysql：将配置文件夹挂在到主机
- -v /tmp/mysql/log:/var/log/mysql：将日志文件夹挂载到主机
- -v /tmp/mysql/data:/var/lib/mysql/：将配置文件夹挂载到主机
- -e MYSQL_ROOT_PASSWORD=hardcore：初始化root用户的密码

### 通过容器的mysql命令行工具连接

``` sh
docker exec -it mysql mysql -uroot -p

password: hardcore
```

### SQL ###

[阿里MySQL 数据库规约](https://github.com/alibaba/p3c/blob/master/阿里巴巴Java开发手册（华山版）.pdf)

```SQL
CREATE DATABASE hardcore_test;

USE hardcore_test;
DROP TABLE IF EXISTS `hcas_user`;
CREATE TABLE `hcas_userinfo` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(64) NOT NULL COMMENT 'user name',
    `password` varchar(64) NOT NULL,
    `create_time` datetime NOT NULL,
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY `pk_id` (`id`),
    UNIQUE KEY `uk_username` (`username`)
);

INSERT hcas_user value (NULL, 'hardcore_admin', 'hardcore', NOW(), NULL);

SELECT * from hardcore_test.hcas_user;

UPDATE hcas_user SET password='accounting' where username = 'hardcore_admin';

```



## SpringBoot与Mybatis集成

### 添加依赖 ###

```xml
<!-- mybatis-spring-boot-starter -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>

<!--Mysql driver-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.15</version>
</dependency>
```

### 添加注解 ###

#### UserInfoMapper

@Mapper

@Select

### 编写代码

#### UserInfoController

#### UserInfoManager

#### UserInfoDAO

#### model

### 添加配置

**application.yml**

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hardcore_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: hardcore
```

### 

### 测试

```json
GET http:localhost:8080/v1/users/1
Response:

```

# 项目代码结构与数据模型转换

## 项目代码组织结构

```text
└── src
    └── main
        └── java
            └── com.hardcore.accounting
              └── config
            	└── controller
            	└── converter
  	          └── manager
  	          └── dao
  	          └── external
            	└── model
            	└── exception
```

## 数据模型转换

### 阿里分层领域模型规约参考 

- DO（Data Object）：此对象与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。 
- DTO（Data Transfer Object）：数据传输对象，Service 或 Manager 向外传输的对象。 
- BO（Business Object）：业务对象，由 Service 层输出的封装业务逻辑的对象。 
- AO（Application Object）：应用对象，在 Web 层与 Service 层之间抽象的复用对象模型，极为贴近展示层，复用度不高。 
- VO（View Object）：显示层对象，通常是 Web 向模板渲染引擎层传输的对象

### 简单的三层数据模型

- persistence: 对应数据库表结构
- common: 对应Manager层使用
- service: 对应Controller层使用



### 如何转换

Google Guava Converter

```xml
<!-- Guava -->
<dependency>
		<groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>28.2-jre</version>
</dependency>
```

# API 接口设计与异常处理

## API接口设计

REST: Representational State Transfer（表述性状态转移）

http://en.wikipedia.org/wiki/Representational_state_transfer



参考资料：https://github.com/Microsoft/api-guidelines/blob/vNext/Guidelines.md

### URL的结构

- 易于理解和组织

- 如果是REST API不要出现动词，url里应该都是对资源的描述

- 个人建议最好带版本控制信息

```json
https://api.hardcore.com/v1.0/users/12

https://api.hardcore.com/v1.0/users/fetch/12


https://api.hardcore.com/v1.0/users/register
https:///api.hardcore.com/v1.0/sessions
```


### 支持的方法 ###

| Method  | Description                                                  | Is Idempotent |
| ------- | ------------------------------------------------------------ | ------------- |
| GET     | Return the current value of an object                        | True          |
| PUT     | Replace an object, or create a named object, when applicable | True          |
| DELETE  | Delete an object                                             | True          |
| POST    | Create a new object based on the data provided, or submit a command | False         |
| HEAD    | Return metadata of an object for a GET response. Resources that support the GET method MAY support the HEAD method as well | True          |
| PATCH   | Apply a partial update to an object                          | False         |
| OPTIONS | Get information about a request; see below for details.      | True          |

- GET /tickets - Retrieves a list of tickets
- GET /tickets/12 - Retrieves a specific ticket
- POST /tickets - Creates a new ticket
- PUT /tickets/12 - Updates ticket #12
- PATCH /tickets/12 - Partially updates ticket #12
- DELETE /tickets/12 - Deletes ticket #12



#### 幂等 

f(x) = f(f(X))

绝对值 abs(x) = abs(abs(x))

一次请求和多次请求某个资源应该具有同样的副作用

##### GET 

获取资源，不应有副作用 幂等

强调的是相同的副作用，而不是GET的结果相同

HEAD本质和GET一样

GET https://api.hardcore.com/resources/lastest

##### POST

创建资源，执行动作，有副作用 不是幂等

POST https://api.hardcore.com/resources/

https://api.hardcore.com/resources/1

https://api.hardcore.com/resources/2

##### DELETE

删除资源，有副作用，但还是会满足幂等性

DELETE https://api.hardcore.com/resources/123

删掉id为123的资源

##### PUT

更新操作，也是有副作用，满足幂等性

PUT https://api.hardcore.com/resources/123

更新id为123的资源

### Standard request headers

### Standard response headers



### Response格式

#### 基本要求

- JSON属性应该用camelCased
- Service应该将JSON作为默认编码
- Service必须要支持application/json，并且将application/json作为默认response format

```json
{
  "user": {
    "id": 1,
    "name": "hardcore"
  }
}
```



#### 错误请求的Response

一定要用HTTP status code

最简单的三类：

- OK 200

- Client side error：4XX
- Service side error： 5XX



```json
{
  "error": {
    "code": "BadArgument",
    "message": "Multiple errors in ContactInfo data",
    "target": "ContactInfo",
    "details": [
      {
        "code": "NullValue",
        "target": "PhoneNumber",
        "message": "Phone number must not be null"
      },
      {
        "code": "NullValue",
        "target": "LastName",
        "message": "Last name must not be null"
      },
      {
        "code": "MalformedValue",
        "target": "Address",
        "message": "Address is not valid"
      }
    ]
  }
}
```

##### ErrorResponse : Object

| Property | Type  | Required | Description       |
| -------- | ----- | -------- | ----------------- |
| `error`  | Error | ✔        | The error object. |

##### Error : Object

| Property     | Type       | Required | Description                                                  |
| ------------ | ---------- | -------- | ------------------------------------------------------------ |
| `code`       | String     | ✔        | One of a server-defined set of error codes.                  |
| `message`    | String     | ✔        | A human-readable representation of the error.                |
| `target`     | String     |          | The target of the error.                                     |
| `details`    | Error[]    |          | An array of details about specific errors that led to this reported error. |
| `innererror` | InnerError |          | An object containing more specific information than the current object about the error. |

##### InnerError : Object

| Property     | Type       | Required | Description                                                  |
| ------------ | ---------- | -------- | ------------------------------------------------------------ |
| `code`       | String     |          | A more specific error code than was provided by the containing error. |
| `innererror` | InnerError |          | An object containing more specific information than the current object about the error. |

##### Examples

```json
{
  "error": {
    "code": "BadArgument",
    "message": "Previous passwords may not be reused",
    "target": "password",
    "innererror": {
      "code": "PasswordError",
      "innererror": {
        "code": "PasswordDoesNotMeetPolicy",
        "minLength": "6",
        "maxLength": "64",
        "characterTypes": ["lowerCase","upperCase","number","symbol"],
        "minDistinctCharacterTypes": "2",
        "innererror": {
          "code": "PasswordReuseNotAllowed"
        }
      }
    }
  }
}
```

ErrorResponse:

```json
HttpStatusCode

Body:
{
  "statusCode":  404, //optional
  "message": "" // must
  "errorCode": "PASSWORD_INVALID", 
  "action": "",
  "details": [
   {},
	 {}
  ]
}
```


## 异常处理