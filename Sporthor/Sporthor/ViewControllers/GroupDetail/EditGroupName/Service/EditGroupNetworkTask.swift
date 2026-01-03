//
//  EditGroupNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//

import Foundation
import NetworkKit

enum EditGroupNetworkTask {
    case getChatGroupSummary(groupId: String)
    case updateGroup(request: UpdateGroupRequestModel)
}

extension EditGroupNetworkTask: NetworkService {
    var path: String {
        switch self {
        case .getChatGroupSummary(let groupId):
            return "/api/Chat/GetChatGroupSummary?GroupId=\(groupId)"
        case .updateGroup:
            return "/api/Chat/UpdateChatGroup"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getChatGroupSummary:
            return .GET
        case .updateGroup:
            return .POST
        }
    }
    
    var parameters: [String: Any]? {
        switch self {
        default:
            return nil
        }
    }
    
    var httpBody: Data? {
        switch self {
        case .updateGroup(let request):
            return request.asData
        default:
            return nil
        }
    }
    
    var token: String? {
        switch self {
        case .getChatGroupSummary, .updateGroup:
            return ApplicationContext.shared.authToken
        }
    }
}
