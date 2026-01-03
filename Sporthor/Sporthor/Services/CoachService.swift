//
//  CoachService.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import NetworkKit

enum CoachService {
    case addTrainingGroup(_ request: [String: Any])
    case addTrainingGroupUser(_ request: [String: Any])
    case getTrainingGroupUser
    case confirmationTrainingGroupUser(_ request: [String: Any])
    case updateTrainingGroup(_ request: [String: Any])
    case getRecomendedGroupNames(_ clubId: String)
    case removeTrainingGroup(_ request: [String: Any])
}

extension CoachService: NetworkService {
    var path: String {
        switch self {
        case .addTrainingGroup:
            return "/api/Coach/AddTrainingGroup"
        case .addTrainingGroupUser:
            return "/api/Coach/AddTrainingGroupUser"
        case .getTrainingGroupUser:
            return "/api/Coach/GetTrainingGroupUser"
        case .confirmationTrainingGroupUser:
            return "/api/Coach/ConfirmationTrainingGroupUser"
        case .updateTrainingGroup:
            return "/api/Coach/UpdateTrainingGroup"
        case .getRecomendedGroupNames(let clubId):
            return "/api/Coach/GetRecomendedGroupNames?ClubId=\(clubId)"
        case .removeTrainingGroup:
            return "/api/Coach/RemoveTrainingGroup"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        switch self {
        case .addTrainingGroup,
                .addTrainingGroupUser,
                .getTrainingGroupUser,
                .confirmationTrainingGroupUser,
                .updateTrainingGroup,
                .removeTrainingGroup:
            return .POST
        case .getRecomendedGroupNames:
            return .GET
        }
    }
    
    var parameters: [String : Any]? {
        switch self {
        case .addTrainingGroup(let request),
                .addTrainingGroupUser(let request),
                .confirmationTrainingGroupUser(let request),
                .updateTrainingGroup(let request),
                .removeTrainingGroup(let request):
            return request
        case .getTrainingGroupUser:
            return [:]
        case .getRecomendedGroupNames:
            return nil
        }
    }
    
    var token: String? {
        switch self {
        case .addTrainingGroup,
                .addTrainingGroupUser,
                .getTrainingGroupUser,
                .confirmationTrainingGroupUser,
                .updateTrainingGroup,
                .getRecomendedGroupNames,
                .removeTrainingGroup:
            return ApplicationContext.shared.authToken
        }
    }
}
