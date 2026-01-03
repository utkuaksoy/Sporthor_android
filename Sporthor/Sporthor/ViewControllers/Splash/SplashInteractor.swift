//
//  SplashInteractor.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//
//

import Foundation
import Factory

final class SplashInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SplashInteractorDelegate? {
        get {
            return self.baseDelegate as? SplashInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - SplashInteractorProtocol
extension SplashInteractor: SplashInteractorProtocol {
    func getLocalizations() async {
        let result = await LocalizationHelper.shared.getLocalization()
        switch result {
        case .success(_):
            delegate?.didGetLocalization()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getConfiguration() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ConfigurationService.getConfiguration,
            responseType: GetConfigurationResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didGetConfiguration(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
