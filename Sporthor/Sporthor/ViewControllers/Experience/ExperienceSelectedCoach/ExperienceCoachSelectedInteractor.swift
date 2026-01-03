//
//  ExperienceCoachSelectedInteractor.swift
//  Sporthor
//
//  Created by derTurke on 16.05.2025.
//
//

import Foundation

final class ExperienceCoachSelectedInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ExperienceCoachSelectedInteractorDelegate? {
        get {
            return self.baseDelegate as? ExperienceCoachSelectedInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - ExperienceCoachSelectedInteractorProtocol
extension ExperienceCoachSelectedInteractor: ExperienceCoachSelectedInteractorProtocol {

}
