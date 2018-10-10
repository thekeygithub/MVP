//
//  HTJCBeginView.m
//  HTJCFaceDetect
//
//  Created by jiao on 15/10/6.
//  Copyright (c) 2015年 HTJCLiveDetect. All rights reserved.
//

#import "HTJCBeginView.h"
#import <HTJCFaceLiveDetectSdk/THIDMCHTJCViewManger.h>

#define SCREENWIDTH        [UIScreen mainScreen].bounds.size.width
#define SCREENHEIGHT       [UIScreen mainScreen].bounds.size.height
#define CHANGEVIEW_HEIGHT  (SCREENHEIGHT-64)

#define LIVEDETECT_BGCOLOR [UIColor colorWithRed:(2/ 255.0) green:(105 / 255.0) blue:(134 / 255.0) alpha:1]
#define NAVIGATION_COLOR [UIColor colorWithRed:(0/ 255.0) green:(133 / 255.0) blue:(170 / 255.0) alpha:1]


@implementation HTJCBeginView{
    UIView *_changedView;
    UIView *_succeedView;
    UIView *_failedView;
    UIView *_beginView;
    UILabel *_titleLB;
}
- (instancetype)init
{
    self = [super initWithFrame:CGRectMake(0, 0, SCREENWIDTH, SCREENHEIGHT)];
    if (self) {
//        [self createNavigation];
        [self createView];
        
    }
    return self;
}
//导航栏
-(void)createNavigation{
    UIView * navigationView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, SCREENWIDTH, 64)];
    navigationView.backgroundColor = [UIColor blackColor];
    [self addSubview:navigationView];
    
    //标题
    _titleLB = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, SCREENWIDTH, 44)];
    _titleLB.text = @"身份检测";
//    _titleLB.adjustsFontSizeToFitWidth = YES;
    _titleLB.font = [UIFont boldSystemFontOfSize:22];
    _titleLB.textColor = [UIColor whiteColor];
    _titleLB.textAlignment = NSTextAlignmentCenter;
    [navigationView addSubview:_titleLB];
    
 }

-(void)createView{
    _changedView = [[UIView alloc]initWithFrame:CGRectMake(0, 64, SCREENWIDTH, SCREENHEIGHT-64)];
    _changedView.backgroundColor = LIVEDETECT_BGCOLOR;
    [self addSubview:_changedView];
    [self changeToBeginView];
}
-(void)changeToBeginView{
    [_changedView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self creatBeginView];
}
-(void)changeToSuccessView:(UIImage *)img{
    [_changedView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self createSucceedView:img];
}

-(void)changeToImageCheckFailView:(UIImage *)img errorInfo:(NSString *)error{
    
    [_changedView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    [self createImageCheckFailView:img errorInfo:error];
}


-(void)changeToFailView:(UIImage *)img failedInfo:(NSString*)failedStr{
    [_changedView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self createFailView:img failedInfo:failedStr];
}

//起始View
-(void)creatBeginView{
    _titleLB.text = @"身份检测";
    
    //背景图
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(SCREENWIDTH/12, CHANGEVIEW_HEIGHT/13, SCREENWIDTH/12*10, CHANGEVIEW_HEIGHT/13*9)];
    bgView.backgroundColor = [UIColor whiteColor];
    bgView.layer.cornerRadius = 8.0;
    [_changedView addSubview:bgView];
    
    //图像
    UIImageView * photoImg = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/10*2, CHANGEVIEW_HEIGHT/16*2, SCREENWIDTH/10*6, CHANGEVIEW_HEIGHT/16*8)];
    
    photoImg.layer.borderWidth = 0.7;
    photoImg.layer.borderColor = [UIColor blackColor].CGColor;
    photoImg.contentMode = UIViewContentModeScaleAspectFill;
    photoImg.image = [UIImage imageNamed:@"HTJCData.bundle/guideImage.png"];
    photoImg.layer.masksToBounds = YES;
    
    [_changedView addSubview:photoImg];
    
    UIImageView *promptView = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/16*2, CHANGEVIEW_HEIGHT/13*8, SCREENWIDTH/16*12, CHANGEVIEW_HEIGHT/13*2)];
    promptView.image = [UIImage imageNamed:@"HTJCData.bundle/pointImage.png"];
    [_changedView addSubview:promptView];

     //开始按钮
    UIButton *beginBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    beginBtn.frame = CGRectMake(SCREENWIDTH/12, CHANGEVIEW_HEIGHT/13*11, SCREENWIDTH/12*10, SCREENWIDTH/8);
    [beginBtn setImage:[UIImage imageNamed:@"HTJCData.bundle/beginBtn-n.png"] forState:UIControlStateNormal];
    [beginBtn setImage:[UIImage imageNamed:@"HTJCData.bundle/beginBtn-h.png"] forState:UIControlStateHighlighted];
    beginBtn.tag = 202;
    [beginBtn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [_changedView addSubview:beginBtn];


    UILabel * label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 300, 20)];
    label.center = CGPointMake(SCREENWIDTH/2, SCREENHEIGHT-64-20);
    label.text = @"应用版本号:V1.2.6  算法版本号:S1.0.1.5740";
