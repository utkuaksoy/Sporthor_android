//
//  SearchesResponse.swift
//  Sporthor
//
//  Created by derTurke on 19.03.2025.
//

import Foundation
import ModelParsers

struct SearchesResponse: Decodable {
    @LossyArray var searchList: [SearchList]
}

struct SearchList: Decodable {
    @SafeDecode var id: String
    @SafeDecode var image: String
    @SafeDecode var name: String
    @SafeDecode var userName: String
    @SafeDecode var attribute: String
    @SafeDecode var type: String
    @SafeDecode var summary: String
    @SafeDecode var isPast: Bool
    @SafeDecode var isSelected: Bool
    @SafeDecode var isTempFollowing: Bool
}

extension SearchList {
    func toGetTrainingGroupUserModelUser() -> GetTrainingGroupUserModelUser {
        return GetTrainingGroupUserModelUser(id: id,
                                             name: name,
                                             username: userName,
                                             summary: summary,
                                             imageUrl: image,
                                             isFollow: false,
                                             isCurrentUser: false,
                                             isSelected: isSelected)
    }
}
