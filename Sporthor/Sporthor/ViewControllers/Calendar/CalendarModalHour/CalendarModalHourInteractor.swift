//
//  CalendarModalHourInteractor.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarModalHourInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarModalHourInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarModalHourInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CalendarModalHourInteractorProtocol
extension CalendarModalHourInteractor: CalendarModalHourInteractorProtocol {

}
