package github.tornaco.xposedmoduletest.x.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import de.robv.android.xposed.XposedBridge;
import github.tornaco.xposedmoduletest.x.util.XLog;

/**
 * Created by guohao4 on 2017/10/27.
 * Email: Tornaco@163.com
 */

class XAppGuardServiceImplDev extends XAppGuardServiceImpl {

    private interface Call {
        void onCall() throws Throwable;
    }

    @Override
    protected void enforceCallingPermissions() {
        // super.enforceCallingPermissions();
        XLog.logV("Skip enforce permission on DEV version!");
    }

    @Override
    protected Handler onCreateServiceHandler() {
        final Handler impl = super.onCreateServiceHandler();
        return new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message msg) {
                return makeSafeCall(new Call() {
                    @Override
                    public void onCall() throws Throwable {
                        impl.handleMessage(msg);
                    }
                });
            }
        });
    }

    private boolean makeSafeCall(Call call) {
        try {
            call.onCall();
            return true;
        } catch (Throwable e) {
            onException(e);
            return false;
        }
    }

    private void onException(Throwable e) {
        String logMsg = "XAppGuard-ERROR:"
                + String.valueOf(e) + "\n"
                + Log.getStackTraceString(e);
        XposedBridge.log(logMsg);
        XLog.logD(logMsg);
    }
}