//
//  BaseSegmentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import UIKit

protocol BaseSegmentViewModel {
    associatedtype DataModel
    var numberOfItems: Int { get }
    func fetch(userId: String?) async -> DataModel?
    func updateData(with data: Any?)
    func cell(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionViewCell
    func size(_ collectionView: UICollectionView, at indexPath: IndexPath) -> CGSize
}
