//
//  MessagesServiceNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 4.04.2025.
//

import Foundation
import NetworkKit
import UserKit

enum MessagesServiceNetworkTask: NetworkService {
    
    case getMessages
    case hideMessages(userId: String)

    var path: String {
        switch self {
        case .getMessages:
            return "/api/Chat/GetAllMessages"
        case .hideMessages:
            return "/api/Chat/HideMessages"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getMessages:
            return .GET
        case .hideMessages:
            return .POST
        }
    }
    
    var parameters: [String: Any]? {
        switch self {
        case .getMessages:
            return nil
        case .hideMessages(let userId):
            return [
                "groupId": userId
            ]
        }
    }
    
    var token: String? {
        return ApplicationContext.shared.authToken
    }
}
