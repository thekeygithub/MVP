//
//  OrderDetailModel.h
//  YiBao
//
//  Created by wangchang on 2018/6/8.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OrderDetailModel : NSObject
@property (copy ,nonatomic) NSString *treatmentType;
@property (copy ,nonatomic) NSString *itemName;
@property (copy ,nonatomic) NSString *specification;
@property (copy ,nonatomic) NSString *price;
@property (copy ,nonatomic) NSString *num;
@property (copy ,nonatomic) NSString *amount;
@property (copy ,nonatomic) NSString *dosageforms;
@property (copy ,nonatomic) NSString *dose;
@property (copy ,nonatomic) NSString *usage;
@property (copy ,nonatomic) NSString *usefreq;
@property (copy ,nonatomic) NSString *exedays;

@end
