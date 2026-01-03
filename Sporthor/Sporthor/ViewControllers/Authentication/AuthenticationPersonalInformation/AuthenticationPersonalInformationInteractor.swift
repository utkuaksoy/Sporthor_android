//
//  AuthenticationPersonalInformationInteractor.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPersonalInformationInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationPersonalInformationInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationPersonalInformationInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - AuthenticationPersonalInformationInteractorProtocol
extension AuthenticationPersonalInformationInteractor: AuthenticationPersonalInformationInteractorProtocol {

}
