//
//  BillViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "BillViewController.h"
#import "BillTableViewCell.h"
#import "BillDetailViewController.h"
#import "OrderViewController.h"
#import "OrderMessageModel.h"
#import "OrderDetailModel.h"
@interface BillViewController ()
{
    NSString *amount;
    BOOL allFlag;//全选状态   1全选  0不全选

}
@property (strong, nonatomic) NSMutableArray *dataArray;
@property (strong, nonatomic) NSMutableArray *detailDataArray;
@property (strong, nonatomic) NSMutableArray *selectArray;;

@end

@implementation BillViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    allFlag = 1;
    [self customUI];
}

#pragma mark - UI
- (void)customUI
{
   self.topView.backgroundColor = [[UIColor whiteColor] colorWithAlphaComponent:0.2f];
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    self.automaticallyAdjustsScrollViewInsets = NO;

    [_tableView setSeparatorColor:UIColorFromRGB(0xffffff)];
    [easyflyFunction setExtraCellLineHidden:_tableView];
//    [self.secondTypeTableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];

    if (IsiPhoneX) {
        _statusView.height = StatusBarHeight;
        _navigationView.centerY = _navigationView.centerY + StatusBarHeight - 20;
        _topView.centerY = _topView.centerY + StatusBarHeight - 20;
        _tableView.frame = CGRectMake(0, StatusBarHeight + _navigationView.height + HEIGHT(27), SCREEN_WIDTH, SCREEN_HEIGHT - StatusBarHeight - _navigationView.height - HEIGHT(27) -  _bottomView.height);
        _bottomView.frame = CGRectMake(0, SCREEN_HEIGHT - _bottomView.height, SCREEN_WIDTH, _bottomView.height);
    }
    _selectArray = [NSMutableArray array];
    _dataArray = [NSMutableArray array];
    [self requestData];
    
}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark UITableViewDelegate UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView;
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return HEIGHT(71);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"BillTableViewCell" owner:self options:nil];
    BillTableViewCell *cell = [array objectAtIndex:0];
    NSLog(@"%ld",(long)indexPath.row);
    OrderMessageModel *model = _dataArray[indexPath.row];
    
    if ([_selectArray[indexPath.row] isEqualToString:@"1"]) {
        cell.chooseIV.image = [UIImage imageNamed:@"1"];
    }
    if ([_selectArray[indexPath.row] isEqualToString:@"0"]) {
        cell.chooseIV.image = [UIImage imageNamed:@"2"];
    }
    
    cell.nameLabel.text = model.prescriptionName;
    cell.contentLabel.text = [NSString stringWithFormat:@"%@|%@",model.deptName,model.doctorName];
    cell.mongeyLabel.text = [NSString stringWithFormat:@"￥%.2f",[model.payActully floatValue] + [model.reimbursement floatValue]];
    cell.selectButton.tag = indexPath.row + 100;
    [cell.selectButton addTarget:self action:@selector(select:) forControlEvents:UIControlEventTouchUpInside];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
    
}

#pragma mark - 是否选择
- (void)select:(UIButton *)button
{
    if ([_selectArray[button.tag - 100] isEqualToString:@"1"]) {
        [_selectArray replaceObjectAtIndex:button.tag - 100 withObject:@"0"];
//        [button setImage:[UIImage imageNamed:@"2"] forState:UIControlStateNormal];
        allFlag = 0;

    } else {
        [_selectArray replaceObjectAtIndex:button.tag - 100 withObject:@"1"];
//        [button setImage:[UIImage imageNamed:@"1"] forState:UIControlStateNormal];
        
        for (NSInteger idx = 0; idx < _selectArray.count; idx++) {
            if ([_selectArray[idx] isEqualToString:@"0"]) {
                allFlag = 0;
                break;
            } else {
                allFlag = 1;
            }
        }
    }
    
    if (allFlag == 0) {
        _waitPayIV.image = [UIImage imageNamed:@"2"];
        _chooseallIV.image = [UIImage imageNamed:@"2"];
    } else {
        _waitPayIV.image = [UIImage imageNamed:@"1"];
        _chooseallIV.image = [UIImage imageNamed:@"1"];
    }
    float k = 0;
    for (NSInteger idx
         = 0; idx < _dataArray.count; idx++) {
        if ([_selectArray[idx] isEqualToString:@"1"]) {
            OrderMessageModel *model = _dataArray[idx];
            k = k + [model.payActully floatValue] + [model.reimbursement floatValue];
        }
    
    }
    self->amount = [NSString stringWithFormat:@"￥%.2lf",k];
    self->_moneyLAbel.text = self->amount;
    
    [_tableView reloadData];

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    _detailDataArray = [NSMutableArray array];
    OrderMessageModel *model = _dataArray[indexPath.row];
//    for (NSDictionary *prescriptionDetails in model.prescriptionDetails) {
//        OrderDetailModel *detailModel = [OrderDetailModel objectWithKeyValues:prescriptionDetails];
//        [_detailDataArray addObject:detailModel];
//    }
    BillDetailViewController *bill = [[BillDetailViewController alloc] init];
    bill.model = model;
    [self.navigationController pushViewController:bill animated:YES];
    
}

