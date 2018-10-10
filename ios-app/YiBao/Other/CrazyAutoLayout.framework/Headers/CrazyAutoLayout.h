//
//  CrazyAutoLayout.h
//  ocCrazy
//
//  Created by dukai on 16/1/5.
//  Copyright © 2016年 dukai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

/*
  该方法等同于  [CrazyAutoLayout crazyHeight:0.0];
 */
#define HEIGHT(f) f * ([UIScreen mainScreen].bounds.size.width/320.0)
#define SINGLE_LINE_WIDTH(f)           (f / [UIScreen mainScreen].scale)
#define SINGLE_LINE_ADJUST_OFFSET(f)   ((f / [UIScreen mainScreen].scale) / 2)

typedef void(^autolayoutBlock)(void);
@interface UIView(View)

@property (nonatomic, assign) IBInspectable NSString * margin_top;
@property (nonatomic, assign) IBInspectable NSString * margin_right;
@property (nonatomic, assign) IBInspectable NSString * margin_bottom;
@property (nonatomic, assign) IBInspectable NSString * margin_left;



@property(nonatomic,strong)NSString * mark;//如果mark是 @"yes"  则取消此UI适配
@property(nonatomic,strong)NSString * parameter;//UI扩展参数 （自定义标记）
@property(nonatomic,strong)NSString * index;//UI扩展参数 （自定义排列位数）

@end


@interface CrazyAutoLayout : NSObject

@property(nonatomic)float scale;

+(void)frameOfSuperView:(UIView *)superView;//适配view(frame)

+(CGFloat)getFontSize:(CGFloat)fontSize;//获取字体大小
+(CGFloat)getUIScale;//比例
+(CGFloat)crazyHeight:(float)height;//cell 增量高度

//框架内部调用 私有api
+(CrazyAutoLayout *)share;
-(void)addMarginLayout:(UIView *)view superView:(UIView *)superView;

@end
