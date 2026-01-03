//
//  AuthenticationService.swift
//  Sporthor
//
//  Created by derTurke on 26.02.2025.
//

import NetworkKit

enum AuthenticationService {
    case generateOtp(_ request: [String: Any])
    case valideOtp(_ request: [String: Any])
    case loginWithUsername(_ request: [String: Any])
    case checkUsername(_ request: [String: Any])
    case register(_ request: [String: Any])
    case updateProfile(_ request: [String: Any])
    case forgotPassword(_ request: [String: Any])
    case info
    case deleteAccount
}

extension AuthenticationService: NetworkService {
    var path: String {
        switch self {
        case .generateOtp:
            return "/api/Authentication/GenerateOtp"
        case .valideOtp:
            return "/api/Authentication/ValidateOtp"
        case .loginWithUsername:
            return "/api/Authentication/LoginWithUserName"
        case .checkUsername:
            return "/api/Authentication/CheckUsername"
        case .register:
            return "/api/Authentication/Register"
        case .updateProfile:
            return "/api/Authentication/UpdateProfile"
        case .forgotPassword:
            return "/api/Authentication/ForgotPassword"
        case .info:
            return "/api/Authentication/Info"
        case .deleteAccount:
            return "/api/Authentication/DeleteAccount"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        switch self {
        default:
            return .POST
        }
    }
    
    var parameters: [String : Any]? {
        switch self {
        case .generateOtp(let request):
            return request
        case .valideOtp(let request):
            return request
        case .loginWithUsername(let request):
            return request
        case .checkUsername(let request):
            return request
        case .register(let request):
            return request
        case .updateProfile(let request):
            return request
        case .forgotPassword(let request):
            return request
        case .deleteAccount:
            return [:]
        case .info:
            return nil
        }
    }
    
    var token: String? {
        switch self {
        case .updateProfile, .info, .deleteAccount:
            return ApplicationContext.shared.authToken
        default:
            return nil
        }
    }
}


