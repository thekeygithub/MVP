//
//  OrderViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/7.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "OrderViewController.h"
#import "FaceStreamDetectorViewController.h"
#import "BrushFacePayViewController.h"
#import "PaySuccessViewController.h"
#import "QrCodeViewController.h"
#import "PayFailViewController.h"
@interface OrderViewController ()<FaceDetectorDelegate>
{
    NSString *payType;//支付方法
    float amount;//总金额
    NSString *timeString;//交易单号
    NSString *tky_amount;//tky总金额
    NSTimer *timer;
    NSInteger k;
}
@end

@implementation OrderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    payType = @"1";
    [self customUI];
}

#pragma mark - UI
- (void)customUI
{
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    if (IsiPhoneX) {
        _statusView.height = StatusBarHeight;
        _navigationView.centerY = _navigationView.centerY + StatusBarHeight - 20;
        _topView.centerY = _topView.centerY + StatusBarHeight - 20;
        _backView.frame = CGRectMake(0, HEIGHT(149) + StatusBarHeight - 20, SCREEN_WIDTH, SCREEN_HEIGHT - (HEIGHT(149) + StatusBarHeight - 20));
        _bottomView.frame = CGRectMake(0, _backView.height - _bottomView.height, SCREEN_WIDTH, _bottomView.height);
        
    }
    NSString *conStr = @"￥354";
    NSRange range1 = [conStr rangeOfString:@"￥"];
    NSMutableAttributedString *attribute1 = [[NSMutableAttributedString alloc]initWithString:conStr];
    [attribute1 addAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:HEIGHT(11)]} range:range1];
    [self.moneyLabel setText:conStr];
    [self.moneyLabel setAttributedText:attribute1];
    _backButton.layer.cornerRadius = 8;
    _backButton.layer.borderWidth = 1;
    _backButton.layer.borderColor = [UIColorFromRGB(0x636363) CGColor];
    _payButton.layer.cornerRadius = 8;
    _payButton.layer.borderWidth = 1;
    _payButton.layer.borderColor = [UIColorFromRGB(0x2195b4) CGColor];
    self.firstLAbel.text = @"商品详述：诊疗费用";
//    NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
//    NSTimeInterval a=[dat timeIntervalSince1970]*1000;
    timeString = [NSString stringWithFormat:@"%@%@%@",[Single shareSingle].hospId,[Single shareSingle].treatmentId,[self getCurrentTimeStr]];
    self.secondLabel.text = [NSString stringWithFormat:@"交易单编号：%@%@%@",[Single shareSingle].hospId,[Single shareSingle].treatmentId,[self getCurrentTimeStr]];
    self.thirdLabel.text = [NSString stringWithFormat:@"收款方：%@",[Single shareSingle].hospName];
    
    float personalPay = 0;
    float yibaoPay = 0;
    for (NSInteger idx = 0; idx < self.array.count; idx++) {
        OrderMessageModel *model = self.array[idx];
        personalPay = personalPay + [model.payActully floatValue];
        yibaoPay = yibaoPay + [model.reimbursement floatValue];
    }
    amount = personalPay;

    self.fourthLabel.text = [NSString stringWithFormat:@"医保统筹支付：￥%.2lf",personalPay];
    self.fifthLabel.text = [NSString stringWithFormat:@"个人支付：￥%.2lf",yibaoPay];
    _moneyLabel.text = [NSString stringWithFormat:@"总计：￥%.2lf",personalPay + yibaoPay];
    _shulianButton.layer.cornerRadius = 8;
    _zhiwenButton.layer.cornerRadius = 8;

    [self initUI];
    self.passwordView.frame = CGRectMake((SCREEN_WIDTH - self.passwordView.width)/2, HEIGHT(115), self.passwordView.width, self.passwordView.height);
    self.passwordView.layer.cornerRadius = 10;
    
    self.enterView.layer.cornerRadius = 5;
    self.enterView.layer.borderWidth = 0.5;
    self.enterView.layer.borderColor = [UIColorFromRGB(0x000000) CGColor];
    [self.zhifuTF addTarget:self
                     action:@selector(textFieldDidChange:)
           forControlEvents:UIControlEventEditingChanged];
    
    [self requestChangeTKYData];
    k = 60;
    timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(changeLabel) userInfo:nil repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSDefaultRunLoopMode];
    
}

