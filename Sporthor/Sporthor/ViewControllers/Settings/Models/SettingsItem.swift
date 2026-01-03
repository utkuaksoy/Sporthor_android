//
//  SettingsItem.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import DesignKit
import Foundation
import UIKit

enum SettingsItem: CaseIterable {
//    case applicationPreferences
    case accountSettings
    case about
    case termsOfUse
    case privacyPolicy
    case contactCenter
    case logout
    
    var title: String {
        switch self {
//        case .applicationPreferences:
//            return L10n.Settings.ApplicationPreferences.title
        case .accountSettings:
            return "Hesap Ayarları"
        case .about:
            return L10n.Settings.About.title
        case .termsOfUse:
            return "Kullanım Şartları"
        case .privacyPolicy:
            return "Gizlilik Politikası"
        case .contactCenter:
            return L10n.Settings.ContactCenter.title
        case .logout:
            return L10n.Settings.Logout.title
        }
    }
    
    var icon: UIImage {
        switch self {
//        case .applicationPreferences:
//            return Asset.settingsApplicationPreferencesIcon.image
        case .accountSettings:
            return Asset.settingsAccountPrivacyIcon.image
        case .about:
            return Asset.settingsAboutIcon.image
        case .termsOfUse:
            return Asset.termsOfUse.image
        case .privacyPolicy:
            return Asset.privacyPolicy.image
        case .contactCenter:
            return Asset.settingsContractCenterIcon.image
        case .logout:
            return Asset.settingsLogoutIcon.image
        }
    }
    
    var route: SettingsRoutes {
        switch self {
//        case .applicationPreferences:
//            return .applicationPreferences
        case .accountSettings:
            return .accountSettings
        case .about:
            return .about
        case .termsOfUse:
            return .termsOfUse
        case .privacyPolicy:
            return .privacyPolicy
        case .contactCenter:
            return .contactCenter
        case .logout:
            return .logout
        }
    }
    
    var isDestructive: Bool {
        return self == .logout
    }
} 
