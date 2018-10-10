 //
//  easyflyNetWork.m
//  easyflyDemo
//
//  Created by hehe on 15/5/26.
//  Copyright (c) 2015年 hehe. All rights reserved.
//

#import "CrazyNetWork.h"
#import "CrazyMemoryCache.h"
#import "CrazyCache.h"
#import "CrazyRequestSerialization.h"
#import "JWT.h"
#import "SVProgressHUD.h"
#import <CFNetwork/CFNetwork.h>

static CrazyNetWork * NetWork = nil;
@implementation CrazyNetWork

+(CrazyNetWork *)share{
    
    NetWork =[[CrazyNetWork alloc]init];
    return NetWork;
}

//网络请求
+(CrazyNetWork *)CrazyRequest_Get:(NSString *)header HUD:(BOOL)HUD parameters:(NSDictionary *)parameters success:(requestSuccess)success fail:(requestFail)fail{
    
    if (HUD == YES) {
        [SVProgressHUD show];
    }
    
    CrazyNetWork * NetWork = [CrazyNetWork share];
    NetWork.block_fail = fail;
    NetWork.block_success = success;
    

    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NetWork.manager = [NSURLSession sessionWithConfiguration:config delegate:NetWork delegateQueue:nil];
    
    
    NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:header]];
    [request setHTTPMethod:@"GET"];
    [request setTimeoutInterval:requstTimeOut];
    
   // NSDictionary * JWTDic = [CrazyNetWork parametersSecret:parameters];
    
    NSString * bodyStr = [CrazyNetWork requestBody: parameters];
    bodyStr = [CrazyNetWork HTMLfilter:bodyStr];
    
    request.URL = [NSURL URLWithString: [NSString stringWithFormat:@"%@?%@",header,bodyStr] ];
    NSURLSessionDataTask *task = [NetWork.manager dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        if (error == nil) {
            
            NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];//转换数据格式
            
            NSString * json = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
            
            NSString *url = [response.URL absoluteString];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
              NetWork.block_success(dic,url,json);
              [SVProgressHUD dismiss];
                
            });
            
        }else{
            
            if (NetWork.block_fail) {
                dispatch_async(dispatch_get_main_queue(), ^{
                  
                  [JKToast showWithText:@"连接服务器异常，请刷新重试"];
                  NetWork.block_fail(error,[task.originalRequest.URL absoluteString]);
                  [SVProgressHUD dismiss];
                    
                });
                
            }
            
        }
        
    }];
    
    [task resume];

    
//    if (NetWork.block_Progress) {
//        NetWork.block_Progress(downloadProgress);
//    }
    
   return NetWork ;
}

//post 请求大数据量信息
+(CrazyNetWork *)CrazyRequest_Post:(NSString *)header HUD:(BOOL)HUD parameters:(NSDictionary *)parameters success:(requestSuccess)success fail:(requestFail)fail{
    
    if (HUD == YES) {
        [SVProgressHUD show];
    }
    
    CrazyNetWork * NetWork = [CrazyNetWork share];
    
    NetWork.block_fail = fail;
    NetWork.block_success = success;
    
    NetWork.manager = [NSURLSession sharedSession];
    
    NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:header]];
    [request setHTTPMethod:@"POST"];
    [request setTimeoutInterval:requstTimeOut];
    
//    NSDictionary * JWTDic = [CrazyNetWork parametersSecret:parameters];
    
    NSString * bodyStr = [CrazyNetWork requestBody:parameters];
    bodyStr = [CrazyNetWork HTMLfilter:bodyStr];
    
    NSData *bodyData = [bodyStr dataUsingEncoding:NSUTF8StringEncoding];
    [request setHTTPBody:bodyData];
    
    NSURLSessionDataTask *task = [NetWork.manager dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        if (error == nil) {
            
            NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];//转换数据格式
            
            NSString * json = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
            
            NSString *url = [response.URL absoluteString];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
              NetWork.block_success(dic,url,json);
              [SVProgressHUD dismiss];
                
            });
            
        }else{
            
            if (NetWork.block_fail) {
                dispatch_async(dispatch_get_main_queue(), ^{
                [JKToast showWithText:@"连接服务器异常，请刷新重试"];
                NetWork.block_fail(error,[task.originalRequest.URL absoluteString]);
                });
                [SVProgressHUD dismiss];
                
            }
            
        }
        
    }];
    
    [task resume];

