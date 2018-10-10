//
//  JWTClaimsSet.h
//  JWT
//
//  Created by Klaas Pieter Annema on 31-05-13.
//  Copyright (c) 2013 Karma. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JWTClaimsSet : NSObject<NSCopying>

@property (nonatomic, readwrite, copy) NSString *issuer;//iss
@property (nonatomic, readwrite, copy) NSString *subject;
@property (nonatomic, readwrite, copy) NSString *audience;//aud
@property (nonatomic, readwrite, copy) NSDate *expirationDate;
@property (nonatomic, readwrite, copy) NSDate *notBeforeDate;//nbf
@property (nonatomic, readwrite, copy) NSDate *issuedAt;
@property (nonatomic, readwrite, copy) NSString *identifier;
@property (nonatomic, readwrite, copy) NSString *type;

@end
