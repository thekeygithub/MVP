//
//  VTFCTableViewCell.m
//  YiBao
//
//  Created by wangchang on 2018/6/7.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "VTFCTableViewCell.h"

@implementation VTFCTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [CrazyAutoLayout frameOfSuperView:self];
    self.backgroundColor = [[UIColor whiteColor]colorWithAlphaComponent:0.2f];
    self.payButton.layer.borderWidth = 1;
    self.payButton.layer.cornerRadius = 15;
    self.payButton.layer.borderColor = [UIColorFromRGB(0x2197B5) CGColor];
    
}



@end
