package com.binar.byteacademy.common.util;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.service.impl.MidtransSnapApiImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MidtransUtil {

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Value("${midtrans.client-key}")
    private String clientKey;

    public MidtransCoreApi getCoreApi() {
        Config configOptions = createConfig();
        return new ConfigFactory(configOptions).getCoreApi();
    }

    public MidtransSnapApiImpl getSnapApi() {
        Config configOptions = createConfig();
        return new ConfigFactory(configOptions).getSnapApi();
    }

    private Config createConfig() {
        return Config.builder()
                .setServerKey(serverKey)
                .setClientKey(clientKey)
                .setIsProduction(false)
                .build();
    }
}