//    if (NetWork.block_Progress) {
//        NetWork.block_Progress(uploadProgress);
//    }
    
    return NetWork ;
}


//文件流上传
+(CrazyNetWork *)CrazyHttpFileUpload:(NSString *)headUrl HUD:(BOOL)HUD parameters:(NSDictionary *)parameters imageArr:(NSArray *)imageArr scale:(float)scale  nameArr:(NSArray*)nameArr fileNameArr:(NSArray *)fileNameArr mimeType:(NSString *)mimeType block:(requestSuccess)success fail:(requestFail)fail{

    if (HUD == YES) {
        [SVProgressHUD show];
    }
    
    CrazyNetWork * network = [CrazyNetWork share];
    NetWork.manager = [NSURLSession sharedSession];
    
    if (scale >1 || scale==0) {
        scale = 0.5;
    }
    
    NSMutableURLRequest *request = [[CrazyHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:headUrl parameters:parameters constructingBodyWithBlock:^(id<CrazyMultipartFormData> formData) {
        
        if (imageArr.count != nameArr.count && imageArr.count != fileNameArr.count) {
            NSLog(@"文件名数组和后台数组和图片数组 数量不统一 ！！！！！");
            return ;
        }
        
        for (int i = 0; i<imageArr.count; i++) {
            UIImage *image = imageArr[i];
            NSString *name = nameArr[i];
            NSString *fileName = fileNameArr[i];
            
            if([image isKindOfClass:[UIImage class]]){
                
                NSData *imageData= UIImageJPEGRepresentation(image, scale);
                [formData appendPartWithFileData:imageData name:name fileName:fileName mimeType:mimeType];
            }
            
        }
        
    } error:nil];
    

    NSURLSessionUploadTask *dataTask=[NetWork.manager uploadTaskWithRequest:request fromData:nil completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        if (error == nil) {
            
            NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];//转换数据格式
            dispatch_async(dispatch_get_main_queue(), ^{
              success(dic,[response.URL absoluteString],@"");
              [SVProgressHUD dismiss];
            });
            
        }else{
            dispatch_async(dispatch_get_main_queue(), ^{
              fail(error,[response.URL absoluteString]);
              [SVProgressHUD dismiss];
            });
        }
    }];
    
    
    [dataTask resume];
    
    
//    if (network.block_Progress) {
//        network.block_Progress(uploadProgress);
//    }
    
    return network;

}

//base64上传
+(CrazyNetWork *)CrazyHttpBase64Upload:(NSString *)headUrl HUD:(BOOL)HUD imageDic:(NSDictionary *)imgBase64Dic parameters:(NSDictionary *)parameters block:(requestSuccess)success fail:(requestFail)fail{
    
    NSMutableDictionary *postDic = [[NSMutableDictionary alloc]init];
    for (int i = 0; i<imgBase64Dic.allKeys.count; i++) {
        
        NSString *key = imgBase64Dic.allKeys[i];
        NSString *base64 = imgBase64Dic[key];
        
        [postDic setObject:base64 forKey:key];
    }
    
    for (int i = 0; i<parameters.allKeys.count; i++) {
        
        NSString *key = parameters.allKeys[i];
        UIImage *value = parameters[key];

        [postDic setObject:value forKey:key];
    }
    
   CrazyNetWork *request =  [CrazyNetWork CrazyTokenHeadRequest_Post:headUrl HUD:HUD parameters:postDic success:^(NSDictionary *dic, NSString *url,NSString *Json) {
       
       dispatch_async(dispatch_get_main_queue(), ^{
         success(dic,url,Json);
       });
       
   } fail:^(NSError *error, NSString *url) {
       
       dispatch_async(dispatch_get_main_queue(), ^{
         fail(error,url);
       });
   }];
    
    return request;

}
//返回base64字符串  scale 默认0.5
+(NSString *)CrazyBase64Str:(UIImage *)img scale:(float)scale{
    if (scale ==0 || scale > 1) {
         scale = 0.5;
    }
   
    NSData* imageData = UIImageJPEGRepresentation(img, scale);
    NSString *strBase64 = [imageData base64EncodedStringWithOptions:0];
    NSString *outputStr = [CrazyNetWork HTMLfilter:strBase64];
    return outputStr;
}
+(NSString *)HTMLfilter:(NSString *)urlStr{
    
    NSMutableString *outputStr = [NSMutableString stringWithString:urlStr];
    [outputStr replaceOccurrencesOfString:@"+"
                               withString:@"%2B"
                                  options:NSLiteralSearch
                                    range:NSMakeRange(0, [outputStr length])];
    return outputStr;
}

