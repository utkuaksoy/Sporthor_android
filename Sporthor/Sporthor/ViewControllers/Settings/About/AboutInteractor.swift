//
//  AboutInteractor.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//
//

import Foundation

final class AboutInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AboutInteractorDelegate? {
        get {
            return self.baseDelegate as? AboutInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - AboutInteractorProtocol
extension AboutInteractor: AboutInteractorProtocol {

}
