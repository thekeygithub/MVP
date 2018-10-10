//
//  BalanceViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/11.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "BalanceViewController.h"

@interface BalanceViewController ()

@end

@implementation BalanceViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    [self customUI];

}

#pragma mark - UI
- (void)customUI
{
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    if (IsiPhoneX) {
        _statusView.height = StatusBarHeight;
        _navigationView.centerY = _navigationView.centerY + StatusBarHeight - 20;
        
    }
    self.yueLabel.text = [NSString stringWithFormat:@"余额：%@TKY",self.balance];
}

#pragma mark - 返回
- (IBAction)back:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
