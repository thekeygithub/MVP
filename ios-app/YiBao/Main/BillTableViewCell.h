//
//  BillTableViewCell.h
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BillTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIButton *selectButton;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *contentLabel;
@property (weak, nonatomic) IBOutlet UILabel *mongeyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *chooseIV;

@end
