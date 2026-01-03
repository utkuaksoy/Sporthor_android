//
//  CreatePostDetailInteractor.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//
//

import UIKit
import Factory

final class CreatePostDetailInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CreatePostDetailInteractorDelegate? {
        get {
            return self.baseDelegate as? CreatePostDetailInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CreatePostDetailInteractorProtocol
extension CreatePostDetailInteractor: CreatePostDetailInteractorProtocol {
    func uploadMultipleImages(_ images: [UIImage]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.uploadMultipleImage(images),
            responseType: [ImageUploadResponseModel].self
        )
        switch result {
        case .success(let response):
            delegate?.didUploadMultipleImages(filePaths: response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func uploadVideo(_ url: URL, index: Int) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: UploadService.videoUpload(videoURL: url),
            responseType: ImageUploadResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.didUploadVideo(filePath: response.filePath, index: index)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func createPost(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.createPost(request),
            responseType: CreatePostResponse.self
        )
        
        switch result {
        case .success(let response):
            delegate?.didCreatePost()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
