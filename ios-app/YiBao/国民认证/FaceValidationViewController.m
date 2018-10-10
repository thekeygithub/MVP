//
//  FaceValidationViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/7.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "FaceValidationViewController.h"
#import <HTJCFaceLiveDetectSdk/THIDMCHTJCViewManger.h>
#import "HTJCBeginView.h"
#import <Accelerate/Accelerate.h>
#import <AVFoundation/AVFoundation.h>
#import "AppDelegate.h"
#import "ShakedViewController.h"
#import "FaceRecognitionViewController.h"

@interface FaceValidationViewController ()<UIAlertViewDelegate>
{
    /*
     * yes 退入后台，保持界面
     * no  退入后台，初始界面
     */
    UIAlertView *_promptView;
    NSInteger imageCount;
    NSTimeInterval beginTimeInterval;
    AppDelegate *_appDelegate;
}

@property(nonatomic,strong) HTJCBeginView *faceView;
@property (nonatomic, strong) UIViewController *rootViewCtr;
@property (nonatomic, strong) UIView *loadingView;
@property (nonatomic, strong) UILabel *naviTitleLb;
@property (nonatomic, strong) ShakedViewController *shakeVC;
@end

@implementation FaceValidationViewController
- (instancetype)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.navigationController.navigationBar.hidden = YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self setDetect];

    self.navigationController.navigationBar.hidden = NO;
    self.navigationController.navigationBar.barTintColor = [UIColor blackColor];
    
    _naviTitleLb = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 200, 40)];
    _naviTitleLb.text = @"身份检测";
    _naviTitleLb.font = [UIFont boldSystemFontOfSize:22];
    _naviTitleLb.textColor = [UIColor whiteColor];
    _naviTitleLb.textAlignment = NSTextAlignmentCenter;
    self.navigationItem.titleView = _naviTitleLb;
    
    [self addSubView];
    _shakeVC = [[ShakedViewController alloc] initWithNibName:@"ShakedViewController" bundle:[NSBundle mainBundle]];
    [self becomeFirstResponder];
}

#pragma mark - 摇一摇相关方法
// 摇一摇开始摇动
- (void)motionBegan:(UIEventSubtype)motion withEvent:(UIEvent *)event {
    NSLog(@"开始摇动");
    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
    return;
}

// 摇一摇取消摇动
- (void)motionCancelled:(UIEventSubtype)motion withEvent:(UIEvent *)event {
    NSLog(@"取消摇动");
    return;
}

