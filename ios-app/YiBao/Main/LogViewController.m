//
//  LogViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "LogViewController.h"
#import "VTFCViewController.h"

@interface LogViewController ()

@end

@implementation LogViewController
- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBar.hidden = YES;
    [CrazyAutoLayout frameOfSuperView:self.view];
    [self customUI];
    if (Token) {
        VTFCViewController *vc = [[VTFCViewController alloc] init];
        [self.navigationController pushViewController:vc animated:NO];
    }
}

#pragma mark - UI
- (void)customUI
{
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    [_phonetf setBorderStyle:UITextBorderStyleNone];
    [_phonetf setValue:UIColorFromRGB(0xffffff) forKeyPath:@"_placeholderLabel.textColor"];
    [_codetf setBorderStyle:UITextBorderStyleNone];
    [_codetf setValue:UIColorFromRGB(0xffffff) forKeyPath:@"_placeholderLabel.textColor"];
    _logButton.layer.cornerRadius = 5;
    _codeButton.layer.cornerRadius = 5;
    [self.codeButton WithFormat:@"重新发送" withPhone:@"" Time:60];
//    self.codeButton.enabled = NO;
}

#pragma mark - 获取验证码
- (IBAction)getcode:(id)sender {
    [_phonetf resignFirstResponder];
    [_codetf resignFirstResponder];

    [self.view endEditing:NO];
    [self.codeButton WithFormat:@"获取验证码" withPhone:@"" Time:60];
    if ([_phonetf.text isEqualToString:@""] || _phonetf.text == nil) {
        [JKToast showWithText:@"手机号不能为空"];
        self.codeButton.enabled = YES;
    } else if (![self validatePhone:_phonetf.text]){
        self.codeButton.enabled = YES;
        [JKToast showWithText:@"手机号格式错误"];
    }else{
        self.codeButton.enabled = NO;
        [self requestCodeData];
    }
}

#pragma mark - 获取验证码数据请求
- (void)requestCodeData
{
    NSDictionary *parameters = @{@"mobile":_phonetf.text,@"type":@"1"};
    [CrazyNetWork CrazyRequest_Post:[NSString stringWithFormat:@"%@sendCode",defaultUrl] HUD:YES parameters:parameters success:^(NSDictionary *dic, NSString *url, NSString *Json) {
        if ([dic[@"statusCode"] integerValue] == 200) {
            [self.codeButton startTime];
        } else {
            self.codeButton.enabled = YES;
        }
        [JKToast showWithText:dic[@"message"]];
    } fail:^(NSError *error, NSString *url) {
        
    }];

}

#pragma mark - 登录
- (IBAction)log:(id)sender {
    [self.view endEditing:NO];
    
    if ([_phonetf.text isEqualToString:@""] || _phonetf.text == nil) {
        [JKToast showWithText:@"手机号不能为空"];
    } else if (![self validatePhone:_phonetf.text]){
        [JKToast showWithText:@"手机号格式错误"];

    } else if ([_codetf.text isEqualToString:@""] || _codetf.text == nil) {
        [JKToast showWithText:@"验证码不能为空"];
    } else {
        [self requestData];
    }
}

#pragma mark - 登录数据请求
- (void)requestData
{
    NSDictionary *parameters = @{@"mobile":_phonetf.text,@"code":_codetf.text};
    [CrazyNetWork CrazyRequest_Post:[NSString stringWithFormat:@"%@login",defaultUrl] HUD:YES parameters:parameters success:^(NSDictionary *dic, NSString *url, NSString *Json) {
        if ([dic[@"statusCode"] integerValue] == 200) {
            NSString *str = [NSString stringWithFormat:@"%@-%@",dic[@"returnObj"][@"deviceId"],dic[@"returnObj"][@"token"]];
            [[NSUserDefaults standardUserDefaults] setObject:str forKey:@"token"];
            [[NSUserDefaults standardUserDefaults] setObject:dic[@"returnObj"][@"deviceId"] forKey:@"phonenum"];

            VTFCViewController *vc = [[VTFCViewController alloc] init];
            vc.phoneStr = self.phonetf.text;
            [self.navigationController pushViewController:vc animated:YES];
            self.phonetf.text = @"";
            self.codetf.text = @"";

        } else {
            
        }
        [JKToast showWithText:dic[@"message"]];
    } fail:^(NSError *error, NSString *url) {
        
    }];
    
}


#pragma mark - 手机验证
- (BOOL)validatePhone:(NSString *)phoneNum
{
    /*
     手机号码
     移动：134[0-8],      135,136,137,138,139,147,150,151,152,157,158,159,170,173,175,176,177,178,182,183,184,187,188
     联通：130,131,132,145,155,156,176,185,186
     电信：133,1349,153,177,180,181,189
     */
    //验证手机号码是否正确
    NSString * MOBILE = @"^1(3[0-9]|4[57]|5[0-35-9]|7[0,3,5-8]|8[0-9])\\d{8}$";
    NSString * CM = @"^1(34[0-8]|(3[5-9]|4[7]|5[0127-9]|7[8]|8[23478])\\d)\\d{7}$";
    NSString * CU = @"^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$";
    NSString * CT = @"^1(3[34]|5[3]|7[7]|8[019])\\d{8}$";
    
    NSPredicate *regextestmobile = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", MOBILE];
    NSPredicate *regextestcm = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CM];
    NSPredicate *regextestcu = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CU];
    NSPredicate *regextestct = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CT];
    
    if (([regextestmobile evaluateWithObject:phoneNum] == YES)
        || ([regextestcm evaluateWithObject:phoneNum] == YES)
        || ([regextestct evaluateWithObject:phoneNum] == YES)
        || ([regextestcu evaluateWithObject:phoneNum] == YES))
    {
        if([regextestcm evaluateWithObject:phoneNum] == YES) {
            NSLog(@"China Mobile");
        } else if([regextestct evaluateWithObject:phoneNum] == YES) {
            NSLog(@"China Telecom");
        } else if ([regextestcu evaluateWithObject:phoneNum] == YES) {
            NSLog(@"China Unicom");
        } else {
            NSLog(@"Unknow");
        }
        
        return YES;
    }
    else
    {
        return NO;
    }
    
}


@end
