//
//  securityCodeBtn.h
//  TiaoWei
//
//  Created by dukai on 15/3/8.
//  Copyright (c) 2015年 longcai. All rights reserved.
//

#import <UIKit/UIKit.h>
//获取验证码 button
@interface securityCodeBtn : UIButton

- (void)WithFormat:(NSString *)format withPhone:(NSString *)phone Time:(int)time;
-(BOOL)securityCode; //验证 手机号
-(void)startTime;  //开始倒计时

-(void)stopTime;

@end
