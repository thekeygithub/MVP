//
//  easyflyFunction.m
//  easyflyDemo
//
//  Created by dukai on 15/6/2.
//  Copyright (c) 2015年 杜凯. All rights reserved.
//

#import "CrazyFunction.h"
#import<CommonCrypto/CommonDigest.h>
static CrazyFunction *function = nil;

@implementation CrazyFunction
{
    UIView *fieldView;
}

+(NSString *) compareCurrentTime:(NSDate*) compareDate

{
    NSTimeInterval  timeInterval = [compareDate timeIntervalSinceNow];
    timeInterval = -timeInterval;
    long temp = 0;
    NSString *result;
    if (timeInterval < 60) {
        result = [NSString stringWithFormat:@"刚刚"];
    }
    else if((temp = timeInterval/60) <60){
        result = [NSString stringWithFormat:@"%ld分前",temp];
    }
    
    else if((temp = temp/60) <24){
        result = [NSString stringWithFormat:@"%ld小时前",temp];
    }
    
    else if((temp = temp/24) <30){
        result = [NSString stringWithFormat:@"%ld天前",temp];
    }
    
    else if((temp = temp/30) <12){
        result = [NSString stringWithFormat:@"%ld月前",temp];
    }
    else{
        temp = temp/12;
        result = [NSString stringWithFormat:@"%ld年前",temp];
    }
    
    return  result;
}

+(void)textfeildSpace:(UITextField *)textFeild space:(float )space
{
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, space, 30)];
    textFeild.leftView = imageView;
    textFeild.leftViewMode = UITextFieldViewModeAlways;
}

+(NSString *)crazy_md5:(NSString *)string count:(int)count{
    
    NSString *md5 = string;
    
    for (int i = 0; i < count ; i++) {
       md5 = [CrazyFunction crazy_md5:md5];
    }
    
    return md5;
}
+(NSString *)crazy_MD5:(NSString *)string count:(int)count{
    
    NSString *md5 = string;
    
    for (int i = 0; i < count ; i++) {
        md5 = [CrazyFunction crazy_MD5:md5];
    }
    
    return md5;
}
+(NSString *)crazy_md5:(NSString *)string{
    
    const char *cStr = [string UTF8String];
    
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    
    CC_MD5( cStr, strlen(cStr), digest );
    
    NSMutableString *result = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH *2];
    
    for(int i =0; i < CC_MD5_DIGEST_LENGTH; i++)
        
        [result appendFormat:@"%02x", digest[i]];
    
    return result;
}
+(NSString *)crazy_MD5:(NSString *)string{
    
    return [CrazyFunction crazy_md5:string].uppercaseString;
}

+(NSString *)crazy_timeToTimestamp:(NSDate *)data{
    //时间转时间戳
    if (data ==nil) {
         data = [NSDate date];//现在时间
    }
   
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[data timeIntervalSince1970]];
    
    return timeSp;
}
+(NSString *)crazy_timestampToTime:(NSString *)timestamp{
    //时间戳转时间
    NSTimeInterval time=[timestamp doubleValue];
    
    NSDate *detaildate=[NSDate dateWithTimeIntervalSince1970:time];
    
    //实例化一个NSDateFormatter对象
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    
    //设定时间格式,这里可以设置成自己需要的格式
//    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];

//    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    [dateFormatter setDateFormat:@"yyyy.MM.dd"];
    
    NSString *currentDateStr = [dateFormatter stringFromDate: detaildate];
    
    return currentDateStr;
}
+(int)crazy_RandomNumber:(int)from to:(int)to
{
    return (int)(from + (arc4random() % (to - from + 1) ));
    
}
+(float)returnTextWidth:(float)fontSize title:(NSString *)title{
    NSDictionary *attribute1 = @{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]};
    
    CGSize size1 = [title boundingRectWithSize:CGSizeMake(MAXFLOAT, 0) options:NSStringDrawingTruncatesLastVisibleLine attributes:attribute1 context:nil].size;
    return size1.width;
}
+(float)returnFontSize:(float)fontSize title:(NSString *)title ViewWidth:(float)width{
   
    UIFont *font = [UIFont fontWithName:@"Arial" size:HEIGHT(fontSize)];
    CGSize size = CGSizeMake(width,2000);
    CGSize labelsize = [ title sizeWithFont:font constrainedToSize:size lineBreakMode:NSLineBreakByWordWrapping];
    
    CGSize recontentSize = [ title sizeWithFont:font constrainedToSize:size lineBreakMode:NSLineBreakByWordWrapping];

    return recontentSize.height;
}

