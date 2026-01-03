//
//  ProfileAboutComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import UIKit

public protocol ProfileAboutViewModelDelegate: AnyObject {
    
}

final class ProfileAboutComponentViewModel: CollectionComponentViewModel {
    typealias CellType = ProfileAboutCell
    private weak var delegate: ProfileAboutViewModelDelegate?
    let data: ProfileAboutComponent.Data
    var defaultInsets: UIEdgeInsets
    
    init(
        data: ProfileAboutComponent.Data,
        defaultInsets: UIEdgeInsets,
        delegate: ProfileAboutViewModelDelegate?
    ) {
        self.data = data
        self.defaultInsets = defaultInsets
        self.delegate = delegate
    }
    
    var details: [KeyValueItem]? {
        data.details
    }
    
    var title: String? {
        data.title
    }
    
    var aboutDescription: String? {
        data.description
    }

    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return ProfileAboutSizeCalculator(
            width: collectionView.frame.size.width,
            title: title ?? "",
            profileAbout: aboutDescription,
            detailCount: details?.count ?? .zero
        ).componentSize
    }
}
