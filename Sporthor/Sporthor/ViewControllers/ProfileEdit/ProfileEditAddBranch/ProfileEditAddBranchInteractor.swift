//
//  ProfileEditAddBranchInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.04.2025.
//
//

import Foundation

final class ProfileEditAddBranchInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ProfileEditAddBranchInteractorDelegate? {
        get {
            return self.baseDelegate as? ProfileEditAddBranchInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - ProfileEditAddBranchInteractorProtocol
extension ProfileEditAddBranchInteractor: ProfileEditAddBranchInteractorProtocol {

}
