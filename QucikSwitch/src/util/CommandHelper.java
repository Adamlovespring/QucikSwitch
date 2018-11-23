package util;

import com.android.ddmlib.*;

import java.io.*;

/**
 * @author warningwang
 */
public class CommandHelper {

    private static final String COMMAND_REFRESH = "am start -a quick_switch_refresh";
    private static final String NAME_BRIDGE = "com.example.warningwang.quickswitch";
    private static final String COMMEND_CHECK_INSTALL = "pm list packages " + NAME_BRIDGE;

    public static void executeCommand(AndroidDebugBridge androidDebugBridge, String cmd) {
        for (IDevice device : androidDebugBridge.getDevices()) {
            try {
                device.executeShellCommand(COMMEND_CHECK_INSTALL, createCheckInstallListener(cmd, device));
            } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    private static IShellOutputReceiver createCheckInstallListener(String cmd, IDevice device) {
        return new IShellOutputReceiver() {
            private StringBuffer result = new StringBuffer();

            @Override
            public void addOutput(byte[] bytes, int i, int i1) {
                try {
                    result.append(new String(bytes, i, i1, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void flush() {
                try {
                    String resultString = result.toString();
                    if (!resultString.contains(NAME_BRIDGE)) {
                        installEnablerApk(device);
                    }
                    device.executeShellCommand(cmd, sReceiver);
                    device.executeShellCommand(COMMAND_REFRESH, sReceiver);
                } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public boolean isCancelled() {
                return false;
            }
        };
    }

    private static IShellOutputReceiver sReceiver = new IShellOutputReceiver() {
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
    };

    private static void installEnablerApk(IDevice device) throws IOException {
        File tmpfile = File.createTempFile("quick_switch", "apk");
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            is = CommandHelper.class.getResourceAsStream("/quick_switch.apk");
            fos = new FileOutputStream(tmpfile);
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }

        try {
            device.installPackage(tmpfile.getAbsolutePath(), true);
        } catch (InstallException ie) {
            ie.printStackTrace();
        }

    }
}