//网络缓存请求
+(void)CrazyRequestCache_Get:(NSString *)header parameters:(NSDictionary *)parameters success:(requestSuccess)success fail:(requestFail)fail{
    NSString *requestUrl = [CrazyNetWork requestURl:header parameters:parameters];
    [[CrazyCache sharedCache]objectForKey:requestUrl block:^(CrazyCache *cache, NSString *key, id object) {
        NSDictionary * dic = (NSDictionary *)object;
        if (dic.allKeys >0) {
            success(dic,requestUrl,@"");
        }else{
            
            
            [CrazyNetWork CrazyRequest_Get:header HUD:NO parameters:parameters success:^(NSDictionary *dic, NSString *url,NSString *Json) {
                [[CrazyCache sharedCache]setObject:dic forKey:requestUrl];
                
                dispatch_async(dispatch_get_main_queue(), ^{
                 success(dic,url,Json);
                });
                
            } fail:^(NSError *error, NSString *url) {
                
                fail(error,url);
            }];
        }
    }];
    
}

//网络POST缓存请求  大数据量
+(void)CrazyRequestCache_Post:(NSString *)header parameters:(NSDictionary *)parameters success:(requestSuccess)success fail:(requestFail)fail{
    
    NSString *requestUrl = [CrazyNetWork requestURl:header parameters:parameters];
    
    [[CrazyCache sharedCache]objectForKey:requestUrl block:^(CrazyCache *cache, NSString *key, id object) {
        NSDictionary * dic = (NSDictionary *)object;
        if (dic.allKeys >0) {
            success(dic,requestUrl,@"");
        }else{
            
            [CrazyNetWork CrazyRequest_Post:header HUD:NO parameters:parameters success:^(NSDictionary *dic, NSString *url,NSString *Json) {
                [[CrazyCache sharedCache]setObject:dic forKey:requestUrl];
                
                success(dic,url,Json);
                
            } fail:^(NSError *error, NSString *url) {
                fail(error,url);
            }];
        }
    }];
    
}
+(void)CrazyRequestCache_removeKey:(NSString *)key{
    [[CrazyCache sharedCache]removeObjectForKey:key];
}
+(void)CrazyRequestCache_removeAll{
    [[CrazyCache sharedCache]removeAllObjects];
}

+(NSString *)requestBody:(NSDictionary *)parameters{
    
    NSMutableString * bodyStr = [[NSMutableString alloc]init];
    for (int i = 0 ; i<parameters.allKeys.count ; i++) {
        NSString *key = parameters.allKeys[i];
        
        if (parameters[key] == nil) {
            [bodyStr appendFormat:@"%@=%@",key,@""];
        }else{
            [bodyStr appendFormat:@"%@=%@",key,parameters[key]];
        }
        if (parameters.allKeys.count-1 > i) {
            [bodyStr appendString:@"&"];
        }
    }
    
    return bodyStr;
}

+(NSString *)requestURl:(NSString*)header parameters:(NSDictionary *)parameters{
    if (parameters.allKeys == 0 && parameters == nil) {
        return header;
    }else {
        NSMutableString *requstStr = [[NSMutableString alloc]initWithString:header];
        [requstStr appendString:@"?"];
        NSArray * keys = parameters.allKeys;
        for (int i = 0 ; i < keys.count; i++) {
            NSString *key  = keys[i];
            [requstStr appendFormat:@"%@=%@&",key,parameters[key]];
        }
        [requstStr deleteCharactersInRange:NSMakeRange(requstStr.length -1, 1)];
        
        return requstStr;
    }
}