//字典或者数组 转换成json串
+(NSString *)crazy_ObjectToJsonString:(id)object{
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:object
                                                       options:nil
                                                         error:nil];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData
                                                 encoding:NSUTF8StringEncoding];
    return jsonString;

}
//json串 转换成字典或者数组
+(id)crazy_JsonStringToObject:(NSString *)JsonString{
    JsonString = [JsonString stringByReplacingOccurrencesOfString:@"\0" withString:@""];
    NSData *jsonData = [JsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    id objc = [NSJSONSerialization JSONObjectWithData:jsonData
                                              options:NSJSONReadingMutableContainers
                                                error:&err];
    return objc;
}
+(NSString *)crazy_pinyin:(NSString *)hanzi{
    NSMutableString *ms;
    if ([hanzi length]) {
        ms = [[NSMutableString alloc] initWithString:hanzi];
        if (CFStringTransform((__bridge CFMutableStringRef)ms, 0, kCFStringTransformMandarinLatin, NO)) {
            
        }
        if (CFStringTransform((__bridge CFMutableStringRef)ms, 0, kCFStringTransformStripDiacritics, NO)) {
        }
    }
    return ms;
}

+(NSString *)crazy_unicodeUTF8:(NSString *)url{
    NSString *encodedValue = (NSString*)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(nil,(CFStringRef)url, nil,(CFStringRef)@"!*'();:@&=+$,/?%#[]", kCFStringEncodingUTF8));
    
    return encodedValue;
}

+ (UIImage*)getSubImage:(UIImage *)image mCGRect:(CGRect)mCGRect
             centerBool:(BOOL)centerBool{
    /*如若centerBool为Yes则是由中心点取mCGRect范围的图片*/
    float imgWidth = image.size.width;
    float imgHeight = image.size.height;
    float viewWidth = mCGRect.size.width;
    float viewHidth = mCGRect.size.height;
    CGRect rect;
    if(centerBool){
        rect = CGRectMake((imgWidth-viewWidth)/2,(imgHeight-viewHidth)/2,viewWidth,viewHidth);
    }else{
        if(viewHidth < viewWidth){
            if(imgWidth <= imgHeight){
                rect = CGRectMake(0, 0, imgWidth, imgWidth*imgHeight/viewWidth);
            }else{
                float width = viewWidth*imgHeight/viewHidth;
                float x = (imgWidth - width)/2;
                if(x > 0){
                    rect = CGRectMake(x, 0, width, imgHeight);
                }else{
                    rect = CGRectMake(0, 0, imgWidth, imgWidth*viewHidth/viewWidth);
                }
            }
        }else{
            if(imgWidth <= imgHeight){
                float height = viewHidth*imgWidth/viewWidth;
                if(height < imgHeight){
                    rect = CGRectMake(0,0, imgWidth, height);
                }else{
                    rect = CGRectMake(0,0, viewWidth*imgHeight/viewHidth,imgHeight);
                }
            }else{
                float width = viewWidth * imgHeight / viewHidth;
                if(width < imgWidth){
                    float x = (imgWidth - width)/2;
                    rect = CGRectMake(x,0,width, imgHeight);
                }else{
                    rect = CGRectMake(0,0,imgWidth, imgHeight);
                }
            }
        }
    }
    
    CGImageRef subImageRef = CGImageCreateWithImageInRect(image.CGImage, rect);
    CGRect smallBounds = CGRectMake(0,0,CGImageGetWidth(subImageRef),CGImageGetHeight(subImageRef));
    
    UIGraphicsBeginImageContext(smallBounds.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextDrawImage(context, smallBounds, subImageRef);
    UIImage *smallImage = [UIImage imageWithCGImage:subImageRef];
    UIGraphicsEndImageContext();
    
    return smallImage;
}

//手机验证
+ (BOOL)validatePhone:(NSString *)phoneNum
{
    /*
     手机号码
     移动：134[0-8],      135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188
     联通：130,131,132,145,155,156,176,185,186
     电信：133,1349,153,177,180,181,189
     */
    //验证手机号码是否正确
    NSString * MOBILE = @"^1(3[0-9]|4[57]|5[0-35-9]|7[6-8]|8[0-9])\\d{8}$";
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

static CrazyCountdownTimer *onceTimer = nil;

@implementation CrazyCountdownTimer
{
    NSTimer *timer;
}
+(void)crazy_CountdownTimer:(int)Second block:(FinishTimerBolck)block finish:(TimerFinishBolck)finishBlcok{
    onceTimer = [[CrazyCountdownTimer alloc]init];
    onceTimer.nowTime = Second;
    [onceTimer createTimer];
    onceTimer.TimerBolck = block;
    onceTimer.finishBolck = finishBlcok;
}
-(void)createTimer{
    
    timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(startTimer) userInfo:nil repeats:YES];
}
-(void)startTimer{
    
    _nowTime --;
    if (self.TimerBolck) {
        self.TimerBolck(_nowTime);
    }
    if (_nowTime ==0) {
        [timer invalidate];
        self.finishBolck();
    }
    
}


@end
