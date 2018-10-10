//
//  CrazyTimerLabel.h
//  ocCrazy
//
//  Created by dukai on 16/9/2.
//  Copyright © 2016年 dukai. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CrazyTimerLabel : UILabel

@property(nonatomic,assign)int allSecond;//总秒数
@property(nonatomic,strong)NSTimer *timer;//计时器
@property(nonatomic,assign)int step;//步距默认1

//正计时
-(void)CrazyStartIsTiming:(int)second;
//倒计时  second倒计时秒数    futureDate 未来时间 YYYY-MM-dd HH:mm:ss
-(void)CrazyStartCountdownTiming:(int)second futureDate:(NSString *)futureDate;


@end
