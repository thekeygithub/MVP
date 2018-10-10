//
//  BillViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BillViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UIView *statusView;
@property (weak, nonatomic) IBOutlet UIView *navigationView;
@property (weak, nonatomic) IBOutlet UIView *topView;
@property (weak, nonatomic) IBOutlet UIImageView *waitPayIV;
@property (weak, nonatomic) IBOutlet UILabel *waitPayLabel;

@property (weak, nonatomic) IBOutlet UIView *bottomView;
@property (weak, nonatomic) IBOutlet UIImageView *chooseallIV;
@property (weak, nonatomic) IBOutlet UILabel *moneyLAbel;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic) NSDictionary *dic;
@end
