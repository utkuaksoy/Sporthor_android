//
//  CalendarAddDescriptionInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddDescriptionInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarAddDescriptionInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarAddDescriptionInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CalendarAddDescriptionInteractorProtocol
extension CalendarAddDescriptionInteractor: CalendarAddDescriptionInteractorProtocol {

}
