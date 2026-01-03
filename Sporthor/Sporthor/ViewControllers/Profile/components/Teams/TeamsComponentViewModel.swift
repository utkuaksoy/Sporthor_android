//
//  TeamsComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentBaseKit
import UIKit

public protocol TeamsViewModelDelegate: AnyObject {
    
}

final class TeamsComponentViewModel: CollectionComponentViewModel {
    typealias CellType = TeamsContainerCell
    let data: TeamsComponent.Data
    var defaultInsets: UIEdgeInsets
    
    var teams: [TeamModel]? {
        data.teams
    }
    
    var teamsCount: Int {
        teams?.count ?? .zero
    }
    
    init(
        data: TeamsComponent.Data,
        defaultInsets: UIEdgeInsets,
        delegate: TeamsViewModelDelegate?
    ) {
        self.data = data
        self.defaultInsets = defaultInsets
    }
    
    func getItem(at indexPath: IndexPath) -> TeamModel? {
        let team = teams?[indexPath.row]
        return team
    }
    
    func size(
        _ collectionView: UICollectionView,
        at indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 40)
    }
}
