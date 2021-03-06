package action;

import com.android.ddmlib.AndroidDebugBridge;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.android.sdk.AndroidSdkUtils;
import util.CommandHelper;

/**
 * @author warningwang
 */
public class ProfileGPURenderAction extends AnAction {

    private static final String COMMAND_SET_PROP = "setprop";
    private static final String FUNCTION_PROFILE_GPU = "debug.hwui.profile";
    private static final String SWITCH_SHOW = "visual_bars";
    private static final String SWITCH_FALSE = "false";
    private AndroidDebugBridge mAndroidDebugBridge;
    private String mSwitch = SWITCH_FALSE;

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
        String command = COMMAND_SET_PROP + " " + FUNCTION_PROFILE_GPU + " " + mSwitch;
        CommandHelper.executeCommand(mAndroidDebugBridge, command);
    }

    private void toggleSwitch() {
        if (mSwitch.equals(SWITCH_SHOW)) {
            mSwitch = SWITCH_FALSE;
        } else {
            mSwitch = SWITCH_SHOW;
        }
    }

    private AndroidDebugBridge getAndroidDebugBridge(AnActionEvent e) {
        if (mAndroidDebugBridge == null) {
            mAndroidDebugBridge = AndroidSdkUtils.getDebugBridge(e.getProject());
        }
        return mAndroidDebugBridge;
    }
}