#pragma mark URLSession delegate
- (void)URLSession:(NSURLSession *)session task:(NSURLSessionTask *)task
didReceiveChallenge:(NSURLAuthenticationChallenge *)challenge
 completionHandler:(void (^)(NSURLSessionAuthChallengeDisposition disposition, NSURLCredential * __nullable credential))completionHandler {
    
        
    NSString *method = challenge.protectionSpace.authenticationMethod;
//    NSLog(@"%@", method);
    
    if([method isEqualToString:NSURLAuthenticationMethodServerTrust]){
        
        NSString *host = challenge.protectionSpace.host;
        NSLog(@"%@", host);
        
        NSURLCredential *credential = [NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust];
        completionHandler(NSURLSessionAuthChallengeUseCredential, credential);
        return;
    }
    
    NSString *thePath = [[NSBundle mainBundle] pathForResource:cerNAME ofType:@"p12"];
    NSData *PKCS12Data = [[NSData alloc] initWithContentsOfFile:thePath];
   
    CFDataRef inPKCS12Data = (CFDataRef)CFBridgingRetain(PKCS12Data);
    SecIdentityRef identity;
    
    // 读取p12证书中的内容
    OSStatus result = [self extractP12Data:inPKCS12Data toIdentity:&identity];
    if(result != errSecSuccess){
        completionHandler(NSURLSessionAuthChallengeCancelAuthenticationChallenge, nil);
        return;
    }
    
    SecCertificateRef certificate = NULL;
    SecIdentityCopyCertificate (identity, &certificate);
    
    const void *certs[] = {certificate};
    CFArrayRef certArray = CFArrayCreate(kCFAllocatorDefault, certs, 1, NULL);
    
    NSURLCredential *credential = [NSURLCredential credentialWithIdentity:identity certificates:(NSArray*)CFBridgingRelease(certArray) persistence:NSURLCredentialPersistencePermanent];
    
    completionHandler(NSURLSessionAuthChallengeUseCredential, credential);
}

-(OSStatus) extractP12Data:(CFDataRef)inP12Data toIdentity:(SecIdentityRef*)identity {
    
    OSStatus securityError = errSecSuccess;
    
    CFStringRef password = CFSTR(cerPWD);
    const void *keys[] = { kSecImportExportPassphrase };
    const void *values[] = { password };
    
    CFDictionaryRef options = CFDictionaryCreate(NULL, keys, values, 1, NULL, NULL);
    
    CFArrayRef items = CFArrayCreate(NULL, 0, 0, NULL);
    securityError = SecPKCS12Import(inP12Data, options, &items);
    
    if (securityError == 0) {
        CFDictionaryRef ident = CFArrayGetValueAtIndex(items,0);
        const void *tempIdentity = NULL;
        tempIdentity = CFDictionaryGetValue(ident, kSecImportItemIdentity);
        *identity = (SecIdentityRef)tempIdentity;
    }
    
    if (options) {
        CFRelease(options);
    }
    
    return securityError;
}


+(NSDictionary *)parametersSecret:(NSDictionary *)parameters{
    NSMutableDictionary *mutableDic = [[NSMutableDictionary alloc]initWithDictionary:parameters];
    
    if ([parameters.allKeys containsObject:@"JWT"]) {
        if ([parameters[@"JWT"] isEqualToString:@"YES"]) {
            NSString *jwt = [CrazyNetWork JWTSecret];
            [mutableDic setObject:jwt forKey:JWT_access_token];
        }
        
        [mutableDic removeObjectForKey:@"JWT"];
        
    } else if(ISJWT == 1){
        NSString *jwt = [CrazyNetWork JWTSecret];
        [mutableDic setObject:jwt forKey:JWT_access_token];
    }

    return mutableDic;
}

