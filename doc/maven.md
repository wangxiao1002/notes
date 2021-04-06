1. 平常使用maven 也只是普通的依赖和打包
2. 这次开源项目学习区别和更多插件的使用
## 打包跳过测试
* mvn clean package -Dmaven.test.skip=true
* mvn clean package -DskipTests=true
 上述俩者的区别 第一个打包跳过测试用例也不打包测试文件 第二个仅仅是打包跳过测试
