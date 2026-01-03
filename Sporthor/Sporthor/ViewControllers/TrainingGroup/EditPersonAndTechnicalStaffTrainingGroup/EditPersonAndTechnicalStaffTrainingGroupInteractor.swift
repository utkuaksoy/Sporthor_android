//
//  EditPersonAndTechnicalStaffTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 31.10.2025.
//
//

import Foundation
import Factory

final class EditPersonAndTechnicalStaffTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: EditPersonAndTechnicalStaffTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? EditPersonAndTechnicalStaffTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol
extension EditPersonAndTechnicalStaffTrainingGroupInteractor: EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol {
    func addTrainingGroupUser(_ request: [String : Any], isDelete: Bool) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: CoachService.addTrainingGroupUser(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didAddTrainingGroupUser(isDelete: isDelete)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateCoach(_ request: [String: Any], isDelete: Bool) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ManagerService.updateCoach(request), responseType: NullResponse.self)
        switch result {
        case .success(_):
            delegate?.didAddTrainingGroupUser(isDelete: isDelete)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
