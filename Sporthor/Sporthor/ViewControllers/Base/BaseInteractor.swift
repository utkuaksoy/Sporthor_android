//
//  BaseInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.01.2025.
//

import Foundation

class BaseInteractor {
    // MARK: - VIPER Variables
    weak var baseDelegate: BaseInteractorDelegate?
}

// MARK: - BaseInteractorProtocol
extension BaseInteractor: BaseInteractorProtocol {
    func baseLogout() {
        KeychainManager.shared.clearAll()
        UserDefaultsManager.shared.removeAllObject(except: [LocalizationHelper.shared.userDefaultsKey])
        ApplicationContext.shared.isSelectedCoach = false
        ApplicationContext.shared.isSelectedClubOfficial = false
        ApplicationContext.shared.isLogin = false
        self.baseDelegate?.didBaseLogout()
    }
}
