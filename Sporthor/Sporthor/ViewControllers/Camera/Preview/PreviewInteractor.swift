//
//  PreviewInteractor.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import UIKit
import Factory

final class PreviewInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: PreviewInteractorDelegate? {
        get {
            return self.baseDelegate as? PreviewInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - PreviewInteractorProtocol
extension PreviewInteractor: PreviewInteractorProtocol {
    func uploadImage(_ image: UIImage) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.uploadImage(image: image),
            responseType: ImageUploadResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.didUpload(filePath: response.filePath)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func uploadVideo(_ url: URL) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.videoUpload(videoURL: url),
            responseType: ImageUploadResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.didUpload(filePath: response.filePath)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func createStory(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.createStory(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didCreateStory()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
