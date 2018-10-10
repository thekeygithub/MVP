//
//  CrazyConfiguration.m
//  ocCrazy
//
//  Created by dukai on 16/1/5.
//  Copyright © 2016年 dukai. All rights reserved.
//
/*
                       @@@
                       @@@
                       @@@
                       @@@
                       @@@
              @@@@@@@@@@@@@@@@@@@@@
               @@@@@@@@@@@@@@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                     @@@@@@@
                      @@@@@
                       @@@
                        @
 
               宝物镇楼中，代码保平安
               项目很顺利，完全无压力
               工期提前完，尾款提前结
               精力特别足，加班轻松事
*/

#import "CrazyConfiguration.h"
#import "IQKeyboardManager.h"
#import "SVProgressHUD.h"
#import "CrazyGuideView.h"
#import "ZMYVersionNotes.h"
#import "UIAlertView+BlocksKit.h"
#import "SVWebViewController.h"
#import "Appirater.h"
#import "CrazyCache.h"

@implementation CrazyConfiguration

+ (instancetype)sharedManager {
    static CrazyConfiguration *_sharedManager = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _sharedManager = [[self alloc] init];
    });
    
    return _sharedManager;
}
- (instancetype)init
{
    self = [super init];
    if (self) {
        [self initCrazyConfiguration];
    }
    return self;
}

-(void)initCrazyConfiguration{
//    [CrazyDB shareDBName:@"mydatabase.sqlite"];//初始化数据库
    [self ConfigGuide];//初始化导航页
    [self ConfigBackBtn]; //初始化返回按钮
    [self ConfigTMCache]; //初始化缓存时间
    [self ConfigIQKeyboard];//初始化键盘
    [self ConfigSVProgressHUD];//初始化挡板
    [self ConfigToast];//初始化Toast
    [self ConfigAppirater];//初始化app打分提示
  //  [self ConfigVersion];//初始化版本升级提示
    
}
-(void)ConfigGuide{
    
    if ([[NSUserDefaults standardUserDefaults]objectForKey:@"Guide"] == nil) {
        CrazyGuideView * guide = [[CrazyGuideView alloc]init];
        guide.pageCtr.hidden = YES;
        [guide createLocationImageArr:@[@"引导页1",@"引导页2"] block:^{
            
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"Guide"];
        }];
    }
}
-(void)ConfigBackBtn{
    self.backBtnImage_NAME = @"Tback.png";
    self.backBtnImage_FRAME = CGRectMake(0, 0, 20, 30);
}
-(void)ConfigTMCache{
    //几分钟清除缓存
    int time = 600; //秒
    CrazyCache * cache =[CrazyCache sharedCache];
    cache.diskCache.ageLimit = time;
    cache.memoryCache.ageLimit =time;
}
-(void)ConfigIQKeyboard{
    
    IQKeyboardManager * manager  = [IQKeyboardManager sharedManager];
    manager.enable = YES ;
    manager.shouldResignOnTouchOutside = YES;
    manager.shouldToolbarUsesTextFieldTintColor = YES;
    manager.enableAutoToolbar = YES;
    manager.toolbarManageBehaviour =IQAutoToolbarByPosition;

}
-(void)ConfigSVProgressHUD{
    
    [SVProgressHUD setDefaultAnimationType:SVProgressHUDAnimationTypeFlat];
    [SVProgressHUD setDefaultStyle:SVProgressHUDStyleDark];
    [SVProgressHUD setDefaultMaskType:SVProgressHUDMaskTypeOpaque];
    
//    UIView *custumView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
//    custumView.backgroundColor = [UIColor redColor];
//    [SVProgressHUD setViewForExtension:custumView];
}
-(void)ConfigToast{
    //center 中间位置  under下面位置
    self.ToastLocaton = under;
}
-(void)ConfigVersion{
    
    [CrazyNetWork CrazyRequest_Get:@"https://itunes.apple.com/lookup" HUD:YES parameters:@{@"id":appStoreId} success:^(NSDictionary *dic, NSString *url, NSString *Json) {
        
        NSDictionary *tempDic = dic[@"results"][0];
        NSString * releaseNotes = tempDic[@"releaseNotes"];
        NSString *version = tempDic[@"version"];
        
//        float localVersion = [self versionToFloat:[UIApplication crazy_appVersion]];
        float itunesVersion = [self versionToFloat:version];
        
//        if (localVersion < itunesVersion) {
//
//            [self updateAlert:releaseNotes Version:version];
//        }
        
    } fail:^(NSError *error, NSString *url) {
        
    }];
}

-(void)updateAlert:(NSString *)releaseNotes Version:(NSString *)version{
    
    UIAlertView *alert = [UIAlertView bk_showAlertViewWithTitle:[NSString stringWithFormat:@"有新版本:%@", version] message:releaseNotes cancelButtonTitle:@"前往更新" otherButtonTitles:@[] handler:^(UIAlertView *alertView, NSInteger buttonIndex) {
        
//        [UIApplication crazy_pushToAppStore:itunesUrl];
        [self updateAlert:releaseNotes Version:version];
        
    }];
    
    [alert show];
}
-(float)versionToFloat:(NSString *)version{
    
    float itunesVersion = 0.0;
    
    NSRange fRange = [version rangeOfString:@"."];
    if(fRange.location != NSNotFound){
        NSString * sVersion = [version stringByReplacingOccurrencesOfString:@"." withString:@""];
        NSMutableString *mVersion = [NSMutableString stringWithString:sVersion];
        [mVersion insertString:@"." atIndex:fRange.location];
        itunesVersion = [mVersion floatValue];
    }else {
        // 版本应该有问题(由于ios 的版本 是7.0.1，没有发现出现过没有小数点的情况)
        itunesVersion = [version floatValue];
    }

    return itunesVersion;
}

-(void)ConfigAppirater{
    [Appirater setAppId:@"1045380671"];
    [Appirater setDaysUntilPrompt:7];
    [Appirater setUsesUntilPrompt:5];
    [Appirater setSignificantEventsUntilPrompt:-1];
    [Appirater setTimeBeforeReminding:2];
    [Appirater setDebug:NO];
    [Appirater appLaunched:YES];
}
@end
