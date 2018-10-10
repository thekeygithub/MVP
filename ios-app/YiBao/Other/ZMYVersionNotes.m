//
//  ZMYVersionNotes.m
//  MYVersion
//
//  Created by chs_net on 14/6/24.
//  Copyright (c) 2014年 Dean. All rights reserved.
//

#import "ZMYVersionNotes.h"
static NSString *const kZMYReleaseNotesViewVersionKey = @"ZMYReleaseNotesVersionKey";
@interface ZMYVersionNotes () <NSURLConnectionDelegate>

@property (strong, nonatomic) NSURL *requestURL;
@property (strong, nonatomic) NSURLConnection *urlConnection;
@property (readwrite, strong, nonatomic) NSError *error;
@property (readwrite, copy, nonatomic) NSString *releaseNotesText;
@property (readwrite, copy, nonatomic) NSString *version;
@property (readwrite, copy, nonatomic) NSDictionary *releaseDict;
@property (strong, nonatomic) NSMutableData *bufferData;
@property (strong, nonatomic) NSData *appMetadata;
@property (assign, nonatomic) BOOL isExecuting;
@property (assign, nonatomic) BOOL isConcurrent;
@property (assign, nonatomic) BOOL isFinished;

- (void)extractReleaseNotes;

@end
@implementation ZMYVersionNotes
+ (void)storeCurrentAppVersionString
{
    // Store current app version string in the user defaults
    NSString *currentAppVersion = [[NSBundle mainBundle] infoDictionary][@"CFBundleVersion"];
    [[NSUserDefaults standardUserDefaults] setObject:currentAppVersion forKey:kZMYReleaseNotesViewVersionKey];
    [[NSUserDefaults standardUserDefaults] synchronize];
}
//检测是否是第一次启动
+ (BOOL)isAppOnFirstLaunch
{
    // Read stored version string
    NSString *previousAppVersion = [[NSUserDefaults standardUserDefaults] stringForKey:kZMYReleaseNotesViewVersionKey];
    
    // Flag app as on first launch if no previous app string is found
    BOOL isFirstLaunch = (!previousAppVersion) ? YES : NO;
    
    if (isFirstLaunch)
    {
        // Store current app version if needed
        [self storeCurrentAppVersionString];
    }
    
    return isFirstLaunch;
}
//检测是否有系统自动更新  有返回信息，没有检测最新版本,如果不是最新版，返回最新版信息
+ (void)isAppVersionUpdatedWithAppIdentifier:(NSString *)appIdentifier updatedInformation:(CompletionBlock)updatedInformation latestVersionInformation:(CompletionBlock)latestVersionInformation completionBlockError:(CompletionBlockError)completionBlockError
{
    if ([self isAppOnFirstLaunch] == NO) {
        //不是第一次打开程序
        // Read stored version string and current version string
        NSString *previousAppVersion = [[NSUserDefaults standardUserDefaults] stringForKey:kZMYReleaseNotesViewVersionKey];
        NSString *currentAppVersion = [[NSBundle mainBundle] infoDictionary][@"CFBundleVersion"];
        
        // Flag app as updated if a previous version string is found and it does not match with the current version string
        BOOL isUpdated = (previousAppVersion && ![previousAppVersion isEqualToString:currentAppVersion]) ? YES : NO;
        
        if (isUpdated || !previousAppVersion)
        {
            // Store current app version if needed
            [self storeCurrentAppVersionString];
        }
        if (isUpdated) {
            [self getTheLatestVersionInformationWithAppIdentifier:appIdentifier completionBlock:updatedInformation completionBlockError:completionBlockError];
        } else {
            [self isOrNotTheLatestVersionInformationWithAppIdentifier:appIdentifier isOrNotTheLatestVersionCompletionBlock:^(BOOL isLatestVersion, NSString *releaseNoteText, NSString *releaseVersionText, NSDictionary *resultDic) {
                if (isLatestVersion == NO) {
                    latestVersionInformation(releaseNoteText ,releaseVersionText ,resultDic);
                }
            } completionBlockError:completionBlockError];
        }
        
    }
}
//判断是否有最新版，并返回版本信息
+(void)isOrNotTheLatestVersionInformationWithAppIdentifier:(NSString *)appIdentifier isOrNotTheLatestVersionCompletionBlock:(isOrNotCompletionBlock)completionBlock completionBlockError:(CompletionBlockError)completionBlockError{
    [self getTheLatestVersionInformationWithAppIdentifier:appIdentifier completionBlock:^(NSString *releaseNoteText, NSString *releaseVersionText, NSDictionary *resultDic) {
        NSString *currentAppVersion = [[NSBundle mainBundle] infoDictionary][@"CFBundleVersion"];
        BOOL isLatest = [currentAppVersion isEqualToString:releaseVersionText];
        completionBlock(isLatest,releaseNoteText ,releaseVersionText ,resultDic);
    } completionBlockError:completionBlockError];
}
//获取最新版本信息
+(void)getTheLatestVersionInformationWithAppIdentifier:(NSString *)appIdentifier completionBlock:(CompletionBlock)completionBlock completionBlockError:(CompletionBlockError)completionBlockError{
    NSOperationQueue *operationQueue = [[NSOperationQueue alloc] init];
    operationQueue.maxConcurrentOperationCount = 1;
    ZMYVersionNotes *versionNotesOperation = [[ZMYVersionNotes alloc]init];
    NSLocale *locale = [NSLocale currentLocale];
    NSString *countryCode = [locale objectForKey:NSLocaleCountryCode];
    versionNotesOperation.requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"http://itunes.apple.com/lookup?id=%@&country=%@", appIdentifier, countryCode]];
    __block ZMYVersionNotes *weakOperation = versionNotesOperation;
    [versionNotesOperation setCompletionBlock:^{
        if (weakOperation.error)
        {
            NSError *error = weakOperation.error;
            
            [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                // Perform completion block with error
                completionBlockError(error);
            }];
        }
        else
        {
            // Get release note text
            NSString *releaseNotes = weakOperation.releaseNotesText;
            NSString *releaseVersion = weakOperation.version;
            NSDictionary *releaseDict = weakOperation.releaseDict;
            [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                // Perform completion block with note text
                completionBlock(releaseNotes ,releaseVersion , releaseDict);
            }];
        }
    }];
    // Add operation
    [operationQueue addOperation:versionNotesOperation];
}


