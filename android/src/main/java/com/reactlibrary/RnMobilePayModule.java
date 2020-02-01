// Original created by Mads Lee Jensen on 04/06/2017.
// Updated by Julius Rajala on 29.01.2020
// Copyright (c) 2017 Facebook. All rights reserved.

package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import com.facebook.react.bridge.*;
import dk.mobilepay.sdk.CaptureType;
import dk.mobilepay.sdk.Country;
import dk.mobilepay.sdk.MobilePay;
import dk.mobilepay.sdk.ResultCallback;
import dk.mobilepay.sdk.model.FailureResult;
import dk.mobilepay.sdk.model.Payment;
import dk.mobilepay.sdk.model.SuccessResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class RnMobilePayModule extends ReactContextBaseJavaModule {

    private static final int MOBILEPAY_PAYMENT_REQUEST_CODE = 1001;
    private boolean mHasBeenSetup = false;
    private String mMerchantId = "APPFI0000000000";
    private Country mCountry = Country.FINLAND;
    private int mReturnSeconds = 1;
    private int mTimeoutSeconds = 90;
    private CaptureType mCaptureType = CaptureType.RESERVE;
    private Promise mPaymentPromise;
    private Payment mPayment;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == MOBILEPAY_PAYMENT_REQUEST_CODE) {
                MobilePay.getInstance().handleResult(resultCode, data, new ResultCallback() {
                    @Override
                    public void onSuccess(SuccessResult result) {
                        // The payment succeeded - you can deliver the product.
                        WritableMap map = Arguments.createMap();
                        map.putBoolean("isCancelled", false);
                        map.putString("orderId", result.getOrderId());
                        map.putString("transactionId", result.getTransactionId());
                        map.putString("transactionSignature", result.getSignature());
                        map.putDouble("amountWithdrawnFromCard", result.getAmountWithdrawnFromCard().doubleValue());

                        mPaymentPromise.resolve(map);

                        cleanUp();
                    }

                    @Override
                    public void onFailure(FailureResult result) {
                        // The payment failed - show an appropriate error message to the user. Consult
                        // the MobilePay class documentation for possible error codes.
                        mPaymentPromise.reject(Integer.toString(result.getErrorCode()), result.getErrorMessage());

                        cleanUp();
                    }

                    @Override
                    public void onCancel(String orderId) {
                        // The payment was cancelled.

                        WritableMap map = Arguments.createMap();
                        map.putBoolean("isCancelled", true);
                        map.putString("orderId", orderId);

                        mPaymentPromise.resolve(map);

                        cleanUp();
                    }
                });
            }
        }
    };

    public RnMobilePayModule(ReactApplicationContext reactContext) {
        super(reactContext);

        reactContext.addActivityEventListener(mActivityEventListener);
    }

    private void cleanUp() {
        mPaymentPromise = null;
        mPayment = null;
    }

    @Override
    public String getName() {
        return "RNMobilePay";
    }

    @ReactMethod
    public void setup(String merchantId, String country, String merchantUrlScheme) {
        mMerchantId = merchantId;

        setCountry(country);

        mHasBeenSetup = true;
    }

    @ReactMethod
    public void createPayment(String orderId, Double productPrice, Promise promise) {
        if (!mHasBeenSetup) {
            promise.reject("-1",
                    "MobilePay has not been setup. Please call setup(merchantId, country, merchantUrlScheme) first.");
        }

        // Test if this works without the hack.
        MobilePay mpInstance = MobilePay.getInstance();
        mpInstance.init(mMerchantId, mCountry);
        mpInstance.setCaptureType(mCaptureType);
        mpInstance.setTimeoutSeconds(mTimeoutSeconds);

        mPaymentPromise = promise;

        mPayment = new Payment();
        mPayment.setProductPrice(new BigDecimal(productPrice));
        mPayment.setOrderId(orderId);

        // Create a payment Intent using the Payment object from above.
        Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(mPayment);

        // We now jump to MobilePay to complete the transaction. Start MobilePay and
        // wait for the result using an unique result code of your choice.
        getCurrentActivity().startActivityForResult(paymentIntent, MOBILEPAY_PAYMENT_REQUEST_CODE);
    }

    @ReactMethod
    public void setTimeoutSeconds(int seconds) {
        mTimeoutSeconds = seconds;
    }

    @ReactMethod
    public void setReturnSeconds(int seconds) {
        mReturnSeconds = seconds;
    }

    @ReactMethod
    public void setCaptureType(String captureType) {
        mCaptureType = CaptureType.valueOf(captureType);
    }

    @ReactMethod
    public void setCountry(String country) {
        mCountry = Country.valueOf(country);
    }

    @ReactMethod
    public void setMerchantId(String merchantId) {
        mMerchantId = merchantId;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        constants.put("CAPTURE_TYPE_RESERVE", CaptureType.RESERVE.name());
        constants.put("CAPTURE_TYPE_PARTIAL", CaptureType.PARTIAL_CAPTURE.name());

        constants.put("COUNTRY_DENMARK", Country.DENMARK.name());
        constants.put("COUNTRY_FINLAND", Country.FINLAND.name());

        constants.put("isMobilePayInstalledDenmark",
                MobilePay.getInstance().isMobilePayInstalled(getReactApplicationContext(), Country.DENMARK));
        constants.put("isMobilePayInstalledFinland",
                MobilePay.getInstance().isMobilePayInstalled(getReactApplicationContext(), Country.FINLAND));
        return constants;
    }

}
