//
//  LogViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "securityCodeBtn.h"

@interface LogViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UITextField *phonetf;
@property (weak, nonatomic) IBOutlet UITextField *codetf;
@property (weak, nonatomic) IBOutlet securityCodeBtn *codeButton;
@property (weak, nonatomic) IBOutlet UIButton *logButton;

@end
