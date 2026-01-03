//
//  Extension+UICollectionView.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit

extension UICollectionViewCell {
    static func dequeueNib(from collectionView: UICollectionView, at indexPath: IndexPath) -> Self {
        let identifier = String(describing: self)
        collectionView.register(UINib(nibName: identifier, bundle: nil), forCellWithReuseIdentifier: identifier)
        
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: identifier, for: indexPath) as? Self else {
            fatalError("Cell with identifier \(identifier) not found")
        }
        
        return cell
    }
    
    static func dequeue(from collectionView: UICollectionView, at indexPath: IndexPath) -> Self {
        let identifier = String(describing: self)
        collectionView.register(Self.self, forCellWithReuseIdentifier: identifier)
        
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: identifier, for: indexPath) as? Self else {
            fatalError("Cell with identifier \(identifier) not found")
        }
        
        return cell
    }
}

extension UICollectionView {
    
    func registerEmptyCell() {
        self.register(UICollectionViewCell.self, forCellWithReuseIdentifier: "EmptyCollectionViewCell")
    }
    
    func dequeueEmptyReusableCell(with indexPath: IndexPath) -> UICollectionViewCell {
        return self.dequeueReusableCell(withReuseIdentifier: "EmptyCollectionViewCell", for: indexPath)
    }
    
    /// Register empty header view to collectionView
    func registerEmptyHeaderReusableView() {
        self.register(UICollectionReusableView.self, forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: "EmptyHeaderReusableView")
    }
    
    func dequeueEmptyHeaderReusableView(with indexPath: IndexPath) -> UICollectionReusableView {
        return self.dequeueReusableSupplementaryView(
            ofKind: UICollectionView.elementKindSectionHeader,
            withReuseIdentifier: "EmptyHeaderReusableView",
            for: indexPath
        )
    }
}

extension UICollectionReusableView {
    static func dequeueReusableSupplementaryView(from collectionView: UICollectionView, ofKind kind: String, at indexPath: IndexPath) -> Self {
        let identifier = String(describing: self)
        collectionView.register(Self.self, forSupplementaryViewOfKind: kind, withReuseIdentifier: identifier)
        
        guard let view = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: identifier, for: indexPath) as? Self else {
            fatalError("Supplementary view with identifier \(identifier) not found")
        }
        return view
    }
}
