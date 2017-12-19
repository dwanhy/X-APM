package github.tornaco.xposedmoduletest.ui.activity.ag;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import github.tornaco.android.common.Collections;
import github.tornaco.android.common.Consumer;
import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.loader.LockPackageLoader;
import github.tornaco.xposedmoduletest.model.CommonPackageInfo;
import github.tornaco.xposedmoduletest.ui.activity.common.CommonPackageInfoListActivity;
import github.tornaco.xposedmoduletest.ui.adapter.common.CommonPackageInfoAdapter;
import github.tornaco.xposedmoduletest.ui.widget.SwitchBar;
import github.tornaco.xposedmoduletest.xposed.app.XAppGuardManager;

public class GuardAppNavActivity extends CommonPackageInfoListActivity
        implements SwitchBar.OnSwitchChangeListener {

    @Override
    protected boolean isLockNeeded() {
        return true;
    }

    @Override
    protected void onInitSwitchBar(SwitchBar switchBar) {
        switchBar.show();
        switchBar.setChecked(XAppGuardManager.get().isEnabled());
        switchBar.addOnSwitchChangeListener(this);
    }

    @Override
    protected void onRequestClearItemsInBackground() {
        Collections.consumeRemaining(getCommonPackageInfoAdapter().getCommonPackageInfos(),
                new Consumer<CommonPackageInfo>() {
                    @Override
                    public void accept(CommonPackageInfo commonPackageInfo) {
                        if (commonPackageInfo.isChecked()) {
                            XAppGuardManager.get().addOrRemoveLockApps(new String[]{commonPackageInfo.getPkgName()}, false);
                        }
                    }
                });
    }

    @Override
    protected void onRequestPick() {
        GuardAppPickerActivity.start(getActivity());
    }

    @Override
    protected int getSummaryRes() {
        return R.string.summary_app_guard;
    }

    @Override
    protected CommonPackageInfoAdapter onCreateAdapter() {
        return new CommonPackageInfoAdapter(this);
    }

    @Override
    protected List<CommonPackageInfo> performLoading() {
        return LockPackageLoader.Impl.create(this).loadInstalled(false, true);
    }

    @Override
    public void onSwitchChanged(SwitchCompat switchView, boolean isChecked) {
        XAppGuardManager.get().setEnabled(isChecked);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guard_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, GuardSettingsDashboardActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
