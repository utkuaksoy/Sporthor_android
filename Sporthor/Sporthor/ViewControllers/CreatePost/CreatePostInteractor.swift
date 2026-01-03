//
//  CreatePostInteractor.swift
//  Sporthor
//
//  Created by derTurke on 21.04.2025.
//
//

import Foundation

final class CreatePostInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreatePostInteractorDelegate? {
        get {
            return self.baseDelegate as? CreatePostInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - CreatePostInteractorProtocol
extension CreatePostInteractor: CreatePostInteractorProtocol {

}
