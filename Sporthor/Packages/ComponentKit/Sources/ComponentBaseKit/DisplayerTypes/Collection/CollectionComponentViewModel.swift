//
//  CollectionComponentViewModel.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol CollectionComponentViewModel: ComponentViewModel {
    associatedtype CellType: UICollectionViewCell & ReusableView & ComponentDisplayer

    var interitemSpacing: CGFloat { get }
    var lineSpacing: CGFloat { get }

    func size(_ collectionView: UICollectionView, at indexPath: IndexPath) -> CGSize
    func sectionHeaderSize(_ collectionView: UICollectionView, at section: Int) -> CGSize
    func sectionFooterSize(_ collectionView: UICollectionView, at section: Int) -> CGSize

    func cell(
        in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?
    ) -> UICollectionViewCell

    func sectionHeader(
        in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?
    ) -> UICollectionReusableView
    
    func sectionFooter(
        in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?
    ) -> UICollectionReusableView

    static func registerCells(
        to collectionView: UICollectionView,
        with delegate: Any
    )

    static func registerSectionHeaders(
        to collectionView: UICollectionView,
        with delegate: Any
    )

    static func registerSectionFooters(
        to collectionView: UICollectionView,
        with delegate: Any
    )

    func fetchLazyComponentData()
}

public extension CollectionComponentViewModel {

    var interitemSpacing: CGFloat { 10 }
    var lineSpacing: CGFloat { 10 }
}

public extension CollectionComponentViewModel where CellType: ComponentDisplayerViewModelConfigurable,
                                                    CellType.ViewModel == Self {
    typealias DisplayerDelegate = CellType.Delegate

    func cell(
        in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?
    ) -> UICollectionViewCell {
        let cell: CellType = collectionView.dequeueCellClass(for: indexPath)
        cell.configure(with: self, at: indexPath, delegate: delegate as? CellType.Delegate)
        return cell
    }

    func sectionHeader(
        in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?
    ) -> UICollectionReusableView {
        assertionFailure("You should implement sectionHeader function")
        let view: ComponentEmptyView = collectionView.dequeueSectionHeaderClass(for: indexPath)
        return view
    }

    func sectionFooter(
        in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?
    ) -> UICollectionReusableView {
        assertionFailure("You should implement sectionFooter function")
        let view: ComponentEmptyView = collectionView.dequeueSectionFooterClass(for: indexPath)
        return view
    }

    func sectionHeaderSize(_ collectionView: UICollectionView, at section: Int) -> CGSize { .zero }
    func sectionFooterSize(_ collectionView: UICollectionView, at section: Int) -> CGSize { .zero }
    func fetchLazyComponentData() {}
}

// MARK: - Registrations

public extension CollectionComponentViewModel where CellType: ComponentDisplayerViewModelConfigurable,
                                                    CellType.ViewModel == Self {

    static func registerCells(
        to collectionView: UICollectionView,
        with delegate: Any
    ) {
        register(CellType.self, to: collectionView, with: delegate)
    }

    static func register<T: ReusableView & ComponentDisplayerViewModelConfigurable>(
        _ cellType: T.Type,
        to collectionView: UICollectionView,
        with delegate: Any
    ) {
        throwErrorIfNeeded(cellType, with: delegate)
        collectionView.registerCellClass(cellType)
    }

    static func registerSectionHeaders(
        to collectionView: UICollectionView,
        with delegate: Any
    ) {
        registerSectionHeader(ComponentEmptyView.self, to: collectionView, with: delegate)
    }

    static func registerSectionHeader<T: ReusableView & ComponentDisplayerViewModelConfigurable>(
        _ reusableType: T.Type,
        to collectionView: UICollectionView,
        with delegate: Any
    ) {
        throwErrorIfNeeded(reusableType, with: delegate)
        collectionView.registerSectionHeaderClass(reusableType)
    }

    static func registerSectionFooters(
        to collectionView: UICollectionView,
        with delegate: Any
    ) {
        registerSectionFooter(ComponentEmptyView.self, to: collectionView, with: delegate)
    }

    static func registerSectionFooter<T: ReusableView & ComponentDisplayerViewModelConfigurable>(
        _ reusableType: T.Type,
        to collectionView: UICollectionView,
        with delegate: Any
    ) {
        throwErrorIfNeeded(reusableType, with: delegate)
        collectionView.registerSectionFooterClass(reusableType)
    }

    private static func throwErrorIfNeeded<T: ComponentDisplayerViewModelConfigurable>(
        _ cellType: T.Type,
        with delegate: Any
    ) {
        if (delegate is T.Delegate) == false {
            assertionFailure("\(self).DisplayerDelegates should confirm \(T.Delegate.self)")
        }
    }
}

private final class ComponentEmptyView:
    UICollectionReusableView, ReusableView, ComponentDisplayerViewModelConfigurable {

    func configure(with viewModel: Any, at indexPath: IndexPath, delegate: Any?) {}
}
