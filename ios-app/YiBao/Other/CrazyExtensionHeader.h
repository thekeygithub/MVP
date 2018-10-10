//
//  CrazyExtensionHeader.h
//  ocCrazy
//
//  Created by dukai on 16/1/4.
//  Copyright © 2016年 dukai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UIImageView+WebCache.h"

//UIApplication
@interface UIApplication(crazyApplication)

+(NSString *)crazy_uuid;
+(NSString *)crazy_bundleId;
+(NSString *)crazy_bundleName;
+(NSString *)crazy_appVersion;
+(NSString *)crazy_appBuild;

//跳转到设置页面 iOS8之前不可用
+(BOOL)crazy_pushToSetting;
//跳转到appstore 进行评价 itmsUrl 传值 @"itunes.apple.com/gb/app/yi-dong-cai-bian/id391945719?mt=8"
+(BOOL)crazy_pushToAppStore:(NSString *)itmsUrl;
//拨打电话
+ (void)crazy_OpenUrl_Tel:(NSString *)tel;
//发送短信
+ (void)crazy_OpenUrl_SMS:(NSString *)sms;
//打开网址
+ (void)crazy_OpenUrl_http:(NSString *)http;

@end
//UIView
@interface UIView(crazyView)

-(void)crazy_cornerRadius:(float)Radius;//圆角
-(void)crazy_cornerLineWidth:(float)Width LineColor:(UIColor *)color;//线边
@end

//UIImageView
@interface UIImageView(crazyImageView)

-(void)crazy_setImageWithURL:(NSString *)url;
-(void)crazy_setImageWithURL:(NSString *)url placeholder:(NSString *)placeholder;
-(void)crazy_setImageWithURL:(NSString *)url placeholder:(NSString *)placeholder optionInfo:(SDWebImageOptions)options;
+(void)crazy_ImageClearCache;

@end

//UITableView
@interface UITableView(crazyTableView)
/**cell 清除分割线*/
-(void)crazy_clearExtraCellLine;
-(void)crazy_gourpExtraTopSpace;

@end

//UITableViewCell
@interface UITableViewCell(crazyTableViewCell)

-(void)crazy_addSegmentLine:(UIColor *)color x:(float)x;//cell分割线

@end

//UIWebView
@interface UIWebView(crazyWebView)

-(float)crazy_webViewHeight;//webView 高度

@end

typedef enum {
    horizontal,//宽度
    vertical //高度
}direction;
//UILabel
@interface UILabel(crazyLabel)
/** 自动返回label的高度或者 长度*/
-(CGSize)crazy_text:(NSString *)text Auto:(direction)direction;
@end

//UIColor
@interface UIColor(crazyColor)

+(UIColor *)crazy_R:(CGFloat)red G:(CGFloat)green B:(CGFloat)blue A:(CGFloat)alpha;
+(UIColor *)crazy_hex:(NSString *)hex A:(CGFloat)alpha;
+(UIColor *)crazy_hex:(NSString *)hex;

@end

//NSString
@interface NSString(crazyStr)
/** 返回字符串的长度*/
-(float)crazy_returnTextWidth:(float)fontSize;
/** 返回字符串的高度*/
-(float)crazy_returnFontSize:(float)fontSize ViewWidth:(float)width;
/** 截取字符串*/
- (NSString *)crazy_subStringStart:(NSString *)start StringEnd:(NSString *)end;
@end

