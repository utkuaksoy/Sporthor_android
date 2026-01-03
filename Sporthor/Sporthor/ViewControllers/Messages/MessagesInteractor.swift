//
//  MessagesInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.03.2025.
//
//

import Factory
import Foundation
import NetworkKit

final class MessagesInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: MessagesInteractorDelegate? {
        get {
            return self.baseDelegate as? MessagesInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager)
    private var networkManager
}

// MARK: - MessagesInteractorProtocol
extension MessagesInteractor: MessagesInteractorProtocol {
    func hideMessagesService(at indexPath: IndexPath, userId: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: MessagesServiceNetworkTask.hideMessages(userId: userId),
            responseType: EmptyResponse.self
        )
        switch result {
        case .success(_):
            delegate?.hideMessagesServiceSuccess(at: indexPath)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func fetchMessagesService() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: MessagesServiceNetworkTask.getMessages,
            responseType: MessagesResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.fetchMessagesSuccess(messages: response.messages)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func markMessageAsRead(for indexPath: IndexPath) async {
        try? await Task.sleep(nanoseconds: 500_000_000)
    }
}

