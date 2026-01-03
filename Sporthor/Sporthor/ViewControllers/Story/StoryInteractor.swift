//
//  StoryInteractor.swift
//  Sporthor
//
//  Created by derTurke on 28.04.2025.
//
//

import Foundation
import Factory

final class StoryInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: StoryInteractorDelegate? {
        get {
            return self.baseDelegate as? StoryInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - StoryInteractorProtocol
extension StoryInteractor: StoryInteractorProtocol {
    func watchedStory(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.watchedStory(request),
            responseType: NullResponse.self,
            showLoading: false
        )
        switch result {
        case .success(_):
            break
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
