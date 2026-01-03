//
//  LocationInteractor.swift
//  Sporthor
//
//  Created by derTurke on 7.05.2025.
//
//

import Foundation

final class LocationInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: LocationInteractorDelegate? {
        get {
            return self.baseDelegate as? LocationInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - LocationInteractorProtocol
extension LocationInteractor: LocationInteractorProtocol {

}
