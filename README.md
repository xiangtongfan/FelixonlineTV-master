# WZYonlineTV  
很多初学者想做视频播放，但是对于Vitamio不会导入，网上版本也比较多，官方版本导入方式也不尽相同，官网也没有做一些详尽教程。  
这个案例教大家如何将Vitamio导入。  
首先，打开studio，新建项目，点击new---import module---导入官方的vitamio包（官方已于2016年6月16日更新至Vitamio 5.0.2）。  
下一步很重要，大家都会不注意这一步就会出现问题。一开始导入的时候在gradle里面会出现这么几行代码： 

    compileSdkVersion Integer.parseInt(project.ANDROID_BUILD_SDK_VERSION)  
    buildToolsVersion project.ANDROID_BUILD_TOOLS_VERSION  
      
    defaultConfig {  
        minSdkVersion Integer.parseInt(project.ANDROID_BUILD_MIN_SDK_VERSION)  
        targetSdkVersion Integer.parseInt(project.ANDROID_BUILD_TARGET_SDK_VERSION)  
    }  
在studio中不会自动将后面的Integer自动转换成版本号的，所以需要大家改这个，切记。  
我将这个版本改成了  

    compileSdkVersion 24  
    buildToolsVersion '24.0.1'  

    defaultConfig {  
        minSdkVersion 15  
        targetSdkVersion 22  
    }  
大家可以根据自己的实际情况去设置这些数值。  
之所以设置成22的版本，是因为安卓6.0的权限问题有改变，为了减少判断这些权限，我就没有写23或者24  
接下来就是将这个module导入依赖包，这个比较简单，不需要详细描述了吧  
最后就可以用它了，记得初始化，以下给出初始化代码（demo）：  


        super.onCreate(savedInstanceState);  
        Vitamio.isInitialized(this);  
        setContentView(R.layout.activity_main);  

另外，Vitamio5.0.2有一个坑，由于eclipse中出现的一些问题，5.0.2进行了升级，改了一些so文件和一些代码，主要在这个文件（vitamio\src\io\vov\vitamio\MediaPlayer.java）中，原来的代码是：  

        String LIB_ROOT = Vitamio.getLibraryPath();  
而现在的代码是：  

    String LIB_ROOT;
    if(VERSION.SDK_INT > 20) {
        LIB_ROOT = "";
    }
    else{
    	LIB_ROOT = Vitamio.getLibraryPath();
    }
所以，要想在studio中使用Vitamio5.0.2，需要先将此代码改回，这里我推荐大家直接使用我在这儿上传的Vitamio5.0.0  
