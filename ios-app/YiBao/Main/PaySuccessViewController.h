//
//  PaySuccessViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/11.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PaySuccessViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UIButton *exitButton;
@property (weak, nonatomic) IBOutlet UIButton *lookBalanceButton;
@property (copy, nonatomic) NSString *balance;
@end
