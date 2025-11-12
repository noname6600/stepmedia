package com.thanhvh.util;

import com.thanhvh.util.constant.Constants;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public interface HastUtils {
    static String hash(String data) {
        return DigestUtils.sha256Hex(data);
    }

    static String hash(String... data) {
        List<String> hashData = List.of(data);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= hashData.size() - 1; i++) {
            stringBuilder.append(hashData.get(i)).append(Constants.HASHTAG);
        }
        stringBuilder.append(hashData.get(hashData.size() - 1));
        return hash(stringBuilder.toString());
    }
}
