//
//  SettingsInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation
import UserKit

final class SettingsInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SettingsInteractorDelegate? {
        get {
            return self.baseDelegate as? SettingsInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - SettingsInteractorProtocol
extension SettingsInteractor: SettingsInteractorProtocol {
    func logout() {
        KeychainManager.shared.clearAll()
        UserDefaultsManager.shared.removeAllObject(except: [LocalizationHelper.shared.userDefaultsKey])
        ApplicationContext.shared.isSelectedCoach = false
        ApplicationContext.shared.isSelectedClubOfficial = false
        self.delegate?.logoutSuccess()
    }
}
