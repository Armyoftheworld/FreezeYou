package cf.playhi.freezeyou;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cf.playhi.freezeyou.app.FreezeYouBaseActivity;

import static cf.playhi.freezeyou.ThemeUtils.processActionBar;
import static cf.playhi.freezeyou.ThemeUtils.processSetTheme;
import static cf.playhi.freezeyou.VersionUtils.checkUpdate;
import static cf.playhi.freezeyou.VersionUtils.getVersionCode;
import static cf.playhi.freezeyou.VersionUtils.getVersionName;
import static cf.playhi.freezeyou.utils.MoreUtils.joinQQGroup;
import static cf.playhi.freezeyou.utils.MoreUtils.requestOpenDonateWebSite;
import static cf.playhi.freezeyou.utils.MoreUtils.requestOpenWebSite;
import static cf.playhi.freezeyou.utils.ToastUtils.showToast;

public class AboutActivity extends FreezeYouBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        processSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        processActionBar(getActionBar());

        final Activity activity = AboutActivity.this;

        TextView aboutSlogan = findViewById(R.id.about_slogan);

        final boolean googleVersion = getVersionName(activity).contains("g");

        final String[] aboutData =
                googleVersion
                        ?
                        new String[]{
                                getResources().getString(R.string.hToUse),
                                getResources().getString(R.string.faq),
                                getResources().getString(R.string.helpTranslate),
                                getResources().getString(R.string.thanksList),
                                getResources().getString(R.string.visitWebsite),
                                getResources().getString(R.string.contactUs),
                                getResources().getString(R.string.update),
                                getResources().getString(R.string.thirdPartyOpenSourceLicenses),
                                "V" + getVersionName(getApplicationContext()) + "(" + getVersionCode(getApplicationContext()) + ")"
                        }
                        :
                        new String[]{
                                getResources().getString(R.string.hToUse),
                                getResources().getString(R.string.faq),
                                getResources().getString(R.string.helpTranslate),
                                getResources().getString(R.string.thanksList),
                                getResources().getString(R.string.visitWebsite),
                                getResources().getString(R.string.contactUs),
                                getResources().getString(R.string.update),
                                getResources().getString(R.string.donate),
                                getResources().getString(R.string.thirdPartyOpenSourceLicenses),
                                "V" + getVersionName(getApplicationContext()) + "(" + getVersionCode(getApplicationContext()) + ")"
                        };

        ListView aboutListView = findViewById(R.id.about_listView);

        aboutListView.setAdapter(new ArrayAdapter<>(AboutActivity.this, android.R.layout.simple_list_item_1, aboutData));

        aboutListView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    requestOpenWebSite(activity, "https://www.zidon.net/zh-CN/guide/how-to-use.html");
                    break;
                case 1:
                    requestOpenWebSite(activity, "https://www.zidon.net/zh-CN/faq/");
                    break;
                case 2:
                    requestOpenWebSite(
                            activity,
                            "https://github.com/FreezeYou/FreezeYou/blob/master/README_Translation.md"
                    );
                    break;
                case 3:
                    requestOpenWebSite(activity, "https://freezeyou.playhi.net/thanks.html");
                    break;
                case 4:
                    requestOpenWebSite(activity, "https://www.zidon.net");
                    break;
                case 5:
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(
                            String.format(getString(R.string.email_colon), "contact@zidon.net")
                                    + System.getProperty("line.separator")
                                    + String.format(getString(R.string.telegramGroup_colon), "t.me/FreezeYou")
                                    + System.getProperty("line.separator")
                                    + String.format(getString(R.string.qqGroup_colon), "704086494")
                    );
                    builder.setTitle(R.string.contactUs);
                    builder.setPositiveButton(R.string.okay, null);
                    builder.setNeutralButton(
                            R.string.addQQGroup, (dialog, which) -> joinQQGroup(activity)
                    );
                    builder.show();
                    break;
                case 6:
                    checkUpdate(activity);
                    break;
                case 7:
                    if (googleVersion) {
                        requestOpenWebSite(activity, "https://freezeyou.playhi.net/ThirdPartyOpenSourceLicenses.html");
                    } else {
                        requestOpenDonateWebSite(activity);
                    }
                    break;
                case 8:
                    if (googleVersion) {
                        showToast(activity, "V" + getVersionName(activity) + "(" + getVersionCode(activity) + ")");
                    } else {
                        requestOpenWebSite(activity, "https://freezeyou.playhi.net/ThirdPartyOpenSourceLicenses.html");
                    }
                    break;
                case 9:
                    showToast(activity, "V" + getVersionName(activity) + "(" + getVersionCode(activity) + ")");
                    break;
                default:
                    break;
            }
        });

        aboutSlogan.setText(String.format("V %s", getVersionCode(activity)));
        aboutSlogan.setOnClickListener(v ->
                requestOpenWebSite(activity, "https://www.zidon.net/zh-CN/changelog/")
        );

        TextView about_appName = findViewById(R.id.about_appName);
        about_appName.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(String.format(getString(R.string.welcomeToUseAppName), getString(R.string.app_name)));
            builder.setIcon(R.mipmap.ic_launcher_new_round);
            builder.setMessage(String.format(getString(R.string.welcomeToUseAppName), getString(R.string.app_name)));
            builder.setNegativeButton(R.string.importConfig, (dialogInterface, i) ->
                    startActivity(
                            new Intent(getApplicationContext(), BackupMainActivity.class)
                    )
            );
            builder.setPositiveButton(R.string.quickSetup, (dialogInterface, i) ->
                    startActivity(
                            new Intent(getApplicationContext(), FirstTimeSetupActivity.class)
                    )
            );
            builder.setNeutralButton(R.string.okay, null);
            builder.create().show();
        });
    }

}
