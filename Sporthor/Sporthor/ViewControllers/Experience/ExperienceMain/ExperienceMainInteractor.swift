//
//  ExperienceMainInteractor.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceMainInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ExperienceMainInteractorDelegate? {
        get {
            return self.baseDelegate as? ExperienceMainInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - ExperienceMainInteractorProtocol
extension ExperienceMainInteractor: ExperienceMainInteractorProtocol {

}
