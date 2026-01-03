//
//  StorySettingInteractor.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//
//

import Foundation
import Factory

final class StorySettingInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: StorySettingInteractorDelegate? {
        get {
            return self.baseDelegate as? StorySettingInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - StorySettingInteractorProtocol
extension StorySettingInteractor: StorySettingInteractorProtocol {
    func deleteStory(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.deleteStory(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didDeleteStory()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
