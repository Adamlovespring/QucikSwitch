package action;

import com.android.ddmlib.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.android.sdk.AndroidSdkUtils;
import util.CommandHelper;

/**
 * @author warningwang
 */
public class ShowLayoutBoundsAction extends AnAction {

    private static final String COMMAND_SET_PROP = "setprop";
    private static final String FUNCTION_SHOW_LAYOUT_BOUNDS = "debug.layout";
    private AndroidDebugBridge mAndroidDebugBridge;
    private boolean mSwitch = false;

    @Override
    public void actionPerformed(AnActionEvent e) {
        init(e);
        handleAction();
    }

    private void init(AnActionEvent e) {
        mAndroidDebugBridge = getAndroidDebugBridge(e);
    }

    private void handleAction() {
        toggleSwitch();
        String command = COMMAND_SET_PROP + " " + FUNCTION_SHOW_LAYOUT_BOUNDS + " " + mSwitch;
        CommandHelper.executeCommandWithoutResponse(mAndroidDebugBridge, command, true);
    }

    private void toggleSwitch() {
        mSwitch = !mSwitch;
    }

    private AndroidDebugBridge getAndroidDebugBridge(AnActionEvent e) {
        if (mAndroidDebugBridge == null) {
            mAndroidDebugBridge = AndroidSdkUtils.getDebugBridge(e.getProject());
        }
        return mAndroidDebugBridge;
    }
}
