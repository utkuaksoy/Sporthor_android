//
//  ProfileActionButtonsDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import DesignKit
import UIKit

public struct ProfileActionButtonsDataModel: Decodable {
    let userId: String?
    var buttons: [ProfileActionButtonType]?
}

public enum ProfileActionButtonType: String, Decodable, CaseIterable {
    case follow
    case following
    case followRequestSent
    case message
    case invite
    case editProfile

    var title: String {
        switch self {
        case .follow: return "Takip Et"
        case .following: return "Takiptesin"
        case .followRequestSent: return "Takip isteği gönderildi"
        case .message: return "Mesaj"
        case .invite: return "Davet Et"
        case .editProfile: return "Profili Düzenle"
        }
    }

    var backgroundColor: UIColor {
        switch self {
        case .follow:
            return DesignKitColorName.backgroundPrimaryGreen.color
        case .following, .followRequestSent, .message, .invite, .editProfile:
            return .white
        }
    }
    
    var isBorder: Bool {
        switch self {
        case .follow:
            return false
        case .following, .followRequestSent, .message, .invite, .editProfile:
            return true
        }
    }
}

extension ProfileActionButtonType {
    mutating func toggleFollowStatus() {
        switch self {
        case .follow:
            self = .followRequestSent
        case .following:
            self = .follow
        case .followRequestSent:
            self = .follow
        default:
            break
        }
    }
}
