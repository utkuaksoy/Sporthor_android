//
//  SelectionInteractor.swift
//  Sporthor
//
//  Created by derTurke on 14.04.2025.
//
//

import Foundation

final class SelectionInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SelectionInteractorDelegate? {
        get {
            return self.baseDelegate as? SelectionInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - SelectionInteractorProtocol
extension SelectionInteractor: SelectionInteractorProtocol {

}