#pragma mark - Label变换
- (void)changeLabel
{
    if (k == 0) {
        [timer invalidate];
        [self.navigationController popViewControllerAnimated:YES];
    } else {
        self.timeLabel.text = [NSString stringWithFormat:@"%ld秒后自动取消支付",k];
        k--;
    }
}

- (void)textFieldDidChange:(id)sender
{
    for (NSInteger idx = 0; idx < self.zhifuTF.text.length; idx++) {
        
        UIImageView *imageView = (id)[self.enterView viewWithTag:80 + idx];
        imageView.backgroundColor = UIColorFromRGB(0x333333);
        //        imageView.image = [UIImage imageNamed:@"syuan"];
        for (NSInteger idy = idx + 1; idy < 6; idy++) {
            UIImageView *imageView = (id)[self.enterView viewWithTag:80 + idy];
            //            imageView.image = [UIImage imageNamed:@"yuan0"];
            imageView.backgroundColor = [UIColor whiteColor];
            
        }
    }
    
    if (self.zhifuTF.text.length == 0) {
        for (NSInteger idy = 0; idy < 6; idy++) {
            UIImageView *imageView = (id)[self.enterView viewWithTag:80 + idy];
            //            imageView.image = [UIImage imageNamed:@"yuan0"];
            imageView.backgroundColor = [UIColor whiteColor];
            
        }
    }
    
    if (self.zhifuTF.text.length == 6) {
        [self.zhifuTF resignFirstResponder];
        UIWindow *window = [UIApplication sharedApplication].keyWindow;
        UIView *blackView = [window viewWithTag:100];
        [blackView removeFromSuperview];
        [self passwordRequestData];
        
    }
    
    
    NSLog(@"%@", self.zhifuTF.text);
}

#pragma mark - 画圈
- (void)initUI
{
    CGFloat danwei = self.enterView.width/6;
    CGFloat ge = (danwei - HEIGHT(10))/2;
    
    for (NSInteger idx = 0; idx < 6; idx++) {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(ge + idx * danwei, ge, HEIGHT(10), HEIGHT(10))];
        [self.enterView addSubview:imageView];
        imageView.backgroundColor = [UIColor whiteColor];
        imageView.layer.cornerRadius = HEIGHT(10)/2;
        imageView.layer.masksToBounds = YES;
        imageView.tag = 80 + idx;
        imageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onTap)];
        tap.numberOfTouchesRequired = 1;
        [imageView addGestureRecognizer:tap];
    }
}

#pragma mark - 单击圆圈
- (void)onTap
{
    [self.zhifuTF becomeFirstResponder];
}

#pragma mark - 返回
- (IBAction)back:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - 支付选择
- (IBAction)payWay:(UIButton *)sender {
    if (sender.tag == 1) {
        payType = @"1";
        self.firstIV.image = [UIImage imageNamed:@"4"];
        self.secondIV.image = [UIImage imageNamed:@"3"];
        self.thirdIV.image = [UIImage imageNamed:@"3"];

    } else if (sender.tag == 2) {
        payType = @"2";
        self.firstIV.image = [UIImage imageNamed:@"3"];
        self.secondIV.image = [UIImage imageNamed:@"4"];
        self.thirdIV.image = [UIImage imageNamed:@"3"];
    } else if (sender.tag == 3) {
        payType = @"3";
        self.firstIV.image = [UIImage imageNamed:@"3"];
        self.secondIV.image = [UIImage imageNamed:@"3"];
        self.thirdIV.image = [UIImage imageNamed:@"4"];
    }
}

