//
//  QrCodeViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/11.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "QrCodeViewController.h"

@interface QrCodeViewController ()

@end

@implementation QrCodeViewController

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
    
}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
