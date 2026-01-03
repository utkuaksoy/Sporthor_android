//
//  MyTeamCoachesInteractor.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation
import Factory

final class MyTeamCoachesInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: MyTeamCoachesInteractorDelegate? {
        get {
            return self.baseDelegate as? MyTeamCoachesInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - MyTeamCoachesInteractorProtocol
extension MyTeamCoachesInteractor: MyTeamCoachesInteractorProtocol {
    func getClubsAndDetails(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ManagerService.getClubsAndDetails(request),
            responseType: GetClubsAndDetailsResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didGetClubsAndDetails(response.clubs)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
