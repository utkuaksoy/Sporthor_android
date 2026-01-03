//
//  OnboardingInteractor.swift
//  Sporthor
//
//  Created by derTurke on 6.02.2025.
//
//

import Foundation

final class OnboardingInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: OnboardingInteractorDelegate? {
        get {
            return self.baseDelegate as? OnboardingInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
}

// MARK: - OnboardingInteractorProtocol
extension OnboardingInteractor: OnboardingInteractorProtocol {

}
