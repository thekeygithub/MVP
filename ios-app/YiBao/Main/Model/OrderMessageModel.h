//
//  OrderMessageModel.h
//  YiBao
//
//  Created by wangchang on 2018/6/8.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OrderMessageModel : NSObject
@property (copy ,nonatomic) NSString *prescriptionId;
@property (copy ,nonatomic) NSString *prescriptionName;
@property (copy ,nonatomic) NSString *doctorName;
@property (copy ,nonatomic) NSString *deptName;
@property (copy ,nonatomic) NSString *diagnosis;
@property (copy ,nonatomic) NSString *treatmentDate;
@property (copy ,nonatomic) NSString *payActully;
@property (copy ,nonatomic) NSString *reimbursement;
@property (copy ,nonatomic) NSDictionary *prescriptionDetails;

@end
