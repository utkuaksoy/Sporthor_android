//
//  GroupDetailServiceNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import Foundation
import NetworkKit

enum GroupDetailServiceNetworkTask {
    case getGroupChatService(groupId: String)
    case leaveGroup(groupId: String)
    case removeMember(groupId: String, userId: String)
}

extension GroupDetailServiceNetworkTask: NetworkService {
    var path: String {
        switch self {
        case .getGroupChatService(let groupId):
            return "/api/Chat/GetChatGroupDetail?GroupId=\(groupId)"
        case .leaveGroup:
            return "/api/Chat/LeaveChat"
        case .removeMember(let groupId, _):
            return "api/groups/\(groupId)/remove-member"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getGroupChatService:
            return .GET
        case .leaveGroup, .removeMember:
            return .POST
        }
    }
    
    var parameters: [String: Any]? {
        switch self {
        case .removeMember(_, let userId):
            return ["userId": userId]
        case .getGroupChatService:
            return nil
        case .leaveGroup(let groupId):
            return [
                "groupId": groupId
            ]
        }
    }
    
    var token: String? {
        switch self {
        case .getGroupChatService, .leaveGroup, .removeMember:
            return ApplicationContext.shared.authToken
        }
    }
} 
