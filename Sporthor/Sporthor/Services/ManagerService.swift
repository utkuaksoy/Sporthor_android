//
//  ManagerService.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import Foundation
import NetworkKit

enum ManagerService {
    case addSportClub(_ request: [String: Any])
    case updateSportClubFiles(_ request: [String: Any])
    case removeSportClubFiles(_ request: [String: Any])
    case getSportClub
    case updateSportClub(_ request: [String: Any])
    case removeSportClub(_ request: [String: Any])
    case getClubsAndDetails(_ request: [String: Any])
    case deleteCoach(_ request: [String: Any])
    case updateCoach(_ request: [String: Any])
}

extension ManagerService: NetworkService {
    var path: String {
        switch self {
        case .addSportClub:
            return "/api/Manager/AddSportClub"
        case .updateSportClubFiles:
            return "/api/Manager/UpdateSportClubFiles"
        case .removeSportClubFiles:
            return "/api/Manager/RemoveSportClubFiles"
        case .getSportClub:
            return "/api/Manager/GetSportClub"
        case .updateSportClub:
            return "/api/Manager/UpdateSportClub"
        case .removeSportClub:
            return "/api/Manager/RemoveSportClub"
        case .getClubsAndDetails:
            return "/api/Manager/GetClubsAndDetails"
        case .deleteCoach:
            return "/api/Manager/DeleteCoach"
        case .updateCoach:
            return "/api/Manager/UpdateCoach"
        }
    }
    var method: HTTPMethod {
        switch self {
        case .addSportClub,
                .updateSportClubFiles,
                .removeSportClubFiles,
                .getSportClub,
                .updateSportClub,
                .removeSportClub,
                .getClubsAndDetails,
                .deleteCoach,
                .updateCoach:
            return .POST
        }
    }
    
    var parameters: [String: Any]? {
        switch self {
        case .addSportClub(let request),
                .updateSportClubFiles(let request),
                .removeSportClubFiles(let request),
                .updateSportClub(let request),
                .removeSportClub(let request),
                .getClubsAndDetails(let request),
                .deleteCoach(let request),
                .updateCoach(let request):
            return request
        default:
            return nil
        }
    }
    
    var token: String? {
        return ApplicationContext.shared.authToken
    }
}
