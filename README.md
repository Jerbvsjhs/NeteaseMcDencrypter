# NeteaseMcDencrypter
适用于网易版Miecraft的跨平台存档解密工具

## 合作链接（点击跳转）
[存档解密网页版 由ihaiming部署](https://mc.hm0.top/) 
[网页版项目 by ihaiming](https://github.com/ihaiming/NetEaseMC-Decryptor/)

## 运行参数
输出LOGO 可多次调用,或与其他命令组合,优先级高于加解密操作
```
NeMcDecrypter.exe -logo
```
保存存档秘钥至当前路径 -gkey:db路径 -key
```
NeMcDecrypter.exe -gkey:db路径 -key
```
保存存档秘钥至指定路径 -gkey:db路径 -key:指定秘钥路径+/名称
```
NeMcDecrypter.exe -gkey:db路径 -key:指定秘钥路径+/名称
```
解密存档 -dec:db路径
```
NeMcDecrypter.exe -dec:db路径
```
扫描路径下加密的文件 -sc:路径
```
NeMcDecrypter.exe -sc:路径
```
加密存档 -dec:加密bd路径 -enc:待加密bd路径
```
NeMcDecrypter.exe -dec:加密bd路径 -enc:待加密bd路径
```
加密存档 -enc:待加密bd路径 -key:指定秘钥加密
```
NeMcDecrypter.exe -enc:待加密bd路径 -key:指定秘钥加密
```

