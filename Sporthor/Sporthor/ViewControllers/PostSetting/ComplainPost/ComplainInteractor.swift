//
//  ComplainInteractor.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation
import Factory

final class ComplainInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ComplainInteractorDelegate? {
        get {
            return self.baseDelegate as? ComplainInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - ComplainInteractorProtocol
extension ComplainInteractor: ComplainInteractorProtocol {
    func reportPost(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.reportPost(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didReportPost()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
