//
//  THIDMCFaceLiveBaseManger.h
//  HTJCFaceBaseSdk
//
//  Created by 于汇江 on 15/7/5.
//  Copyright (c) 2015年 于汇江. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface THIDMCFaceLiveBaseManger : NSObject

@property (nonatomic,strong,readonly) NSArray * liveDetectValueCache;
@property (nonatomic,assign,readonly) NSInteger LiveDetectionNum;

@property (nonatomic, assign) BOOL isRandom;
@property (nonatomic, strong) NSArray *liveDetectArr;
//获取实例对象
+(THIDMCFaceLiveBaseManger *)sharedManager;

/*      初始化配置
 *
 *      _sucessed：成功回调
 *      _failed：失败回调
 */
-(void)initializedSdkSuccess:(void (^)(BOOL success))_sucessed failed:(void (^)(NSString *error))_failed;

/*      向SDK发送图片
 *
 *      detectImage：抓到的每桢图片
 *      _completion：成功回调
 *      _failed：失败回调
 */
- (void)getLiveDetectProcessPushed:(UIImage *)detectImage withCompletion:(void (^)(NSInteger procedueState, NSInteger returnVal))_completion failed:(void (^)(NSInteger state, NSInteger returnErr))_failed;

-(void)UnInitHTJCSDK;

//图片加密
-(NSData*)getEcryptFaceRectImage:(UIImage*)_image;
-(NSData*)getEcryptFaceRectImage:(UIImage*)_imageOne imageData:(NSData *)imageData;
//插入图片数据
- (NSData *)insertImage:(NSData *)imageData toImage:(NSData *)inputImageData;
//插图图片内容
- (NSData *)insertDetectName:(NSString *)detectName toImage:(NSData *)inputImageData;

//获取人脸框
-(CGRect)getFaceTHIDRectWithImage:(UIImage*)image;

//获取下一个状态
- (void)getNextStepLiveDetection:(void (^)(NSInteger state))nextState;
- (float)getImageGuideScore;
- (float)getImageLiveScore:(UIImage *)resizeImage;

@end
