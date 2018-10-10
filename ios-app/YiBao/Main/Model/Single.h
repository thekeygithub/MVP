//
//  Single.h
//  YiBao
//
//  Created by wangchang on 2018/6/8.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserMessageModel.h"

@interface Single : NSObject
+ (instancetype)shareSingle;
@property (strong, nonatomic) UserMessageModel *model;
@property (copy, nonatomic) NSString *hospName;
@property (copy, nonatomic) NSString *hospId;
@property (copy, nonatomic) NSString *treatmentId;
@property (copy, nonatomic) NSString *idNumber;

@property (strong, nonatomic) NSMutableArray *payArray;
@end
