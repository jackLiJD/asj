package com.ald.asjauthlib.authframework.core.protocol.data;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 15:27
 * 描述： 分享data
 * 修订历史：
 */
public class ShareData {
    private String shareData = "";
    private String shareChannel;

    public ShareData(String shareChannel, String shareData) {
        this.shareChannel = shareChannel;
        this.shareData = shareData;
    }

    public String getShareData() {
        return shareData;
    }

    public String getShareChannel() {
        return shareChannel;
    }
}
