//
//  easyflyFunction.m
//  easyflyDemo
//
//  Created by dukai on 15/6/2.
//  Copyright (c) 2015年 杜凯. All rights reserved.
//

#import "easyflyFunction.h"
#import<CommonCrypto/CommonDigest.h>
//#import "easyflyStart.h"
static easyflyFunction *function = nil;

@implementation easyflyFunction
{
    UIView *fieldView;
}
#define SCREEN_WIDTH [UIScreen mainScreen].bounds.size.width
#define HEIGHT(f) f * (SCREEN_WIDTH/320.0) //屏幕比例 算出实际UI大小

+(id)shareEasyflyClass
{
    if (function ==nil) {
        
        function = [[self alloc]init];
    }
    
    return function;
}

+ (void)setExtraCellLineHidden:(UITableView *)tableView
{
    UIView *view =[ [UIView alloc]init];
    view.backgroundColor = [UIColor clearColor];
    [tableView setTableFooterView:view];
    //[tableView setTableHeaderView:view];
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

//跟随键盘
-(void)textfeildFollowKeyBoard:(UIView *)view
{
    fieldView = view ;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    
}

- (void)keyboardWillShow:(NSNotification *)notification{
    
    NSDictionary *userInfo = [notification userInfo];
    
    NSValue *aValue = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
    
    CGRect keyboardRect = [aValue CGRectValue];
    
    NSValue *animationDurationValue = [userInfo objectForKey:UIKeyboardAnimationDurationUserInfoKey];
    
    NSTimeInterval animationDuration;
    
    [animationDurationValue getValue:&animationDuration];
    
    [UIView animateWithDuration:animationDuration animations:^{
        
        //此处的viewFooter即是你的输入框View
        
        //20为状态栏的高度
        
        fieldView.frame = CGRectMake(fieldView.frame.origin.x, keyboardRect.origin.y - 20 - fieldView.frame.size.height, fieldView.frame.size.width, fieldView.frame.size.height);
        
    } completion:^(BOOL finished){
        
        
        
    }];
    
}

- (void)keyboardWillHide:(NSNotification *)notification{
    //    NSDictionary* userInfo = [notification userInfo];
    //    NSValue* aValue = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
    //
    //    CGRect keyboardRect = [aValue CGRectValue];
    
    
    [UIView animateWithDuration:0 animations:^{
        
        fieldView.frame = CGRectMake(fieldView.frame.origin.x,fieldView.frame.origin.y, fieldView.frame.size.width, fieldView.frame.size.height);
        
    } completion:^(BOOL finished){
        
        
    }];
    
}
-(void)alertsheetUploadHeadImage:(UIViewController *)controller block:(ImageFinishBlock)ImageBlock{
    
     _sheetImgblock = ImageBlock;
     _VC = controller;
    
    UIActionSheet *sheet =  [[UIActionSheet alloc]initWithTitle:@"选择图片方式" delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"从照相机选取" otherButtonTitles:@"从相册中选取", nil];
    [sheet showInView:controller.view];

}
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex ==0){
        [self readFromcamera:_VC block:^(UIImage *image) {
            if (_sheetImgblock) {
                _sheetImgblock(image);
            }
        }];
    }else if (buttonIndex ==1){
        [self readFromAlum:_VC block:^(UIImage *image) {
            if (_sheetImgblock) {
                _sheetImgblock(image);
            }
        }];
    }
}

-(void)readFromAlum:(id)viewController block:(ImageFinishBlock)ImageBlock
{
    
    if ((YES == [UIImagePickerController isSourceTypeAvailable:
                 UIImagePickerControllerSourceTypeSavedPhotosAlbum])
        )
    {
        
        _imgblock = ImageBlock;
        _VC = viewController;
        
        UIImagePickerController *cameraUI = [[UIImagePickerController alloc] init];
        cameraUI.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
        
        //        cameraUI.mediaTypes =
        //        [UIImagePickerController availableMediaTypesForSourceType:
        //         UIImagePickerControllerSourceTypePhotoLibrary];
        [cameraUI setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
        cameraUI.allowsEditing = YES;
        
        cameraUI.delegate = self;
        
        [viewController presentViewController:cameraUI animated:YES completion:nil];
        
    }
    
}
-(void)readFromcamera:(id)viewController block:(ImageFinishBlock)ImageBlock
{
    
    if ((YES == [UIImagePickerController isSourceTypeAvailable:
                 UIImagePickerControllerSourceTypeCamera])
        )
    {
        
        _imgblock = ImageBlock;
        _VC = viewController;
        
        UIImagePickerController *cameraUI = [[UIImagePickerController alloc] init];
        cameraUI.sourceType = UIImagePickerControllerSourceTypeCamera;
        
        cameraUI.mediaTypes =
        [UIImagePickerController availableMediaTypesForSourceType:
         UIImagePickerControllerSourceTypeCamera];
        
        cameraUI.allowsEditing = YES;
        
        cameraUI.delegate = self;
        
        [viewController presentViewController:cameraUI animated:YES completion:nil];
        
    }
    else{
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"打开照相机失败了" message:@"请检查照相机是否已损坏" delegate:nil cancelButtonTitle:@"取消" otherButtonTitles: nil];
        [alert show];
        
    }
    
}
#pragma mark UIImagePickerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    NSString *mediaType = [info objectForKey:UIImagePickerControllerMediaType];
    
    UIImage *originalImage, *editedImage;
    
    if ([mediaType isEqualToString:@"public.image"]){
        
        editedImage = (UIImage *) [info objectForKey:
                                   UIImagePickerControllerEditedImage];
        originalImage = (UIImage *) [info objectForKey:
                                     UIImagePickerControllerOriginalImage];
        
        
        //  NSData *imageData= UIImageJPEGRepresentation(editedImage, 100);
        //    NSData*  imageData = UIImagePNGRepresentation(editedImage);
        
        // photoImageView.image = [UIImage imageWithData:imageData];
        
    }
    
    if (_imgblock) {
        _imgblock(editedImage);
    }
    
    [_VC dismissViewControllerAnimated: YES completion:nil];
    
}