#pragma mark - 付款
- (IBAction)pay:(id)sender {
    if ([self.timeLabel.text isEqualToString:@"00:00:00"]) {
        [JKToast showWithText:@"支付时间超时"];
        [self.navigationController popViewControllerAnimated:YES];
        
    } else {
        if ([payType isEqualToString:@"1"]) {
            UIWindow *window = [UIApplication sharedApplication].keyWindow;
            UIView *backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, ScreenWidth, ScreenHeight)];
            backView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.2f];
            backView.tag = 100;
            [window addSubview:backView];
            
            UIButton *button = [[UIButton alloc] initWithFrame:backView.frame];
            [button addTarget:self action:@selector(delete:) forControlEvents:UIControlEventTouchUpInside];
            [backView addSubview:button];
            
            self.payView.centerX = SCREEN_WIDTH/2;
            self.payView.centerY = HEIGHT(185) + self.payView.height/2;
//            [CrazyAutoLayout frameOfSuperView:self.payView];
            
            [backView addSubview:self.payView];
        } else if ([payType isEqualToString:@"2"]) {
            QrCodeViewController *vc = [[QrCodeViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        } else if ([payType isEqualToString:@"3"]) {
            QrCodeViewController *vc = [[QrCodeViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
    }
    
    
    
}

#pragma mark - delete
- (void)delete:(UIButton *)button
{
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    UIView *backView = [window viewWithTag:100];
    [backView removeFromSuperview];
}

#pragma mark - 退出tky页
- (IBAction)exitTKYView:(id)sender {
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    UIView *backView = [window viewWithTag:100];
    [backView removeFromSuperview];
}

#pragma mark - 使用密码
- (IBAction)usePassword:(id)sender {
    [self.payView removeFromSuperview];
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    UIView *backView = [window viewWithTag:100];
    [backView addSubview:self.passwordView];
    [self.zhifuTF becomeFirstResponder];
}


#pragma mark - 刷脸支付
- (IBAction)shualian:(UIButton *)sender {
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    UIView *backView = [window viewWithTag:100];
    [backView removeFromSuperview];
    FaceStreamDetectorViewController *faceVC = [[FaceStreamDetectorViewController alloc]init];
    faceVC.faceDelegate = self;
    [self.navigationController pushViewController:faceVC animated:YES];
}

-(void)sendFaceImage:(UIImage *)faceImage
{
    [self brushfaceRequestData:faceImage];
}

#pragma mark - 刷脸支付数据请求
- (void)brushfaceRequestData:(UIImage *)image
{
    NSMutableArray *prescriptionList = [NSMutableArray array];
    for (NSInteger idx = 0; idx < self.array.count; idx++) {
        OrderMessageModel *model = self.array[idx];
        [prescriptionList addObject:model.prescriptionId];
    }
    
    NSString *string = [prescriptionList componentsJoinedByString:@","];
    NSString *prescriptionListStr = [NSString stringWithFormat:@"[%@]",string];
    
    NSDictionary *parameters = @{@"hospId":[Single shareSingle].hospId,@"deviceId":PhoneNum,@"dateTime":[self getCurrentTime],@"idNumber":[Single shareSingle].idNumber,@"treatmentId":[Single shareSingle].treatmentId,@"transactionNO":timeString,@"prescriptionList":string,@"TKY":tky_amount};
    NSDictionary *imageDic = @{@"image":[CrazyNetWork CrazyBase64Str:image scale:1]};
    [CrazyNetWork CrazyHttpBase64Upload:[NSString stringWithFormat:@"%@payByPicture",defaultUrl] HUD:YES imageDic:imageDic parameters:parameters block:^(NSDictionary *dic, NSString *url, NSString *Json) {
        BaseModel *baseModel = [BaseModel objectWithKeyValues:dic];
        if ([baseModel.statusCode integerValue] == 200) {
            NSString *balance = [NSString stringWithFormat:@"%@",baseModel.returnObj[@"balance"]];
            PaySuccessViewController *vc = [[PaySuccessViewController alloc] init];
            vc.balance = balance;
            [self.navigationController pushViewController:vc animated:YES];
        } else {
            PayFailViewController *vc = [[PayFailViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
//        [JKToast showWithText:dic[@"message"]];
    } fail:^(NSError *error, NSString *url) {
        
    }];
}


#pragma mark - 密码支付数据请求
- (void)passwordRequestData
{
    NSMutableArray *prescriptionList = [NSMutableArray array];
    for (NSInteger idx = 0; idx < self.array.count; idx++) {
        OrderMessageModel *model = self.array[idx];
        [prescriptionList addObject:model.prescriptionId];
    }
    NSString *string = [prescriptionList componentsJoinedByString:@","];
    NSString *prescriptionListStr = [NSString stringWithFormat:@"[%@]",string];
    
    NSDictionary *parameters = @{@"hospId":[Single shareSingle].hospId,@"deviceId":PhoneNum,@"dateTime":[self getCurrentTime],@"idNumber":[Single shareSingle].idNumber,@"treatmentId":[Single shareSingle].treatmentId,@"transactionNO":timeString,@"prescriptionList":string,@"TKY":tky_amount,@"password":self.zhifuTF.text};
    [CrazyNetWork CrazyTokenHeadRequest_Post:[NSString stringWithFormat:@"%@payByPassword",defaultUrl] HUD:YES parameters:parameters success:^(NSDictionary *dic, NSString *url, NSString *Json) {
        BaseModel *baseModel = [BaseModel objectWithKeyValues:dic];
        if ([baseModel.statusCode integerValue] == 200) {
            NSString *balance = [NSString stringWithFormat:@"%@",baseModel.returnObj[@"balance"]];
            PaySuccessViewController *vc = [[PaySuccessViewController alloc] init];
            vc.balance = balance;
            [self.navigationController pushViewController:vc animated:YES];
        } else {
            PayFailViewController *vc = [[PayFailViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
        
    } fail:^(NSError *error, NSString *url) {
        
    }];
}



#pragma mark - TKY换算数据请求
- (void)requestChangeTKYData
{
    NSDictionary *parameters = @{@"hospId":[Single shareSingle].hospId,@"deviceId":PhoneNum,@"dateTime":[self getCurrentTime],@"amount":[NSString stringWithFormat:@"%.2lf",amount]};
    [CrazyNetWork CrazyTokenHeadRequest_Post:[NSString stringWithFormat:@"%@exchangeToTKY",defaultUrl] HUD:YES parameters:parameters success:^(NSDictionary *dic, NSString *url, NSString *Json) {
        BaseModel *baseModel = [BaseModel objectWithKeyValues:dic];
        if ([baseModel.statusCode integerValue] == 200) {
            self->tky_amount = [NSString stringWithFormat:@"%@",baseModel.returnObj[@"tky"]];
            self.tkyLabel.text = [NSString stringWithFormat:@"TKY%@",baseModel.returnObj[@"tky"]];
        } else {
            
        }
        
    } fail:^(NSError *error, NSString *url) {
        
    }];
}

#pragma mark - 获取当前时间
- (NSString *)getCurrentTime
{
    NSDate *currentDate = [NSDate date];//获取当前时间，日期
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"YYYY-MM-dd"];
    NSString *dateString = [dateFormatter stringFromDate:currentDate];
    NSLog(@"dateString:%@",dateString);
    return dateString;
}

#pragma mark - 获取当前时间
- (NSString *)getCurrentTimeStr
{
    NSDate *currentDate = [NSDate date];//获取当前时间，日期
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"YYYYMMddHHmmss"];
    NSString *dateString = [dateFormatter stringFromDate:currentDate];
    NSLog(@"dateString:%@",dateString);
    return dateString;
}

@end
