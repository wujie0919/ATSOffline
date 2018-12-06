package df.yyzc.com.yydf.tools;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;

public class ApkDownLoad {

    public static final String DOWNLOAD_FOLDER_NAME = "Download";
    public static final String DOWNLOAD_FILE_NAME = "yiyizuche.apk";
    public static final String APK_DOWNLOAD_ID = "apkDownloadId";
    private Context context;
    private String url;
    private String notificationTitle;
    private String notificationDescription;

    private DownloadManager downloadManager;
    private CompleteReceiver completeReceiver;

    /**
     * @param context
     * @param url                     下载apk的url
     * @param notificationTitle       通知栏标题
     * @param notificationDescription 通知栏描述
     */
    public ApkDownLoad(Context context, String url, String notificationTitle,
                       String notificationDescription) {
        super();
        this.context = context;
        this.url = url;
        this.notificationTitle = notificationTitle;
        this.notificationDescription = notificationDescription;
        downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        completeReceiver = new CompleteReceiver();

        /** register download success broadcast **/
        context.registerReceiver(completeReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 查询下载状态
     */
    public static int queryDownloadStatus(DownloadManager downloadManager,
                                          long downloadId) {
        int result = -1;
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                result = c.getInt(c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }

    /**
     * install app
     *
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath),
                    "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public void execute() {

        // 清除已下载的内容重新下载
        long downloadId = UpdateUtils.getLong(context, APK_DOWNLOAD_ID);
        if (downloadId != -1) {
            downloadManager.remove(downloadId);
            UpdateUtils.removeSharedPreferenceByKey(context, APK_DOWNLOAD_ID);
        }

        Request request = new Request(Uri.parse(url));
        // 设置Notification中显示的文字
        request.setTitle(notificationTitle);
        request.setDescription(notificationDescription);
        // 设置可用的网络类型
        request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
                | Request.NETWORK_WIFI);
        // 设置状态栏中显示Notification
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 不显示下载界面
        request.setVisibleInDownloadsUi(false);
        // 设置下载后文件存放的位置
        File folder = Environment
                .getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        // 设置下载文件的保存路径
        request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME,
                DOWNLOAD_FILE_NAME);
        // 设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
        // 保存返回唯一的downloadId
        UpdateUtils.putLong(context, APK_DOWNLOAD_ID,
                downloadManager.enqueue(request));
    }

    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * get the id of download which have download success, if the id is
             * my id and it'interfaces status is successful, then install it
             **/
            long completeDownloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            long downloadId = UpdateUtils.getLong(context, APK_DOWNLOAD_ID);

            if (completeDownloadId == downloadId) {

                // if download successful
                if (queryDownloadStatus(downloadManager, downloadId) == DownloadManager.STATUS_SUCCESSFUL) {

                    // clear downloadId
                    UpdateUtils.removeSharedPreferenceByKey(context,
                            APK_DOWNLOAD_ID);

                    // unregisterReceiver
                    context.unregisterReceiver(completeReceiver);

                    // install apk
                    String apkFilePath = new StringBuilder(Environment
                            .getExternalStorageDirectory().getAbsolutePath())
                            .append(File.separator)
                            .append(DOWNLOAD_FOLDER_NAME)
                            .append(File.separator).append(DOWNLOAD_FILE_NAME)
                            .toString();
                    install(context, apkFilePath);
                }
            }
        }
    }

}
