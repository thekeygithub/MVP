//
//  BillDetailViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "BillDetailViewController.h"
#import "BillDetailTableViewCell.h"

@interface BillDetailViewController ()
@property (strong, nonatomic) NSMutableArray *dataArray;
@end

@implementation BillDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    [self customUI];

}

#pragma mark - 返回上一页
- (IBAction)back:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - UI
- (void)customUI
{
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    [easyflyFunction setExtraCellLineHidden:_tableView];
    [self.tableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    if (IsiPhoneX) {
        _statusView.height = StatusBarHeight;
        _navigationView.centerY = _navigationView.centerY + StatusBarHeight - 20;
        _tableView.frame = CGRectMake(0, StatusBarHeight + _navigationView.height, SCREEN_WIDTH, SCREEN_HEIGHT - StatusBarHeight - _navigationView.height);
    }
    [self dataParsing];
}

#pragma mark - 数据解析
- (void)dataParsing
{
    _dataArray = [NSMutableArray array];
    for (NSDictionary *prescriptionDetails in _model.prescriptionDetails) {
        OrderDetailModel *detail = [OrderDetailModel objectWithKeyValues:prescriptionDetails];
        [_dataArray addObject:detail];
    }
    [_tableView reloadData];
}


#pragma mark -
#pragma mark UITableViewDelegate UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView;
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    } else {
        return _dataArray.count;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 0) {
        return HEIGHT(90);
    } else {
        return HEIGHT(110);
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"BillDetailTableViewCell" owner:self options:nil];
    BillDetailTableViewCell *cell;
    if (indexPath.section == 0) {
        cell = [array objectAtIndex:0];
        cell.kebieLabel.text = [NSString stringWithFormat:@"科别：%@",self.model.deptName];
        cell.yishiLabel.text = [NSString stringWithFormat:@"医师：%@",self.model.doctorName];
        cell.zhenduanLabel.text = [NSString stringWithFormat:@"初步诊断：%@",self.model.diagnosis];
        cell.dateLabel.text = [NSString stringWithFormat:@"厨房日期：%@",self.model.treatmentDate];
    } else {
        cell = [array objectAtIndex:1];
        OrderDetailModel *detailModel = _dataArray[indexPath.row];
        cell.firstLabel.text = [NSString stringWithFormat:@"药品名称：%@",detailModel.itemName];
        cell.secondLabel.text = [NSString stringWithFormat:@"规格：%@",detailModel.specification];
        cell.thirdLabel.text = [NSString stringWithFormat:@"价格：%@",detailModel.price];
        cell.fourthLabel.text = [NSString stringWithFormat:@"数量：%@支",detailModel.num];
        cell.fifthLabel.text = [NSString stringWithFormat:@"用法用量：%@",detailModel.usage];

    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
    
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] init];
    view.frame = CGRectMake(0, 0, HEIGHT(320), HEIGHT(38));
    view.backgroundColor = UIColorFromRGB(0x2e7892);
    UILabel *label = [[UILabel alloc] init];
    label.frame = CGRectMake(HEIGHT(9), 0, HEIGHT(200), HEIGHT(38));
    [view addSubview:label];
    label.textColor = [UIColor whiteColor];
    label.font = [UIFont systemFontOfSize:HEIGHT(17)];
    if (section == 0) {
        label.text = @"基本信息";
    } else {
        label.text = @"医药处方明细";
    }
    return view;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    
    return HEIGHT(38);
}



@end
