//
//  SuccessTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SuccessTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? SuccessTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - SuccessTrainingGroupInteractorProtocol
extension SuccessTrainingGroupInteractor: SuccessTrainingGroupInteractorProtocol {

}
