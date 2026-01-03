//
//  MenuInteractor.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation
import Factory

final class MenuInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: MenuInteractorDelegate? {
        get {
            return self.baseDelegate as? MenuInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - MenuInteractorProtocol
extension MenuInteractor: MenuInteractorProtocol {
    func getMenu() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ConfigurationService.getMenu,
            responseType: MenuResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didGetMenu(response.menu)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
