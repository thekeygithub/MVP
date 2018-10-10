//
//  VTFCViewController.m
//  YiBao
//
//  Created by wangchang on 2018/6/6.
//  Copyright © 2018年 Mac. All rights reserved.
//

#import "VTFCViewController.h"
#import "FaceRecognitionViewController.h"
#import "VTFCTableViewCell.h"
#import "UserMessageModel.h"
#import "Single.h"
#import "FaceValidationViewController.h"
//#import "CrazyExtensionHeader.h"
@interface VTFCViewController ()
@property (strong, nonatomic) NSMutableArray *dataArray;
@property (strong, nonatomic) UserMessageModel *userModel;
@end

@implementation VTFCViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [CrazyAutoLayout frameOfSuperView:self.view];
    [self customUI];
    [self setupRefresh];

}

#pragma mark - UI
- (void)customUI
{
    if (PhoneNum) {
        _phonenumLabel.text = PhoneNum;
    }
//    NSString *str = @"";
//    CGFloat height = [_agreeLabel crazy_text:@"" Auto:vertical].height;
    
    self.backImageView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    [easyflyFunction setExtraCellLineHidden:_tableView];
    [self.tableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    [self requestData];
    

}

#pragma mark - 切换账号
- (IBAction)changeaccount:(UIButton *)sender {
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"token"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"phonenum"];
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - 验证
- (IBAction)val:(id)sender {
    FaceRecognitionViewController *vc = [[FaceRecognitionViewController alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark -
#pragma mark UITableViewDelegate UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView;
{
    return _dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return HEIGHT(38);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"VTFCTableViewCell" owner:self options:nil];
    VTFCTableViewCell *cell = [array objectAtIndex:0];
    UserMessageModel *model = _dataArray[indexPath.row];
    cell.nameLabel.text = [NSString stringWithFormat:@"所属医院：%@",model.hospName];
    cell.payButton.tag = indexPath.row + 10;
    [cell.payButton addTarget:self action:@selector(pay:) forControlEvents:UIControlEventTouchUpInside];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
    
}

#pragma mark - 缴费
- (void)pay:(UIButton *)button
{
    UserMessageModel *model = _dataArray[button.tag - 10];
    self.userModel = model;
    if (IsiPhoneX) {
        self.scrollView.frame = CGRectMake(HEIGHT(11), HEIGHT(44), SCREEN_WIDTH - HEIGHT(22), SCREEN_HEIGHT - HEIGHT(64));

    } else {
        self.scrollView.frame = CGRectMake(HEIGHT(11), HEIGHT(20), SCREEN_WIDTH - HEIGHT(22), SCREEN_HEIGHT - HEIGHT(40));

    }
    self.scrollView.contentOffset = CGPointZero;
    self.scrollView.contentSize = CGSizeMake(SCREEN_WIDTH - HEIGHT(22), HEIGHT(600));
    self.scrollView.hidden = NO;
    [Single shareSingle].hospName = model.hospName;
    [Single shareSingle].hospId = model.hospId;
    [Single shareSingle].treatmentId = model.treatmentId;
    [Single shareSingle].idNumber = model.idNumber;
//    [CrazyAutoLayout frameOfSuperView:self.scrollView];
}

#pragma mark - 不同意关闭
- (IBAction)noAgree:(id)sender {
    self.scrollView.hidden = YES;
}

#pragma mark - 同意验证
- (IBAction)agree:(id)sender {
    self.scrollView.hidden = YES;
    [Single shareSingle].model = self.userModel;
    FaceValidationViewController *vc = [[FaceValidationViewController alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
//    FaceRecognitionViewController *vc = [[FaceRecognitionViewController alloc] init];
//    [self.navigationController pushViewController:vc animated:YES];
    
}


#pragma mark - 数据请求
- (void)requestData
{
    _dataArray = [NSMutableArray array];
    NSDictionary *parameters = @{@"mobile":PhoneNum};
    [CrazyNetWork CrazyTokenHeadRequest_Post:[NSString stringWithFormat:@"%@getTreatmentInfo",defaultUrl] HUD:YES parameters:parameters success:^(NSDictionary *dic, NSString *url, NSString *Json) {
        BaseModel *baseModel = [BaseModel objectWithKeyValues:dic];
        if ([baseModel.statusCode integerValue] == 200) {
            for (NSDictionary *returnObj in baseModel.returnObj) {
                UserMessageModel *model = [UserMessageModel objectWithKeyValues:returnObj];
                [self->_dataArray addObject:model];
            }
        } else {
            
        }
        [self.tableView.mj_header endRefreshing];
        [self.tableView reloadData];

//        [JKToast showWithText:dic[@"message"]];
    } fail:^(NSError *error, NSString *url) {
        
    }];
}

#pragma mark - 上拉加载
- (void)setupRefresh{
    
    self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        //刷新表格
        [self headerRereshing];
    }];
//    self.tableView.mj_header = [MJRefreshNormalHeader footerWithRefreshingBlock:^{
//        //刷新表格
//        [self headerRereshing];
//    }];
    
    
}
- (void)headerRereshing
{
    [self requestData];
    
}

@end