#pragma mark - 设置tableview分割线长度
-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat topHeight = HEIGHT(71) - 1.5;
    if ([cell respondsToSelector:@selector(setSeparatorInset:)]) {
        [cell setSeparatorInset:UIEdgeInsetsMake(topHeight, 0, 0, 0)];
    }
    
    if ([cell respondsToSelector:@selector(setLayoutMargins:)]) {
        [cell setLayoutMargins:UIEdgeInsetsMake(topHeight, 0, 0, 0)];
    }
    
}

#pragma mark - 结算
- (IBAction)settlement:(id)sender {
    NSMutableArray *payArray = [NSMutableArray array];
    for (NSInteger idx = 0; idx < _selectArray.count; idx++) {
        if ([_selectArray[idx] isEqualToString:@"1"]) {
            OrderMessageModel *order = _dataArray[idx];
            [payArray addObject:order];
        }
    }
    
    if (payArray.count > 0) {
        OrderViewController *vc = [[OrderViewController alloc] init];
        vc.array = payArray;
        [self.navigationController pushViewController:vc animated:YES];
    } else {
        [JKToast showWithText:@"请选择支付项"];
    }
    
    
}

#pragma mark - 验证数据接口请求
- (void)requestData
{
    float k = 0.0;
    for (NSDictionary *prescriptions in self.dic) {
        OrderMessageModel *model = [OrderMessageModel objectWithKeyValues:prescriptions];
        self->_waitPayLabel.text = [NSString stringWithFormat:@"待支付：%@",model.treatmentDate];
        k = k + [model.payActully floatValue] + [model.reimbursement floatValue];
        
        [self->_dataArray addObject:model];
        [self->_selectArray addObject:@"1"];
    }
    self->amount = [NSString stringWithFormat:@"￥%.2lf",k];
    self->_moneyLAbel.text = self->amount;
    [self->_tableView reloadData];

    
}



#pragma mark - 点击全选
- (IBAction)chooseAll:(id)sender {
    
    if (allFlag == 1) {
        for (NSInteger idx = 0; idx < _selectArray.count; idx++) {
            [_selectArray replaceObjectAtIndex:idx withObject:@"0"];
        }
        allFlag = 0;
        _chooseallIV.image = [UIImage imageNamed:@"2"];
        _waitPayIV.image = [UIImage imageNamed:@"2"];
    } else {
        for (NSInteger idx = 0; idx < _selectArray.count; idx++) {
            [_selectArray replaceObjectAtIndex:idx withObject:@"1"];
        }
        allFlag = 1;
        _chooseallIV.image = [UIImage imageNamed:@"1"];
        _waitPayIV.image = [UIImage imageNamed:@"1"];
        
    }
    
    float k = 0;
    for (NSInteger idx
         = 0; idx < _dataArray.count; idx++) {
        if ([_selectArray[idx] isEqualToString:@"1"]) {
            OrderMessageModel *model = _dataArray[idx];
            k = k + [model.payActully floatValue] + [model.reimbursement floatValue];
        }
        
    }
    self->amount = [NSString stringWithFormat:@"￥%.2lf",k];
    self->_moneyLAbel.text = self->amount;
    
    [_tableView reloadData];

}

@end
