//
//  CrazyExtensionHeader.m
//  ocCrazy
//
//  Created by dukai on 16/1/4.
//  Copyright © 2016年 dukai. All rights reserved.
//

#import "CrazyExtensionHeader.h"
//#import "UIColor+Chameleon.h"
#import "SDImageCache.h"

static UILabel *staticLabel = nil;
@implementation UIApplication(crazyApplication)

+(NSString *)crazy_uuid{
    return [UIDevice currentDevice].identifierForVendor.UUIDString;
}
+(NSString *)crazy_bundleId{
    return [[NSBundle mainBundle].infoDictionary objectForKey:(NSString *)kCFBundleIdentifierKey];
}
+(NSString *)crazy_bundleName{
    return [[NSBundle mainBundle].infoDictionary objectForKey:(NSString *)kCFBundleNameKey];
}
+(NSString *)crazy_appVersion{
   return [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
}
+(NSString *)crazy_appBuild{
    return [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
}

+(BOOL)crazy_pushToSetting{
    // iOS8 之前不可用
    return [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
}
+(BOOL)crazy_pushToAppStore:(NSString *)itmsUrl{
    NSString *url = [NSString stringWithFormat:@"itms-apps ://%@",itmsUrl];
    return  [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}
+ (void)crazy_OpenUrl_Tel:(NSString *)tel{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",tel]]];
}
+ (void)crazy_OpenUrl_SMS:(NSString *)sms{
    [[UIApplication sharedApplication]openURL:[NSURL URLWithString:[NSString stringWithFormat:@"sms://%@",sms]]];
}
+ (void)crazy_OpenUrl_http:(NSString *)http{
    if ([[http substringToIndex:4]isEqualToString:@"http"]) {
        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:http]];
    }else{
        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://%@",http]]];
    }
}

@end

@implementation UIView(crazyView)

-(void)crazy_cornerRadius:(float)Radius{
    self.layer.cornerRadius = Radius;
    self.clipsToBounds = YES;
}
-(void)crazy_cornerLineWidth:(float)Width LineColor:(UIColor *)color{
    self.layer.borderWidth = Width;
    self.layer.borderColor = color.CGColor;
}
@end

@implementation UIImageView(crazyImageView)

-(void)crazy_setImageWithURL:(NSString *)url{
    [self sd_setImageWithURL:[NSURL URLWithString:url]];
}
-(void)crazy_setImageWithURL:(NSString *)url placeholder:(NSString *)placeholder{
    [self sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:[UIImage imageNamed:placeholder]];
}
-(void)crazy_setImageWithURL:(NSString *)url placeholder:(NSString *)placeholder optionInfo:(SDWebImageOptions)options{
    [self sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:[UIImage imageNamed:placeholder] options:options];
}
+(void)crazy_ImageClearCache{
    [[SDImageCache sharedImageCache]clearDisk];
    [[SDImageCache sharedImageCache]cleanDisk];
    [[SDImageCache sharedImageCache]clearMemory];
}

@end

@implementation UITableView(crazyTableView)

-(void)crazy_clearExtraCellLine{
    UIView * view = [[UIView alloc]init];
    view.backgroundColor = [UIColor clearColor];
    
    self.tableFooterView = view;
    self.tableHeaderView = view;
}

-(void)crazy_gourpExtraTopSpace{
    self.tableHeaderView = [[UIView alloc] initWithFrame:CGRectMake(0.0f, 0.0f, self.bounds.size.width, 0.01f)];
}
@end

@implementation UITableViewCell(crazyTableViewCell)
-(void)crazy_addSegmentLine:(UIColor *)color{
    UILabel *lineLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, self.frame.size.height-1, self.frame.size.width, 1)];
    lineLabel.backgroundColor = color;
    [self addSubview:lineLabel];
}
@end

@implementation UIWebView(crazyWebView)

-(float)crazy_webViewHeight{
   
   return [[self stringByEvaluatingJavaScriptFromString:@"document.body.offsetHeight;"] floatValue];
}

@end

//UILabel
@implementation UILabel(crazyLabel)

-(CGSize)crazy_text:(NSString *)text Auto:(direction)direction{
    self.numberOfLines =0;
    self.lineBreakMode = NSLineBreakByWordWrapping;
    self.text = text;
    
    CGRect rect = self.frame;
    CGSize size;
    if (direction == horizontal){
         size = [self sizeThatFits:CGSizeMake(MAXFLOAT, self.frame.size.height)];
        self.frame =CGRectMake(rect.origin.x, rect.origin.y, size.width, rect.size.height);
        self.layout_width.constant = size.height;
        
    }else if (direction == vertical){
         size = [self sizeThatFits:CGSizeMake(self.frame.size.width, MAXFLOAT)];
        if (size.height > rect.size.height) {
            self.frame =CGRectMake(rect.origin.x, rect.origin.y, rect.size.width, size.height);
            self.layout_height.constant = size.height;
        }else{
            size = CGSizeMake(rect.size.width, rect.size.height) ;
        }
    }
    
    return size;
}
@end

@implementation UIColor(crazyColor)

+(UIColor *)crazy_R:(CGFloat)red G:(CGFloat)green B:(CGFloat)blue A:(CGFloat)alpha{
    
    return [UIColor colorWithRed:red/255.0 green:green/255.0 blue:blue/255.0 alpha:alpha ];
}
+(UIColor *)crazy_hex:(NSString *)hex A:(CGFloat)alpha{
    return  [UIColor colorWithHexString:hex withAlpha:alpha];
}
+(UIColor *)crazy_hex:(NSString *)hex{
    return  [UIColor colorWithHexString:hex];
}

@end

@implementation NSString(crazyStr)

-(float)crazy_returnTextWidth:(float)fontSize{
    
    if (staticLabel == nil) {
        staticLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
    }
    
    staticLabel.numberOfLines =0;
    staticLabel.lineBreakMode = NSLineBreakByWordWrapping;
    staticLabel.text = self;
    staticLabel.font = [UIFont systemFontOfSize:fontSize];
    
    CGSize size = [staticLabel sizeThatFits:CGSizeMake(MAXFLOAT,0 )];
    
    return size.width;
}
-(float)crazy_returnFontSize:(float)fontSize ViewWidth:(float)width{
    
    if (staticLabel == nil) {
        staticLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
    }
    
    staticLabel.numberOfLines =0;
    staticLabel.lineBreakMode = NSLineBreakByWordWrapping;
    staticLabel.text = self;
    staticLabel.font = [UIFont systemFontOfSize:fontSize];
    
    CGSize size = [staticLabel sizeThatFits:CGSizeMake(width, MAXFLOAT)];
    
    return size.height;
    
}

- (NSString *)crazy_subStringStart:(NSString *)start StringEnd:(NSString *)end{
    
    NSRange startRange = [self rangeOfString:start];
    NSRange endRange = [self rangeOfString:end];
    NSString *subStr = @"";
    if (startRange.length > 0 && endRange.length > 0) {
        subStr = [self substringWithRange:NSMakeRange(startRange.location + startRange.length, endRange.location - (startRange.location + startRange.length) )];
    }else if (startRange.length > 0 && endRange.length == 0){
        subStr = [self substringFromIndex:startRange.location + startRange.length];
    }else if (startRange.length == 0 && endRange.length > 0){
        subStr = [self substringToIndex:endRange.location];
    }
    return subStr;
}

@end