//    label.backgroundColor = [UIColor whiteColor];
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont systemFontOfSize:12.0f];
    label.textColor = [UIColor colorWithRed:154.0/255.0 green:195.0/255.0 blue:207.0/255.0 alpha:1];
    [_changedView addSubview:label];
}
//成功view
-(void)createSucceedView:(UIImage *)img{
    _titleLB.text = @"检测结果";
    UIImageView * photoImgView = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/4, CHANGEVIEW_HEIGHT/6, SCREENWIDTH/2, SCREENWIDTH/2/3*4)];
    photoImgView.image = img;
    photoImgView.layer.cornerRadius = 10.0f;
    photoImgView.layer.masksToBounds = YES;
    [_changedView addSubview:photoImgView];
    
    UIImageView *viewBorder = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/4-1, CHANGEVIEW_HEIGHT/6-1, SCREENWIDTH/2+2, SCREENWIDTH/2/3*4+2)];
    viewBorder.image = [UIImage imageNamed:@"HTJCData.bundle/photoBorder.png"];
    [_changedView addSubview:viewBorder];
    
    UILabel * okLB = [[UILabel alloc]initWithFrame:CGRectMake(SCREENWIDTH/4, CGRectGetMaxY(viewBorder.frame)+15, SCREENWIDTH/2,50)];
    okLB.text = @"检测成功";
    okLB.textColor = [UIColor whiteColor];
    okLB.font = [UIFont systemFontOfSize:21];
    okLB.textAlignment = NSTextAlignmentCenter;
    [_changedView addSubview:okLB];

    
    [self creatBtn];

}
//成功view 但比对失败
-(void)createImageCheckFailView:(UIImage *)img errorInfo:(NSString *)error{
    _titleLB.text = @"对比失败";
    UIImageView * photoImgView = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/4, CHANGEVIEW_HEIGHT/6, SCREENWIDTH/2, SCREENWIDTH/2/3*4)];
    photoImgView.image = img;
    photoImgView.layer.cornerRadius = 8.0f;
    photoImgView.layer.masksToBounds = YES;
    [_changedView addSubview:photoImgView];
    
    UIImageView *viewBorder = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/4, CHANGEVIEW_HEIGHT/6, SCREENWIDTH/2, SCREENWIDTH/2/3*4)];
    viewBorder.image = [UIImage imageNamed:@"HTJCData.bundle/photoBorder.png"];
    [_changedView addSubview:viewBorder];
    
    UILabel * okLB = [[UILabel alloc]initWithFrame:CGRectMake(SCREENWIDTH/4, CHANGEVIEW_HEIGHT/6+SCREENWIDTH/2/3*4, SCREENWIDTH/2,50)];
    okLB.text = [NSString stringWithFormat:@"%@",error];
    okLB.textColor = [UIColor whiteColor];
    okLB.font = [UIFont systemFontOfSize:21];
    okLB.textAlignment = NSTextAlignmentCenter;
    okLB.adjustsFontSizeToFitWidth=YES;
    [_changedView addSubview:okLB];
    
    
    [self creatBtn];
    
}


