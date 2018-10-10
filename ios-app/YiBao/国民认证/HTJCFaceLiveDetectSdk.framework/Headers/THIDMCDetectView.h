//
//  THIDMCDetectView.h
//  HTJCFaceLiveDetectSdk
//
//  Created by sunjiachen on 15/11/25.
//  Copyright © 2015年 HTJCLiveDetect. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@protocol THIDMCSetDetectViewDelegate <NSObject>

@required

#pragma mark -
#pragma mark 活检背景视图
/**
 *  导航栏
 *
 *  @return 导航栏View
 */
- (UIView *)setNavigationView;

/**
 *  导航栏左侧返回按钮
 *
 *  @return 按钮
 */
- (UIButton *)setBackButton;

/**
 *  引导
 *
 *  @return 引导界面
 */
- (UIView *)setFaceGuideView;

/**
 *  引导过程中光线提醒label
 *
 *  @return label
 */
- (UILabel *)setLightGuideLabel;

/**
 *  活体检测
 *
 *  @return 活体检测界面
 */
- (UIView *)setLiveDetecteView;

/**
 *  321倒计时
 *
 *  @return 倒计时视图
 */
- (UIView *)setNumberDownView;

#pragma mark -
#pragma mark 引导动画
/**
 *  引导遮罩提示动画开始
 *
 */
-(void)beginMaskViewAnimation;

#pragma mark -
#pragma mark 活检提示文本

/**
 *  底部视图信息提示
 */
- (void)setBottomReminderText:(NSString *)remindStr;

#pragma mark -
#pragma mark 单个动作成功动画

/**
 *  对勾动画
 */
- (void)setGoodNextAnimation;

#pragma mark -
#pragma mark 活检流程动画

/**
 *  注视屏幕动画
 */
- (void)setKeepStillAnimation;

/**
 *  摇头动画
 */
- (void)setShakeHeadAnimatinon;

/**
 *  点头动画
 */
- (void)setNodHeadAnimation;

/**
 *  张嘴动画
 */
- (void)setOpenMouthAnimation;

/**
 *  眨眼动画
 */
- (void)setBlinkEyesAnimation;
#pragma mark -
#pragma mark 进度条状态

/**
 *  开始进度条
 */
- (void)beginProgressView;

/**
 *  停止进度条
 */
- (void)stopProgressView;

/**
 *  填充进度条
 */
- (void)endProgressView;


@end

