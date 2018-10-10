//
//  PayFailViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/12.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "PayFailViewController.h"

@interface PayFailViewController ()

@end

@implementation PayFailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    [self customUI];
}

#pragma mark - UI
- (void)customUI
{
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    _cancelButton.layer.cornerRadius = 5;
    _exitButton.layer.cornerRadius = 5;
    
}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
//    int index = (int)[[self.navigationController viewControllers]indexOfObject:self];
    [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:1] animated:YES];
}



@end