//失败view
-(void)createFailView:(UIImage *)img failedInfo:(NSString*)failedStr{
    _titleLB.text = @"检测结果";
    
    UIImageView * photoImgView = [[UIImageView alloc]init];

    if (!img) {
        photoImgView.frame = CGRectMake(SCREENWIDTH/4, CHANGEVIEW_HEIGHT/8, SCREENWIDTH/2, CHANGEVIEW_HEIGHT/13*3);
         photoImgView.image = [UIImage imageNamed:@"HTJCData.bundle/failedImage.png"];
        [_changedView addSubview:photoImgView];
    }else{
        photoImgView.frame = CGRectMake(SCREENWIDTH/4, CHANGEVIEW_HEIGHT/12, SCREENWIDTH/2, SCREENWIDTH/2/3*4);
        photoImgView.image = img;
        photoImgView.layer.cornerRadius = 10.0f;
        photoImgView.layer.masksToBounds = YES;
        [_changedView addSubview:photoImgView];
        
        UIImageView *viewBorder = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/4-1, CHANGEVIEW_HEIGHT/12-1, SCREENWIDTH/2+2, SCREENWIDTH/2/3*4+2)];
        viewBorder.image = [UIImage imageNamed:@"HTJCData.bundle/photoBorder.png"];
        [_changedView addSubview:viewBorder];
    }
    
    UILabel *failLB = [[UILabel alloc]initWithFrame:CGRectMake(SCREENWIDTH/4, CGRectGetMaxY(photoImgView.frame)+15, SCREENWIDTH/2, 50)];
    failLB.text = @"检测失败";
    failLB.adjustsFontSizeToFitWidth = YES;
    failLB.textColor = [UIColor whiteColor];
    failLB.textAlignment = NSTextAlignmentCenter;
    failLB.font = [UIFont systemFontOfSize:21];
    [_changedView addSubview:failLB];
    
    //
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(SCREENWIDTH/11, CGRectGetMaxY(failLB.frame), SCREENWIDTH/11*9, 40)];
    label.text = @"可能失败的原因:";
    label.adjustsFontSizeToFitWidth = YES;
    label.textColor = [UIColor whiteColor];
    label.font = [UIFont systemFontOfSize:19];

    [_changedView addSubview:label];

    CGSize size = [failedStr sizeWithFont:[UIFont systemFontOfSize:17.0f] constrainedToSize:CGSizeMake(SCREENWIDTH/9*7, 10000)];
    //取得高度
    CGFloat h = size.height;
    UILabel *reasonLabel = [[UILabel alloc]initWithFrame:CGRectMake(SCREENWIDTH/11, CGRectGetMaxY(label.frame), SCREENWIDTH/11*9, h)];
    reasonLabel.text = failedStr;
//    reasonLabel.adjustsFontSizeToFitWidth = YES;
    //根据显示的内容确定高度
    reasonLabel.font = [UIFont systemFontOfSize:17.0f];
    reasonLabel.textColor = [UIColor whiteColor];
    reasonLabel.numberOfLines = 0;
//    reasonLabel.textAlignment = NSTextAlignmentCenter;
    [_changedView addSubview:reasonLabel];

    UILabel * line = [[UILabel alloc]initWithFrame:CGRectMake(SCREENWIDTH/11, CGRectGetMaxY(reasonLabel.frame)+3, SCREENWIDTH/11*9, 1)];
    line.backgroundColor = [UIColor whiteColor];
    [_changedView addSubview:line];

    
    [self creatBtn];


}


-(void)creatBtn{
    //重新检测
    UIButton * againBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    againBtn.frame = CGRectMake(SCREENWIDTH/9, CHANGEVIEW_HEIGHT-SCREENWIDTH/8-25, SCREENWIDTH/9*3, SCREENWIDTH/8);
    [againBtn setImage:[UIImage imageNamed:@"HTJCData.bundle/againBtn-n.png"] forState:UIControlStateNormal];
    againBtn.tag = 203;
    [againBtn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [_changedView addSubview:againBtn];
    
    //退出
    UIButton * quitBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    quitBtn.frame = CGRectMake(SCREENWIDTH/9*5, CHANGEVIEW_HEIGHT-SCREENWIDTH/8-25, SCREENWIDTH/9*3, SCREENWIDTH/8);
    [quitBtn setImage:[UIImage imageNamed:@"HTJCData.bundle/quitBtn-n.png"] forState:UIControlStateNormal];
    quitBtn.tag = 204;
    [quitBtn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [_changedView addSubview:quitBtn];


}

//生成加载中View
-(UIView *)creatLoadingView{
    UIView *loadingView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, SCREENWIDTH, SCREENHEIGHT)];
    loadingView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.9];
    //
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, CHANGEVIEW_HEIGHT/12*6, SCREENWIDTH, 50)];
    label.text = @"前往\"全国公民身份证号码查询服务中心\"认证!";
    label.font = [UIFont boldSystemFontOfSize:15];
    label.textColor = [UIColor whiteColor];
    label.textAlignment = NSTextAlignmentCenter;
    [loadingView addSubview:label];
    //
    UIImageView * loadImgView = [[UIImageView alloc]initWithFrame:CGRectMake(SCREENWIDTH/3, CHANGEVIEW_HEIGHT/13*3, SCREENWIDTH/3, CHANGEVIEW_HEIGHT/13*3)];
    loadImgView.image = [UIImage imageNamed:@"HTJCData.bundle/iconImg.png"];
    [loadingView addSubview:loadImgView];
    
    return loadingView;
}

//按钮点击事件
-(void)btnAction:(UIButton *)btn{
    if (btn.tag == 201) {
        //左上角返回
        _backBlock();
    }else if (btn.tag == 202) {
        //开始检测
        _beginBlock();
    }else if (btn.tag == 203) {
        //重新检测
        _againBlock();
    }else if (btn.tag == 204) {
        //退出
        _quitBlock();
    }else if (btn.tag == 205) {
        //查看照片
        _wifishareBlock();
    }
    
}

@end
