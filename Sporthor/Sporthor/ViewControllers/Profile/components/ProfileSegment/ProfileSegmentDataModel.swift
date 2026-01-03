//
//  ProfileSegmentDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 13.02.2025.
//

import Foundation
import ModelParsers

struct ProfileSegmentDataModel: Decodable {
    @LossyArray
    private(set) var segments: [SegmentItem]
    @SafeDecode
    private(set) var selectedSegmentIndex: Int
    @SafeDecode
    private(set) var userId: String
}

struct SegmentItem: Decodable, SafeDecodable {
    
    @SafeDecode
    private(set) var id: String
    @SafeDecode
    private(set) var title: String
    @SafeDecode
    private(set) var image: String
    @SafeDecode
    private(set) var type: SegmentType
    @SafeDecode
    private(set) var isSelected: Bool
    
    public static func safeDecode(from decoder: any Decoder) throws -> SegmentItem {
        try SegmentItem(from: decoder)
    }
}

public enum SegmentType: String, Decodable, HasDefaultDecode, CaseIterable {
    case posts = "Posts"
    case statistics = "Statistics" 
    case personalInfo = "PersonalInfo"
    case teamSquad = "TeamSquad"
    case matches = "Matches"
    
    public static var defaultDecodeValue: Self { .posts }
}