// 摇一摇摇动结束
- (void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event {
    if (event.subtype == UIEventSubtypeMotionShake) { // 判断是否是摇动结束
        NSLog(@"摇动结束");
        //        [self performSelector:@selector(presentViewController:animated:completion:) withObject:_shakeVC afterDelay:0.5];
        [self presentViewController:_shakeVC animated:YES completion:nil];
    }
    return;
}

- (void)addSubView
{
    self.faceView = [[HTJCBeginView alloc]init];
    [self.view addSubview:_faceView];

    __weak typeof(self) weakSelf = self;
    _faceView.beginBlock = ^(){
        [weakSelf setDetect];
        //        [ViewControllerD HTJC:self];
    };

    _faceView.backBlock = ^() {
        [weakSelf.faceView changeToBeginView];
        weakSelf.naviTitleLb.text = @"身份检测";

    };
    _faceView.quitBlock = ^() {
        [weakSelf.faceView changeToBeginView];
        weakSelf.naviTitleLb.text = @"身份检测";

    };
    _faceView.againBlock = ^() {
        weakSelf.faceView.beginBlock();
    };
}

//调用SDK
-(void)setDetect {
    beginTimeInterval = [NSDate date].timeIntervalSince1970*1000;//毫秒
    THIDMCHTJCViewManger *manager = [THIDMCHTJCViewManger sharedManager:self];
    
    NSString *sdkVersion = [manager getSDKVersion];
    NSString *bundleVersion = [manager getBundleVersion];
    
    _appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    NSMutableArray *detectTypes = [NSMutableArray arrayWithArray:@[@1,@2,@1]];
    if (_appDelegate.liveDetectTypes && _appDelegate.liveDetectTypes.count != 0) {
        detectTypes = [NSMutableArray arrayWithArray:_appDelegate.liveDetectTypes];
    }
    //是否随机
    BOOL isRandom = [[detectTypes lastObject] boolValue];
    [detectTypes removeLastObject];
    
    if (!_appDelegate.liveDetectTypes || _appDelegate.liveDetectTypes.count == 0) {
        detectTypes = [self random:detectTypes withRandomNum:1];
    }
    
    [manager navigationType:0];
    [manager isNeedRandom:NO];
    [manager liveDetectTypeArray:detectTypes];
    //    [self redirectNSlogToDocumentFolder];
    [manager getLiveDetectCompletion:^(BOOL sueccess, NSData *imageData) {
        
        //及时释放
        [manager dismissTakeCaptureSessionViewController];
        UIImage *image = [UIImage imageWithData:imageData];
        [self.faceView changeToSuccessView:image];
        self->_naviTitleLb.text = @"检测结果";
        FaceRecognitionViewController *vc = [[FaceRecognitionViewController alloc] init];
        vc.image = image;
        [self.navigationController pushViewController:vc animated:YES];
        [self writeToSandBox:imageData];
    }
                              cancel:^(BOOL sueccess, NSError *error) {
                                  
                                  [self.faceView changeToBeginView];
                                  self->_naviTitleLb.text = @"身份检测";
                              }
                              failed:^(NSError *error,NSData *imageData) {//ps:imageData 可能为 nil
//
                                  UIImage *failedImage = [UIImage imageWithData:imageData];
                                  NSString *errorInfo = [self errorToMessage:error];
                                  NSLog(@"failedImage:%@,%ld",failedImage,imageData.length);
                                  [self.faceView changeToFailView:failedImage failedInfo:errorInfo];
                                  self->_naviTitleLb.text = @"检测结果";
                                  [self writeToSandBox:imageData];
                                  
                              }];
}


-(NSString *)movementWithType:(NSString *)movementType{
    NSString *movement = @"";
    if ([movementType isEqualToString:@"0"]) {
        movement = @"凝视";
    }else
        if ([movementType isEqualToString:@"1"]){
        movement = @"摇头";
    }else if ([movementType isEqualToString:@"2"]){
        movement = @"点头";
    }
    else if ([movementType isEqualToString:@"3"]){
        movement = @"张嘴";
    }
    else if ([movementType isEqualToString:@"4"]){
        movement = @"眨眼";
    }
    else{
        movement = @"监测中";// 在动作切换之间的时间段
    }
    return movement;
    
}

//检测失败错误信息处理
-(NSString *)errorToMessage:(NSError *)error{
    NSString *errorInfo = error.domain;//错误信息: 包括 错误描述 和 具体的错误动作,以":"分开,请注意分开来取
    NSInteger errorCode = error.code;//错误码(可选)
    NSTimeInterval currentTimerval = [NSDate date].timeIntervalSince1970*1000;
    float time = currentTimerval - beginTimeInterval;//检测耗时(可选)
    NSRange range = [errorInfo rangeOfString:@":"];
    if (range.length>0) {
        NSString *movementType = [errorInfo substringFromIndex:range.location+1];
        movementType = [self movementWithType:movementType];//具体的错误动作(可选)
        NSString *failedStr = [errorInfo substringToIndex:range.location];//错误描述(可选)
        //错误描述 + 错误码 + 具体的错误动作 + 检测耗时. 需要如何显示可自行调整.
        errorInfo = [NSString stringWithFormat:@"%@:%ld\n失败动作是:%@\n总耗时:%.f毫秒",failedStr,errorCode,movementType,time];
    }else{
        //错误描述 + 错误码
        errorInfo = [NSString stringWithFormat:@"%@:%ld",errorInfo,errorCode];
    }
    return errorInfo;
    
}


//存入沙盒
-(void)writeToSandBox:(NSData *)imageData{
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *testDirectory = [documentsDirectory stringByAppendingPathComponent:@"/HTJCLiveDetect/unDownLoad"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL res=[fileManager createDirectoryAtPath:testDirectory withIntermediateDirectories:YES attributes:nil error:nil];
    if (!res) NSLog(@"文件夹创建失败");
    else {
        NSString *name = [self GetTimeInterval];
        NSString *testPath = [testDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.jpg",name]];
        NSLog(@"--------testPath:%@----------",testPath);
        BOOL res = [imageData writeToFile:testPath atomically:YES];
        if (!res){
            NSLog(@"文件写入失败");
        }
    }
    
}

// 将NSlog打印信息保存到Document目录下的文件中
- (void)redirectNSlogToDocumentFolder
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDirectory = [paths objectAtIndex:0];
    NSString *fileName = [NSString stringWithFormat:@"blinkEyesCrash.log"];// 注意不是NSData!
    NSString *logFilePath = [documentDirectory stringByAppendingPathComponent:fileName];
    // 先删除已经存在的文件
    NSFileManager *defaultManager = [NSFileManager defaultManager];
    [defaultManager removeItemAtPath:logFilePath error:nil];
    
    // 将log输入到文件
    freopen([logFilePath cStringUsingEncoding:NSASCIIStringEncoding], "a+", stdout);
    freopen([logFilePath cStringUsingEncoding:NSASCIIStringEncoding], "a+", stderr);
}

//获取当前时间
-(NSString *)GetTimeInterval{
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"YYYYMMddHHmmss"];
    NSString *currentTime = [formatter stringFromDate:[NSDate date]];
    return currentTime;
}

//随机数组
- (NSMutableArray *)random:(NSArray *)array withRandomNum:(NSInteger)num
{
    NSMutableArray *startArray = [[NSMutableArray alloc] initWithArray:array];
    //随机数产生结果
    NSMutableArray *resultArray=[[NSMutableArray alloc] initWithCapacity:0];
    //随机数个数
    NSInteger m=num;
    for (int i=0; i<m; i++) {
        int t=arc4random()%startArray.count;
        resultArray[i]=startArray[t];
        startArray[t]=[startArray lastObject]; //为更好的乱序，故交换下位置
        [startArray removeLastObject];
    }
    return resultArray;
}

@end
