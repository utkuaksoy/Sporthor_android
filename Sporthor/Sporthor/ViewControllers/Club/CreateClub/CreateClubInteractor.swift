//
//  CreateClubInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import Factory
import NetworkKit

final class CreateClubInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreateClubInteractorDelegate? {
        get {
            return self.baseDelegate as? CreateClubInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CreateClubInteractorProtocol
extension CreateClubInteractor: CreateClubInteractorProtocol {
    func uploadImage(_ image: UIImage) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.uploadImage(image: image),
            responseType: ImageUploadResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.didUploadImage(filePath: response.filePath)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func deleteImage(filePath: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.deleteImage(filePath),
            responseType: String.self
        )
        
        switch result {
        case .success(_):
            delegate?.didDeleteImage()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addSportClub(_ request: [String: Any], isEdit: Bool = false) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: isEdit ? ManagerService.updateSportClub(request) : ManagerService.addSportClub(request),
            responseType: SportClubResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didAddSportClub(response.club)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
