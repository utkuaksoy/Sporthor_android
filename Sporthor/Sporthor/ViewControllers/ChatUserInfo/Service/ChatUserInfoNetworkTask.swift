//
//  ChatUserInfoNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//

import Foundation
import NetworkKit
import UserKit

enum ChatUserInfoNetworkTask: NetworkService {
    
    case getChatUserProfile(userId: String, groupId: String)

    var path: String {
        switch self {
        case .getChatUserProfile(let userId, let groupId):
            return "/api/Chat/GetChatUserProfile?GroupId=\(groupId)&UserId=\(userId)"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getChatUserProfile:
            return .GET
        }
    }
    
    var token: String? {
        return ApplicationContext.shared.authToken
    }
}
