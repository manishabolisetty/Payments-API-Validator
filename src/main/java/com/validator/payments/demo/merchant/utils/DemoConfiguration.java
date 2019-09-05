package com.validator.payments.demo.merchant.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfiguration {

    @Value("${validator.api.keystore.path}")
    public String keystorePath;

    @Value("${validator.api.keystore.pwd}")
    public String keystorePwd;

    @Value("${validator.api.key.merchantkeyalias}")
    public String merchantKeyAlias = "merchant";

    @Value("${validator.api.key.validatorkeyalias}")
    public String validatorKeyAlias = "ngcert";

    @Value("${validator.api.merchantId}")
    public String merchantId;

    @Value("${validator.api.posId}")
    public String posId;

    @Value("${validator.api.url}")
    public String validatorURL;

}