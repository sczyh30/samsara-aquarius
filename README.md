# Samsara Aquarius

[![Travis Build Status](https://travis-ci.org/sczyh30/samsara-aquarius.svg?branch=master)](https://travis-ci.org/sczyh30/samsara-aquarius)

Samsara Aquarius是一个由 **Scala** 编写，基于Play Framework 2.5.2, Slick 3以及Akka的分享类Web Application。

Present master version: **0.7.6-B4**

![Samsara Aquarius](docs/imgs/aquarius.png)

## Build Dist Version

```bash
sbt dist
```

Notice: 在生产环境中，请开启 `play.http.filters` 选项以保证安全，同时需要去掉前端的所有外部依赖。另外，Aquarius使用了 *Geetest Captcha*，因此如果要使其正常工作，你需自行在`conf`目录中创建`captcha.conf`文件并配置。

## Current Task

- [ ] Review: Ensure security(current version is **not** verified)
- [ ] More functional style
- [ ] Microservice