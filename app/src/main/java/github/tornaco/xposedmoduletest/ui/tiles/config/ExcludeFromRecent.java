package github.tornaco.xposedmoduletest.ui.tiles.config;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.newstand.logger.Logger;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;
import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;

/**
 * Created by guohao4 on 2017/11/10.
 * Email: Tornaco@163.com
 */

public class ExcludeFromRecent extends QuickTile {

    public ExcludeFromRecent(final Context context, String pkg) {
        super(context);
        this.titleRes = R.string.title_exclude_from_recent;
        this.iconRes = R.drawable.ic_fiber_smart_record_black_24dp;
        int setting = XAshmanManager.ExcludeRecentSetting.NONE;
        final ComponentName componentName = new ComponentName(pkg, "DUMMY");
        if (XAshmanManager.get().isServiceAvailable()) {
            setting = XAshmanManager.get().getRecentTaskExcludeSetting(componentName);
            if (setting == XAshmanManager.ExcludeRecentSetting.NONE) {
                this.summaryRes = R.string.summary_exclude_recent_none;
            } else if (setting == XAshmanManager.ExcludeRecentSetting.EXCLUDE) {
                this.summaryRes = R.string.summary_exclude_recent_exclude;
            } else if (setting == XAshmanManager.ExcludeRecentSetting.INCLUDE) {
                this.summaryRes = R.string.summary_exclude_recent_include;
            }

        }
        final int[] newSetting = new int[1];
        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);

                String[] source = context.getResources().getStringArray(R.array.exclude_from_recent_settings);

                new AlertDialog.Builder(context)
                        .setSingleChoiceItems(source, XAshmanManager.get().getRecentTaskExcludeSetting(componentName),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        newSetting[0] = which;
                                    }
                                })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logger.d("Set exclude setting: " + newSetting[0]);
                                XAshmanManager.get().setRecentTaskExcludeSetting(componentName, newSetting[0]);

                                if (newSetting[0] == XAshmanManager.ExcludeRecentSetting.NONE) {
                                    getSummaryTextView().setText(R.string.summary_exclude_recent_none);
                                } else if (newSetting[0] == XAshmanManager.ExcludeRecentSetting.EXCLUDE) {
                                    getSummaryTextView().setText(R.string.summary_exclude_recent_exclude);
                                } else if (newSetting[0] == XAshmanManager.ExcludeRecentSetting.INCLUDE) {
                                    getSummaryTextView().setText(R.string.summary_exclude_recent_include);
                                }
                            }
                        })
                        .show();

            }
        };
    }
}
