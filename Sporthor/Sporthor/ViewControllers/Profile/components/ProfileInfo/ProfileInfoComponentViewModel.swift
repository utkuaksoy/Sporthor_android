//
//  ProfileInfoComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import ComponentBaseKit
import UIKit

public protocol ProfileInfoViewModelDelegate: AnyObject {
    
}

final class ProfileInfoComponentViewModel: CollectionComponentViewModel {
    typealias CellType = ProfileInfoCollectionViewCell
    private weak var delegate: Delegate?
    let data: ProfileInfoComponent.Data
    var defaultInsets: UIEdgeInsets
    
    init(
        data: ProfileInfoComponent.Data,
        defaultInsets: UIEdgeInsets,
        delegate: ProfileInfoViewModelDelegate?
    ) {
        self.data = data
        self.defaultInsets = defaultInsets
        self.delegate = delegate
    }
    
    var profileImage: String? {
        data.imageUrl
    }
    
    var profileName: String? {
        data.username
    }
    
    var postTitle: String {
        "Gönderi"
    }
    
    var postCount: Int {
        data.postCount ?? .zero
    }
    
    var followerTitle: String {
        "Takipçi"
    }
    
    var follewerCount: Int {
        data.followerCount ?? .zero
    }
    
    var followingTitle: String {
        "Takip"
    }
    
    var followingCount: Int {
        data.followingCount ?? .zero
    }
    
    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 72)
    }
}
