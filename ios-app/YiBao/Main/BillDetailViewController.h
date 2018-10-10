//
//  BillDetailViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OrderDetailModel.h"
#import "OrderMessageModel.h"
@interface BillDetailViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UIView *statusView;
@property (weak, nonatomic) IBOutlet UIView *navigationView;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong, nonatomic) OrderMessageModel *model;
@end