+(NSString *)MD5:(NSString *)string{
    
    const char *cStr = [string UTF8String];
    
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    
    CC_MD5( cStr, strlen(cStr), digest );
    
    NSMutableString *result = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH *2];
    
    for(int i =0; i < CC_MD5_DIGEST_LENGTH; i++)
        
        [result appendFormat:@"%02x", digest[i]];
    
    return result;
    
    
}
+(NSString *)timeToTimestamp:(NSDate *)data{
    //时间转时间戳
    if (data ==nil) {
         data = [NSDate date];//现在时间
    }
   
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[data timeIntervalSince1970]];
    
    return timeSp;
}
//+(NSString *)timestampToTime:(NSString *)timestamp{
//    //时间戳转时间
//    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
//    [formatter setDateStyle:NSDateFormatterMediumStyle];
//    [formatter setTimeStyle:NSDateFormatterShortStyle];
//    [formatter setDateFormat:@"yyyyMMddHHMMss"];
//    NSDate *date = [formatter dateFromString:timestamp];
//    NSString *time = [formatter stringFromDate:date];
//    return time;
//}
+(NSString *)timestampToTime:(NSString *)timestamp{
    //时间戳转时间
    NSTimeInterval time=[timestamp doubleValue];
    
    NSDate *detaildate=[NSDate dateWithTimeIntervalSince1970:time];
    
    //实例化一个NSDateFormatter对象
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    
    //设定时间格式,这里可以设置成自己需要的格式
    
    //    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    [dateFormatter setDateFormat:@"yyyy-MM-dd  HH:MM"];
    
    NSString *currentDateStr = [dateFormatter stringFromDate: detaildate];
    
    return currentDateStr;
}
+(int)getRandomNumber:(int)from to:(int)to
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


+(NSDictionary *)nullDic:(NSDictionary *)myDic
{
    NSArray *keyArr = [myDic allKeys];
    NSMutableDictionary *resDic = [[NSMutableDictionary alloc]init];
    for (int i = 0; i < keyArr.count; i ++)
    {
        id obj = [myDic objectForKey:keyArr[i]];
        
        obj = [self changeType:obj];
        
        [resDic setObject:obj forKey:keyArr[i]];
    }
    return resDic;
}

//将NSDictionary中的Null类型的项目转化成@""
+(NSArray *)nullArr:(NSArray *)myArr
{
    NSMutableArray *resArr = [[NSMutableArray alloc] init];
    for (int i = 0; i < myArr.count; i ++)
    {
        id obj = myArr[i];
        
        obj = [self changeType:obj];
        
        [resArr addObject:obj];
    }
    return resArr;
}

//将NSString类型的原路返回
+(NSString *)stringToString:(NSString *)string
{
    return string;
}

//将Null类型的项目转化成@""
+(NSString *)nullToString
{
    return @"";
}

//类型识别:将所有的NSNull类型转化成@""
+(id)changeType:(id)myObj
{
    if ([myObj isKindOfClass:[NSDictionary class]])
    {
        return [self nullDic:myObj];
    }
    else if([myObj isKindOfClass:[NSArray class]])
    {
        return [self nullArr:myObj];
    }
    else if([myObj isKindOfClass:[NSString class]])
    {
        return [self stringToString:myObj];
    }
    else if([myObj isKindOfClass:[NSNull class]])
    {
        return [self nullToString];
    }
    else
    {
        return myObj;
    }
}

+ (BOOL)validateEmail:(NSString *)email

{
    NSString *emailRegex =@"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:email];
    
}
@end
