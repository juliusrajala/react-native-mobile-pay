// Created by Mads Lee Jensen on 04/06/2017.
// Updated by Julius Rajala on 01.02.2020
// Copyright (c) 2017 Facebook. All rights reserved.

#import "RnMobilePay.h"
#import "MobilePayManager.h"
#import "RnMobilePayHandler.h"


@implementation RnMobilePay {
}

// https://reactnative.dev/docs/native-modules-ios.html
RCT_EXPORT_MODULE();

// only allow code to be run on main-queue
- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(setup:(NSString *)merchantId country:(MobilePayCountry)country merchantUrlScheme:(NSString *)merchantUrlScheme)
{
    [[RnMobilePayHandler sharedInstance] setup:merchantId country:country merchantUrlScheme:merchantUrlScheme];
}

RCT_EXPORT_METHOD(createPayment:(NSString *)orderId productPrice:(float)productPrice resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    [[RnMobilePayHandler sharedInstance] createPayment:orderId productPrice:productPrice resolve:resolve reject:reject];
}

RCT_EXPORT_METHOD(setTimeoutSeconds:(int)seconds)
{
    [[MobilePayManager sharedInstance] setTimeoutSeconds:seconds];
}

RCT_EXPORT_METHOD(setCountry:(MobilePayCountry)county)
{
    [[RnMobilePayHandler sharedInstance] setCountry:county];
}

RCT_EXPORT_METHOD(setCaptureType:(MobilePayCaptureType)captureType)
{
    [[MobilePayManager sharedInstance] setCaptureType:captureType];
}

RCT_EXPORT_METHOD(setMerchantId:(NSString *)merchantId)
{
    [[RnMobilePayHandler sharedInstance] setMerchantId:merchantId];
}

- (NSDictionary *)constantsToExport {
    return @{
      @"CAPTURE_TYPE_RESERVE": @(MobilePayCaptureType_Reserve),
      @"CAPTURE_TYPE_PARTIALCAPTURE": @(MobilePayCaptureType_PartialCapture),

      @"COUNTRY_DENMARK": @(MobilePayCountry_Denmark),
      @"COUNTRY_FINLAND": @(MobilePayCountry_Finland),

      @"isMobilePayInstalledDenmark": @([[MobilePayManager sharedInstance] isMobilePayInstalled:MobilePayCountry_Denmark]),
      @"isMobilePayInstalledFinland": @([[MobilePayManager sharedInstance] isMobilePayInstalled:MobilePayCountry_Finland]),
    };
}

@end
