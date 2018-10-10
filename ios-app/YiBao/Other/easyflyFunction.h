//
//  easyflyFunction.h
//  easyflyDemo
//
//  Created by dukai on 15/6/2.
//  Copyright (c) 2015年 杜凯. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void (^ImageFinishBlock)(UIImage *image);
typedef void (^sheetFinishBlock)(UIImage *image);

@interface easyflyFunction : NSObject<UITextFieldDelegate,UIImagePickerControllerDelegate,UIActionSheetDelegate,UINavigationControllerDelegate>

@property(nonatomic,strong)ImageFinishBlock imgblock;
@property(nonatomic,strong)sheetFinishBlock sheetImgblock;
@property(nonatomic,strong)id VC;

+(id)shareEasyflyClass;

+ (void)setExtraCellLineHidden: (UITableView *)tableView; //没数据时清除分割线

+(void)textfeildSpace:(UITextField *)textFeild space:(float )space; //textfield 不顶头

+(NSString *)compareCurrentTime:(NSDate*) compareDate; //计算时间差

-(void)textfeildFollowKeyBoard:(UIView *)view; //textfield 跟随键盘

-(void)alertsheetUploadHeadImage:(UIViewController *)controller  block:(sheetFinishBlock)ImageBlock;
-(void)readFromAlum:(id)viewController block:(ImageFinishBlock)ImageBlock;
-(void)readFromcamera:(id)viewController block:(ImageFinishBlock)ImageBlock;

+(NSString *)MD5:(NSString *)string;//md5加密
+(NSString *)timeToTimestamp:(NSDate *)data;//时间转时间戳
+(NSString *)timestampToTime:(NSString *)timestamp;//时间戳转时间
+(int)getRandomNumber:(int)from to:(int)to; //随机数 间隔

+(float)returnTextWidth:(float)fontSize title:(NSString *)title;//一行字的总长
+(float)returnFontSize:(float)fontSize title:(NSString *)title ViewWidth:(float)width;//多行高度


+(id)changeType:(id)myObj; 
+(NSDictionary *)nullDic:(NSDictionary *)myDic;
+ (BOOL)validateEmail:(NSString *)email;//邮箱验证
@end
