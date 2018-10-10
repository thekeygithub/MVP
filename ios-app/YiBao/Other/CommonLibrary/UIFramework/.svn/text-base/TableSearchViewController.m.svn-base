//
//  TableSearchViewController.m
//  CommonLibrary
//
//  Created by AlexiChen on 16/2/18.
//  Copyright © 2016年 AlexiChen. All rights reserved.
//

#import "TableSearchViewController.h"

@implementation TableSearchResultViewController

- (void)addHeaderView
{
    
}

- (void)addFooterView
{
    
}

- (void)updateSearchResultsForSearchController:(UISearchController *)searchController
{
}

- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar
{
    [self.searchDisplayController setActive:YES animated:YES];
    [searchBar setShowsCancelButton:YES animated:YES];   //  动画显示取消按钮
    return YES;
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar
{
    [searchBar setShowsCancelButton:NO animated:NO];    // 取消按钮回收
    [searchBar resignFirstResponder];                                // 取消第一响应值,键盘回收,搜索结束
}

- (BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString
{
    DebugLog(@"searchString = %@",searchString);
    return YES;
}

@end


@interface TableSearchViewController () <UISearchControllerDelegate>

@end


@implementation TableSearchViewController

- (Class)searchResultControllerClass
{
    return [TableSearchResultViewController class];
}

- (void)addSearchController
{
    _searchResultViewController = [[[self searchResultControllerClass] alloc] init];

    CGFloat ios = [[UIDevice currentDevice].systemVersion floatValue];
    if (ios >= 8.0)
    {
        _searchController = [[UISearchController alloc] initWithSearchResultsController:_searchResultViewController];
        _searchController.delegate = self;
        _searchController.searchResultsUpdater = _searchResultViewController;
        
        // 必须要让searchBar自适应才会显示
        [_searchController.searchBar sizeToFit];
        _searchController.searchBar.delegate = _searchResultViewController;
        [_searchController.searchBar setAutocapitalizationType:UITextAutocapitalizationTypeNone];
        _searchController.searchBar.backgroundImage = [UIImage imageWithColor:kAppBakgroundColor];
        //把searchBar 作为 tableView的头视图
        self.tableView.tableHeaderView = _searchController.searchBar;
    }
    else
    {
        UISearchBar *searchBar = [[UISearchBar alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 44)];
        searchBar.backgroundImage = [UIImage imageWithColor:kAppBakgroundColor];
        [searchBar setAutocapitalizationType:UITextAutocapitalizationTypeNone];
        [searchBar sizeToFit];
        searchBar.delegate = _searchResultViewController;
        self.tableView.tableHeaderView = searchBar;
        
        _searchResultViewController.view.backgroundColor = [UIColor clearColor];

        _searchDisController = [[UISearchDisplayController alloc] initWithSearchBar:searchBar contentsController:self];
        
        _searchDisController.searchResultsDataSource = _searchResultViewController;
        _searchDisController.searchResultsDelegate = _searchResultViewController;
        _searchDisController.delegate = _searchResultViewController;
        
        UIView *footer = [[UIView alloc] initWithFrame:CGRectZero];
        _searchDisController.searchResultsTableView.tableFooterView = footer;
    }
    self.definesPresentationContext = YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIView *v = [[UIView alloc] initWithFrame:CGRectZero];
    [self.tableView setTableFooterView:v];
    
    self.clearsSelectionOnViewWillAppear = YES;
    self.tabBarController.tabBar.translucent = NO;
    self.navigationController.navigationBar.translucent = NO;
    self.navigationController.navigationBar.hidden  = NO;
    self.view.backgroundColor = kAppBakgroundColor;
    
    [self addSearchController];
}


- (void)willPresentSearchController:(UISearchController *)searchController
{
    [self unpinHeaderView];
    
    [searchController.searchResultsController layoutSubviewsFrame];
    
}

- (void)willDismissSearchController:(UISearchController *)searchController
{
    [searchController.searchResultsController layoutSubviewsFrame];
}


@end
