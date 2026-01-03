//
//  GroupChatNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 4.04.2025.
//


import Foundation
import NetworkKit
import UserKit

enum GroupChatNetworkTask: NetworkService {
    
    case createGroupChat(request: GroupChatRequestModel)
    case getUsers

    var path: String {
        switch self {
        case .createGroupChat:
            return "/api/Chat/GenerateChatGroup"
        case .getUsers:
            return "/api/Profile/GetMyFriends"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .createGroupChat:
            return .POST
        case .getUsers:
            return .GET
        }
    }
    
    var httpBody: Data? {
        switch self {
        case .createGroupChat(let request):
            return request.asData
        case .getUsers:
            return nil
        }
    }
    
    var token: String? {
        return ApplicationContext.shared.authToken
    }
}
