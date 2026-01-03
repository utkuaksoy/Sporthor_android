//
//  MyTeamCoachesDetailInteractor.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation
import Factory

final class MyTeamCoachesDetailInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: MyTeamCoachesDetailInteractorDelegate? {
        get {
            return self.baseDelegate as? MyTeamCoachesDetailInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - MyTeamCoachesDetailInteractorProtocol
extension MyTeamCoachesDetailInteractor: MyTeamCoachesDetailInteractorProtocol {
    func deleteCoach(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ManagerService.deleteCoach(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(_):
            delegate?.didDeleteCoach()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
