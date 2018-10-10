//
//  CrazyConfiguration.h
//  ocCrazy
//
//  Created by dukai on 16/1/5.
//  Copyright © 2016年 dukai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#define appStoreId @"1149330157"
#define itunesUrl @"https://itunes.apple.com/cn/app/you-piao/id1149330157?mt=8"

typedef enum {
    center,
    under
}ToastEnum;

@interface CrazyConfiguration : NSObject

@property(nonatomic,strong)NSString * backBtnImage_NAME;
@property(nonatomic) CGRect backBtnImage_FRAME;
@property(nonatomic)ToastEnum ToastLocaton;
+ (instancetype)sharedManager;

@end
