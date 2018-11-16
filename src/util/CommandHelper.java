package util;

import com.android.ddmlib.*;

import java.io.IOException;

/**
 * @author warningwang
 */
public class CommandHelper {

    private static final String COMMAND_REFRESH = "am start -a POKESYSPROPS";

    public static void executeCommand(AndroidDebugBridge androidDebugBridge, String cmd, boolean isRefresh, IShellOutputReceiver receiver) {
        for (IDevice device : androidDebugBridge.getDevices()) {
            try {
                device.executeShellCommand(cmd, receiver);
                if (isRefresh) {
                    device.executeShellCommand(COMMAND_REFRESH, receiver);
                }
            } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public static void executeCommandWithoutResponse(AndroidDebugBridge androidDebugBridge, String cmd, boolean isRefresh) {
        executeCommand(androidDebugBridge, cmd, isRefresh, new IShellOutputReceiver() {
            @Override
            public void addOutput(byte[] bytes, int i, int i1) {

            }

            @Override
            public void flush() {

            }

            @Override
            public boolean isCancelled() {
                return false;
            }
        });
    }

}
