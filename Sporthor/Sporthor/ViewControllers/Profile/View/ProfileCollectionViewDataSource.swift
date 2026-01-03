//
//  ProfileCollectionViewDataSource.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import UIKit

typealias ProfileDataSourceProtocols = ProfileInfoCellDelegate & 
    TeamsContainerCellDelegate & 
    ProfileActionButtonsCellDelegate & 
    PostImagesCellDelegate

final class ProfileCollectionViewDataSourceManager: NSObject {
    private(set) var viewModel: ProfileViewModel?
    private weak var dataSourceDelegate: ProfileDataSourceProtocols?
    
    func update(viewModel: ProfileViewModel) {
        self.viewModel = viewModel
    }
    
    func setDelegates(with delegate: ProfileDataSourceProtocols?) {
        dataSourceDelegate = delegate
    }
}

extension ProfileCollectionViewDataSourceManager: UICollectionViewDataSource {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        viewModel?.components.count ?? .zero
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        viewModel?.components[safe: section]?.numberOfItems ?? 1
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        viewModel?.components[safe: indexPath.section]?.cell(in: collectionView, at: indexPath, delegate: self) ?? UICollectionViewCell()
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        viewForSupplementaryElementOfKind kind: String,
        at indexPath: IndexPath
    ) -> UICollectionReusableView {
        return viewModel?.components[safe: indexPath.section]?.sectionHeader(in: collectionView, at: indexPath, delegate: self) ?? UICollectionReusableView()
    }
}

extension ProfileCollectionViewDataSourceManager: ProfileComponentsContracts.Components.DisplayerDelegates {    
    
    func didTapFollowerView() {
        dataSourceDelegate?.didTapFollowerView()
    }
    
    func didTapFollowingView() {
        dataSourceDelegate?.didTapFollowingView()
    }
    
    func didTapButton(ofType type: ProfileActionButtonType) {
        dataSourceDelegate?.didTapButton(ofType: type)
    }
    
    func didTapPostStatView() {
        dataSourceDelegate?.didTapPostStatView()
    }
    
    func didTapPost(selectedPostId: String) {
        dataSourceDelegate?.didTapPost(selectedPostId: selectedPostId)
    }
}
