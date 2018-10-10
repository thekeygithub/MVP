//
//  ShakedViewController.m
//  ALiDemo
//
//  Created by sunjiachen on 15/11/17.
//  Copyright © 2015年 sunjiachen. All rights reserved.
//

#import "ShakedViewController.h"
#import "AppDelegate.h"
#import <MyBlocksKit/NSArray+BlocksKit.h>

#define kScreenWidth    [UIScreen mainScreen].bounds.size.width
#define kScreenHeight   [UIScreen mainScreen].bounds.size.height

@interface ShakedViewController () {
    AppDelegate *_viewCtrl;
    NSMutableArray *selectedArr;
}
@property (weak, nonatomic) IBOutlet UIButton *btnKeepStill;
@property (weak, nonatomic) IBOutlet UIButton *btnShakeHead;
@property (weak, nonatomic) IBOutlet UIButton *btnNodHead;
@property (weak, nonatomic) IBOutlet UIButton *btnOpenMouth;
@property (weak, nonatomic) IBOutlet UIButton *btnBlinkEyes;
@property (weak, nonatomic) IBOutlet UIButton *btnIsRandom;

@end

@implementation ShakedViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _viewCtrl = (AppDelegate *)[UIApplication sharedApplication].delegate;
    
    self.view.backgroundColor = [UIColor whiteColor];
    UIButton *beginBtn = [UIButton buttonWithType:UIButtonTypeSystem];
    beginBtn.frame = CGRectMake(kScreenWidth/4, kScreenHeight-80, kScreenWidth/2, 40);
    [beginBtn setTitle:@"返回" forState:UIControlStateNormal];
    beginBtn.backgroundColor = [UIColor grayColor];
    [beginBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [beginBtn addTarget:self action:@selector(buttonClick1:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:beginBtn];
    
    selectedArr = [NSMutableArray array];
}

- (IBAction)buttonClick:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [selectedArr addObject:@(sender.tag)];
    } else {
        if ([selectedArr indexOfObject:@(sender.tag)] != NSNotFound) {
            [selectedArr removeObject:@(sender.tag)];
        }
    }
}

- (void)buttonClick1:(UIButton *)button {
    NSMutableArray *mutabArr = [NSMutableArray array];
    [selectedArr bk_each:^(id  _Nonnull obj) {
        NSInteger objTag = [obj integerValue];
        if (objTag == 2001) {
            [mutabArr addObject:@(0)];
        } else if (objTag == 2002) {
            [mutabArr addObject:@(1)];
        } else if (objTag == 2003) {
            [mutabArr addObject:@(2)];
        } else if (objTag == 2004) {
            [mutabArr addObject:@(3)];
        } else if (objTag == 2005) {
            [mutabArr addObject:@(4)];
        }
    }];
    if ([selectedArr indexOfObject:@(2006)] != NSNotFound) {
        [mutabArr addObject:@(1)];
    } else {
        [mutabArr addObject:@(0)];
    }
    
    _viewCtrl.liveDetectTypes = mutabArr;
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
