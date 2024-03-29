package com.validator.payments.demo.merchant;

import com.digitalriver.worldpayments.api.security6.JKSKeyHandlerV6;
import com.validator.payments.api.*;
import com.validator.payments.api.PaymentRequest.StoreFlag;
import com.validator.payments.demo.merchant.utils.DemoConfiguration;
import com.validator.payments.demo.merchant.utils.DemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Demo endpoint that emulates registration of a user and returns an encrypted
 * payload if the user does not already exist.
 * The second call is used by the demo javascript to decrypt the response payload.
 */
@Component
@Path("/demo")
public class RegistrationEndpoint {

    @Autowired
    private DemoConfiguration props;
    
    @POST
    @Path("/registrations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegistrationResponse registerForm(RegistrationData request) {

        // First we check if the user is already registered, and similar.
        boolean alreadyRegistered = false;

        // Initialize the PaymentHandler
        PaymentHandler handler = new PaymentHandler(new JKSKeyHandlerV6(props.keystorePath, props.keystorePwd, props.merchantKeyAlias, props.worldlineKeyAlias), props.worldlineURL);

        // Build the PaymentRequest.
        PaymentRequest details = new PaymentRequestBuilder()
                .setBillingAddressLine1(request.billingAddressLine1)
                .setBillingAddressLine2(request.billingAddressLine2)
                .setBillingCity(request.billingCity)
                .setBillingZipCode(request.billingZipCode)
                .setBillingStateProvince(request.billingStateProvince)
                .setBillingCountryCode(request.billingCountryCode)
                .setBillingEmailAddress(request.billingEmailAddress)
                .setBillingBuyerType(request.billingBuyerType)
                .setBillingFullName(request.billingFullName)
                .setBirthDate(request.birthDate)
                .setBillingMobilePhone(request.billingMobilePhone)
                .setBillingBuyerVATNumber(request.billingBuyerVATNumber)
                .setMid(Long.parseLong(props.merchantId))
                .setPosId(props.posId)
                .setOrderId("Example_order_" + System.currentTimeMillis())
                .setAmount(request.demoAmount)
                .setCurrency(request.demoCurrency)
                .setTransactionChannel(request.demoTransactionChannel)
                .setAutoCapture(request.demoAutoCapture != null && request.demoAutoCapture.equals("on"))
                .setConsumerCountry("US")
                .setConsumerLanguage("en")
                .setAuthorizationType(AuthorizationType.UNDEFINED)
                .setStoreFlag(StoreFlag.valueOf(request.demoTokenization))
                .setShippingAddressLine1(request.shippingAddressLine1)
                .setShippingAddressLine2(request.shippingAddressLine2)
                .setShippingCity(request.shippingCity)
                .setShippingCompanyName(request.shippingCompanyName)
                .setShippingCountryCode(request.shippingCountryCode)
                .setShippingEmailAddress(request.shippingEmailAddress)
                .setShippingFullName(request.shippingFullName)
                .setShippingMobilePhone(request.shippingMobilePhone)
                .setShippingZipCode(request.shippingZipCode)
                .setShippingStateProvince(request.shippingStateProvince)
                .createPaymentRequest();
        
        final String deviceAPIRequest = handler.createDeviceAPIRequest(details);

        // Return the deviceAPIRequest and custom information to the form.
        return new RegistrationResponse(deviceAPIRequest, alreadyRegistered);
    }
    
	@POST
    @Path("/unpackResponse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UnpackedResponse unpackResponse(EncodedResponse encodedReponse) {
        PaymentHandler handler = new PaymentHandler(new JKSKeyHandlerV6(props.keystorePath, props.keystorePwd, props.merchantKeyAlias, props.worldlineKeyAlias), props.worldlineURL);

        PaymentResponse decodedResponse = handler.unpackResponse(encodedReponse.encResponse);

        // The contents of the decodedResponse can be saved in a database
        DemoUtil.printDemoResponse(decodedResponse);

        // Only select fields to be returned to the web page
        UnpackedResponse response = new UnpackedResponse(decodedResponse.getStatus(), decodedResponse.getTransaction() == null ? 0 : decodedResponse
                .getTransaction()
                .getTransactionId(), decodedResponse.getOrderId(), decodedResponse.getPaymentMethodName(), decodedResponse.getEftPaymentSlipUrl());
        return response;
    }
	
	@GET
	@Path("/paymentMethodEndPoint")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPaymentMethodEndPoint(){
		
		 // Initialize the PaymentHandler
        PaymentHandler handler = new PaymentHandler(new JKSKeyHandlerV6(props.keystorePath, props.keystorePwd, props.merchantKeyAlias, props.worldlineKeyAlias), props.worldlineURL);

        // Build the PaymentRequest.
    	PaymentRequest details = new PaymentRequestBuilder()
    			.setMid(Long.parseLong(props.merchantId))
    			.setPosId(props.posId).setCurrency("USD")
    			.setOrderId("Example_order_" + System.currentTimeMillis())
    			.setConsumerCountry("US")
                .setConsumerLanguage("en")
                .createPaymentRequest();
		
		return handler.createDeviceAPIRequest(details);
	} 
	
	@GET
	@Path("/redirectEndPoint")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRedirectEndPoint(){
		StringBuffer baseUrl = new StringBuffer(props.worldlineURL);
		return baseUrl.toString();
	} 

    /**
     * Example response with a custom field like "alreadyRegistered" that illustrates several actions are executed
     * on the server side on the first call:
     * 1. Verifying if the user is already registered, or similar. Hence the example parameter alreadyRegistered.
     * 2. Creation and securing a paymentRequest.
     */
    public static class RegistrationResponse {

        private String deviceAPIRequest;
        private boolean alreadyRegistered;

        RegistrationResponse(String deviceAPIRequest, boolean alreadyRegistered) {
            this.deviceAPIRequest = deviceAPIRequest;
            this.alreadyRegistered = alreadyRegistered;
        }

        public String getDeviceAPIRequest() {
            return deviceAPIRequest;
        }

        public boolean isAlreadyRegistered() {
            return alreadyRegistered;
        }
    }

    public static class EncodedResponse {
        String encResponse;

        public String getEncResponse() {
            return encResponse;
        }

        EncodedResponse() {}
        EncodedResponse(String encResponse) {
            this.encResponse = encResponse;
        }
    }

    public static class UnpackedResponse {
        String status;
        Long transactionId;
        String orderId;
        String paymentMethodName;
        String eftPaymentSlipUrl;


        UnpackedResponse(
                String status, Long transactionId,
                String orderId, String paymentMethodName,String eftPaymentSlipUrl) {
            this.status = status;
            this.transactionId = transactionId;
            this.orderId = orderId;
            this.paymentMethodName = paymentMethodName;
            this.eftPaymentSlipUrl=eftPaymentSlipUrl;
        }

        public String getStatus() {
            return status;
        }

        public Long getTransactionId() {
            return transactionId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getPaymentMethodName() {
            return paymentMethodName;
        }
        public String getEftPaymentSlipUrl() {
        	return eftPaymentSlipUrl;
        }
    }
 }