//
//  CreateChatInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//
//

import Factory
import Foundation
import NetworkKit

final class CreateChatInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreateChatInteractorDelegate? {
        get {
            return self.baseDelegate as? CreateChatInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager) private var networkManager
    private var users: [CreateChatUserModel] = []
}

// MARK: - CreateChatInteractorProtocol
extension CreateChatInteractor: CreateChatInteractorProtocol {
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
        selectedUserId: String,
        groupImagePath: String?,
        userName: String
    ) async {
        guard let networkManager else { return }
        let request = GroupChatRequestModel(
            name: name,
            image: groupImagePath,
            users: [selectedUserId],
            isPrivate: true
        )
        let result = await networkManager.request(
            service: GroupChatNetworkTask.createGroupChat(
                request: request
            ),
            responseType: GroupChatResponseModel.self
        )
        switch result {
        case .success(let response):
            self.delegate?.didCreateGroupSuccess(response: response, userName: userName)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
}
