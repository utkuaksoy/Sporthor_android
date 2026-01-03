//
//  ComponentGroup+Collection.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol CollectionComponentGroup: ComponentGroup {

    static var collectionSources: [String: any CollectionComponent.Type] { get }
}

public extension CollectionComponentGroup {

    static func item(for kind: String, with decoder: Decoder) throws -> (any CollectionComponent)? {
        guard let contentType = collectionSources[kind] else { return nil }
        return try contentType.init(from: decoder)
    }

    static func registerAll(to collectionView: UICollectionView, delegate: DisplayerDelegates) {
        for (_, componentType) in collectionSources {
            register(componentType, to: collectionView, with: delegate)
        }
    }
}

private extension CollectionComponentGroup {

    static func register<T: CollectionComponent>(
        _ componentType: T.Type,
        to collectionView: UICollectionView,
        with delegate: DisplayerDelegates
    ) {
        componentType.ViewModel.registerCells(to: collectionView, with: delegate)
        componentType.ViewModel.registerSectionHeaders(to: collectionView, with: delegate)
        componentType.ViewModel.registerSectionFooters(to: collectionView, with: delegate)
    }
}
