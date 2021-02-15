package com.kumaraswamy.wifitransmo.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DataUtil {
    public static byte[] readFromFile(String fileName) {
        byte[] bytes;

        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            bytes = new byte[inputStream.available()];

            if(inputStream.read(bytes) > 0) {
                inputStream.close();
                return bytes;
            } else {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void saveBytesToFile(String saveTo, byte[] bytes) {
        try {
            FileOutputStream outputStream = new FileOutputStream(saveTo);
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeFiles(String[] names, String saveTo) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveTo);

            for(String name : names) {
                byte[] bytes = DataUtil.readFromFile(name);
                fileOutputStream.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] makeByteArray(Object[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);

        for(Object object : bytes) {
            byteBuffer.put((byte) (Integer.decode(object.toString()) & 0xFF));
        }

        byte[] data = new byte[bytes.length];
        byteBuffer.put(data);
        return data;
    }
}