/**************************************************  NSOperation配置  *************************************************/

#pragma mark - Instance Methods

- (void)start
{
    // Setup URL request
    NSURLRequest *request = [NSURLRequest requestWithURL:self.requestURL];
    self.isExecuting = YES;
    self.isConcurrent = YES;
    self.isFinished = NO;
    
    // Setup URL connection
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        self.urlConnection = [NSURLConnection connectionWithRequest:request delegate:self];
    }];
}

- (void)setIsExecuting:(BOOL)isExecuting
{
    [self willChangeValueForKey:@"isExecuting"];
    _isExecuting = isExecuting;
    [self didChangeValueForKey:@"isExecuting"];
    
    // Toggle network activity indicator
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:isExecuting];
}

- (void)setIsFinished:(BOOL)isFinished
{
    [self willChangeValueForKey:@"isFinished"];
    _isFinished = isFinished;
    [self didChangeValueForKey:@"isFinished"];
}

- (void)cancel
{
    [super cancel];
    
    // Cancel URL connection
    [self.urlConnection cancel];
    self.isFinished = YES;
    self.isExecuting = NO;
}

#pragma mark - Private Methods

- (void)extractReleaseNotes
{
    // Decode data
    NSError *decodeError;
    id rootObject = [NSJSONSerialization JSONObjectWithData:self.appMetadata options:NSJSONReadingAllowFragments error:&decodeError];
    
    if (!decodeError && [rootObject isKindOfClass:[NSDictionary class]])
    {
        NSDictionary *rootDictionary = (NSDictionary *)rootObject;
        id resultsObject = rootDictionary[@"results"];
        
        if ([resultsObject isKindOfClass:[NSArray class]])
        {
            NSArray *resultsArray = (NSArray *)resultsObject;
            if ([resultsArray count])
            {
                id metadataObject = resultsArray[0];
                
                if ([metadataObject isKindOfClass:[NSDictionary class]])
                {
                    NSDictionary *metadataDictionary = (NSDictionary *)metadataObject;
                    self.releaseDict = metadataDictionary;
                    id releaseVersionObject = metadataDictionary[@"version"];
                    
                    if ([releaseVersionObject isKindOfClass:[NSString class]])
                    {
                        // Set release note text
                        self.version = releaseVersionObject;
                    }
                    id releaseNotesObject = metadataDictionary[@"releaseNotes"];
                    
                    if ([releaseNotesObject isKindOfClass:[NSString class]])
                    {
                        // Set release note text
                        self.releaseNotesText = releaseNotesObject;
                        
                        self.isExecuting = NO;
                        self.isFinished = YES;
                        
                        return;
                    }
                }
            }
        }
    }
    
    decodeError = [NSError errorWithDomain:@"error.releaseNotes" code:0 userInfo:nil];
    self.error = decodeError;
    self.isExecuting = NO;
    self.isFinished = YES;
}

#pragma mark - NSURLConnectionDelegate Methods

- (NSURLRequest *)connection:(NSURLConnection *)connection willSendRequest:(NSURLRequest *)request redirectResponse:(NSURLResponse *)response
{
    return request;
}

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse *)cachedResponse
{
    return cachedResponse;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    // Setup buffer
    self.bufferData = [NSMutableData data];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    // Append data to buffer
    [self.bufferData appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // Set release notes data
    self.appMetadata = self.bufferData;
    self.bufferData = nil;
    
    // Extract release notes text
    [self extractReleaseNotes];
}

- (void)connection:(NSURLConnection*)connection didFailWithError:(NSError*)error
{
    // Set error
    self.error = error;
    
    self.isExecuting = NO;
    self.isFinished = YES;
}











@end
