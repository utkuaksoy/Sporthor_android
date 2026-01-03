//
//  ChatMediaAndDocumentNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import Foundation
import NetworkKit
import UserKit

enum ChatMediaAndDocumentNetworkTask: NetworkService {
    
    case getChatGroupAttachments(groupId: String)

    var path: String {
        switch self {
        case .getChatGroupAttachments(let groupId):
            return "/api/Chat/GetChatGroupAttachments?GroupId=\(groupId)"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getChatGroupAttachments:
            return .GET
        }
    }
    
    var token: String? {
        return ApplicationContext.shared.authToken
    }
}
