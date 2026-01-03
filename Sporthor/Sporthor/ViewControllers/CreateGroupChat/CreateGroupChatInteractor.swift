//
//  CreateGroupChatInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import Factory
import NetworkKit
import UIKit

final class CreateGroupChatInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreateGroupChatInteractorDelegate? {
        get {
            return self.baseDelegate as? CreateGroupChatInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager) private var networkManager
    private var users: [CreateChatUserModel] = []
}

// MARK: - CreateGroupChatInteractorProtocol
extension CreateGroupChatInteractor: CreateGroupChatInteractorProtocol {

    func updateGroup(groupId: String, newUsers: [String]) async {
        guard let networkManager else { return }
        let request = EditGroupNetworkTask.updateGroup(
            request: .init(
                groupId: groupId,
                newUsers: newUsers
            )
        )
        
        let result = await networkManager.request(service: request, responseType: EmptyResponse.self)
        switch result {
        case .success:
            delegate?.didUpdateGroupSuccess()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func fetchAllContacts() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: GroupChatNetworkTask.getUsers,
            responseType: ChatUserResponseModel.self
        )
        switch result {
        case .success(let response):
            self.users = response.friends
            delegate?.didFetchAllContacts(response.friends)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func search(query: String) {
        let filtered = users.filter { $0.name.lowercased().contains(query.lowercased()) }
        delegate?.didSearchResults(filtered)
    }
    
    func createGroup(
        name: String,
        selectedUsers: [CreateChatUserModel],
        groupImagePath: String?
    ) async {
        guard let networkManager else { return }
        let request = GroupChatRequestModel(
            name: name,
            image: groupImagePath,
            users: selectedUsers.map {
                $0.id
            },
            isPrivate: false
        )
        let result = await networkManager.request(
            service: GroupChatNetworkTask.createGroupChat(
                request: request
            ),
            responseType: GroupChatResponseModel.self
        )
        switch result {
        case .success(let response):
            self.delegate?.didCreateGroupSuccess(response: response)
        case .failure(let error):
            self.delegate?.didFailure(error)
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
