//
//  TeamService.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//

import Foundation

enum TeamService {
    case getTeams
    case getSporthorTeams
    case saveUserTeams(_ request: [String: Any])
}

extension TeamService: NetworkService {
    var path: String {
        switch self {
        case .getTeams:
            return "/api/Teams/GetTeams"
        case .getSporthorTeams:
            return "/api/Teams/GetSporthorTeams"
        case .saveUserTeams:
            return "/api/Teams/SaveUserTeams"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .saveUserTeams:
            return .POST
        default:
            return .GET
        }
    }
    
    var parameters: [String : Any]? {
        switch self {
        case .saveUserTeams(let request):
            return request
        default:
            return nil
        }
    }
    
    var token: String? {
        switch self {
        case .saveUserTeams,
                .getSporthorTeams:
            return ApplicationContext.shared.authToken
        default:
            return nil
        }
    }
}
