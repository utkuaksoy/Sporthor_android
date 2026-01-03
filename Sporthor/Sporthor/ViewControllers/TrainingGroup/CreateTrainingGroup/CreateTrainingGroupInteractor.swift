//
//  CreateTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation
import Factory

final class CreateTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreateTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? CreateTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CreateTrainingGroupInteractorProtocol
extension CreateTrainingGroupInteractor: CreateTrainingGroupInteractorProtocol {
    func getSeasons() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ConfigurationService.getSeasons, responseType: SeasonResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didGetSeasons(response.seasons)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addTrainingGroup(_ request: [String : Any], isEdit: Bool) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: isEdit ? CoachService.updateTrainingGroup(request) : CoachService.addTrainingGroup(request),
            responseType: TrainingGroupResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didAddTrainingGroup(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getRecomendedGroupNames(clubId: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: CoachService.getRecomendedGroupNames(clubId),
            responseType: GetRecomendedGroupNamesResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didGetRecomendedGroupNames(response.names)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
