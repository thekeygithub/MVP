//
//  BillDetailTableViewCell.m
//  YiBao
//
//  Created by wangchang on 2018/6/8.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "BillDetailTableViewCell.h"

@implementation BillDetailTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [CrazyAutoLayout frameOfSuperView:self];
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
