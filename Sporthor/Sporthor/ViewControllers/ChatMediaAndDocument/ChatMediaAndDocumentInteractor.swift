//
//  ChatMediaAndDocumentInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import Factory
import Foundation
import NetworkKit

final class ChatMediaAndDocumentInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ChatMediaAndDocumentInteractorDelegate? {
        get { return self.baseDelegate as? ChatMediaAndDocumentInteractorDelegate }
        set { self.baseDelegate = newValue }
    }
    
    // MARK: - Private Properties
    @LazyInjected(\.networkManager) private var networkManager
    private let groupId: String
    
    
    
    // MARK: - Initialize
    init(groupId: String) {
        self.groupId = groupId
        super.init()
    }
}

// MARK: - ChatMediaAndDocumentInteractorProtocol
extension ChatMediaAndDocumentInteractor: ChatMediaAndDocumentInteractorProtocol {
    func fetchMediaAndDocumentService() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ChatMediaAndDocumentNetworkTask.getChatGroupAttachments(groupId: groupId),
            responseType: ChatMediaAndDocumentResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.fetchMediaAndDocumentsSuccess(response)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
} 
