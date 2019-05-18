package cf.playhi.freezeyou;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.grandcentrix.tray.AppPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static cf.playhi.freezeyou.ThemeUtils.processActionBar;
import static cf.playhi.freezeyou.ThemeUtils.processSetTheme;

public class BackupMainActivity extends Activity {

//    Camera mCamera = null; 先把文本方式稳定下来，再做 QRCode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        processSetTheme(this);
        super.onCreate(savedInstanceState);
        processActionBar(getActionBar());
        setContentView(R.layout.bma_main);

        onCreateInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCreateInit() {
        initButtons();
    }

    private void initButtons() {
        Button bma_main_export_button = findViewById(R.id.bma_main_export_button);
        Button bma_main_import_button = findViewById(R.id.bma_main_import_button);
        Button bma_main_copy_button = findViewById(R.id.bma_main_copy_button);
        Button bma_main_paste_button = findViewById(R.id.bma_main_paste_button);

        bma_main_export_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.bma_main_inputAndoutput_editText);
                editText.setText(processExportContent());
                editText.selectAll();
            }
        });

        bma_main_import_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.bma_main_inputAndoutput_editText);
                String data = editText.getText().toString();
                editText.setText(GZipUtils.gzipDecompress(data));
            }
        });

        bma_main_copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.bma_main_inputAndoutput_editText);
                MoreUtils.copyToClipboard(getApplicationContext(), editText.getText().toString());
            }
        });

        bma_main_paste_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.bma_main_inputAndoutput_editText);
                editText.setText(MoreUtils.getClipboardItemText(getApplicationContext()));
            }
        });

    }

    private String processExportContent() {
        final AppPreferences appPreferences = new AppPreferences(getApplicationContext());
        final SharedPreferences defSP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        JSONObject finalOutputJsonObject = new JSONObject();

        try {

            // 通用设置转出开始（更多设置 中的选项，不转移图标选择相关设置）

            // boolean 开始
            JSONArray generalSettingsBooleanJSONArray = new JSONArray();
            JSONObject generalSettingsBooleanJSONObject = new JSONObject();
            generalSettingsBooleanJSONObject.put(
                    "allowEditWhenCreateShortcut",
                    convertSharedPreference(defSP, "allowEditWhenCreateShortcut", true)
            );
            generalSettingsBooleanJSONObject.put(
                    "noCaution",
                    convertSharedPreference(defSP, "noCaution", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "saveOnClickFunctionStatus",
                    convertSharedPreference(defSP, "saveOnClickFunctionStatus", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "cacheApplicationsIcons",
                    convertSharedPreference(defSP, "cacheApplicationsIcons", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "showInRecents",
                    convertSharedPreference(appPreferences, "showInRecents", true)
            );
            generalSettingsBooleanJSONObject.put(
                    "lesserToast",
                    convertSharedPreference(appPreferences, "lesserToast", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "notificationBarFreezeImmediately",
                    convertSharedPreference(appPreferences, "notificationBarFreezeImmediately", true)
            );
            generalSettingsBooleanJSONObject.put(
                    "notificationBarDisableSlideOut",
                    convertSharedPreference(appPreferences, "notificationBarDisableSlideOut", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "notificationBarDisableClickDisappear",
                    convertSharedPreference(appPreferences, "notificationBarDisableClickDisappear", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "onekeyFreezeWhenLockScreen",
                    convertSharedPreference(appPreferences, "onekeyFreezeWhenLockScreen", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "freezeOnceQuit",
                    convertSharedPreference(appPreferences, "freezeOnceQuit", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "avoidFreezeForegroundApplications",
                    convertSharedPreference(appPreferences, "avoidFreezeForegroundApplications", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "avoidFreezeNotifyingApplications",
                    convertSharedPreference(appPreferences, "avoidFreezeNotifyingApplications", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "openImmediately",
                    convertSharedPreference(appPreferences, "openImmediately", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "openAndUFImmediately",
                    convertSharedPreference(appPreferences, "openAndUFImmediately", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "shortcutAutoFUF",
                    convertSharedPreference(defSP, "shortcutAutoFUF", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "needConfirmWhenFreezeUseShortcutAutoFUF",
                    convertSharedPreference(defSP, "needConfirmWhenFreezeUseShortcutAutoFUF", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "openImmediatelyAfterUnfreezeUseShortcutAutoFUF",
                    convertSharedPreference(defSP, "openImmediatelyAfterUnfreezeUseShortcutAutoFUF", true)
            );
            generalSettingsBooleanJSONObject.put(
                    "enableInstallPkgFunc",
                    convertSharedPreference(defSP, "enableInstallPkgFunc", true)
            );
            generalSettingsBooleanJSONObject.put(
                    "tryDelApkAfterInstalled",
                    convertSharedPreference(appPreferences, "tryDelApkAfterInstalled", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "useForegroundService",
                    convertSharedPreference(appPreferences, "useForegroundService", false)
            );
            generalSettingsBooleanJSONObject.put(
                    "debugModeEnabled",
                    convertSharedPreference(appPreferences, "debugModeEnabled", false)
            );
            generalSettingsBooleanJSONArray.put(generalSettingsBooleanJSONObject);
            finalOutputJsonObject.put("generalSettings_boolean", generalSettingsBooleanJSONArray);
            // boolean 结束

            // String 开始
            JSONArray generalSettingsStringJSONArray = new JSONArray();
            JSONObject generalSettingsStringJSONObject = new JSONObject();
            generalSettingsStringJSONObject.put(
                    "onClickFuncChooseActionStyle",
                    convertSharedPreference(defSP, "onClickFuncChooseActionStyle", "1")
            );
            generalSettingsStringJSONObject.put(
                    "uiStyleSelection",
                    convertSharedPreference(defSP, "uiStyleSelection", "default")
            );
            generalSettingsStringJSONObject.put(
                    "launchMode",
                    convertSharedPreference(defSP, "launchMode", "all")
            );
            generalSettingsStringJSONObject.put(
                    "organizationName",
                    convertSharedPreference(defSP, "organizationName", getString(R.string.app_name))
            );
            generalSettingsStringJSONObject.put(
                    "shortCutOneKeyFreezeAdditionalOptions",
                    convertSharedPreference(appPreferences, "shortCutOneKeyFreezeAdditionalOptions", "nothing")
            );
            generalSettingsStringJSONArray.put(generalSettingsStringJSONObject);
            finalOutputJsonObject.put("generalSettings_string", generalSettingsStringJSONArray);
            // String 结束

            // 通用设置转出结束

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GZipUtils.gzipCompress(finalOutputJsonObject.toString());//Base64.encodeToString(result.toString().getBytes(),Base64.DEFAULT)
    }

    private String convertSharedPreference(
            SharedPreferences sharedPreferences, String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    private String convertSharedPreference(
            AppPreferences appPreferences, String key, String defValue) {
        return appPreferences.getString(key, defValue);
    }

    private boolean convertSharedPreference(
            SharedPreferences sharedPreferences, String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    private boolean convertSharedPreference(
            AppPreferences appPreferences, String key, boolean defValue) {
        return appPreferences.getBoolean(key, defValue);
    }

//    先把文本方式稳定下来，再决定是否做 QRCode
//    private void processQRCodeAndQRCodeImageView(String qrContent) {
//        ImageView bma_main_qrCode_imageView = findViewById(R.id.bma_main_qrCode_imageView);
//        FrameLayout bma_main_qrCode_frameLayout = findViewById(R.id.bma_main_qrCode_frameLayout);
//
//        int width = getWindowManager().getDefaultDisplay().getWidth();
//        if (width <= 0) {
//            width = 300;
//        }
//
//        int wh = (int) (width * 0.6);
//        int padding = (width - wh) / 2;
//
//        int frontColor;
//        if (Build.VERSION.SDK_INT < 21) {
//            frontColor = Color.BLACK;
//        } else {
//            // https://stackoverflow.com/questions/27611173/how-to-get-accent-color-programmatically
//            TypedValue typedValue = new TypedValue();
//            TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorAccent});
//            frontColor = a.getColor(0, 0);
//            a.recycle();
//        }
//
//        final Bitmap qrCode = QRCodeUtil.createQRCodeBitmap(
//                qrContent == null ? "" : qrContent,
//                wh, wh, "L", "1", frontColor, Color.TRANSPARENT);
//
//        bma_main_qrCode_imageView.setImageBitmap(qrCode);
//
//        bma_main_qrCode_frameLayout.setPadding(padding, padding, padding, padding);
//        bma_main_qrCode_frameLayout.setMinimumWidth(wh);
//        bma_main_qrCode_frameLayout.setMinimumHeight(wh);
//
//        bma_main_qrCode_imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String imgPath = getCacheDir() + File.separator + new Date().toString();
//                ApplicationIconUtils.writeBitmapToFile(imgPath, qrCode);
//                startActivity(
//                        new Intent(BackupMainActivity.this, FullScreenImageViewerActivity.class)
//                                .putExtra("imgPath", imgPath)
//                );
//            }
//        });
//    }
//
//    // https://developer.android.google.cn/guide/topics/media/camera.html
//    private boolean checkCameraHardware(Context context) {
//        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
//    }
//
//    // https://developer.android.google.cn/guide/topics/media/camera.html
//
//    /**
//     * A safe way to get an instance of the Camera object.
//     */
//    public static Camera getCameraInstance() {
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//        } catch (Exception e) {
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
//
//}
//
//
//class QRCodeUtil {
//    //部分来自 https://www.jianshu.com/p/b275e818de6a
//
//    /**
//     * 创建二维码位图
//     *
//     * @param content 字符串内容(支持中文)
//     * @param width   位图宽度(单位:px)
//     * @param height  位图高度(单位:px)
//     * @return Bitmap
//     */
//    static Bitmap createQRCodeBitmap(String content, int width, int height) {
//        return createQRCodeBitmap(content, width, height, "UTF-8", "M", "1", Color.BLACK, Color.WHITE);
//    }
//
//    /**
//     * @param content    字符串内容(支持中文)
//     * @param width      位图宽度(单位:px)
//     * @param height     位图高度(单位:px)
//     * @param ec         容错级别
//     * @param margin     空白边距
//     * @param front      前景色的自定义颜色值
//     * @param background 背景色的自定义颜色值
//     * @return Bitmap
//     */
//    static Bitmap createQRCodeBitmap(String content, int width, int height, String ec, String margin, int front, int background) {
//        return createQRCodeBitmap(content, width, height, "UTF-8", ec, margin, front, background);
//    }
//
//    /**
//     * 创建二维码位图 (支持自定义配置和自定义样式)
//     *
//     * @param content          字符串内容
//     * @param width            位图宽度,要求>=0(单位:px)
//     * @param height           位图高度,要求>=0(单位:px)
//     * @param character_set    字符集/字符转码格式。传null时,zxing源码默认使用 "ISO-8859-1"
//     * @param error_correction 容错级别。传null时,zxing源码默认使用 "L"
//     * @param margin           空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
//     * @param color_black      黑色色块的自定义颜色值
//     * @param color_white      白色色块的自定义颜色值
//     * @return Bitmap
//     */
//    private static Bitmap createQRCodeBitmap(String content, int width, int height,
//                                             String character_set, String error_correction, String margin,
//                                             int color_black, int color_white) {
//
//        /* 1.参数合法性判断 */
//        if (TextUtils.isEmpty(content)) { // 字符串内容判空
//            return null;
//        }
//
//        if (width < 0 || height < 0) { // 宽和高都需要>=0
//            return null;
//        }
//
//        try {
//            /* 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
//            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
//
//            if (!TextUtils.isEmpty(character_set)) {
//                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
//            }
//
//            if (!TextUtils.isEmpty(error_correction)) {
//                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction); // 容错级别设置
//            }
//
//            if (!TextUtils.isEmpty(margin)) {
//                hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
//            }
//            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//
//            /* 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
//            int[] pixels = new int[width * height];
//            for (int y = 0; y < height; y++) {
//                for (int x = 0; x < width; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * width + x] = color_black; // 黑色色块像素设置
//                    } else {
//                        pixels[y * width + x] = color_white; // 白色色块像素设置
//                    }
//                }
//            }
//
//            /* 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象 */
//            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//            return bitmap;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    static String decodeQRCodeBitmap(Bitmap qrCode) {
//
//        try {
//
//            int width = qrCode.getWidth();
//            int height = qrCode.getHeight();
//
//            int[] qrCodeImgData = new int[width * height];
//            qrCode.getPixels(qrCodeImgData, 0, width, 0, 0, width, height);
//
//            RGBLuminanceSource rgbLuminanceSource =
//                    new RGBLuminanceSource(width, height, qrCodeImgData);
//
//            Result qrCodeReaderResult =
//                    new QRCodeReader().decode(
//                            new BinaryBitmap(
//                                    new HybridBinarizer(rgbLuminanceSource)
//                            )
//                    );
//
//            return qrCodeReaderResult.getText();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
}

class GZipUtils {
    //参考 http://www.cnblogs.com/whoislcj/p/5473806.html

    /**
     * @param unGzipStr 被压缩字符串
     * @return 压缩后字符串，失败返回 String s=""
     */
    public static String gzipCompress(String unGzipStr) {

        if (TextUtils.isEmpty(unGzipStr)) {
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream);
            gzip.write(unGzipStr.getBytes());
            gzip.close();
            byte[] b = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param gzipStr 已压缩过的 String
     * @return 解压缩后的 String
     */
    public static String gzipDecompress(String gzipStr) {
        if (TextUtils.isEmpty(gzipStr)) {
            return "";
        }
        byte[] t = Base64.decode(gzipStr, Base64.DEFAULT);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(t);
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024 * 1024];
            int n;
            while ((n = gzip.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, n);
            }
            gzip.close();
            in.close();
            out.close();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}