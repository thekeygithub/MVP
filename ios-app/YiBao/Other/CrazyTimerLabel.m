//
//  CrazyTimerLabel.m
//  ocCrazy
//
//  Created by dukai on 16/9/2.
//  Copyright © 2016年 dukai. All rights reserved.
//

#import "CrazyTimerLabel.h"
#import "LogViewController.h"

@implementation CrazyTimerLabel

//正计时
-(void)CrazyStartIsTiming:(int)second{
    
    second = second>0?second:0;
    _allSecond = second;
    
    if (_timer == nil)
    {
        _step = _step >0?_step:1;
        
        _timer =[NSTimer scheduledTimerWithTimeInterval:_step target:self selector:@selector(startIsTimer) userInfo:nil repeats:YES];
    }
    
}
//倒计时  second倒计时秒数    futureDate 未来时间 YYYY-MM-dd HH:mm:ss
-(void)CrazyStartCountdownTiming:(int)second futureDate:(NSString *)futureDate{
    
    if (second > 0) {
        
        _allSecond = second;
        
    }else if(futureDate.length > 0){
        
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateStyle:NSDateFormatterMediumStyle];
        [formatter setTimeStyle:NSDateFormatterShortStyle];
        [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss"];
        
        NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
        [formatter setTimeZone:timeZone];
        
        NSDate* date = [formatter dateFromString:futureDate];
        NSString * endTimestamp = [CrazyFunction crazy_timeToTimestamp:date];
        
        NSString *startTimestamp = [CrazyFunction crazy_timeToTimestamp:[NSDate date]];
        
        _allSecond = [endTimestamp intValue]- [startTimestamp intValue];
        
        _allSecond = _allSecond>0?_allSecond:0;
    }
    
    
    if (_timer == nil)
    {
        _step = _step >0?_step:1;
        
        _timer =[NSTimer scheduledTimerWithTimeInterval:_step target:self selector:@selector(startCountdownTimer) userInfo:nil repeats:YES];
    }

}


-(void)startIsTimer{
    
    [self update];
    
    _allSecond ++;
}


-(void)startCountdownTimer{
    
    [self update];
    
    if (_allSecond > 0 ) {
        _allSecond --;
//        if (_allSecond == 0) {
////            [[self getCurrentVC].navigationController popViewControllerAnimated:YES];
//        }
    }
}
-(void)update{
    
    int hour = _allSecond/3600>0?_allSecond/3600:0;
    int min = (_allSecond- hour *3600 )/60;
    int second = _allSecond- hour *3600 - min*60;
    
    NSString * hourStr = hour > 10?[NSString stringWithFormat:@"%d",hour]:[NSString stringWithFormat:@"0%d",hour];
    
    NSString * minStr = min > 10?[NSString stringWithFormat:@"%d",min]:[NSString stringWithFormat:@"0%d",min];
    
    NSString *secondStr = second > 10 ?[NSString stringWithFormat:@"%d",second]:[NSString stringWithFormat:@"0%d",second];
    
    
    self.text = [NSString stringWithFormat:@"%@:%@:%@",hourStr,minStr,secondStr];

}

//获取当前屏幕显示的viewcontroller   (这里面获取的相当于rootViewController)
- (UIViewController *)getCurrentVC
{
    UIViewController *result = nil;
    UIWindow * window = [[UIApplication sharedApplication] keyWindow];
    if (window.windowLevel != UIWindowLevelNormal) {
        NSArray *windows = [[UIApplication sharedApplication] windows];
        for(UIWindow * tmpWin in windows) {
            if (tmpWin.windowLevel == UIWindowLevelNormal) {
                window = tmpWin;
                break;
            }
        }
    }
    
    UIView *frontView = [[window subviews] objectAtIndex:0];
    id nextResponder = [frontView nextResponder];
    
    if ([nextResponder isKindOfClass:[UIViewController class]]) {
        result = nextResponder;
    } else {
        result = window.rootViewController;
    }
    /*
     *  在此判断返回的视图是不是你的根视图--我的根视图是tabbar
     */
//    if ([result isKindOfClass:[LogViewController class]]) {
//        LogViewController *mainTabBarVC = (LogViewController *)result;
//        result = [mainTabBarVC selectedViewController];
//        result = [result.childViewControllers lastObject];
//    }
    
    NSLog(@"非模态视图%@", result);
    return result;
}
@end
