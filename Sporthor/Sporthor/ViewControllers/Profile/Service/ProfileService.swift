//
//  ProfileService.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

enum ProfileService {
    case fetchProfile(userId: String?)
    case getProfileDetail(userId: String?)
    case getProfileSummary
    case getBranches
    case getBranchAttributes(_ request: [String: Any])
    case updateProfileSummary(_ request: [String: Any])
    case updateProfileImage(_ request: [String: Any])
    case getUserPostAsync(_ request: [String: Any])
    case getNotifications
    case getMyRoles
    case updateUserRoles(_ request: [String: Any])
    case updateProfilePublicPrivate(_ request: [String: Any])
}

extension ProfileService: NetworkService {

    var path: String {
        switch self {
        case .fetchProfile:
            return "/api/Profile/GetProfile"
        case .getProfileDetail:
            return "/api/Profile/GetProfileDetail"
        case .getProfileSummary:
            return "/api/Profile/GetProfileSummary"
        case .getBranches:
            return "/api/Profile/GetBranches"
        case .getBranchAttributes:
            return "/api/Profile/GetBranchAttributes"
        case .updateProfileSummary:
            return "/api/Profile/UpdateProfileSummary"
        case .updateProfileImage:
            return "/api/Profile/UpdateProfileImage"
        case .getUserPostAsync:
            return "/api/Social/GetUserPostsAsync"
        case .getNotifications:
            return "/api/Profile/GetNotifications"
        case .getMyRoles:
            return "/api/Profile/GetMyRoles"
        case .updateUserRoles:
            return "/api/Profile/UpdataUserRoles"
        case .updateProfilePublicPrivate:
            return "/api/Profile/UpdateProfilePublicPrivate"
        }
    }

    var method: HTTPMethod {
        switch self {
        case .fetchProfile,
                .getProfileDetail,
                .getProfileSummary,
                .getBranches,
                .getBranchAttributes,
                .getUserPostAsync,
                .getNotifications,
                .getMyRoles:
            return .GET
        case .updateProfileSummary,
                .updateProfileImage,
                .updateUserRoles,
                .updateProfilePublicPrivate:
            return .POST
        }
    }

    var parameters: [String: Any]? {
        switch self {
        case .fetchProfile(let userId), .getProfileDetail(let userId):
            if let userId {
                return [
                    "UserId": userId
                ]
            } else {
                return nil
            }
        case .getProfileSummary, .getBranches:
            return nil
        case .getBranchAttributes(let request):
            return request
        case .updateProfileSummary(let request):
            return request
        case .updateProfileImage(let request):
            return request
        case .getUserPostAsync(let request):
            return request
        case .getNotifications:
            return nil
        case .getMyRoles:
            return nil
        case .updateUserRoles(let request):
            return request
        case .updateProfilePublicPrivate(let request):
            return request
        }
    }
    
    var token: String? {
        switch self {
        case .fetchProfile,
                .getProfileDetail,
                .getProfileSummary,
                .updateProfileSummary,
                .updateProfileImage,
                .getUserPostAsync,
                .getNotifications,
                .getMyRoles,
                .updateUserRoles,
                .updateProfilePublicPrivate:
            return ApplicationContext.shared.authToken
        default:
            return nil
        }
    }
}
