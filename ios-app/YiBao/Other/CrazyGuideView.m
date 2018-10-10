//
//  easyflyGuideView.m
//  ZiHaiKeJiP2P
//
//  Created by dukai on 15/6/7.
//  Copyright (c) 2015年 dukai. All rights reserved.
//

#import "CrazyGuideView.h"
#import "UIImageView+WebCache.h"
#import "AppDelegate.h"
@implementation CrazyGuideView
{
    int allPage;
    CGFloat currentPageNumber;
    CGFloat otherPageNumber;
}
- (instancetype)init
{
    self = [super init];
    if (self) {
        self.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        [self createView];
    }
    return self;
}
-(void)createView{
    
    self.ScrollView = [[UIScrollView alloc]initWithFrame:[UIScreen mainScreen].bounds];
    self.ScrollView.showsHorizontalScrollIndicator = NO;
    self.ScrollView.pagingEnabled = YES;
    self.ScrollView.delegate = self;
    self.ScrollView.bounces = NO;
    self.userInteractionEnabled = YES;
    [self addSubview:self.ScrollView];

    self.startBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    self.startBtn.frame = CGRectMake((SCREEN_WIDTH-170)/2,SCREEN_HEIGHT-HEIGHT(80), 170, 50);
    [self.startBtn setTitle:@"" forState:UIControlStateNormal];
    [self.startBtn addTarget:self action:@selector(start) forControlEvents:UIControlEventTouchUpInside];
    self.startBtn.backgroundColor = [UIColor clearColor];

    self.pageCtr = [[UIPageControl alloc]initWithFrame:CGRectMake(0, SCREEN_HEIGHT-30, SCREEN_WIDTH, 30)];
    self.pageCtr.currentPageIndicatorTintColor = currentPageColor;
    self.pageCtr.pageIndicatorTintColor = pageIndicatorColor;
    [self addSubview:self.pageCtr];
    
    AppDelegate * app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    [app.window addSubview:self];

}


-(void)createLocationImageArr:(NSArray *)ImageArr block:(finish_block)block{
    currentPageNumber = 0.0;
    otherPageNumber = 0.0;
    self.block = block;
    
    for (int i =0 ; i<ImageArr.count; i++) {
        
        CGRect frame = CGRectMake(SCREEN_WIDTH *i, 0.0, SCREEN_WIDTH, SCREEN_HEIGHT);
        UIView * subview = [[UIView alloc]initWithFrame:frame];
        
        UIScrollView * internalScrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0.0, 0.0, SCREEN_WIDTH, SCREEN_HEIGHT)];
        internalScrollView.scrollEnabled = NO;
        CGRect imageViewFrame =CGRectMake(0.0, 0.0, internalScrollView.frame.size.width+15, internalScrollView.frame.size.height);
        
        UIImageView *imageView = [[UIImageView alloc]initWithFrame:imageViewFrame];
        //判断是本地图片 还是网络图片
        NSString *imageName = ImageArr[i];
        if (imageName.length>4 && [[imageName substringToIndex:4] isEqualToString:@"http"]) {
                [imageView sd_setImageWithURL:[NSURL URLWithString:imageName] placeholderImage:[UIImage imageNamed:placehoderBackImage]];
        }else{
            [imageView setImage:[UIImage imageNamed:imageName]];
        }
        
        [self addMotionEffectToView:imageView magnitude:0.0];
        
        internalScrollView.tag = (i + 1) * 10;
        imageView.tag = (i + 1) * 1000;
        
        [internalScrollView addSubview:imageView];
        [subview addSubview:internalScrollView];
        [self.ScrollView addSubview:subview];
        
        if (i == ImageArr.count-1) {
            //最后一页 加入按钮
            [internalScrollView addSubview:self.startBtn];
        }
        
    }
    self.pageCtr.numberOfPages = ImageArr.count;
    self.ScrollView.contentSize = CGSizeMake(SCREEN_WIDTH * (ImageArr.count+1), SCREEN_HEIGHT);
   
    allPage = (int)ImageArr.count;
}
-(void)start{
    
    if (self.block) {
        self.block();
    }
    [self.ScrollView setContentOffset:CGPointMake(allPage*SCREEN_WIDTH, 0) animated:YES];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1*NSEC_PER_SEC), dispatch_get_main_queue(), ^{
    [self removeFromSuperview];
    });
    
}
-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    UIScrollView * internalScrollView = (UIScrollView *)[scrollView viewWithTag:((currentPageNumber + 1) * 10)];
    internalScrollView.contentOffset = CGPointMake(0.0, 0.0);
    
    currentPageNumber = (int)scrollView.contentOffset.x / (int)scrollView.frame.size.width;
    
    //页面控制器的当前页
    self.pageCtr.currentPage = scrollView.contentOffset.x/scrollView.bounds.size.width;
    //移除页面
    if (scrollView.contentOffset.x/scrollView.bounds.size.width == allPage) {
        
        if (self.block) {
            self.block();
        }
        [self removeFromSuperview];
        
    }
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    
    //判断 让pageCtr 跟着最后一张图 一起滑动
    if (scrollView.contentOffset.x>(allPage-1)*SCREEN_WIDTH ) {
        
        float ptx = scrollView.contentOffset.x-(allPage-1)*SCREEN_WIDTH;
        self.pageCtr.frame = CGRectMake(-ptx,self.pageCtr.frame.origin.y , self.pageCtr.frame.size.width, self.pageCtr.frame.size.height);
    }
    
    CGFloat offset = scrollView.contentOffset.x;
    
    UIScrollView * internalScrollView = (UIScrollView*)[scrollView viewWithTag:((currentPageNumber + 1) * 10)];
    internalScrollView.contentOffset = CGPointMake(-0.4 * (offset - (SCREEN_WIDTH * (CGFloat)currentPageNumber)), 0.0);
}

//视差滑动效果
-(void)addMotionEffectToView:(UIView*)view magnitude:(CGFloat)magnitude{
    UIInterpolatingMotionEffect * xMotion = [[UIInterpolatingMotionEffect alloc]initWithKeyPath:@"center.x" type:UIInterpolatingMotionEffectTypeTiltAlongHorizontalAxis];
    UIInterpolatingMotionEffect * yMotion = [[UIInterpolatingMotionEffect alloc]initWithKeyPath:@"center.y" type:UIInterpolatingMotionEffectTypeTiltAlongVerticalAxis];
    xMotion.minimumRelativeValue = @(magnitude);
    xMotion.maximumRelativeValue = @(magnitude);
    yMotion.minimumRelativeValue = @(-magnitude);
    yMotion.maximumRelativeValue = @(magnitude);
    UIMotionEffectGroup * motionGroup = [[UIMotionEffectGroup alloc]init];
    motionGroup.motionEffects = @[xMotion, yMotion];
    [view addMotionEffect:motionGroup ];
    
}
@end
