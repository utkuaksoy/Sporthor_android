//
//  PostsViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import Factory
import NetworkKit
import UIKit

final class PostsViewModel: BaseSegmentViewModel {
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager) private var networkManager
    
    var posts: [Post] = []

    var numberOfItems: Int {
        return posts.count
    }
    
    func fetch(userId: String?) async -> [Post]? {
        guard let networkManager, let userId else { return nil }
        let result = await networkManager.request(
            service: ProfileService.getUserPostAsync(["userId": userId, "page": 1, "pageSize":20]),
            responseType: PostResponse.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            self.posts = response.posts ?? []
            return response.posts
        case .failure(let error):
            return nil
        }
    }

    func updateData(with data: Any?) {
        if let posts = data as? [Post] {
            self.posts = posts
        }
    }

    func cell(
        in collectionView: UICollectionView,
        at indexPath: IndexPath,
        delegate: AnyObject?
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: PostImagesCell.reuseIdentifier,
            for: indexPath
        ) as? PostImagesCell,
              let medias = posts[safe: indexPath.row]?.media,
              let media = medias.first
        else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
        cell.configurePost(
            delegate: delegate,
            with: media.url,
            isMultipleImage: medias.count > 1,
            isVideo: media.type != 0,
            postId: posts[safe: indexPath.row]?.id
        )
        return cell
    }

    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return CGSize(
            width: (
                collectionView.frame.width - 6
            ) / 3 ,
            height: 161
        )
    }
}
