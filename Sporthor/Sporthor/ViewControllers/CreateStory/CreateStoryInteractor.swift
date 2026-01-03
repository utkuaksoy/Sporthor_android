//
//  CreateStoryInteractor.swift
//  Sporthor
//
//  Created by derTurke on 26.04.2025.
//
//

import Foundation

final class CreateStoryInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreateStoryInteractorDelegate? {
        get {
            return self.baseDelegate as? CreateStoryInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CreateStoryInteractorProtocol
extension CreateStoryInteractor: CreateStoryInteractorProtocol {

}
