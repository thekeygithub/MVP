//
//  easyflyGuideView.h
//  ZiHaiKeJiP2P
//
//  Created by dukai on 15/6/7.
//  Copyright (c) 2015年 dukai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

#define ScreenWidth  [UIScreen mainScreen].bounds.size.width
#define ScreenHeight [UIScreen mainScreen].bounds.size.height
#define currentPageColor [UIColor redColor] //页面控制器选中颜色
#define pageIndicatorColor [UIColor blackColor] //页面控制器未选中颜色
#define placehoderBackImage @"" //获取网络图片时 默认图片

typedef void (^finish_block)(void);

@interface CrazyGuideView : UIView<UIScrollViewDelegate>

@property(nonatomic,strong)finish_block block;

@property(nonatomic,strong)UIPageControl *pageCtr;
@property(nonatomic,strong)UIScrollView *ScrollView;
@property(nonatomic,strong)UIButton *startBtn;


-(void)createLocationImageArr:(NSArray *)ImageArr block:(finish_block)block;

@end



//2015-07-08   1.新添加视差滑动效果   2.可自动判断 图片来源本地还是网络