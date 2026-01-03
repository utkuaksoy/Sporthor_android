//
//  EmptyViewComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.03.2025.
//

import ComponentBaseKit
import UIKit

public protocol EmptyViewViewModelDelegate: AnyObject {
    
}

final class EmptyViewComponentViewModel: CollectionComponentViewModel {
    typealias CellType = EmptyViewCell
    let data: EmptyViewComponent.Data
    var defaultInsets: UIEdgeInsets
    
    var title: String {
        data.title
    }
    
    var emptyImage: String {
        data.emptyImage
    }
    
    init(
        data: EmptyViewComponent.Data,
        defaultInsets: UIEdgeInsets,
        delegate: EmptyViewViewModelDelegate?
    ) {
        self.data = data
        self.defaultInsets = defaultInsets
    }
    
    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 180)
    }
}
