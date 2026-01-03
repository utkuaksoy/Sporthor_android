//
//  ExperienceBranchInteractor.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBranchInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ExperienceBranchInteractorDelegate? {
        get {
            return self.baseDelegate as? ExperienceBranchInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - ExperienceBranchInteractorProtocol
extension ExperienceBranchInteractor: ExperienceBranchInteractorProtocol {

}
