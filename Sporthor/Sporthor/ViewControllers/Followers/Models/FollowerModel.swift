//
//  FollowerModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation
import ModelParsers
import ComponentKit

struct FollowerModel: Decodable {
    @SafeDecode
    private(set) var id: String
    @SafeDecode
    private(set) var username: String
    @SafeDecode
    private(set) var name: String
    @SafeOptionalDecode
    private(set) var imageUrl: String?
    @SafeDecode
    var isFollow: Bool
    @SafeDecode
    private(set) var summary: String
    @SafeDecode
    private(set) var role: String
    @SafeDecode
    var isCurrentUser: Bool
}

extension FollowerModel {
    func toCellModel() -> CKFollowerCellModel {
        CKFollowerCellModel(
            imageUrl: imageUrl,
            name: name,
            summary: summary,
            followState: isFollow ? .following : .notFollowing,
            isCurrentUser: isCurrentUser
        )
    }
    
    func toSearchListModel() -> SearchList {
        SearchList(id: id,
                   image: imageUrl ?? "",
                   name: name,
                   userName: username,
                   attribute: "",
                   type: "",
                   summary: role,
                   isPast: false,
                   isSelected: false,
                   isTempFollowing: false)
    }
        
}
