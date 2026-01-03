//
//  SuccessCreateClubInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessCreateClubInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SuccessCreateClubInteractorDelegate? {
        get {
            return self.baseDelegate as? SuccessCreateClubInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - SuccessCreateClubInteractorProtocol
extension SuccessCreateClubInteractor: SuccessCreateClubInteractorProtocol {

}
