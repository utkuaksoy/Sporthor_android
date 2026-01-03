//
//  UserManager.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 4.04.2025.
//

import CommonKit
import Factory
import Foundation
import UserKit

public final class UserManagerLive {
    
    public init() {}
    
    var lang: String {
        get {
            return UserDefaultsManager.shared.getString(forKey: "lang") ?? "tr_TR"
        } set {
            UserDefaultsManager.shared.setString(newValue, forKey: "lang")
        }
    }
    
    var authResponse: AuthResponse {
        get {
            return KeychainManager.shared.get(AuthResponse.self, forKey: "authResponse") ?? AuthResponse()
        } set {
            KeychainManager.shared.set(newValue, forKey: "authResponse")
        }
    }
    
    var authToken: String? {
        get {
            return authResponse.authToken
        }
    }
    
    var isSelectedCoach: Bool = false
    var isSelectedClubOfficial: Bool = false
    
    var userInfo: UserInfoResponse? {
        get {
            return KeychainManager.shared.get(UserInfoResponse.self, forKey: "UserInfo")
        } set {
            KeychainManager.shared.set(newValue, forKey: "UserInfo")
        }
    }
    
    var userId: String {
        return authResponse.userId ?? ""
    }
    
    var userName: String {
        return authResponse.userName ?? ""
    }
    
    var userAvatar: String {
        return authResponse.profilePhoto ?? ""
    }
}

public extension Container {
    var userManager: Factory<UserManagerLive?> {
        self { UserManagerLive() }.singleton
    }
}
