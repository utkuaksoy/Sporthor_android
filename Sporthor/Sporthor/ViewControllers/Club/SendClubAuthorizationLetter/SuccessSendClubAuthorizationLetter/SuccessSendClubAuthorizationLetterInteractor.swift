//
//  SuccessSendClubAuthorizationLetterInteractor.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

final class SuccessSendClubAuthorizationLetterInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SuccessSendClubAuthorizationLetterInteractorDelegate? {
        get {
            return self.baseDelegate as? SuccessSendClubAuthorizationLetterInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - SuccessSendClubAuthorizationLetterInteractorProtocol
extension SuccessSendClubAuthorizationLetterInteractor: SuccessSendClubAuthorizationLetterInteractorProtocol {

}
