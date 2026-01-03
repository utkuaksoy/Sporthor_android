//
//  SportIconCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//

import Foundation
import ModelParsers

struct EditGroupNameResponse: Decodable {
    
    @SafeDecode
    private(set) var groupId: String
    
    @SafeDecode
    private(set) var groupName: String
    
    @SafeOptionalDecode
    private(set) var groupImageUrl: String?
    
    @LossyArray
    private(set) var icons: [SportIconModel]
}

struct SportIconModel: Decodable {
    
    @SafeDecode
    private(set) var iconPath: String
    
    @SafeDecode
    private(set) var bgColor: String
} 
