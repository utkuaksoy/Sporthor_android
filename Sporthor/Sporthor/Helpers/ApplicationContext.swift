//
//  ApplicationContext.swift
//  Sporthor
//
//  Created by derTurke on 28.02.2025.
//

import Foundation

public final class ApplicationContext {
    static let shared = ApplicationContext()
    
    private init() {}
    
    var lang: String {
        get {
            return UserDefaultsManager.shared.getString(forKey: "lang") ?? "tr_TR"
        } set {
            UserDefaultsManager.shared.setString(newValue, forKey: "lang")
        }
    }
    
    var authResponse: AuthResponse? {
        get {
            return KeychainManager.shared.get(AuthResponse.self, forKey: "authResponse") ?? AuthResponse()
        } set {
            KeychainManager.shared.set(newValue, forKey: "authResponse")
        }
    }
    
    var getConfiguration: GetConfigurationResponse {
        get {
            return UserDefaultsManager.shared.get(GetConfigurationResponse.self, forKey: "getConfiguration") ?? GetConfigurationResponse()
        } set {
            UserDefaultsManager.shared.set(newValue, forKey: "getConfiguration")
        }
    }
    
    var authToken: String? {
        get {
            return authResponse?.authToken
        }
     }
    
    var isFirstLaunch: Bool {
        get {
            return UserDefaultsManager.shared.getBool(forKey: "isFirstLaunch") ?? false
        } set {
            UserDefaultsManager.shared.setBool(newValue, forKey: "isFirstLaunch")
        }
    }
    
    var isSelectedCoach: Bool = false
    var isSelectedClubOfficial: Bool = false
    
    var userInfo: UserInfo? {
        get {
            return KeychainManager.shared.get(UserInfo.self, forKey: "UserInfo")
        } set {
            KeychainManager.shared.set(newValue, forKey: "UserInfo")
        }
    }
    
    var profilePhoto: String {
        return authResponse?.profilePhoto ?? ""
    }
    
    var isPrivateAccount: Bool = false
    
    var isLogin: Bool = false
    
    var userId: String {
        return authResponse?.userId ?? ""
    }
}
