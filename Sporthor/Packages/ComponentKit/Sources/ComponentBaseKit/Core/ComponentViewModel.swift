//
//  ComponentViewModel.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol ComponentViewModel {
    associatedtype Delegate
    associatedtype DataType: Decodable
    associatedtype DisplayerDelegate

    var isSelectable: Bool { get }
    var numberOfItems: Int { get }
    var insets: UIEdgeInsets { get }
    var defaultInsets: UIEdgeInsets { get }

    init(data: DataType, defaultInsets: UIEdgeInsets, delegate: Delegate?)

    func didSelectAction(at indexPath: IndexPath, delegate: DisplayerDelegate?)
}

public extension ComponentViewModel {

    var isSelectable: Bool { true }
    var numberOfItems: Int { 1 }
    var insets: UIEdgeInsets { defaultInsets }

    func didSelectAction(at indexPath: IndexPath, delegate: DisplayerDelegate?) {}
}
