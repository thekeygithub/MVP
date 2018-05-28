package com.twimi.apiservice.Util;

import io.ipfs.api.IPFS;

public class IPFSConnction {
    private static IPFS ipfs;

    public static IPFS getIpfs() {
        if (ipfs == null) {
            ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
        }
        return ipfs;
    }
}
