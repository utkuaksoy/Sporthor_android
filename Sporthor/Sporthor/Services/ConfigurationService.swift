//
//  ConfigurationService.swift
//  Sporthor
//
//  Created by derTurke on 5.03.2025.
//

import NetworkKit

enum ConfigurationService {
    case getOnboarding
    case getConfiguration
    case getSeasons
    case getMenu
}

extension ConfigurationService: NetworkService {
    var path: String {
        switch self {
        case .getOnboarding:
            return "/api/Configuration/GetOnboarding"
        case .getConfiguration:
            return "/api/Configuration/GetConfiguration"
        case .getSeasons:
            return "/api/Configuration/GetSeasons"
        case .getMenu:
            return "/api/Configuration/GetMenu"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        .GET
    }
    
    var parameters: [String : Any]? {
        nil
    }
    
    var token: String? {
        switch self {
        case .getMenu:
            return ApplicationContext.shared.authToken
        default:
            return nil
        }
    }
}
