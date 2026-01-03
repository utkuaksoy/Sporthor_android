//
//  ProfileCollectionViewDelegate.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import UIKit

protocol ProfileCollectionViewDelegateProtocol: AnyObject {
    func shouldHideHeaderView(_ shouldHide: Bool)
}

final class ProfileCollectionViewDelegate: NSObject {
    
    private(set) var viewModel: ProfileViewModel?
    private weak var output: ProfileCollectionViewDelegateProtocol?
    private var isHeaderHidden: Bool = false
    
    init(output: ProfileCollectionViewDelegateProtocol) {
        self.output = output
    }
    
    func update(viewModel: ProfileViewModel) {
        self.viewModel = viewModel
    }
    
}

extension ProfileCollectionViewDelegate: UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        viewModel?.components[safe: indexPath.section]?.size(collectionView, at: indexPath) ?? .zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        return viewModel?.components[safe: section]?.sectionHeaderSize(collectionView, at: section) ?? .zero
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        insetForSectionAt section: Int
    ) -> UIEdgeInsets {
        viewModel?.components[safe: section]?.insets ?? .zero
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        minimumLineSpacingForSectionAt section: Int
    ) -> CGFloat {
        viewModel?.components[safe: section]?.lineSpacing ?? 8
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        minimumInteritemSpacingForSectionAt section: Int
    ) -> CGFloat {
        viewModel?.components[safe: section]?.interitemSpacing ?? 8
    }
    
    func scrollViewWillEndDragging(
        _ scrollView: UIScrollView,
        withVelocity velocity: CGPoint,
        targetContentOffset: UnsafeMutablePointer<CGPoint>
    ) {
        guard let isCurrentUser = viewModel?.profileInfo?.isCurrentUser, isCurrentUser else { return }
        let shouldHide = velocity.y > 0
        guard shouldHide != isHeaderHidden else { return }
        isHeaderHidden = shouldHide
        output?.shouldHideHeaderView(shouldHide)
    }
}
