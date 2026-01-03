//
//  AuthenticationForgotPasswordInteractor.swift
//  Sporthor
//
//  Created by derTurke on 20.02.2025.
//
//

import Foundation
import Factory

final class AuthenticationForgotPasswordInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationForgotPasswordInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationForgotPasswordInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - AuthenticationForgotPasswordInteractorProtocol
extension AuthenticationForgotPasswordInteractor: AuthenticationForgotPasswordInteractorProtocol {
    func forgotPassword(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: AuthenticationService.forgotPassword(request), responseType: NullResponse.self)
        
        switch result {
        case .success(_):
            delegate?.didForgotPassword()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
