//
//  CalendarModalDateInteractor.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//
//

import Foundation

final class CalendarModalDateInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarModalDateInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarModalDateInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CalendarModalDateInteractorProtocol
extension CalendarModalDateInteractor: CalendarModalDateInteractorProtocol {

}
