package cn.red2;
import android.content.SharedPreferences;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import java.io.IOException;
import java.security.InvalidParameterException;

public class App extends android.app.Application {

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort()
            throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */

            String packageName = getPackageName();
            SharedPreferences sp = getSharedPreferences(packageName + "_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            //mSerialPort = new SerialPort(new File(path), baudrate, 0);

            SerialPort serialPort = SerialPort //
                    .newBuilder(path, baudrate) // 串口地址地址，波特率
                    .parity(2) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
                    .dataBits(7) // 数据位,默认8；可选值为5~8
                    .stopBits(2) // 停止位，默认1；1:1位停止位；2:2位停止位
                    .build();

            mSerialPort = serialPort;
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
