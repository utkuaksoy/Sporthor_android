//
//  Homev2Interactor.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//
//

import Foundation
import Factory

final class Homev2Interactor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: Homev2InteractorDelegate? {
        get {
            return self.baseDelegate as? Homev2InteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - Homev2InteractorProtocol
extension Homev2Interactor: Homev2InteractorProtocol {
    func getInfo() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: AuthenticationService.info,
            responseType: UserInfo.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            ApplicationContext.shared.userInfo = response
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getPosts(page: Int, pageSize: Int) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.getFeedAsync(["page": page, "pageSize": pageSize]),
            responseType: PostResponse.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            delegate?.didGetPosts(response.posts ?? [])
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getStories() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.getStoryFeed,
            responseType: StoryResponse.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            delegate?.didGetStories(response.stories ?? [])
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func likePost(id: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.likePost(["postId": id]),
            responseType: LikeAndUnlikeResponse.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            delegate?.didLikeUnlikePost(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func unlikePost(id: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.unlikePost(["postId": id]),
            responseType: LikeAndUnlikeResponse.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            delegate?.didLikeUnlikePost(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getUserPosts(userId: String, page: Int, pageSize: Int) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.getUserPostsAsync([
                "UserId": userId,
                "Page": page,
                "PageSize": pageSize
            ]),
            responseType: PostResponse.self,
            showLoading: false
        )
        
        switch result {
        case .success(let response):
            delegate?.didGetPosts(response.posts ?? [])
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
