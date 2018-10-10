//
//  TableSearchViewController.h
//  CommonLibrary
//
//  Created by AlexiChen on 16/2/18.
//  Copyright © 2016年 AlexiChen. All rights reserved.
//

#import "TableRefreshViewController.h"

//
//@interface UISearchController (ReplaceCancelText)
//- (void)replaceCancelText;
//@end


@interface TableSearchResultViewController : TableRefreshViewController<UISearchResultsUpdating, UISearchBarDelegate, UISearchDisplayDelegate>

@end


@interface TableSearchViewController : TableRefreshViewController
{
@protected
    UISearchController          *_searchController;
    
    UISearchDisplayController   *_searchDisController;
    
@protected
    // 搜索结果界面
    UIViewController<UISearchResultsUpdating, UISearchBarDelegate, UISearchDisplayDelegate, UITableViewDelegate, UITableViewDataSource> *_searchResultViewController;

}

- (Class)searchResultControllerClass;


- (void)addSearchController;




@end
