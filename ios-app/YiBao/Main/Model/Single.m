//
//  Single.m
//  YiBao
//
//  Created by wangchang on 2018/6/8.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "Single.h"

@implementation Single
+ (instancetype)shareSingle
{
    static Single *single = nil;
    if (single == nil) {
        single = [[Single alloc] init];
    }
    
    return single;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.model = [[UserMessageModel alloc] init];
        self.payArray = [NSMutableArray array];
    }
    return self;
}
@end
