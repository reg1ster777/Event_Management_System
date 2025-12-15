# ProgramDesignB 校园活动管理平台

## 远程网站
http://39.98.87.220/

## 项目概述
- 基于 Spring Boot + MyBatis + MySQL 的后端，提供活动、报名、签到、管理员帐号等 REST API。
- 前端使用纯静态 HTML/JavaScript，部署在 `src/main/resources/static` 下，涵盖首页、管理员工作台、报名页等场景。
- 通过 `application-dev.yml` 连接本地 `campus` 数据库，数据库需要你在本地自己创建（可在配置中修改）。
- 适用于课程/社团活动的发布、报名、签到记录与后台管理练习。

## 功能模块
### 后端 API
1. **管理员**（`AdminUserController`）  
   - `POST /admin/login`：校验用户名密码并返回管理员信息。  
   - `POST /admin/create`：创建新的管理员账号。
2. **活动管理**（`ActivityController` + `ActivityServiceImpl`）  
   - `GET /activities` / `GET /activities/{id}`：查询活动列表或详情，并自动刷新活动状态（未发布、报名中、已截止、已结束、已删除）。  
   - `POST /activities`：创建活动，校验开始/结束/报名截止时间及默认状态。  
   - `PUT /activities/{id}`：更新活动信息。  
   - `DELETE /activities/{id}?requesterId=`：仅允许创建者软删除活动。
3. **报名管理**（`RegistrationController`）  
   - `POST /activities/{id}/registrations`：检查活动状态、报名截止时间、人数上限、手机号/学号唯一性后完成报名。  
   - `GET /activities/{id}/registrations`：获取某活动全部报名记录。
4. **签到记录**（`CheckinController`）  
   - `POST /checkins`：基于报名 ID 和签到地点写入签到记录。  
   - `GET /checkins/registration/{regId}`：按报名记录查看签到历史。

### 前端静态页面
| 页面 | 作用 |
| --- | --- |
| `index.html` | 面向学生/访客的门户，展示活动信息入口。 |
| `login.html` | 管理员登录，写入 `localStorage`。 |
| `admin-activities.html` | 后台活动列表、筛选、查看详情、发布、删除。访客也可只读访问。 |
| `admin-create-activity.html` | 管理员创建活动的多字段表单。 |
| `register.html` & `register-success.html` | 学生报名活动及成功提示页。 |

静态页面通过 `fetch` 调用上述 REST API，与后端完全解耦，方便部署在同一 Spring Boot 服务中。

## 目录结构速览
```
programDesignB/
├── src/main/java/com/program/programdesignb
│   ├── ProgramDesignBApplication.java   # Spring Boot 启动类
│   ├── SecurityConfig.java              # 放行静态资源与 API 的安全配置
│   ├── controller/                      # Activity/Admin/Registration/Checkin 控制器
│   ├── domain/                          # Activity、Registration、AdminUser 等实体
│   ├── mapper/                          # MyBatis Mapper 接口（XML 位于 resources/mapper）
│   └── service/ & service/impl/         # 业务接口与实现
├── src/main/resources
│   ├── application.properties           # 通用配置（启用 dev profile）
│   ├── application-dev.yml              # 数据源 & MyBatis & 日志配置
│   ├── db/*.sql                         # 表结构及增补 SQL
│   ├── mapper/*.xml                     # MyBatis SQL 映射
│   └── static/*.html                    # 前端页面
└── pom.xml                              # Maven 依赖与构建配置
```

## 本地运行
1. **准备环境**：安装 JDK 17+、Maven、MySQL 8；新建 `campus` 数据库。
2. **初始化数据**：执行 `src/main/resources/db` 下的 SQL（根据需要扩展表结构）。
3. **配置检查**：若数据库连接信息不同，请修改 `application-dev.yml` 中的数据源配置。
4. **启动服务**：在项目根目录运行 `mvn spring-boot:run`，或在 IDE 中直接运行 `src/main/java/com/program/programdesignb/ProgramDesignBApplication.java`，也可以先执行 `mvn clean package` 后再运行 `java -jar target/program-design-b-*.jar`。
5. **访问页面**：浏览器打开 `http://localhost:8088/index.html`、`/login.html` 等静态页面，或使用 API 客户端调试 REST 接口。

## 典型使用流程
1. 管理员登录（或创建新账号）→ 进入 `admin-activities.html` 管理后台。
2. 在“新建活动”页面创建活动并发布，系统会根据时间自动更新状态。
3. 学生从首页跳转到报名页，完成报名信息提交。
4. 管理员可查询报名明细或在现场通过报名 ID 记录签到。
5. 通过活动列表查看详情、删除自己发布的活动，或在报名结束后导出数据（可在 Mapper 层自定义 SQL 扩展）。

## 进一步开发建议
- 在 `mapper` 中补充统计、导出 SQL，以支持报表或图表分析。
- 为管理员端补写权限控制或 Token 认证（当前仅靠前端 localStorage）。
- 对前端页面引入模块化构建（如 Vite/React）或使用组件库提升体验。

如需更多细节，可阅读对应包下的 Java 代码以及 `static` 目录中的页面脚本。


## 服务器更新

1. **打包生成新 JAR**
```powershell
.\mvnw.cmd clean package -DskipTests
```

2. **上传到服务器**
```powershell
scp .\target\programDesignB-0.0.1-SNAPSHOT.jar admin@39.98.87.220:/www/wwwroot/programDesignB/app/app.jar.new
```

3. **登录服务器，备份旧包并原子替换**
```bash
cd /www/wwwroot/programDesignB/app
sudo cp -a app.jar app.jar.bak.$(date +%F_%H%M%S)
sudo mv -f app.jar.new app.jar
sudo chown admin:admin app.jar
```

4. **（可选）同步配置文件**
```powershell
scp .\src\main\resources\application-prod.yml admin@39.98.87.220:/www/wwwroot/programDesignB/app/application-prod.yml
```

5. **（可选）执行数据库脚本**
```bash
mysql -uroot -p -S /tmp/mysql.sock campus < /www/wwwroot/programDesignB/sql/schema_fixed.sql
```

6. **重启 systemd 服务**
```bash
sudo systemctl restart programdesignb
sudo systemctl status programdesignb --no-pager
```
