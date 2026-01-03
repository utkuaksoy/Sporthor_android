//
//  SendClubAuthorizationLetterInteractor.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation
import Factory

final class SendClubAuthorizationLetterInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SendClubAuthorizationLetterInteractorDelegate? {
        get {
            return self.baseDelegate as? SendClubAuthorizationLetterInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - SendClubAuthorizationLetterInteractorProtocol
extension SendClubAuthorizationLetterInteractor: SendClubAuthorizationLetterInteractorProtocol {
    func updateSportClubFiles(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ManagerService.updateSportClubFiles(request),
            responseType: SportClubResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didSportClub(response.club)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func removeSportClubFiles(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ManagerService.removeSportClubFiles(request),
            responseType: SportClubResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didSportClub(response.club)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func uploadFile(url: URL) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.fileUpload(fileUrl: url),
            responseType: ImageUploadResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.didUploadFile(response.filePath)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
