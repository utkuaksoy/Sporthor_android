//
//  ProfileEditInteractor.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//
//

import UIKit
import Factory


final class ProfileEditInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ProfileEditInteractorDelegate? {
        get {
            return self.baseDelegate as? ProfileEditInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - ProfileEditInteractorProtocol
extension ProfileEditInteractor: ProfileEditInteractorProtocol {
    func getProfileSummary() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ProfileService.getProfileSummary, responseType: GetProfileSummaryResponse.self)
        switch result {
        case .success(let response):
            delegate?.didGetProfileSummary(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getBranches() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ProfileService.getBranches, responseType: ProfileSummaryHighlights.self)
        switch result {
        case .success(let response):
            delegate?.didGetBranches(response.branches ?? [])
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getBranchAttributes(request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ProfileService.getBranchAttributes(request), responseType: ProfileSummaryHighlightsBranchAttributes.self)
        switch result {
        case .success(let response):
            delegate?.didGetBranchAttributes(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateProfileSummary(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ProfileService.updateProfileSummary(request), responseType: NullResponse.self)
        switch result {
        case .success(_):
            delegate?.didUpdateProfileSummary()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func uploadImage(_ image: UIImage) async {
        guard let networkManager else { return }
        let request = UploadService.uploadImage(image: image)
        let result = await networkManager.request(
            service: request,
            responseType: ImageUploadResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.didUploadSuccess(filePath: response.filePath)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateProfileImage(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ProfileService.updateProfileImage(request), responseType: NullResponse.self)
        switch result {
        case .success(_):
            break
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
