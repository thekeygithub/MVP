//
//  BrushFacePayViewController.h
//  YiBao
//
//  Created by wangchang on 2018/6/7.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

#define ScreenWidth [UIScreen mainScreen].bounds.size.width
#define ScreenHeight [UIScreen mainScreen].bounds.size.height

@protocol FaceDetectorDelegate <NSObject>

-(void)sendFaceImage:(UIImage *)faceImage; //上传图片成功
-(void)sendFaceImageError; //上传图片失败

@end
@interface BrushFacePayViewController : UIViewController
@property (assign,nonatomic) id<FaceDetectorDelegate> faceDelegate;
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (strong, nonatomic) UIImage *uploadImage;
//-(void)pushToFaceStreamDetectorVC;

@end
