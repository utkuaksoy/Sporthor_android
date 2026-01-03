//
//  AddPersonAndTechnicalStaffTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 27.10.2025.
//
//

import Foundation

final class AddPersonAndTechnicalStaffTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AddPersonAndTechnicalStaffTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? AddPersonAndTechnicalStaffTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol
extension AddPersonAndTechnicalStaffTrainingGroupInteractor: AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol {

}
