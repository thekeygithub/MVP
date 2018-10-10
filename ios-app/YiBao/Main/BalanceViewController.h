//
//  BalanceViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/11.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BalanceViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UIView *statusView;
@property (weak, nonatomic) IBOutlet UIView *navigationView;
@property (weak, nonatomic) IBOutlet UIView *yueView;
@property (weak, nonatomic) IBOutlet UILabel *yueLabel;
@property (copy, nonatomic) NSString *balance;
@end
