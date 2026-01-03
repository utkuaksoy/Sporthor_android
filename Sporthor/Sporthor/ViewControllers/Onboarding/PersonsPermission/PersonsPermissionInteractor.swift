//
//  PersonsPermissionInteractor.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class PersonsPermissionInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: PersonsPermissionInteractorDelegate? {
        get {
            return self.baseDelegate as? PersonsPermissionInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - PersonsPermissionInteractorProtocol
extension PersonsPermissionInteractor: PersonsPermissionInteractorProtocol {

}
