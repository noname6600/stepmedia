package com.thanhvh.shopmanagement.util;

import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;

/**
 * The interface File util.
 */
public interface FileUtil {

    /**
     * Get byte from file path byte [ ].
     *
     * @param filePath the file path
     * @return the byte [ ]
     */
    static byte[] getByteFromFilePath(String filePath) {
        InputStream inputStream = null;

        byte[] var3;
        try {
            URL url = new URL(filePath);
            inputStream = url.openStream();
            var3 = inputStream.readAllBytes();
            return var3;
        } catch (Exception var13) {
            var3 = new byte[0];
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception var12) {
            }

        }

        return var3;
    }

    /**
     * Gets mime type from file path.
     *
     * @param filePath the file path
     * @return the mime type from file path
     */
    static String getMimeTypeFromFilePath(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(filePath);
    }
}
