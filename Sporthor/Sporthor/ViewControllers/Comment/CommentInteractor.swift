//
//  CommentInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//
//

import Foundation
import Factory

final class CommentInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CommentInteractorDelegate? {
        get {
            return self.baseDelegate as? CommentInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CommentInteractorProtocol
extension CommentInteractor: CommentInteractorProtocol {
    func getComments(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.getComments(request),
            responseType: CommentResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didGetComments(response.comments)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addComment(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.addComment(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didAddComment()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
