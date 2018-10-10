//
//  THIDMCHTJCViewManger.h
//  THIDMCHTJCViewManger
//
//  Created by 孙佳臣 on 15/9/21.
//  Copyright (c) 2015年 THIDMCHTJCViewManger. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface THIDMCHTJCViewManger : NSObject

//获取实例对象
+(THIDMCHTJCViewManger *)sharedManager:(UIViewController*)_vc;

/*      活体检测
 *
 *      _model：活体检测输入参数
 *      _completion：成功回调
 *      _failed：失败返回值
 */
-(void)getLiveDetectCompletion:(void (^)(BOOL success,NSData * imageData))_completion
                        cancel:(void(^)(BOOL success, NSError* error))_cancel
                        failed:(void (^)(NSError *error,NSData *imageData))_failed;

/**
 *  退出活体
 */
-(void)dismissTakeCaptureSessionViewController;



#pragma mark -
#pragma mark 可能需要配置信息  如需配置,请设置完成后  在启动检测 getLiveDetectCompletion:cancel:failed:
/**
 *  导航栏样式       navType = 0 方式：addSubView   （默认）
 *                 navType = 1 方式：present  
 *                 navType = 2 方式：push
 *
 *  @param navType 样式代号
 */
- (void)navigationType:(int)navType;

/**
 *  活体检测动作配置
 *
 *  @param liveDetectTypeArray   数组 @0 = 凝视  @1 = 摇头  @2 = 点头  @3 = 张嘴  @4 = 眨眼
 */
- (void)liveDetectTypeArray:(NSArray *)liveDetectTypeArray;

/**
 *  动作是否随机出现     NO则为liveDetectTypeArray配置的顺序
 *
 *  @param isRandom   随机
 */
- (void)isNeedRandom:(BOOL)isRandom;

/**
 *  界面代理设置   需要实现 THIDMCSetDetectViewDelegate 代理方法
 *
 *  @param delegate 代理
 */
- (void)setDetectViewListener:(id)delegate;



#pragma mark -
#pragma mark 内部版本记录
/**
 *  获取framework版本号
 *
 *  @return 版本号
 */
- (NSString *)getBundleVersion;

/**
 *  获取算法SDK版本号
 *
 *  @return 版本号
 */
- (NSString *)getSDKVersion;

@end
