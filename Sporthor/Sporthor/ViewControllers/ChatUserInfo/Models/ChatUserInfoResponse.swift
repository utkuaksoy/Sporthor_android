//
//  ChatUserInfoResponse.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import Foundation
import ModelParsers

struct ChatUserInfoResponse: Decodable {
    @SafeDecode
    private(set) var userId: String
    
    @SafeDecode
    private(set) var name: String
    
    @SafeDecode
    private(set) var userName: String
    
    @SafeOptionalDecode
    private(set) var profilePhoto: String?
    
    @SafeDecode
    private(set) var followersCount: Int
    
    @SafeDecode
    private(set) var followingCount: Int
    
    @SafeDecode
    private(set) var postCount: Int
    
    @SafeDecode
    private(set) var mediaCount: Int
    
    @SafeDecode
    private(set) var isFollow: Bool
} 