+(NSString *)JWTSecret{
    
    JWTClaimsSet *claimsSet = [[JWTClaimsSet alloc] init];
    claimsSet.issuer = @"Facebook";
    claimsSet.subject = @"Token";
    claimsSet.audience = @"http://yourkarma.com";
    claimsSet.expirationDate = [NSDate dateWithTimeIntervalSinceNow:requstTimeOut];//[NSDate distantFuture];
    claimsSet.notBeforeDate =  [NSDate dateWithTimeIntervalSinceNow:-10];
    claimsSet.issuedAt = [NSDate date];
    claimsSet.identifier = IDENTIFIER;
    claimsSet.type = @"JWT";
    
    
    id<JWTAlgorithm> algorithm1 = [JWTAlgorithmFactory algorithmByName:@"HS256"];//HS256
    
    NSString *builder2 = [JWTBuilder encodeClaimsSet:claimsSet].secret(JWTSECRET).algorithm(algorithm1).encode;
    
    return builder2;
}

+ (BOOL)getProxyStatus {
    
    NSDictionary *proxySettings = CFBridgingRelease((__bridge CFTypeRef _Nullable)((__bridge NSDictionary *)CFNetworkCopySystemProxySettings()));
    NSArray *proxies = CFBridgingRelease((__bridge CFTypeRef _Nullable)((__bridge NSArray *)CFNetworkCopyProxiesForURL((__bridge CFURLRef)[NSURL URLWithString:@"https://www.baidu.com"], (__bridge CFDictionaryRef)proxySettings)));
    NSDictionary *settings = [proxies objectAtIndex:0];
    
    NSLog(@"host=%@", [settings objectForKey:(NSString *)kCFProxyHostNameKey]);
    NSLog(@"port=%@", [settings objectForKey:(NSString *)kCFProxyPortNumberKey]);
    NSLog(@"type=%@", [settings objectForKey:(NSString *)kCFProxyTypeKey]);
    
    if ([[settings objectForKey:(NSString *)kCFProxyTypeKey] isEqualToString:@"kCFProxyTypeNone"])
    {
        //没有设置代理
        return NO;
    }
    else
    {
        [JKToast showWithText:@"当前网络不安全"];
        //设置代理了
        return YES;
    }
}

+(CrazyNetWork *)CrazyTokenHeadRequest_Post:(NSString *)header HUD:(BOOL)HUD parameters:(NSDictionary *)parameters success:(requestSuccess)success fail:(requestFail)fail{
    
    if (HUD == YES) {
        [SVProgressHUD show];
    }
    
    CrazyNetWork * NetWork = [CrazyNetWork share];
    
    NetWork.block_fail = fail;
    NetWork.block_success = success;
    
    NetWork.manager = [NSURLSession sharedSession];
    
    NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:header]];
    [request setHTTPMethod:@"POST"];
    [request setTimeoutInterval:requstTimeOut];
    [request setValue:Token forHTTPHeaderField:@"TOKEN"];
    NSLog(@"%@",Token);
    //    NSDictionary * JWTDic = [CrazyNetWork parametersSecret:parameters];
    
    NSString * bodyStr = [CrazyNetWork requestBody:parameters];
    bodyStr = [CrazyNetWork HTMLfilter:bodyStr];
    
    NSData *bodyData = [bodyStr dataUsingEncoding:NSUTF8StringEncoding];
    [request setHTTPBody:bodyData];
    
    NSURLSessionDataTask *task = [NetWork.manager dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        if (error == nil) {
            
            NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];//转换数据格式
            
            NSString * json = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
            
            NSString *url = [response.URL absoluteString];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                NetWork.block_success(dic,url,json);
                [SVProgressHUD dismiss];
                
            });
            
        }else{
            
            if (NetWork.block_fail) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    [JKToast showWithText:@"连接服务器异常，请刷新重试"];
                    NetWork.block_fail(error,[task.originalRequest.URL absoluteString]);
                });
                [SVProgressHUD dismiss];
                
            }
            
        }
        
    }];
    
    [task resume];
    
    //    if (NetWork.block_Progress) {
    //        NetWork.block_Progress(uploadProgress);
    //    }
    
    return NetWork ;
}

@end
