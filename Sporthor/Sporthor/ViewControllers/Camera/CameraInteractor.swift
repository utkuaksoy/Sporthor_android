//
//  CameraInteractor.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import Foundation

final class CameraInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CameraInteractorDelegate? {
        get {
            return self.baseDelegate as? CameraInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CameraInteractorProtocol
extension CameraInteractor: CameraInteractorProtocol {

}
