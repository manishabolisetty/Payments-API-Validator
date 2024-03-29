package com.validator.payments.demo.merchant.utils;

import com.validator.payments.api.PaymentResponse;

public class DemoUtil {
    public static void printDemoResponse(PaymentResponse decodedResponse) {
        System.out.println("------------------------------ Response ------------------------------");
        if (decodedResponse != null) {
            System.out.printf("%-25s: %-60s\n", "Status", decodedResponse.getStatus());
            System.out.printf("%-25s: %-60s\n", "Client Code",  decodedResponse.getClientAnswerCode());
            System.out.printf("%-25s: %-60s\n", "code description",  decodedResponse.getAnswerDescription());
            System.out.printf("%-25s: %-60s\n", "Acquirer Code",  decodedResponse.getAcquirerAnswerCode());
            System.out.printf("%-25s: %-60s\n", "Masked Card",  decodedResponse.getMaskedCardNumber());
            System.out.printf("%-25s: %-60s\n", "Payment Method Name",  decodedResponse.getPaymentMethodName());
            System.out.printf("%-25s: %-60s\n", "Payment Slip URL",  decodedResponse.getEftPaymentSlipUrl());
            System.out.printf("%-25s: %-60s\n", "Auth Code",  decodedResponse.getAcquirerAuthCode());
            if (decodedResponse.getTransaction() != null) {
                System.out.printf("%-25s: %-60s\n", "TransactionId",  decodedResponse.getTransaction().getTransactionId());
                System.out.printf("%-25s: %-60s\n", "TransactionDesc",  decodedResponse.getTransaction().getTransactionDesc());
            }
            if (decodedResponse.getTokenizationResult() != null) {
                System.out.printf("%-25s: %-60s\n", "Token",  decodedResponse.getTokenizationResult().getToken());
                System.out.printf("%-25s: %-60s\n", "Token Masked Card",  decodedResponse
                        .getTokenizationResult()
                        .getStoreMaskedCardNumber());
                System.out.printf("%-25s: %-60s\n", "Token expiry date",  decodedResponse
                        .getTokenizationResult()
                        .getStoreExpirationDate());
            }
            System.out.printf("%-25s: %-60s\n", "Currency", decodedResponse.getCurrency());
            System.out.printf("%-25s: %-60s\n", "Order Amount", decodedResponse.getOrderAmount());
            System.out.printf("%-25s: %-60s\n", "Fulfilment Amount", decodedResponse.getFulfilmentAmount());
            System.out.printf("%-25s: %-60s\n", "Captured Amount", decodedResponse.getCapturedAmount());
            System.out.printf("%-25s: %-60s\n", "Refunded Amount", decodedResponse.getRefundedAmount());
            System.out.printf("%-25s: %-60s\n", "Transaction State", decodedResponse.getTransaction().getTransactionState());
        }
    }
}