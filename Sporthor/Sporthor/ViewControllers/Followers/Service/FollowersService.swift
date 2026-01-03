//
//  FollowersService.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 8.04.2025.
//

enum FollowersService {
    case fetchFollowers(userId: String)
    case fetchFollowing(userId: String,
                        role: Int? = nil)
    case fetchFollowTogether(userId: String)
    case followUser(userId: String)
    case unFollowUser(userId: String)
}

extension FollowersService: NetworkService {

    var path: String {
        switch self {
        case .fetchFollowers:
            return "/api/Social/GetFollowers"
        case .fetchFollowing:
            return "/api/Social/GetFollowing"
        case .fetchFollowTogether:
            return "/api/Social/GetFollowTogether"
        case .followUser:
            return "/api/Social/FollowUser"
        case .unFollowUser:
            return "/api/Social/UnFollowUser"
        }
    }

    var method: HTTPMethod {
        switch self {
        case .fetchFollowers, .fetchFollowing, .fetchFollowTogether:
            return .GET
        case .followUser, .unFollowUser:
            return .POST
        }
    }

    var parameters: [String: Any]? {
        switch self {
        case .fetchFollowers(let userId), .fetchFollowTogether(let userId):
            return [
                "UserId": userId
            ]
            
        case .fetchFollowing(let userId, let role):
            var request: [String: Any] = ["userId": userId]
            if let role { request["role"] = role }
            return request
            
        case .followUser(let userId), .unFollowUser(let userId):
            return [
                "targetUserId": userId
            ]
        }
    }
    
    var token: String? {
        switch self {
        case .fetchFollowers, .fetchFollowing, .fetchFollowTogether, .followUser, .unFollowUser:
            return ApplicationContext.shared.authToken
        }
    }
}
