//
//  SuccessViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "SuccessViewController.h"
#import "BillViewController.h"

@interface SuccessViewController ()

@end

@implementation SuccessViewController

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
    _billButton.layer.cornerRadius = 5;

}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
    int index = (int)[[self.navigationController viewControllers]indexOfObject:self];
    [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:1] animated:YES];
}

#pragma mark - 查看账单
- (IBAction)biil:(id)sender {
    BillViewController *vc = [[BillViewController alloc] init];
    vc.dic = self.dic;
    [self.navigationController pushViewController:vc animated:YES];
}


@end
