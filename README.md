项目说明：
JavaMethodPerformanceMonitor项目是为了实现对任何Java方法在执行过程中的性能监控而创建的学习项目，该项目可以对任何Java程序，在无需源代码的情况下通过Java Byte Code植入的方式添加性能监控代码。本项目实现的核心技术需要代码学习者或者使用者至少了解Java ClassFile文件格式，Apache BCEL，以及Java ClassLoader相关技术和工具。

文件目录说明：
1. MyClassLoader文件夹存储了核心实现代码，其核心类为MyClassLoader.java, ClassTransfor.java类实现了性能监控器植入代码
2. SampleJava文件夹为Java示例代码

调用方式：
java -Djava.system.class.loader=com.alex.classloader.MyClassLoader -Djava.class.path=/Users/wangwei/Java/public_lib/MyClassLoader.jar:/Users/wangwei/Java/public_lib/SampleJava.jar com.alex.sample.Caculator

1.-Djava.system.class.loader用来改变Java在执行时所采用的类加载器，此处替换为com.alex.classloader.MyClassLoader，让此类来进行类的加载，该类在加载的过程中能自动化植入性能监控代码
2.-Djava.class.path用来指定Java在加载类时所寻找的目录
3.com.alex.sample.Caculator为测试类，可替换为任意需要执行的Java代码