//
//  CalendarMapViewInteractor.swift
//  Sporthor
//
//  Created by derTurke on 9.06.2025.
//
//

import Foundation

final class CalendarMapViewInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarMapViewInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarMapViewInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CalendarMapViewInteractorProtocol
extension CalendarMapViewInteractor: CalendarMapViewInteractorProtocol {

}
