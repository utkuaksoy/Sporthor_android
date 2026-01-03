//
//  EditGroupNameInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import Factory
import NetworkKit
import UIKit

final class EditGroupNameInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: EditGroupNameInteractorDelegate? {
        get {
            return self.baseDelegate as? EditGroupNameInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - EditGroupNameInteractorProtocol
extension EditGroupNameInteractor: EditGroupNameInteractorProtocol {
    func fetchGroupSummary(groupId: String) async {
        guard let networkManager else { return }
        
        let request = EditGroupNetworkTask.getChatGroupSummary(groupId: groupId)
        let result = await networkManager.request(service: request, responseType: EditGroupNameResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didFetchGroupSummary(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateGroupName(groupId: String, name: String?, icon: String?) async {
        guard let networkManager else { return }
        let request = EditGroupNetworkTask.updateGroup(
            request: .init(
                groupId: groupId,
                name: name,
                image: icon
            )
        )
        
        let result = await networkManager.request(service: request, responseType: EmptyResponse.self)
        switch result {
        case .success:
            delegate?.didUpdateGroupName()
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
}
