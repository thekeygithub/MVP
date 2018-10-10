//
//  FaceRecognitionViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "FaceRecognitionViewController.h"
#import "SuccessViewController.h"
#import "FailViewController.h"
@interface FaceRecognitionViewController ()
{
    NSDictionary * passdic;
    NSArray *dataArray;
    NSTimer *timer;
    NSInteger k;
}
@end

@implementation FaceRecognitionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    dataArray = @[@"正在验证社保卡头像信息",@"正在验证人脸识别",@"正在验证社保卡状态信息",@"正在进行生物体征判断",@"正在验证公安数据源信息",@"正在验证公安数据源信息",@"正在验证国家机密特殊诊断信息",@"正在验证国家机密特殊人员信息",@"正在验证位置信息",@"正在验证CPA信息"];
    k = 0;
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    self.personalPicIV.image = self.image;
    self.progressLabel.layer.borderColor = [UIColorFromRGB(0x27ccfd) CGColor];
    self.progressLabel.layer.borderWidth = 1;
    [self requestData];
    timer = [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(changeLabel) userInfo:nil repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSDefaultRunLoopMode];
    [UIView animateWithDuration:30.0f animations:^{
        self.progressView.width = HEIGHT(180);
    } completion:^(BOOL finished) {
    }];
    
}

#pragma mark - Label变换
- (void)changeLabel
{
    if (k < dataArray.count) {
        self.messageLabel.text = dataArray[k];
        k++;
    } else {
        [timer invalidate];
    }
}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
    int index = (int)[[self.navigationController viewControllers]indexOfObject:self];
    [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:1] animated:YES];
}

#pragma mark - 成功
- (IBAction)success:(id)sender {
    SuccessViewController *vc = [[SuccessViewController alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - 验证数据接口请求
- (void)requestData
{
//    UIImage *image = [UIImage imageNamed:@"成功"];
    NSDictionary *imageDic = @{@"image":[CrazyNetWork CrazyBase64Str:self.image scale:1]};
    NSDictionary *parameters = @{@"hospId":[Single shareSingle].model.hospId,@"deviceId":PhoneNum,@"dateTime":[self getCurrentTime],@"treatmentId":[Single shareSingle].model.treatmentId,@"idNumber":[Single shareSingle].model.idNumber};
    [CrazyNetWork CrazyHttpBase64Upload:[NSString stringWithFormat:@"%@verifyUser",defaultUrl] HUD:YES imageDic:imageDic parameters:parameters block:^(NSDictionary *dic, NSString *url, NSString *Json) {
        BaseModel *baseModel = [BaseModel objectWithKeyValues:dic];
        if ([baseModel.statusCode integerValue] == 200) {
            [self->timer invalidate];
            self->passdic = baseModel.returnObj[@"prescriptions"];
            SuccessViewController *vc = [[SuccessViewController alloc] init];
            vc.dic = self->passdic;
            [self.navigationController pushViewController:vc animated:YES];
        } else {
            [self->timer invalidate];
            FailViewController *vc = [[FailViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
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

@end
