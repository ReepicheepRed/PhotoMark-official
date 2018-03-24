# Posterify
[ ![Bintray](https://img.shields.io/badge/bintray-v2.1.0-brightgreen.svg) ](https://bintray.com/jessyancoding/maven/MVPArms/2.1.0/link)
[ ![Build Status](https://travis-ci.org/JessYanCoding/MVPArms.svg?branch=master) ](https://travis-ci.org/JessYanCoding/MVPArms)
[ ![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat-square) ](https://developer.android.com/about/versions/android-4.0.3.html)
[ ![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)
[ ![QQGroup](https://img.shields.io/badge/QQ群-301733278-ff69b4.svg) ](https://shang.qq.com/wpa/qunwpa?idkey=1a5dc5e9b2e40a780522f46877ba243eeb64405d42398643d544d3eec6624917)

## Posterify是为商店营销创建促销海报的最简单和最强大的海报制作工具。 它提供数百个专业设计的海报模板，让您立即创建促销和产品海报在Facebook ，lnstagram ，Twitter等最流行的社交网络上分享。

## Main Function

·海报模板        海报模板分类eg.节日、价格、热销、促销...
·制作海报        导入海报样式，编辑文字，添加图片
·保存分享        保存编辑完成后的海报预览或分享

## Feature

* **网络请求层**: 如今主流的网络请求框架有 `Volley`,`Okhttp`,`Retrofit` (`Android-async-http` 停止维护了) ,因为这个库是基于 `Rxjava` , `Retrofit` 支持 `Rxjava` ,默认使用 `Okhttp` 请求网络(`Okhttp` 使用 `Okio` , `Okio` 基于 IO 和 NIO 性能优于 `Volley` , `Volley` 内部封装有 Imageloader ,支持扩展 `Okhttp` ,封装比 `Okhttp` 好,但是比较适合频繁,数据量小的网络请求),所以此库默认使用 `Retrofit` 作为网络请求层.

* **图片加载**: 因为图片加载框架各有优缺点，`Fresco`,`Picasso`,`Glide` 这些都是现在比较主流得图片加载框架,所以为了扩展性本框架提供一个统一的管理类 Imageloader ,使用策略者模式,开发者只用实现接口,就可以动态替换图片框架,外部提供统一接口加载图片,替换图片加载框架毫无痛点,并且为了快速实现,默认提供一个 Glide 的默认实现类,有其它需求可以参照 [**Wiki**](https://github.com/JessYanCoding/MVPArms/wiki#3.4) 替换为别的图片加载框架.

* **Model层**: 优秀的数据库太多, `GreenDao`,`Realm`,`SqlBrite`（Square 公司出品，对 SQLiteOpenHelper 封装，提供响应式 Api 访问数据库）, `SqlDelight`,`Storio`,`DBFlow` ,每个框架的使用方法都不一样,本框架只提供有一个管理类 RepositoryManager 里面默认封装了 `RxCache` (此框架根据 `Retrofit` Api 实现了缓存逻辑,并提供响应式接口), `Retrofit` 等与数据相关的框架,以后有其他需求如需使用数据库,就可以直接封装进 RepositoryManager ,本框架通过 Dagger2 向 Model 层注入 RepositoryManager,来提供给开发者数据处理的能力,这样的好处的是上层（Activity/Fragment/Presenter）不需要知道数据源的细节（来自于网络、数据库、亦或是内存等等）,下层可以根据需求修改（缓存的实现细节）上下两层分离互不影响.

## Functionality & Libraries
1. [`Mvp`Google官方出品的`Mvp`架构项目，含有多个不同的架构分支(此为Dagger分支).](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/)
2. [`Dagger2`Google根据Square的Dagger1出品的依赖注入框架，通过Apt编译时生成代码，性能优于使用运行时反射技术的依赖注入框架.](https://github.com/google/dagger)
3. [`Rxjava`提供优雅的响应式Api解决异步请求以及事件处理.](https://github.com/ReactiveX/RxJava)
4. [`RxAndroid`为Android提供响应式Api.](https://github.com/ReactiveX/RxAndroid)
5. [`Rxlifecycle`在Android上使用rxjava都知道的一个坑，就是生命周期的解除订阅，这个框架通过绑定activity和fragment的生命周期完美解决.](https://github.com/trello/RxLifecycle)
6. [`RxCache`是使用注解为Retrofit加入二级缓存(内存,磁盘)的缓存库.](https://github.com/VictorAlbertos/RxCache)
7. [`RxErroHandler` 是 `Rxjava` 的错误处理库,可在出现错误后重试.](https://github.com/JessYanCoding/RxErrorHandler)
8. [`RxPermissions`用于处理Android运行时权限的响应式库.](https://github.com/tbruyelle/RxPermissions)
9. [`Retrofit`Square出品的网络请求库，极大的减少了http请求的代码和步骤.](https://github.com/square/retrofit)
10. [`Okhttp`同样Square出品，不多介绍，做Android都应该知道.](https://github.com/square/okhttp)
11. [`Autolayout`鸿洋大神的Android全尺寸适配框架.](https://github.com/hongyangAndroid/AndroidAutoLayout)
12. [`Gson`Google官方的Json Convert框架.](https://github.com/google/gson)
13. [`Butterknife`JakeWharton大神出品的view注入框架.](https://github.com/JakeWharton/butterknife)
14. [`Androideventbus`一个轻量级使用注解的Eventbus.](https://github.com/hehonghui/AndroidEventBus)
15. [`Timber`JakeWharton大神出品Log框架容器，内部代码极少，但是思想非常不错.](https://github.com/JakeWharton/timber)
16. [`Glide`此库为本框架默认封装图片加载库，可参照着例子更改为其他的库，Api和`Picasso`差不多,缓存机制比`Picasso`复杂,速度快，适合处理大型图片流，支持gfit，`Fresco`太大了！，在5.0以下优势很大，5.0以上系统默认使用的内存管理和`Fresco`类似.](https://github.com/bumptech/glide)
17. [`LeakCanary`Square出品的专门用来检测`Android`和`Java`的内存泄漏,通过通知栏提示内存泄漏信息.](https://github.com/square/leakcanary)


## Acknowledgements 
感谢本框架所使用到的所有三方库的 **Author** ,以及所有为 `Open Sourece` 做无私贡献的 **Developer** 和 **Organizations** ,使我们能更好的工作和学习,本人也会将业余时间回报给开源社区


## About Me
* **Email**: <jess.yan.effort@gmail.com>  
* **Home**: <http://jessyan.me>
* **掘金**: <https://gold.xitu.io/user/57a9dbd9165abd0061714613>
* **简书**: <http://www.jianshu.com/u/1d0c0bc634db>

## License
``` 
 Copyright 2016, jessyan       
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at 
 
       http://www.apache.org/licenses/LICENSE-2.0 

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
