package df.yyzc.com.yydf.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.dataexchange.DataExchangeManage;
import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import df.yyzc.com.yydf.constans.YYOptions;
import df.yyzc.com.yydf.tools.DevMountInfo;

/**
 * Created by zhangyu on 16-4-13.
 */
public class YYDFApp extends Application {
    public static LocationClient mLocationClient;
    //    public static double Longitude = 116.4977040000;
//    public static double Latitude = 39.9713500000;
    public static double Longitude = 115.48715;
    public static double Latitude = 38.86837;
    //////    Longitude = 115.48715;
//////    Latitude = 38.86837;
    public static String LocaAdrrName = "保定市火车站";
    private static OnGetLocationlistener onGetLocationlistener;

    public static String BASEIP = "";
    public static String uploadImageDirectory = "";


    /**
     * 本地文件缓存的根目录e
     */
    public static String sdCardRootPath = "";

    private static YYDFApp instance;

    private static OSS oss;
    // 运行sample前需要配置以下字段为有效的值
    public static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String bucketName = "dzrzloss";
    public static final String OSSBaseUrl = "http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/";
    private MKOfflineMap offlineMap;
    private boolean hasUPloadOffMap = false;

    private DataExchangeManage exchangeManage;


    BDLocationListener baseListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (location != null) {
//                Longitude = location.getLongitude();
//                Latitude = location.getLatitude();
                LocaAdrrName = location.getAddrStr();
                //定位到保定
                Longitude = location.getLongitude();
                Latitude = location.getLatitude();

                if (onGetLocationlistener != null) {
                    onGetLocationlistener.getBdlocation(location);
                }

                if (!hasUPloadOffMap && !TextUtils.isEmpty(location.getCityCode())) {
                    hasUPloadOffMap = true;
                    if ("131".equals(location.getCityCode())) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                offlineMap.start(1);
                                offlineMap.start(131);
                            }
                        }.start();
                    } else if ("31".equals(location.getCityCode()) || "334".equals(location.getCityCode())) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                offlineMap.start(1);
                                offlineMap.start(31);
                                offlineMap.start(334);
                            }
                        }.start();
                    }
                }
            }
        }
    };
    private ImageLoader imageLoader;
    private HttpUtils httpUtils;

    // private RefWatcher refWatcher;

    public static YYDFApp getInstance() {
        return instance;
    }


    public static OSS getOss() {
        return oss;
    }

    public DataExchangeManage getExchangeManage() {
        if (exchangeManage == null) {
            exchangeManage = new DataExchangeManage();
            exchangeManage.initDataExchangeManage(this);
        }

        return exchangeManage;
    }


    /**
     * 注册地图回调接口
     *
     * @param listener
     */
    public static void setLocationIFC(OnGetLocationlistener listener) {
        onGetLocationlistener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.gc();
    }

    public HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        return imageLoader;
    }


//    public RefWatcher getRefWatcher() {
//        return refWatcher;
//    }

    private void init() {
        readConfig();
        //PgyCrashManager.register(this);//蒲公英
//        refWatcher = LeakCanary.install(this);
//        在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());//初始化百度地图
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(baseListener); // 注册监听函数
        DevMountInfo.getInstance().init(getApplicationContext(), true);
        sdCardRootPath = DevMountInfo.getInstance().getSDCardPath();
        initImageLoad(sdCardRootPath);
        initHttp();
        initOffBaiDuMap();
        initBaseLocation();
        initOSS();
    }

    private void initImageLoad(String rootPath) {

        if (!TextUtils.isEmpty(rootPath)) {

            File externalStorageDirectory = new File(rootPath + File.separator
                    + "yiyi" + File.separator + "image" + File.separator
                    + "imageCach" + File.separator);
            if (!externalStorageDirectory.exists()) {
                if (!externalStorageDirectory.mkdirs()) {
                    externalStorageDirectory = null;
                }
            }
            Builder config = new Builder(getApplicationContext())
                    .threadPriority(Thread.NORM_PRIORITY - 1)
                    .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                    .memoryCacheSize(10 * 1024 * 1024)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO);
            if (externalStorageDirectory != null) {
                config.diskCache(new UnlimitedDiscCache(
                        externalStorageDirectory));
            }
            config.defaultDisplayImageOptions(YYOptions.OPTION_DEF);
            ImageLoaderConfiguration build = config.build();
            ImageLoader.getInstance().init(build);
            // String imageUri = "http://site.com/image.png"; // from Web
            // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
            // String imageUri = "content://media/external/audio/albumart/13";
            // //
            // from content provider
            // String imageUri = "assets://image.png"; // from assets
            // String imageUri = "drawable://" + R.drawable.image; // from
            // drawables
            // (only images, non-9patch)

        }
    }


    /**
     * 初始化百度离线地图
     */
    private void initOffBaiDuMap() {

        offlineMap = new MKOfflineMap();
        // 传入接口事件，离线地图更新会触发该回调
        offlineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int type, int state) {
                //type - 事件类型: MKOfflineMap.TYPE_NEW_OFFLINE, MKOfflineMap.TYPE_DOWNLOAD_UPDATE, MKOfflineMap.TYPE_VER_UPDATE.
                //state - 事件状态: 当type为TYPE_NEW_OFFLINE时，表示新安装的离线地图数目. 当type为TYPE_DOWNLOAD_UPDATE时，表示更新的城市ID.

                switch (type) {
                    case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                        // 离线地图下载更新事件类型
                        MKOLUpdateElement update = offlineMap.getUpdateInfo(state);
                        Log.e("off", update.cityName + " ," + update.ratio);
                        Log.e("off", "TYPE_DOWNLOAD_UPDATE");
                        break;
                    case MKOfflineMap.TYPE_NEW_OFFLINE:
                        // 有新离线地图安装
                        Log.e("off", "TYPE_NEW_OFFLINE");
                        break;
                    case MKOfflineMap.TYPE_VER_UPDATE:
                        if (state == 1 || state == 131 || state == 31 || state == 334) {
                            offlineMap.update(state);
                        }
                        // 版本更新提示
                        Log.e("off", "TYPE_VER_UPDATE");
                        break;
                }
            }
        });

    }


    /**
     * 初始化定位参数
     */
    public void initBaseLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;// 设置为5分钟定位一次
        option.setScanSpan(span);//
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        // option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        // option.setLocationNotify(true);//
        // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        // option.setIsNeedLocationPoiList(true);//
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initHttp() {
    }

    public interface OnGetLocationlistener {
        void getBdlocation(BDLocation location);
    }


    private void initOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAImm2E6ormnadm", "tyXhFM42jz4f4VsXxm6TNyddNHaLaY");
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setBucketACL(CannedAccessControlList.PublicRead);
        createBucketRequest.setLocationConstraint("oss-cn-shanghai");
        OSSAsyncTask createTask = oss.asyncCreateBucket(createBucketRequest, new OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>() {
            @Override
            public void onSuccess(CreateBucketRequest request, CreateBucketResult result) {
                Log.d("locationConstraint", request.getLocationConstraint());
            }

            @Override
            public void onFailure(CreateBucketRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常
                if (clientException != null) {
                    // 本地异常如网络异常等
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }


    private void readConfig() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            BASEIP = appInfo.metaData.getString("BASEIP");
            uploadImageDirectory = appInfo.metaData.getString("uploadImageDirectory");


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
