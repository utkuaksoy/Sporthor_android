//
//  HomeInteractor.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//
//

import Foundation
import NetworkKit
import Factory

final class HomeInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: HomeInteractorDelegate? {
        get {
            return self.baseDelegate as? HomeInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - HomeInteractorProtocol
extension HomeInteractor: HomeInteractorProtocol {
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
