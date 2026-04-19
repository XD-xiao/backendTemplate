# BackendTemplate - 后端模板项目

`backendTemplate` 是一个基于 Spring Boot 4.0.5 构建的高效后端基础模板项目。该项目集成了现代开发中常用的核心技术栈，旨在为开发者提供一个开箱即用的开发环境，快速启动新的业务逻辑开发。

## 🚀 技术栈

- **核心框架**: [Spring Boot 4.0.5](https://spring.io/projects/spring-boot)
- **人工智能**: [Spring AI (Ollama)](https://spring.io/projects/spring-ai) - 集成本地 AI 模型聊天功能。
- **持久层**: [MyBatis](https://mybatis.org/mybatis-3/zh/index.html) + [PostgreSQL](https://www.postgresql.org/) - 灵活的 SQL 映射与可靠的关系型数据库。
- **缓存与并发**: [Redis](https://redis.io/) + [Redisson](https://redisson.org/) - 支持高性能缓存、分布式锁等。
- **鉴权**: [JJWT (JSON Web Token)](https://github.com/jwtk/jjwt) - 基于令牌的无状态身份验证。
- **工具类库**: [Hutool](https://hutool.cn/) - 强大的 Java 工具包，简化日常开发。
- **简化代码**: [Lombok](https://projectlombok.org/) - 自动生成 Getter/Setter/ToString 等代码。
- **其他**: JavaMail (邮件发送)、Maven (构建工具)、Java 17。

## ✨ 主要功能模块

- **AI 交互**: `AiController` 集成了 `ChatClient`，支持通过 `/ai/chat` 接口与本地 Ollama 模型进行对话。
- **文件管理**: `FileController` 提供了文件上传（`/upload`）和下载/预览（`/file/{fileId}`）的功能。
- **安全鉴权**: 
  - `LoginCheckInterceptor` 实现请求拦截，校验 JWT 令牌。
  - `JwtUtil` 提供令牌的生成与解析。
  - `PasswordUtil` 提供安全的密码加密。
- **全局异常处理**: `GlobalExceptionHandler` 统一捕获并返回标准化的错误响应。
- **通用工具类**:
  - `EmailUtil`: 支持邮件发送功能。
  - `FileUtil`: 文件处理工具。
  - `RedisIdWorker`: 基于 Redis 的分布式全局唯一 ID 生成器。
  - `UserContext`: 基于 `ThreadLocal` 的用户信息上下文管理。
- **定时任务**: `TimeBasedTask` 展示了如何配置 Spring 定时任务（`@Scheduled`）。
- **缓存示例**: `TestCacheService` 展示了基本的缓存逻辑应用。

## 🛠️ 快速启动

### 1. 环境准备
- **Java 17+**
- **Maven 3.6+**
- **Redis**: 确保 Redis 服务已启动。
- **PostgreSQL**: 准备好数据库实例。
- **Ollama**: 如果需要使用 AI 功能，请确保 Ollama 已安装并运行。

### 2. 配置修改
在 `src/main/resources/application.yml` 中配置以下信息：
- 数据库连接信息 (`spring.datasource`)
- Redis 连接信息 (`spring.data.redis`)
- Ollama 连接信息 (`spring.ai.ollama`)
- 邮件服务配置 (`spring.mail`)

### 3. 运行
```bash
mvn clean install
mvn spring-boot:run
```

## 📂 项目结构
```text
src/main/java/com/example/backendtemplate/
├── Cache/           # 缓存服务示例
├── Config/          # 配置类 (Redis, Web, MyBatis 等)
├── Controller/      # 控制层 (AI, 文件处理等)
├── Exception/       # 异常处理
├── Interceptor/     # 拦截器 (登录校验)
├── Pojo/            # 实体类、通用返回结果、分页对象
├── Service/         # 业务逻辑
├── Utils/           # 工具类 (JWT, RedisIdWorker, UserContext 等)
├── scheduler/       # 定时任务
└── BackendTemplateApplication.java # 启动类
```

## 📝 开源协议
本项目采用 [MIT License](LICENSE) 协议。
