//
//  ExperienceBirthdateAndGenderInteractor.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBirthdateAndGenderInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ExperienceBirthdateAndGenderInteractorDelegate? {
        get {
            return self.baseDelegate as? ExperienceBirthdateAndGenderInteractorDelegate
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

// MARK: - ExperienceBirthdateAndGenderInteractorProtocol
extension ExperienceBirthdateAndGenderInteractor: ExperienceBirthdateAndGenderInteractorProtocol {
    func updateProfile(_ request: [String : Any]) async {
        let result = await networkManager.request(service: AuthenticationService.updateProfile(request),
                                                  responseType: NullResponse.self)
        
        switch result {
        case .success(_):
            delegate?.didUpdateProfile()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
