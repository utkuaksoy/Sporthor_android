//
//  NextOnboardingInteractor.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class NextOnboardingInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: NextOnboardingInteractorDelegate? {
        get {
            return self.baseDelegate as? NextOnboardingInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    private let networkManager: NetworkKitProtocol
    
    override init() {
        networkManager = NetworkManager()
        super.init()
    }
}

// MARK: - NextOnboardingInteractorProtocol
extension NextOnboardingInteractor: NextOnboardingInteractorProtocol {
    func getOnboarding() async {
        let result = await networkManager.request(service: ConfigurationService.getOnboarding, responseType: OnboardingResponse.self)
        switch result {
        case .success(let response):
            delegate?.didGetOnboarding(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
