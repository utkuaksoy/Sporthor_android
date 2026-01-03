//
//  SocialService.swift
//  Sporthor
//
//  Created by derTurke on 12.04.2025.
//

import NetworkKit

enum SocialService {
    case search(_ request: [String: Any])
    case addSearchHistory(_ request: [String: Any])
    case getSearchHistory
    case removeSearchHistory(_ request: [String: Any])
    case createPost(_ request: [String: Any])
    case getFeedAsync(_ request: [String: Any])
    case likePost(_ request: [String: Any])
    case unlikePost(_ request: [String: Any])
    case getStoryFeed
    case createStory(_ request: [String: Any])
    case getComments(_ request: [String: Any])
    case addComment(_ request: [String: Any])
    case watchedStory(_ request: [String: Any])
    case hidePost(_ request: [String: Any])
    case deletePost(_ request: [String: Any])
    case reportPost(_ request: [String: Any])
    case deleteStory(_ request: [String: Any])
    case getUserPostsAsync(_ request: [String: Any])
    case confirmationFollow(_ request: [String: Any])
    case getBlockUser
    case addBlockUser(_ request: [String: Any])
    case removeBlockUser(_ request: [String: Any])
}

extension SocialService: NetworkService {
    var path: String {
        switch self {
        case .search:
            return "/api/Social/Search"
        case .addSearchHistory:
            return "/api/Social/AddSearchHistory"
        case .getSearchHistory:
            return "/api/Social/GetSearchHistory"
        case .removeSearchHistory:
            return "/api/Social/RemoveSearchHistory"
        case .createPost:
            return "/api/Social/CreatePost"
        case .getFeedAsync:
            return "/api/Social/GetFeedAsync"
        case .likePost:
            return "/api/Social/LikePost"
        case .unlikePost:
            return "/api/Social/UnlikePost"
        case .getStoryFeed:
            return "/api/Social/GetStoryFeed"
        case .createStory:
            return "/api/Social/CreateStory"
        case .getComments:
            return "/api/Social/GetComments"
        case .addComment:
            return "/api/Social/AddComment"
        case .watchedStory:
            return "/api/Social/WatchedStory"
        case .hidePost:
            return "/api/Social/HidePost"
        case .deletePost:
            return "/api/Social/DeletePost"
        case .reportPost:
            return "/api/Social/ReportPost"
        case .deleteStory:
            return "/api/Social/DeleteStory"
        case .getUserPostsAsync:
            return "/api/Social/GetUserPostsAsync"
        case .confirmationFollow:
            return "/api/Social/ConfirmationFollow"
        case .getBlockUser:
            return "/api/Social/GetBlockUser"
        case .addBlockUser:
            return "/api/Social/AddBlockUser"
        case .removeBlockUser:
            return "/api/Social/RemoveBlockUser"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        switch self {
        case .addSearchHistory,
                .removeSearchHistory,
                .createPost,
                .likePost,
                .unlikePost,
                .createStory,
                .addComment,
                .watchedStory,
                .hidePost,
                .deletePost,
                .reportPost,
                .deleteStory,
                .confirmationFollow,
                .addBlockUser,
                .removeBlockUser:
            return .POST
        default:
            return .GET
        }
    }
    
    var parameters: [String : Any]? {
        switch self {
        case .search(let request):
            return request
        case .addSearchHistory(let request):
            return request
        case .removeSearchHistory(let request):
            return request
        case .createPost(let request):
            return request
        case .getFeedAsync(let request):
            return request
        case .likePost(let request):
            return request
        case .unlikePost(let request):
            return request
        case .createStory(let request):
            return request
        case .getComments(let request):
            return request
        case .addComment(let request):
            return request
        case .watchedStory(let request):
            return request
        case .hidePost(let request):
            return request
        case .deletePost(let request):
            return request
        case .reportPost(let request):
            return request
        case .deleteStory(let request):
            return request
        case .getUserPostsAsync(let request):
            return request
        case .confirmationFollow(let request):
            return request
        case .addBlockUser(let request):
            return request
        case .removeBlockUser(let request):
            return request
        default:
            return nil
        }
    }
    
    var token: String? {
        switch self {
        case .addSearchHistory,
                .getSearchHistory,
                .removeSearchHistory,
                .createPost,
                .getFeedAsync,
                .likePost,
                .unlikePost,
                .getStoryFeed,
                .createStory,
                .getComments,
                .addComment,
                .watchedStory,
                .hidePost,
                .deletePost,
                .reportPost,
                .deleteStory,
                .getUserPostsAsync,
                .confirmationFollow,
                .getBlockUser,
                .addBlockUser,
                .removeBlockUser,
                .search:
            return ApplicationContext.shared.authToken
        default:
            return nil
        }
    }
}
