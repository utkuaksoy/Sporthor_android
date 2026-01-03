//
//  NotificationInteractor.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//
//

import Foundation
import Factory

final class NotificationInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: NotificationInteractorDelegate? {
        get {
            return self.baseDelegate as? NotificationInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - NotificationInteractorProtocol
extension NotificationInteractor: NotificationInteractorProtocol {
    func getNotifications() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ProfileService.getNotifications,
            responseType: NotificationResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didGetNotifications(response.notifications)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func confirmationTrainingGroupUser(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: CoachService.confirmationTrainingGroupUser(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didConfirmationTrainingGroupUser()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func confirmationFollow(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.confirmationFollow(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didConfirmationTrainingGroupUser()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
