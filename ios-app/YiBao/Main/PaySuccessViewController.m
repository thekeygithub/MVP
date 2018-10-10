//
//  PaySuccessViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/11.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "PaySuccessViewController.h"
#import "BalanceViewController.h"
@interface PaySuccessViewController ()

@end

@implementation PaySuccessViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    [self customUI];


}

#pragma mark - UI
- (void)customUI
{
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    self.exitButton.layer.cornerRadius = 5;
    self.lookBalanceButton.layer.cornerRadius = 5;
    
}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
    int index = (int)[[self.navigationController viewControllers]indexOfObject:self];
    [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:1] animated:YES];
}

#pragma mark - 查看余额
- (IBAction)lookBalance:(id)sender {
    BalanceViewController *vc = [[BalanceViewController alloc] init];
    vc.balance = self.balance;
    [self.navigationController pushViewController:vc animated:YES];
}

@end
