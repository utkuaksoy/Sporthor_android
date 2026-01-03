//
//  ReusableView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import UIKit

public extension UICollectionView {

    // swiftlint:disable:next force_cast
    var flowLayout: UICollectionViewFlowLayout { collectionViewLayout as! UICollectionViewFlowLayout }

    static func createFlow() -> UICollectionView {
        return UICollectionView(frame: .zero, collectionViewLayout: UICollectionViewFlowLayout())
    }
}

public extension UICollectionView {
    func registerCellClass<T: ReusableView>(_ cellClass: T.Type) {
        self.register(T.self, forCellWithReuseIdentifier: T.reuseIdentifier)
    }

    func registerSectionHeaderClass<T: ReusableView>(_ cellClass: T.Type) {
        self.register(
            T.self,
            forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader,
            withReuseIdentifier: T.reuseIdentifier
        )
    }

    func registerSectionFooterClass<T: ReusableView>(_ cellClass: T.Type) {
        self.register(
            T.self,
            forSupplementaryViewOfKind: UICollectionView.elementKindSectionFooter,
            withReuseIdentifier: T.reuseIdentifier
        )
    }

    func registerCellClasses<T: ReusableView>(_ cellClasses: [T.Type]) {
        cellClasses.forEach { _ in
            let value = T.reuseIdentifier
            register(T.self, forCellWithReuseIdentifier: value)
        }
    }

    func dequeueCellClass<T: ReusableView>(for indexPath: IndexPath) -> T where T: UICollectionViewCell {
        // swiftlint:disable:next force_cast
        return dequeueReusableCell(withReuseIdentifier: T.reuseIdentifier, for: indexPath) as! T
    }

    func dequeueSectionHeaderClass<T: ReusableView>(for indexPath: IndexPath) -> T where T: UICollectionReusableView {
        return dequeueReusableSupplementaryClass(ofKind: UICollectionView.elementKindSectionHeader, for: indexPath)
    }

    func dequeueSectionFooterClass<T: ReusableView>(for indexPath: IndexPath) -> T where T: UICollectionReusableView {
        return dequeueReusableSupplementaryClass(ofKind: UICollectionView.elementKindSectionFooter, for: indexPath)
    }

    func dequeueReusableSupplementaryClass<T: ReusableView>(
        ofKind kind: String, for indexPath: IndexPath
    ) -> T where T: UICollectionReusableView {
        // swiftlint:disable:next force_cast
        return dequeueReusableSupplementaryView(
            ofKind: kind,
            withReuseIdentifier: T.reuseIdentifier,
            for: indexPath
        ) as! T
    }
}

public protocol ReusableView: UIView {
    static var reuseIdentifier: String { get }
}

public extension ReusableView {
    static var reuseIdentifier: String { String(describing: Self.self) }
}

public extension UICollectionViewLayoutAttributes {

    func dynamicHeight(for contentView: UIView) -> UICollectionViewLayoutAttributes {
        let targetSize = CGSize(width: self.frame.width, height: 0)
        self.frame.size.height = contentView.systemLayoutSizeFitting(
            targetSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel
        ).height
        return self
    }

    func dynamicWidth(for contentView: UIView) -> UICollectionViewLayoutAttributes {
        let targetSize = CGSize(width: .zero, height: self.frame.height)
        self.frame.size.width = contentView.systemLayoutSizeFitting(
            targetSize, withHorizontalFittingPriority: .fittingSizeLevel, verticalFittingPriority: .required
        ).width
        return self
    }
}

// MARK: - Animation

public extension UICollectionView {

    func setContentOffsetWithAnimation(_ contentOffset: CGPoint, with duration: CGFloat = 0.3) {
        let key = "contentOffsetAnimationDuration"
        let initialDuration = value(forKey: key)
        setValue(duration, forKey: key)
        setContentOffset(contentOffset, animated: true)
        setValue(initialDuration, forKey: key)
    }
}


public extension UICollectionReusableView {
    static var identifier: String { String(describing: Self.self) }
}
