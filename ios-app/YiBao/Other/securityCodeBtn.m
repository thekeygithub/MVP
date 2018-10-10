//
//  securityCodeBtn.m
//  TiaoWei
//
//  Created by dukai on 15/3/8.
//  Copyright (c) 2015年 longcai. All rights reserved.
//

#import "securityCodeBtn.h"

@implementation securityCodeBtn
{
    NSInteger _time;
    NSString *_phone;
    NSString *_format;
    NSTimer *timer;
}

- (void)WithFormat:(NSString *)format withPhone:(NSString *)phone Time:(int)time
{
        _time = time;
        _phone = phone;
        _format = format;
}

-(BOOL)securityCode{
    //验证  手机号
    NSString *regex = @"^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$";
    
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    
    BOOL isMatch = [pred evaluateWithObject:_phone];
    
    return isMatch;

}
-(void)startTime{
    //开始倒计时
    timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(refreshTime) userInfo:nil repeats:YES];
}

-(void)refreshTime{
    
    _time --;
    
    if(_time == 0){
//        [timer invalidate];
        self.enabled = YES;
        [self stopTime];
        
    }else{
        self.enabled = NO;
        NSString *string = [NSString stringWithFormat:@"%ld秒后重新获取",_time];
        [self setTitle:string forState:UIControlStateNormal];
   
    }
  
}

-(void)stopTime
{
    [timer invalidate];
    self.enabled = YES;
    [self setTitle:_format forState:UIControlStateNormal];
}
@end
