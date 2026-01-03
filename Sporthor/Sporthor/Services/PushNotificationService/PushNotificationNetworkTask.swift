//
//  PushNotificationNetworkTask.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.04.2025.
//

import Foundation
import NetworkKit

enum PushNotificationNetworkTask {
    case pushToken(fcmToken: PushTokenRequest)
}

extension PushNotificationNetworkTask: NetworkService {
    var path: String {
        switch self {
        case .pushToken:
            return "/api/Profile/UpdateConfiguration"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        switch self {
        case .pushToken:
            return .POST
        }
    }

    var httpBody: Data? {
        switch self {
        case .pushToken(let request):
            return request.asData
        }
    }
    
    var token: String? {
        switch self {
        case .pushToken:
            return ApplicationContext.shared.authToken
        }
    }
}
