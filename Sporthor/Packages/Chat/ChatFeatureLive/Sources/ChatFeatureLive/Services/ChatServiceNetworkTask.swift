//
//  ChatServiceNetworkTask.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 19.02.2025.
//

import Factory
import Foundation
import ChatKit
import NetworkKit
import UserKit

public enum ChatServiceNetworkTask: NetworkService {
    
    case getMessages(channelId: String, page: Int)
    
    public var baseURL: String {
        return "https://api.sporthor.com"
    }
    
    public var path: String {
        switch self {
        case .getMessages:
            return "/api/Chat/GetMessages"
        }
    }
    
    public var method: HTTPMethod {
        switch self {
        case .getMessages:
            return .GET
        }
    }
    
    public var headers: [String: String]? {
        return ["Content-Type": "application/json"]
    }
    
    public var parameters: [String: Any]? {
        switch self {
        case .getMessages(let channelId, let page):
            return [
                "ChannelId": channelId,
                "Page": page
            ]
        }
    }
    
    public var token: String? {
        return Container.shared.userManager()?.authToken
    }
}
