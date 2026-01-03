//
//  NextMatchesComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import UIKit

public protocol NextMatchesViewModelDelegate: AnyObject {
    
}

final class NextMatchesComponentViewModel: CollectionComponentViewModel {
    typealias CellType = NextMatchesCell
    let data: NextMatchesComponent.Data
    var defaultInsets: UIEdgeInsets
    
    var numberOfItemsMatches: Int {
        data.matches?.count ?? .zero
    }
    
    var title: String {
        data.title ?? "Sıradaki Maçlar"
    }
    
    init(
        data: NextMatchesComponent.Data,
        defaultInsets: UIEdgeInsets,
        delegate: ProfileAboutViewModelDelegate?
    ) {
        self.data = data
        self.defaultInsets = defaultInsets
    }
    
    func getItem(at indexPath: IndexPath) -> MatchModel? {
        data.matches?[safe: indexPath.row]
    }
    
    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 144)
    }
}
