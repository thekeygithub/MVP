//
//  OrderViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/7.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CrazyTimerLabel.h"
#import "OrderMessageModel.h"
@interface OrderViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UIView *statusView;
@property (weak, nonatomic) IBOutlet UIView *navigationView;
@property (weak, nonatomic) IBOutlet UIView *topView;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;

@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIView *backView;
@property (weak, nonatomic) IBOutlet UIButton *backButton;
@property (weak, nonatomic) IBOutlet UIButton *payButton;
@property (weak, nonatomic) IBOutlet UIView *bottomView;

@property (weak, nonatomic) IBOutlet UILabel *firstLAbel;
@property (weak, nonatomic) IBOutlet UILabel *secondLabel;
@property (weak, nonatomic) IBOutlet UILabel *thirdLabel;
@property (weak, nonatomic) IBOutlet UILabel *fourthLabel;
@property (weak, nonatomic) IBOutlet UILabel *fifthLabel;

@property (weak, nonatomic) IBOutlet UIImageView *firstIV;
@property (weak, nonatomic) IBOutlet UIImageView *secondIV;
@property (weak, nonatomic) IBOutlet UIImageView *thirdIV;
@property (weak, nonatomic) IBOutlet UIButton *shulianButton;
@property (weak, nonatomic) IBOutlet UIButton *zhiwenButton;
@property (weak, nonatomic) IBOutlet UILabel *tkyLabel;
@property (strong, nonatomic) IBOutlet UIView *payView;

@property (strong, nonatomic) IBOutlet UIView *passwordView;
@property (weak, nonatomic) IBOutlet UIView *enterView;
@property (weak, nonatomic) IBOutlet UITextField *zhifuTF;

//@property (strong, nonatomic) OrderMessageModel *model;
@property (strong, nonatomic) NSArray *array;
@end
