//
//  CalendarAddMissionTypeSelectorInteractor.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//
//

import Foundation

final class CalendarAddMissionTypeSelectorInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarAddMissionTypeSelectorInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarAddMissionTypeSelectorInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CalendarAddMissionTypeSelectorInteractorProtocol
extension CalendarAddMissionTypeSelectorInteractor: CalendarAddMissionTypeSelectorInteractorProtocol {

}
