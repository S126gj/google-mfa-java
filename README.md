# Google MFA

基于 Spring Boot 的 Google 多因素身份验证（MFA）服务端实现。

## 项目简介

本项目实现了 Google 身份验证器（Google Authenticator）的 Java 服务端功能，支持：
- 生成随机密钥（Secret Key）
- 生成二维码供用户扫描绑定
- 验证用户输入的动态验证码

## 技术栈

- Java 17
- Spring Boot 2.7.3
- Google ZXing 3.3.3（二维码生成）
- Apache Commons Codec 1.15

## 项目结构

```
src/main/java/com/sgj/
├── TestApplication.java          # Spring Boot 启动类
├── auth/
│   └── GoogleAuthenticator.java  # Google 身份验证器核心实现
├── controller/
│   └── Test.java                 # 测试接口
└── util/
    ├── GoogleAuthUtil.java       # 身份认证工具类
    └── QrCodeUtil.java           # 二维码工具类
```

## 核心功能

### 1. GoogleAuthenticator

Google 身份验证器的核心实现类，主要方法：

| 方法 | 说明 |
|------|------|
| `generateSecretKey()` | 生成随机密钥 |
| `getQRBarcode(user, secret)` | 生成二维码内容（otpauth 格式） |
| `check_code(secret, code, time)` | 验证动态验证码是否有效 |

### 2. QrCodeUtil

二维码生成工具类，支持：
- 生成普通二维码
- 生成带 Logo 的二维码
- 支持输出到文件或 HttpServletResponse

### 3. GoogleAuthUtil

封装的身份认证工具类，提供便捷的业务方法。

## 接口说明

### 1. 生成密钥并返回二维码

**请求：**
```
GET /genSecretTest?username=test@example.com
```

**响应：**
- 直接输出二维码图片（建议使用 Postman/Apifox 查看）
- 控制台输出 `secret` 信息，需保存用于后续验证

### 2. 验证动态验证码

**请求：**
```
GET /verifyTest?code=123456&secret=生成的密钥
```

**响应：**
```
验证成功  // 或 验证失败
```

## 使用示例

### 1. 启动项目

```bash
mvn spring-boot:run
```

### 2. 绑定 Google Authenticator

1. 访问 `http://localhost:8080/genSecretTest?username=你的账号`
2. 使用 Postman 或 Apifox 获取二维码图片
3. 用 Google Authenticator App 扫描二维码
4. 保存控制台输出的 `secret`

### 3. 验证动态码

1. 打开 Google Authenticator App，查看当前 6 位动态码
2. 访问 `http://localhost:8080/verifyTest?code=动态码&secret=之前保存的secret`
3. 返回"验证成功"即表示功能正常

## TOTP 原理

本实现基于 TOTP（Time-based One-Time Password）算法：
- 密钥长度：20 字节
- 时间步长：30 秒
- 验证码位数：6 位
- 允许时间偏移：可配置（默认 3 个时间窗口，约 90 秒）

## 注意事项

1. `secret` 必须妥善保存，建议存储在数据库中并与用户账号关联
2. 验证码有效期为 30 秒，建议设置适当的时间窗口以容错时钟偏差
3. 生产环境建议将 `SEED` 配置为自定义值

## License

MIT
