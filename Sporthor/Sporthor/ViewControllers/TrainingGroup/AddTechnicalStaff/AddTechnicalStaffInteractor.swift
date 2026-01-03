//
//  AddTechnicalStaffInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation
import Factory

final class AddTechnicalStaffInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AddTechnicalStaffInteractorDelegate? {
        get {
            return self.baseDelegate as? AddTechnicalStaffInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - AddTechnicalStaffInteractorProtocol
extension AddTechnicalStaffInteractor: AddTechnicalStaffInteractorProtocol {
    func updateTechnicalStaffRole(_ request: [String: Any]) async {
        guard let networkManager else { return }
        delegate?.didUpdateTechnicalStaffRole()
    }
}
